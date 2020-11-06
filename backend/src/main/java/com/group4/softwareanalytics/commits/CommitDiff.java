package com.group4.softwareanalytics.commits;
import com.group4.softwareanalytics.metrics.ProjectMetric;

public class CommitDiff {
    private String changeType;
    private String oldPath;
    private String newPath;
    private String diffs;
    private ProjectMetric metrics;

    public CommitDiff(String oldPath, String newPath, String changeType, String diffs, ProjectMetric metrics) {
        this.changeType = changeType;
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.diffs = diffs;
        this.metrics = metrics;
    }

    public ProjectMetric getMetrics() {
        return metrics;
    }

    public void setMetrics(ProjectMetric metrics) {
        this.metrics = metrics;
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
