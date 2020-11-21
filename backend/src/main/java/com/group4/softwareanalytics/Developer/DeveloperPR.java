package com.group4.softwareanalytics.Developer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class DeveloperPR {
    @Field("DeveloperPR")
    @Id
    private String id;

    private String owner;
    private String repo;

    private String username;

    private int opened;
    private int reviewed;

    private int accepted_opened;
    private int accepted_reviewed;

    private double accepted_opened_percentage;
    private double accepted_reviewed_percentage;

    public DeveloperPR(String owner, String repo, String username) {
        this.owner = owner;
        this.repo = repo;
        this.username = username;
        this.opened = 0;
        this.reviewed = 0;
        this.accepted_opened = 0;
        this.accepted_reviewed = 0;
        this.accepted_opened_percentage = 0;
        this.accepted_reviewed_percentage = 0;
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getOpened() {
        return opened;
    }
    public int getReviewed() {
        return reviewed;
    }
    public int getAccepted_opened() {
        return accepted_opened;
    }
    public int getAccepted_reviewed() {
        return accepted_reviewed;
    }
    public double getAccepted_opened_percentage() {
        return accepted_opened_percentage;
    }
    public double getAccepted_reviewed_percentage() {
        return accepted_reviewed_percentage;
    }

    public void setOpened(int opened) {
        this.opened = opened;
        if (this.opened > 0 ){
            this.accepted_opened_percentage = (double) this.accepted_opened / this.opened;
        } else{
            this.accepted_opened_percentage = 0;
        }
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
        if (this.reviewed > 0 ){
            this.accepted_reviewed_percentage = (double) this.accepted_reviewed / this.reviewed  ;
        } else{
            this.accepted_reviewed_percentage = 0;
        }
    }

    public void setAccepted_opened(int accepted_opened) {
        this.accepted_opened = accepted_opened;
        if (this.opened > 0 ){
            this.accepted_opened_percentage = (double) this.accepted_opened / this.opened;
        } else{
            this.accepted_opened_percentage = 0;
        }
    }

    public void setAccepted_reviewed(int accepted_reviewed) {
        this.accepted_reviewed = accepted_reviewed;
        if (this.reviewed > 0 ){
            this.accepted_reviewed_percentage = (double) this.accepted_reviewed / this.reviewed;
        } else{
            this.accepted_reviewed_percentage = 0;
        }
    }

    public void setAccepted_opened_percentage(double accepted_opened_percentage) {
        this.accepted_opened_percentage = accepted_opened_percentage;
    }

    public void setAccepted_reviewed_percentage(double accepted_reviewed_percentage) {
        this.accepted_reviewed_percentage = accepted_reviewed_percentage;
    }
}
