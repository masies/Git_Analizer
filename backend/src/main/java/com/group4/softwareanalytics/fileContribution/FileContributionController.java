package com.group4.softwareanalytics.fileContribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileContributionController {

    @Autowired
    private  FileContributionRepository fileContributionRepository;

    @RequestMapping(value = "/repo/{owner}/{repo}/fileContributions", method = {RequestMethod.GET})
    public @ResponseBody
    Page<FileContribution> getAttr(
            @PathVariable(value="owner") String owner,
            @PathVariable(value="repo") String repo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        return fileContributionRepository.findByOwnerAndRepo(
                owner,
                repo,
                PageRequest.of(page, size)
        );
    }
}
