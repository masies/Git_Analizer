package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncService {

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Async
    public void fetchData(String owner, String name) throws InterruptedException {
        try {
            RepositoryService service = new RepositoryService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");
            Repository r = service.getRepository(owner, name);
            Repo repo = new Repo(r, owner, name);

            repoRepository.findAndRemove(owner,name);
            issueRepository.findAndRemove(owner,name);
            issueCommentRepository.findAndRemove(owner,name);
            commitRepository.findAndRemove(owner,name);

            repo.hasInfoDone();
            repoRepository.save(repo);
            fetchIssues(owner, name, repo);
            fetchCommits(owner, name, repo);
        } catch (Exception ignored) {
        }
    }

    public void fetchCommits(String owner, String repoName, Repo r) throws IOException, GitAPIException {
        System.out.println("Fetching commits...");
        String repo_url = "https://github.com/"+ owner +"/"+ repoName;
        String dest_url = "./repo/" + owner +"/"+ repoName;
        List<Commit> commitList = new ArrayList<Commit>();
        List<String> branches = new ArrayList<>();

        if (!Files.exists(Paths.get(dest_url))) {
            CommitExtractor.DownloadRepo(repo_url, dest_url);
        }

        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

        Git git = new Git(repo);

        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        for(Ref branch: refs)
        {
            branches.add(branch.getName());
        }

        List<RevCommit> revCommitList = CommitExtractor.getCommits(branches.get(0), git, repo);

        for(RevCommit revCommit: revCommitList)
        {
            List<DiffEntry> diffEntries = CommitExtractor.getModifications(git, revCommit.getName());
            List<String> modifications = new ArrayList<>();

            for (DiffEntry diffEntry : diffEntries) {
                modifications.add(diffEntry.getChangeType().toString());
            }

            String developerName = revCommit.getAuthorIdent().getName();
            String developerMail = revCommit.getAuthorIdent().getEmailAddress();
            String encodingName = revCommit.getEncodingName();
            String fullMessage = revCommit.getFullMessage();
            String shortMessage = revCommit.getShortMessage();
            String commitName = revCommit.getName();
            int commitType = revCommit.getType();

            //TODO: check is the right timezone. https://archive.eclipse.org/jgit/docs/jgit-2.0.0.201206130900-r/apidocs/org/eclipse/jgit/revwalk/RevCommit.html#getCommitTime()
            long millis = revCommit.getCommitTime();
            Date d = new Date(millis*1000);

            Commit c = new Commit(modifications, owner, repoName, developerName, developerMail, encodingName, fullMessage, shortMessage, commitName, commitType, d);
            commitList.add(c);
        }
        commitRepository.saveAll(commitList);

        System.out.println("done fetching commits");
        r.hasCommitsDone();
        repoRepository.save(r);
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

            // merge the two list of issues
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
            repoRepository.save(repo);


        } catch (IOException e) {
            System.out.println("Error during issue fetching");
            e.printStackTrace();
        }
    }

}
