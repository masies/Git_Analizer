package com.group4.softwareanalytics;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Repo {
//    @CreatedDate
//    private Date created_at;
//    @LastModifiedDate
//    private Date updated_at;

    @Field("id") private Long id;
	private String name;
	private String full_name;
	private String html_url;
    private String description;
    private Date created_at;
    private Date updated_at;
    private String homepage;
    private Long size;
    private Long watchers_count;
    private String language;
    private Long forks_count;
    private Long open_issues_count;
    private Long forks;
    private Long subscribers_count;

    public Repo(Long id, String name, String full_name, String html_url, String description, Date created_at, Date updated_at, String homepage, Long size, Long watchers_count, String language, Long forks_count, Long open_issues_count, Long forks, Long subscribers_count) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.html_url = html_url;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.homepage = homepage;
        this.size = size;
        this.watchers_count = watchers_count;
        this.language = language;
        this.forks_count = forks_count;
        this.open_issues_count = open_issues_count;
        this.forks = forks;
        this.subscribers_count = subscribers_count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(Long watchers_count) {
        this.watchers_count = watchers_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getForks_count() {
        return forks_count;
    }

    public void setForks_count(Long forks_count) {
        this.forks_count = forks_count;
    }

    public Long getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(Long open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public Long getForks() {
        return forks;
    }

    public void setForks(Long forks) {
        this.forks = forks;
    }

    public Long getSubscribers_count() {
        return subscribers_count;
    }

    public void setSubscribers_count(Long subscribers_count) {
        this.subscribers_count = subscribers_count;
    }
}
