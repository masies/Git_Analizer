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
        assertEquals("my Owner", ic.getOwner());

        ic.setRepo("my Repo");
        assertEquals("my Repo", ic.getRepo());

        ic.setId("my Id");
        assertEquals("my Id", ic.getId());

        ic.setIssueNumber(321);
        assertEquals(321, ic.getIssueNumber());
    }
}