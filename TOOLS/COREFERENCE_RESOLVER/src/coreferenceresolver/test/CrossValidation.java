package coreferenceresolver.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.test.Evaluation;
import coreferenceresolver.process.FeatureExtractor;
import coreferenceresolver.process.MarkupMain;
import coreferenceresolver.util.CrfChunkerUtil;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.util.Util;
import coreferenceresolver.weka.Weka;

public class CrossValidation {
	
	private static int kFold = 5;
	
	public static int getkFold(){
		return kFold;
	}
	public static void main(String[] args) throws Exception{
		File inputFile = new File("E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\input.txt");
        File markupFile = new File("E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\input.txt.markup");
        StanfordUtil su = new StanfordUtil(inputFile);
        

        Util.readDataset();
        try {
            FeatureExtractor.loadSDict();
            //Init every info
            System.out.println("Stanford Init");
            su.init(false);
            System.out.println("End of Stanford Init");

            //Call CRFChunker, result is in input.txt.pos.chk file
            CrfChunkerUtil.runChunk();
            System.out.println("Done CRFChunker");
            
            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();
            System.out.println("Done Get all NPs");
            
            //Assign NPs obtained from Chunker to StanfordUtil reviews
            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);
            System.out.println("Done Assign NPs to Stanford NPs");
            
            //Discard some NPs
            StanfordUtil.reviews.forEach((review) -> {
//            	Set OWs and NPs
            	//EDIT CREATE
//                for (int i = 0; i < review.getSentences().size(); i++) {
//                    FeatureExtractor.setNPForOPInSentence(review.getSentences().get(i));
//                }
                Util.discardUnneccessaryNPs(review);
            });
            System.out.println("Done Discard NPs and set OWs for NPs");

            //Read the hand-modified markup file
            Util.readMarkupFile(StanfordUtil.reviews, markupFile);
            System.out.println("Done Read Markup NPs");

            //Initialize sentiment and comparatives for each NP in each review
            Util.initSentimentAndComparativesForNPs();
            System.out.println("Done init Sentiment and Comparatives for NPs");
            
            
            //Set list instances of each review
          //EDIT CREATE
//            for (Review re : StanfordUtil.reviews){
//            	Util.setInstancesForReviews(re);
//            }
//            System.out.println("Done create instances for all NPs");

            
            //Create list random element's order
//            ArrayList<Integer> shuffleList = new ArrayList<>();
//    		for (int i = 0; i < StanfordUtil.reviews.size(); i++)
//    			shuffleList.add(i);
//    		Collections.shuffle(shuffleList);
            
            ArrayList<ArrayList<Integer>> listForIndexTests = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> listIndex1 = new ArrayList<Integer>(Arrays.asList(66,115,10,35,144,154,32,118,14,116,60,81,96,4,120,131,1,26,119,37,36,5,44,55,20,13,123,93,97,126,122,106));
            ArrayList<Integer> listIndex2 = new ArrayList<Integer>(Arrays.asList(54,72,62,15,94,28,105,75,25,0,129,77,80,31,82,142,153,67,109,24,124,104,42,17,63,49,152,23,30,27,100,52));
            ArrayList<Integer> listIndex3 = new ArrayList<Integer>(Arrays.asList(57,56,91,21,138,89,64,140,29,61,40,16,90,114,71,137,113,19,79,133,38,132,88,50,145,143,2,69,83,141,125,130));
            ArrayList<Integer> listIndex4 = new ArrayList<Integer>(Arrays.asList(103,45,85,111,39,92,84,101,148,136,78,46,107,11,146,73,8,12,18,95,99,128,41,33,135,53,147,155,43,9,76,59));
            ArrayList<Integer> listIndex5 = new ArrayList<Integer>(Arrays.asList(149,134,87,34,127,74,6,7,108,121,98,151,110,117,139,3,102,86,47,68,150,58,70,156,51,112,48,22,65));
            listForIndexTests.add(listIndex1);
            listForIndexTests.add(listIndex2);
            listForIndexTests.add(listIndex3);
            listForIndexTests.add(listIndex4);
            listForIndexTests.add(listIndex5);
            
//            ArrayList<ArrayList<Review>> listForTrains = new ArrayList<ArrayList<Review>>();
//          ArrayList<ArrayList<Review>> listForTests = new ArrayList<ArrayList<Review>>();
           
            //if you want to write the training files and testing files again, please uncomment under codes
    		//Create the kFold dataset
          //EDIT CREATE
//            for (int i = 1; i <= kFold; i++){
//	            ArrayList<Review> listForTrain = new ArrayList<>();
//	            ArrayList<Review> listForTest = new ArrayList<>();
////	            int sizeEachFold = StanfordUtil.reviews.size()/kFold + 1;
//	            for (int j = 0; j < StanfordUtil.reviews.size(); j++){
////	            	if (j >= i*sizeEachFold - sizeEachFold && j < i*sizeEachFold){
////	            		listForTest.add(StanfordUtil.reviews.get(shuffleList.get(j)));
////	            	}
////	            	else
////	            		listForTrain.add(StanfordUtil.reviews.get(shuffleList.get(j)));
//	            	
//	            	if (listForIndexTests.get(i-1).contains(j))
//	            		listForTest.add(StanfordUtil.reviews.get(j));
//	            	else 
//	            		listForTrain.add(StanfordUtil.reviews.get(j));
//	            }
////	            listForTests.add(listForTest);
////	            listForTrains.add(listForTrain);
//	            String strTrainFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\kfold\\trainFile" + i + ".arff";
//	            String strTestFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\kfold\\testFile" + i + ".arff";
////	            File outputTrainFile = new File(strTrainFile);
//	            try {
//	            	System.out.println("Writing the fileTrain " + i);
//					run(listForTrain, strTrainFile, true);
//					System.out.println("Done fileTrain " + i);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////	            File outputTestFile = new File(strTestFile);
//	            try {
//	            	System.out.println("Writing the fileTest " + i);
//					run(listForTest, strTestFile, false);
//					System.out.println("Done fileTest " + i);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
               
            //Run the kFold
            for (int i = 1; i <= kFold; i++){
            	 String strTrainFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\kfold\\trainFile" + i + ".arff";
 	            String strTestFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\kfold\\testFile" + i + ".arff";
// 	            String strResultFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\result.txt";
            	Weka.trainJustOpinionWords(strTrainFile, strTestFile);
            	System.out.println("-----Fold " + i + "-----");
            	ArrayList<Review> listForTest = new ArrayList<>();
            	for (int j = 0; j < StanfordUtil.reviews.size(); j++)
            		if (listForIndexTests.get(i-1).contains(j))
            			listForTest.add(StanfordUtil.reviews.get(j));
            	Evaluation.scoreMUC(listForTest);
            	Evaluation.scoreB3(listForTest);
           	    Evaluation.scoreCEAF4(listForTest);
            }
            
            Evaluation.resultFinal();
            
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	
	public static void run(List<Review> listReview, String outputFilePath, boolean forTraining) throws IOException, Exception {
        File outputFile = new File(outputFilePath);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        String relationName = forTraining ? "train" : "test";
        bw.write("@RELATION " + relationName + "\n"
                + "\n"
                + "@ATTRIBUTE review REAL\n"
                + "@ATTRIBUTE np1 REAL\n"
                + "@ATTRIBUTE np2 REAL\n"
                + "@ATTRIBUTE np1ispr {false,true}\n"
                + "@ATTRIBUTE np2ispr {false,true}\n"
                + "@ATTRIBUTE np2isdefnp {false,true}\n"
                + "@ATTRIBUTE np2isdemnp {false,true}\n"
                + "@ATTRIBUTE distance REAL\n"
                + "@ATTRIBUTE numberagreement {false,true}\n"
                + "@ATTRIBUTE isbetween {false,true}\n"
                + "@ATTRIBUTE hasbetween {false,true}\n"
                + "@ATTRIBUTE comparative {false,true}\n"
                + "@ATTRIBUTE sentiment {0,1,2}\n"
                + "@ATTRIBUTE bothpropername {false,true}\n"
                + "@ATTRIBUTE subString {false,true}\n"
                + "@ATTRIBUTE headMatch {false,true}\n"
                + "@ATTRIBUTE exactMatch {false,true}\n"
                + "@ATTRIBUTE PMI {0,1,2,3,4,10,11}\n"
                + "@ATTRIBUTE relativeClause {false,true}\n"
                + "@ATTRIBUTE coref {false,true}\n"
                + "\n"
                + "@DATA");
        bw.newLine();

        try {
            //Begin create training set
            for (Review review : listReview) {
                //Extract features
            	for (int i = 0; i < review.getInstances().size(); i++){
            		bw.write(review.getInstances().get(i));
    				bw.newLine();
            	}
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        bw.close();
    }
}
