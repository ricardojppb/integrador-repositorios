package com.indra.repos.git.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Branche {

    private String id;
    private String displayId;
    private String type;
    private String latestCommit;
    private String latestChangeset;
    private Boolean isDefault;


}
