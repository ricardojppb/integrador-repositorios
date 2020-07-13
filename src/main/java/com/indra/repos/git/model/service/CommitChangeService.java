package com.indra.repos.git.model.service;

@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class CommitChangeService extends GenericService {

    @org.springframework.beans.factory.annotation.Autowired
    private com.indra.repos.git.model.repository.CommitRepository commitRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.indra.repos.git.model.repository.CommitChangesRepository commitChangesRepository;

    public java.util.Collection<com.indra.repos.git.model.dto.mysql.CommitChanges> restGetCommitChanges() {

        java.util.Collection<com.indra.repos.git.model.dto.mysql.CommitChanges> commitsChanges = new java.util.concurrent.ConcurrentLinkedQueue<>();

        java.util.Collection<com.indra.repos.git.model.domain.mysql.Commit> commits = commitRepository.findAll();

        commits.forEach(commit -> {

            com.indra.repos.git.model.domain.mysql.Branch branch = commit.getBranch();
            com.indra.repos.git.model.domain.mysql.Repository repository = branch.getRepository();
            com.indra.repos.git.model.domain.mysql.Project project = repository.getProject();

            Integer indiceMaximo = commitChangesRepository.obterIndexMaxChangeWhereCommit(commit.getSqCommit());

            java.util.concurrent.atomic.AtomicReference<Integer> start = new java.util.concurrent.atomic.AtomicReference<>(0);
            java.util.Optional.ofNullable(indiceMaximo).ifPresent(max -> start.set(max + 1));

            kong.unirest.HttpResponse<com.indra.repos.git.model.dto.mysql.CommitChanges> response = kong.unirest.Unirest.get(gitProperties.getChanges())
                    .routeParam("projects", project.getKey())
                    .routeParam("repos", repository.getSlug())
                    .routeParam("hash", commit.getId())
                    .routeParam("start", start.get().toString())
                    .header("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", gitProperties.getToken()).asObject(com.indra.repos.git.model.dto.mysql.CommitChanges.class);


            if (response.getStatus() == org.springframework.http.HttpStatus.OK.value()) {

                response.getParsingError().ifPresent(e -> {
                    e.getOriginalBody();
                    e.getMessage();
                });

                java.util.Optional.ofNullable(response.getBody()).ifPresent(responseBodyCommitChanges -> {
                    java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> commitChanges = responseBodyCommitChanges.getValues();
                    commitChanges.forEach(commitChange -> {
                        commitChange.setCommit(commit);
                    });
                    commitsChanges.add(responseBodyCommitChanges);
                });
            }
        });

        return commitsChanges;
    }

    /**
     * @param commits
     * @return
     */
    public java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> findCommitChanges(java.util.Collection<com.indra.repos.git.model.dto.mysql.CommitChanges> responseCommitChanges) {

        java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> commitCahngesCollection = new java.util.concurrent.ConcurrentLinkedQueue<>();
        java.util.Optional.ofNullable(responseCommitChanges).ifPresent(responseCommitChange -> {
            responseCommitChange.forEach(rCommitChanges -> {
                java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> commitChanges = rCommitChanges.getValues();
                commitChanges.removeIf(commitChange -> commitChangesRepository.existsByContentIdAndCommit(commitChange.getContentId(), commitChange.getCommit()));

                java.util.concurrent.atomic.AtomicInteger index = new java.util.concurrent.atomic.AtomicInteger(rCommitChanges.getStart().intValue());
                commitChanges.forEach(commitChange -> {
                    commitChange.setIndex(index.getAndIncrement());
                });
                commitCahngesCollection.addAll(commitChanges);
            });
        });

        return commitCahngesCollection;
    }

    /**
     * @param commits
     */
    @javax.transaction.Transactional
    public void saveAllCommitChanges(java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> commitChanges) {

        java.util.Optional.ofNullable(commitChanges).ifPresent(changes -> commitChangesRepository.saveAll(changes));

    }


}
