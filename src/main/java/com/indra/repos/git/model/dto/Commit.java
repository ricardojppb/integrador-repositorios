package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class Commit {

    private String id;
    private String displayId;
    private Author author;

    private String message;
    private Date committerTimestamp;


}
