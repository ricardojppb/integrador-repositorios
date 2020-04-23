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
@Document(collection = "branches")
//@CompoundIndexes({
//        @CompoundIndex(name = "email_age", def = "{'email.id' : 1, 'age': 1}")
//})
public class Branche implements Serializable {

    @Id
    private ObjectId sq;

    @Indexed
    @Field("branche_id")
    private String id;
    @Field("branche_ref")
    private String displayId;
    private String type;
    private String latestCommit;
    private String latestChangeset;
    private Boolean isDefault;
    @Indexed
    private int index;
    @DBRef
    @Indexed(name = "Branche_Repository")
    private Repository repository;



}


