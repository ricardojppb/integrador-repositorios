package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Project {

    private Integer id;
    private String key;
    private String name;
    private String description;
    private String type;

}
