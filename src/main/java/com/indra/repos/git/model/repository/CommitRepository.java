package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Branch;
import com.indra.repos.git.model.domain.mysql.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    public boolean existsByIdAndBranch(String commitId, Branch branch);

    @Query(value = "SELECT * FROM tb_commits ORDER BY  commit_index DESC LIMIT 1", nativeQuery = true)
    public Commit obterCommitOrderByIndexDescLimitUm();

}
