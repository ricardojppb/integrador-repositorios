package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Project;
import com.indra.repos.git.model.domain.mysql.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReposRepository extends JpaRepository<Repository, Long> {

    public boolean existsByIdAndProject(Integer repositoryId, Project project);

    @Query(value = "SELECT * FROM tb_repositories ORDER BY repository_index DESC LIMIT 1", nativeQuery = true)
    public Repository obterRepositorytOrderByIndexDescLimitUm();
}
