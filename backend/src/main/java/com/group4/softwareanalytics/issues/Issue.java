package com.group4.softwareanalytics.issues;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Issue {
    @Field("issue")
    private org.eclipse.egit.github.core.Issue issue;

    @Id
    private String id;

    private String owner;
    private String repo;
    private Boolean isPR;

    public Issue(org.eclipse.egit.github.core.Issue issue, String owner, String repo, Boolean isPR) {
        this.owner = owner;
        this.repo = repo;
        this.issue = issue;
        this.isPR = isPR;
    }

    public Boolean getPR() {
        return isPR;
    }

    public void setPR(Boolean PR) {
        isPR = PR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public org.eclipse.egit.github.core.Issue getIssue() {
        return issue;
    }

    public void setIssue(org.eclipse.egit.github.core.Issue issue) {
        this.issue = issue;
    }


}
