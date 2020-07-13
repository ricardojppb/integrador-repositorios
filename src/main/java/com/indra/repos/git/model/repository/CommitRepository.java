package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Branch;
import com.indra.repos.git.model.domain.mysql.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    public boolean existsByIdAndBranch(String commitId, Branch branch);

    @Query(value = "SELECT max(commit_index) FROM tbgit_commits WHERE branche_fk = :branchFK", nativeQuery = true)
    public Integer obterIndexMaxCommitWhereBranch(@org.springframework.data.repository.query.Param("branchFK") Long branchFK);

}
