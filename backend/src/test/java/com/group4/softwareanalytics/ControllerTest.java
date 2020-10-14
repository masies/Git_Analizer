package com.group4.softwareanalytics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.softwareanalytics.repository.RepoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void apiTest() throws Exception {
        String owner = "houarizegai";
        String repo = "calculator";
        HashMap<String, Object> body = new HashMap<>();
        body.put("owner",(Object) owner);
        body.put("repo",(Object) repo);

        ObjectMapper mapper = new ObjectMapper();

        String todoJson = mapper.writeValueAsString(body);


        MvcResult repoRes = mvc.perform(post("/api/repo/")
                .content(todoJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult commitRes = mvc.perform(get("/api/commits/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();


        MvcResult issueRes = mvc.perform(get("/api/issues/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult issueCommentRes = mvc.perform(get("/api/issue/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();


    }

}
