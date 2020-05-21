package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mysql.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    public boolean existsByIdAndRepository(String branchId, com.indra.repos.git.model.domain.mysql.Repository repository);

    @Query(value = "SELECT * FROM tb_branches ORDER BY  branch_index DESC LIMIT 1", nativeQuery = true)
    public Branch obterBranchtOrderByIndexDescLimitUm();

}
