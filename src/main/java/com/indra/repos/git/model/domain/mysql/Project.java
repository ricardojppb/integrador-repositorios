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


}
