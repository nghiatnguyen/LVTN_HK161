/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.util.List;

/**
 *
 * @author TRONGNGHIA
 */
public class FeatureExtractor {

    private static final String SINGULAR_KEYWORDS = ";it;";
    private static final String PLURAL_KEYWORDS = ";they;";
    private static final String[] TO_BEs = {"is", "'s", "are", "'re", "was", "were", "been", "be"};
    private static final String[] SPECIAL_COMPARATIVES = {"inferior", "superior", "junior", "senior", "anterior", "posterior", "prior"};
    private static final String ADJECTIVE = "JJ";
    private static final String COMPARATIVE_ADJECTIVE = "JJR";
    private static final String COMPARATIVE_ADVERB = "RBR";
    private static final String ADVERB = "RB";
    private static final String SINGULAR_NOUN = "NN";
    private static final String PLURAL_NOUN = "NNS";

    /**
     * Check if two Noun Phrases agree in NUMBER
     *
     * @param np1
     * @param np2
     * @return true if agree, otherwise false
     */
    public static boolean numberAgreementExtract(NounPhrase np1, NounPhrase np2) {
        if (SINGULAR_KEYWORDS.contains(";" + np1.getHeadNode().value() + ";")
                && SINGULAR_KEYWORDS.contains(";" + np2.getHeadNode().value() + ";")) {
            return true;
        }
        if (PLURAL_KEYWORDS.contains(";" + np1.getHeadNode().value() + ";")
                && PLURAL_KEYWORDS.contains(";" + np2.getHeadNode().value() + ";")) {
            return true;
        }

        if ((np1.getHeadLabel().equals("NN") || np1.getHeadLabel().equals("NNP"))
                && (np2.getHeadLabel().equals("NN") || np1.getHeadLabel().equals("NNP"))) {
            return true;
        }

        if ((np1.getHeadLabel().equals("NNS") || np1.getHeadLabel().equals("NNPS"))
                && (np2.getHeadLabel().equals("NNS") || np1.getHeadLabel().equals("NNPS"))) {
            return true;
        }

        return false;
    }

    /**
     * Check for is-between feature
     *
     * @param reviews np1 np2
     * @param np1
     * @param np2
     * @return true if there is an 'is-like' between 2 NPs, otherwise false
     */
    public static boolean isBetweenExtract(List<Review> reviews, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (np1.getOffsetEnd() + 1 < np2.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np1.getOffsetEnd() + 1, np2.getOffsetBegin()))) {
                        if (!containsComparativeIndicator(curSentence, np1, np2)) {
                            return true;
                        }
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (np1.getOffsetEnd() + 1 < np2.getOffsetBegin() && contains3rdTobe(curSentence.getRawContent().substring(np2.getOffsetEnd() + 1, np1.getOffsetBegin()))) {
                        if (!containsComparativeIndicator(curSentence, np1, np2)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check for comparativeIndicator-between feature
     *
     * @param reviews
     * @param np1
     * @param np2
     * @return true if there is a comparative indicator between, otherwise false
     */
    public static boolean comparativeIndicatorExtract(List<Review> reviews, NounPhrase np1, NounPhrase np2) {
        if (np1.getReviewId() == np2.getReviewId()) {
            if (np1.getSentenceId() == np2.getSentenceId()) {
                Sentence curSentence = reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId());
                if (np1.getOffsetEnd() < np2.getOffsetBegin()) {
                    if (containsComparativeIndicator(curSentence, np1, np2)) {
                        return true;
                    }
                } else if (np2.getOffsetEnd() < np1.getOffsetBegin()) {
                    if (containsComparativeIndicator(curSentence, np1, np2)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean containsComparativeIndicator(Sentence curSentence, NounPhrase np1, NounPhrase np2) {
        int i = 0;
        List<Token> tokens = curSentence.getTokens();
        while (i < tokens.size()) {
            Token token = tokens.get(i);
            if (token.getOffsetBegin() > np1.getOffsetEnd() && token.getOffsetEnd() < np2.getOffsetBegin()) {

                //Case 1: comparative ADJ + than
                if (isComparativeAdjective(token) || isComparativeAdverb(token)) {
                    if (tokens.get(i + 1).getWord().equals("than")) {
                        return true;
                    }
                }

                if (isAdjective(token) || isAdverb(token)) {
                    if (tokens.get(i + 1).getWord().equals("than")) {
                        //Case 2: more + ADJ/ADV + than
                        if (tokens.get(i - 1).getWord().equals("more")) {
                            return true;
                        }

                        //Case 3: less + ADJ/ADV + than
                        if (tokens.get(i - 1).getWord().equals("less")) {
                            return true;
                        }
                    }

                    if (tokens.get(i + 1).getWord().equals("as")) {
                        //Case 4: as + ADJ/ADV + as
                        if (tokens.get(i - 1).getWord().equals("as")) {
                            return true;
                        }
                    }
                }

                //Case 5: same + N + as
                if (isNoun(token)) {
                    if (tokens.get(i - 1).getWord().equals("same")) {
                        if (tokens.get(i + 1).getWord().equals("as")) {
                            return true;
                        }
                    }
                }

                //Case 6: special comparative adj + to
                if (isSpecialComparativeAdjective(token)) {
                    if (tokens.get(i + 1).getWord().equals("to")) {
                        return true;
                    }
                }
            }

            ++i;
        }

        return false;
    }

    private static boolean contains3rdTobe(String sequence) {
        for (String aTobeVerb : TO_BEs) {
            if (sequence.contains(aTobeVerb)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNoun(Token token) {
        return (token.getPOS().equals(SINGULAR_NOUN) || token.getPOS().equals(PLURAL_NOUN));
    }

    private static boolean isAdjective(Token token) {
        return (token.getPOS().equals(ADJECTIVE));
    }

    private static boolean isComparativeAdjective(Token token) {
        return (token.getPOS().equals(COMPARATIVE_ADJECTIVE));
    }

    private static boolean isSpecialComparativeAdjective(Token token) {
        for (String aSpecialComparative : SPECIAL_COMPARATIVES) {
            if (token.getWord().equals(aSpecialComparative)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isComparativeAdverb(Token token) {
        return (token.getPOS().equals(COMPARATIVE_ADVERB));
    }

    private static boolean isAdverb(Token token) {
        return (token.getPOS().equals(ADVERB));
    }
}
