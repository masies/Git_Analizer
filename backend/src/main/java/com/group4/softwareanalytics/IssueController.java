package com.group4.softwareanalytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @GetMapping("/issues")
    @ResponseBody
    public List<Issue> getRepos() throws IOException {
        return issueRepository.findAll();
    }

    @RequestMapping("/repo/{owner}/{repo}/issues")
    public @ResponseBody Page<Issue> getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @RequestParam(value = "page",defaultValue = "0") String page, @RequestParam(value = "size",defaultValue = "20") String size) {
        return issueRepository.findByOwnerAndRepo(owner,repo, PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
    }

    @RequestMapping("/repo/{owner}/{repo}/issues/{number}")
    public @ResponseBody Issue getAttr( @PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @PathVariable(value="number") String number) {
        return issueRepository.findByOwnerAndRepoAndId(owner, repo, Integer.parseInt(number));
    }



}

