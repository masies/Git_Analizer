package com.group4.softwareanalytics.developer;

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

    @GetMapping("/repo/{owner}/{repo}/developerExpertise")
    public @ResponseBody
    Page<DeveloperExpertise> getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "sort", defaultValue = "expertise") String sort
    ) {
        return developerExpertiseRepository.findByOwnerAndRepo(
                owner,
                repo,
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortingScheme(sort,order))
                )
        );
    }

    @GetMapping("/repo/{owner}/{repo}/developerExpertise/{mail}")
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
            @RequestParam(value = "email", defaultValue = "") String q,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "sort", defaultValue = "percentage_accepted_opened") String sort) {

        Pageable paging = PageRequest.of(page, size, Sort.by(sortingScheme(sort, order)));
        return developerExpertiseRepository.findByQuery(owner, repo, q, paging);
    }

    private Sort.Order sortingScheme(String sort, String order){
        Sort.Order s;
        if (order.equals("asc")){
            s = Sort.Order.asc(sort);
        } else {
            s = Sort.Order.desc(sort);
        }
        return s;
    }
}
