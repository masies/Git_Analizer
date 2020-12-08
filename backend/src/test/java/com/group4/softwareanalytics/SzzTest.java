package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.CommitGeneralInfo;
import com.group4.softwareanalytics.issues.IssueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SzzTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Test
    void testComputeSZZ() throws IOException, GitAPIException, InvocationTargetException, IllegalAccessException {
        String owner = "ManbangGroup";
        String name = "Phantom";

        Repo r = asyncService.fetchRepo(owner,name);

        repoRepository.findAndRemove(owner,name);
        issueRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

        asyncService.fetchIssues(owner,name,r);

        asyncService.fetchCommits(owner,name,r);

        asyncService.computeSZZ(owner,name,r);

        List<Commit> commits = commitRepository.findAndRemove(owner,name);

        assertEquals(1,commits.get(18).getBugInducingCommits().size());

        CommitGeneralInfo bugCommit = (CommitGeneralInfo) commits.get(18).getBugInducingCommits().toArray()[0];

        List<String> nullFields = Arrays.asList("getEncodingName","getBugInducingCommits","getLinkedFixedIssues");

        for (Method m : bugCommit.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                final Object met = m.invoke(bugCommit);
                if(!nullFields.contains(m.getName()))
                {
                    assertNotNull(met);
                }
            }
        }
        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);
        issueRepository.findAndRemove(owner,name);
    }
}
