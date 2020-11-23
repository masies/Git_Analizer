package com.group4.softwareanalytics.developer;

import com.group4.softwareanalytics.fileContribution.FileContribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "developerExpertise", path = "developerExpertise")
public interface DeveloperExpertiseRepository extends MongoRepository<DeveloperExpertise,String> {

    @Query("{'owner' : ?0 , 'repo' : ?1}")
    Page<DeveloperExpertise> findByOwnerAndRepo(String owner, String repo, Pageable pageable);


    @Query("{'owner' : ?0 , 'repo' : ?1}")
    ArrayList<DeveloperExpertise> findByOwnerAndRepoArr(String owner, String repo);


    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    public void findAndRemove (String owner, String repo);

    @Query("{'owner' : ?0 , 'repo' : ?1, 'email': {$regex: ?2 }}")
    Page<DeveloperExpertise> findByQuery(String owner, String repo, String query, Pageable p);

    DeveloperExpertise findByOwnerAndRepoAndEmail(String owner, String repo, String mail);
}
