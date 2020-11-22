package com.group4.softwareanalytics.fileContribution;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;

@RepositoryRestResource(collectionResourceRel = "FileContribution", path = "FileContribution")
public interface FileContributionRepository extends MongoRepository<FileContribution,String> {

    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    public void findAndRemove(String owner, String repo);

    @Query("{'owner' : ?0 , 'repo' : ?1}")
    ArrayList<FileContribution> findByOwnerAndRepo(String owner, String repo);



}
