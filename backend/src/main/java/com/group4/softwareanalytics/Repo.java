package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Repository;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

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
