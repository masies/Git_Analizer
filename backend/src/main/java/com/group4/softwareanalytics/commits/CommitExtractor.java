package com.group4.softwareanalytics.commits;

import com.github.mauricioaniche.ck.CK;
import com.group4.softwareanalytics.metrics.MetricResults;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class CommitExtractor {

    static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CommitExtractor.class.getName());

    public static void DownloadRepo(String repo_url, String destUrl) {
        try {
            Git.cloneRepository()
                    .setURI(repo_url)
                    .setDirectory(Paths.get(destUrl).toFile())
                    .call();
            logger.info("------- Repo cloned succesfully! -------");

        } catch (GitAPIException e){
            logger.info(e.getMessage());
        }
    }

    public static List<RevCommit> getCommits(String branchName, Git git, Repository repo)  {
        List<RevCommit> commitList = new ArrayList<>();
        try {
            for (RevCommit commit : git.log().add(repo.resolve(branchName)).call()) {
                commitList.add(commit);
            }
        } catch (GitAPIException | IOException e) {
            logger.info(e.getMessage());
        }
        return commitList;
    }

    public static List<CommitDiff> getModifications(Git git, String commitHashID, String path, ArrayList<String> commitParentsIDs) {
        List<CommitDiff> entriesList = new ArrayList<>();
        try {
            CanonicalTreeParser oldTreeIter;
            CanonicalTreeParser newTreeIter;
            try (ObjectReader reader = git.getRepository().newObjectReader()) {
                oldTreeIter = new CanonicalTreeParser();
                ObjectId oldTree = git.getRepository().resolve(commitHashID + "~1^{tree}");
                oldTreeIter.reset(reader, oldTree);
                newTreeIter = new CanonicalTreeParser();
                ObjectId newTree = git.getRepository().resolve(commitHashID + "^{tree}");
                newTreeIter.reset(reader, newTree);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DiffFormatter diffFormatter = new DiffFormatter(stream);

                diffFormatter.setRepository(git.getRepository());
                for (DiffEntry entry : diffFormatter.scan(oldTreeIter, newTreeIter)) {
                    diffFormatter.format(entry);
                    String diffText = stream.toString();

                    ArrayList<Float> currentMetrics = new ArrayList<>();
                    ArrayList<Float> parentMetrics = new ArrayList<>();

                    if (entry.getChangeType().name().equals("MODIFY") && entry.getNewPath().endsWith(".java")) {
                        String repoNewPath = path + "/" + entry.getNewPath();
                        String repoOldPath = path + "/" + entry.getOldPath();
                        git.checkout().setName(commitHashID).call();

                        MetricResults results = new MetricResults();
                        CK ck = new CK();
                        ck.calculate(repoNewPath, results);
                        currentMetrics = results.getResults();

                        if (commitParentsIDs.size() == 1) {
                            results = new MetricResults();
                            git.checkout().setName(commitParentsIDs.get(0)).call();
                            ck.calculate(repoOldPath, results);
                            parentMetrics = results.getResults();
                            git.checkout().setName(commitHashID).call();
                        }
                    }

                    ProjectMetric pm = new ProjectMetric(currentMetrics.get(0),
                            currentMetrics.get(1),
                            currentMetrics.get(2),
                            currentMetrics.get(3),
                            parentMetrics.get(0),
                            parentMetrics.get(1),
                            parentMetrics.get(2),
                            parentMetrics.get(3));

                    CommitDiff cd = new CommitDiff(entry.getOldPath(), entry.getNewPath(), entry.getChangeType().name(), diffText, pm);

                    entriesList.add(cd);
                    stream.reset();
                }
            } catch (Exception e){
//            logger.info(e.getMessage());
        }
        }
        catch (Exception ignore) {
            /* commits with no modification */
        }
        return entriesList;
    }
}
