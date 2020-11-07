package com.group4.softwareanalytics.metrics;

import com.github.mauricioaniche.ck.CK;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectMetricExtractor {
    static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProjectMetricExtractor.class.getName());

    public static void metricsPrinter(ArrayList<Float> metrics){
        logger.info("CBO: " + metrics.get(0).toString() + " , WMC:" + metrics.get(1).toString() + " , LCOM:" + metrics.get(2).toString() + " , LOC:" + metrics.get(3).toString() );
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
            git.checkout().setName( previousCommitId.getName() ).call();
        } catch (IOException | GitAPIException e){
            logger.info(e.getMessage());
        }
    }

    public static ProjectMetric commitCodeQualityExtractor(String owner, String repoName, String commit, ArrayList<String> parentCommits){
        String path = "./repo/" + owner +"/"+ repoName;

        try (Git git = Git.open(new File(path + "/.git"))) {
            git.checkout().setName(commit).call();
            ArrayList<Float> metrics = classMetricsExtractor(path);
            if (parentCommits.size() == 1) {
                git.checkout().setName(parentCommits.get(0)).call();
                ArrayList<Float> parentMetrics = classMetricsExtractor(path);
                return new ProjectMetric(metrics.get(0), metrics.get(1), metrics.get(2), metrics.get(3), parentMetrics.get(0), parentMetrics.get(1), parentMetrics.get(2), parentMetrics.get(3));
            } else {
                logger.info("Commit with more than one parent");
                return new ProjectMetric(metrics.get(0), metrics.get(1), metrics.get(2), metrics.get(3), 0, 0, 0, 0);
            }
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return new ProjectMetric(0,0,0,0,0,0,0,0);
    }
}
