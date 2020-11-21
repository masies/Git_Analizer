package com.group4.softwareanalytics;

import java.io.File;
import java.util.HashMap;

public class DirNav {
    static HashMap<String,String>  repoContents = new HashMap<>();

    public static HashMap<String,String> repoContents(String repoPath){
        File repoDirectory = new File(repoPath);
        File[] contents = repoDirectory.listFiles();
        assert contents != null;
        for(File item: contents) {
            if (item.isFile()) {
                repoContents.put(item.getPath(),"F");
            } else if (item.isDirectory()) {
                repoContents.put( item.getPath(),"D");
                repoContents(item.getPath());
            }
        }
        return repoContents;
    }
}

