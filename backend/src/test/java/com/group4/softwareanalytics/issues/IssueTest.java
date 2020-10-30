package com.group4.softwareanalytics.issues;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueTest {
    @Test
    void testIssueTest() {
        Issue i = new Issue(null, "owner", "repo");

        i.setId("myid");
        assertEquals(i.getId(), "myid");

        i.setOwner("my owner");
        assertEquals(i.getOwner(), "my owner");

        i.setRepo("my repo");
        assertEquals(i.getRepo(), "my repo");

        org.eclipse.egit.github.core.Issue issue = new org.eclipse.egit.github.core.Issue();
        i.setIssue(issue);
        assertEquals(i.getIssue(), issue);
    }

}