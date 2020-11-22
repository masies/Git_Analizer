package com.group4.softwareanalytics;

import java.io.File;
import java.util.HashMap;

public class DirNav {
    static HashMap<String,Boolean>  repoContents = new HashMap<>();



    public static HashMap<String,Boolean> repoContents(String repoPath){
        Boolean directory = false;
        Boolean file = true;

        File repoDirectory = new File(repoPath);
        File[] contents = repoDirectory.listFiles();
        assert contents != null;
        for(File item: contents) {
            if (item.isFile()) {
                repoContents.put(item.getPath(),file);
            } else if (item.isDirectory()) {
                repoContents.put( item.getPath(),directory);
                repoContents(item.getPath());
            }
        }
        return repoContents;
    }
}

