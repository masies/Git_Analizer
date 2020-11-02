
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
        assertEquals(c.getModifications(),cd);

        c.setId("id");
        assertEquals(c.getId(),"id");

        c.setOwner("owner");
        assertEquals(c.getOwner(),"owner");

        c.setRepo("repo");
        assertEquals(c.getRepo(),"repo");

        c.setDeveloperName("name");
        assertEquals(c.getDeveloperName(),"name");

        c.setDeveloperMail("mail");
        assertEquals(c.getDeveloperMail(),"mail");

        c.setEncodingName("ename");
        assertEquals(c.getEncodingName(),"ename");

        c.setFullMessage("fm");
        assertEquals(c.getFullMessage(),"fm");

        c.setShortMessage("sm");
        assertEquals(c.getShortMessage(),"sm");

        c.setCommitName("cn");
        assertEquals(c.getCommitName(),"cn");

        c.setCommitType(1);
        assertEquals(c.getCommitType(),1);

        c.setCommitDate(date);
        assertEquals(c.getCommitDate(),date);

        c.setProjectMetrics(pm);
        assertEquals(c.getProjectMetrics(),pm);

        c.setCommitParentsIDs(ids);
        assertEquals(c.getCommitParentsIDs(),ids);

        c.setHasMetrics(false);
        assertFalse(c.getHasMetrics());

    }
}
