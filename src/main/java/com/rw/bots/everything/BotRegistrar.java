package com.rw.bots.everything;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BotRegistrar implements DisposableBean {
    private final List<AutoCloseable> closeables = new ArrayList<>();

    public void register(AutoCloseable closeable) {
        closeables.add(closeable);
    }

    @Override
    public void destroy() {
        for (AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("Error stopping bot", e);
            }
        }
    }
}
