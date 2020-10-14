
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

        assertEquals(pm.getCBO(), 10);
        assertEquals(pm.getWMC(), 10);
        assertEquals(pm.getLCOM(), 10);
        assertEquals(pm.getLOC(), 10);

        assertEquals(pm.getParentCBO(), 20);
        assertEquals(pm.getParentWMC(), 20);
        assertEquals(pm.getParentLCOM(), 20);
        assertEquals(pm.getParentLOC(), 20);

        assertEquals(pm.getDeltaCBO(), 10);
        assertEquals(pm.getDeltaWMC(), 10);
        assertEquals(pm.getDeltaLCOM(), 10);
        assertEquals(pm.getDeltaLOC(), 10);


        pm.setCBO(11);
        pm.setWMC(22);
        pm.setLCOM(33);
        pm.setLOC(44);

        pm.setParentCBO(55);
        pm.setParentWMC(66);
        pm.setParentLCOM(77);
        pm.setParentLOC(88);

        assertEquals(pm.getCBO(), 11);
        assertEquals(pm.getWMC(), 22);
        assertEquals(pm.getLCOM(), 33);
        assertEquals(pm.getLOC(), 44);

        assertEquals(pm.getParentCBO(), 55);
        assertEquals(pm.getParentWMC(), 66);
        assertEquals(pm.getParentLCOM(), 77);
        assertEquals(pm.getParentLOC(), 88);

        assertEquals(pm.getDeltaCBO(), 44);
        assertEquals(pm.getDeltaWMC(), 44);
        assertEquals(pm.getDeltaLCOM(), 44);
        assertEquals(pm.getDeltaLOC(), 44);

    }
}
