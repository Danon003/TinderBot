package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OpenerStrategy implements BotStrategy {
    @Override
    public void execute(Update update, TinderBoltApp bot) {
        String pr = "";
        switch (bot.getModel()) {
            case 0:
                pr = MultiSessionTelegramBot.loadPrompt("openerForMan");
                break;
            case 1:
                pr = MultiSessionTelegramBot.loadPrompt("openerForWoman");
                break;
        }
        switch (bot.getQuestionCount()) {
            case 1:
                bot.getInterlocutor().setName(bot.getMessageText());
                bot.setQuestionCount(2);
                bot.sendTextMessage("Сколько ему лет?");
                break;
            case 2:
                bot.getInterlocutor().setAge(bot.getMessageText());
                bot.setQuestionCount(3);
                bot.sendTextMessage("Какое у него хобби?");
                break;
            case 3:
                bot.getInterlocutor().setHobby(bot.getMessageText());
                bot.setQuestionCount(4);
                bot.sendTextMessage("Кем он работает?");
                break;
            case 4:
                bot.getInterlocutor().setOccupation(bot.getMessageText());
                bot.setQuestionCount(5);
                bot.sendTextMessage("Цель знакомства?");
                break;
            case 5:
                bot.getInterlocutor().setGoals(bot.getMessageText());
                String aboutFriend = bot.getInterlocutor().toString();
                Message msg = bot.sendTextMessage("Подождите пару секунд, ChatGPT думает...");
                String answer = bot.getChatGPT().sendMessage(pr, aboutFriend);
                bot.updateTextMessage(msg, answer);
                break;
        }
    }
}