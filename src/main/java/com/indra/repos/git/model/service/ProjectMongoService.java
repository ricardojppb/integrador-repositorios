package com.indra.repos.git.model.service;

import com.indra.repos.git.model.repository.ProjectMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectMongoService {

    @Autowired
    private ProjectMongoRepository projectMongoRepository;

    private RestTemplate restTemplate;


}
