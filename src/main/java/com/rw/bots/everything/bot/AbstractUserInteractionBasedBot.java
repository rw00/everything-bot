package com.rw.bots.everything.bot;

import com.rw.bots.everything.bot.cfg.UserInteractionBasedBotConfig;
import com.rw.bots.everything.bot.cfg.UserInteractionBasedBotConfig.UserInteractionBasedBotConfigBuilder;
import com.rw.bots.everything.bot.common.MessageContext;
import com.rw.bots.everything.bot.common.TelegramBotListener;
import com.rw.bots.everything.bot.common.TelegramBotSender;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

@Slf4j
public abstract class AbstractUserInteractionBasedBot extends
        AbstractSelfRegisteringBot<UserInteractionBasedBotConfig, UserInteractionBasedBotConfigBuilder> {
    private static final Pattern COMMAND_MESSAGE_PATTERN =
            Pattern.compile("^/([a-zA-Z0-9-]+)(\\s(.+))?$");
    protected final Map<String, Function<MessageContext, String>> commandFunctions;
    private final TelegramBotListener botListener;
    private final TelegramBotSender botSender;
    @Setter(onMethod_ = @Autowired)
    private StringRedisTemplate stringRedisTemplate;
    @Setter(onMethod_ = @Autowired)
    private TelegramBotsApi telegramBotsListenerRegistrar;
    @Setter(onMethod_ = @Autowired)
    private List<AbstractSelfRegisteringBot<?, ?>> otherBots;

    protected AbstractUserInteractionBasedBot(UserInteractionBasedBotConfigBuilder builder) {
        super(builder);

        this.commandFunctions = new HashMap<>();
        this.botListener = new TelegramBotListener(builder.getBotToken(), builder.getBotUsername());
        this.botSender = new TelegramBotSender(builder.getBotToken());
    }

    @Override
    protected void initBot() {
        botListener.setUpdateConsumer(update -> {
            Optional<Message> message = Optional.of(update).map(Update::getMessage);
            String text = message.map(Message::getText).orElse("null");
            Matcher matcher = COMMAND_MESSAGE_PATTERN.matcher(text);
            if (matcher.matches()) {
                String command = matcher.group(1);
                String argument = matcher.group(3);

                handleMessage(command, argument, message.orElseThrow());
            }
        });

        commandFunctions.put("start", msgCtx -> {
            String key = msgCtx.fromUsername() + "_active";
            String exists = stringRedisTemplate.opsForValue().get(key);
            if (Boolean.parseBoolean(exists)) {
                return "You are already active.";
            } else {
                stringRedisTemplate.opsForValue().set(key, "true");
                return "You are now active!";
            }
        });
        commandFunctions.put("stop", msgCtx -> {
            String key = msgCtx.fromUsername() + "_active";
            stringRedisTemplate.opsForValue().set(key, "false");
            return "You are no longer active!";
        });
        commandFunctions.put("trigger", msgCtx -> {
            otherBots.stream().filter(Runnable.class::isInstance).map(Runnable.class::cast)
                    .forEach(Runnable::run);
            return "Triggered!";
        });
        commandFunctions.put("echo", msgCtx -> "Echo: " + msgCtx.text());
    }

    @Override
    protected AutoCloseable startBot() throws TelegramApiException {
        BotSession botSession = telegramBotsListenerRegistrar.registerBot(botListener);
        return new BotSessionCloseable(botSession);
    }

    protected void reply(MessageContext messageContext, String reply) {
        if (reply != null) {
            try {
                botSender.sendText(messageContext.chatId(), reply);
            } catch (TelegramApiException e) {
                log.warn("Could not send message", e);
            }
        }
    }

    private void handleMessage(String command, String argument, Message message) {
        Function<MessageContext, String> function = commandFunctions.get(command.toLowerCase());
        if (function != null) {
            MessageContext messageContext = new MessageContext(String.valueOf(message.getChatId()),
                    message.getFrom().getUserName(), argument);
            String reply = function.apply(messageContext);
            reply(messageContext, reply);
        }
    }

    private static class BotSessionCloseable implements AutoCloseable {
        private final BotSession botSession;

        public BotSessionCloseable(BotSession botSession) {
            this.botSession = botSession;
        }

        @Override
        public void close() {
            botSession.stop();
        }
    }
}
