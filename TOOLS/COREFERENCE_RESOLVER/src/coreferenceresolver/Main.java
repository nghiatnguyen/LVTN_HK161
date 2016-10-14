/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private static String sDataset = null;
    private static BufferedReader buffReaderDict;
    
    public static String get_sDataset(){
		return sDataset;
	}

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
        
        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++)
        	FeatureExtractor.set_NP_for_OP_in_sentence(review.getSentences().get(i));
        	
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
                System.out.print("Opinion words: ");
                for (int k = 0; k < np1.getOpinionWords().size(); k++)
                	System.out.print(np1.getOpinionWords().get(k) + " ; ");
                System.out.println();
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
//                    System.out.println("NP1 is Pronoun: " + FeatureExtractor.is_Pronoun(np1));
//                    System.out.println("NP2 is Pronoun: " + FeatureExtractor.is_Pronoun(np2));
//                    System.out.println("NP2 is Definite Noun Phrase: " + FeatureExtractor.is_Definite_NP(np2));
//                    System.out.println("NP2 is Demonstrative Noun Phrase: " + FeatureExtractor.is_Demonstrative_NP(np2));
//                    System.out.println("String similarity: " + FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())));
//                    System.out.println("Distance Feature: " + FeatureExtractor.count_Distance(np1, np2));
//                    System.out.println("number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
                    System.out.println("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(review, np1, np2));
                    System.out.println("is-between: " + FeatureExtractor.isBetweenExtract(review, np1, np2));
//                    System.out.println("has-between: " + FeatureExtractor.has_Between_Extract(review, np1, np2));
//                    System.out.println("***Entity and opinion words association***");
//                    System.out.println("Probability of Opinion Word of NP1: " + FeatureExtractor.probability_opinion_word(np1));
//                    System.out.println("Probability of NP2: " + FeatureExtractor.probability_noun_phrase(np2));
//                    System.out.println("Probability of (NP2 and Opinion Word of NP1): " + FeatureExtractor.probability_NP_and_OW(np1, np2));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Exception NP1 words: " + np1.getNpNode().getLeaves());
                    System.out.println("Exception NP2 words: " + np2.getNpNode().getLeaves());
                }

                System.out.println("------------End of NP pair--------------");
            }
            
//            System.out.println("------------");
//            System.out.println("NP1 words: " + np1.getNpNode().getLeaves());
//            System.out.println("NP1 head label: " + np1.getHeadLabel());
//            System.out.println("NP1 head: " + np1.getHeadNode());
//            System.out.println("NP1 begin: " + np1.getOffsetBegin());
//            System.out.println("NP1 end: " + np1.getOffsetEnd());
//            System.out.println("NP1 review: " + np1.getReviewId());
//            System.out.println("NP1 sentence: " + np1.getSentenceId());
//            System.out.println("is Pronoun: " + FeatureExtractor.is_Pronoun(np1));
//            System.out.println("is Definite Noun Phrase: " + FeatureExtractor.is_Definite_NP(np1));
//            System.out.println("is Demonstrative Noun Phrase: " + FeatureExtractor.is_Demonstrative_NP(np1));
//            System.out.println("is Proper name: " + FeatureExtractor.is_Proper_name(np1));
//            System.out.print("Opinion words: ");
//            for (int k = 0; k < np1.getOpinionWords().size(); k++)
//            	System.out.print(np1.getOpinionWords().get(k) + " ; ");
//            System.out.println();
        }
        	
    }

    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File inputFile = new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\NOUN_PHRASE_FINDER\\DomParser\\input.txt");
        StanfordUtil su = new StanfordUtil(inputFile);
        //Read the big database to find relation between NP and OW
        // read the Dataset
		File fData = new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\NOUN_PHRASE_FINDER\\DomParser\\dataset.txt");
		FileReader fReaderData = new FileReader(fData);
		buffReaderDict = new BufferedReader(fReaderData);
		String line;
		while ((line = buffReaderDict.readLine()) != null){
			sDataset = sDataset + line + "\n";
		}
		
		
        try {
            su.init();
            int i = 0;
//            for (Review review : StanfordUtil.reviews) {
//                System.out.println("-----BEGIN REVIEW-----");
//                System.out.println("Extract from review " + i);
//                featureExtract(review);
//                System.out.println("-----END REVIEW-----");
//                ++i;
//            }
           for (Review review : StanfordUtil.reviews) {
               System.out.println("-----BEGIN REVIEW-----");
               System.out.println("Extract from review " + i);
               featureExtract(review);
               System.out.println("-----END REVIEW-----");
               ++i;
           }
            // for (int j = 0; j < 14; j++){
            // 	System.out.println("-----BEGIN REVIEW-----");
            //     System.out.println("Extract from review " + i);
            //     featureExtract(StanfordUtil.reviews.get(j));
            //     System.out.println("-----END REVIEW-----");
            //     ++i;
            // }
            

            
            StanfordUtil.test();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
