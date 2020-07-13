package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Project;
import com.indra.repos.git.model.domain.mysql.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReposRepository extends JpaRepository<Repository, Long> {

    public boolean existsByIdAndProject(Integer repositoryId, Project project);

    @Query(value = "SELECT max(repository_index) FROM tbgit_repositories WHERE project_fk = :projectFK", nativeQuery = true)
    public Integer obterIndexMaxRepositoryWhereProject(@org.springframework.data.repository.query.Param("projectFK") Long projectFK);
}
