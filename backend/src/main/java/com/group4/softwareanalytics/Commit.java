package com.group4.softwareanalytics;

import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

public class Commit {

//    @Field("commit")
//    private RevCommit commit;

//    private String commitID;
    private List<String> modifications = new ArrayList<>();
    private String owner;
    private String repo;


    public Commit(List<String> modifications, String owner, String repo) {
        this.modifications = modifications;
        this.owner = owner;
        this.repo = repo;
    }

    public List<String> getModifications() {
        return modifications;
    }

    public void setModifications(List<String> modifications) {
        this.modifications = modifications;
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

    //    public RevCommit getCommit() {
//        return commit;
//    }
//
//    public void setCommit(RevCommit commit) {
//        this.commit = commit;
//    }
}
