package com.indra.repos.git.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryMongoRepository extends MongoRepository<com.indra.repos.git.model.domain.Repository, Integer> {

}
