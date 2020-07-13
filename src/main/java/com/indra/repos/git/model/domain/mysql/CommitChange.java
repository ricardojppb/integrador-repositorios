package com.indra.repos.git.model.domain.mysql;

@lombok.Data
@lombok.Getter
@lombok.Setter
@lombok.ToString
@javax.persistence.Entity
@javax.persistence.Table(name = "tbgit_changes")
public class CommitChange extends com.indra.repos.util.AuditModel {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @javax.persistence.Column(name = "sq_change")
    private Long sqChange;

    private String contentId;
    private String fromContentId;
    private String type;
    private String nodeType;
    private String fileName;

    @javax.validation.constraints.NotNull
    @javax.persistence.Column(name = "change_index")
    private Integer index;

    @javax.persistence.Transient
    private Path path;

    @javax.validation.constraints.NotNull
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, optional = false)
    @javax.persistence.JoinColumn(name = "commit_fk", nullable = false)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Commit commit;

    public Long getSqChange() {
        return sqChange;
    }

    public void setSqChange(Long sqChange) {
        this.sqChange = sqChange;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getFromContentId() {
        return fromContentId;
    }

    public void setFromContentId(String fromContentId) {
        this.fromContentId = fromContentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public com.indra.repos.git.model.domain.mysql.Path getPath() {
        fileName = path.getToString();
        return path;
    }

    public void setPath(com.indra.repos.git.model.domain.mysql.Path path) {
        fileName = path.getToString();
        this.path = path;
    }
}
