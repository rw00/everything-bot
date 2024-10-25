package com.rw.bots.everything;

import com.rw.bots.everything.bot.impl.middaywalk.entity.WeatherNowSummary;
import com.rw.bots.everything.bot.impl.middaywalk.helper.AiMessageGenerator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles(Constants.LOCAL_TEST_PROFILE)
class EverythingBotApplicationTests {
    @Autowired
    private AiMessageGenerator aiMessageGenerator;

    @Test
    void contextLoads() {
    }

    @org.junit.jupiter.api.Disabled
    @Test
    void testAiMessageGenerator() throws Exception {
        String message = aiMessageGenerator.generateMessage(new WeatherNowSummary(0, 12, "sunny"));
        assertFalse(StringUtils.isBlank(message));
    }
}
