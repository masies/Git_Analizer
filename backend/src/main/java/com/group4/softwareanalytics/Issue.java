package com.group4.softwareanalytics;

import org.springframework.data.mongodb.core.mapping.Field;

public class Issue {
    @Field("issue")
    private org.eclipse.egit.github.core.Issue issue;

    private String owner;
    private String repo;

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

    public void setRepository(org.eclipse.egit.github.core.Issue issue) {
        this.issue = issue;
    }

    public Issue(org.eclipse.egit.github.core.Issue issue, String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        this.issue = issue;
    }
}
