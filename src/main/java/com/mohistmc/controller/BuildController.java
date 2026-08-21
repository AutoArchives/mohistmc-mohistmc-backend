package com.mohistmc.controller;

import com.mohistmc.dto.response.BuildResponseDto;
import com.mohistmc.entity.Build;
import com.mohistmc.mapper.BuildMapper;
import com.mohistmc.service.BuildDownloadStatsService;
import com.mohistmc.service.BuildService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project/{projectName}/{versionName}")
@RequiredArgsConstructor
public class BuildController {
    private final BuildService buildService;
    private final BuildDownloadStatsService buildDownloadStatsService;

    private final BuildMapper buildMapper;

    @GetMapping("/builds")
    public List<BuildResponseDto> getProjectVersions(@PathVariable String projectName,
                                                     @PathVariable String versionName) {
        return buildService.getBuildsByProjectAndVersion(projectName, versionName)
                .stream()
                .sorted((b1, b2) -> b2.getBuiltOn().compareTo(b1.getBuiltOn()))
                .map(buildMapper::toDto)
                .toList();
    }

    @GetMapping("/builds/{buildId}")
    public BuildResponseDto getBuild(@PathVariable String projectName,
                                     @PathVariable String versionName,
                                     @PathVariable Integer buildId) {
        return buildMapper.toDto(buildService.getBuildById(buildId));
    }

    @GetMapping("/builds/latest")
    public BuildResponseDto getLatestBuild(@PathVariable String projectName,
                                           @PathVariable String versionName) {
        return buildMapper.toDto(buildService.getLatestBuild(projectName, versionName));
    }

    @GetMapping("/builds/{buildId}/download")
    public ResponseEntity<Resource> downloadBuild(@PathVariable String projectName,
                                                  @PathVariable String versionName,
                                                  @PathVariable Integer buildId,
                                                  HttpServletRequest request) throws IOException {
        return downloadResponse(buildService.getBuildById(buildId), request);
    }

    @GetMapping("/builds/latest/download")
    public ResponseEntity<Resource> downloadLatestBuild(@PathVariable String projectName,
                                                        @PathVariable String versionName,
                                                        HttpServletRequest request) throws IOException {
        return downloadResponse(buildService.getLatestBuild(projectName, versionName), request);
    }

    private ResponseEntity<Resource> downloadResponse(Build build, HttpServletRequest request) throws IOException {
        Resource buildFile = buildService.getBuildFileStream(build);

        buildDownloadStatsService.saveBuildDownload(build, request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(build.getFileName()).build().toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(buildFile.contentLength())
                .body(buildFile);
    }
}
