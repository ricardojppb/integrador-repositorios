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
@Document(collection = "branches")
public class Branche implements Serializable {

    @Id
    private String id;
    private String displayId;
    private String type;
    private String latestCommit;
    private String latestChangeset;
    private Boolean isDefault;
    private int index;
    @DBRef
    private Repository repository;


}
