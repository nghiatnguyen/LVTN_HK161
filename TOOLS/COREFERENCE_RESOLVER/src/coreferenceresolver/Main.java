/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class Main {
    
    FeatureExtractor fe = new FeatureExtractor();

    public static void featureExtract(Review review) {
        System.out.println("-------------------------");
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase np1 = review.getNounPhrases().get(i);
            for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
                NounPhrase np2 = review.getNounPhrases().get(j);
                System.out.println("-------------------------");
                System.out.println("NP1 words: " + np1.getNpNode().getLeaves());
                System.out.println("NP1 head label: " + np1.getHeadLabel());
                System.out.println("NP1 head: " + np1.getHeadNode());
                System.out.println("NP1 begin: " + np1.getOffsetBegin());
                System.out.println("NP1 end: " + np1.getOffsetEnd());
                System.out.println("NP1 review: " + np1.getReviewId());
                System.out.println("NP1 sentence: " + np1.getSentenceId());
                System.out.println("------------");
                System.out.println("NP2 words: " + np2.getNpNode().getLeaves());
                System.out.println("NP2 head label: " + np2.getHeadLabel());
                System.out.println("NP2 head: " + np2.getHeadNode());
                System.out.println("NP2 begin: " + np2.getOffsetBegin());
                System.out.println("NP2 end: " + np2.getOffsetEnd());
                System.out.println("NP2 review: " + np2.getReviewId());
                System.out.println("NP2 sentence: " + np2.getSentenceId());
                System.out.println("------------");
                System.out.println("number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
                System.out.println("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(StanfordUtil.reviews, np1, np2));
                try {
                    System.out.println("is-between: " + FeatureExtractor.isBetweenExtract(StanfordUtil.reviews, np1, np2));
                } catch (Exception e) {
                    System.out.println("Exception NP1 words: " + np1.getNpNode().getLeaves());
                    System.out.println("Exception NP2 words: " + np2.getNpNode().getLeaves());
                }

                System.out.println("------------");
                System.out.println("--------------------------");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File inputFile = new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\NOUN_PHRASE_FINDER\\DomParser\\input.txt");
        StanfordUtil su = new StanfordUtil(inputFile);        
        try {
            su.init();
            int i = 0;
            for (Review review : StanfordUtil.reviews){
                System.out.println("-----REVIEW-----");
                System.out.println("Extract from review " + i);                
                featureExtract(review);
                System.out.println("-----END OF REVIEW-----");
                ++i;
            }
//            StanfordUtil.test();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
