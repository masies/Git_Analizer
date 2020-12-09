package com.group4.softwareanalytics;

import java.util.*;

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

    public ArrayList<CommitEntry> exportTrainingSet(){
        Collections.sort(this.commitsEntries, Collections.reverseOrder());
        ArrayList<CommitEntry> toReturn = new ArrayList<>();
        for (int i = 10; i < this.commitsEntries.size() ; i++) {
            toReturn.add(this.commitsEntries.get(i));
        }
        return  toReturn;
    }

    public ArrayList<CommitEntry> exportPredictionSet(){
        Collections.sort(this.commitsEntries, Collections.reverseOrder());
        ArrayList<CommitEntry> toReturn = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            toReturn.add(this.commitsEntries.get(i));
        }
        return  toReturn;
    }


    public void setBuggyCommit(String hash){
        for (CommitEntry ce: this.commitsEntries) {
            if (ce.getCommitHash().equals(hash)){
                ce.setBuggy(true);
            }
        }
    }

    public void computeFinalMetrics() {
        Collections.sort(this.commitsEntries, Collections.reverseOrder());
        for (int i = this.commitsEntries.size() - 1; i >= 1 ; i--) {
            CommitEntry ce = this.commitsEntries.get(i);
            String developer = ce.getDeveloperMail();
            float devCommitsCount = 0;
            float devBuggyCommitsCount = 0;
            for (int j = i - 1 ; j >=0 ; j--) {
                CommitEntry nextCe = this.commitsEntries.get(j);
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

        for (int i = this.commitsEntries.size() - 1; i >= 1; i--) {
            Collections.sort(this.commitsEntries, Collections.reverseOrder());
            CommitEntry ce = this.commitsEntries.get(i);
            String dev = ce.getDeveloperMail();
            ArrayList<Pair<String,String>> contribs = ce.getContibutions();
            for (int j = i - 1; j >= 0; j--) {
                CommitEntry nextCe = this.commitsEntries.get(j);
                String nextDev = nextCe.getDeveloperMail();
                ArrayList<Pair<String, String>> nextContribs = ce.getContibutions();
                for (Pair<String, String> pair : nextContribs) {
                    if (contribs.contains(pair) && dev.equals(nextDev)) {
                        ce.setDeveloperAverageExperience(ce.getDeveloperAverageExperience() + 1);
                    }
                }
                if (contribs.size() > 0) {
                    ce.setDeveloperAverageExperience(ce.getDeveloperAverageExperience() / contribs.size());
                }
            }
        }

    }
}
