package com.mohistmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.kohsuke.github.GHArtifact;

import java.text.MessageFormat;
import java.time.Instant;

@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "build")
public class Build {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_version_id")
    private ProjectVersion projectVersion;

    @Column(name = "file_sha256", length = Integer.MAX_VALUE)
    private String fileSha256;

    @ColumnDefault("true")
    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "built_on")
    private Instant builtOn = Instant.now();

    @Column(name = "artifact_id")
    private Long artifactId;

    @Column(name = "workflow_run_id")
    private Long workflowRunId;

    @Column(name = "artifact_downloaded")
    private Boolean artifactDownloaded = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "git_info")
    private GitCommit gitInfo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "loader_version_id")
    private LoaderVersion loaderVersion;

    private transient GHArtifact ghArtifact;

    public String getFileName() {
        return MessageFormat.format("{0}-{1}-{2}-server.jar", projectVersion.getProject().getName(), projectVersion.getVersionName(), gitInfo.getGitSha().substring(0, 8));
    }
}