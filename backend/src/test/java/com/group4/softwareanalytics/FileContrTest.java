package com.group4.softwareanalytics;


import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitController;
import com.group4.softwareanalytics.commits.CommitExtractor;
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
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    void repoFilesContentTest() throws IOException {
        String owner = "HouariZegai";
        String name = "Calculator";

        String repo_url = "https://github.com/" + owner + "/" + name;
        String dest_url = "./repo/" + owner + "/" + name;


        deleteDir();

        CommitExtractor.DownloadRepo(repo_url, dest_url);

        ArrayList<FileContribution> fileContributions = asyncService.computeFileContributions(owner,name,dest_url);

        assertNotNull(fileContributions);

        assertTrue(fileContributions.size()>=18);

        List<String> lines = Arrays.asList("This is just a test");
        Path file = Paths.get("./repo/" + owner +"/" + name  +"/test-file.txt");
        Files.write(file, lines, StandardCharsets.UTF_8);

        ArrayList<FileContribution> newFileContributions = asyncService.computeFileContributions(owner,name,dest_url);

        assertTrue(newFileContributions.size() > fileContributions.size());

        deleteDir();
    }

    @Test
    void FileRepoTest() throws IOException, InvocationTargetException, IllegalAccessException {
        String owner = "HouariZegai";
        String name = "Calculator";

        Repo r = asyncService.fetchRepo(owner,name);

        asyncService.fetchCommits("HouariZegai","Calculator",r);

        List<FileContribution> files = fileContributionRepository.findByOwnerAndRepo(owner,name);

        FileContribution tarFile = null;

        for(FileContribution file: files)
        {
            if(file.getPath().equals("/src/com/houarizegai/calculator/Calculator.java"))
                tarFile = file;
        }

        assertTrue(files.size() > 1);

        File dir = new File("./repo/" +  owner);
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }

        fileContributionRepository.findAndRemove(owner,name);
        repoRepository.findAndRemove(owner,name);
        commitRepository.findAndRemove(owner,name);

        for (Method m : tarFile.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                final Object met = m.invoke(tarFile);
                System.out.println(met);
                assertNotNull(met);
            }
        }
    }

    public void deleteDir() throws IOException {
        File dir = new File("./repo/" +  "HouariZegai");
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }
    }

}

