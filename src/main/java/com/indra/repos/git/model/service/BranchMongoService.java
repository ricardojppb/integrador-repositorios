package com.indra.repos.git.model.service;

import com.indra.repos.git.model.repository.BrancheMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BranchMongoService {

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    private RestTemplate restTemplate;

    public void findBranchsInRepos() {

    }
}
