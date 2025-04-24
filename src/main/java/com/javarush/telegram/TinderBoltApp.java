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
import java.util.HashMap;
import java.util.Map;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "myFirstBotTinder_bot";
    public static final String TELEGRAM_BOT_TOKEN = "7766262772:AAG7aieH7MYhCaqUU8IOOQWqBu-3KsOhoe4";
    public static final String OPEN_AI_TOKEN = "gpt:GMEpATyDssZWFB4H2wdsJFkblB3TtbsOsfleLvmgKt8qL2qt";
    private static int model;
    private Map<DialogMode, BotStrategy> strategies;


    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);

        // Инициализация стратегий
        strategies = new HashMap<>();
        strategies.put(DialogMode.PROFILE, new ProfileStrategy());
        strategies.put(DialogMode.OPENER, new OpenerStrategy());
        strategies.put(DialogMode.MESSAGE, new MessageStrategy());
        strategies.put(DialogMode.DATE, new DateStrategy());
        strategies.put(DialogMode.GPT, new GPTStrategy());
        strategies.put(DialogMode.SETTING, new SettingStrategy());
    }

    private ChatGPTService chatgpt = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;
    private ArrayList<String> list = new ArrayList<>();
    private UserInfo me = new UserInfo();
    private UserInfo interlocutor = new UserInfo(); //собеседник
    private int questionCount;
    private int question;



    @Override
    public void onUpdateEventReceived(Update update) throws TelegramApiException {
        String message = getMessageText();

        if(message.equals("/setting")) {
            currentMode = DialogMode.SETTING;
            if (getCurrentUser().getName() == null || getCurrentUser().getSex() == null) {
                sendTextMessage("Похоже, вы еще не зарегистрированы. Давайте начнем!");
                SettingStrategy.setSettingCount(1);
                sendTextMessage("Введите Имя");
            }
            else{
                SettingStrategy.setSettingCount(3);
                sendTextButtonsMessage(
                        "Ваши данные:\n" +
                                "Имя - " + getCurrentUser().getName() + "\n" +
                                "Пол - " + getCurrentUser().getSex() + "\n" +
                                "Изменить?",
                        "Да", "y",
                        "Нет", "n"
                );
            }
            return;
        }

        if (message.equals("/date")) {
            currentMode = DialogMode.DATE;
            sendPhotoMessage("date");
            String text;
            switch (getModel()) {
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

        if (message.equals("/profile")) {
            currentMode = DialogMode.PROFILE;
            questionCount = 1;
            sendPhotoMessage("profile");
            sendTextMessage(loadMessage("profile"));
            sendTextMessage("Введите Имя");
            return;
        }


        if (message.equals("/message")) {
            currentMode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            sendTextMessage(loadMessage("message"));
            sendTextButtonsMessage(
                    "Выберите действие:",
                    "Следующее сообщение", "message_next",
                    "Пригласить на свидание", "message_date"
            );
            return;
        }

        if (message.equals("/opener")) {
            currentMode = DialogMode.OPENER;
            questionCount = 1;
            sendPhotoMessage("opener");
            sendTextMessage(loadMessage("opener"));
            sendTextMessage("Имя собеседника");
            return;
        }

        if (message.equals("/gpt")) {
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            sendTextMessage(loadMessage("gpt"));
            return;
        }
        if (message.equals("/menu")) {
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String text = loadMessage("main");
            sendTextMessage(text);
            showMainMenu();
            return;
        }

        if (currentMode != null && strategies.containsKey(currentMode)) {
            strategies.get(currentMode).execute(update, this);
        } else if (!isMessageCommand()) {
            sendTextMessage("Неизвестная команда.");
        }
    }

    public ChatGPTService getChatGPT() {
        return chatgpt;
    }

    public UserInfo getCurrentUser() {
        return me;
    }

    public UserInfo getInterlocutor() {
        return interlocutor;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int count) {
        this.questionCount = count;
    }

    public void setCurrentMode(DialogMode mode) {
        this.currentMode = mode;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public int getModel() {
        return model;
    }
    public void setModel(int model) {
        TinderBoltApp.model = model;
    }
    public void showMainMenu(){
        showMainMenu(
                "Меню", "/menu",
                "Сгенерировать Tinder-профиль\uD83D\uDE0E", "/profile",
                "Сгенерировать сообщение для знакомства\uD83E\uDD70", "/opener",
                "Начать переписку от моего имени\uD83D\uDE08", "/message",
                "Начать переписку со звездами\uD83D\uDD25", "/date",
                "Начать общение с ChatGPT\uD83E\uDDE0", "/gpt",
                "Изменить мои данные\uD83D\uDD27", "/setting"
        );
    }


    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
