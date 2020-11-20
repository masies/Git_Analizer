package com.group4.softwareanalytics.Developer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeveloperExpertiseController {

    @Autowired
    private DeveloperExpertiseRepository developerExpertiseRepository;

    @RequestMapping(value = "/repo/{owner}/{repo}/developerExpertise", method = {RequestMethod.GET})
    public @ResponseBody
    Page<DeveloperExpertise> getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "20") String size,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "sort", defaultValue = "expertise") String sort
    ) {
        Sort.Order s;
        if (order.equals("desc")){
            s = Sort.Order.desc(sort);
        } else {
            s = Sort.Order.asc(sort);
        }

        return developerExpertiseRepository.findByOwnerAndRepo(
                owner,
                repo,
                PageRequest.of(
                        Integer.parseInt(page),
                        Integer.parseInt(size),
                        Sort.by(s)
                )
        );
    }

    @RequestMapping(value = "/repo/{owner}/{repo}/developerExpertise/{mail}", method = {RequestMethod.GET})
    public @ResponseBody
    DeveloperExpertise getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @PathVariable(value="mail") String mail) {
        return developerExpertiseRepository.findByOwnerAndRepoAndEmail(owner,repo,mail);
    }

    @GetMapping("/repo/{owner}/{repo}/developerExpertise/search")
    @ResponseBody
    public Page<DeveloperExpertise> getRepos(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(value = "mail", defaultValue = "") String q) {
        Pageable paging = PageRequest.of(page, size);
        return developerExpertiseRepository.findByQuery(owner, repo, q, paging);
    }
}
