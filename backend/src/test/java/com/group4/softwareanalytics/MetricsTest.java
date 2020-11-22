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
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@AutoConfigureMockMvc
public class MetricsTest {

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
        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        asyncService.fetchCommits("HouariZegai","Calculator",r);

        List<Commit> commits = commitRepository.findAndRemove(owner,name);

        Commit targetCommit = commits.get(0);

        assertEquals(1, targetCommit.getCommitParentsIDs().size());

        assertNotNull(targetCommit);

        ProjectMetric metrics = ProjectMetricExtractor.commitCodeQualityExtractor(owner, name, targetCommit.getCommitName(), targetCommit.getCommitParentsIDs());

        assertNotNull(metrics);

        targetCommit.setProjectMetrics(metrics);

        ProjectMetric targetCommitMetrics = targetCommit.getProjectMetrics();
//        //Parent Metrics Check
        assertEquals(metrics.getParentLOC(),targetCommitMetrics.getParentLOC());
        assertEquals(metrics.getParentCBO(),targetCommitMetrics.getParentCBO());
        assertEquals(metrics.getParentWMC(),targetCommitMetrics.getParentWMC());
        assertEquals(metrics.getParentLCOM(),targetCommitMetrics.getParentLCOM());

//        //Self Metrics Check
        assertEquals(metrics.getLOC(),targetCommitMetrics.getParentLOC());
        assertEquals(metrics.getCBO(),targetCommitMetrics.getCBO());
        assertEquals(metrics.getWMC(),targetCommitMetrics.getWMC());
        assertEquals(metrics.getLCOM(),targetCommitMetrics.getLCOM());

        for (Method m : metrics.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                final Object met = m.invoke(metrics);
                assertNotNull(met);
            }
        }


        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

    }

    @Test
    void testDiffMetrics() throws IOException, GitAPIException {
        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        asyncService.fetchCommits("HouariZegai","Calculator",r);

        List<Commit> commits = commitRepository.findAndRemove(owner,name);

        Commit targetCommit = commits.get(8);

        assertFalse(targetCommit.getHasMetrics());

        String dest_url = "./repo/" + owner +"/"+ name;
        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");
        Git git = new Git(repo);

        List<CommitDiff> diffEntries = CommitExtractor.getModifications(git, targetCommit.getCommitName(), dest_url, targetCommit.getCommitParentsIDs());

        assertEquals(1, diffEntries.size());

        assertEquals("src/com/houarizegai/calculator/Calculator.java", diffEntries.get(0).getNewPath());
        assertEquals( "src/com/houarizegai/calculator/Calculator.java",diffEntries.get(0).getOldPath());
        assertNotNull(diffEntries.get(0).getDiffs());
        assertEquals("MODIFY", diffEntries.get(0).getChangeType());

        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

    }
}
