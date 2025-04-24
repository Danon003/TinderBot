package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "myFirstBotTinder_bot";
    public static final String TELEGRAM_BOT_TOKEN = "7766262772:AAG7aieH7MYhCaqUU8IOOQWqBu-3KsOhoe4";
    public static final String OPEN_AI_TOKEN = "gpt:GMEpATyDssZWFB4H2wdsJFkblB3TtbsOsfleLvmgKt8qL2qt";
    private static int model;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);

    }
    private ChatGPTService chatgpt = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;
    private ArrayList<String> list = new ArrayList<>();
    private UserInfo me = new UserInfo();
    private UserInfo interlocutor = new UserInfo(); //собеседник
    private int questionCount;
    private int question;




    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();


        if(message.equals("/start")){
            currentMode = DialogMode.PROFILE;
            question = 1;
            sendTextMessage("Введите Имя");
            return;
        }
        if(currentMode == DialogMode.PROFILE && !isMessageCommand()){
            switch (question){
                case 1:
                    me.setName(message);
                    question = 2;
                    sendTextButtonsMessage("Выберите ваш пол:", "Мужчина", "male", "Женщина", "female");
                    break;
                case 2:
                    String callbackData = getCallbackQueryButtonKey();
                    if (!callbackData.isEmpty()) {
                        if ("male".equals(callbackData)) {
                            me.setSex("Мужчина");
                            interlocutor.setSex("Женщина");
                            model = 0; //модель поведения, если собеседник женщина
                        } else if ("female".equals(callbackData)) {
                            me.setSex("Женщина");
                            interlocutor.setSex("Мужчина");
                            model = 1;
                        } else {
                            sendTextMessage("Некорректный выбор пола. Попробуйте снова.");
                            return;
                        }

                        sendTextMessage("Вы выбрали: " + me.getSex());
                        message = "/menu";

                    }

                    break;
            }

        }

        if(message.equals("/setting")){
            currentMode = DialogMode.SETTING;
            sendTextButtonsMessage("Ваши данные: \nИмя - " + me.getName() + ",\n пол - " + me.getSex() + " \nИзменить?",
                    "да", "да",
                    "нет", "нет");

            return;
        }
        if(!isMessageCommand() && currentMode == DialogMode.SETTING) {
            if (getCallbackQueryButtonKey().equals("да")) {
                currentMode = DialogMode.PROFILE;
                question = 1;
                sendTextMessage("Введите Имя");
                return;
            }
            else if(getCallbackQueryButtonKey().equals("нет")){
                currentMode = DialogMode.MAIN;
                String text;
                text = loadMessage("main");
                sendTextMessage(text);
                showMainMenu(
                        "Регистрация", "/start",
                        "Меню", "/menu",
                        "Сгенерировать Tinder-профиль\uD83D\uDE0E", "/profile",
                        "Сгенерировать сообщение для знакомства\uD83E\uDD70", "/opener ",
                        "Начать переписку от моего имени\uD83D\uDE08", "/message",
                        "Начать переписку со звездами\uD83D\uDD25", "/date",
                        "Начать общение с ChatGPT\uD83E\uDDE0", "/gpt",
                        "Изменить мои данные", "/setting");
                return;
            }
            return;
        }

        if(message.equals("/menu")) {
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String text;
            text = loadMessage("main");
            sendTextMessage(text);
            showMainMenu(
                    "Регистрация", "/start",
                        "Меню", "/menu",
                        "Сгенерировать Tinder-профиль\uD83D\uDE0E", "/profile",
                        "Сгененрировать сообщение для знакомства\uD83E\uDD70", "/opener ",
                        "Начать переписку от моего имени\uD83D\uDE08", "/message",
                        "Начать переписку со звездами\uD83D\uDD25", "/date",
                        "Начать общение с ChatGPT\uD83E\uDDE0", "/gpt",
                        "Изменить мои данные", "/setting");
            return;
        }


        if(message.equals("/gpt")){
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }
        if(currentMode == DialogMode.GPT && !isMessageCommand()){
            String prompt = loadPrompt("gpt");
            Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
            String answer = chatgpt.sendMessage(prompt, message);
            updateTextMessage(msg, answer);

            return;
        }

        /**
         * Здесь работает функция DATE, которая позволяет тренироваться приглашать на свидание собеседника по переписке.
         * Здесь учитывается выбранный при регистрации @sex (пол) и в качестве собеседника
         * выбирается человек противоположного пола
         * */

        if(message.equals("/date")){
            currentMode = DialogMode.DATE;
            sendPhotoMessage("date");
            String text;
            switch(model){
                case 0:
                    text = loadMessage("dateForMan");
                    sendTextButtonsMessage(text,
                            "Ариана Гранде", "date_grande",
                            "Марго Робби", "date_robbie",
                            "Зендея", "date_zendaya");
                    break;
                case 1:
                    text = loadMessage("dateForWoman");
                    sendTextButtonsMessage(text,
                            "Райан Гослинг", "date_gosling",
                            "Том Харди", "date_hardy");
                    break;
            }
            return;

        }

        if(currentMode == DialogMode.DATE && !message.equals("/message")&& !isMessageCommand()){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("date_")){
                sendPhotoMessage(query);
                sendTextMessage(" Отличный выбор!\nТвоя задача пригласить собеседника на свидание за 5 сообщений.");

                String prompt = loadPrompt(query);
                chatgpt.setPrompt(prompt);
                return;
            }
            Message msg = sendTextMessage("Подождите, собеседник набирает текст...");
            String answer = chatgpt.addMessage( message);
            updateTextMessage(msg, answer);
            return;
        }

        //command Message
        if(message.equals("/message")){
            currentMode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            String text = loadMessage("message");
            sendTextButtonsMessage(text,
            "Следующее сообщение" , "message_next",
            "Пригласить на свидание", "message_date");

            return;
        }

        if(currentMode == DialogMode.MESSAGE && !isMessageCommand()){
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("message_")) {
                String prompt = loadPrompt(query );
                String userChatHistory = String.join("\n\n", list);

                Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
                String answer = chatgpt.sendMessage(prompt + me.getSex(), userChatHistory);
                updateTextMessage(msg, answer);
                return;
            }
            list.add(message);
            return;
        }

        //command PROFILE
        if(message.equals("/profile")){

            sendPhotoMessage("profile");
            String text = loadMessage("profile");
            sendTextMessage(text);

            questionCount = 1;
            sendTextMessage("Сколько вам лет?");
            return;
        }
        if(currentMode == DialogMode.PROFILE && !isMessageCommand()){
            switch(questionCount){
                case 1:
                me.age = message;
                questionCount = 2;
                sendTextMessage("Кем вы работаете?");
                break;

                case 2:
                me.occupation = message;
                questionCount = 3;
                sendTextMessage("какое у вас хобби?");
                break;

                case 3:
                me.hobby = message;
                questionCount = 4;
                sendTextMessage("Что вас раздражает в людях?");
                break;

                case 4:
                    me.annoys = message;
                    questionCount = 5;
                    sendTextMessage("Цель знакомства?");
                    break;
                case 5:
                    me.goals = message;
                    String aboutMyself = me.toString();

                    Message msg = sendTextMessage("Подождите пару секунд, ChatGPT \uD83E\uDDE0 думает...");
                    String answer = chatgpt.sendMessage(loadPrompt("profile") + me.getSex(), aboutMyself);
                    updateTextMessage(msg, answer);
                    break;
            }
        }

        //command OPENER
        if(message.equals("/opener")){
            currentMode = DialogMode.OPENER;
            sendPhotoMessage("opener");
            sendTextMessage(loadMessage("opener"));
            questionCount = 1;
            sendTextMessage("Имя собеседника");
            return;
        }

        if(currentMode == DialogMode.OPENER && !isMessageCommand()){
            String pr = "";
            switch(model){
                case 0:
                    pr = loadPrompt("openerForMan");
                    break;
                case 1:
                    pr = loadPrompt("openerForWoman");
                    break;
            }

            switch (questionCount){
                case 1:
                    interlocutor.setName(message);
                    questionCount = 2;
                    sendTextMessage("Сколько ему лет?");
                    break;
                case 2:
                    interlocutor.age = message;
                    questionCount = 3;
                    sendTextMessage("Какое у него хобби?");
                    break;
                case 3:
                    interlocutor.hobby = message;
                    questionCount = 4;
                    sendTextMessage("Кем он работает?");
                    break;
                case 4:
                    interlocutor.occupation = message;
                    questionCount = 5;
                    sendTextMessage("Цель знакомства?");
                    break;
                case 5:
                    interlocutor.goals = message;
                    String aboutFriend = interlocutor.toString();


                    Message msg = sendTextMessage("Подождите пару секунд, ChatGPT \uD83E\uDDE0 думает...");

                    String answer = chatgpt.sendMessage(pr, aboutFriend);

                    updateTextMessage(msg, answer);
                    break;
            }

        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());

    }
}

