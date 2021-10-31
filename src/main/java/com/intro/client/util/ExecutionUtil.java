package com.intro.client.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutionUtil {

    private static final ScheduledExecutorService removeClicksExecutor = Executors.newScheduledThreadPool(1);

    public static void submitScheduledTask(Runnable runnable, long delay, TimeUnit timeUnit) {
        removeClicksExecutor.schedule(runnable, delay, timeUnit);
    }

}
