/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.FileWriter;
import java.io.IOException;
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

    private static String DISCARDED_PERSONAL_PRONOUNS = ";i;me;you;he;him;his;she;her;hers;";

    public static void markupOutFileFromReview(Review review, FileWriter fw) throws IOException {

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
//            System.out.println("Raw NP " + rawNp);
//            System.out.println("Consider " + markupReview.substring(openNpOffsets.get(i)));

            String regex = regex(rawNp);
            pattern = Pattern.compile(regex);
//            System.out.println("Regex " + regex);
            String subString = markupReview.substring(openNpOffsets.get(i));
            matcher = pattern.matcher(subString);
            if (matcher.find()) {
//                System.out.println("Found: " + matcher.group());
                subString = markupReview.substring(openNpOffsets.get(i)).replaceFirst(matcher.group(), matcher.group() + ">");
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

    private static boolean isDiscardedPersonalPronoun(NounPhrase np) {
        if (DISCARDED_PERSONAL_PRONOUNS.contains(";" + np.getHeadNode().value().toLowerCase() + ";")) {
            return true;
        }
        return false;
    }

    private static String regex(String sequence) {
        return sequence.replaceAll("\\s", " <*");
    }
}
