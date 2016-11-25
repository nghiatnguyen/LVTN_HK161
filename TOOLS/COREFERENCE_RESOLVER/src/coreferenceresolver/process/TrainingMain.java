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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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

    public static void run(String inputFilePath, String markupFilePath, String outputFilePath, boolean forTraining) throws IOException, Exception {
        File inputFile = new File(inputFilePath);
        File markupFile = new File(markupFilePath);
        File outputFile = new File(outputFilePath);
        StanfordUtil su = new StanfordUtil(inputFile);

        Util.readDataset();

        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        String relationName = forTraining ? "train" : "test";
        bw.write("@RELATION " + relationName + "\n"
                + "\n"
                + "@ATTRIBUTE review REAL\n"
                + "@ATTRIBUTE np1 REAL\n"
                + "@ATTRIBUTE np2 REAL\n"
                //                + "@ATTRIBUTE np1ispr {false,true}\n"
                //                + "@ATTRIBUTE np2ispr {false,true}\n"
                //                + "@ATTRIBUTE np2isdefnp {false,true}\n"
                //                + "@ATTRIBUTE np2isdemnp {false,true}\n"
                //                + "@ATTRIBUTE distance REAL\n"
                //                + "@ATTRIBUTE numberagreement {false,true}\n"
                //                + "@ATTRIBUTE isbetween {false,true}\n"
                //                + "@ATTRIBUTE hasbetween {false,true}\n"
                //                + "@ATTRIBUTE comparative {false,true}\n"
                + "@ATTRIBUTE sentiment REAL\n"
                //                + "@ATTRIBUTE bothpropername {false,true}\n"
                //                + "@ATTRIBUTE np1propername {false,true}\n"
                //                + "@ATTRIBUTE np2propername {false,true}\n"
                //                + "@ATTRIBUTE bothPronoun {false,true}\n"
                //                + "@ATTRIBUTE bothNormal {false,true}\n"
                //                + "@ATTRIBUTE subString {false,true}\n"
                //                + "@ATTRIBUTE headMatch {false,true}\n"
                //                + "@ATTRIBUTE exactMatch {false,true}\n"
                //                + "@ATTRIBUTE matchAfterRemoveDetermine {false,true}\n"
                //                + "@ATTRIBUTE PMI {0,1,2,3,4,10,12}\n"
                //                + "@ATTRIBUTE headPhone {false,true}\n"
                + "@ATTRIBUTE coref {false,true}\n"
                + "\n"
                + "@DATA");
        bw.newLine();

        try {
            FeatureExtractor.loadSDict();
            //Init every info
            su.init(false);

            //Call CRFChunker, result is in input.txt.pos.chk file
            CrfChunkerUtil.runChunk();

            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();

            Util.checkPOSFilesMatchingInput(StanfordUtil.reviews);

            //Assign NPs obtained from Chunker to StanfordUtil reviews
            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);

            //Initialize sentiment and comparatives for each NP in each review
            Util.initSentimentAndComparativesForNPs();

            //Discard some NPs
            StanfordUtil.reviews.forEach((review) -> {
                Util.discardUnneccessaryNPs(review);
            });

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
