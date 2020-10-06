package com.group4.softwareanalytics.issues.comments;

import org.eclipse.egit.github.core.Comment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class IssueComment {
    @Field("comment")
    @Id
    private String id;
    private Comment comment;
    private String owner;
    private String repo;
    private int issueNumber;

    public IssueComment(Comment comment, String owner, String repo, int issueNumber) {
        this.comment = comment;
        this.owner = owner;
        this.repo = repo;
        this.issueNumber = issueNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
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

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }
}


