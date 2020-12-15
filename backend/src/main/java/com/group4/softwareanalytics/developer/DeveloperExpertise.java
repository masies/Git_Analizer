package com.group4.softwareanalytics.developer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DeveloperExpertise {
    @Field("DeveloperExpertise")
    @Id
    private String id;

    private String owner;
    private String repo;


    private String email;

    Set<String> commitHashSet = new HashSet<>();
    Set<Date> commitDates = new HashSet<>();
    private int expertise;


    public DeveloperExpertise(String owner, String repo, int expertise, String email) {
        this.owner = owner;
        this.repo = repo;
        this.expertise = expertise;
        this.email = email;
    }

    public Set<Date> getCommitDates() {
        return commitDates;
    }

    public void setCommitDates(Set<Date> commitDates) {
        this.commitDates = commitDates;
    }

    public void addCommitDate(Date date){
        this.commitDates.add(date);
    }



    public Set<String> getCommitHashSet() {
        return commitHashSet;
    }

    public void setCommitHashSet(Set<String> commitHashSet) {
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

    public int computeMonthExpertise(Date commitDate) {
        int monthExpertise = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(commitDate);
        c.add(Calendar.MONTH, -1);
        Date cmp = c.getTime();
        for (Date d : this.commitDates) {
            if (d.after(cmp)){
                monthExpertise++;
            }
        }
        return monthExpertise;
    }
}
