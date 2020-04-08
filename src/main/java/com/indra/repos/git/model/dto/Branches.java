package com.indra.repos.git.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class Branches {

    Collection<Branche> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;


}
