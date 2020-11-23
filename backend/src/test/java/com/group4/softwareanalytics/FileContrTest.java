package com.group4.softwareanalytics;


import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitController;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.fileContribution.FileContribution;
import com.group4.softwareanalytics.fileContribution.FileContributionRepository;
import com.group4.softwareanalytics.repository.Repo;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class FileContrTest {

    @Autowired
    private AsyncService asyncService;
    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private FileContributionRepository fileContributionRepository;



    @Test
    void FileRepoTest() throws IOException {
        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        asyncService.fetchCommits("HouariZegai","Calculator",r);

        List<Commit> commits = commitRepository.findAndRemove(owner,name);
        List<FileContribution> files = fileContributionRepository.findAndRemove(owner,name);

        assertTrue(files.size() > 1);

        File dir = new File("./repo/" +  owner);
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }
        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);
    }





}
