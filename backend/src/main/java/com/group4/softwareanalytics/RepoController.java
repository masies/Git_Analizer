package com.group4.softwareanalytics;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private AsyncService asyncService;

    public void fetchCommits() throws IOException, GitAPIException {
        String repo_url = "https://github.com/mcostalba/Stockfish";
        String dest_url = "./Repo";
        List<Commit> commitList = new ArrayList<Commit>();
        List<RevCommit> revCommitList = new ArrayList<RevCommit>();
        List<String> branches = new ArrayList<>();

        if (!Files.exists(Paths.get(dest_url))) {
            CommitExtractor.DownloadRepo(repo_url, dest_url);
        }

        org.eclipse.jgit.lib.Repository repo = new FileRepository(dest_url + "/.git");

        Git git = new Git(repo);

        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        for(Ref branch: refs)
        {
            branches.add(branch.getName());
        }

        revCommitList = CommitExtractor.getCommits(branches.get(0),git,repo);

        for(RevCommit revCommit: revCommitList)
        {
            List<DiffEntry> diffEntries = CommitExtractor.getModifications(git, revCommit.getName());
            List<String> modifications = new ArrayList<>();

            for (DiffEntry diffEntry : diffEntries) {
                modifications.add(diffEntry.getChangeType().toString());
            }
            Commit c = new Commit( modifications);
            commitList.add(c);
        }
        commitRepository.saveAll(commitList);
    }

    @PostMapping("/commitsFetch")
    @ResponseBody
    public void postCommits() throws IOException, GitAPIException {
        fetchCommits();
    }

    @GetMapping("/repo")
    @ResponseBody
    public Page<Repo> getRepos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int size) throws IOException {
        Pageable paging = PageRequest.of(page, size);
        return repository.findAll(paging);
    }

    @RequestMapping("/repo/{owner}/{repo}/status")
    public @ResponseBody RepoStatus getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo) {
        Repo r = repository.findByOwnerAndRepo(owner,repo);
        return r.getStatus();
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







