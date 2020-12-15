package com.group4.softwareanalytics;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.meta.ThresholdSelector;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Predictor {



//    Class CommitEntry needs to be for this to work

    public static Instances createArfFile(List<CommitEntry> ces) {

        Instances data;
        ArrayList<Attribute> atts = new ArrayList<>();
        ArrayList<String> attVals = new ArrayList<>();
        double[] vals;

        attVals.add("True");
        attVals.add("False");

        atts.add(new Attribute("addedFileCount"));
        atts.add(new Attribute("modifiedFileCount"));
        atts.add(new Attribute("deleteFileCount"));
        atts.add(new Attribute("addedLinesCount"));
        atts.add(new Attribute("deletedLinesCount"));
        atts.add(new Attribute("developerAbsoluteExperience"));
        atts.add(new Attribute("developerAverageExperience"));
        atts.add(new Attribute("developerBuggyCommitsRatio"));
        atts.add(new Attribute("developerTotalCommitsLastMont"));
        atts.add(new Attribute("buggy", attVals));

        data = new Instances("CommitsSet", atts, 0);

        for (CommitEntry ce: ces) {
            vals = new double[data.numAttributes()];

            vals[0] = (double) ce.getAddedFileCount();
            vals[1] = (double) ce.getModifiedFileCount();
            vals[2] = (double) ce.getDeleteFileCount();
            vals[3] = (double) ce.getAddedFileCount();
            vals[4] = (double) ce.getDeleteFileCount();
            vals[5] = (double) ce.getDeveloperAbsoluteExperience();
            vals[6] = (double) ce.getDeveloperAverageExperience();
            vals[7] = (double) ce.getDeveloperBuggyCommitsRatio();
            vals[8] = (double) ce.getDeveloperTotalCommitsLastMont();
            if (ce.isBuggy()) {
                vals[9] = attVals.indexOf("True");
            } else {
                vals[9] = attVals.indexOf("False");
            }
            data.add(new DenseInstance(1.0, vals));
        }
        return data;
    }


    public static PredictorStats evaluate(Instances dataset) throws Exception {

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("true");
        categories.add("false");
        String toPredictName = "buggy";
        
        Instances balancedTrainingFilePath = balanceTrainingSetWithSMOTE(dataset, toPredictName, categories);
        if(balancedTrainingFilePath == null)
            balancedTrainingFilePath = dataset;

        Instances data =  balancedTrainingFilePath;
        AbstractClassifier classifier = buildRandomForestClassifier(data, toPredictName);
        Evaluation eval = new Evaluation(data);
        List<Double> pre = new ArrayList<>();
        List<Double> rec = new ArrayList<>();
        List<Double> acc = new ArrayList<>();

        ThresholdSelector ts = new ThresholdSelector();
        ts.setClassifier(classifier);
        ts.buildClassifier(data);
        ts.setManualThresholdValue(0.7);

        int seed = 1;
        int folds = 10;
        int runs = 10;
        for (int i = 0; i < runs; i++) {
            seed = i + 1;
            eval.crossValidateModel(ts, data, folds, new Random(seed));

            Double currentPrecision = eval.precision(0);
            if (currentPrecision.isNaN()){
                return null;
            }
            pre.add(currentPrecision);

            Double currentRecall = eval.recall(0);
            if (currentRecall.isNaN()){
                return null;
            }
            rec.add(currentRecall);

            Double currentAccuracy = eval.pctCorrect();
            if (currentAccuracy.isNaN()){
                return null;
            }
            acc.add(currentAccuracy);

        }

        Double AccTotal = .0;
        for(Double res: acc)
        {
            AccTotal +=res;
        }

        Double PreTotal = .0;
        for(Double res: pre)
        {
            PreTotal +=res;
        }

        Double RecTotal = .0;
        for(Double res: rec)
        {
            RecTotal +=res;
        }

        double precision = (PreTotal/10) * 100;
        double recall = (RecTotal/10) * 100;
        double accuracy = AccTotal/10;


        return new PredictorStats(precision, recall, accuracy);
    }

    public static ArrayList<Double> predict(Instances dataset, Instances predictionSet) throws Exception {

        ArrayList<Double> cleanProbability = new ArrayList<>();

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("true");
        categories.add("false");
        String toPredictName = "buggy";

        Instances balancedTrainingFilePath = balanceTrainingSetWithSMOTE(dataset, toPredictName, categories);
        if(balancedTrainingFilePath == null)
            balancedTrainingFilePath = dataset;

        Instances data =  balancedTrainingFilePath;
        AbstractClassifier classifier = buildRandomForestClassifier(data, toPredictName);

        ThresholdSelector ts = new ThresholdSelector();
        ts.setClassifier(classifier);
        ts.buildClassifier(data);
        ts.setManualThresholdValue(0.7);

        Evaluation results = runOnTestSet(ts, predictionSet, toPredictName);


        for (int i = 0; i < results.predictions().size() ; i++) {
            String[] parts = results.predictions().get(i).toString().split(" ");
            String lastOne = parts[parts.length-1];
            cleanProbability.add(Double.parseDouble(lastOne));
        }

        return cleanProbability;
    }


    private static RandomForest buildRandomForestClassifier(Instances instances, String toPredictName) throws Exception{


        RandomForest classifier = new RandomForest();

//        String options = ("-I 100");
//        String[] optionsArray = options.split(" ");
//        classifier.setOptions(optionsArray);
        instances.setClass(instances.attribute(toPredictName));
        classifier.buildClassifier(instances);
        return classifier;
    }


    public static Instances balanceTrainingSetWithSMOTE(Instances instances, String toPredictName, ArrayList<String> categories) throws Exception{

        instances.setClass(instances.attribute(toPredictName));

        ArrayList<Integer> percentages = getPercentageOfArtificialInstancesNeeded(instances, categories);
        if(percentages == null)
            return null;

        for(int j=0; j<percentages.size(); j++){
            if(percentages.get(j)==0)
                continue;

            SMOTE filter =new SMOTE();
            filter.setInputFormat(instances);
            String options = ("-C " + (j+1) + " -K 5 -P " + percentages.get(j) + " -S 1");
            String[] optionsArray = options.split(" ");
            filter.setOptions(optionsArray);
            instances = Filter.useFilter(instances, filter);

        }

        return instances;

    }

    private static ArrayList<Integer> getPercentageOfArtificialInstancesNeeded(Instances instances, ArrayList<String> categories){
        ArrayList<Integer> result = new ArrayList<Integer>();
        ArrayList<Integer> numberOfInstancesPerCategory = new ArrayList<Integer>();
        for(int i=0; i<categories.size(); i++){
            numberOfInstancesPerCategory.add(0);
        }

        Iterator<Instance> iterator = instances.iterator();
        while(iterator.hasNext()){
            Instance instance = iterator.next();
            int index = ((Double) instance.classValue()).intValue();
            numberOfInstancesPerCategory.set(index,numberOfInstancesPerCategory.get(index)+1);
        }

        int max = 0;
        int maxIndex = -1;

        for(int i=0; i<numberOfInstancesPerCategory.size(); i++){
            if(numberOfInstancesPerCategory.get(i) < 6)
                return null;
            if(numberOfInstancesPerCategory.get(i) > max){
                max = numberOfInstancesPerCategory.get(i);
                maxIndex = i;
            }
        }

        for(int i=0; i<numberOfInstancesPerCategory.size(); i++){
            if(i==maxIndex){
                result.add(0);
            } else {
                int percentage = ((max-numberOfInstancesPerCategory.get(i))*100)/numberOfInstancesPerCategory.get(i);
                result.add(percentage);
            }
        }

        return result;
    }

    private static Evaluation runOnTestSet(Classifier classifier, Instances testInstances, String toPredict) throws Exception{

        testInstances.setClass(testInstances.attribute(toPredict));

        StringBuffer predsBuffer = new StringBuffer();
        PlainText pt = new PlainText();
        pt.setBuffer(predsBuffer);
        pt.setHeader(testInstances);
        Evaluation eval = new Evaluation(testInstances);
        eval.evaluateModel(classifier, testInstances, pt);

        return eval;
    }
}


