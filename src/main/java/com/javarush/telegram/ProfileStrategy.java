package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ProfileStrategy implements BotStrategy {
    @Override
    public void execute(Update update, TinderBoltApp bot) {
        String message = bot.getMessageText();
        switch (bot.getQuestionCount()) {
            case 1:
                bot.getCurrentUser().setName(message);
                bot.sendTextMessage("Сколько вам лет?");
                bot.setQuestionCount(2);
                break;
            case 2:
                bot.getCurrentUser().setAge(message);
                bot.setQuestionCount(3);
                bot.sendTextMessage("Кем вы работаете?");
                break;
            case 3:
                bot.getCurrentUser().setOccupation(message);
                bot.setQuestionCount(4);
                bot.sendTextMessage("Какое у вас хобби?");
                break;
            case 4:
                bot.getCurrentUser().setHobby(message);
                bot.setQuestionCount(5);
                bot.sendTextMessage("Что вас раздражает в людях?");
                break;
            case 5:
                bot.getCurrentUser().setAnnoys(message);
                bot.setQuestionCount(6);
                bot.sendTextMessage("Цель знакомства?");
                break;
            case 6:
                bot.getCurrentUser().setGoals(message);
                String aboutMyself = bot.getCurrentUser().toString();
                Message msg = bot.sendTextMessage("Подождите пару секунд, ChatGPT думает...");
                String answer = bot.getChatGPT().sendMessage(MultiSessionTelegramBot.loadPrompt("profile") + bot.getCurrentUser().getSex(), aboutMyself);
                bot.updateTextMessage(msg, answer);
                break;
        }
    }
}