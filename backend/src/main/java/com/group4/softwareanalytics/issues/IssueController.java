package com.group4.softwareanalytics.issues;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/issues")
    @ResponseBody
    public List<Issue> getRepos() {
        return issueRepository.findAll();
    }

    @GetMapping("/repo/{owner}/{repo}/issues")
    public @ResponseBody
    Page<Issue> getAttr(
            @PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo,
            @RequestParam(value = "userLogin", defaultValue = "") String userLogin,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
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

        if (userLogin != null && !userLogin.isEmpty()) {
            criteria.add(Criteria.where("issue.user.login").regex(userLogin, "i"));
        }
        if (text != null && !text.isEmpty()) {
            criteria.add(new Criteria().orOperator(Criteria.where("issue.title").regex(text, "i"), Criteria.where("issue.body").regex(text, "i")));
        }

        if (startDate != null && endDate != null) {
            criteria.add(Criteria.where("issue.createdAt").gte(startDate).lte(endDate));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        List<Issue> list = mongoTemplate.find(query, Issue.class);
        return PageableExecutionUtils.getPage(
                list,
                PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)),
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Issue.class));
    }

    @GetMapping("/repo/{owner}/{repo}/issues/{number}")
    public @ResponseBody
    Issue getAttr(@PathVariable(value = "owner") String owner, @PathVariable(value = "repo") String repo, @PathVariable(value = "number") String number) {
        return issueRepository.findByOwnerAndRepoAndId(owner, repo, Integer.parseInt(number));
    }


}

