package com.group4.softwareanalytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private AsyncService asyncService;


    @GetMapping("/repo")
    @ResponseBody
    public Page<Repo> getRepos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) throws IOException {
        Pageable paging = PageRequest.of(page, size);
        return repoRepository.findAll(paging);
    }

    @RequestMapping("/repo/{owner}/{repo}/status")
    public @ResponseBody RepoStatus getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        Repo r = repoRepository.findByOwnerAndRepo(owner,repo);
        return r != null ?  r.getStatus() : new RepoStatus();
    }

    @RequestMapping("/repo/{owner}/{repo}")
    public @ResponseBody Repo getRepo(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        return repoRepository.findByOwnerAndRepo(owner,repo);
    }

    @PostMapping("/repo")
    @ResponseBody
    public Repo fetchRepo(@RequestBody Map<String, Object> body) throws InterruptedException {
        String owner = body.getOrDefault("owner", "google").toString();
        String name = body.getOrDefault("name", "guava").toString();
        asyncService.fetchData(owner, name);
        return null;
    }
}







