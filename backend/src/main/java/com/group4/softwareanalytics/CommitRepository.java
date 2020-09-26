package com.group4.softwareanalytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commit", path = "commit")
public interface CommitRepository extends MongoRepository<Commit,String> {
    @Query(value="{'owner' : ?0 , 'repo' : ?1}", delete = true)
    public void findAndRemove (String owner, String repo);

    @Query("{'owner' : ?0 , 'repo' : ?1}")
    Page<Commit> findByOwnerAndRepo(String owner, String repo, Pageable pageable);

}
