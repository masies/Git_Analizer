package com.group4.softwareanalytics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.IssueService;
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

    private CommitExtractor commitExtractor;
    private List<com.group4.softwareanalytics.issues.Issue> issueList = new ArrayList<>();
    private List<Commit> commitList = new ArrayList<>();

    @Async
    public void fetchData(String owner, String name) throws InterruptedException {
        try {

            Repo repo = fetchRepo(owner,name);

            repoRepository.findAndRemove(owner,name);
            issueRepository.findAndRemove(owner,name);
            issueCommentRepository.findAndRemove(owner,name);
            commitRepository.findAndRemove(owner,name);

            repo.hasInfoDone();
            repoRepository.save(repo);

            try {
                issueList = fetchIssues(owner, name, repo);
            } catch (IOException e){
                Logger logger = LogManager.getLogger(AsyncService.class.getName());
                logger.error(e.getMessage(),e);
            }

            commitList = fetchCommits(owner, name, repo);

        } catch (Exception ignored) {
        }
    }

    public static ArrayList<Integer> fixedIssuesRelated(RevCommit commit, List<com.group4.softwareanalytics.issues.Issue> issueList) {
        List<String> keywords = Arrays.asList("fix","solve","resolve");
        List<String> stopWord = Arrays.asList("must","should","will");

        List<Integer> linkedIssues = new ArrayList<>();
        String fullText = "Start " + commit.getShortMessage() + " " + commit.getFullMessage();
        String[] words = fullText.replace("\n", "").replace("\r", "").toLowerCase().split(" ");

        for(int i=0;i<words.length;i++) {
            for (String keyword:keywords) {
                if (words[i].contains(keyword) && !(stopWord.contains(words[i-1]))) {
                    for(int j=i+1;j<words.length;j++) {
                        if(stopWord.contains(words[j])) {
                            break;
                        }
                        if(words[j].replaceAll("[-'\',.+^/]*", "").matches("[#][Z0-9]*")) {
//                            System.out.println(words[i] + "  " + words[j]);
                            for(com.group4.softwareanalytics.issues.Issue issue:issueList) {
                                String[] urlString = issue.getIssue().getHtmlUrl().split("/");
                                if(Integer.parseInt(urlString[urlString.length -1]) == Integer.parseInt(words[j].substring(1))) {
                                    linkedIssues.add(issue.getIssue().getNumber());
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Integer>  linkedIssuesWithoutDuplicates = Lists.newArrayList(Sets.newHashSet(linkedIssues));
        return linkedIssuesWithoutDuplicates;
//        commit.setLinkedFixedIssues(linkedIssuesWithoutDuplicates);
    }

    public Repo fetchRepo(String owner, String name) throws IOException {
        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token("9a7ae8cd24203a8035b91d753326cabc6ade6eac");
        Repository r = service.getRepository(owner, name);
        Repo repo = new Repo(r, owner, name);
        return repo;
    }

    public List<Commit> fetchCommits(String owner, String repoName, Repo r) throws IOException, GitAPIException {
        String repo_url = "https://github.com/"+ owner +"/"+ repoName;
        String dest_url = "./repo/" + owner +"/"+ repoName;

        List<Commit> commitList = new ArrayList<Commit>();
        List<String> branches = new ArrayList<>();

        File dir = new File(dest_url);

        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }

        commitExtractor.DownloadRepo(repo_url, owner, repoName);


        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

        List<RevCommit> revCommitList;
        try (Git git = new Git(repo)) {

            List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

            for (Ref branch : refs) {
                branches.add(branch.getName());
            }

            revCommitList = commitExtractor.getCommits(branches.get(0), git, repo);


            for (Iterator<RevCommit> iterator = revCommitList.iterator(); iterator.hasNext(); ) {
                RevCommit revCommit = iterator.next();

                String developerName = revCommit.getAuthorIdent().getName();
                String developerMail = revCommit.getAuthorIdent().getEmailAddress();
                String encodingName = revCommit.getEncodingName();
                String fullMessage = revCommit.getFullMessage();
                String shortMessage = revCommit.getShortMessage();
                String commitName = revCommit.getName();

                ArrayList<String> commitParentsIDs = new ArrayList<>();
                for (RevCommit parent : revCommit.getParents()) {
                    commitParentsIDs.add(parent.name());
                }

                List<CommitDiff> diffEntries = new ArrayList<>();
                //            diffEntries = CommitExtractor.getModifications(git, commitName, dest_url, commitParentsIDs);

                ProjectMetric projectMetric = new ProjectMetric(0, 0, 0, 0, 0, 0, 0, 0);

                int commitType = revCommit.getType();
                long millis = revCommit.getCommitTime();
                Date date = new Date(millis * 1000);

                ArrayList<Integer> fixedIssues = fixedIssuesRelated(revCommit,issueList);

                Commit c = new Commit(diffEntries, owner, repoName, developerName, developerMail, encodingName, fullMessage, shortMessage, commitName, commitType, date, projectMetric, commitParentsIDs, false, fixedIssues);
                commitList.add(c);
            }
            commitRepository.saveAll(commitList);

            System.out.println("------- Commits fetched successfully! -------");
            r.hasCommitsDone();
            repoRepository.save(r);
            return commitList;
        } catch (Exception e){
            Logger logger = LogManager.getLogger(AsyncService.class.getName());
            logger.error(e.getMessage(),e);
            System.out.println(e);
            return commitList;
        }
    }

    //@Async
    public List<com.group4.softwareanalytics.issues.Issue> fetchIssues(String owner, String name, Repo repo) throws IOException {
        try {
            IssueService service = new IssueService();

            service.getClient().setOAuth2Token("ab6bd4f53af3a9a35077c9d06dfb48047240fe8e");

            List<Issue> issuesOpen = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));

            List<Issue> issuesClosed = service.getIssues(owner, name,
                    Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_CLOSED));

            List<Issue> issues = Stream.concat(issuesOpen.stream(), issuesClosed.stream())
                             .collect(Collectors.toList());

            System.out.println("Found " + issues.size() + " Issues, start fetching them...");
            List<com.group4.softwareanalytics.issues.Issue> issueList = new ArrayList<com.group4.softwareanalytics.issues.Issue>();

            for (Issue issue : issues) {
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
            System.out.println("------- Issues fetched successfully! -------");


            repo.hasIssuesDone();
            repoRepository.save(repo);
            return issueList;
        } catch (IOException e){
            Logger logger = LogManager.getLogger(AsyncService.class.getName());
            logger.error(e.getMessage(),e);
            return null;
        }

    }

}
