package com.group4.softwareanalytics.fileContribution;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

public class FileContribution {
    @Field("FileContribution")
    @Id
    private String id;

    private String owner;
    private String repo;

    private String path;
    private Boolean file;

    private String dir;

    // map developer (String username) to number of commits he performed on this file
    HashMap<String,Integer> contributionsMap = new HashMap<>();

    private String topContributor;

    public FileContribution(String owner, String repo, String path, boolean file) {
        this.owner = owner;
        this.repo = repo;
        this.path = path;
        this.dir = this.path.replaceAll("/(?:.(?!/))+$", "");
        // true if file
        // false if directory
        this.file = file;
    }

    public void addDeveloperContribute(String developer){
        this.contributionsMap.merge(developer, 1, Integer::sum);

        // update top Contributor
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : this.contributionsMap.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        topContributor = maxEntry.getKey();

    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getFile() {
        return file;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public HashMap<String, Integer> getContributionsMap() {
        return contributionsMap;
    }

    public void setContributionsMap(HashMap<String, Integer> contributionsMap) {
        this.contributionsMap = contributionsMap;
    }

    public String getTopContributor() {
        return topContributor;
    }

    public void setTopContributor(String topContributor) {
        this.topContributor = topContributor;
    }
}
