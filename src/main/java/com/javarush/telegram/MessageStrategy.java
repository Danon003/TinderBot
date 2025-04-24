package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageStrategy implements BotStrategy {
    @Override
    public void execute(Update update, TinderBoltApp bot) throws TelegramApiException {
        String query = bot.getCallbackQueryButtonKey();
        if (query.startsWith("message_")) {
            String prompt = bot.loadPrompt(query);
            String userChatHistory = String.join("\n", bot.getList());
            Message msg = bot.sendTextMessage("Подождите пару секунд - ChatGPT думает...");
            String answer = bot.getChatGPT().sendMessage(prompt + bot.getCurrentUser().getSex(), userChatHistory);
            bot.updateTextMessage(msg, answer);
            return;
        }
        bot.getList().add(bot.getMessageText());
    }
}