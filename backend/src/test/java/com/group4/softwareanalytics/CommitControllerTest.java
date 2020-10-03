package com.group4.softwareanalytics;

import com.sun.tools.hat.internal.model.JavaValue;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
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
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@AutoConfigureMockMvc
class CommitControllerTest {

    @Autowired
    private MockMvc mvc;



    @Test
    void testDuplicateCommits() throws Exception {
        RequestBuilder request  = MockMvcRequestBuilders.get("/api/commits");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));

        JsonArray jsonArray = jsonReader.readArray();

        List<String> ids = new ArrayList<>();

        for(JsonValue jsonObject: jsonArray)
        {
            ids.add(jsonObject.asJsonObject().get("id").toString());
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