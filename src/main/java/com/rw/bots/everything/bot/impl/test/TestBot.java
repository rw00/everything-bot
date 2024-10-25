package com.rw.bots.everything.bot.impl.test;

import com.rw.bots.everything.Constants;
import com.rw.bots.everything.bot.AbstractUserInteractionBasedBot;
import com.rw.bots.everything.bot.BotBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class TestBot extends AbstractUserInteractionBasedBot {
    public TestBot(BotBuilder botBuilder,
                   @Value("${bot.telegram.test.token}") String token,
                   @Value("${bot.telegram.test.username}") String botUsername) {
        super(botBuilder.userInteractionBasedBot("TestBot")
                        .botToken(token)
                        .botUsername(botUsername));
    }

    @Override
    protected void initBot() {
        super.initBot();
        commandFunctions.put("test", msgCtx -> "Test success");
    }
}
