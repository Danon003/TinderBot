package com.javarush.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public class SettingStrategy implements BotStrategy {
    private static int settingCount = 0;
    @Override
    public void execute(Update update, TinderBoltApp bot) {
        String message = bot.getMessageText();
        String callbackData = bot.getCallbackQueryButtonKey();

        switch (settingCount) {
            case 1:
                bot.getCurrentUser().setName(message);
                setSettingCount(2);
                bot.sendTextButtonsMessage("Выберите ваш пол:", "Мужчина", "male", "Женщина", "female");
                break;

            case 2:
                if ("male".equals(callbackData)) {
                    bot.getCurrentUser().setSex("Мужчина");
                    bot.getInterlocutor().setSex("Женщина");
                    bot.setModel(0); // Модель поведения, если собеседник женщина
                } else if ("female".equals(callbackData)) {
                    bot.getCurrentUser().setSex("Женщина");
                    bot.getInterlocutor().setSex("Мужчина");
                    bot.setModel(1);
                } else {
                    bot.sendTextMessage("Некорректный выбор пола. Попробуйте снова.");
                    return;
                }

                bot.sendTextMessage("Вы выбрали: " + bot.getCurrentUser().getSex());
                bot.sendTextMessage("Регистрация завершена!");

                bot.setCurrentMode(DialogMode.MAIN);
                bot.sendTextMessage(MultiSessionTelegramBot.loadMessage("main"));
                bot.showMainMenu();
                setSettingCount(0);
                break;

            case 3:
                if (callbackData.equals("y")) {
                    setSettingCount(1);
                    bot.sendTextMessage("Введите Имя");
                } else if (callbackData.equals("n")) {
                    bot.setCurrentMode(DialogMode.MAIN);
                    bot.sendTextMessage(MultiSessionTelegramBot.loadMessage("main"));
                    bot.showMainMenu();
                    setSettingCount(0);
                }
                break;
        }
    }
    public static void setSettingCount(int s) {
        settingCount = s;
    }
}

