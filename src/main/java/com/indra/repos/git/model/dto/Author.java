package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class Author implements Serializable {

    private String name;
    private String emailAddress;

}
