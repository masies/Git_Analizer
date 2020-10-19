package com.group4.softwareanalytics.issues.comments;

import org.eclipse.egit.github.core.Comment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueCommentTest {

    @Test
    public void testIssueComment() {
        IssueComment ic = new IssueComment(null, "owner", "repo", 123);
        Comment c = new Comment();

        ic.setComment(c);
        assertEquals(ic.getComment(), c);

        ic.setOwner("my Owner");
        assertEquals(ic.getOwner(), "my Owner");

        ic.setRepo("my Repo");
        assertEquals(ic.getRepo(), "my Repo");

        ic.setId("my Id");
        assertEquals(ic.getId(), "my Id");

        ic.setIssueNumber(321);
        assertEquals(ic.getIssueNumber(), 321);
    }
}