/*
x * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import edu.stanford.nlp.trees.Tree;
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

    private static final String DISCARDED_PERSONAL_PRONOUNS = ";i;me;myself;we;us;ourselves;you;yourself;yourselves;he;him;himself;she;her;herself;anyone;someone;somebody;everyone;anybody;everybody;nobody;people;";

    private static final String DISCARDED_TIME_NOUNS = ";minute;minutes;hour;hours;day;days;week;weeks;month;months;year;years;january;february;march;april;may;june;july;august;september;october;november;december;monday;tuesday;wednesday;thursday;friday;saturday;sunday;";

    private static final String DISCARDED_EX_NOUN_POS = "EX"; //There

    //private static final String DISCARDED_NUMBER_NOUN_POS = "CD"; //one, two, three
    private static final String DISCARDED_QUANTITY_NOUNS = ";lot;lots;number;total;";

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
            FeatureExtractor.set_NP_for_OP_in_sentence(review.getSentences().get(i));
        }

        //Create the train dataset
        if (forTraining) {
            for (int i = review.getNounPhrases().size() - 1; i > 0; i--) {
                NounPhrase np2 = review.getNounPhrases().get(i);
                if ((np2.getRefId() == -1) || (np2.getType() == 1)) {

                } else {
                    for (int j = i - 1; j >= 0; j--) {
                        NounPhrase np1 = review.getNounPhrases().get(j);
                        createTrain(np1, np2, review, bw);
                        if (np1.getId() == np2.getRefId()) {
                            break;
                        }
                    }
                }

            }
        } else {
//            Create the test database
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
        }
//       
//        Check features of each NP
//        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
//            NounPhrase np1 = review.getNounPhrases().get(i);
//            list = new ArrayList<Integer>();
//            for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
//                NounPhrase np2 = review.getNounPhrases().get(j);
//                if ((np1.getId() == np2.getRefId() && np2.getType() != 1) || list.contains(np2.getRefId())) {
//                    list.add(np2.getId());
//                }
//            }
//            createCheck(np1, review, bw);
//            for (Integer in : list) {
//                bw.write(in + ",");
//            }
//            bw.newLine();
//
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
            if (isDiscardedConjNP(np)) {
                itr.remove();
            } else if (isDiscardedPersonalPronounNP(np) || isDiscardedTimeNP(np) || isDiscardedCurrencyNP(np)
                    || isDiscardedNPByPOS(np) || isDiscardedQuantityNP(np)) {
                itr.remove();
            } //Consider all the NPs that have the same HEAD
            else {
                List<NounPhrase> curSentenceNPs = review.getNounPhrases();
                for (int i = 0; i < curSentenceNPs.size(); ++i) {
                    if (curSentenceNPs.get(i).getHeadNode().value().equals(np.getHeadNode().value())
                            && ((curSentenceNPs.get(i).getOffsetBegin() < np.getOffsetBegin() && curSentenceNPs.get(i).getOffsetEnd() >= np.getOffsetEnd())
                            || (curSentenceNPs.get(i).getOffsetBegin() <= np.getOffsetBegin() && curSentenceNPs.get(i).getOffsetEnd() > np.getOffsetEnd()))) {
                        itr.remove();
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < review.getNounPhrases().size(); i++) {
            review.getNounPhrases().get(i).setId(i);
        }
    }

    //Discard the NP that has many HEADS, in particular it is in the form NP => NP1 (CC|,) NP2 (CC|,) ... 
    private static boolean isDiscardedConjNP(NounPhrase np) {
        List<Tree> npChildren = np.getNpNode().getChildrenAsList();
        for (int i = 0; i < npChildren.size(); ++i) {
            if (!npChildren.get(i).value().equals("NP") && !npChildren.get(i).value().equals("CC") && !npChildren.get(i).value().equals(",")) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDiscardedPersonalPronounNP(NounPhrase np) {
        if (DISCARDED_PERSONAL_PRONOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    private static boolean isDiscardedTimeNP(NounPhrase np) {
        if (DISCARDED_TIME_NOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        if (np.getHeadNode().value().toLowerCase().equals("time") && np.getNpNode().getLeaves().size() == 1) {
            return true;
        }

        return false;
    }

    //Discard all Existential NP: There
    private static boolean isDiscardedNPByPOS(NounPhrase np) {
        if (np.getHeadLabel().equals(DISCARDED_EX_NOUN_POS)) {
            return true;
        }
        return false;
    }

    //Discard all NP indicating quantity: lot, lots, number, total
    private static boolean isDiscardedQuantityNP(NounPhrase np) {
        if (DISCARDED_QUANTITY_NOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    private static boolean isDiscardedCurrencyNP(NounPhrase np) {
        if (np.getHeadNode().value().contains("$")) {
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

    private static void createTrain(NounPhrase np1, NounPhrase np2, Review review, BufferedWriter bwtrain) throws IOException {
        bwtrain.write(np1.getReviewId() + ",");
        bwtrain.write(np1.getId() + ",");
        bwtrain.write(np2.getId() + ",");
        bwtrain.write(FeatureExtractor.is_Pronoun(np1).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Pronoun(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Definite_NP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Demonstrative_NP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isBothPropername(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.count_Distance(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.numberAgreementExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBetweenExtract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.has_Between_Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.comparativeIndicatorExtract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.sentimentConsistencyExtract(np1, np2) + ",");
//        bwtrain.write(FeatureExtractor.PMI(np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isCoref(np1, np2).toString());
        bwtrain.newLine();
    }

    private static void createTest(NounPhrase np1, NounPhrase np2, Review review, BufferedWriter bwtrain, Integer IdPMIinList) throws IOException {
        bwtrain.write(np1.getReviewId() + ",");
        bwtrain.write(np1.getId() + ",");
        bwtrain.write(np2.getId() + ",");
//        bwtrain.write(Math.abs(np2.getId() - np1.getId()) + ",");
        bwtrain.write(FeatureExtractor.is_Pronoun(np1).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Pronoun(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Definite_NP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.is_Demonstrative_NP(np2).toString() + ",");
        bwtrain.write(FeatureExtractor.isBothPropername(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())).toString() + ",");
        bwtrain.write(FeatureExtractor.count_Distance(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.numberAgreementExtract(np1, np2) + ",");
        bwtrain.write(FeatureExtractor.isBetween3Extract(review, np1, np2).toString() + ",");
        bwtrain.write(FeatureExtractor.has_Between_Extract(review, np1, np2).toString() + ",");
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

    private static void createCheck(NounPhrase np2, Review review, BufferedWriter bwtrain) throws IOException {
        bwtrain.write(np2.getReviewId() + ";" + np2.getId() + ";" + np2.getNpNode().getLeaves().toString() + ";" + np2.getHeadNode().toString() + ";");
        bwtrain.write(FeatureExtractor.is_Pronoun(np2).toString() + ";");
        bwtrain.write(FeatureExtractor.is_Definite_NP(np2).toString() + ";");
        bwtrain.write(FeatureExtractor.is_Demonstrative_NP(np2).toString() + ";");
        bwtrain.write(FeatureExtractor.is_Proper_name(np2).toString());
        bwtrain.newLine();
    }
}
