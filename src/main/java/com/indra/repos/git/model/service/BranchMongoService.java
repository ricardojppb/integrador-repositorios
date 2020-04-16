package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.domain.Repository;
import com.indra.repos.git.model.dto.Branches;
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
            Branche branche = mongoTemplate.findOne(query, Branche.class);

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.of(branche).ifPresent(b -> start.set(b.getIndex() + 1));

            HttpResponse<Branches> response = Unirest.get(gitProperties.getBranches())
                    .routeParam("projects", project.getKey())
                    .routeParam("repos", repository.getSlug())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Branches.class);

            if (response.getStatus() == HttpStatus.OK.value()) {

                Branches responseBodyBranches = response.getBody();
                Collection<Branche> cBranches = responseBodyBranches.getValues();
                cBranches.forEach(cb -> {
                    cb.setRepository(repository);
                });
                Optional.of(responseBodyBranches).ifPresent(ob -> branches.add(ob));
            }
        });

        return branches;
    }

    /**
     * @param branches
     * @return
     */
    public Collection<Branche> mongoGetBranches(Collection<Branches> branches) {

        Collection<Branche> brancheCollection = new ConcurrentLinkedQueue<>();
        Optional.of(branches).ifPresent(oBranches -> {
            oBranches.forEach(b -> {
                Collection<Branche> cBranche = b.getValues();
                cBranche.removeIf(branche -> brancheMongoRepository.existsById(branche.getId()));

                AtomicInteger index = new AtomicInteger(b.getStart().intValue());
                cBranche.forEach(branche -> {
                    branche.setIndex(index.getAndIncrement());
                });
                brancheCollection.addAll(cBranche);
            });
        });

        return brancheCollection;
    }

    /**
     * @param branches
     */
    public void saveAllBranches(Collection<Branche> branches) {
        Optional.of(branches).ifPresent(branche -> brancheMongoRepository.saveAll(branche));
    }
}
