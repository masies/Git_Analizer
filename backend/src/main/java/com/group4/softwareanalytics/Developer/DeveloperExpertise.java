package com.group4.softwareanalytics.Developer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class DeveloperExpertise {
    @Field("DeveloperExpertise")
    @Id
    private String id;

    private String owner;
    private String repo;

    private String developerMail;
    private int expertise;


    public DeveloperExpertise(String owner, String repo, int expertise, String developerMail) {
        this.owner = owner;
        this.repo = repo;
        this.expertise = expertise;
        this.developerMail = developerMail;
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

    public String getDeveloperMail() {
        return developerMail;
    }

    public void setDeveloperMail(String developerMail) {
        this.developerMail = developerMail;
    }
}
