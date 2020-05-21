package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.domain.mongo.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryMongoRepository extends MongoRepository<com.indra.repos.git.model.domain.mongo.Repository, ObjectId> {

    public boolean existsByIdAndProject(Integer repositoryId, Project project);
}
