
package com.group4.softwareanalytics;


import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitDiff;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc
public class CommitTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AsyncService asyncService;
    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private RepoRepository repoRepository;

    @Test
    void testProjectMetrics() throws IOException, GitAPIException, InvocationTargetException, IllegalAccessException {

        List<CommitDiff> cd = new ArrayList<>();
        Date date = new Date();
        ProjectMetric pm = new ProjectMetric(0,0,0,0,0,0,0,0);
        ArrayList<String> ids = new ArrayList<>();

        Commit c = new Commit(null,"owner","repo","developer", "developerMail","encodingName",
                "fullMessage","shortMessage","commitName",0,null,null,null,true,null);

        c.setModifications(cd);

        c.setId("id");
        c.setOwner("owner");
        c.setRepo("repo");
        c.setDeveloperName("name");
        c.setDeveloperMail("mail");
        c.setEncodingName("ename");
        c.setFullMessage("fm");
        c.setShortMessage("sm");
        c.setCommitName("cn");
        c.setCommitType(1);
        c.setCommitDate(date);
        c.setProjectMetrics(pm);
        c.setCommitParentsIDs(ids);

        assertEquals(cd, c.getModifications());
        assertEquals("id", c.getId());
        assertEquals("owner", c.getOwner());
        assertEquals("repo", c.getRepo());
        assertEquals("name", c.getDeveloperName());
        assertEquals("mail", c.getDeveloperMail());
        assertEquals("ename", c.getEncodingName());
        assertEquals("fm", c.getFullMessage());
        assertEquals("sm", c.getShortMessage());
        assertEquals("cn", c.getCommitName());
        assertEquals(1, c.getCommitType());
        assertEquals(date, c.getCommitDate());
        assertEquals(pm, c.getProjectMetrics());
        assertEquals(ids, c.getCommitParentsIDs());

        c.setHasMetrics(false);
        assertFalse(c.getHasMetrics());

    }
}
