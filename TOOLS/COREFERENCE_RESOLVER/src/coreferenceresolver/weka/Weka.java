package coreferenceresolver.weka;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

public class Weka {

    public static void j48Classify(String testFilePath, String resultFilePath) throws Exception {                
        BufferedReader reader = new BufferedReader(
                new FileReader(testFilePath));
        Instances data = new Instances(reader);
        reader.close();

        //Remove 1st and 4th features
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,4";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
        Instances newData = Filter.useFilter(data, remove);   // apply filter
        // setting class attribute 
        newData.setClassIndex(newData.numAttributes() - 1);

//		useCrossValidation(newData);
        useTestSet(newData, testFilePath, resultFilePath);
    }

    public static void main(String[] agrs) throws Exception {

        BufferedReader reader = new BufferedReader(
                new FileReader(".\\test.arff"));
        Instances data = new Instances(reader);
        reader.close();

        //Remove 1st and 4th features
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,4";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
        Instances newData = Filter.useFilter(data, remove);   // apply filter
        // setting class attribute 
        newData.setClassIndex(newData.numAttributes() - 1);

//		useCrossValidation(newData);
        useTestSet(newData, ".\\test.arff", ".\\result.txt");

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

    public static void useTestSet(Instances inst, String testFilePath, String resultFilePath) throws Exception {
        Instances data = new Instances(new BufferedReader(new FileReader(testFilePath)));
        PrintWriter writer = new PrintWriter(resultFilePath, "UTF-8");
        //Remove 1st and 4th features
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1,4";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(options);                           // set options
        remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
        Instances test = Filter.useFilter(data, remove);   // apply filter
        // setting class attribute 
        test.setClassIndex(test.numAttributes() - 1);
        J48 tree = new J48();
        tree.buildClassifier(inst);
        for (int i = 0; i < test.numInstances(); i++) {
            double index = tree.classifyInstance(test.instance(i));
            String className = inst.attribute(inst.numAttributes() - 1).value((int) index);
//                    System.out.println(data.instance(i) + " : " + className);
//                    if (data.instance(i).stringValue(4).equals("true")
//                    		&& data.instance(i).stringValue(5).equals("true"))
                    if  (data.instance(i).stringValue(7).equals("true"))
                    	writer.println(data.instance(i) + " : " + className);
				}
				Evaluation eval = new Evaluation(test);
				eval.evaluateModel(tree,test);
//				System.out.println(eval.toSummaryString("=== Result: ===", false));
//			    System.out.println(eval.toClassDetailsString());
//			    System.out.println(eval.toMatrixString());
        writer.println(eval.toSummaryString("=== Result: ===", false));
        writer.println(eval.toClassDetailsString());
        writer.println(eval.toMatrixString());
        writer.close();
        System.out.println("Done");

    }
}
