package com.group4.softwareanalytics.commits;


import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.metrics.ProjectMetricExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitController {

    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/commits")
    @ResponseBody
    public List<Commit> getCommits() {
        return commitRepository.findAll();
    }

    @GetMapping("/repo/{owner}/{repo}/commits")
    public @ResponseBody
    Page<Commit> getAttr(
            @PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo,
            @RequestParam(value = "developerName", defaultValue = "") String developerName,
            @RequestParam(value = "hasMetrics", required = false) Boolean hasMetrics,
            @RequestParam(value = "isFixing", required = false) Boolean isFixing,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "20") String size) {
        final Query query = new Query().with(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
        final List<Criteria> criteria = new ArrayList<>();
        if (owner != null && !owner.isEmpty()) {
            criteria.add(Criteria.where("owner").is(owner));
        }
        if (repo != null && !repo.isEmpty()) {
            criteria.add(Criteria.where("repo").is(repo));
        }

        if (hasMetrics != null) {
            criteria.add(Criteria.where("hasMetrics").is(hasMetrics));
        }

        if (isFixing != null) {
            criteria.add(Criteria.where("LinkedFixedIssues.0").exists(isFixing));
        }

        if (developerName != null && !developerName.isEmpty()) {
            criteria.add(Criteria.where("developerName").regex(developerName, "i"));
        }
        if (text != null && !text.isEmpty()) {
            criteria.add(new Criteria().orOperator(Criteria.where("fullMessage").regex(text, "i"), Criteria.where("shortMessage").regex(text, "i")));
        }

        if (startDate != null && endDate != null) {
            criteria.add(Criteria.where("commitDate").gte(startDate).lte(endDate));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        List<Commit> list = mongoTemplate.find(query, Commit.class);
        return PageableExecutionUtils.getPage(
                list,
                PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)),
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Commit.class));
    }

    @GetMapping("/repo/{owner}/{repoName}/commits/{commitId}")
    public @ResponseBody
    Commit getAttr(@PathVariable(value = "owner") String owner,
                   @PathVariable(value = "repoName") String repoName,
                   @PathVariable(value = "commitId") String commitID,
                   @RequestParam(value = "mode", defaultValue = "quick") String AnalysisMode) throws IOException {
        Commit commit = commitRepository.findByOwnerAndRepoAndCommitName(owner, repoName, commitID);
        if (!commit.getHasMetrics() || (AnalysisMode != null && AnalysisMode.equals("deep"))) {
            ArrayList<String> parentCommitIDs = commit.getCommitParentsIDs();
            // TODO: add a boolean flag to check if we want to skip this step (quick vs deep analysis)
            ProjectMetric metrics = new ProjectMetric(0,0,0,0,0,0,0,0);
            if (AnalysisMode != null && AnalysisMode.equals("deep")) {
                metrics = ProjectMetricExtractor.commitCodeQualityExtractor(owner, repoName, commitID, parentCommitIDs);
            }
            commit.setProjectMetrics(metrics);
            commit.setHasMetrics(true);

//            String dest_url = "./repo/" + owner + "/" + repoName;
//            org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");
//            Git git = new Git(repo);

            commitRepository.save(commit);

        }
        return commit;
    }

}
