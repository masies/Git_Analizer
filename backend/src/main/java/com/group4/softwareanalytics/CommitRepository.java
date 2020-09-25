package com.group4.softwareanalytics;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commit", path = "commit")
public interface CommitRepository extends MongoRepository<Commit,String> {


}
