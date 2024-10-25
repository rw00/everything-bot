package com.rw.bots.everything.bot.common;

import java.util.concurrent.Callable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SilentOperator {
    public <T> T call(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception ignore) {
            return null;
        }
    }
}
