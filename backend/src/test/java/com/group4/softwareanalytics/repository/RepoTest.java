package com.group4.softwareanalytics.repository;

import org.eclipse.egit.github.core.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepoTest {
    @Test
    public void testRepo(){
        Repo r = new Repo(null, "owner", "repo");

        r.setId("my Id");
        assertEquals(r.getId(), "my Id");

        r.setOwner("my Owner");
        assertEquals(r.getOwner(), "my Owner");

        r.setRepo("my Repo");
        assertEquals(r.getRepo(), "my Repo");

        RepoStatus rs = new RepoStatus();
        r.setStatus(rs);
        assertEquals(r.getStatus(), rs);

        r.hasCommitsDone();
        r.hasInfoDone();
        r.hasIssuesDone();

        assertTrue(rs.getFetchedCommits());
        assertTrue(rs.getFetchedInfo());
        assertTrue(rs.getFetchedIssues());


        Repository re = new Repository();
        r.setRepository(re);
        assertEquals(r.getRepository(), re);
    }
}