package com.group4.softwareanalytics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.group4.softwareanalytics.developer.DeveloperExpertise;
import com.group4.softwareanalytics.developer.DeveloperExpertiseRepository;
import com.group4.softwareanalytics.developer.DeveloperPR;
import com.group4.softwareanalytics.developer.DeveloperPRRepository;
import com.group4.softwareanalytics.commits.*;
import com.group4.softwareanalytics.issues.IssueRepository;
import com.group4.softwareanalytics.issues.comments.IssueComment;
import com.group4.softwareanalytics.issues.comments.IssueCommentRepository;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncService {

    @Autowired
    private DeveloperExpertiseRepository developerExpertiseRepository;

    @Autowired
    private DeveloperPRRepository developerPRRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @Autowired
    private CommitRepository commitRepository;

    // list of developer expertise
    private ArrayList<DeveloperExpertise> developerExpertiseList = new ArrayList<>();

    // list of developer expertise
    private ArrayList<DeveloperPR> developerPRRatesList = new ArrayList<>();

    // list of issues, retrieved in fetchIssues and used by szz
    private List<com.group4.softwareanalytics.issues.Issue> issueList = new ArrayList<>();

    // list of fixingCommits, retrieved in fetchCommits and used by szz
    private ArrayList<Commit> fixingCommits = new ArrayList<>();

    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AsyncService.class.getName());

    private static final String repoFolderPath = "./repo/";

    @Async
    public void fetchData(String owner, String name){
        try {
            // clear the two lists for each new repo to mine
            fixingCommits.clear();
            issueList.clear();
            developerPRRatesList.clear();
            developerExpertiseList.clear();
            // clean existing data
            repoRepository.findAndRemove(owner,name);
            issueRepository.findAndRemove(owner,name);
            issueCommentRepository.findAndRemove(owner,name);
            commitRepository.findAndRemove(owner,name);
            developerExpertiseRepository.findAndRemove(owner,name);
            developerPRRepository.findAndRemove(owner,name);


            // mining repo
            Repo repo = fetchRepo(owner,name);
            fetchIssues(owner, name, repo);
            fetchCommits(owner, name, repo);
            computeSZZ(owner, name);

            developerExpertiseRepository.saveAll(developerExpertiseList);
            developerPRRepository.saveAll(developerPRRatesList);

        } catch (Exception e){
            logger.warning(e.getMessage());
        }
    }

    public Repo fetchRepo(String owner, String name) throws IOException {
        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token("9a7ae8cd24203a8035b91d753326cabc6ade6eac");
        Repository r = service.getRepository(owner, name);
        Repo repo = new Repo(r, owner, name);
        repo.hasInfoDone();
        repoRepository.save(repo);
        return repo;
    }

    public void fetchIssues(String owner, String repoName, Repo repo) throws IOException {
        IssueService service = new IssueService();

        service.getClient().setOAuth2Token("ab6bd4f53af3a9a35077c9d06dfb48047240fe8e");
        List<Issue> issuesOpen = service.getIssues(owner, repoName,
                Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));

        List<Issue> issuesClosed = service.getIssues(owner, repoName,
                Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_CLOSED));

        List<Issue> issues = Stream.concat(issuesOpen.stream(), issuesClosed.stream())
                .collect(Collectors.toList());

        logger.log(Level.ALL, "------- Found " + issues.size() + " Issues, start fetching them... -------" );

        for (Issue issue : issues) {
            com.group4.softwareanalytics.issues.Issue i = new com.group4.softwareanalytics.issues.Issue(issue, owner, repoName, false);

            if(issue.getHtmlUrl().contains("pull")) {
                i.setPR(true);
                if(issue.getState().equals("closed")) {
                    linkIssueDev(owner, repoName ,issue.getUser().getLogin(), issue.getNumber());
                }
            }

            issueList.add(i);
            // gather all the issue comments
            List<Comment> comments = service.getComments(owner, repoName, issue.getNumber());
            List<IssueComment> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                IssueComment c = new IssueComment(comment, owner, repoName, issue.getNumber());
                commentList.add(c);
            }
            issueCommentRepository.saveAll(commentList);
        }

        issueRepository.saveAll(issueList);

        logger.info("------- Issues fetched successfully! -------");
        repo.hasIssuesDone();
        repoRepository.save(repo);
    }

     private void linkIssueDev(String owner, String repoName, String userName, int PRnumber){
        ProcessBuilder curlPRProcess = new ProcessBuilder(
                "curl", "-X", "GET", "https://api.github.com/repos/" + owner + "/" + repoName+ "/pulls/" + PRnumber,
                "-H", "Authorization: Bearer 9a7ae8cd24203a8035b91d753326cabc6ade6eac");

        JsonObject jsonPR = curlRequest(curlPRProcess);

        if (jsonPR == null){
            return;
        }

        String merged = jsonPR.get("merged").toString();

        if (merged.equals("true")){
            // in this case the PR is accepted, we will increment the accepted count of the owner
            // then we need to add all the reviewers, increment all it's reviewed count and accepted reviewed count

            String mergedBy = jsonPR.getAsJsonObject("merged_by").get("login").toString().replaceAll("\"","");

            // TODO: ask if we have to consider this
    //      HashSet<String> reviewers;
    //      JsonArray jsonReviewers = jsonPR.getAsJsonArray("requested_reviewers");
    //      System.out.println(jsonReviewers.toString());
    //      for (int i = 0; i < jsonReviewers.size(); i++) {
    //          String reviewer = jsonReviewers.get(i).getAsJsonObject().get("login").toString().replaceAll("\"","");
    //          System.out.println(reviewer);
    //      }

            boolean userFound = false;
            boolean reviewerFound = false;

            for (DeveloperPR dev : developerPRRatesList) {
                // the developer which opened the PR gets it's PR total and accepeted total increased by 1
                if (dev.getUsername().equals(userName)) {
                    dev.setOpened(dev.getOpened()+1);
                    dev.setAccepted_opened(dev.getAccepted_opened() + 1);
                    dev.addPROpened(PRnumber);
                    userFound = true;
                }
                // the developer which approved the PR gets it's PR reviewed total increased by 1
                // and the accepted reviewed total increased by 1
                if (dev.getUsername().equals(mergedBy)) {
                    dev.setReviewed(dev.getReviewed()+1);
                    dev.setAccepted_reviewed(dev.getAccepted_reviewed() + 1);
                    dev.addPRreviewed(PRnumber);
                    reviewerFound = true;
                }
                if(userFound && reviewerFound){
                    return;
                }
            }

            if (!userFound && !reviewerFound){
                if (mergedBy.equals(userName)){
                    DeveloperPR newDev = new DeveloperPR(owner,repoName,userName);
                    newDev.setOpened(1);
                    newDev.setAccepted_opened(1);
                    newDev.setReviewed(1);
                    newDev.setAccepted_reviewed(1);
                    newDev.addPROpened(PRnumber);
                    newDev.addPRreviewed(PRnumber);
                    developerPRRatesList.add(newDev);
                    return;
                }
            }

            if (!userFound){
                DeveloperPR newDev = new DeveloperPR(owner,repoName,userName);
                newDev.setOpened(1);
                newDev.setAccepted_opened(1);
                newDev.addPROpened(PRnumber);
                developerPRRatesList.add(newDev);
            }

            if (!reviewerFound){
                DeveloperPR newDev = new DeveloperPR(owner,repoName,mergedBy);
                newDev.setReviewed(1);
                newDev.setAccepted_reviewed(1);
                newDev.addPRreviewed(PRnumber);
                developerPRRatesList.add(newDev);
            }


        } else {
            // we do not have the closed_by info in the pull request fetched as pull
            // hence we need to fetch it as issue
            ProcessBuilder curlIssueProcess = new ProcessBuilder(
                    "curl", "-X", "GET", "https://api.github.com/repos/" + owner + "/" + repoName + "/issues/" + PRnumber,
                    "-H", "Authorization: Bearer 9a7ae8cd24203a8035b91d753326cabc6ade6eac");

            JsonObject jsonIssue = curlRequest(curlIssueProcess);

            if (jsonIssue == null){
                return;
            }

            String closedBy = jsonIssue.getAsJsonObject("closed_by").get("login").toString().replaceAll("\"", "");

            boolean userFound = false;
            boolean reviewerFound = false;

            for (DeveloperPR dev : developerPRRatesList) {
                // the developer which opened the PR gets it's PR total increased by 1, but not the accepted total
                if (dev.getUsername().equals(userName)) {
                    dev.setOpened(dev.getOpened()+1);
                    dev.addPROpened(PRnumber);
                    userFound = true;
                }
                // the developer which closed the PR gets it's PR reviewed total increased by 1
                if (dev.getUsername().equals(closedBy)) {
                    dev.setReviewed(dev.getReviewed()+1);
                    dev.addPRreviewed(PRnumber);
                    reviewerFound = true;
                }
                if(userFound && reviewerFound){
                    return;
                }
            }

            if (!userFound && !reviewerFound){
                if (closedBy.equals(userName)){
                    DeveloperPR newDev = new DeveloperPR(owner,repoName,userName);
                    newDev.setOpened(1);
                    newDev.setReviewed(1);
                    newDev.addPROpened(PRnumber);
                    newDev.addPRreviewed(PRnumber);
                    developerPRRatesList.add(newDev);
                    return;
                }
            }

            if (!userFound){
                DeveloperPR newDev = new DeveloperPR(owner,repoName,userName);
                newDev.setOpened(1);
                newDev.addPROpened(PRnumber);
                developerPRRatesList.add(newDev);
            }

            if (!reviewerFound){
                DeveloperPR newDev = new DeveloperPR(owner,repoName,closedBy);
                newDev.setReviewed(1);
                newDev.addPRreviewed(PRnumber);
                developerPRRatesList.add(newDev);
            }
        }

    }

    private JsonObject curlRequest(ProcessBuilder pb){
        Process process = null;
        try {
            process = pb.start();
            process.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = stdInput.lines().collect(Collectors.joining());
            return JsonParser.parseString(s).getAsJsonObject();
        } catch (IOException | InterruptedException e) {
            assert process != null;
            process.destroy();
            logger.warning(e.getMessage());
            Thread.currentThread().interrupt();
        }

        return null;
    }

    public void fetchCommits(String owner, String repoName, Repo r) throws IOException {
        String repo_url = "https://github.com/" + owner + "/" + repoName;
        String dest_url = repoFolderPath + owner + "/" + repoName;

        List<Commit> commitList = new ArrayList<>();
        List<String> branches = new ArrayList<>();

        File dir = new File(dest_url);
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }

        CommitExtractor.DownloadRepo(repo_url, dest_url);

        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

        List<RevCommit> revCommitList;
        try (Git git = new Git(repo)) {
            List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref branch : refs) {
                branches.add(branch.getName());
            }
            // focus on master branch
            revCommitList = CommitExtractor.getCommits(branches.get(0), git, repo);

            for (RevCommit revCommit : revCommitList) {
                String developerName = revCommit.getAuthorIdent().getName();
                String developerMail = revCommit.getAuthorIdent().getEmailAddress();
                String encodingName = revCommit.getEncodingName();
                String fullMessage = revCommit.getFullMessage();
                String shortMessage = revCommit.getShortMessage();
                String commitName = revCommit.getName();
                int commitType = revCommit.getType();

                long millis = revCommit.getCommitTime();
                Date date = new Date(millis * 1000);
                List<CommitDiff> diffEntries = new ArrayList<>();
                ProjectMetric projectMetric = new ProjectMetric(0, 0, 0, 0, 0, 0, 0, 0);

                ArrayList<String> commitParentsIDs = new ArrayList<>();
                for (RevCommit parent : revCommit.getParents()) {
                    commitParentsIDs.add(parent.name());
                }

                ArrayList<Integer> fixedIssues = fixedIssuesRelated(revCommit);

                linkCommitDev(owner, repoName, revCommit.getAuthorIdent().getEmailAddress(), commitName);

                Commit c = new Commit(diffEntries, owner, repoName, developerName, developerMail, encodingName, fullMessage, shortMessage, commitName, commitType, date, projectMetric, commitParentsIDs, false, fixedIssues);
                commitList.add(c);

                // if it is a fixing commit we save it for a later use in szz
                if (!c.getLinkedFixedIssues().isEmpty()) {
                    fixingCommits.add(c);
                }
            }
            commitRepository.saveAll(commitList);

            logger.info("------- Commits stored successfully! -------");

            r.hasCommitsDone();
            repoRepository.save(r);
        } catch (Exception e){
            logger.warning(e.getMessage());
        }
    }

    private void linkCommitDev(String owner, String repo, String devEmail, String commitHash) {
        for (DeveloperExpertise dev : developerExpertiseList) {   // CHECK IF DEV EXISTS
            if (dev.getEmail().equals(devEmail)) {
                dev.setExpertise(dev.getExpertise() + 1);
                dev.addCommitHash(commitHash);
                return;
            }
        }

        DeveloperExpertise newDev = new DeveloperExpertise(owner, repo,1, devEmail);
        newDev.addCommitHash(commitHash);
        developerExpertiseList.add(newDev);
    }

    private ArrayList<Integer> fixedIssuesRelated(RevCommit commit) {
        List<String> keywords = Arrays.asList("fix","fixes","fixed","close","closes","closed","resolve","resolves","resolved");
        List<String> stopWord = Arrays.asList("must","should","will");

        List<Integer> linkedIssues = new ArrayList<>();
        String fullText = commit.getShortMessage() + " " + commit.getFullMessage();
        String[] words = fullText.replace("\t", " ").replace("\n", " ").replace("\r", " ").toLowerCase().split(" ");

        for (int i=0; i<words.length; i++) {
            if (keywords.contains(words[i].replaceAll("[^a-zA-Z0-9]", ""))){
                if (i>0 && !(stopWord.contains(words[i-1].replaceAll("[^a-zA-Z0-9]", "")))) {
                    for(int j=i+1; j<words.length; j++) {
                        words[j] = words[j].replaceAll(",","");
                        if(words[j].matches("[#][0-9]+")) {
                            String relatedIssue = words[j].replaceAll("\\D+","");
                            for(com.group4.softwareanalytics.issues.Issue issue:issueList) {
                                if (!issue.getPR()) {
                                    String[] urlString = issue.getIssue().getHtmlUrl().split("/");
                                    try {
                                        if (Integer.parseInt(urlString[urlString.length - 1]) == Integer.parseInt(relatedIssue)) {
                                            linkedIssues.add(issue.getIssue().getNumber());
                                        }
                                    } catch (Exception ignore) {
                                        // abnormal
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // no duplicates
        return Lists.newArrayList(Sets.newHashSet(linkedIssues));
    }

    public void computeSZZ(String owner, String repoName) throws IOException, GitAPIException {
        for (Commit commit : fixingCommits) {

            // CHECKOUT this specific commit
            String dest_url = repoFolderPath + commit.getOwner() +"/"+ commit.getRepo();
            org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

            HashSet<String> bugInducingCommitsHashSet;
            try (Git git = new Git(repo)) {
                git.checkout().setName(commit.getCommitName()).call();

                // compute modified files
                List<CommitDiff> diffEntries = CommitExtractor.getModifications(git, commit.getCommitName(), dest_url, commit.getCommitParentsIDs());
                commit.setModifications(diffEntries);

                // java file --> list of modified lines
                HashMap<String, ArrayList<Integer>> modifiedLinesPerJavaClass = new HashMap<>();

                for (CommitDiff modification : commit.getModifications()) {
                    if (modification.getChangeType().equals("MODIFY") && modification.getNewPath().endsWith(".java")) {
                        ArrayList<Integer> deletedLines = new ArrayList<>();
                        String diffs = modification.getDiffs();
                        String reg = "@@(\\s[-,+]\\d+[,]\\d+)+\\s@@";
                        Pattern pattern = Pattern.compile(reg);
                        ArrayList<String> chunksHeader = new ArrayList<>();
                        Matcher matcher = pattern.matcher(diffs);

                        while (matcher.find()) {
                            chunksHeader.add(matcher.group());
                        }

                        String[] diffsChunks = diffs.split(reg);
                        for (int i = 1; i < diffsChunks.length; i++) {

                            String header = chunksHeader.get(i - 1);
                            ArrayList<Integer> startAndCont = new ArrayList<>();
                            String reg2 = "\\d+";
                            Pattern pattern2 = Pattern.compile(reg2);
                            Matcher matcher2 = pattern2.matcher(header);

                            while (matcher2.find()) {
                                startAndCont.add(Integer.parseInt(matcher2.group()));
                            }
                            int start = startAndCont.get(0);

                            // first chunk block
                            String codeBlock = diffsChunks[i];
                            String[] diffLines = codeBlock.split("\\r?\\n");

                            int count = 0;
                            for (String diffLine : diffLines) {
                                if (diffLine.startsWith("-")) {
                                    deletedLines.add(start + count - 1);
                                } else if (diffLine.startsWith("+")) {
                                    count = count - 1;
                                }
                                count = count + 1;
                            }
                        }
                        modifiedLinesPerJavaClass.put(modification.getNewPath(), deletedLines);
                    }
                }

                bugInducingCommitsHashSet = new HashSet<>();

                // checkout parent
                if (commit.getCommitParentsIDs().size() == 1) {
                    git.checkout().setName(commit.getCommitParentsIDs().get(0)).call();
                } else {
                    // in case we have multiple parents e.g., merge commits, we just skip
                    continue;
                }

                for (Map.Entry<String, ArrayList<Integer>> entry : modifiedLinesPerJavaClass.entrySet()) {
                    String file = entry.getKey();
                    ArrayList<Integer> deletedLines = entry.getValue();

                    String relativePath = repoFolderPath + owner + "/" + repoName + "/" + file;
                    ArrayList<Integer> codeLines = LOCExtractor.extractLines(relativePath);

                    BlameResult blameResult = git.blame().setFilePath(file).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
                    final RawText rawText = blameResult.getResultContents();

                    for (int i = 0; i < rawText.size(); i++) {
                        if (codeLines.contains(i) && deletedLines.contains(i)) {
                            final String commitHash = blameResult.getSourceCommit(i).name();
                            bugInducingCommitsHashSet.add(commitHash);
                        }
                    }
                }
            }

            HashSet<CommitGeneralInfo> bugInducingCommitsSet= new HashSet<>();

            for (String bugInducingCommitId: bugInducingCommitsHashSet) {
                Commit bugInducingCommit = commitRepository.findByOwnerAndRepoAndCommitName(owner,repoName,bugInducingCommitId);

                bugInducingCommit.setBugInducing(true);
                CommitGeneralInfo info = new CommitGeneralInfo(commit.getCommitName(), commit.getDeveloperName(), commit.getCommitDate());
                bugInducingCommit.addBugFixingCommit(info);
                commitRepository.save(bugInducingCommit);

                bugInducingCommitsSet.add(new CommitGeneralInfo(bugInducingCommit.getCommitName(),bugInducingCommit.getDeveloperName(),bugInducingCommit.getCommitDate()));
            }

            if (bugInducingCommitsSet.isEmpty()){
                commit.setBugInducingCommits(null);
            } else {
                commit.setBugInducingCommits(bugInducingCommitsSet);
            }

            commitRepository.save(commit);

        }
        logger.info("------- SZZ completed. -------");
    }
}
