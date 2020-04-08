package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.dto.Branche;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrancheMongoRepository extends MongoRepository<Branche, String> {
    Optional<Branche> findById(String id);
}
