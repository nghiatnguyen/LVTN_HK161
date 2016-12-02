/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.process;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.test.Evaluation;
import coreferenceresolver.util.CrfChunkerUtil;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.util.Util;
import coreferenceresolver.weka.Weka;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class WekaMain {

    public static void run(String inputFilePath, String markupFilePath, String trainingFilePath, String testFilePath, String resultFilePath) throws Exception {
        File inputFile = new File(inputFilePath);
        StanfordUtil su = new StanfordUtil(inputFile);

        try {
            //Init every info
            su.init(true);

            //Call CRFChunker, result is in input.txt.pos.chk file
            CrfChunkerUtil.runChunk();

            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();

            //Assign NPs obtained from Chunker to StanfordUtil reviews
            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);

            for (Review review : StanfordUtil.reviews) {
                Util.discardUnneccessaryNPs(review);
            }

            //Read the hand-modified markup file
            Util.readMarkupFile(StanfordUtil.reviews, new File(markupFilePath));
            
            Weka.j48Classify(trainingFilePath, testFilePath, resultFilePath);

            Util.discardUnneccessaryChains(StanfordUtil.reviews);
            
            int No = 0;
            //Evaluation MUC score
            for (Review review : StanfordUtil.reviews) {
                System.out.println("--------Review " + No + "-----------");
                for (CorefChain cf : review.getCorefChainsPredicted()) {
                    System.out.println("Predict chain: " + cf.getChain().toString()
                            + "; Size: " + coreferenceresolver.process.Evaluation.findSize(cf)
                            + "; Numpartitions: " + coreferenceresolver.process.Evaluation.findNumPartitions(cf, review.getCorefChainsActual()));
                }
                for (CorefChain cf : review.getCorefChainsActual()) {
                    System.out.println("Actual Chain: " + cf.getChain().toString()
                            + "; Size: " + coreferenceresolver.process.Evaluation.findSize(cf)
                            + "; Numpartitions: " + coreferenceresolver.process.Evaluation.findNumPartitions(cf, review.getCorefChainsPredicted()));
                }
                No++;
            }
            //TODO Write these to file
            Evaluation.scoreMUC(StanfordUtil.reviews);
            Evaluation.scoreB3(StanfordUtil.reviews);
//            Evaluation.scoreCEAF4(StanfordUtil.reviews);

        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
