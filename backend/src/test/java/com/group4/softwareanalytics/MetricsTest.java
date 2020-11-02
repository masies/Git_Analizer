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

        System.out.println(targetCommit.getCommitName());

        assertEquals(targetCommit.getCommitParentsIDs().size(),1);

        assertNotNull(targetCommit);

        ProjectMetric metrics = ProjectMetricExtractor.commitCodeQualityExtractor(owner, name, targetCommit.getCommitName(), targetCommit.getCommitParentsIDs());

        assertNotNull(metrics);

        //Parent Metrics Check
        assertEquals(metrics.getParentCBO(),2);
        assertEquals(metrics.getParentWMC(),73);
        assertEquals(metrics.getParentLCOM(),3);


        //Self Metrics Check
        assertEquals(metrics.getCBO(),2);
        assertEquals(metrics.getWMC(),73);
        assertEquals(metrics.getLCOM(),3);

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

        assertEquals(diffEntries.size(),1);

        assertEquals(diffEntries.get(0).getNewPath(), "src/com/houarizegai/calculator/Calculator.java");
        assertEquals(diffEntries.get(0).getOldPath(), "src/com/houarizegai/calculator/Calculator.java");
        assertNotNull(diffEntries.get(0).getDiffs());
        assertEquals(diffEntries.get(0).getChangeType(),"MODIFY");

        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

    }
}
