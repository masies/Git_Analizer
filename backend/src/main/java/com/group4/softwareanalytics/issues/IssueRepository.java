package com.group4.softwareanalytics.issues;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "issue", path = "issue")
public interface IssueRepository extends MongoRepository<Issue,String> {
    @Query("{'owner' : ?0 , 'repo' : ?1, 'issue.htmlUrl' : {$regex : '.*issues*'}}")
    Page<Issue> findByOwnerAndRepo(String owner, String repo, Pageable pageable);

    @Query("{'owner' : ?0 , 'repo' : ?1, 'issue.number' : ?2}")
    Issue findByOwnerAndRepoAndId(String owner, String repo, int number);

    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    List<Issue> findAndRemove (String owner, String repo);

}


