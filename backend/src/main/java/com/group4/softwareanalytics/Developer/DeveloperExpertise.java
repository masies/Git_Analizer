package com.group4.softwareanalytics.Developer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashSet;

public class DeveloperExpertise {
    @Field("DeveloperExpertise")
    @Id
    private String id;

    private String owner;
    private String repo;

    private String email;

    HashSet<String> commitHashSet = new HashSet<>();
    private int expertise;


    public DeveloperExpertise(String owner, String repo, int expertise, String email) {
        this.owner = owner;
        this.repo = repo;
        this.expertise = expertise;
        this.email = email;
    }

    public HashSet<String> getCommitHashSet() {
        return commitHashSet;
    }

    public void setCommitHashSet(HashSet<String> commitHashSet) {
        this.commitHashSet = commitHashSet;
    }

    public void addCommitHash(String commitHash){
        this.commitHashSet.add(commitHash);
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

    public int getExpertise() {
        return expertise;
    }

    public void setExpertise(int expertise) {
        this.expertise = expertise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
