package coreferenceresolver.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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

            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();

            //Assign NPs obtained from Chunker to StanfordUtil reviews
            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);

            //Discard some NPs
            StanfordUtil.reviews.forEach((review) -> {
                Util.discardUnneccessaryNPs(review);
            });

            //Read the hand-modified markup file
            Util.readMarkupFile(StanfordUtil.reviews, markupFile);

            //Initialize sentiment and comparatives for each NP in each review
            Util.initSentimentAndComparativesForNPs();
            
            //Set list instances of each review
            for (Review re : StanfordUtil.reviews)
            	Util.setInstancesForReviews(re);
            
            //Create list random element's order
            ArrayList<Integer> shuffleList = new ArrayList<>();
    		for (int i = 0; i < StanfordUtil.reviews.size(); i++)
    			shuffleList.add(i);
    		Collections.shuffle(shuffleList);
            
    		ArrayList<ArrayList<Review>> listForTrains = new ArrayList<ArrayList<Review>>();
    		ArrayList<ArrayList<Review>> listForTests = new ArrayList<ArrayList<Review>>();
    		//Create the kFold dataset
            for (int i = 1; i <= kFold; i++){
	            ArrayList<Review> listForTrain = new ArrayList<>();
	            ArrayList<Review> listForTest = new ArrayList<>();
	            int sizeEachFold = StanfordUtil.reviews.size()/kFold + 1;
	            for (int j = 0; j < StanfordUtil.reviews.size(); j++){
	            	if (j >= i*sizeEachFold - sizeEachFold && j < i*sizeEachFold)
	            		listForTest.add(StanfordUtil.reviews.get(shuffleList.get(j)));
	            	else
	            		listForTrain.add(StanfordUtil.reviews.get(shuffleList.get(j)));
	            }
	            listForTrains.add(listForTrain);
	            listForTests.add(listForTest); 
	            String strTrainFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\trainFile" + i + ".arff";
	            String strTestFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\testFile" + i + ".arff";
	            File outputTrainFile = new File(strTrainFile);
	            try {
	            	System.out.println("Writing the fileTrain " + i);
					run(listForTrain, strTrainFile, true);
					System.out.println("Done fileTrain " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            File outputTestFile = new File(strTestFile);
	            try {
	            	System.out.println("Writing the fileTest " + i);
					run(listForTest, strTestFile, false);
					System.out.println("Done fileTest " + i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            
            //Run the kFold
            for (int i = 1; i <= kFold; i++){
            	 String strTrainFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\trainFile" + i + ".arff";
 	            String strTestFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\testFile" + i + ".arff";
 	            String strResultFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\result.txt";
            	Weka.j48Classify(strTrainFile, strTestFile, strResultFile);
            	System.out.println("-----Fold " + i + "-----");
            	Evaluation.scoreMUC(listForTests.get(i-1));
            	Evaluation.scoreB3(listForTests.get(i-1));
            	Evaluation.scoreCEAF4(listForTests.get(i-1));
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
                + "@ATTRIBUTE sentiment REAL\n"
                + "@ATTRIBUTE bothpropername {false,true}\n"
                + "@ATTRIBUTE np1propername {false,true}\n"
                + "@ATTRIBUTE np2propername {false,true}\n"
                + "@ATTRIBUTE bothPronoun {false,true}\n"
                + "@ATTRIBUTE bothNormal {false,true}\n"
                + "@ATTRIBUTE subString {false,true}\n"
                + "@ATTRIBUTE headMatch {false,true}\n"
                + "@ATTRIBUTE exactMatch {false,true}\n"
                + "@ATTRIBUTE matchAfterRemoveDetermine {false,true}\n"
                + "@ATTRIBUTE PMI {0,1,2,3,4,10,11,12}\n"
                //                + "@ATTRIBUTE headPhone {false,true}\n"
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
            		if (forTraining){
            			if (review.getSupportInstances().get(i) == true){
            				bw.write(review.getInstances().get(i));
            				bw.newLine();
            			}
            		}
            		else{
            			bw.write(review.getInstances().get(i));
        				bw.newLine();
            		}
            	}
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        bw.close();
    }
}
