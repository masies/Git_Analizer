package com.group4.softwareanalytics.issues;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueTest {
    @Test
    void testIssueTest() {
        Issue i = new Issue(null, "owner", "repo", false);

        i.setId("myid");
        assertEquals("myid", i.getId());

        i.setOwner("my owner");
        assertEquals("my owner", i.getOwner());

        i.setRepo("my repo");
        assertEquals("my repo", i.getRepo());

        org.eclipse.egit.github.core.Issue issue = new org.eclipse.egit.github.core.Issue();
        i.setIssue(issue);
        assertEquals(issue, i.getIssue());
    }

}