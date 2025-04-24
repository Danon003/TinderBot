package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


public class DateStrategy implements BotStrategy {
    @Override
    public void execute(Update update, TinderBoltApp bot) {
        String query = bot.getCallbackQueryButtonKey();
        if (query.startsWith("date_")) {
            bot.sendPhotoMessage(query);
            bot.sendTextMessage("Отличный выбор!\nТвоя задача пригласить собеседника на свидание за 5 сообщений.");
            String prompt = MultiSessionTelegramBot.loadPrompt(query);
            bot.getChatGPT().setPrompt(prompt);
            return;
        }
        Message msg = bot.sendTextMessage("Подождите, собеседник набирает текст...");
        String answer = bot.getChatGPT().addMessage(bot.getMessageText());
        bot.updateTextMessage(msg, answer);
    }

}