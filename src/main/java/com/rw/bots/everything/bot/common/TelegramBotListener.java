package com.rw.bots.everything.bot.common;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotListener extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    @Setter
    private Consumer<Update> updateConsumer;

    public TelegramBotListener(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateConsumer.accept(update);
    }
}
