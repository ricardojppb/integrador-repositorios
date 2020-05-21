package com.indra.repos.git.model.domain.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indra.repos.util.AuditModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbgit_commits")
public class Commit extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_commit")
    private Long sqCommit;

    @Column(name = "commit_id")
    @NotNull
    private String id;

    @Column(name = "commit_ref")
    @NotNull
    private String displayId;

    @Lob
    @NotNull
    private String message;

    @NotNull
    private Integer committerTimestamp;

    @NotNull
    @Column(name = "commit_index")
    private Integer index;

    @Transient
    private Date committerDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Author author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branche_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Branch branch;

//    public Date getCommitterDate() {
//        return new Date(this.committerTimestamp);
//    }
//
//    public void setCommitterDate(Long committerTimestamp) {
//        this.committerDate = new Date(committerTimestamp);
//    }


    public Long getSqCommit() {
        return sqCommit;
    }

    public void setSqCommit(Long sqCommit) {
        this.sqCommit = sqCommit;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCommitterTimestamp() {
        return committerTimestamp;
    }

    public void setCommitterTimestamp(Integer committerTimestamp) {
        this.committerTimestamp = committerTimestamp;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getCommitterDate() {
        return committerDate;
    }

    public void setCommitterDate(Date committerDate) {
        this.committerDate = committerDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}