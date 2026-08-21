package com.mohistmc.cron;

import com.mohistmc.service.util.GitHubArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BuildSyncCron extends AbstractSyncCron {
    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private final GitHubArtifactService githubArtifactService;

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = TEN_MINUTES)
    public void synchronizeBuilds() {
        runOnce();
    }

    @Override
    protected String taskName() {
        return "Builds";
    }

    @Override
    protected void doSynchronize() {
        githubArtifactService.synchronize();
    }
}
