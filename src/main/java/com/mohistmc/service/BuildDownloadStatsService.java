package com.mohistmc.service;

import com.mohistmc.entity.Build;
import com.mohistmc.entity.BuildDownloadStat;
import com.mohistmc.repository.BuildDownloadStatsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuildDownloadStatsService {
    // Longest possible IPv6 representation.
    private static final int MAX_IP_LENGTH = 45;
    private static final int MAX_USER_AGENT_LENGTH = 512;

    private final BuildDownloadStatsRepository buildDownloadStatsRepository;

    public void saveBuildDownload(Build build, HttpServletRequest request) {
        BuildDownloadStat buildDownloadStat = new BuildDownloadStat()
                .setIp(truncate(resolveClientIp(request), MAX_IP_LENGTH))
                .setUserAgent(truncate(request.getHeader("User-Agent"), MAX_USER_AGENT_LENGTH))
                .setBuild(build);

        buildDownloadStatsRepository.save(buildDownloadStat);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor == null || forwardedFor.isBlank()) {
            return request.getRemoteAddr();
        }
        return forwardedFor.split(",")[0].trim();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
