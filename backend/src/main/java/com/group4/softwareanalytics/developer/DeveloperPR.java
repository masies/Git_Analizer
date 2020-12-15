package com.group4.softwareanalytics.developer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;

public class DeveloperPR {
    @Field("DeveloperPR")
    @Id
    private String id;

    private String owner;
    private String repo;

    private String username;

    private int opened;
    private int reviewed;

    private int acceptedOpened;
    private int acceptedReviewed;

    private double acceptedOpenedPercentage;
    private double acceptedReviewedPercentage;

    private HashSet<Integer> propenednumbers = new HashSet<>();
    private HashSet<Integer> prreviewednumbers = new HashSet<>();

    public DeveloperPR(String owner, String repo, String username) {
        this.owner = owner;
        this.repo = repo;
        this.username = username;
        this.opened = 0;
        this.reviewed = 0;
        this.acceptedOpened = 0;
        this.acceptedReviewed = 0;
        this.acceptedOpenedPercentage = 0;
        this.acceptedReviewedPercentage = 0;
    }

    public void addPROpened(int number){
        this.propenednumbers.add(number);
    }

    public HashSet<Integer> getPropenednumbers() {
        return propenednumbers;
    }

    public void setPropenednumbers(HashSet<Integer> propenednumbers) {
        this.propenednumbers = propenednumbers;
    }

    public void addPRreviewed(int number){
        prreviewednumbers.add(number);
    }

    public HashSet<Integer> getPrreviewednumbers() {
        return prreviewednumbers;
    }

    public void setPrreviewednumbers(HashSet<Integer> prreviewednumbers) {
        this.prreviewednumbers = prreviewednumbers;
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
    public int getAcceptedOpened() {
        return acceptedOpened;
    }
    public int getAcceptedReviewed() {
        return acceptedReviewed;
    }
    public double getAcceptedOpenedPercentage() {
        return acceptedOpenedPercentage;
    }
    public double getAcceptedReviewedPercentage() {
        return acceptedReviewedPercentage;
    }

    public void setOpened(int opened) {
        this.opened = opened;
        if (this.opened > 0 ){
            this.acceptedOpenedPercentage = (double) this.acceptedOpened / this.opened;
        } else{
            this.acceptedOpenedPercentage = 0;
        }
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
        if (this.reviewed > 0 ){
            this.acceptedReviewedPercentage = (double) this.acceptedReviewed / this.reviewed  ;
        } else{
            this.acceptedReviewedPercentage = 0;
        }
    }

    public void setAcceptedOpened(int acceptedOpened) {
        this.acceptedOpened = acceptedOpened;
        if (this.opened > 0 ){
            this.acceptedOpenedPercentage = (double) this.acceptedOpened / this.opened;
        } else{
            this.acceptedOpenedPercentage = 0;
        }
    }

    public void setAcceptedReviewed(int acceptedReviewed) {
        this.acceptedReviewed = acceptedReviewed;
        if (this.reviewed > 0 ){
            this.acceptedReviewedPercentage = (double) this.acceptedReviewed / this.reviewed;
        } else{
            this.acceptedReviewedPercentage = 0;
        }
    }

    public void setAcceptedOpenedPercentage(double acceptedOpenedPercentage) {
        this.acceptedOpenedPercentage = acceptedOpenedPercentage;
    }

    public void setAcceptedReviewedPercentage(double acceptedReviewedPercentage) {
        this.acceptedReviewedPercentage = acceptedReviewedPercentage;
    }
}
