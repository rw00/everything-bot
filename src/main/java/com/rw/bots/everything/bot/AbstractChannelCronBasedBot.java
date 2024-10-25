package com.rw.bots.everything.bot;

import com.rw.bots.everything.bot.cfg.ChannelBasedBotConfig;
import com.rw.bots.everything.bot.cfg.ChannelBasedBotConfig.ChannelBasedBotConfigBuilder;
import com.rw.bots.everything.bot.common.TelegramBotSender;
import com.rw.bots.everything.util.DateTimeUtils;
import java.util.concurrent.ScheduledFuture;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Slf4j
public abstract class AbstractChannelCronBasedBot extends AbstractSelfRegisteringBot<ChannelBasedBotConfig, ChannelBasedBotConfigBuilder> implements Runnable {
    private final TelegramBotSender botSender;
    @Setter(onMethod_ = @Autowired)
    private TaskScheduler taskScheduler;

    protected AbstractChannelCronBasedBot(ChannelBasedBotConfigBuilder builder) {
        super(builder);

        this.botSender = new TelegramBotSender(builder.getBotToken());
    }

    @Override
    public void run() {
        try {
            String message = doExecute();
            if (message != null) {
                botSender.sendText(getBotConfig().getChannelId(), message);
            }
        } catch (Exception e) {
            log.warn("Error occurred while running bot '{}'", getBotName(), e);
        }
    }

    @Override
    protected void initBot() {
    }

    @Override
    protected AutoCloseable startBot() {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(
                this,
                new CronTrigger(getBotConfig().getCron(), DateTimeUtils.DEFAULT_TIMEZONE)
        );
        return new ScheduledFutureCloseable(scheduledFuture);
    }

    protected String doExecute() throws Exception {
        return null;
    }

    private static class ScheduledFutureCloseable implements AutoCloseable {
        private final ScheduledFuture<?> scheduledFuture;

        public ScheduledFutureCloseable(ScheduledFuture<?> scheduledFuture) {
            this.scheduledFuture = scheduledFuture;
        }

        @Override
        public void close() {
            scheduledFuture.cancel(true);
        }
    }
}
