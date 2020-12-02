package com.group4.softwareanalytics;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

public class Predictor {


    public static void EvaluateAndTrain(String trainingFilePath, String predictFilePath) {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("true");
        categories.add("false");
        String toPredictName = "buggy";

        int seed = 444;

        try {
            String balancedTrainingFilePath = balanceTrainingSetWithSMOTE(trainingFilePath, toPredictName, categories);
            if(balancedTrainingFilePath == null)
                balancedTrainingFilePath = trainingFilePath;

            RandomForest evalclassifier = new RandomForest();
            String options = ("-I 100");
            String[] optionsArray = options.split(" ");
            evalclassifier.setOptions(optionsArray);

            FileReader frTrain = new FileReader(balancedTrainingFilePath);

            Instances originalTrain = new Instances(frTrain);

            originalTrain.setClass(originalTrain.attribute(toPredictName));

            Evaluation eval = new Evaluation(originalTrain);

            List<Double> preEval = new ArrayList<>();

            System.out.println("Evaluating the Classifier.");
            int runs = 10;
            for (int i = 0; i < runs; i++) {
                seed = i + 1;
                eval.crossValidateModel(evalclassifier, originalTrain, 10, new Random(seed));
                preEval.add(eval.pctCorrect());
                System.out.println("Estimated Accuracy of iteration nb " + i +": "+Double.toString(eval.pctCorrect()));
            }

            Double total = .0;
            for(Double ev: preEval)
            {
                total +=ev;
            }

            System.out.println("Overall Evaluation:" +  total/10);

            AbstractClassifier classifier = buildRandomForestClassifier(evalclassifier, balancedTrainingFilePath, toPredictName);

            Evaluation results = runOnTestSet(classifier, predictFilePath, toPredictName);

            printResults(results);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void printResults(Evaluation results)
    {
        ArrayList<Prediction> predictions = results.predictions();

        int predictedAsBuggy = 0;
        int predictedAsClean = 0;
        for(Prediction prediction : predictions){
            //if is predicted as true
            if(prediction.predicted() == 0.0){
                //the last two numbers of the toString method provide information on the probability
                //that an instance is buggy
                predictedAsBuggy++;
            } else {
                predictedAsClean++;
            }
            //The toString prints: actual,predicted,probabilities
            String[] parts = prediction.toString().split(" ");
            String lastOne = parts[parts.length-1];
            double acc =Double.parseDouble(lastOne);
            if(acc>=0.7)
            {
                System.out.println(prediction.toString());
            }
        }


        System.out.println("Buggy: " + predictedAsBuggy);
        System.out.println("Clean: " + predictedAsClean);

    }
    private static RandomForest buildRandomForestClassifier(RandomForest classifier, String trainingFilePath, String toPredictName) throws Exception{

        FileReader frTraining = new FileReader(trainingFilePath);
        Instances ts = new Instances(frTraining);

        String options = ("-I 100");
        String[] optionsArray = options.split(" ");
        classifier.setOptions(optionsArray);
        ts.setClass(ts.attribute(toPredictName));
        classifier.buildClassifier(ts);
        return classifier;
    }


    public static String balanceTrainingSetWithSMOTE(String trainingFilePath, String toPredictName, ArrayList<String> categories) throws Exception{

        FileReader frTraining = new FileReader(trainingFilePath);
        Instances instances = new Instances(frTraining);
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

        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        File outputFile = new File(trainingFilePath.replace(".arff", "_balanced.arff"));
        saver.setFile(outputFile);
        saver.writeBatch();

        return outputFile.getAbsolutePath();

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

    private static Evaluation runOnTestSet(Classifier classifier, String testFilePath, String toPredict) throws Exception{

        FileReader frTest = new FileReader(testFilePath);
        Instances test = new Instances(frTest);
        test.setClass(test.attribute(toPredict));

        StringBuffer predsBuffer = new StringBuffer();
        PlainText pt = new PlainText();
        pt.setBuffer(predsBuffer);
        pt.setHeader(test);
        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(classifier, test, pt);

        return eval;
    }




}
