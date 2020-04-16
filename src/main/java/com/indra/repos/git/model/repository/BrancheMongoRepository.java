package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.Branche;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrancheMongoRepository extends MongoRepository<Branche, String> {

}
