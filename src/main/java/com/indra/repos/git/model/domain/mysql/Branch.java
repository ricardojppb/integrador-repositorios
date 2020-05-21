package com.indra.repos.git.model.domain.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indra.repos.util.AuditModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbgit_branches")
public class Branch extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_branch")
    private Long sqBranch;

    @NotNull
    @Column(name = "branch_id")
    private String id;

    @NotNull
    @Column(name = "branch_display")
    private String displayId;

    @NotNull
    private String type;

    @NotNull
    private String latestCommit;

    @NotNull
    private String latestChangeset;

    @NotNull
    private boolean isDefault;

    @NotNull
    @Column(name = "branch_index")
    private Integer index;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repository_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Repository repository;

    public Long getSqBranch() {
        return sqBranch;
    }

    public void setSqBranch(Long sqBranch) {
        this.sqBranch = sqBranch;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public String getLatestChangeset() {
        return latestChangeset;
    }

    public void setLatestChangeset(String latestChangeset) {
        this.latestChangeset = latestChangeset;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}


