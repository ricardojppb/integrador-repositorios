package com.indra.repos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MongoBDConfig {

    @Autowired
    private Environment env;

//    public MongoDbFactory mongoDbFactory() {
//        //return new SimpleMongoClientDbFactory(new MongoClientURI(env.getProperty("spring.data.mongodb.uri")), "repos");
//    }
}
