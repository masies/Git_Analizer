package com.group4.softwareanalytics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repository;

    @GetMapping("/repo")
    @ResponseBody
    public Page<Repo> getRepos(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size)  throws IOException {
        Pageable paging = PageRequest.of(page, size);
        return repository.findAll(paging);
    }

    @PostMapping("/repo")
    @ResponseBody
    public Repo fetchRepo(@RequestBody Map<String, Object> body) {
        try {
            String owner = body.getOrDefault("owner", "google").toString();
            String name = body.getOrDefault("name", "guava").toString();

            RepositoryService service = new RepositoryService();
            service.getClient().setOAuth2Token("516c48a3eabd845073efe0df4234945fdff65dc0");
            Repository r = service.getRepository(owner, name);
            Repo repo = new Repo(r);
            repository.save(repo);

            return repo;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}







