package com.group4.softwareanalytics.repository;

import com.group4.softwareanalytics.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {

    static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RepoController.class.getName());


    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/repo/{owner}/{repo}/delete")
    public @ResponseBody
    void removeRepo(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        repoRepository.findAndRemove(owner,repo);
        // TODO: Remove all the othed data from all the other repository
    }

    @GetMapping("/repo")
    @ResponseBody
    public Page<Repo> getRepos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        Pageable paging = PageRequest.of(page, size);
        return repoRepository.findAll(paging);
    }

    @GetMapping("/repo/search")
    @ResponseBody
    public Page<Repo> getRepos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "") String q) {
        Pageable paging = PageRequest.of(page, size);
        return repoRepository.findByQuery(q, paging);
    }

    @GetMapping("/repo/{owner}/{repo}/status")
    public @ResponseBody
    RepoStatus getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        Repo r = repoRepository.findByOwnerAndRepo(owner,repo);
        return r != null ?  r.getStatus() : new RepoStatus();
    }

    @GetMapping("/repo/{owner}/{repo}")
    public @ResponseBody Repo getRepo(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        return repoRepository.findByOwnerAndRepo(owner,repo);
    }

    @PostMapping("/repo")
    @ResponseBody
    public Repo fetchRepo(@RequestBody Map<String, Object> body) {
        String owner = body.getOrDefault("owner", "google").toString();
        String name = body.getOrDefault("name", "guava").toString();
        Repo repo = repoRepository.findByOwnerAndRepo(owner,name);
        if (repo != null && (!repo.getStatus().getFetchedInfo() || !repo.getStatus().getFetchedCommits() || !repo.getStatus().getFetchedIssues())) {
            logger.info("====== Repo is still on process");
            return null;
        }
        asyncService.fetchData(owner, name);
        return repo;
    }
}







