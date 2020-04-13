package com.indra.repos.git.model.dto;

import com.indra.repos.git.model.domain.Commit;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;


@Getter
@Setter
public class Commits implements Serializable {

    private Collection<Commit> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
