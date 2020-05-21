package com.indra.repos.git.model.dto.mongo;

import com.indra.repos.git.model.domain.mongo.Commit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;


@Getter
@Setter
@ToString
public class Commits implements Serializable {

    private Collection<Commit> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

}
