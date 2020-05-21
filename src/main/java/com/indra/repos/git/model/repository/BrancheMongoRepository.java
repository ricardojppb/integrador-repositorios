package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mongo.Branch;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrancheMongoRepository extends MongoRepository<Branch, ObjectId> {

    public boolean existsByIdAndRepository(String branchId, com.indra.repos.git.model.domain.mongo.Repository repository);
}
