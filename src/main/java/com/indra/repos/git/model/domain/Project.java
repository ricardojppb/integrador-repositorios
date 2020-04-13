package com.indra.repos.git.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Getter
@Setter
@Document(collection = "projects")
public class Project implements Serializable {

    @Id
    private Integer id;
    private String key;
    private String name;
    private String description;
    private String type;
    private int index;

}
