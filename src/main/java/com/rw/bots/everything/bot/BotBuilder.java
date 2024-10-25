package com.rw.bots.everything.bot;

import com.rw.bots.everything.bot.cfg.ChannelBasedBotConfig.ChannelBasedBotConfigBuilder;
import com.rw.bots.everything.bot.cfg.UserInteractionBasedBotConfig.UserInteractionBasedBotConfigBuilder;
import org.springframework.stereotype.Component;

@Component
public class BotBuilder {

    public ChannelBasedBotConfigBuilder channelBasedBot(String name) {
        return new ChannelBasedBotConfigBuilder(name);
    }

    public UserInteractionBasedBotConfigBuilder userInteractionBasedBot(String name) {
        return new UserInteractionBasedBotConfigBuilder(name);
    }
}
