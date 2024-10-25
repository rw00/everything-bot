package com.rw.bots.everything.util;

import java.util.concurrent.Callable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Operator {
    public void doSilently(Callable<?> callable) {
        try {
            callable.call();
        } catch (Exception ignore) {
        }
    }
}
