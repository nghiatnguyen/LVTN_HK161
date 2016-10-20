/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class TrainingMain {
    
    public static void run(String inputFilePath, String markupFilePath, String outputFilePath, boolean forTraining) throws IOException {
        File inputFile = new File(inputFilePath);
        File markupFile = new File(markupFilePath);
        File outputFile = new File(outputFilePath);
        StanfordUtil su = new StanfordUtil(inputFile);

        // read the Dataset
//        File fData = new File(".\\dataset.txt");
//        FileReader fReaderData = new FileReader(fData);
//        BufferedReader buffReaderDict = new BufferedReader(fReaderData);
//        String sData = null;
//        String line;
//        while ((line = buffReaderDict.readLine()) != null) {
//            sData = sData + line + "\n";
//        }
//        MarkupMain.set_sDataset(sData);
        
<<<<<<< HEAD
        
      //Write to train.txt
//        File ftrain = new File("train.txt");
//    	FileOutputStream fostrain = new FileOutputStream(ftrain);
//    	BufferedWriter bwtrain = new BufferedWriter(new OutputStreamWriter(fostrain));
//    	bwtrain.write("Review,NP1,NP2,NP1isPr,NP2isPr,NP2isDefNP,NP2isDemNP,isBothPropername,Stringsimilary,Distance,NumberAgreement,isBetween,hasBetween,Comparative,COREF");
//		bwtrain.newLine();
		
		//Write to test.txt
//        File ftest = new File("test.txt");
//    	FileOutputStream fostest = new FileOutputStream(ftest);
//    	BufferedWriter bwtest = new BufferedWriter(new OutputStreamWriter(fostest));
//    	bwtest.write("Review,NP1,NP2,NP1isPr,NP2isPr,NP2isDefNP,NP2isDemNP,isBothPropername,Stringsimilary,Distance,NumberAgreement,isBetween,hasBetween,Comparative,COREF");
//		bwtest.newLine();
    	
    	//Write to check features of each NP
        File fcheck = new File("check.txt");
    	FileOutputStream foscheck = new FileOutputStream(fcheck);
    	BufferedWriter bwcheck = new BufferedWriter(new OutputStreamWriter(foscheck));	
=======
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        String relationName = forTraining ? "train" : "test";
        bw.write("@RELATION " + relationName + "\n"
                + "\n"
                + "@ATTRIBUTE review REAL\n"
                + "@ATTRIBUTE np1 REAL\n"
                + "@ATTRIBUTE np2 REAL\n"
                + "@ATTRIBUTE npdistance REAL\n"
                + "@ATTRIBUTE np1ispr {false,true}\n"
                + "@ATTRIBUTE np2ispr {false,true}\n"
                + "@ATTRIBUTE np2isdefnp {false,true}\n"
                + "@ATTRIBUTE np2isdemnp {false,true}\n"
                + "@ATTRIBUTE isbothpropername {false,true}\n"
                + "@ATTRIBUTE stringsimilary {false,true}\n"
                + "@ATTRIBUTE distance REAL\n"
                + "@ATTRIBUTE numberagreement {false,true}\n"
                + "@ATTRIBUTE isbetween {false,true}\n"
                + "@ATTRIBUTE hasbetween {false,true}\n"
                + "@ATTRIBUTE comparative {false,true}\n"
                + "@ATTRIBUTE sentiment REAL\n"
                + "@ATTRIBUTE PMI REAL\n"
                + "@ATTRIBUTE coref {false,true}\n"
                + "\n"
                + "@DATA");
        bw.newLine();

//        Write to check features of each NP
//        File fcheck = new File("check.txt");
//    	FileOutputStream foscheck = new FileOutputStream(fcheck);
//    	BufferedWriter bwcheck = new BufferedWriter(new OutputStreamWriter(foscheck));	
>>>>>>> cbee46ad9a37728e4124963c1fa703ca37c8a54a
        try {
            //Init every info
            su.init();

            //Begin create training set
            for (Review review : StanfordUtil.reviews) {

                //Discard all NPs that is Personal Pronoun
                Util.discardPersonalProNPs(review);

                //Read the hand-modified markup file
                Util.readMarkupFile(markupFile);

                //Extract features
<<<<<<< HEAD
//                Util.extractFeatures(review,bwtrain);
//                Util.extractFeatures(review,bwtest);
                Util.extractFeatures(review,bwcheck);
 //               StanfordUtil.test();

                System.out.println("-----END REVIEW-----");

                ++i;
=======
                Util.extractFeatures(review, bw, forTraining);
>>>>>>> cbee46ad9a37728e4124963c1fa703ca37c8a54a
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
<<<<<<< HEAD
//        bwtrain.close();
//        bwtest.close();
        bwcheck.close();
=======
        bw.close();
>>>>>>> cbee46ad9a37728e4124963c1fa703ca37c8a54a
    }
}
