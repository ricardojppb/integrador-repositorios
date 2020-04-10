package com.indra.repos.configurarion;

import com.indra.repos.properties.GitProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties(GitProperties.class)
public class GitConfiguration {

    @Autowired
    private GitProperties gitProperties;

}
