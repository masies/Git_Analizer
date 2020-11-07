
package com.group4.softwareanalytics;


import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitDiff;
import com.group4.softwareanalytics.commits.CommitExtractor;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.metrics.ProjectMetricExtractor;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectMetricTest {

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

        ProjectMetric pm = new ProjectMetric(10, 10, 10, 10, 20, 20, 20, 20);

        assertEquals(10, pm.getCBO());
        assertEquals(10, pm.getWMC());
        assertEquals(10, pm.getLCOM());
        assertEquals(10, pm.getLOC());

        assertEquals(20, pm.getParentCBO());
        assertEquals(20, pm.getParentWMC());
        assertEquals(20, pm.getParentLCOM());
        assertEquals(20, pm.getParentLOC());

        assertEquals(10, pm.getDeltaCBO());
        assertEquals(10, pm.getDeltaWMC());
        assertEquals(10, pm.getDeltaLCOM());
        assertEquals(10, pm.getDeltaLOC());


        pm.setCBO(11);
        pm.setWMC(22);
        pm.setLCOM(33);
        pm.setLOC(44);

        pm.setParentCBO(55);
        pm.setParentWMC(66);
        pm.setParentLCOM(77);
        pm.setParentLOC(88);

        assertEquals(11,pm.getCBO());
        assertEquals(22,pm.getWMC());
        assertEquals(33,pm.getLCOM());
        assertEquals(44,pm.getLOC());

        assertEquals(55,pm.getParentCBO());
        assertEquals(66,pm.getParentWMC());
        assertEquals(77,pm.getParentLCOM());
        assertEquals(88,pm.getParentLOC());

        assertEquals(44,pm.getDeltaCBO());
        assertEquals(44,pm.getDeltaWMC());
        assertEquals(44,pm.getDeltaLCOM());
        assertEquals(44,pm.getDeltaLOC());

        pm.setDeltaCBO(2);
        pm.setDeltaWMC(2);
        pm.setDeltaLCOM(2);
        pm.setDeltaLOC(2);

        assertEquals(2, pm.getDeltaCBO() );
        assertEquals(2, pm.getDeltaWMC() );
        assertEquals(2, pm.getDeltaLCOM() );
        assertEquals(2, pm.getDeltaLOC() );

    }
}
