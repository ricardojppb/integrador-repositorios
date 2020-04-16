package com.indra.repos.git.model.dto;

import com.indra.repos.git.model.domain.Branche;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@ToString
public class Branches implements Serializable {

    Collection<Branche> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
