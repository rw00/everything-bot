package com.rw.bots.everything.bot;

import com.rw.bots.everything.util.Operator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class UsersScheduledFutures implements DisposableBean {
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledForUsers = new ConcurrentHashMap<>();

    public void set(String username, ScheduledFuture<?> future) {
        remove(username);
        scheduledForUsers.put(username, future);
    }

    public void remove(String username) {
        var future = scheduledForUsers.remove(username);
        if (future != null) {
            stop(future);
        }
    }

    @Override
    public void destroy() {
        scheduledForUsers.forEach((u, f) -> stop(f));
        scheduledForUsers.clear();
    }

    private void stop(ScheduledFuture<?> future) {
        Operator.doSilently(() -> future.cancel(true));
    }
}
