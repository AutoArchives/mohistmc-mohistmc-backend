package com.mohistmc.repository;

import com.mohistmc.entity.Build;
import com.mohistmc.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuildRepository extends JpaRepository<Build, Integer> {
    @Query("SELECT b.workflowRunId FROM Build b WHERE b.projectVersion.id = ?1")
    List<Long> findAllWorkflowRunsIds(Integer projectVersionId);

    List<Build> findAllByArtifactDownloadedIsFalseAndActiveIsTrue();

    @EntityGraph(attributePaths = {"projectVersion", "projectVersion.project", "loaderVersion", "gitInfo"})
    Optional<Build> findWithDetailsById(Integer id);

    @Query("""
            SELECT b FROM Build b
            JOIN FETCH b.projectVersion pv
            JOIN FETCH pv.project p
            LEFT JOIN FETCH b.loaderVersion
            WHERE b.active = true AND b.artifactDownloaded = true AND pv.active = true AND p.active = true
            AND p.name = ?1 AND pv.versionName = ?2
            """)
    List<Build> findAllByProjectAndVersion(String projectName, String versionName);

    @EntityGraph(attributePaths = {"projectVersion", "projectVersion.project", "loaderVersion", "gitInfo"})
    List<Build> findAllByLoaderVersionNullAndProjectVersion_Project(Project projectVersionProject);
}
