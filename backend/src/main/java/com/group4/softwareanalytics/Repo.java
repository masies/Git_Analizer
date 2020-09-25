package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.IOException;
import java.util.*;

public class Repo {

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Field("repository")
    private Repository repository;


    public Repo(Repository repository) {
        this.repository = repository;
    }


}
