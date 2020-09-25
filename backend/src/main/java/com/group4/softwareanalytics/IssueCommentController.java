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
public class IssueCommentController {
    @Autowired
    private IssueCommentRepository repository;

    @GetMapping("/issue/comments")
    @ResponseBody
    public List<IssueComment> getRepos() throws IOException {
        return repository.findAll();
    }

    @RequestMapping("/{owner}/{repo}/issues/{issuesNumber}/comments")
    public @ResponseBody Page<IssueComment> getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @PathVariable(value="issuesNumber") String number, @RequestParam(value = "page",defaultValue = "0") String page, @RequestParam(value = "size",defaultValue = "20") String size) {
        return repository.findByOwnerAndRepoAndIssueNumber(owner,repo,Integer.parseInt(number), PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
    }
}






