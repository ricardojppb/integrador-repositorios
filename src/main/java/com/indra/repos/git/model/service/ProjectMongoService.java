package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.dto.Projects;
import com.indra.repos.git.model.repository.ProjectMongoRepository;
import com.indra.repos.git.properties.GitProperties;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ProjectMongoService {

    @Autowired
    private ProjectMongoRepository projectMongoRepository;

    @Autowired
    private GitProperties gitProperties;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @return
     */
    public Projects restGetProject() {

        log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
        Projects projects = null;
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
        Project p = mongoTemplate.findOne(query, Project.class);

        Integer start = (p instanceof Project) ? p.getIndex() + 1 : 0;

        HttpResponse<Projects> response = Unirest.get(gitProperties.getProjects())
                .routeParam("start", start.toString())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", gitProperties.getToken()).asObject(Projects.class);

        if (response.getStatus() == HttpStatus.OK.value())
            projects = response.getBody();

        log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
        return projects;
    }

    /**
     * @param projects
     * @return
     */
    public Collection<Project> mongoGetProjects(Projects projects) {

        log.info("{}: Init Get Projects", ProjectMongoService.class.getName());

        Collection<Project> projectCollection = null;
        if (projects instanceof Projects) {

            projectCollection = projects.getValues();

            projectCollection.removeIf(project -> projectMongoRepository.existsById(project.getId()));

            AtomicInteger index = new AtomicInteger(projects.getStart().intValue());
            projectCollection.forEach(project -> {
                project.setIndex(index.getAndIncrement());
            });
        }

        log.info("{}: Finish Get Projects", ProjectMongoService.class.getName());
        return projectCollection;
    }

    /**
     * @param projects
     */
    public void saveAllProject(Collection<Project> projects) {
        log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
        if (projects instanceof Collection) {
            projectMongoRepository.saveAll(projects);
        }
        log.info("{}: Init Get Projects", ProjectMongoService.class.getName());
    }

}
