package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.Commit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitMongoRepository extends MongoRepository<Commit, String> {

}
