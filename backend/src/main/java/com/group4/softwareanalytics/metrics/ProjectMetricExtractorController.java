package com.group4.softwareanalytics.metrics;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectMetricExtractorController {

//    @RequestMapping("/repo/{owner}/{repo}/{commitHash}/metrics")
//    public @ResponseBody
//    ProjectMetric getAttr(@PathVariable(value="owner") String owner, @PathVariable(value="repo") String repo, @PathVariable(value="commitHash") String commit) {
//        return ProjectMetricExtractor.commitCodeQualityExtractor(owner,repo,commit);
//    }
}
