package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.mongo.Branch;
import com.indra.repos.git.model.domain.mongo.Project;
import com.indra.repos.git.model.domain.mongo.Repository;
import com.indra.repos.git.model.dto.mongo.Branches;
import com.indra.repos.git.model.repository.BrancheMongoRepository;
import com.indra.repos.git.model.repository.RepositoryMongoRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class BranchMongoService extends GenericService {

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    @Autowired
    private RepositoryMongoRepository repositoryMongoRepository;

    /**
     * @return
     */
    public Collection<Branches> restGetBranches() {

        Collection<Branches> branches = new ConcurrentLinkedQueue<>();

        Collection<Repository> repositories = repositoryMongoRepository.findAll();

        repositories.forEach(repository -> {

            Project project = repository.getProject();

            Query query = new Query();
            query.addCriteria(Criteria.where("repository").is(repository));
            query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
            Branch branch = mongoTemplate.findOne(query, Branch.class);

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.ofNullable(branch).ifPresent(b -> start.set(b.getIndex() + 1));

            HttpResponse<Branches> response = Unirest.get(gitProperties.getBranches())
                    .routeParam("projects", project.getKey())
                    .routeParam("repos", repository.getSlug())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Branches.class);

            if (response.getStatus() == HttpStatus.OK.value()) {

                Optional.ofNullable(response.getBody()).ifPresent(responseBodyBranches -> {
                    Collection<Branch> cBranches = responseBodyBranches.getValues();
                    cBranches.forEach(cb -> {
                        cb.setRepository(repository);
                    });
                    branches.add(responseBodyBranches);
                });

            }
        });

        return branches;
    }

    /**
     * @param branches
     * @return
     */
    public Collection<Branch> mongoGetBranches(Collection<Branches> branches) {

        Collection<Branch> branchCollection = new ConcurrentLinkedQueue<>();
        Optional.ofNullable(branches).ifPresent(oBranches -> {
            oBranches.forEach(b -> {
                Collection<Branch> cBranch = b.getValues();
                cBranch.removeIf(branche -> brancheMongoRepository.existsByIdAndRepository(branche.getId(), branche.getRepository()));

                AtomicInteger index = new AtomicInteger(b.getStart().intValue());
                cBranch.forEach(branche -> {
                    branche.setIndex(index.getAndIncrement());
                });
                branchCollection.addAll(cBranch);
            });
        });

        return branchCollection;
    }

    /**
     * @param branches
     */
    public void saveAllBranches(Collection<Branch> branches) {
        Optional.ofNullable(branches).ifPresent(branche -> brancheMongoRepository.saveAll(branche));
    }
}
