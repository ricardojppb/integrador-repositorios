package com.indra.repos.git.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryMongoRepository extends MongoRepository<com.indra.repos.git.model.domain.Repository, String> {
    Optional<com.indra.repos.git.model.domain.Repository> findById(String id);
}
