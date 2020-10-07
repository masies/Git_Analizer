package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitDiff;
import com.group4.softwareanalytics.commits.CommitExtractor;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.issues.comments.IssueComment;
import com.group4.softwareanalytics.issues.comments.IssueCommentRepository;
import com.group4.softwareanalytics.issues.IssueRepository;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        fetchIssues(owner, name, repo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

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

        File dir = new File(dest_url);

        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }

        CommitExtractor.DownloadRepo(repo_url, owner, repoName);

        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

        Git git = new Git(repo);

        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        for(Ref branch: refs)
        {
            branches.add(branch.getName());
        }

        List<RevCommit> revCommitList = CommitExtractor.getCommits(branches.get(0), git, repo);

        for (Iterator<RevCommit> iterator = revCommitList.iterator(); iterator.hasNext(); ) {
            RevCommit revCommit = iterator.next();

            String developerName = revCommit.getAuthorIdent().getName();
            String developerMail = revCommit.getAuthorIdent().getEmailAddress();
            String encodingName = revCommit.getEncodingName();
            String fullMessage = revCommit.getFullMessage();
            String shortMessage = revCommit.getShortMessage();
            String commitName = revCommit.getName();

            ArrayList<String> commitParentsIDs = new ArrayList<>();
            for (RevCommit parent: revCommit.getParents()) {
                commitParentsIDs.add(parent.name());
            }

            List<CommitDiff> diffEntries = new ArrayList<>();
//            diffEntries = CommitExtractor.getModifications(git, commitName, dest_url, commitParentsIDs);

            ProjectMetric projectMetric = new ProjectMetric(0,0,0,0,0,0,0,0);

            int commitType = revCommit.getType();
            long millis = revCommit.getCommitTime();
            Date date = new Date(millis * 1000);

            Commit c = new Commit(diffEntries, owner, repoName, developerName, developerMail, encodingName, fullMessage, shortMessage, commitName, commitType, date, projectMetric, commitParentsIDs, false);
            commitList.add(c);
        }
        commitRepository.saveAll(commitList);

        System.out.println("done fetching commits");
        r.hasCommitsDone();
        repoRepository.save(r);
    }

    @Async
    public void fetchIssues(String owner, String name, Repo repo) throws IOException {
        try {
            IssueService service = new IssueService();

            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");

            List<Issue> issuesOpen = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));

            List<Issue> issuesClosed = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_CLOSED));

            List<Issue> issues = Stream.concat(issuesOpen.stream(), issuesClosed.stream())
                             .collect(Collectors.toList());

            System.out.println("Found" + issues.size() + "Issues");
            List<com.group4.softwareanalytics.issues.Issue> issueList = new ArrayList<com.group4.softwareanalytics.issues.Issue>();

            System.out.println("storing comments and issues..");
            for (Issue issue : issues) {
                System.out.println("procsessing "+ issue.toString());

                com.group4.softwareanalytics.issues.Issue i = new com.group4.softwareanalytics.issues.Issue(issue, owner, name);
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
