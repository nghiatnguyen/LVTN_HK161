package coreferenceresolver.weka;

import coreferenceresolver.util.StanfordUtil;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Weka {

    public static void j48Classify(String trainingFilePath, String testFilePath, String resultFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(
                new FileReader(trainingFilePath));
        Instances trainInstances = new Instances(reader);
        reader.close();

        //Remove 1st feature
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,2,3";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(trainInstances);                          // inform filter about dataset **AFTER** setting options
        Instances newTrainInstances = Filter.useFilter(trainInstances, remove);   // apply filter
        // setting class attribute 
        newTrainInstances.setClassIndex(newTrainInstances.numAttributes() - 1);

//		useCrossValidation(newData);
        useTestSet(newTrainInstances, testFilePath, resultFilePath);
    }

    public static void useCrossValidation(Instances inst) throws Exception {
        int seed = 1;          // the seed for randomizing the data
        int folds = 10;         // the number of folds to generate, >=2
        // randomize data
        Random rand = new Random(seed);
        Instances randData = new Instances(inst);
        randData.randomize(rand);
        if (randData.classAttribute().isNominal()) {
            randData.stratify(folds);
        }
//	    System.out.println(newData);
        // perform cross-validation
        Evaluation eval = new Evaluation(randData);
        for (int n = 0; n < folds; n++) {
            Instances train = randData.trainCV(folds, n);
            Instances test = randData.testCV(folds, n);
            // the above code is used by the StratifiedRemoveFolds filter, the
            // code below by the Explorer/Experimenter:
            // Instances train = randData.trainCV(folds, n, rand);

            // build and evaluate classifier
            J48 tree = new J48();
            tree.buildClassifier(train);
            eval.evaluateModel(tree, test);

        }
        System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());
    }

    public static void useTestSet(Instances trainInstances, String testFilePath, String resultFilePath) throws Exception {
        Instances testInstances = new Instances(new BufferedReader(new FileReader(testFilePath)));
        PrintWriter writer = new PrintWriter(resultFilePath, "UTF-8");

        Remove remove = new Remove(); 
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,2,3";    // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(testInstances);                          // inform filter about dataset **AFTER** setting options
        Instances newTestInstances = Filter.useFilter(testInstances, remove);   // apply filter
        // setting class attribute 
        newTestInstances.setClassIndex(newTestInstances.numAttributes() - 1);
        J48 tree = new J48();
        tree.buildClassifier(trainInstances);
        for (int i = 0; i < newTestInstances.numInstances(); i++) {
            double index = tree.classifyInstance(newTestInstances.instance(i));
            String classPredicted = newTestInstances.classAttribute().value((int) index);
            String classActual = newTestInstances.classAttribute().value((int) newTestInstances.instance(i).classValue());
            
            if (classActual.equals("true")) {
                int reviewId = (int) testInstances.instance(i).value(0);
                int np1Id = (int) testInstances.instance(i).value(1);
                int np2Id = (int) testInstances.instance(i).value(2);

                StanfordUtil.reviews.get(reviewId).addCorefChainActual(np1Id, np2Id);
            }
            
            
            //If 2 instances is coref, consider to add them to COREFs chain of the review
            if (classPredicted.equals("true")) {
                int reviewId = (int) testInstances.instance(i).value(0);
                int np1Id = (int) testInstances.instance(i).value(1);
                int np2Id = (int) testInstances.instance(i).value(2);

                StanfordUtil.reviews.get(reviewId).addCorefChainPredict(np1Id, np2Id);
            }
//            System.out.println("Class Actual = " + classActual);
//            for (int j = 0; j < data.numAttributes(); j++){
//            	writer.print(data.instance(i).value(j) + ",");
//            }
//            writer.println();
        }
//        Evaluation eval = new Evaluation(trainInstances);
//        eval.evaluateModel(tree, newTestInstances);
//        writer.println(eval.toSummaryString("=== Result: ===", false));
//        writer.println(eval.toClassDetailsString());
//        writer.println(eval.toMatrixString());
        writer.close();
        System.out.println("Done");
    }
    
    public static void trainJustOpinionWords(String trainingFilePath, String testFilePath) throws Exception{
        BufferedReader reader = new BufferedReader(
                new FileReader(trainingFilePath));
        Instances trainInstances = new Instances(reader);
        reader.close();

        //Remove 1st feature
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,2,3";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(trainInstances);                          // inform filter about dataset **AFTER** setting options
        Instances newTrainInstances = Filter.useFilter(trainInstances, remove);   // apply filter
        newTrainInstances.setClassIndex(newTrainInstances.numAttributes() - 1);
        useTestSetOpinionWords(newTrainInstances, testFilePath);
    }
    
    public static void useTestSetOpinionWords(Instances trainInstances, String testFilePath) throws Exception {
        Instances testInstances = new Instances(new BufferedReader(new FileReader(testFilePath)));
        PrintWriter writer = new PrintWriter(new File(".\\trang.txt"), "UTF-8");

        Remove remove = new Remove(); 
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,2,3";    // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(testInstances);                          // inform filter about dataset **AFTER** setting options
        Instances newTestInstances = Filter.useFilter(testInstances, remove);   // apply filter
        // setting class attribute 
        newTestInstances.setClassIndex(newTestInstances.numAttributes() - 1);
        J48 tree = new J48();
        tree.buildClassifier(trainInstances);
        for (int i = 0; i < newTestInstances.numInstances(); i++) {
            double index = tree.classifyInstance(newTestInstances.instance(i));
            String classPredicted = newTestInstances.classAttribute().value((int) index);
            String classActual = newTestInstances.classAttribute().value((int) newTestInstances.instance(i).classValue());
            
            if (classActual.equals("true")) {
                int reviewId = (int) testInstances.instance(i).value(0);
                int np1Id = (int) testInstances.instance(i).value(1);
                int np2Id = (int) testInstances.instance(i).value(2);

                StanfordUtil.reviews.get(reviewId).addCorefChainActual(np1Id, np2Id);
            }
            
            
            //If 2 instances is coref, consider to add them to COREFs chain of the review
            if (classPredicted.equals("true")) {
                int reviewId = (int) testInstances.instance(i).value(0);
                int np1Id = (int) testInstances.instance(i).value(1);
                int np2Id = (int) testInstances.instance(i).value(2);

                StanfordUtil.reviews.get(reviewId).addCorefChainPredict(np1Id, np2Id);
            }
//            System.out.println("Class Actual = " + classActual);
//            for (int j = 0; j < data.numAttributes(); j++){
//            	writer.print(data.instance(i).value(j) + ",");
//            }
//            writer.println();
        }
//        Evaluation eval = new Evaluation(trainInstances);
//        eval.evaluateModel(tree, newTestInstances);
//        writer.println(eval.toSummaryString("=== Result: ===", false));
//        writer.println(eval.toClassDetailsString());
//        writer.println(eval.toMatrixString());
        writer.close();
        System.out.println("Done");
    }
}
