package com.group4.softwareanalytics;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProjectMetricExtractor {
    public static void metricsPrinter(ArrayList<Float> metrics){
        System.out.println("CBO: " + metrics.get(0).toString() + " , WMC:" + metrics.get(1).toString() + " , LCOM:" + metrics.get(2).toString() + " , LOC:" + metrics.get(3).toString() );
    }

    public static ArrayList<Float> classMetricsExtractor(String path){
        MetricResults results = new MetricResults();
        CK ck = new CK();
        ck.calculate(path,results);
        return results.getResults();
    }

    public static void checkoutParent(Git git){
        try {
            ObjectId previousCommitId = git.getRepository().resolve( "HEAD^" );
            System.out.println("i will set HEAD on commit " + previousCommitId.getName());
            git.checkout().setName( previousCommitId.getName() ).call();
            System.out.println("Now HEAD is set on commit " + previousCommitId.getName());
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static ProjectMetric commitCodeQualityExtractor(String owner, String repoName, String commit, ArrayList<String> parentCommits){
        String path = "./repo/" + owner +"/"+ repoName;
        System.out.println(commit);

        try {
            Git git = Git.open( new File(path + "/.git") );
            // checkout custom commit and get metrics
            git.checkout().setName(commit).call();

            ArrayList<Float> metrics = classMetricsExtractor(path);
            metricsPrinter(metrics);

//            ObjectId previousCommitId = git.getRepository().resolve( "HEAD^" );

            ArrayList<Float> parentMetrics = new ArrayList<>();
            if (parentCommits.size() == 1){
                System.out.println(parentCommits);
                System.out.println("i will set HEAD on parent commit: " + parentCommits.get(0));
                git.checkout().setName( parentCommits.get(0) ).call();
                System.out.println("Now HEAD is set on commit " + parentCommits.get(0));

                parentMetrics = classMetricsExtractor(path);
                metricsPrinter(parentMetrics);
            } else {
                System.out.println("Commit with more than one parent");
                parentMetrics.add(0F);
                parentMetrics.add(0F);
                parentMetrics.add(0F);
                parentMetrics.add(0F);
            }


            System.out.println(parentMetrics.get(0));
            return new ProjectMetric(metrics.get(0), metrics.get(1), metrics.get(2), metrics.get(3), parentMetrics.get(0), parentMetrics.get(1), parentMetrics.get(2), parentMetrics.get(3));
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.out.println("jere");
        }
        return null;
    }
}
