package com.indra.repos.svn.model.repository;

import com.indra.repos.svn.model.domain.SVNRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SVNReposRepository extends JpaRepository<SVNRepositories, Long> {


}
