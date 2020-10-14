package com.group4.softwareanalytics;

import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitExtractor;
import com.group4.softwareanalytics.commits.CommitRepository;
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
class CommitControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private AsyncService asyncService;
    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private RepoRepository repoRepository;



    @Test
    void testFetchCommits() throws IOException, GitAPIException, InterruptedException, InvocationTargetException, IllegalAccessException {

        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        List<Commit> commits = asyncService.fetchCommits("HouariZegai","Calculator",r);

        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

        assertNotNull(commits);

        for(Commit commit:commits)
        {
            for (Method m : commit.getClass().getMethods()) {
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                    final Object met = m.invoke(commit);
                    if(m.getName() != "getEncodingName")
                    {
                        assertNotNull(met);
                    }
                }
            }
        }


    }
    @Test
    void testDuplicateCommits() throws Exception {
        RequestBuilder request  = MockMvcRequestBuilders.get("/api/commits");
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
        RequestBuilder request  = MockMvcRequestBuilders.get("/api/repo/"+owner+"/"+name+"/commits");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));
        JsonObject object = jsonReader.readObject();
        int ids = Integer.parseInt(object.get("totalElements").toString()); // GET TOTAL NUMBER OF COMMITS FROM THE DB

        org.eclipse.jgit.lib.Repository repo = new FileRepository("./repo/"+owner+"/"+name+"/.git"); // GET TOTAL NUMBER OF COMMITS FROM THE ACTUAL REPO
        List<String> branches = new ArrayList<>();
        Git git = new Git(repo);
        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        for(Ref branch: refs)
        {
            branches.add(branch.getName());
        }

        List<RevCommit> revCommitList = CommitExtractor.getCommits(branches.get(0), git, repo);

        assertEquals(revCommitList.size(),ids);
    }

}