package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.issues.IssueRepository;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class IssueControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private AsyncService asyncService;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private RepoRepository repoRepository;

    @Test
    void testFetchIssues() throws IOException, GitAPIException, InterruptedException {

        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        repoRepository.findAndRemove(owner,name);


        assertNotNull(r);

        asyncService.fetchIssues("HouariZegai","Calculator",r);

        List<com.group4.softwareanalytics.issues.Issue> issues = issueRepository.findAndRemove(owner,name);

        assertNotNull(issues);

        issueRepository.findAndRemove(owner,name);
        repoRepository.findAndRemove(owner,name);
        for(com.group4.softwareanalytics.issues.Issue issue:issues)
        {
            assertNotNull(issue.getId());
            assertEquals(issue.getRepo(),name);
            assertEquals(issue.getOwner(),owner);
        }

    }


    @Test
    void testDuplicateIssues() throws Exception {
        RequestBuilder request  = MockMvcRequestBuilders.get("/api/issues");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));

        JsonArray jsonArray = jsonReader.readArray();

        List<String> ids = new ArrayList<>();

        for(JsonValue jsonValue: jsonArray)
        {
            jsonReader = Json.createReader(new StringReader(jsonValue.toString()));
            JsonObject object = jsonReader.readObject();
            ids.add(object.get("id").toString());
        }

        Set<String> setIds = new HashSet<String>(ids);

        assertEquals(setIds.size(),ids.size());
    }

    @Test
    void numberOfCommits() throws Exception {
        String owner = "mcostalba";
        String name = "Stockfish";

        Path path = Paths.get("./repo/"+owner+"/"+name+"/");

        if (!Files.exists(path)) {
            assertTrue(true);
            return;
        }

        RequestBuilder request  = MockMvcRequestBuilders.get("/api/repo/"+owner+"/"+name+"/issues");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));
        JsonObject object = jsonReader.readObject();
        int ids = Integer.parseInt(object.get("totalElements").toString()); // GET TOTAL NUMBER OF COMMITS FROM THE DB

        int expectedIds = fetchIssues(owner,name);

        assertEquals(expectedIds,ids);
    }

    int fetchIssues(String owner, String name) throws IOException {
        IssueService service = new IssueService();
        service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");

        // gather all the open issues
        List<Issue> issuesOpen = service.getIssues(owner, name,
                Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_OPEN));
        //System.out.println("OPEN ISSUES: "+ issuesOpen.size());

        // gather all the closed issues
        List<Issue> issuesClosed = service.getIssues(owner, name,
                Collections.singletonMap(IssueService.FILTER_STATE, IssueService.STATE_CLOSED));
        //System.out.println("CLOSED ISSUES: "+ issuesClosed.size());

        // merge the two list of issues
        List<Issue> issues = Stream.concat(issuesOpen.stream(), issuesClosed.stream())
                .collect(Collectors.toList());
        //System.out.println("ALL ISSUES: "+ issues.size());

        return  issues.size();
    }

}