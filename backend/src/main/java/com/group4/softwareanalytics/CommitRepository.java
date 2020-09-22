
package com.group4.softwareanalytics;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commit", path = "commit")
public interface CommitRepository extends MongoRepository<Commit, String> {

	List<Commit> findByDeveloper(@Param("developer") String developer);

}
