package com.intro.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutionUtil {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    private static final ExecutorService OSMIUM_DYNAMIC_EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void submitScheduledTask(Runnable runnable, long delay, TimeUnit timeUnit) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(runnable, delay, timeUnit);
    }

    public static void submitTask(Runnable runnable) {
        OSMIUM_DYNAMIC_EXECUTOR_SERVICE.execute(runnable);
    }

}
