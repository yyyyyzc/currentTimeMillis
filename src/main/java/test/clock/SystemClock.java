package test.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on 2020-11-04
 *
 * @author yangzc
 **/
public class SystemClock {

    private final AtomicLong now;
    private static ScheduledExecutorService scheduler;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (scheduler != null) {
                    scheduler.shutdownNow();
                }
            }
        });
    }

    private SystemClock() {
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "SystemClockScheduled");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), 1, 1, TimeUnit.MILLISECONDS);
    }

    public static long now() {
        return getInstance().now.get();
    }

    private enum SystemClockEnum {
        SYSTEM_CLOCK;
        private final SystemClock systemClock;

        SystemClockEnum() {
            systemClock = new SystemClock();
        }

        public SystemClock getInstance() {
            return systemClock;
        }
    }

    private static SystemClock getInstance() {
        return SystemClockEnum.SYSTEM_CLOCK.getInstance();
    }

}