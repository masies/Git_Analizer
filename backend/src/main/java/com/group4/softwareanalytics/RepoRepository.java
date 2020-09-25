package com.group4.softwareanalytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "repo", path = "repo")
public interface RepoRepository extends MongoRepository<Repo, String>  {
    Page<Repo> findAll(Pageable pageable);
}

