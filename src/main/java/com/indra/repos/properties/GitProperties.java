package com.indra.repos.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ConfigurationProperties(prefix = "git")
public class GitProperties {

    @NotBlank
    private String endPoint;
    @NotBlank
    private String token;
    @NotBlank
    private String projects;
    @NotBlank
    private String repos;
    @NotBlank
    private String branches;
    @NotBlank
    private String commits;

}
