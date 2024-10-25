package com.rw.bots.everything.bot;

import com.rw.bots.everything.Bot;
import com.rw.bots.everything.BotRegistrar;
import com.rw.bots.everything.bot.cfg.BotConfig;
import com.rw.bots.everything.bot.cfg.BotConfig.BotConfigBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSelfRegisteringBot<T extends BotConfig, B extends BotConfigBuilder<T, B>> implements Bot {
    @Getter
    private final T botConfig;
    @Setter(onMethod_ = @Autowired)
    private BotRegistrar botRegistrar;

    protected AbstractSelfRegisteringBot(B botConfigBuilder) {
        this.botConfig = botConfigBuilder.build();
    }

    @Override
    public final String getBotName() {
        return botConfig.getBotName();
    }

    @PostConstruct
    void register() {
        initBot();

        try {
            AutoCloseable botHandle = startBot();
            botRegistrar.register(botHandle);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to start bot '%s'", getBotName()), e);
        }
    }

    protected abstract void initBot();

    protected abstract AutoCloseable startBot() throws Exception;
}
