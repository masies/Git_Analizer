package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.CommitDiff;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommitDiffTest {

    @Test
    void testCommitDiff() {
        CommitDiff cf = new CommitDiff("oldpath", "newpath", "changetype", "diffs", null);
        ProjectMetric metrics = new ProjectMetric(1, 2, 3, 4, 1, 2, 3, 4);
        cf.setMetrics(metrics);
        assertEquals(cf.getMetrics(), metrics);

        cf.setDiffs("New Diffs");
        assertEquals(cf.getDiffs(), "New Diffs");

        cf.setChangeType("new change type");
        assertEquals(cf.getChangeType(), "new change type");

        cf.setOldPath("new oldpath");
        assertEquals(cf.getOldPath(), "new oldpath");

        cf.setNewPath("new newpath");
        assertEquals(cf.getNewPath(), "new newpath");
    }

}