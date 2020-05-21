package com.indra.repos.svn.model.repository;

import com.indra.repos.svn.model.domain.SVNLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SVNLogRepository extends JpaRepository<SVNLog, Long> {

}
