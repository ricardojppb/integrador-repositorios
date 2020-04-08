package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Repository {

    private Integer id;
    private String slug;
    private String name;
    private String description;
    private Project project;

}
