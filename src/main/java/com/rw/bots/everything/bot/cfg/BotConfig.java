package com.rw.bots.everything.bot.cfg;

import lombok.Getter;

@Getter
public class BotConfig {
    private final String botName;
    private final String botToken;

    protected BotConfig(BotConfigBuilder<?, ? extends BotConfigBuilder<?, ?>> builder) {
        this.botName = builder.botName;
        this.botToken = builder.botToken;
    }

    @Getter
    public abstract static class BotConfigBuilder<T extends BotConfig, S extends BotConfigBuilder<T, S>> {
        private final String botName;
        private String botToken;

        protected BotConfigBuilder(String botName) {
            this.botName = botName;
        }

        protected S self() {
            return (S) this;
        }

        public S botToken(String botToken) {
            this.botToken = botToken;
            return self();
        }

        public abstract T build();
    }
}
