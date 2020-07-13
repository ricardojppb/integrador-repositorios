package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    public boolean existsByIdAndRepository(String branchId, com.indra.repos.git.model.domain.mysql.Repository repository);

    @Query(value = "SELECT MAX(branch_index) FROM tbgit_branches WHERE repository_fk = :repositoryFK", nativeQuery = true)
    public Integer obterIndexMaxBranchWhereRepository(@org.springframework.data.repository.query.Param("repositoryFK") Long repositoryFK);

}
