package com.mohistmc.cron;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Runs a synchronization task at most once at a time, always releasing the in-progress flag so
 * a failing run does not disable the schedule until the next restart.
 */
@Slf4j
public abstract class AbstractSyncCron {
    private final AtomicBoolean synchronizing = new AtomicBoolean(false);

    protected abstract String taskName();

    protected abstract void doSynchronize();

    protected void runOnce() {
        if (!synchronizing.compareAndSet(false, true)) {
            log.info("{} synchronization is already in progress.", taskName());
            return;
        }

        try {
            log.info("Synchronizing {}...", taskName());
            doSynchronize();
            log.info("{} synchronization completed.", taskName());
        } catch (Exception e) {
            log.error("{} synchronization failed: {}", taskName(), e.getMessage(), e);
        } finally {
            synchronizing.set(false);
        }
    }
}
