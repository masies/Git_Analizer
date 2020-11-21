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
public class DeveloperPRController {

    @Autowired
    private  DeveloperPRRepository developerPRRepository;

    @RequestMapping(value = "/repo/{owner}/{repo}/developerPR", method = {RequestMethod.GET})
    public @ResponseBody
    Page<DeveloperPR> getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "20") String size,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "sort", defaultValue = "percentage_accepted_opened") String sort
    ) {
        Sort.Order s;
        if (order.equals("desc")){
            s = Sort.Order.desc(sort);
        } else {
            s = Sort.Order.asc(sort);
        }

        return developerPRRepository.findByOwnerAndRepo(
                owner,
                repo,
                PageRequest.of(
                        Integer.parseInt(page),
                        Integer.parseInt(size),
                        Sort.by(s)
                )
        );
    }

    @RequestMapping(value = "/repo/{owner}/{repo}/developerPR/{username}", method = {RequestMethod.GET})
    public @ResponseBody
    DeveloperPR getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @PathVariable(value="username") String username) {
        return developerPRRepository.findByOwnerAndRepoAndUsername(owner,repo,username);
    }

    @GetMapping("/repo/{owner}/{repo}/developerPR/search")
    @ResponseBody
    public Page<DeveloperPR> getRepos(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(value = "username", defaultValue = "") String q) {
        Pageable paging = PageRequest.of(page, size);
        return developerPRRepository.findByQuery(owner, repo, q, paging);
    }


}
