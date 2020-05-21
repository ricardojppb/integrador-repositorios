package com.indra.repos.git.model.dto.mysql;

import com.indra.repos.git.model.domain.mysql.Repository;
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

    public Collection<Repository> getValues() {
        return values;
    }

    public void setValues(Collection<Repository> values) {
        this.values = values;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getNextPageStart() {
        return nextPageStart;
    }

    public void setNextPageStart(Integer nextPageStart) {
        this.nextPageStart = nextPageStart;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public void setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
    }
}