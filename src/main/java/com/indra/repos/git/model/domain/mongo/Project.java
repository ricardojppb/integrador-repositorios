package com.indra.repos.git.model.domain.mongo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@Document(collection = "projects")
public class Project implements Serializable {

    @Id
    private ObjectId sq;
    //@Indexed
    @Field("project_id")
    private Integer id;
    private String key;
    private String name;
    private String description;
    private String type;
    //@Indexed
    private int index;

}
