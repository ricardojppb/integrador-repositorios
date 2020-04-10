package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.dto.Commit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitMongoRepository extends MongoRepository<Commit, String> {
    Optional<Commit> findById(String id);
}
