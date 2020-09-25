package com.group4.softwareanalytics;

import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

public class Commit {

//    @Field("commit")
//    private RevCommit commit;

//    private String commitID;
    private List<String> modifications = new ArrayList<>();


    public Commit(
//            RevCommit commit,
                  List<String> modifications) {
//        this.commit = commit;
        this.modifications = modifications;
    }

    public List<String> getModifications() {
        return modifications;
    }

    public void setModifications(List<String> modifications) {
        this.modifications = modifications;
    }

//    public RevCommit getCommit() {
//        return commit;
//    }
//
//    public void setCommit(RevCommit commit) {
//        this.commit = commit;
//    }
}
