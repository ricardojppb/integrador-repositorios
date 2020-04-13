package com.indra.repos.git.model.service;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.repository.BrancheMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Optional;

@Service
public class BranchMongoService {

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    private RestTemplate restTemplate;

    public void findBranchsInRepos(Optional<Collection<Branche>> branches) {

    }
}
