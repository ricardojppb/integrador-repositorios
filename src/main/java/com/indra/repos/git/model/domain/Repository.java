package com.indra.repos.git.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Getter
@Setter
@Document(collection = "repositories")
public class Repository implements Serializable {

    @Id
    private Integer id;
    private String slug;
    private String name;
    private String description;
    private int index;
    @DBRef
    private Project project;

}
