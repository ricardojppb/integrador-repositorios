package com.indra.repos.git.model.dto;

import com.indra.repos.git.model.domain.Project;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class Projects implements Serializable {

    Collection<Project> values;
    private Integer size;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;
    private Boolean isLastPage;

    public Collection<Project> getValues() {
        return values;
    }

    public void setValues(Collection<Project> values) {
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
