/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class Main {

    FeatureExtractor fe = new FeatureExtractor();

    private static String DISCARDED_PERSONAL_PRONOUNS = ";i;me;you;he;him;his;she;her;hers;";

    private static boolean isDiscardedPersonalPronoun(NounPhrase np) {
        if (DISCARDED_PERSONAL_PRONOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    public static void discardPersonalPronoun(Review review) {
        List nps = review.getNounPhrases();
        Iterator<NounPhrase> itr = nps.iterator();

        while (itr.hasNext()) {
            NounPhrase np = itr.next();
            if (isDiscardedPersonalPronoun(np)) {
                itr.remove();
            }
        }
    }

    public static void featureExtract(Review review) {
        discardPersonalPronoun(review);
        System.out.println("All NPs in this review:");
        for (NounPhrase np : review.getNounPhrases()) {
            System.out.print(np.getNpNode().getLeaves() + "  ");
        }
        System.out.println();
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase np1 = review.getNounPhrases().get(i);
            for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
                NounPhrase np2 = review.getNounPhrases().get(j);
                System.out.println("-----------NP pair--------------");
                System.out.println("In review");
                System.out.println(review.getRawContent());
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
                try {
                    System.out.println("number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
                    System.out.println("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(review, np1, np2));
                    System.out.println("is-between: " + FeatureExtractor.isBetweenExtract(review, np1, np2));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Exception NP1 words: " + np1.getNpNode().getLeaves());
                    System.out.println("Exception NP2 words: " + np2.getNpNode().getLeaves());
                }

                System.out.println("------------End of NP pair--------------");
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
            for (Review review : StanfordUtil.reviews) {
                System.out.println("-----BEGIN REVIEW-----");
                System.out.println("Extract from review " + i);
                featureExtract(review);
                System.out.println("-----END REVIEW-----");
                ++i;
            }
//            StanfordUtil.test();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
