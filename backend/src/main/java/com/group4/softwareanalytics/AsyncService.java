package com.group4.softwareanalytics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.group4.softwareanalytics.commits.*;
import com.group4.softwareanalytics.commits.Commit;
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
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
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
    private ArrayList<Commit> fixingCommits = new ArrayList<>();

    @Async
    public void fetchData(String owner, String name)  {
        try {
            fixingCommits.clear();
            issueList.clear();

            Repo repo = fetchRepo(owner,name);
            repoRepository.findAndRemove(owner,name);
            issueRepository.findAndRemove(owner,name);
            issueCommentRepository.findAndRemove(owner,name);
            commitRepository.findAndRemove(owner,name);

            repo.hasInfoDone();
            repoRepository.save(repo);

            try {
                fetchIssues(owner, name, repo);
            } catch (IOException e){
                Logger logger = LogManager.getLogger(AsyncService.class.getName());
                logger.error(e.getMessage(),e);
            }

            fetchCommits(owner, name, repo);

            computeSZZ(owner, name, repo);

        } catch (Exception ignored) {
        }
    }

    private void computeSZZ(String owner, String repoName, Repo r) throws IOException, GitAPIException {
        System.out.println("____________ "+ fixingCommits.size() +" FIXING COMMITS __________________");
        for (Commit commit : fixingCommits) {
            ArrayList<String> pathsModifiedFiles = new ArrayList<>();


            String dest_url = "./repo/" + commit.getOwner() +"/"+ commit.getRepo();
            org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");
            Git git = new Git(repo);
            List<CommitDiff> diffEntries = CommitExtractor.getModifications(git, commit.getCommitName(), dest_url, commit.getCommitParentsIDs());
            commit.setModifications(diffEntries);


            System.out.println("_____________________________________");
            System.out.println("COMMIT :" + commit.getCommitName());
            System.out.println("HEAD :" + commit.getShortMessage());
            System.out.println("RELATED ISSUES :" + commit.getLinkedFixedIssues().toString());


            for (CommitDiff modification : commit.getModifications()) {
                if (modification.getChangeType().equals("MODIFY")){
                    if (modification.getNewPath().endsWith(".java")){
                        pathsModifiedFiles.add(modification.getNewPath());
                    }
                }
            }
            System.out.println("MODIFIED FILES :" + pathsModifiedFiles);
            System.out.println("_____________________________________");
            System.out.println();

            for (String file : pathsModifiedFiles) {
//                String relativePath = "./repo/" + owner +"/"+ repoName + "/" + file;

                BlameResult blameResult = git.blame().setFilePath(file).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
                final RawText rawText = blameResult.getResultContents();
                for (int i = 0; i < rawText.size(); i++) {
                    final String sourceAuthor = blameResult.getSourceAuthor(i).getName();
                    final String commitHash = blameResult.getSourceCommit(i).name();
                    final Date date = new Date(blameResult.getSourceCommit(i).getCommitTime() * 1000);
                    System.out.println("++++++++++++++++++++++");
                    System.out.println(blameResult.getSourceLine(i));
                    System.out.println(sourceAuthor);
                    System.out.println(commitHash);
                    System.out.println(date);
                    System.out.println("++++++++++++++++++++++");
                }
            }
        }
    }

    private ArrayList<Integer> fixedIssuesRelated(RevCommit commit) {
        List<String> keywords = Arrays.asList("fix","fixes","fixed","close","closes","closed","resolve","resolves","resolved");
        List<String> stopWord = Arrays.asList("must","should","will");

        List<Integer> linkedIssues = new ArrayList<>();
        String fullText = commit.getShortMessage() + " " + commit.getFullMessage();
        String[] words = fullText.replace("\t", " ").replace("\n", " ").replace("\r", " ").toLowerCase().split(" ");


        for (int i=0; i<words.length; i++) {
            if (keywords.contains(words[i].replaceAll("[^a-zA-Z0-9]", ""))){
                System.out.print("fullText: ");
                System.out.println(fullText);
                System.out.print("words: ");
                System.out.println(Arrays.toString(words));
                if (i>0 && !(stopWord.contains(words[i-1].replaceAll("[^a-zA-Z0-9]", "")))) {
                    for(int j=i+1; j<words.length; j++) {
                        words[j] = words[j].replaceAll(",","");
                        if(words[j].matches("[#][0-9]+")) {
                            String relatedIssue = words[j].replaceAll("\\D+","");
                            System.out.print("relatedIssue: ");
                            System.out.println(relatedIssue);

                            for(com.group4.softwareanalytics.issues.Issue issue:issueList) {
                                String[] urlString = issue.getIssue().getHtmlUrl().split("/");
                                try{
                                    if(Integer.parseInt(urlString[urlString.length -1]) == Integer.parseInt(relatedIssue)) {
                                        linkedIssues.add(issue.getIssue().getNumber());
                                    }
                                } catch (Exception ignore){}
                            }
                        }
                    }
                }
            }
        }
        return Lists.newArrayList(Sets.newHashSet(linkedIssues));
    }

    public Repo fetchRepo(String owner, String name) throws IOException {
        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token("9a7ae8cd24203a8035b91d753326cabc6ade6eac");
        Repository r = service.getRepository(owner, name);
        Repo repo = new Repo(r, owner, name);
        return repo;
    }

    public void fetchCommits(String owner, String repoName, Repo r) throws IOException, GitAPIException {
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

                ProjectMetric projectMetric = new ProjectMetric(0, 0, 0, 0, 0, 0, 0, 0);

                int commitType = revCommit.getType();
                long millis = revCommit.getCommitTime();
                Date date = new Date(millis * 1000);

                ArrayList<Integer> fixedIssues = fixedIssuesRelated(revCommit);

                Commit c = new Commit(diffEntries, owner, repoName, developerName, developerMail, encodingName, fullMessage, shortMessage, commitName, commitType, date, projectMetric, commitParentsIDs, false, fixedIssues);
                commitList.add(c);
                if (!c.getLinkedFixedIssues().isEmpty()){
                    fixingCommits.add(c);
                }
            }
            commitRepository.saveAll(commitList);

            System.out.println("------- Commits stored successfully! -------");

            r.hasCommitsDone();
            repoRepository.save(r);
        } catch (Exception e){
            Logger logger = LogManager.getLogger(AsyncService.class.getName());
            logger.error(e.getMessage(),e);
            System.out.println(e);
        }
    }

    //@Async
    public void fetchIssues(String owner, String name, Repo repo) throws IOException {
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
        } catch (IOException e){
            Logger logger = LogManager.getLogger(AsyncService.class.getName());
            logger.error(e.getMessage(),e);
            issueList = null;
        }

    }

}
