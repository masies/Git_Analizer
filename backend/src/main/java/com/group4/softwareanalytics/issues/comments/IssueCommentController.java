package com.group4.softwareanalytics.issues.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssueCommentController {
    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @GetMapping("/issue/comments")
    @ResponseBody
    public List<IssueComment> getRepos() {
        return issueCommentRepository.findAll();
    }

    @GetMapping("/repo/{owner}/{repo}/issues/{issuesNumber}/comments")
    public @ResponseBody Page<IssueComment> getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @PathVariable(value="issuesNumber") String number, @RequestParam(value = "page",defaultValue = "0") String page, @RequestParam(value = "size",defaultValue = "20") String size) {
        return issueCommentRepository.findByOwnerAndRepoAndIssueNumber(owner,repo,Integer.parseInt(number), PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
    }

}






