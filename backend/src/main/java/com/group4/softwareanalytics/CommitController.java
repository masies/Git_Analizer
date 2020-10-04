package com.group4.softwareanalytics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitController {

    @Autowired
    private CommitRepository commitRepository;


    @GetMapping("/commits")
    @ResponseBody
    public List<Commit> getCommits() throws IOException {
        return commitRepository.findAll();
    }

    @RequestMapping("/repo/{owner}/{repo}/commits")
    public @ResponseBody
    Page<Commit> getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @RequestParam(value = "page",defaultValue = "0") String page, @RequestParam(value = "size",defaultValue = "20") String size) {
        return commitRepository.findByOwnerAndRepo(owner,repo, PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
    }

    @RequestMapping("/repo/{owner}/{repoName}/commits/{commitId}")
    public @ResponseBody
    Commit getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repoName") String repoName, @PathVariable(value="commitId") String commitID ) {
        Commit commit = commitRepository.findByOwnerAndRepoAndCommitName(owner,repoName, commitID);
        if (!commit.getHasMetrics()) {
            ArrayList<String> parentCommitIDs = commit.getCommitParentsIDs();
            ProjectMetric metrics = ProjectMetricExtractor.commitCodeQualityExtractor(owner, repoName, commitID, parentCommitIDs);
            commit.setProjectMetrics(metrics);
            commit.setHasMetrics(true);
            commitRepository.save(commit);
        }
        return commit;
    }

}
