package com.indra.repos.git.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "git.bitbucket")
public class GitProperties {

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
