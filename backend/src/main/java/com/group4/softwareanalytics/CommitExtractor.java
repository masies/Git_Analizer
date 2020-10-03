package com.group4.softwareanalytics;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;


import java.io.*;
import java.nio.file.Paths;
import java.util.*;


public class CommitExtractor {

    public static void DownloadRepo(String url, String owner, String repoName) {
        String path = "./repo/" + owner +"/"+ repoName;
        try {
            System.out.println("Cloning "+ url +" into "+ path);
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(Paths.get(path).toFile())
                    .call();
            System.out.println("------- Repo cloned succesfully! -------");

        } catch (GitAPIException e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }
    }

    public static List<RevCommit> getCommits(String branchName, Git git, Repository repo) throws IOException, GitAPIException {
        List<RevCommit> commitList = new ArrayList<>();
        for (RevCommit commit : git.log().add(repo.resolve(branchName)).call()) {
            commitList.add(commit);
        }
        return commitList;
    }

    public static List<DiffEntry> getModifications(Git git, String commitID) {
        List<DiffEntry> entriesList = new ArrayList<>();
        try {
            ObjectReader reader = git.getRepository().newObjectReader();
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            ObjectId oldTree = git.getRepository().resolve(commitID + "~1^{tree}");
            oldTreeIter.reset(reader, oldTree);
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            ObjectId newTree = git.getRepository().resolve(commitID + "^{tree}");
            newTreeIter.reset(reader, newTree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            List<DiffEntry> entries = git.diff().setOutputStream(stream)
                    .setOldTree(oldTreeIter)
                    .setNewTree(newTreeIter)
                    .call();
            entriesList.addAll(entries);
        }
        catch (Exception e)
        {
            /* commits with no modification */
        }

        return entriesList;
    }

    public static String getDiffComb(Git git, String commitID) throws IOException, GitAPIException {
        String diffCombine = " ";
        try {
            ObjectReader reader = git.getRepository().newObjectReader();
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            ObjectId oldTree = git.getRepository().resolve(commitID + "~1^{tree}");
            oldTreeIter.reset(reader, oldTree);
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            ObjectId newTree = git.getRepository().resolve(commitID + "^{tree}");
            newTreeIter.reset(reader, newTree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            git.diff().setOutputStream(stream)
                    .setOldTree(oldTreeIter)
                    .setNewTree(newTreeIter)
                    .call();
            diffCombine = new String(stream.toByteArray());
        } catch (Exception e) {
            // commits with no modification
        }

        return diffCombine;
    }
}
