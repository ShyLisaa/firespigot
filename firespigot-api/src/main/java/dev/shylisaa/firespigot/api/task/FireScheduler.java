package dev.shylisaa.firespigot.api.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FireScheduler {

    private final ScheduledExecutorService scheduledExecutorService;

    public FireScheduler() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
    }

    public void runTask(Runnable runnable) {
        this.scheduledExecutorService.execute(runnable);
    }

    public void runTaskLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        this.scheduledExecutorService.schedule(runnable, delay, timeUnit);
    }

    public void runTaskTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        this.scheduledExecutorService.scheduleAtFixedRate(runnable, delay, period, timeUnit);
    }

    public void shutdown() {
        this.scheduledExecutorService.shutdown();
    }


}
