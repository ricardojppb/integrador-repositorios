package com.indra.repos.git.model.domain.mongo;

import com.indra.repos.git.model.dto.mongo.Author;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;


@Data
@Getter
@Setter
@ToString
@Document(collection = "commits")
public class Commit implements Serializable {

    @Id
    private ObjectId sq;
    //@Indexed
    @Field("commit_id")
    private String id;
    @Field("commit_ref")
    private String displayId;
    private Author author;
    private String message;
    @Transient
    private Date committerDate;
    private Long committerTimestamp;
    //@Indexed
    private int index;
    @DBRef
    //@Indexed(name = "Commit_Branche")
    private Branch branch;

    public Date getCommitterDate() {
        return new Date(this.committerTimestamp);
    }

    public void setCommitterDate(Long committerTimestamp) {
        this.committerDate = new Date(committerTimestamp);
    }
}
