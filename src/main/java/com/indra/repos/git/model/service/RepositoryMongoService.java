package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.domain.Repository;
import com.indra.repos.git.model.dto.Repositories;
import com.indra.repos.git.model.repository.ProjectMongoRepository;
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
public class RepositoryMongoService extends GenericService {

    @Autowired
    private RepositoryMongoRepository repositoryMongoRepository;

    @Autowired
    private ProjectMongoRepository projectMongoRepository;

    /**
     * @return
     */
    public Collection<Repositories> restGetRepositories() {

        Collection<Repositories> repositories = new ConcurrentLinkedQueue<>();

        Collection<Project> projects = projectMongoRepository.findAll();
        projects.forEach(p -> {

            Query query = new Query();
            query.addCriteria(Criteria.where("project").is(p));
            query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
            Repository repository = mongoTemplate.findOne(query, Repository.class);

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.of(repository).ifPresent(r -> start.set(r.getIndex() + 1));

            HttpResponse<Repositories> response = Unirest.get(gitProperties.getRepos())
                    .routeParam("projects", p.getKey())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Repositories.class);

            if (response.getStatus() == HttpStatus.OK.value()) {

                Repositories responseBodyRepositories = response.getBody();
                Collection<Repository> cRepos = responseBodyRepositories.getValues();
                cRepos.forEach(cr -> {
                    cr.setProject(p);
                });
                Optional.of(responseBodyRepositories).ifPresent(or -> repositories.add(or));
            }

        });

        return repositories;
    }

    /**
     * @param repositories
     * @return
     */
    public Collection<Repository> mongoGetRepositories(Collection<Repositories> repositories) {

        Collection<Repository> repositoryCollection = new ConcurrentLinkedQueue<>();
        Optional.of(repositories).ifPresent(repos -> {
            repos.forEach(r -> {
                Collection<Repository> cRepository = r.getValues();
                cRepository.removeIf(repository -> repositoryMongoRepository.existsById(repository.getId()));

                AtomicInteger index = new AtomicInteger(r.getStart().intValue());
                cRepository.forEach(repository -> {
                    repository.setIndex(index.getAndIncrement());
                });
                repositoryCollection.addAll(cRepository);
            });
        });

        return repositoryCollection;

    }

    /**
     * @param repositories
     */
    public void saveAllRepositories(Collection<Repository> repositories) {

        Optional.of(repositories).ifPresent(repository -> repositoryMongoRepository.saveAll(repository));

    }


}
