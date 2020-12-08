package com.group4.softwareanalytics;

public class PredictorStats {

    private double precision;
    private double recall;
    private double accuracy;

    public PredictorStats(double precision, double recall, double accuracy) {
        this.precision = precision;
        this.recall = recall;
        this.accuracy = accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
