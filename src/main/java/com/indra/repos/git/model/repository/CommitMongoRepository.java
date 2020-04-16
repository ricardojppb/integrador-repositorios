package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.domain.Commit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitMongoRepository extends MongoRepository<Commit, ObjectId> {

    public boolean existsByIdAndBranche(String commitId, Branche branche);
}
