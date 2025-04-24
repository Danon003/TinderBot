package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GPTStrategy implements BotStrategy {
    @Override
    public void execute(Update update, TinderBoltApp bot) {
        String prompt = MultiSessionTelegramBot.loadPrompt("gpt");
        Message msg = bot.sendTextMessage("Подождите пару секунд - ChatGPT думает...");
        String answer = bot.getChatGPT().sendMessage(prompt, bot.getMessageText());
        bot.updateTextMessage(msg, answer);
    }

}