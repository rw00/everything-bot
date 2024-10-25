package com.rw.bots.everything.bot.impl.middaywalk.helper;

import com.rw.bots.everything.bot.impl.middaywalk.entity.WeatherNowSummary;
import com.rw.bots.everything.bot.impl.middaywalk.helper.model.ShowerRadarModel;
import com.rw.bots.everything.bot.impl.middaywalk.helper.model.ShowerRadarModel.RainResponse;
import com.rw.bots.everything.util.DateTimeUtils;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ShowerRadarApi {
    private static final java.util.Map<String, ShowerRadarModel.Location> CITY_LOCATIONS =
            java.util.Map.of("Utrecht", new ShowerRadarModel.Location("52.09", "5.12"), //
                    "Amsterdam", new ShowerRadarModel.Location("52.37", "4.89"), //
                    "Rotterdam", new ShowerRadarModel.Location("51.92", "4.48"));

    private final RestTemplate restTemplate;

    public ShowerRadarApi() {
        this.restTemplate = new RestTemplateBuilder()
                .defaultHeader("Referer", "https://www.buienradar.nl/").build();
    }

    public WeatherNowSummary weatherNowSummary(String city) {
        ShowerRadarModel.Location location = CITY_LOCATIONS.get(city);
        String lat = location.lat();
        String lon = location.lon();

        // 1. Get rain forecast (next 2 hours)
        String rainApiUrl = String.format(
                "https://graphdata.buienradar.nl/3.0/forecast/geo/RainHistoryForecast?lat=%s&lon=%s&language=nl",
                lat, lon);
        RainResponse rainResponse = restTemplate.getForObject(rainApiUrl, RainResponse.class);

        double maxRain = 0;
        if (rainResponse != null && rainResponse.forecasts() != null) {
            LocalDateTime now = LocalDateTime.now(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId());
            LocalDateTime twoHoursLater = now.plusHours(2);

            maxRain = rainResponse.forecasts().stream().filter(f -> {
                LocalDateTime dt = LocalDateTime.parse(f.dateTime());
                return !dt.isBefore(now) && dt.isBefore(twoHoursLater);
            }).mapToDouble(ShowerRadarModel.Forecast::dataValue).max().orElse(0);
        }

        // 2. Get current temperature and description
        String feedApiUrl = "https://api.buienradar.nl/data/public/2.0/jsonfeed";
        ShowerRadarModel.FeedResponse feedResponse =
                restTemplate.getForObject(feedApiUrl, ShowerRadarModel.FeedResponse.class);

        double temperature = 0;
        String state = "Unknown";
        if (feedResponse != null && feedResponse.actual() != null
                && feedResponse.actual().stationMeasurements() != null) {
            // Find station matching regio
            var station = feedResponse.actual().stationMeasurements().stream()
                    .filter(s -> city.equalsIgnoreCase(s.region())).findFirst();

            if (station.isPresent()) {
                temperature = station.get().temperature();
                state = station.get().weatherDescription();
            } else {
                var first = feedResponse.actual().stationMeasurements().getFirst();
                temperature = first.temperature();
                state = first.weatherDescription();
            }
        }

        return new WeatherNowSummary(maxRain, temperature, state);
    }
}
