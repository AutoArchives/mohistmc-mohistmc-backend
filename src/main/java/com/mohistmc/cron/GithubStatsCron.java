package com.mohistmc.cron;

import com.mohistmc.service.stats.GithubStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GithubStatsCron extends AbstractSyncCron {
    private static final int MINUTES_30 = 30 * 60 * 1000;
    private final GithubStatsService githubStatsService;

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = MINUTES_30)
    public void synchronizeGithubStats() {
        runOnce();
    }

    @Override
    protected String taskName() {
        return "Github stats";
    }

    @Override
    protected void doSynchronize() {
        githubStatsService.synchronize();
    }
}
