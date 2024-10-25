package com.rw.bots.everything.bot.impl.middaywalk;

import com.rw.bots.everything.Constants;
import com.rw.bots.everything.bot.AbstractChannelCronBasedBot;
import com.rw.bots.everything.bot.BotBuilder;
import com.rw.bots.everything.bot.impl.middaywalk.entity.WeatherNowSummary;
import com.rw.bots.everything.bot.impl.middaywalk.helper.AiMessageGenerator;
import com.rw.bots.everything.bot.impl.middaywalk.helper.ShowerRadarApi;
import com.rw.bots.everything.util.DateTimeUtils;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class MiddayWalkReminderBot extends AbstractChannelCronBasedBot {
    private static final long MINIMUM_TEMPERATURE = 5;
    private static final long WARM_TEMPERATURE = 12;
    private final ShowerRadarApi showerRadarApi;
    private final AiMessageGenerator messageGenerator;
    private final String city;
    private LocalDate lastSentDate;

    public MiddayWalkReminderBot(BotBuilder botBuilder, ShowerRadarApi showerRadarApi,
            AiMessageGenerator messageGenerator,
            @Value("${bot.telegram.MIDDAYWALKREMINDERBOT.token}") String token,
            @Value("${bot.telegram.MIDDAYWALKREMINDERBOT.channel-chat-id}") String channelId,
            @Value("${MIDDAYWALKREMINDERBOT.city}") String city) {
        super(botBuilder.channelBasedBot("MiddayWalkReminderBot").botToken(token)
                .channelId(channelId).cron("0 25 12 * * *"));
        this.showerRadarApi = showerRadarApi;
        this.messageGenerator = messageGenerator;
        this.city = city;
        this.lastSentDate = LocalDate.now(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId());
    }

    @Override
    protected String doExecute() throws Exception {
        LocalDate today = LocalDate.now(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId());

        long daysSinceLastSent = ChronoUnit.DAYS.between(lastSentDate, today);
        long currentThresholdTemp =
                Math.max(MINIMUM_TEMPERATURE, WARM_TEMPERATURE - daysSinceLastSent);

        WeatherNowSummary weatherNowSummary = showerRadarApi.weatherNowSummary(city);
        if (weatherNowSummary.precipitationMM() == 0
                && weatherNowSummary.temperatureC() >= currentThresholdTemp) {
            String message = messageGenerator.generateMessage(weatherNowSummary);
            if (message == null) {
                message = String.format(
                        "The weather is great! It's %.0f°C and %s. Wanna go for a walk?",
                        weatherNowSummary.temperatureC(), weatherNowSummary.state());
            }
            this.lastSentDate = today;
            return message;
        }
        return null;
    }
}
