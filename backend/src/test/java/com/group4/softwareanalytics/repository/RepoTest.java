package com.group4.softwareanalytics.repository;

import org.eclipse.egit.github.core.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepoTest {
    @Test
    public void testRepo(){
        Repo r = new Repo(null, "owner", "repo");

        r.setId("my Id");
        assertEquals("my Id", r.getId());

        r.setOwner("my Owner");
        assertEquals("my Owner", r.getOwner());

        r.setRepo("my Repo");
        assertEquals("my Repo", r.getRepo());

        RepoStatus rs = new RepoStatus();
        r.setStatus(rs);
        assertEquals(rs, r.getStatus());

        r.hasCommitsDone();
        r.hasInfoDone();
        r.hasIssuesDone();

        assertTrue(rs.getFetchedCommits());
        assertTrue(rs.getFetchedInfo());
        assertTrue(rs.getFetchedIssues());


        Repository re = new Repository();
        r.setRepository(re);
        assertEquals(re, r.getRepository());
    }
}