/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author TRONGNGHIA
 */
public class Util {

    private static final String DISCARDED_PERSONAL_PRONOUNS = ";i;me;we;us;ours;you;he;him;his;she;her;hers;";

    public static void extractFeatures(Review review, BufferedWriter bwtrain) throws IOException {
        System.out.println("All NPs in this review:");
        for (NounPhrase np : review.getNounPhrases()) {
            System.out.print(np.getNpNode().getLeaves() + "  ");
        }
        System.out.println();
        

        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++) {
            FeatureExtractor.set_NP_for_OP_in_sentence(review.getSentences().get(i));
        }
        
        
    	
//		for (int i = review.getNounPhrases().size()-1; i > 0; i--){
//			NounPhrase np2 = review.getNounPhrases().get(i);
//			if ((np2.getRefId() == -1)|| (np2.getType() == 1)){
//				
//			}
//			else{
//				for (int j = i-1 ; j >= 0; j-- ){
//					NounPhrase np1 = review.getNounPhrases().get(j);
//					createTrain(np1, np2, review, bwtrain);
//					if (np1.getId() == np2.getRefId())
//						break;
//				}
//			}
//			
//		}
		
		for (int i = 0; i < review.getNounPhrases().size(); ++i) {
          NounPhrase np1 = review.getNounPhrases().get(i);
          for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
              NounPhrase np2 = review.getNounPhrases().get(j);
              if (np1.getType() == 0 || np2.getType() == 0){
              	createTrain(np1, np2, review, bwtrain);
              }
          }
		}
        
//        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
//            NounPhrase np1 = review.getNounPhrases().get(i);
//            for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
//                NounPhrase np2 = review.getNounPhrases().get(j);
//                System.out.println("-----------NP pair--------------");
//                System.out.println("In review");
//                System.out.println(review.getRawContent());
//                System.out.println("NP1 id: " + np1.getId());
//                System.out.println("NP1 ref: " + np1.getRefId());
//                System.out.println("NP1 type: " + np1.getType());
//                System.out.println("NP1 words: " + np1.getNpNode().getLeaves());
//                System.out.println("NP1 head label: " + np1.getHeadLabel());
//                System.out.println("NP1 head: " + np1.getHeadNode());
//                System.out.println("NP1 begin: " + np1.getOffsetBegin());
//                System.out.println("NP1 end: " + np1.getOffsetEnd());
//                System.out.println("NP1 review: " + np1.getReviewId());
//                System.out.println("NP1 sentence: " + np1.getSentenceId());
//                System.out.print("Opinion words: ");
//               
//                for (int k = 0; k < np1.getOpinionWords().size(); k++) {
//                    System.out.print(np1.getOpinionWords().get(k) + " ; ");
//                }
//                System.out.println();
//                System.out.println("------------");
//                System.out.println("NP2 id: " + np2.getId());
//                System.out.println("NP2 ref: " + np2.getRefId());
//                System.out.println("NP2 type: " + np2.getType());
//                System.out.println("NP2 words: " + np2.getNpNode().getLeaves());
//                System.out.println("NP2 head label: " + np2.getHeadLabel());
//                System.out.println("NP2 head: " + np2.getHeadNode());
//                System.out.println("NP2 begin: " + np2.getOffsetBegin());
//                System.out.println("NP2 end: " + np2.getOffsetEnd());
//                System.out.println("NP2 review: " + np2.getReviewId());
//                System.out.println("NP2 sentence: " + np2.getSentenceId());
//                System.out.println("------------");
//                try {
//                    System.out.println("COREF: " + FeatureExtractor.isCoref(np1, np2));
//                    System.out.println("NP1 is Pronoun: " + FeatureExtractor.is_Pronoun(np1));
//                    System.out.println("NP2 is Pronoun: " + FeatureExtractor.is_Pronoun(np2));
//                    System.out.println("NP2 is Definite Noun Phrase: " + FeatureExtractor.is_Definite_NP(np2));
//                    System.out.println("NP2 is Demonstrative Noun Phrase: " + FeatureExtractor.is_Demonstrative_NP(np2));
//                    System.out.println("String similarity: " + FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())));
//                    System.out.println("Distance Feature: " + FeatureExtractor.count_Distance(np1, np2));
//                    System.out.println("Number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
//                    System.out.println("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(review, np1, np2));
//                    System.out.println("is-between: " + FeatureExtractor.isBetweenExtract(review, np1, np2));
//                    System.out.println("has-between: " + FeatureExtractor.has_Between_Extract(review, np1, np2));
//                    System.out.println("***Entity and opinion words association*** " );
//                    System.out.println("Probability of Opinion Word of NP1: " + FeatureExtractor.probability_opinion_word(np1));
//                    System.out.println("Probability of NP2: " + FeatureExtractor.probability_noun_phrase(np2));
//                    System.out.println("Probability of (NP2 and Opinion Word of NP1): " + FeatureExtractor.probability_NP_and_OW(np1, np2));
//    
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                    System.out.println("Exception NP1 words: " + np1.getNpNode().getLeaves());
//                    System.out.println("Exception NP2 words: " + np2.getNpNode().getLeaves());
//                }
//                
//
//                System.out.println("------------End of NP pair--------------");
//            }
//        }
        
		 System.out.println("------------Done--------------");
    }
    
    public static void initMarkupFile(Review review, FileWriter fw) throws IOException {

        String markupReview = review.getRawContent();
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase curNp = review.getNounPhrases().get(i);
            System.out.println(curNp.getNpNode().getLeaves());

            int openNpOffset = curNp.getOffsetBegin() + i;
            markupReview = markupReview.substring(0, openNpOffset) + "<" + markupReview.substring(openNpOffset);
        }

        Pattern pattern = null;
        Matcher matcher = null;

        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            List<Integer> openNpOffsets = new ArrayList<>();
            for (int j = 0; j < markupReview.length(); ++j) {
                if (markupReview.charAt(j) == '<') {
                    openNpOffsets.add(j);
                }
            }

            NounPhrase curNp = review.getNounPhrases().get(i);
            String rawNp = review.getRawContent().substring(curNp.getOffsetBegin(), curNp.getOffsetEnd());
            System.out.println("Raw NP " + rawNp);
            System.out.println("Consider " + markupReview.substring(openNpOffsets.get(i)));

            String regex = specialRegex(rawNp);
            System.out.println("Regex " + regex);
            pattern = Pattern.compile(regex);            
            String subString = markupReview.substring(openNpOffsets.get(i));
            matcher = pattern.matcher(subString);
            if (matcher.find()) {
                System.out.println("Found: " + matcher.group());
                int replacedStringIndex = markupReview.substring(openNpOffsets.get(i)).indexOf(matcher.group());
                subString = markupReview.substring(openNpOffsets.get(i), openNpOffsets.get(i) + replacedStringIndex + matcher.group().length()) + ">" + markupReview.substring(openNpOffsets.get(i) + replacedStringIndex + matcher.group().length());
            }

            markupReview = markupReview.substring(0, openNpOffsets.get(i)) + subString;
            System.out.println("Markup Review " + markupReview);
        }

        int npIndex = -1;
        int i = 0;
        while (i < markupReview.length()) {
            if (markupReview.charAt(i) == '<') {
                ++npIndex;
                String coref = npIndex + "," + "0" + "," + "0" + " ";
                markupReview = markupReview.substring(0, i) + "<" + coref + markupReview.substring(i + 1);
                i += coref.length();
            } else {
                ++i;
            }
        }

        fw.write(markupReview);
        fw.write("\n");
    }

    public static void readMarkupFile(File markupFile) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(markupFile));
        String line = "";

        int reviewId = 0;
        while ((line = br.readLine()) != null) {
            readMarkup(line, reviewId);
            ++reviewId;
        }
    }

    private static void readMarkup(String markupLine, int reviewId) {
        List<NounPhrase> nounPhrases = StanfordUtil.reviews.get(reviewId).getNounPhrases();
        int charId = 0;
        int npId = 0;
        while (charId < markupLine.length()) {
            if (markupLine.charAt(charId) == '<') {
                String corefInfo = "";
                int j = 0;
                for (j = charId; j < markupLine.length(); ++j){
                    if (markupLine.charAt(j) == ' '){
                        break;
                    }
                    corefInfo += markupLine.charAt(j);
                    
                }
                String[] corefInfos = corefInfo.split(",");
                int refId = corefInfos[1].equals("/") ? -1 : Integer.valueOf(corefInfos[1]);
                int type = Integer.valueOf(corefInfos[2]);
                nounPhrases.get(npId).setRefId(refId);
                nounPhrases.get(npId).setType(type);
                ++npId;
                charId = j;
            } else {
                ++charId;
            }
        }
    }

    public static void discardPersonalProNPs(Review review) {
        List nps = review.getNounPhrases();
        Iterator<NounPhrase> itr = nps.iterator();

        while (itr.hasNext()) {
            NounPhrase np = itr.next();
            if (isDiscardedPersonalPronounNP(np)) {
                itr.remove();
            }
           
        }
        
        for (int i = 0; i< review.getNounPhrases().size(); i++)
        	review.getNounPhrases().get(i).setId(i);
    }

    private static boolean isDiscardedPersonalPronounNP(NounPhrase np) {
        if (DISCARDED_PERSONAL_PRONOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    private static String specialRegex(String sequence) {
        return sequence.replaceAll("\\$", "[\\$]")
                .replaceAll("[?]", "[\\?]")
                .replaceAll("[*]", "[\\*]")
                .replaceAll("[+]", "[+]")
                .replaceAll("[\\(]", "[(]<*")
                .replaceAll("[\\)]", "[)]")                
                .replaceAll("\\s", " <*");        
    }
    
    private static void createTrain(NounPhrase np1, NounPhrase np2, Review review, BufferedWriter bwtrain) throws IOException{
//      System.out.println("COREF: " + FeatureExtractor.isCoref(np1, np2));
//      System.out.println("NP1 is Pronoun: " + FeatureExtractor.is_Pronoun(np1));
//      System.out.println("NP2 is Pronoun: " + FeatureExtractor.is_Pronoun(np2));
//      System.out.println("NP2 is Definite Noun Phrase: " + FeatureExtractor.is_Definite_NP(np2));
//      System.out.println("NP2 is Demonstrative Noun Phrase: " + FeatureExtractor.is_Demonstrative_NP(np2));
//      System.out.println("String similarity: " + FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())));
//      System.out.println("Distance Feature: " + FeatureExtractor.count_Distance(np1, np2));
//      System.out.println("Number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
//      System.out.println("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(review, np1, np2));
//      System.out.println("is-between: " + FeatureExtractor.isBetweenExtract(review, np1, np2));
//      System.out.println("has-between: " + FeatureExtractor.has_Between_Extract(review, np1, np2));
//      System.out.println("***Entity and opinion words association*** " + FeatureExtractor.PMI(np1, np2));
//      System.out.println("Probability of Opinion Word of NP1: " + FeatureExtractor.probability_opinion_word(np1));
//      System.out.println("Probability of NP2: " + FeatureExtractor.probability_noun_phrase(np2));
//      System.out.println("Probability of (NP2 and Opinion Word of NP1): " + FeatureExtractor.probability_NP_and_OW(np1, np2));
    	bwtrain.write(np1.getReviewId() + ",");
    	bwtrain.write(np1.getId() + ",");
    	bwtrain.write(np2.getId() + ",");
     bwtrain.write(FeatureExtractor.is_Pronoun(np1).toString() + ",");
     bwtrain.write(FeatureExtractor.is_Pronoun(np2).toString() + ",");
     bwtrain.write(FeatureExtractor.is_Definite_NP(np2).toString() + ",");
     bwtrain.write(FeatureExtractor.is_Demonstrative_NP(np2).toString() + ",");
     bwtrain.write(FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())).toString() + ",");
     bwtrain.write(FeatureExtractor.count_Distance(np1, np2) + ",");
     bwtrain.write(FeatureExtractor.numberAgreementExtract(np1, np2) + ",");
     bwtrain.write(FeatureExtractor.isBetweenExtract(review, np1, np2).toString() + ",");
     bwtrain.write(FeatureExtractor.has_Between_Extract(review, np1, np2).toString() + ",");
     bwtrain.write(FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",");
     bwtrain.write(FeatureExtractor.PMI(np1, np2).toString() + ",");
//     bwtrain.write(FeatureExtractor.probability_NP_and_OW(np1, np2) + ",");
//     bwtrain.write(FeatureExtractor.probability_opinion_word(np1) + ",");
//     bwtrain.write(FeatureExtractor.probability_noun_phrase(np2) + ",");
     bwtrain.write(FeatureExtractor.isCoref(np1, np2).toString());
     bwtrain.newLine();
    }
    
    
}
