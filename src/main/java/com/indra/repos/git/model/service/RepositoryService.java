package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.mysql.Project;
import com.indra.repos.git.model.domain.mysql.Repository;
import com.indra.repos.git.model.dto.mysql.Repositories;
import com.indra.repos.git.model.repository.ProjectRepository;
import com.indra.repos.git.model.repository.ReposRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class RepositoryService extends GenericService {

    @Autowired
    private ReposRepository reposRepository;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * @return
     */
    public Collection<Repositories> restGetRepositories() {

        Collection<Repositories> repositories = new ConcurrentLinkedQueue<>();

        Collection<Project> projects = projectRepository.findAll();
        projects.forEach(p -> {

//            Query query = new Query();
//            query.addCriteria(Criteria.where("project").is(p));
//            query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
//            Repository repository = mongoTemplate.findOne(query, Repository.class);

            Repository repository = reposRepository.obterRepositorytOrderByIndexDescLimitUm();

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.ofNullable(repository).ifPresent(r -> start.set(r.getIndex() + 1));

            HttpResponse<Repositories> response = Unirest.get(gitProperties.getRepos())
                    .routeParam("projects", p.getKey())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Repositories.class);

            if (response.getStatus() == HttpStatus.OK.value()) {
                Optional.ofNullable(response.getBody()).ifPresent(responseBodyRepositories -> {
                    Collection<Repository> cRepos = responseBodyRepositories.getValues();
                    cRepos.forEach(cr -> {
                        cr.setProject(p);
                    });
                    repositories.add(responseBodyRepositories);
                });
                Repositories responseBodyRepositories = response.getBody();
                Collection<Repository> cRepos = responseBodyRepositories.getValues();
                cRepos.forEach(cr -> {
                    cr.setProject(p);
                });
                Optional.ofNullable(responseBodyRepositories).ifPresent(or -> repositories.add(or));
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
        Optional.ofNullable(repositories).ifPresent(repos -> {
            repos.forEach(r -> {
                Collection<Repository> cRepository = r.getValues();

                cRepository.removeIf(repository -> reposRepository.existsByIdAndProject(repository.getId(), repository.getProject()));

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
    @Transactional
    public void saveAllRepositories(Collection<Repository> repositories) {

        Optional.ofNullable(repositories).ifPresent(repository -> reposRepository.saveAll(repository));

    }


}
