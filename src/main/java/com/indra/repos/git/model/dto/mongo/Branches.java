package com.indra.repos.git.model.dto.mongo;

import com.indra.repos.git.model.domain.mongo.Branch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@ToString
public class Branches implements Serializable {

    Collection<Branch> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
