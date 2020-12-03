package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitController;
import com.group4.softwareanalytics.commits.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TraingSetBuilder {

    private ArrayList<CommitEntry> commitsEntries = new ArrayList<>();

    public void addCommitEntry(CommitEntry c) {
        this.commitsEntries.add(c);
    }

    public ArrayList<CommitEntry> getCommits() {
        return commitsEntries;
    }

    public void setCommits(ArrayList<CommitEntry> commitEntries) {
        this.commitsEntries = commitEntries;
    }

    public void resume(){
        for (CommitEntry ce: this.commitsEntries) {
            System.out.println("Hash: " +ce.getCommitHash());
            System.out.println("developer mail: " +ce.getDeveloperMail());

            System.out.println("added files: " +ce.getAddedFileCount());
            System.out.println("deleted files: " +ce.getDeleteFileCount());
            System.out.println("modified files: " +ce.getModifiedFileCount());
            System.out.println("added lines: " +ce.getAddedLinesCount());
            System.out.println("deleted lines: " +ce.getDeletedLinesCount());
            System.out.println("dev absolute expertise: " +ce.getDeveloperAbsoluteExperience());
            System.out.println("dev average expertise: " +ce.getDeveloperAverageExperience());
            System.out.println("dev buggy ratio: " +ce.getDeveloperBuggyCommitsRatio());
            System.out.println("commits last month: " +ce.getDeveloperTotalCommitsLastMont());

            System.out.println("BUGGY: " + ce.isBuggy());
            System.out.println();
        }
    }


    public void setBuggyCommit(String hash){
        for (CommitEntry ce: this.commitsEntries) {
            if (ce.getCommitHash().equals(hash)){
                ce.setBuggy(true);
            }
        }
    }

    public void computeFinalMetrics() {

        for (int i = this.commitsEntries.size() - 1; i >= 1 ; i--) {
            CommitEntry ce = this.commitsEntries.get(i);
            String developer = ce.getDeveloperMail();
            float devCommitsCount = 0;
            float devBuggyCommitsCount = 0;
            for (int j = i - 1 ; j >=0 ; j--) {
                CommitEntry nextCe = this.commitsEntries.get(j);
                int nextExpertise = nextCe.getDeveloperAbsoluteExperience();
                String nextDeveloper = nextCe.getDeveloperMail();
                if (developer.equals(nextDeveloper)){
                    devCommitsCount += 1;
                    if (nextCe.isBuggy()){
                        devBuggyCommitsCount += 1;
                    }
                }
            }

            if (devBuggyCommitsCount != 0){
                ce.setDeveloperBuggyCommitsRatio(devBuggyCommitsCount/devCommitsCount);
            }

        }

    }
}
