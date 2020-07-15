package com.indra.repos.git.model.domain.mysql;

import com.indra.repos.util.AuditModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbgit_projects")
public class Project extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_project")
    private Long sqProject;

    @NotNull
    @Column(name = "project_id")
    private Integer id;
    @NotNull
    @Column(name = "project_key")
    private String key;
    @NotNull
    private String name;

    @Column(name = "descricao")
    private String description;
    @NotNull
    private String type;
    @NotNull
    @Column(name = "project_index")
    private Integer index;

    public Long getSqProject() {
        return sqProject;
    }

    public void setSqProject(Long sqProject) {
        this.sqProject = sqProject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
