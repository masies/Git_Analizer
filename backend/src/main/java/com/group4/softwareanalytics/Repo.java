package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.IOException;
import java.util.*;

public class Repo {

    @Field("repository")
    private Repository repository;

    private Boolean fetchedInfo = false;
    private Boolean fetchedIssues = false;
    private Boolean fetchedCommits = false;

    public Repo(Repository repository) {
        this.repository = repository;
        this.fetchedCommits = false;
        this.fetchedInfo = false;
        this.fetchedCommits = false;
    }

    public Boolean getFetchedInfo() {
        return fetchedInfo;
    }

    public void setFetchedInfo(Boolean fetchedInfo) {
        this.fetchedInfo = fetchedInfo;
    }

    public Boolean getFetchedIssues() {
        return fetchedIssues;
    }

    public void setFetchedIssues(Boolean fetchedIssues) {
        this.fetchedIssues = fetchedIssues;
    }

    public Boolean getFetchedCommits() {
        return fetchedCommits;
    }

    public void setFetchedCommits(Boolean fetchedCommits) {
        this.fetchedCommits = fetchedCommits;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }


}
