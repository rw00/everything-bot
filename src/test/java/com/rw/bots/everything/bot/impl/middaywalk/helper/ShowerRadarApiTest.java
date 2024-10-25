package com.rw.bots.everything.bot.impl.middaywalk.helper;

import com.rw.bots.everything.bot.impl.middaywalk.entity.WeatherNowSummary;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class ShowerRadarApiTest {

    @Disabled
    @Test
    void test() {
        ShowerRadarApi showerRadarApi = new ShowerRadarApi();
        WeatherNowSummary weatherNowSummary = showerRadarApi.weatherNowSummary("Amsterdam");
        log.info(weatherNowSummary.toString());
    }
}
