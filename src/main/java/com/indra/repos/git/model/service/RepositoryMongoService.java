package com.indra.repos.git.model.service;

import com.indra.repos.git.model.repository.RepositoryMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RepositoryMongoService {

    @Autowired
    private RepositoryMongoRepository repositoryMongoRepository;

    private RestTemplate restTemplate;

//    public void findBranchsInRepos(Optional<Collection<Repository>> repositories) {
//
//    }
}
