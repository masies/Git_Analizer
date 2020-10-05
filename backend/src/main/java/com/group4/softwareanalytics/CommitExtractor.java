package com.group4.softwareanalytics;

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

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_ENCODING;


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

    public static List<CommitDiff> getModifications(Git git, String commit) {
        List<CommitDiff> entriesList = new ArrayList<>();
        try {
            ObjectReader reader = git.getRepository().newObjectReader();
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            ObjectId oldTree = git.getRepository().resolve(commit + "~1^{tree}");
            oldTreeIter.reset(reader, oldTree);
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            ObjectId newTree = git.getRepository().resolve(commit + "^{tree}");
            newTreeIter.reset(reader, newTree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(stream);

            df.setRepository( git.getRepository() );
            for (DiffEntry entry:df.scan( oldTreeIter, newTreeIter )) {
                df.format(entry);
                String diffText = stream.toString(DEFAULT_ENCODING);
                CommitDiff cd = new CommitDiff(entry.getOldPath(), entry.getNewPath(), entry.getChangeType().name(), diffText);
                entriesList.add(cd);
                stream.reset();
            }
        }
        catch (Exception e) {
            /* commits with no modification */
        }
        return entriesList;
    }
}
