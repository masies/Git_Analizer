package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncService {

    @Autowired
    private RepoRepository repository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @Async
    public void fetchData(String owner, String name) throws InterruptedException {
        try {
            RepositoryService service = new RepositoryService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");
            Repository r = service.getRepository(owner, name);
            Repo repo = new Repo(r, owner, name);
//            TODO: delete old file, if any.
//            repository.deleteById(r.getId());
            repo.hasInfoDone();
            repository.save(repo);
            fetchIssues(owner, name, repo);
        } catch (Exception ignored) {
        }
    }


    public void fetchIssues(String owner, String name, Repo repo) throws IOException {
        try {
            IssueService service = new IssueService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");

            // gather all the open issues
            List<Issue> issuesOpen = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));
            System.out.println("OPEN ISSUES: "+ issuesOpen.size());

            // gather all the closed issues
            List<Issue> issuesClosed = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_CLOSED));
            System.out.println("CLOSED ISSUES: "+ issuesClosed.size());

            // merge the two list of issues (maybe in future we need to have a field for open/closed)
            List<Issue> issues = Stream.concat(issuesOpen.stream(), issuesClosed.stream())
                             .collect(Collectors.toList());
            System.out.println("ALL ISSUES: "+ issues.size());

            System.out.println(issues.size());
            List<com.group4.softwareanalytics.Issue> issueList = new ArrayList<com.group4.softwareanalytics.Issue>();

            System.out.println("storing comments and issues..");
            for (Issue issue : issues) {
                com.group4.softwareanalytics.Issue i = new com.group4.softwareanalytics.Issue(issue, owner, name);
                issueList.add(i);

                // gather all the issue comments
                List<Comment> comments = service.getComments(owner, name, issue.getNumber());
                List<IssueComment> commentList = new ArrayList<IssueComment>();
                for (Comment comment : comments) {
                    IssueComment c = new IssueComment(comment, owner, name, issue.getNumber());
                    commentList.add(c);
                }
                issueCommentRepository.saveAll(commentList);
            }
            System.out.println("done with storing comments");
            issueRepository.saveAll(issueList);
            System.out.println("done with storing issues");

            repo.hasIssuesDone();
            repository.save(repo);


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
