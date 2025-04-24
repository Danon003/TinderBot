package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotStrategy {
    void execute(Update update, TinderBoltApp bot) throws TelegramApiException;
}
