package com.indra.repos.git.model.domain;

import com.indra.repos.git.model.dto.Author;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@ToString
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCommitterTimestamp() {
        return committerTimestamp;
    }

    public void setCommitterTimestamp(Date committerTimestamp) {
        this.committerTimestamp = committerTimestamp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Branche getBranche() {
        return branche;
    }

    public void setBranche(Branche branche) {
        this.branche = branche;
    }
}
