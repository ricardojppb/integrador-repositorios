package com.indra.repos.git.model.service;


import com.indra.repos.git.properties.GitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class GenericService {

    @Autowired
    protected GitProperties gitProperties;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
