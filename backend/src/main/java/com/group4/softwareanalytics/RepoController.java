package com.group4.softwareanalytics;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    public void fetchIssues(String owner, String name) throws IOException {
        try {
            IssueService service = new IssueService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");

            // gather all the issues
            List<Issue> issues = service.getIssues(owner, name,
                    // TODO: take all the issues and not just the open ones
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));
            List<com.group4.softwareanalytics.Issue> issueList = new ArrayList<com.group4.softwareanalytics.Issue>();

            System.out.println("storing comments and issues..");
            for (Issue issue : issues) {
                com.group4.softwareanalytics.Issue i = new com.group4.softwareanalytics.Issue(issue, owner, name);
                issueList.add(i);

                // gather all the issue comments
                List<Comment> comments = service.getComments(owner,name, issue.getNumber());
                List<IssueComment> commentList = new ArrayList<IssueComment>();
                for (Comment comment: comments) {
                    IssueComment c = new IssueComment(comment, owner, name, issue.getNumber());
                    commentList.add(c);
                }
                issueCommentRepository.saveAll(commentList);

            }
            System.out.println("done with storing comments");
            issueRepository.saveAll(issueList);
            System.out.println("done with storing issues");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @GetMapping("/repo")
    @ResponseBody
    public List<Repo> getRepos() throws IOException {
        return repository.findAll();
    }

    @PostMapping("/repo")
    @ResponseBody
    public Repo fetchRepo(@RequestBody Map<String, Object> body) {
        try {
            String owner = body.getOrDefault("owner", "google").toString();
            String name = body.getOrDefault("name", "guava").toString();

            RepositoryService service = new RepositoryService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");
            Repository r = service.getRepository(owner, name);
            Repo repo = new Repo(r);
            repository.save(repo);

            fetchIssues(owner, name);

            return repo;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}







