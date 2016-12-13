/*
x * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.util;

import coreferenceresolver.element.CRFToken;
import coreferenceresolver.element.CorefChain;
import coreferenceresolver.process.FeatureExtractor;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.element.Sentence;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author TRONGNGHIA
 */
public class Util {

    public static int POSITIVE = 1;
    public static int NEGATIVE = -1;
    public static int NEUTRAL = 0;
    private static final ArrayList<String> TO_BES = new ArrayList<String>(
            Arrays.asList("is", "'s", "are", "'re", "was", "were", "been", "be"));

    private static final String DISCARDED_PERSONAL_PRONOUNS = ";i;me;myself;we;us;ourselves;you;yourself;yourselves;he;him;himself;she;her;herself;anyone;someone;somebody;everyone;anybody;everybody;nobody;people;";

    private static final String DISCARDED_TIME_NOUNS = ";minute;minutes;hour;hours;day;days;week;weeks;month;months;year;years;january;february;march;april;may;june;july;august;september;october;november;december;monday;tuesday;wednesday;thursday;friday;saturday;sunday;today;yesterday;tomorrow;";

    private static final String DISCARDED_STOP_WORDS = ";there;etc;oh;";

    //private static final String DISCARDED_NUMBER_NOUN_POS = "CD"; //one, two, three
    private static final String DISCARDED_QUANTITY_NOUNS = ";lot;lots;number;total;amount;little;much;many;ton;tons;plenty;some;bit;a;";

    private static final String DISCARDED_TIME_REGEX = "([0-9]+:[0-9]+)|([0-9]+[ ]*(AM|PM)) | (AM|PM)";

    private static final String DEP_RELATIONS = ";nn;acomp;advmod;amod;det;dobj;infmod;iobj;measure;nsubj;nsubjpass;partmod;prep;rcmod;xcomp;xsubj;";

    private static Boolean checkNPhasOW = false;
    //Each PMI appears 1 times.
    private static ArrayList<Float> listAllPMI = new ArrayList<Float>();
    //List PMI of each NP2 with NP1
    private static ArrayList<Float> listRawPMI = new ArrayList<Float>();
    private static ArrayList<Integer> listAllAfter = new ArrayList<Integer>();
    private static ArrayList<Integer> listRawAfter = new ArrayList<Integer>();
    private static ArrayList<Integer> listAllBefore = new ArrayList<Integer>();
    private static ArrayList<Integer> listRawBefore = new ArrayList<Integer>();
    
    private static String sDataset = null;    

    public static String getDataset() {
        return sDataset;
    }

    public static void setDataset(String s) {
        sDataset = s;
    }
    
//    public static void setVerbforNP(List<Review> listReview){
//    	for (Review re : listReview){
//    		for (Sentence se : re.getSentences()){
//    			for (NounPhrase np : se.getNounPhrases()){
//    				for (int j = se.getTokens().size() - 1; j >= 0; j--)
//    					if (se.getTokens().get(j).getOffsetEnd() < np.getOffsetBegin() && j != 0){
//    						String POS = se.getTokens().get(j).getPOS();
//    						if (POS.contains("VB") && !TO_BES.contains(se.getTokens().get(j).getWord().toLowerCase()))
//    							np.setVerbBefore(se.getTokens().get(j).getWord().toLowerCase());
//    						break;
//    					}
//    				for (int j = 0; j < se.getTokens().size(); j++)
//    					if (se.getTokens().get(j).getOffsetBegin() < np.getOffsetEnd() && j != se.getTokens().size()-1){
//    						String POS = se.getTokens().get(j).getPOS();
//    						if (POS.contains("VB") && !TO_BES.contains(se.getTokens().get(j).getWord().toLowerCase()))
//    							np.setVerbAfter(se.getTokens().get(j).getWord().toLowerCase());
//    						break;
//    					}	
//    			}
//    				
//    		}
//    	}
//    }

    public static void extractFeatures(Review review, BufferedWriter bw, boolean forTraining) throws IOException {
        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++) {
            FeatureExtractor.setNPForOPInSentence(review.getSentences().get(i));
        }

        for (int i = review.getNounPhrases().size() - 1; i >= 1; i--) {
            checkNPhasOW = true;
            NounPhrase np2 = review.getNounPhrases().get(i);

            listAllPMI.clear();
            listRawPMI.clear();
//            listAllAfter.clear();
//            listAllBefore.clear();
//            listRawAfter.clear();
//            listRawBefore.clear();
            
            //Find PMI of NP2 with NP1
            if (np2.getOpinionWords().isEmpty()) {
                checkNPhasOW = false;
            } else {
                for (int j = i - 1; j >= 0; j--) {
                    NounPhrase np1 = review.getNounPhrases().get(j);
                    if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
                    	Float rawPMIof2NP = FeatureExtractor.PMI(np2, np1);
                        listRawPMI.add(rawPMIof2NP);
                        if (!listAllPMI.contains(rawPMIof2NP)) {
                            listAllPMI.add(rawPMIof2NP);
	                        }
                    }
                }

                Collections.sort(listAllPMI, Collections.reverseOrder());
            }
            
//            if (np2.getVerbAfter() == ""){}
//            else {
//            	for (int j = i - 1; j >= 0; j--) {
//                    NounPhrase np1 = review.getNounPhrases().get(j);
//                    if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                        int rawAfter = FeatureExtractor.countNPWithVerbAfter(np2, np1);
//                        listRawAfter.add(rawAfter);
//                        if (!listAllAfter.contains(rawAfter))
//                        	listAllAfter.add(rawAfter);
//            	}      
//                    }
//            	Collections.sort(listAllAfter, Collections.reverseOrder());     
//            }
//            
//            if (np2.getVerbBefore() == ""){}
//            else {
//            	for (int j = i - 1; j >= 0; j--) {
//                    NounPhrase np1 = review.getNounPhrases().get(j);
//                    if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                    int rawBefore = FeatureExtractor.countNPWithVerbBefore(np2, np1);
//                    listRawBefore.add(rawBefore);
//                    if (!listAllBefore.contains(rawBefore))
//                    	listAllBefore.add(rawBefore);
//                    
//            	}      
//                    }
//            	Collections.sort(listAllBefore, Collections.reverseOrder());     
//            }
            	

            //Create all pair of 2 NPs
            int k = 0;
            for (int j = i - 1; j >= 0; j--) {
                NounPhrase np1 = review.getNounPhrases().get(j);
                if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
                    createTest(np1, np2, review, bw, k);
                    k++;
                }
            }
            
            //Each True pair is created from 2 closest NPs which coreference each other. 
//            int k = 0;
//            for (int j = i - 1; j >= 0; j--) {
//                NounPhrase np1 = review.getNounPhrases().get(j);
//                if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                    createTest(np1, np2, review, bw, k);
//                    k++;
//                    if (FeatureExtractor.isCorefTest(np1, np2))
//                    	break;
//                }
//            }
//            
            //Create if NP1 or NP2 has TYPE is 0,3
//            int k = 0;
//            for (int j = i - 1; j >= 0; j--) {
//                NounPhrase np1 = review.getNounPhrases().get(j);
//                if (np1.getType() == 0 || np2.getType() == 0 || np1.getType() == 3 || np2.getType() == 3) {
//                    createTest(np1, np2, review, bw, k);
//                    k++;
////                    if (FeatureExtractor.isCorefTest(np1, np2))
////                    	break;
//                }
//            }
            
            //Each True pair is created from 2 closest NPs which coreference each other. 
            //If NP2 is not pronoun, than closest NP1 which coreferences with NP2 is not pronoun.
//          int k = 0;
//          if (FeatureExtractor.isPronoun(np2)){
//        	  for (int j = i - 1; j >= 0; j--) {
//                  NounPhrase np1 = review.getNounPhrases().get(j);
//                  if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                	  if (FeatureExtractor.isCorefTest(np1, np2) && FeatureExtractor.isPronoun(np1)){}
//                	  else{ 
//	                	  createTest(np1, np2, review, bw, k);
//	                      k++;
//                	  }
//                	  if (FeatureExtractor.isCorefTest(np1, np2) && !FeatureExtractor.isPronoun(np1))
//                		  break;
//                  }
//              }
//          }
//          else{
//        	  for (int j = i - 1; j >= 0; j--) {
//                  NounPhrase np1 = review.getNounPhrases().get(j);
//                  if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                      createTest(np1, np2, review, bw, k);
//                      k++;
//                      if (FeatureExtractor.isCorefTest(np1, np2))
//                      	break;
//                  }
//              }
//          }
        }        
    }

    public static void initMarkupFile(Review review, FileWriter fw) throws IOException {

        String markupReview = review.getRawContent();
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase curNp = review.getNounPhrases().get(i);

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

            String regex = specialRegex(rawNp);
            pattern = Pattern.compile(regex);
            String subString = markupReview.substring(openNpOffsets.get(i));
            matcher = pattern.matcher(subString);
            if (matcher.find()) {
                int replacedStringIndex = markupReview.substring(openNpOffsets.get(i)).indexOf(matcher.group());
                subString = markupReview.substring(openNpOffsets.get(i), openNpOffsets.get(i) + replacedStringIndex + matcher.group().length()) + ">" + markupReview.substring(openNpOffsets.get(i) + replacedStringIndex + matcher.group().length());
            }

            markupReview = markupReview.substring(0, openNpOffsets.get(i)) + subString;
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

    public static void readMarkupFile(List<Review> reviews, File markupFile) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(markupFile));
        String line = "";

        int reviewId = 0;
        while ((line = br.readLine()) != null) {
            readMarkup(reviews, line, reviewId);
            ++reviewId;
        }
    }

    private static void readMarkup(List<Review> reviews, String markupLine, int reviewId) {
        List<NounPhrase> nounPhrases = reviews.get(reviewId).getNounPhrases();
        int charId = 0;
        int npId = 0;        
        
        while (charId < markupLine.length()) {
            if (markupLine.charAt(charId) == '<') {
                String corefInfo = "";
                int j = 0;
                for (j = charId; j < markupLine.length(); ++j) {
                    if (markupLine.charAt(j) == ' ') {
                        break;
                    }
                    corefInfo += markupLine.charAt(j);

                }
                String[] corefInfos = corefInfo.split(",");
                int refId = Integer.valueOf(corefInfos[1]);
                int type = Integer.valueOf(corefInfos[2]);    
//                System.out.println("NP " + nounPhrases.get(npId).getReviewId() + " " + nounPhrases.get(npId).getId() + " type " + type);
                nounPhrases.get(npId).setRefId(refId);
                nounPhrases.get(npId).setType(type);
                ++npId;
                charId = j;
            } else {
                ++charId;
            }
        }
    }

    public static void discardUnneccessaryNPs(Review review) {
        List nps = review.getNounPhrases();
        Iterator<NounPhrase> itr = nps.iterator();

        while (itr.hasNext()) {
            NounPhrase np = itr.next();
            if (isDiscardedPersonalPronounNP(np) || isDiscardedTimeNP(np) || isDiscardedCurrencyNP(np)
                    || isDiscardedStopWordNP(np) || isDiscardedQuantityNP(np) || isDiscardedPercentageNP(np)
                    || isDiscardedWrongCase(np)) {
                itr.remove();
                
                Iterator<NounPhrase> sentNPsItr = review.getSentences().get(np.getSentenceId()).getNounPhrases().iterator();
                while (sentNPsItr.hasNext()) {
                    NounPhrase npSent = sentNPsItr.next();
                    if (npSent.getId() == np.getId()){
                        sentNPsItr.remove();
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < review.getNounPhrases().size(); i++) {
            review.getNounPhrases().get(i).setId(i);
        }
    }

    private static boolean isDiscardedPersonalPronounNP(NounPhrase np) {
        if (np.getHeadNode() != null && DISCARDED_PERSONAL_PRONOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    private static boolean isDiscardedPercentageNP(NounPhrase np) {
        if (np.getHeadNode() != null && np.getHeadNode().value().contains("%")) {
            return true;
        }
        return false;
    }

    private static boolean isDiscardedTimeNP(NounPhrase np) {
        //Discard NP with HOUR LITERAL
        if (np.getHeadNode() != null && (np.getHeadNode().value().matches(DISCARDED_TIME_REGEX) || np.getHeadNode().value().toLowerCase().equals("am")
                || np.getHeadNode().value().toLowerCase().equals("pm"))) {
            return true;
        }

        //Discard NP with TIME LITERAL
        if (np.getHeadNode() != null && DISCARDED_TIME_NOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        //Discard NP with HEAD "time": the first time, the second time, ...        
        if (np.getHeadNode() != null && (np.getHeadNode().value().toLowerCase().equals("time") || np.getHeadNode().value().toLowerCase().equals("times"))) {
            for (int i = 0; i < np.getNpNode().getLeaves().size(); ++i) {
                if (np.getNpNode().getLeaves().get(i).value().equals("time") || np.getNpNode().getLeaves().get(i).value().equals("times")) {
                    //NP starts with "time"
                    if (i == 0) {
                        return true;
                    }
                    String tokenBeforeHeadPOS = ((CoreLabel) np.getNpNode().getLeaves().get(i - 1).label()).get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if (!tokenBeforeHeadPOS.equals("NN") && !tokenBeforeHeadPOS.equals("NNS")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //Discard all stop words: there, etc, oh, ...
    private static boolean isDiscardedStopWordNP(NounPhrase np) {
        if (np.getHeadNode() != null && DISCARDED_STOP_WORDS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    //Discard all NP indicating quantity: lot, lots, number, total
    public static boolean isDiscardedQuantityNP(NounPhrase np) {
        if (np.getHeadNode() != null && DISCARDED_QUANTITY_NOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    //Discard NP type " 's "
    public static boolean isDiscardedWrongCase(NounPhrase np) {
        if (np.getHeadNode() != null
                && (np.getHeadLabel().equals("POS")
                || np.getHeadLabel().equals("RB"))) {
            return true;
        }
        return false;
    }

    private static boolean isDiscardedCurrencyNP(NounPhrase np) {
        if (np.getHeadNode() != null && (np.getHeadNode().value().contains("$") || np.getHeadNode().value().contains("dollar"))) {
            return true;
        }
        return false;
    }

    public static int retrieveOriginalSentiment(String word) {
        if (FeatureExtractor.NEGATIVE_WORDS.contains(";" + word.toLowerCase() + ";")) {
            return NEGATIVE;
        } else if (FeatureExtractor.POSITIVE_WORDS.contains(";" + word.toLowerCase() + ";")) {
            return POSITIVE;
        } else {
            return NEUTRAL;
        }
    }

    public static int reverseSentiment(int sentiment) {
        return sentiment == POSITIVE ? NEGATIVE : sentiment == NEGATIVE ? POSITIVE : 0;
    }

    public static void assignNounPhrases(List<NounPhrase> nounPhrases, List<Review> reviews) {
        CollinsHeadFinder headFinder = new CollinsHeadFinder();
        for (NounPhrase np : nounPhrases) {                          
            Review review = reviews.get(np.getReviewId());
            Sentence sentence = review.getSentences().get(np.getSentenceId());
            String npContent = "";
            for (CRFToken token : np.getCRFTokens()) {
                npContent += token.getWord() + " ";
            }

            //Initiate a NP Tree
            Tree npNode = initNPTree();
            for (CRFToken cRFToken : np.getCRFTokens()) {                
                Tree cRFTokenTree = sentence.getTokens().get(cRFToken.getIdInSentence()).getTokenTree();
                npNode.addChild(cRFTokenTree);
            }
            np.setNpNode(npNode);
            np.setHeadNode(npNode.headTerminal(headFinder));

            int npOffsetBegin = sentence.getTokens().get(np.getCRFTokens().get(0).getIdInSentence()).getOffsetBegin();
            np.setOffsetBegin(npOffsetBegin);
            int npOffsetEnd = sentence.getTokens().get(np.getCRFTokens().get(np.getCRFTokens().size() - 1).getIdInSentence()).getOffsetEnd();
            np.setOffsetEnd(npOffsetEnd);

            review.addNounPhrase(np);
            sentence.addNounPhrase(np);
            sentence.setSentimentForNPs();
        }
    }
    
    public static void initSentimentAndComparativesForNPs(){
        if (StanfordUtil.reviews == null){
            System.out.println("StanfordUtil reviews has not been initialized");
            return;
        }
        for (Review review: StanfordUtil.reviews){
            for (Sentence sentence: review.getSentences()){
                //Set sentiment corresponding to each NP in the sentence
                sentence.setSentimentForNPs(); 
                sentence.initComparativeNPs();
            }
        }
    }
    
    public static void readDataset() throws FileNotFoundException, IOException{        
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
        setDataset(sData);
    }      
    
    public static void discardUnneccessaryChains(List<Review> reviews){
        for (Review re : reviews) {            
            Iterator<CorefChain> itr = re.getCorefChainsPredicted().iterator();
            while (itr.hasNext()) {
                CorefChain curCc = itr.next();
                boolean isSatisfied = false;
                for (int npId : curCc.getChain()) {
                    int curNpType = re.getNounPhrases().get(npId).getType();
                    if (curNpType == 0 || curNpType == 3) {
                        isSatisfied = true;
                        break;
                    }
                }                
                if (!isSatisfied) {
                    itr.remove();
                }
            }
        }
    }

    private static Tree initNPTree() {
        Annotation document = new Annotation("Dog");
        StanfordUtil.pipeline.annotate(document);
        Tree node = null;
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            node = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).children()[0];
        }
        node.removeChild(0);
        return node;
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

    private static void createTest(NounPhrase np1, NounPhrase np2, Review review, BufferedWriter bwtrain, Integer IdPMIinList) throws IOException {
        bwtrain.write(np1.getReviewId() + ",");
        bwtrain.write(np1.getId() + ",");
        bwtrain.write(np2.getId() + ",");
        bwtrain.write(FeatureExtractor.isPronoun(np1).toString() + ",");
        bwtrain.write(FeatureExtractor.isPronoun(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isDefiniteNP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isDemonstrativeNP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.countDistance(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.numberAgreementExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBetween2Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.hasBetween2Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.sentimentConsistencyExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBothPropername(np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.hasProperName(np1, StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.hasProperName(np2, StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.isBothPronoun(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBothNormal(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isSubString(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isHeadMatch(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isExactMatch(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isMatchAfterRemoveDeterminer(np1, np2) + ",");        

<<<<<<< HEAD
        if (checkNPhasOW == false) {
=======
         if (checkNPhasOW == false) {
>>>>>>> 1cc3a6b81b7751766f73fc286f7c78902801ff22
            bwtrain.write(10 + ",");
        } 
        else {
        	if (listRawPMI.get(IdPMIinList) == 0) {
                bwtrain.write(4 + ",");
            }
        	else if (listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) < 4) {
                bwtrain.write(listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) + ",");
            } else {
                bwtrain.write(4 + ",");
            }
        }
         
//        if (listRawAfter.isEmpty())
//        	bwtrain.write(10 + ",");
//        else {
//        	if (listRawAfter.get(IdPMIinList) == 0) {
//                bwtrain.write(4 + ",");
//            }
//        	else if (listAllAfter.indexOf(listRawAfter.get(IdPMIinList)) < 4) {
//                bwtrain.write(listAllAfter.indexOf(listRawAfter.get(IdPMIinList)) + ",");
//            } else {
//                bwtrain.write(4 + ",");
//            }
//        }
//        
//        if (listRawBefore.isEmpty())
//        	bwtrain.write(10 + ",");
//        else {
//        	if (listRawBefore.get(IdPMIinList) == 0) {
//                bwtrain.write(4 + ",");
//            }
//        	else if (listAllBefore.indexOf(listRawBefore.get(IdPMIinList)) < 4) {
//                bwtrain.write(listAllBefore.indexOf(listRawBefore.get(IdPMIinList)) + ",");
//            } else {
//                bwtrain.write(4 + ",");
//            }
//        }
        bwtrain.write(FeatureExtractor.isRelativePronounNPs(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isCorefTest(np1, np2).toString());
        bwtrain.newLine();
    }   
    
    
    /************************************************/
    /***********Cross validation**********************/
    public static void setInstancesForReviews(Review review) throws IOException {
        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++) {
            FeatureExtractor.setNPForOPInSentence(review.getSentences().get(i));
        }

        for (int i = review.getNounPhrases().size() - 1; i >= 1; i--) {
            checkNPhasOW = true;
            NounPhrase np2 = review.getNounPhrases().get(i);

            listAllPMI.clear();
            listRawPMI.clear();
            //Find PMI of NP2 with NP1
            if (np2.getOpinionWords().isEmpty()) {
                checkNPhasOW = false;
            } else {
                for (int j = i - 1; j >= 0; j--) {
                    NounPhrase np1 = review.getNounPhrases().get(j);
                    	Float rawPMIof2NP = FeatureExtractor.PMI(np2, np1);
                        listRawPMI.add(rawPMIof2NP);
                        if (!listAllPMI.contains(rawPMIof2NP)) {
                            listAllPMI.add(rawPMIof2NP);
	                        }
                }

                Collections.sort(listAllPMI, Collections.reverseOrder());

            }

            //Create all pair of 2 NPs
            int k = 0;
            for (int j = i - 1; j >= 0; j--) {
                NounPhrase np1 = review.getNounPhrases().get(j);
                    createInstance(np1, np2, review, k);
                    k++;
                if (np1.getType() == 0 || np2.getType() == 0 || np1.getType() == 3 || np2.getType() == 3 || np1.getType() == 2 || np2.getType() == 2)
                	review.addSupportInstance(true);
                else
                	review.addSupportInstance(false);
            }
        }        
    }
    
    private static void createInstance(NounPhrase np1, NounPhrase np2, Review review, Integer IdPMIinList) throws IOException {
        String instance = "";
        instance += np1.getReviewId() + ",";
        instance += np1.getId() + ",";
        instance += np2.getId() + ",";
        instance += FeatureExtractor.isPronoun(np1).toString() + ",";
        instance += FeatureExtractor.isPronoun(np2).toString() + ",";
        instance += FeatureExtractor.isDefiniteNP(np2).toString() + ",";
        instance += FeatureExtractor.isDemonstrativeNP(np2).toString() + ",";
        instance += FeatureExtractor.countDistance(np1, np2) + ",";
        instance += FeatureExtractor.numberAgreementExtract(np1, np2) + ",";
        instance += FeatureExtractor.isBetween2Extract(review, np1, np2).toString() + ",";
        instance += FeatureExtractor.hasBetween2Extract(review, np1, np2).toString() + ",";
        instance += FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",";
        instance += FeatureExtractor.sentimentConsistencyExtract(np1, np2) + ",";
        instance += FeatureExtractor.isBothPropername(np1, np2).toString() + ",";
        instance += FeatureExtractor.hasProperName(np1, StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId())).toString() + ",";
        instance += FeatureExtractor.hasProperName(np2, StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId())).toString() + ",";
        instance += FeatureExtractor.isBothPronoun(np1, np2) + ",";
        instance += FeatureExtractor.isBothNormal(np1, np2) + ",";
        instance += FeatureExtractor.isSubString(np1, np2) + ",";
        instance += FeatureExtractor.isHeadMatch(np1, np2) + ",";
        instance += FeatureExtractor.isExactMatch(np1, np2) + ",";
        instance += FeatureExtractor.isMatchAfterRemoveDeterminer(np1, np2) + ",";        

<<<<<<< HEAD
       if (checkNPhasOW == false) {
=======
        if (checkNPhasOW == false) {
>>>>>>> 1cc3a6b81b7751766f73fc286f7c78902801ff22
        	instance += 10 + ",";
        } 
        else {
        	if (listRawPMI.get(IdPMIinList) == 0) {
        		instance += 4 + ",";
            }
        	else if (listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) < 4) {
//        		System.out.println("Review: " + np1.getReviewId() +"ID NP1: " + np1.getId() + "ID NP2: " + np2.getId() + " PMI: "+ listRawPMI.get(IdPMIinList));
        		instance += listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) + ",";
            } else {
            	instance += 4 + ",";
//                System.out.println("Review: " + np1.getReviewId() +"ID NP1: " + np1.getId() + "ID NP2: " + np2.getId() + " PMI: "+ listRawPMI.get(IdPMIinList));
            }
        }
        instance += FeatureExtractor.isRelativePronounNPs(np1, np2) + ",";
        instance += FeatureExtractor.isCorefTest(np1, np2).toString();
        review.addInstance(instance);
    }   
}
