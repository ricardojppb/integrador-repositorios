package com.indra.repos.svn.model.domain;

import com.indra.repos.util.AuditModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbsvn_logs")
public class SVNLog extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sq_log")
    private Long sqLog;

    private Integer revision;
    private String author;
    private String message;
    private Date date;

}
