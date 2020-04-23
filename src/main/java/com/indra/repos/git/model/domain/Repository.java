package com.indra.repos.git.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@Document(collection = "repositories")
public class Repository implements Serializable {

    @Id
    private ObjectId sq;

    @Indexed
    @Field("repository_id")
    private Integer id;
    private String slug;
    private String name;
    private String description;
    @Indexed
    private int index;
    @DBRef
    @Indexed(name = "Repository_Project")
    private Project project;

}
