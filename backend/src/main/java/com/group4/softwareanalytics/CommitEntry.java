package com.group4.softwareanalytics;

public class CommitEntry {
    private int addedFileCount = 0;
    private int modifiedFileCount = 0;
    private int deleteFileCount = 0;
    private int addedLinesCount = 0;
    private int deletedLinesCount = 0;

//    The absolute experience of the developer authoring the commit computed at the commit’s date (1 pre- dictor variable);
    private int developerAbsoluteExperience = 0;

//    The average absolute experience of the developer authoring the commit on the files impacted by the commit,
//    e.g., given a commit impacting files Fj and Fk, the platform counts the number of commits performed in the past by the
//    developer on Fj and Fk and computes the average (1 predictor variable).
    private int developerAverageExperience = 0;

//     The ratio of buggy commits authored by the developer in the past,
//     e.g.,if she committed 4 times, and 1 of these commits was buggy, this variable is equal 0.25 (1 predictor variable).
    private int developerBuggyCommitsRatio = 0;

//    The number of commits performed in the previous month by the developer authoring the commit (1 predictor variable).
    private int developerTotalCommitsLastMont = 0;

    public CommitEntry(int addedFileCount, int modifiedFileCount, int deleteFileCount, int addedLinesCount, int deletedLinesCount, int developerAbsoluteExperience, int developerAverageExperience, int developerBuggyCommitsRatio, int developerTotalCommitsLastMont) {
        this.addedFileCount = addedFileCount;
        this.modifiedFileCount = modifiedFileCount;
        this.deleteFileCount = deleteFileCount;
        this.addedLinesCount = addedLinesCount;
        this.deletedLinesCount = deletedLinesCount;
        this.developerAbsoluteExperience = developerAbsoluteExperience;
        this.developerAverageExperience = developerAverageExperience;
        this.developerBuggyCommitsRatio = developerBuggyCommitsRatio;
        this.developerTotalCommitsLastMont = developerTotalCommitsLastMont;
    }

    public int getAddedFileCount() {
        return addedFileCount;
    }

    public void setAddedFileCount(int addedFileCount) {
        this.addedFileCount = addedFileCount;
    }

    public int getModifiedFileCount() {
        return modifiedFileCount;
    }

    public void setModifiedFileCount(int modifiedFileCount) {
        this.modifiedFileCount = modifiedFileCount;
    }

    public int getDeleteFileCount() {
        return deleteFileCount;
    }

    public void setDeleteFileCount(int deleteFileCount) {
        this.deleteFileCount = deleteFileCount;
    }

    public int getAddedLinesCount() {
        return addedLinesCount;
    }

    public void setAddedLinesCount(int addedLinesCount) {
        this.addedLinesCount = addedLinesCount;
    }

    public int getDeletedLinesCount() {
        return deletedLinesCount;
    }

    public void setDeletedLinesCount(int deletedLinesCount) {
        this.deletedLinesCount = deletedLinesCount;
    }

    public int getDeveloperAbsoluteExperience() {
        return developerAbsoluteExperience;
    }

    public void setDeveloperAbsoluteExperience(int developerAbsoluteExperience) {
        this.developerAbsoluteExperience = developerAbsoluteExperience;
    }

    public int getDeveloperAverageExperience() {
        return developerAverageExperience;
    }

    public void setDeveloperAverageExperience(int developerAverageExperience) {
        this.developerAverageExperience = developerAverageExperience;
    }

    public int getDeveloperBuggyCommitsRatio() {
        return developerBuggyCommitsRatio;
    }

    public void setDeveloperBuggyCommitsRatio(int developerBuggyCommitsRatio) {
        this.developerBuggyCommitsRatio = developerBuggyCommitsRatio;
    }

    public int getDeveloperTotalCommitsLastMont() {
        return developerTotalCommitsLastMont;
    }

    public void setDeveloperTotalCommitsLastMont(int developerTotalCommitsLastMont) {
        this.developerTotalCommitsLastMont = developerTotalCommitsLastMont;
    }
}
