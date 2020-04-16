package com.indra.repos.git.model.dto;

import com.indra.repos.git.model.domain.Project;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@ToString
public class Projects implements Serializable {

    Collection<Project> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
