package com.mohistmc.cron;

import com.mohistmc.service.stats.BStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BStatsCron extends AbstractSyncCron {
    private static final int MINUTES_30 = 30 * 60 * 1000;
    private final BStatsService bStatsService;

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = MINUTES_30)
    public void synchronizeBStats() {
        runOnce();
    }

    @Override
    protected String taskName() {
        return "Bstats";
    }

    @Override
    protected void doSynchronize() {
        bStatsService.synchronize();
    }
}
