package com.group4.softwareanalytics;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "repo", path = "repo")
public interface RepoRepository extends MongoRepository<Repo, String>  {

    @Query("{'$or': [{'owner': {$regex: ?0 }}, {'repo': {$regex: ?0 }}]}")
    Page<Repo> findByQuery(String query, Pageable p);


    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    public void findAndRemove (String owner, String repo);

    @Query("{'owner' : ?0 , 'repo' : ?1}")
    Repo findByOwnerAndRepo(String owner, String repo);
}
