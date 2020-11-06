package com.group4.softwareanalytics.commits;

import java.util.Date;

public class CommitGeneralInfo {
    private String commitName;
    private String developerName;
    private Date commitDate;

    public CommitGeneralInfo(String commitName, String developerName, Date commitDate) {
        this.commitName = commitName;
        this.developerName = developerName;
        this.commitDate = commitDate;
    }

    public String getCommitName() {
        return commitName;
    }

    public void setCommitName(String commitName) {
        this.commitName = commitName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }
}
