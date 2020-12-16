package com.group4.softwareanalytics.fileContribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileContributionController {

    @Autowired
    private  FileContributionRepository fileContributionRepository;

    @GetMapping("/repo/{owner}/{repo}/FileContributions")
    public @ResponseBody
    List<FileContribution> getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo) {
        return fileContributionRepository.findByOwnerAndRepo(owner, repo);
    }

    @GetMapping("/repo/{owner}/{repo}/tree/search")
    public List<FileContribution> searchTree(@PathVariable(value="owner") String owner,
                                             @PathVariable(value="repo") String repo,
                                             @RequestParam(defaultValue = "") String q){
        return fileContributionRepository.findByOwnerAndRepoAndName(owner,repo, q);
    }

    @GetMapping("/repo/{owner}/{repo}/tree/**")
    public List<FileContribution> getAttr(@PathVariable(value="owner") String owner,
                                          @PathVariable(value="repo") String repo,
                                          HttpServletRequest request){
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = restOfTheUrl.replace("/api/repo/"+ owner + File.separator + repo +"/tree","");
        return fileContributionRepository.findByOwnerAndRepoAndDir(owner,repo,path);
    }



}
