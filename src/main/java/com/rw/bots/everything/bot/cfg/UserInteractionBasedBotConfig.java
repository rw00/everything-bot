package com.rw.bots.everything.bot.cfg;

import lombok.Getter;

@Getter
public class UserInteractionBasedBotConfig extends BotConfig {
    private final String botUsername;

    private UserInteractionBasedBotConfig(UserInteractionBasedBotConfigBuilder builder) {
        super(builder);
        this.botUsername = builder.botUsername;
    }

    @Getter
    public static class UserInteractionBasedBotConfigBuilder extends BotConfigBuilder<UserInteractionBasedBotConfig, UserInteractionBasedBotConfigBuilder> {
        private String botUsername;

        public UserInteractionBasedBotConfigBuilder(String botName) {
            super(botName);
        }

        public UserInteractionBasedBotConfigBuilder botUsername(String botUsername) {
            this.botUsername = botUsername;
            return this;
        }

        @Override
        public UserInteractionBasedBotConfig build() {
            return new UserInteractionBasedBotConfig(this);
        }
    }
}
