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

        Collection<Repositories> responseRepositories = new ConcurrentLinkedQueue<>();

        Collection<Project> projects = projectRepository.findAll();
        projects.forEach(project -> {

            Integer indiceMaximo = reposRepository.obterIndexMaxRepositoryWhereProject(project.getSqProject());

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.ofNullable(indiceMaximo).ifPresent(max -> start.set(max + 1));

            HttpResponse<Repositories> response = Unirest.get(gitProperties.getRepos())
                    .routeParam("projects", project.getKey())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Repositories.class);

            if (response.getStatus() == HttpStatus.OK.value()) {

                response.getParsingError().ifPresent(e -> {
                    e.getOriginalBody();
                    e.getMessage();
                });

                Optional.ofNullable(response.getBody()).ifPresent(responseBodyRepositories -> {
                    Collection<Repository> repositories = responseBodyRepositories.getValues();
                    repositories.forEach(repository -> {
                        repository.setProject(project);
                    });
                    responseRepositories.add(responseBodyRepositories);
                });
            }

        });

        return responseRepositories;
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
