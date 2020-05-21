package com.indra.repos.git.model.dto.mongo;

import com.indra.repos.git.model.domain.mongo.Repository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@ToString
public class Repositories {

    Collection<Repository> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
