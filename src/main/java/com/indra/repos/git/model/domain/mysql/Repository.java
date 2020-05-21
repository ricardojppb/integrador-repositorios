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
@Table(name = "tbgit_repositories")
public class Repository extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_repository")
    private Long sqRepository;
    @NotNull
    @Column(name = "repository_id")
    private Integer id;
    @NotNull
    private String slug;
    @NotNull
    private String name;

    @Column(name = "descricao")
    private String description;

    @NotNull
    @Column(name = "repository_index")
    private Integer index;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    public Long getSqRepository() {
        return sqRepository;
    }

    public void setSqRepository(Long sqRepository) {
        this.sqRepository = sqRepository;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
