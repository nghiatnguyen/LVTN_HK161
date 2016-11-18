/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.process;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.util.Util;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.CrfChunkerUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
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

        // Read the Dataset
        System.out.println("Reading dataset");
        File fData = new File(".\\dataset.txt");
        FileReader fReaderData = new FileReader(fData);
        BufferedReader buffReaderDict = new BufferedReader(fReaderData);
        String sData = null;
        String line;
        while ((line = buffReaderDict.readLine()) != null) {
            sData = sData + line + "\n";
        }
        System.out.println("End of Reading dataset");
        MarkupMain.set_sDataset(sData);

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
                + "@ATTRIBUTE isbothpronoun {false,true}\n"
                + "@ATTRIBUTE isbothnormal {false,true}\n"
                + "@ATTRIBUTE issubstring {false,true}\n"
                + "@ATTRIBUTE isheadmatch {false,true}\n"
                + "@ATTRIBUTE isexactmatch {false,true}\n"
                + "@ATTRIBUTE ismatchafterremovedetermine {false,true}\n"
                + "@ATTRIBUTE distance REAL\n"
                + "@ATTRIBUTE numberagreement {false,true}\n"
                + "@ATTRIBUTE isbetween {false,true}\n"
                + "@ATTRIBUTE hasbetween {false,true}\n"
                + "@ATTRIBUTE comparative {false,true}\n"
                + "@ATTRIBUTE sentiment REAL\n"
                + "@ATTRIBUTE bothPronoun REAL\n"
                + "@ATTRIBUTE bothNormal REAL\n"
                + "@ATTRIBUTE subString REAL\n"
                + "@ATTRIBUTE headMatch REAL\n"
                + "@ATTRIBUTE exactMatch REAL\n"
                + "@ATTRIBUTE matchAfterRemoveDetermine REAL\n"
                + "@ATTRIBUTE PMI {0,1,2,3,4,10}\n"
                + "@ATTRIBUTE coref {false,true}\n"
                + "\n"
                + "@DATA");
        bw.newLine();

        try {
            //Init every info
            su.init();

            //Call CRFChunker, result is in input.txt.pos.chk file
            CrfChunkerUtil.runChunk();

            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();

            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);

            //Begin create training set
            for (Review review : StanfordUtil.reviews) {
            //Discard all NPs that is Personal Pronoun
                Util.discardUnneccessaryNPs(review);
            }                        

            //Read the hand-modified markup file
            Util.readMarkupFile(StanfordUtil.reviews, markupFile);

            //Begin create training set
            for (Review review : StanfordUtil.reviews) {                
                //Extract features
                Util.extractFeatures(review, bw, forTraining);
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        bw.close();
    }
}
