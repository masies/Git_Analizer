package com.group4.softwareanalytics.developer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "DeveloperPR", path = "DeveloperPR")
public interface DeveloperPRRepository extends MongoRepository<DeveloperPR,String> {
    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    public void findAndRemove(String owner, String repo);

     @Query("{'owner' : ?0 , 'repo' : ?1}")
     Page<DeveloperPR> findByOwnerAndRepo(String owner, String repo, Pageable pageable);

     @Query("{'owner' : ?0 , 'repo' : ?1, 'username': {$regex: ?2 }}")
     Page<DeveloperPR> findByQuery(String owner, String repo, String query, Pageable p);

     DeveloperPR findByOwnerAndRepoAndUsername(String owner, String repo, String username);
}
