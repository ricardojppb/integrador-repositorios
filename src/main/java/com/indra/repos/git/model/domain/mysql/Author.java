package com.indra.repos.git.model.domain.mysql;

import com.indra.repos.util.AuditModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbgit_authors")
public class Author extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_author")
    private Long sqAuthor;

    private String name;

    private String emailAddress;

    private String displayName;

    public Long getSqAuthor() {
        return sqAuthor;
    }

    public void setSqAuthor(Long sqAuthor) {
        this.sqAuthor = sqAuthor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
