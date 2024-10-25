package com.rw.bots.everything.bot.common;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotSender {
    private final DefaultAbsSender botSender;

    public TelegramBotSender(String botToken) {
        botSender = new DefaultAbsSender(new DefaultBotOptions(), botToken) {
        };
    }

    public void sendText(String chatId, String messageText) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        botSender.execute(message);
    }
}
