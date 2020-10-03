package com.group4.softwareanalytics;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectMetricExtractor {
    public static void metricsPrinter(ArrayList<Float> metrics){
        System.out.println("CBO= " + metrics.get(0).toString() + " , WMC=" + metrics.get(1).toString() + " , LCOM=" + metrics.get(2).toString() + " , LOC=" + metrics.get(3).toString() );
    }

    public static ArrayList<Float> classMetricsExtractor(String path){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "cd "+ path +" && java -jar ./../../../src/ck-0.6.3-SNAPSHOT-jar-with-dependencies.jar ./ true 0 false");

        try {
            Process process = processBuilder.start();
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                System.out.println("failed obtaining project metrics");
            }

            float averageCBO = 0F;
            float averageWMC = 0F;
            float averageLCOM = 0F;
            float averageLOC = 0F;
            int numberOfClasses = 0;
            String csvFile = path + "/class.csv";
            BufferedReader br = null;
            String line;
            String cvsSplitBy = ",";

            try {
                br = new BufferedReader(new FileReader(csvFile));
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] metrics = line.split(cvsSplitBy);
                    numberOfClasses = numberOfClasses + 1;
                    averageCBO = averageCBO + Integer.parseInt(metrics[1]);
                    averageWMC = averageWMC + Integer.parseInt(metrics[2]);
                    averageLCOM = averageLCOM + Integer.parseInt(metrics[3]);
                    averageLOC = averageLOC + Integer.parseInt(metrics[4]);
                }
                if (numberOfClasses > 0){
                    averageCBO = averageCBO / numberOfClasses;
                    averageWMC = averageWMC / numberOfClasses;
                    averageLCOM = averageLCOM / numberOfClasses;
                    averageLOC = averageLOC / numberOfClasses;
                }
                ArrayList<Float> avgMetrics = new ArrayList<>();
                avgMetrics.add(averageCBO);
                avgMetrics.add(averageWMC);
                avgMetrics.add(averageLCOM);
                avgMetrics.add(averageLOC);
                return avgMetrics;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception occurred while computing code metrics");
            e.printStackTrace();
        }
        System.out.println("Cannot extract class metric, method: classMetricsExtractor");
        return new ArrayList<>(4);
    }

    public static void checkoutParent(Git git){
        try {
            ObjectId previousCommitId = git.getRepository().resolve( "HEAD^" );
            git.checkout().setName( previousCommitId.getName() ).call();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static ProjectMetric commitCodeQualityExtractor(String owner, String repoName, String commit){
        String path = "./repo/" + owner +"/"+ repoName;
        System.out.println(commit);

        try {
            Git git = Git.open( new File(path + "/.git") );
            // checkout custom commit and get metrics
            git.checkout().setName(commit).call();

            ArrayList<Float> metrics = classMetricsExtractor(path);
            metricsPrinter(metrics);
            ObjectId previousCommitId = git.getRepository().resolve( "HEAD^" );
            System.out.println("Parent is");
            System.out.println(previousCommitId.getName());

            git.checkout().setName(previousCommitId.getName()).call();
            ArrayList<Float> parentMetrics = classMetricsExtractor(path);
            metricsPrinter(parentMetrics);

            System.out.println(parentMetrics.get(0));
            return new ProjectMetric(owner, repoName, metrics.get(0), metrics.get(1), metrics.get(2), metrics.get(3), parentMetrics.get(0), parentMetrics.get(1), parentMetrics.get(2), parentMetrics.get(3));
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.out.println("jere");
        }
        return null;
    }
}
