package com.indra.repos.svn.model.domain;

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
@Table(name = "tbsvn_repositories")
public class SVNRepositories extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_repository")
    private Long sqRepository;

    private String url;
    private String username;
    private String password;

}
