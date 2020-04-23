package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.dto.Projects;
import com.indra.repos.git.model.repository.ProjectMongoRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class ProjectMongoService extends GenericService {

    @Autowired
    private ProjectMongoRepository projectMongoRepository;


    /**
     * @return
     */
    public Projects restGetProject() {

        //log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
        Projects projects = null;
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
        Project project = mongoTemplate.findOne(query, Project.class);

        AtomicReference<Integer> start = new AtomicReference<>(0);
        Optional.ofNullable(project).ifPresent(p -> start.set(p.getIndex() + 1));

        HttpResponse<Projects> response = Unirest.get(gitProperties.getProjects())
                .routeParam("start", start.get().toString())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", gitProperties.getToken()).asObject(Projects.class);

        if (response.getStatus() == HttpStatus.OK.value())
            projects = response.getBody();

        //log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
        return projects;
    }

    /**
     * @param projects
     * @return
     */
    public Collection<Project> mongoGetProjects(Projects projects) {

        //log.info("{}: Init Get Projects", ProjectMongoService.class.getName());

        AtomicReference<Collection<Project>> projectCollection = new AtomicReference<>();
        Optional.ofNullable(projects).ifPresent(p -> {

            projectCollection.set(p.getValues());

            projectCollection.get().removeIf(project -> projectMongoRepository.existsById(project.getId()));

            AtomicInteger index = new AtomicInteger(p.getStart().intValue());
            projectCollection.get().forEach(project -> {
                project.setIndex(index.getAndIncrement());
            });
        });


        //log.info("{}: Finish Get Projects", ProjectMongoService.class.getName());
        return projectCollection.get();
    }

    /**
     * @param projects
     */
    public void saveAllProject(Collection<Project> projects) {
        //log.info("{}: Init Get Projects", ProjectMongoService.class.getName());

        Optional.ofNullable(projects).ifPresent(p -> projectMongoRepository.saveAll(p));

        //log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
    }

}
