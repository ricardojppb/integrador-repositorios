package com.indra.repos.git.model.repository;

import com.indra.repos.git.model.dto.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMongoRepository extends MongoRepository<Project, String> {
    Optional<Project> findById(String id);
}
