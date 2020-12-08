package com.group4.softwareanalytics;

import com.group4.softwareanalytics.repository.Repo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class RepoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AsyncService asyncService;

    @Test
    void testRepoFetch() throws Exception {

        String owner = "HouariZegai";
        String name = "Calculator";

        Repo repo = asyncService.fetchRepo(owner,name);

        assertNotNull(repo);
        assertEquals("Calculator", repo.getRepo());
        assertEquals("HouariZegai", repo.getOwner());
    }

    @Test
    void duplicateTest() throws Exception {
        RequestBuilder request  = MockMvcRequestBuilders.get("/api/repo");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));
        JsonObject object = jsonReader.readObject();

        JsonArray jsonArray = object.getJsonArray("content");
        List<String> repos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            repos.add(jsonArray.getJsonObject(i).get("repo").toString());
        }

        Set<String> set = new HashSet<String>(repos);

        assertEquals(set.size(),repos.size());
    }

    @Test
    void statusTest() throws Exception{

        RequestBuilder request  = MockMvcRequestBuilders.get("/api/repo");
        MvcResult result = mvc.perform(request).andReturn();

        JsonReader jsonReader = Json.createReader(new StringReader(result.getResponse().getContentAsString()));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        JsonArray jsonArray = object.getJsonArray("content");


        for (int i = 0; i < jsonArray.size(); i++) {

            String owner = jsonArray.getJsonObject(i).get("owner").toString().replaceAll("^\"|\"$", "");
            String repo = jsonArray.getJsonObject(i).get("repo").toString().replaceAll("^\"|\"$", "");

            RequestBuilder statusRequest  = MockMvcRequestBuilders.get("/api/repo/"+owner+"/"+repo+"/status");
            MvcResult statusResult = mvc.perform(statusRequest).andReturn();

            JsonReader statusJsonReader = Json.createReader(new StringReader(statusResult.getResponse().getContentAsString()));
            JsonObject statusObject = statusJsonReader.readObject();
            statusJsonReader.close();

            for (String key: statusObject.keySet())
            {
                assertNotNull(statusObject.get(key).toString());
            }
        }
    }

}