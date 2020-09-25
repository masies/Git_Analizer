package com.group4.softwareanalytics;

import com.sun.codemodel.internal.JFieldRef;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.IOException;
import java.util.*;

public class Repo {

    @Field("repository")
    private Repository repository;

    @Id private String id;

    private String owner;
    private String repo;

    private RepoStatus status;

    public Repo(Repository repository, String owner, String repo) {
        this.repository = repository;
        this.status = new RepoStatus();
        this.owner = owner;
        this.repo = repo;

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

    public RepoStatus getStatus() {
        return status;
    }

    public void setStatus(RepoStatus status) {
        this.status = status;
    }

    public void hasInfoDone(){
        this.status.setFetchedInfo(true);
    }
    public void hasIssuesDone(){
        this.status.setFetchedIssues(true);
    }
    public void hasCommitsDone(){
        this.status.setFetchedCommits(true);
    }


    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }


}
