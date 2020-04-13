package com.indra.repos.git.model.domain;

import com.indra.repos.git.model.dto.Author;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@Document(collection = "commits")
public class Commit implements Serializable {

    @Id
    private String id;
    private String displayId;
    private Author author;
    private String message;
    private Date committerTimestamp;
    private int index;
    @DBRef
    private Branche branche;


}
