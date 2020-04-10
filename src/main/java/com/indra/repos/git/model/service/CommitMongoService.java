package com.indra.repos.git.model.service;

import com.indra.repos.git.model.repository.CommitMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommitMongoService {

    @Autowired
    private CommitMongoRepository commitMongoRepository;

    private RestTemplate restTemplate;


}
