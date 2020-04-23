package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.domain.Commit;
import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.domain.Repository;
import com.indra.repos.git.model.dto.Commits;
import com.indra.repos.git.model.repository.BrancheMongoRepository;
import com.indra.repos.git.model.repository.CommitMongoRepository;
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
public class CommitMongoService extends GenericService {

    @Autowired
    private CommitMongoRepository commitMongoRepository;

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    public Collection<Commits> restGetCommits() {

        Collection<Commits> commits = new ConcurrentLinkedQueue<>();

        Collection<Branche> branches = brancheMongoRepository.findAll();

        branches.forEach(branche -> {

            Repository repository = branche.getRepository();
            Project project = repository.getProject();

            Query query = new Query();
            query.addCriteria(Criteria.where("branche").is(branche));
            query.with(Sort.by(Sort.Direction.DESC, "index")).limit(1);
            Commit commit = mongoTemplate.findOne(query, Commit.class);

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.ofNullable(commit).ifPresent(c -> start.set(c.getIndex() + 1));

            HttpResponse<Commits> response = Unirest.get(gitProperties.getCommits())
                    .routeParam("projects", project.getKey())
                    .routeParam("repos", repository.getSlug())
                    .routeParam("branch", branche.getDisplayId())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Commits.class);

            if (response.getStatus() == HttpStatus.OK.value()) {

                Optional.ofNullable(response.getBody()).ifPresent(responseBodyCommits -> {
                    Collection<Commit> cCommits = responseBodyCommits.getValues();
                    cCommits.forEach(cb -> {
                        cb.setBranche(branche);
                    });
                    commits.add(responseBodyCommits);
                });
            }
        });

        return commits;
    }

    /**
     * @param commits
     * @return
     */
    public Collection<Commit> mongoGetCommits(Collection<Commits> commits) {

        Collection<Commit> commitCollection = new ConcurrentLinkedQueue<>();
        Optional.ofNullable(commits).ifPresent(oCommits -> {
            oCommits.forEach(oc -> {
                Collection<Commit> cCommit = oc.getValues();
                cCommit.removeIf(commit -> commitMongoRepository.existsByIdAndBranche(commit.getId(), commit.getBranche()));

                AtomicInteger index = new AtomicInteger(oc.getStart().intValue());
                cCommit.forEach(commit -> {
                    commit.setIndex(index.getAndIncrement());
                });
                commitCollection.addAll(cCommit);
            });
        });

        return commitCollection;
    }

    /**
     * @param commits
     */
    public void saveAllCommits(Collection<Commit> commits) {

        Optional.ofNullable(commits).ifPresent(commit -> commitMongoRepository.saveAll(commit));

    }


}
