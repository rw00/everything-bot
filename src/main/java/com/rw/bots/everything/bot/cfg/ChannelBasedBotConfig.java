package com.rw.bots.everything.bot.cfg;

import lombok.Getter;

@Getter
public class ChannelBasedBotConfig extends BotConfig {
    private final String channelId;
    private final String cron;

    private ChannelBasedBotConfig(ChannelBasedBotConfigBuilder builder) {
        super(builder);
        this.channelId = builder.channelId;
        this.cron = builder.cron;
    }

    @Getter
    public static class ChannelBasedBotConfigBuilder extends BotConfigBuilder<ChannelBasedBotConfig, ChannelBasedBotConfigBuilder> {
        private String channelId;
        private String cron;

        public ChannelBasedBotConfigBuilder(String botName) {
            super(botName);
        }

        public ChannelBasedBotConfigBuilder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public ChannelBasedBotConfigBuilder cron(String cron) {
            this.cron = cron;
            return this;
        }

        @Override
        public ChannelBasedBotConfig build() {
            return new ChannelBasedBotConfig(this);
        }
    }
}
