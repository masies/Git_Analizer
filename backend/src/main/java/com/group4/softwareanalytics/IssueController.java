package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssueController {

    @Autowired
    private IssueRepository repository;

    @GetMapping("/issues")
    @ResponseBody
    public List<Issue> getRepos() throws IOException {
        return repository.findAll();
    }

    @RequestMapping("/{owner}/{repo}/issues")
    public @ResponseBody Page<Issue> getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @RequestParam(value = "page",defaultValue = "10") String page, @RequestParam(value = "size",defaultValue = "10") String size) {
        return repository.findByOwnerAndRepo(owner,repo, PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
    }

}

