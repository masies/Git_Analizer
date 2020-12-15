
package com.group4.softwareanalytics;


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

        assertEquals(10, pm.getCbo());
        assertEquals(10, pm.getWmc());
        assertEquals(10, pm.getLcom());
        assertEquals(10, pm.getLoc());

        assertEquals(20, pm.getParentCBO());
        assertEquals(20, pm.getParentWMC());
        assertEquals(20, pm.getParentLCOM());
        assertEquals(20, pm.getParentLOC());

        assertEquals(10, pm.getDeltaCBO());
        assertEquals(10, pm.getDeltaWMC());
        assertEquals(10, pm.getDeltaLCOM());
        assertEquals(10, pm.getDeltaLOC());


        pm.setCbo(11);
        pm.setWmc(22);
        pm.setLcom(33);
        pm.setLoc(44);

        pm.setParentCBO(55);
        pm.setParentWMC(66);
        pm.setParentLCOM(77);
        pm.setParentLOC(88);

        assertEquals(11,pm.getCbo());
        assertEquals(22,pm.getWmc());
        assertEquals(33,pm.getLcom());
        assertEquals(44,pm.getLoc());

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
