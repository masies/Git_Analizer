
package com.group4.softwareanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoftwareAnalytics {

	public static void main(String[] args) {
		SpringApplication.run(SoftwareAnalytics.class, args);
	}
}

// routes
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/status - status of a repo
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas - specific repo
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/commits - commits of a repo (paged)
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/commits/commitHASH - specific commit + code metrics computed (if not already present)
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/issues - issues of a repo (paged)
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/issues/252 - specific issue of a repo
//http://localhost:8080/api/repo/ClearCanvas/ClearCanvas/issues/233/comments - comments of a specific issue of a repo (paged)

//http://localhost:8080/api/commits - all commits
//http://localhost:8080/api/issues - all issues
//http://localhost:8080/api/repo - all repos
