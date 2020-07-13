package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    public boolean existsById(Integer id);

    @Query(value = "SELECT max(project_index) FROM tbgit_projects ", nativeQuery = true)
    public Integer obterIndexMaxProject();

}
