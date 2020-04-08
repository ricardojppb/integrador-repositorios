package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Author {

    private String name;
    private String emailAddress;

}
