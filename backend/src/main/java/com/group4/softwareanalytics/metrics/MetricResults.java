package com.group4.softwareanalytics.metrics;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;

import java.util.*;

public class MetricResults implements CKNotifier {
    private float averageCBO = 0F;
    private float averageWMC = 0F;
    private float averageLCOM = 0F;
    private float averageLOC = 0F;
    private int numberOfClasses = 0;

    private Set<String> javaFiles = new HashSet<>();

    @Override
    public void notify(CKClassResult ckClassResult) {
            this.numberOfClasses = this.numberOfClasses + 1;
            this.averageCBO += ckClassResult.getCbo();
            this.averageLCOM += ckClassResult.getLcom();
            this.averageWMC += ckClassResult.getWmc();
            this.averageLOC += ckClassResult.getLoc();
    }

    public List<Float> getResults(){
        if (numberOfClasses > 0){
            averageCBO = averageCBO / numberOfClasses;
            averageWMC = averageWMC / numberOfClasses;
            averageLCOM = averageLCOM / numberOfClasses;
            averageLOC = averageLOC / numberOfClasses;
        }
        return new ArrayList<>(Arrays.asList(averageCBO, averageWMC, averageLOC, averageLCOM));
    }

    public Set<String> getJavaFiles() {
        return javaFiles;
    }

    public void setJavaFiles(Set<String> javaFiles) {
        this.javaFiles = javaFiles;
    }
}
