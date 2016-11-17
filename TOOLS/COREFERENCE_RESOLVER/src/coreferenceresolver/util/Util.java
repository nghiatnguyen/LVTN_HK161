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
import static coreferenceresolver.util.StanfordUtil.pipeline;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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
    private static final String DISCARDED_QUANTITY_NOUNS = ";lot;lots;number;total;amount;little;much;many;ton;tons;plenty;some;";

    private static final String DISCARDED_TIME_REGEX = "([0-9]+:[0-9]+)|([0-9]+[ ]*(AM|PM)) | (AM|PM)";

    private static final String DEP_RELATIONS = ";nn;acomp;advmod;amod;det;dobj;infmod;iobj;measure;nsubj;nsubjpass;partmod;prep;rcmod;xcomp;xsubj;";

    private static ArrayList<Integer> list;

    private static Boolean checkNPhasOW = false;
    //Each PMI appears 1 times.
    private static ArrayList<Float> listAllPMI = new ArrayList<Float>();
    //List PMI of each NP2 with NP1
    private static ArrayList<Float> listRawPMI = new ArrayList<Float>();

    public static void extractFeatures(Review review, BufferedWriter bw, boolean forTraining) throws IOException {
        System.out.println("All NPs in this review:");
        for (NounPhrase np : review.getNounPhrases()) {
            System.out.print(np.getNpNode().getLeaves() + "  ");
        }
        System.out.println();

        //Set Opinion Words for Noun Phrases
        for (int i = 0; i < review.getSentences().size(); i++) {
            FeatureExtractor.setNPForOPInSentence(review.getSentences().get(i));
        }

        //Create the test dataset
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            checkNPhasOW = false;
            NounPhrase np1 = review.getNounPhrases().get(i);
            list = new ArrayList<Integer>();

            listAllPMI.clear();
            listRawPMI.clear();
            //Find PMI of NP2 with NP1
            if (np1.getOpinionWords().isEmpty()) {
                checkNPhasOW = true;
            } else {
                for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
                    NounPhrase np2 = review.getNounPhrases().get(j);
                    if (np1.getType() == 0 || np2.getType() == 0) {
                        Float rawPMIof2NP = FeatureExtractor.PMI(np1, np2);
                        listRawPMI.add(rawPMIof2NP);
                        if (!listAllPMI.contains(rawPMIof2NP)) {
                            listAllPMI.add(rawPMIof2NP);
                        }
                    }
                }

                Collections.sort(listAllPMI, Collections.reverseOrder());

            }

            int k = 0;
            for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
                NounPhrase np2 = review.getNounPhrases().get(j);
                if (np1.getType() == 0 || np2.getType() == 0) {
                    if ((np1.getId() == np2.getRefId() && np2.getType() != 1) || list.contains(np2.getRefId())) {
                        list.add(np2.getId());
                    }
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

    public static void readMarkupFile(File markupFile) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(markupFile));
        String line = "";

        int reviewId = 0;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
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
                for (j = charId; j < markupLine.length(); ++j) {
                    if (markupLine.charAt(j) == ' ') {
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

    public static void discardUnneccessaryNPs(Review review) {
        List nps = review.getNounPhrases();
        Iterator<NounPhrase> itr = nps.iterator();

        while (itr.hasNext()) {
            NounPhrase np = itr.next();
            if (isDiscardedPersonalPronounNP(np) || isDiscardedTimeNP(np) || isDiscardedCurrencyNP(np)
                    || isDiscardedStopWordNP(np) || isDiscardedQuantityNP(np) || isDiscardedPercentageNP(np)) {
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
    private static boolean isDiscardedQuantityNP(NounPhrase np) {
        if (np.getHeadNode() != null && DISCARDED_QUANTITY_NOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
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
        if (FeatureExtractor.NEGATIVE_WORDS.contains(";" + token.getWord() + ";")) {
            return NEGATIVE;
        } else if (FeatureExtractor.POSITIVE_WORDS.contains(";" + token.getWord() + ";")) {
            return POSITIVE;
        } else {
            return NEUTRAL;
        }
    }

    public static int reverseSentiment(int sentiment) {
        return sentiment == POSITIVE ? NEGATIVE : sentiment == NEGATIVE ? POSITIVE : 0;
    }

    public static Tree[] findPhraseHead(String phraseContent, CollinsHeadFinder headFinder, StanfordCoreNLP pipeline) {
        Tree[] res = new Tree[2];
        Tree npNodeTree = null;
        Tree sentenceTree = null;
        Annotation document = pipeline.process(phraseContent);
        for (CoreMap sentence : document
                .get(CoreAnnotations.SentencesAnnotation.class)) {
            sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            npNodeTree = findNPNode(sentenceTree);
        }
        res[0] = sentenceTree;
        res[1] = npNodeTree == null ? null : npNodeTree.headTerminal(headFinder);
        return res;
    }

    private static Tree findNPNode(Tree rootTree) {
        Tree res = null;
        if (!rootTree.isLeaf() && rootTree.value().equals("NP")) {
            return rootTree;
        }
        for (Tree child : rootTree.children()) {
            res = findNPNode(child);
        }
        return res;
    }

    public static void assignNounPhrases(List<NounPhrase> nounPhrases, List<Review> reviews) {
        CollinsHeadFinder headFinder = new CollinsHeadFinder();
        for (NounPhrase np : nounPhrases) {
            System.out.println("NP");
            Review review = reviews.get(np.getReviewId());
            Sentence sentence = review.getSentences().get(np.getSentenceId());
            String npContent = "";
            for (CRFToken token : np.getCRFTokens()) {
                npContent += token.getWord() + " ";
            }

            npContent = npContent.substring(0, npContent.length() - 1);

            //Initiate a NP Tree
            Tree npNode = initNPTree();
            for (CRFToken cRFToken : np.getCRFTokens()) {
                System.out.print("CRFTokenID: " + cRFToken.getIdInSentence() + "\t");
                System.out.println();
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
        bwtrain.write(FeatureExtractor.isBothPropername(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.countDistance(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.numberAgreementExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBetween3Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.hasBetweenExtract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.sentimentConsistencyExtract(np1, np2) + ",");
        if (checkNPhasOW == true) {
            bwtrain.write(10 + ",");
        } else {
            if (listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) < 4) {
                bwtrain.write(listAllPMI.indexOf(listRawPMI.get(IdPMIinList)) + ",");
            } else {
                bwtrain.write(4 + ",");
            }
        }
        bwtrain.write(FeatureExtractor.isNested(np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isCorefTest(np1, np2, list).toString());
        bwtrain.newLine();
    }
}
