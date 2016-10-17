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

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File inputFile = new File(".\\input.txt");
        File markupFile = new File(".\\markup1.out.txt");
        StanfordUtil su = new StanfordUtil(inputFile);

        // read the Dataset
        File fData = new File(".\\dataset.txt");
        FileReader fReaderData = new FileReader(fData);
        BufferedReader buffReaderDict = new BufferedReader(fReaderData);
        String sData = null;
        String line;
        while ((line = buffReaderDict.readLine()) != null) {
            sData = sData + line + "\n";
        }
        MarkupMain.set_sDataset(sData);
        
        
      //Write to train.txt
        File ftrain = new File("train.txt");
    	FileOutputStream fostrain = new FileOutputStream(ftrain);
    	BufferedWriter bwtrain = new BufferedWriter(new OutputStreamWriter(fostrain));
    	bwtrain.write("Review,NP1,NP2,NP1isPr,NP2 is Pr,NP2isDefNP,NP2isDemNP,Stringsimilary,Distance,NumberAgreement,isBetween,hasBetween,Comparative,PMI,COREF");
		bwtrain.newLine();
		
        try {
            //Init every info
            su.init();

            int i = 0;
            //Begin create training set
            for (Review review : StanfordUtil.reviews) {

                //Discard all NPs that is Personal Pronoun
                Util.discardPersonalProNPs(review);

                //Read the hand-modified markup file
                Util.readMarkupFile(markupFile);

                StanfordUtil.test();
                System.out.println("-----BEGIN REVIEW " + i + "-----");

                //Extract features
                Util.extractFeatures(review,bwtrain);
//                StanfordUtil.test();

                System.out.println("-----END REVIEW-----");

                ++i;
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        bwtrain.close();
    }
}
