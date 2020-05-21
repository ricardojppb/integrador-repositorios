package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mongo.Branch;
import com.indra.repos.git.model.domain.mongo.Commit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitMongoRepository extends MongoRepository<Commit, ObjectId> {

    public boolean existsByIdAndBranch(String commitId, Branch branch);
}
