package com.rw.bots.everything.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {
    public final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Europe/Amsterdam");

    public Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
