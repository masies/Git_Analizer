package com.group4.softwareanalytics;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;

import java.util.ArrayList;
import java.util.Arrays;

public class MetricResults implements CKNotifier {
    private float averageCBO = 0F;
    private float averageWMC = 0F;
    private float averageLCOM = 0F;
    private float averageLOC = 0F;
    private int numberOfClasses = 0;

    @Override
    public void notify(CKClassResult ckClassResult) {
        this.numberOfClasses = this.numberOfClasses + 1;
        this.averageCBO += ckClassResult.getCbo();
        this.averageLCOM += ckClassResult.getLcom();
        this.averageWMC += ckClassResult.getWmc();
        this.averageLOC += ckClassResult.getLoc();
    }

    public ArrayList<Float> getResults(){
        if (numberOfClasses > 0){
            averageCBO = averageCBO / numberOfClasses;
            averageWMC = averageWMC / numberOfClasses;
            averageLCOM = averageLCOM / numberOfClasses;
            averageLOC = averageLOC / numberOfClasses;
        }
        return new ArrayList<Float>(Arrays.asList(averageCBO,averageWMC,averageLCOM,averageLOC));
    }
}
