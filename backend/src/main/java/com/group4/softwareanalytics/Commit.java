package com.group4.softwareanalytics;

import org.springframework.data.annotation.Id;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commit {
    @Id
    private String id;

    private List<String> modifications;
    private String owner;
    private String repo;

    private String developerName;
    private String developerMail;
    private String encodingName;
    private String fullMessage;
    private String shortMessage;
    private String commitName;
    private int commitType;
    private Date commitDate;

    public Commit(List<String> modifications, String owner, String repo, String developerName, String developerMail, String encodingName, String fullMessage, String shortMessage, String commitName, int commitType, Date commitDate) {
        this.modifications = modifications;
        this.owner = owner;
        this.repo = repo;
        this.developerName = developerName;
        this.developerMail = developerMail;
        this.encodingName = encodingName;
        this.fullMessage = fullMessage;
        this.shortMessage = shortMessage;
        this.commitName = commitName;
        this.commitType = commitType;
        this.commitDate = commitDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getDeveloperMail() {
        return developerMail;
    }

    public void setDeveloperMail(String developerMail) {
        this.developerMail = developerMail;
    }

    public String getEncodingName() {
        return encodingName;
    }

    public void setEncodingName(String encodingName) {
        this.encodingName = encodingName;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getCommitName() {
        return commitName;
    }

    public void setCommitName(String commitName) {
        this.commitName = commitName;
    }

    public int getCommitType() {
        return commitType;
    }

    public void setCommitType(int commitType) {
        this.commitType = commitType;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
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

}
