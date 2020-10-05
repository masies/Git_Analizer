package com.group4.softwareanalytics;

import org.eclipse.jgit.diff.DiffEntry;

public class CommitDiff {

    private String changeType;
    private String oldPath;
    private String newPath;
    private String diffs;

    public CommitDiff(String oldPath, String newPath, String changeType, String diffs) {
        this.changeType = changeType;
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.diffs = diffs;
    }

    public String getDiffs() {
        return diffs;
    }

    public void setDiffs(String diffs) {
        this.diffs = diffs;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }
}
