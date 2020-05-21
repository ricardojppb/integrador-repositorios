package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    public boolean existsById(Integer id);

    @Query(value = "SELECT * FROM tb_projects ORDER BY project_index DESC LIMIT 1", nativeQuery = true)
    public Project obterProjectOrderByIndexDescLimitUm();

}
