package com.rw.bots.everything.bot;

import com.rw.bots.everything.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
class TelegramBotConfig {

    @Bean
    TelegramBotsApi telegramBotsRegistrar() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
