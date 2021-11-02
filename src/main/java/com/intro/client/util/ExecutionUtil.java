package com.intro.client.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutionUtil {

    private static final ScheduledExecutorService OSMIUM_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);

    public static void submitScheduledTask(Runnable runnable, long delay, TimeUnit timeUnit) {
        OSMIUM_EXECUTOR_SERVICE.schedule(runnable, delay, timeUnit);
    }

    public static void submitTask(Runnable runnable) {
        OSMIUM_EXECUTOR_SERVICE.schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }

}
