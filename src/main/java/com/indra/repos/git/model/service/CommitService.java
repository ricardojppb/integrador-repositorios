package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.mysql.Branch;
import com.indra.repos.git.model.domain.mysql.Commit;
import com.indra.repos.git.model.domain.mysql.Project;
import com.indra.repos.git.model.domain.mysql.Repository;
import com.indra.repos.git.model.dto.mysql.Commits;
import com.indra.repos.git.model.repository.BranchRepository;
import com.indra.repos.git.model.repository.CommitRepository;
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
public class CommitService extends GenericService {

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private BranchRepository branchRepository;

    public Collection<Commits> restGetCommits() {

        Collection<Commits> commits = new ConcurrentLinkedQueue<>();

        Collection<Branch> branches = branchRepository.findAll();

        branches.forEach(branche -> {

            Repository repository = branche.getRepository();
            Project project = repository.getProject();

            Integer indiceMaximo = commitRepository.obterIndexMaxCommitWhereBranch(branche.getSqBranch());

            AtomicReference<Integer> start = new AtomicReference<>(0);
            Optional.ofNullable(indiceMaximo).ifPresent(max -> start.set(max + 1));

            HttpResponse<Commits> response = Unirest.get(gitProperties.getCommits())
                    .routeParam("projects", project.getKey())
                    .routeParam("repos", repository.getSlug())
                    .routeParam("branch", branche.getDisplayId())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(Commits.class);


            if (response.getStatus() == HttpStatus.OK.value()) {

                response.getParsingError().ifPresent(e -> {
                    e.getOriginalBody();
                    e.getMessage();
                });

                Optional.ofNullable(response.getBody()).ifPresent(responseBodyCommits -> {
                    Collection<Commit> cCommits = responseBodyCommits.getValues();
                    cCommits.forEach(cb -> {
                        cb.setBranch(branche);
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
                cCommit.removeIf(commit -> commitRepository.existsByIdAndBranch(commit.getId(), commit.getBranch()));

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
    @Transactional
    public void saveAllCommits(Collection<Commit> commits) {

        Optional.ofNullable(commits).ifPresent(commit -> commitRepository.saveAll(commit));

    }


}
