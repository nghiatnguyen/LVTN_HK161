/*
x * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.util;

import coreferenceresolver.element.CRFToken;
import coreferenceresolver.process.FeatureExtractor;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Token;
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

    private static final String DISCARDED_PERSONAL_PRONOUNS = ";i;me;myself;we;us;ourselves;you;yourself;yourselves;he;him;himself;she;her;herself;anyone;someone;somebody;everyone;anybody;everybody;nobody;people;";

    private static final String DISCARDED_TIME_NOUNS = ";minute;minutes;hour;hours;day;days;week;weeks;month;months;year;years;january;february;march;april;may;june;july;august;september;october;november;december;monday;tuesday;wednesday;thursday;friday;saturday;sunday;";

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

    public static void extractFeatures(Review review, BufferedWriter bw, boolean forTraining) throws IOException {
//        System.out.println("All NPs in this review:");
//        for (NounPhrase np : review.getNounPhrases()) {
//            System.out.print(np.getNpNode().getLeaves() + "  ");
//        }
//        System.out.println();

        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++) {
            FeatureExtractor.setNPForOPInSentence(review.getSentences().get(i));
        }

        for (int i = review.getNounPhrases().size() - 1; i >= 1; i--) {
            checkNPhasOW = false;
            NounPhrase np2 = review.getNounPhrases().get(i);

            listAllPMI.clear();
            listRawPMI.clear();
            //Find PMI of NP2 with NP1
            if (np2.getOpinionWords().isEmpty()) {
                checkNPhasOW = true;
            } else {
                for (int j = 0; j < i; j++) {
                    NounPhrase np1 = review.getNounPhrases().get(j);
                    if (np1.getType() == 0 || np2.getType() == 0 || np1.getType() == 2 || np2.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
//                    if (np1.getType() == 0 || np2.getType() == 0) {
//                        if (FeatureExtractor.isPronoun(np1) || FeatureExtractor.isNotObject(np1))
//                        	listRawPMI.add((float) -1);
//                        else {
                        Float rawPMIof2NP = FeatureExtractor.PMI(np2, np1);
                        listRawPMI.add(rawPMIof2NP);
                        if (!listAllPMI.contains(rawPMIof2NP)) {
                            listAllPMI.add(rawPMIof2NP);
//	                        }
                        }
                    }
                }

                Collections.sort(listAllPMI, Collections.reverseOrder());

            }

            int k = 0;
            for (int j = 0; j < i; ++j) {
                NounPhrase np1 = review.getNounPhrases().get(j);
                if (np1.getType() == 0 || np2.getType() == 0 || np2.getType() == 2 || np1.getType() == 2 || np1.getType() == 3 || np2.getType() == 3) {
                    createTest(np1, np2, review, bw, k);
                    k++;
                }
            }
        }

        System.out.println("------------Done--------------");
    }

    public static void initMarkupFile(Review review, FileWriter fw) throws IOException {

        String markupReview = review.getRawContent();
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase curNp = review.getNounPhrases().get(i);
//            System.out.println(curNp.getNpNode().getLeaves());

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
//            System.out.println("Raw NP " + rawNp);
//            System.out.println("Consider " + markupReview.substring(openNpOffsets.get(i)));

            String regex = specialRegex(rawNp);
//            System.out.println("Regex " + regex);
            pattern = Pattern.compile(regex);
            String subString = markupReview.substring(openNpOffsets.get(i));
            matcher = pattern.matcher(subString);
            if (matcher.find()) {
//                System.out.println("Found: " + matcher.group());
                int replacedStringIndex = markupReview.substring(openNpOffsets.get(i)).indexOf(matcher.group());
                subString = markupReview.substring(openNpOffsets.get(i), openNpOffsets.get(i) + replacedStringIndex + matcher.group().length()) + ">" + markupReview.substring(openNpOffsets.get(i) + replacedStringIndex + matcher.group().length());
            }

            markupReview = markupReview.substring(0, openNpOffsets.get(i)) + subString;
//            System.out.println("Markup Review " + markupReview);
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

    public static int retrieveOpinion(Token token) {
        if (FeatureExtractor.NEGATIVE_WORDS.contains(";" + token.getWord().toLowerCase() + ";")) {
            return NEGATIVE;
        } else if (FeatureExtractor.POSITIVE_WORDS.contains(";" + token.getWord().toLowerCase() + ";")) {
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
            sentence.setOpinionForNPs();
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
        bwtrain.write(FeatureExtractor.isBetween3Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.hasBetween2Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",");
//        bwtrain.write(FeatureExtractor.sentimentConsistencyExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBothPropername(np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.hasProperName(np1, StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.hasProperName(np1, StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.isBothPronoun(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBothNormal(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isSubString(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isHeadMatch(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isExactMatch(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isMatchAfterRemoveDetermine(np1, np2) + ",");        

        if (checkNPhasOW == true) {
            bwtrain.write(10 + ",");
        } else if (np2.getType() == 0) {
            bwtrain.write(12 + ",");
        } else {
//        	if (listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) == -1)
//        		bwtrain.write(11 + ",");
            if (listRawPMI.get(IdPMIinList) == 0) {
                bwtrain.write(4 + ",");
            } else if (listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) < 4) {
//        		System.out.println("Review: " + np1.getReviewId() +"ID NP1: " + np1.getId() + "ID NP2: " + np2.getId() + " PMI: "+ listRawPMI.get(IdPMIinList));
                bwtrain.write(listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) + ",");
            } else {
                bwtrain.write(4 + ",");
//                System.out.println("Review: " + np1.getReviewId() +"ID NP1: " + np1.getId() + "ID NP2: " + np2.getId() + " PMI: "+ listRawPMI.get(IdPMIinList));
            }
        }
        bwtrain.write(FeatureExtractor.isPhoneHead(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isCorefTest(np1, np2).toString());
        bwtrain.newLine();
    }

    public static void checkPOSFilesMatchingInput(List<Review> reviews) throws Exception {
        long numOfLinesCheck = Files.lines(Paths.get(".\\input.txt.pos.chk")).count();
        int totalSentences = 0;
        for (Review review : reviews) {
            totalSentences += review.getSentences().size();
        }

        if (numOfLinesCheck != totalSentences + reviews.size()) {
            throw new Exception("The file input.txt.pos or input.txt.pos.chk in current dir is not matching the input file");
        }
    }
}
