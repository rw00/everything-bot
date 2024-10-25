package com.rw.bots.everything.bot.impl.middaywalk.helper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ShowerRadarModel {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record RainResponse(List<Forecast> forecasts, String description) {
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Forecast(String dateTime, double dataValue) {
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public record FeedResponse(Actual actual) {
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Actual(
                        @JsonProperty("stationmeasurements") List<StationMeasurement> stationMeasurements) {
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public record StationMeasurement(double temperature,
                        @JsonProperty("weatherdescription") String weatherDescription,
                        @JsonProperty("regio") String region) {
        }

        public record Location(String lat, String lon) {
        }
}
