package com.indra.repos.git.model.repository;

public interface CommitChangesRepository extends org.springframework.data.jpa.repository.JpaRepository<com.indra.repos.git.model.domain.mysql.CommitChange, Long> {

    public boolean existsByContentIdAndCommit(String changeId, com.indra.repos.git.model.domain.mysql.Commit commit);

    @org.springframework.data.jpa.repository.Query(value = "SELECT max(change_index) FROM tbgit_changes WHERE commit_fk = :commitFK", nativeQuery = true)
    public Integer obterIndexMaxChangeWhereCommit(@org.springframework.data.repository.query.Param("commitFK") Long commitFK);

}
