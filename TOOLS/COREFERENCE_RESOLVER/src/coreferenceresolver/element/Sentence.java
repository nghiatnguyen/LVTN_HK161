/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

import coreferenceresolver.util.Util;
import edu.stanford.nlp.trees.TypedDependency;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author TRONGNGHIA
 */
public class Sentence {

    private List<Token> tokens;
    private List<NounPhrase> nounPhrases;
    private int reviewId;
    private String rawContent;
    private int offsetBegin;
    private int offsetEnd;
    private int sentimentLevel;
    private List<Token> comparativeIndicatorTokens;
    private boolean comparativeSentence;
    private List<Token> opinionWords;
    private Collection dependencies;

    public Sentence() {
        tokens = new ArrayList<>();
        nounPhrases = new ArrayList<>();
        comparativeIndicatorTokens = new ArrayList<>();
        opinionWords = new ArrayList<>();
    }

    /**
     * @return the tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * @param tokenAdded the tokens to set
     */
    public void addToken(Token tokenAdded) {
        this.tokens.add(tokenAdded);
        if (tokenAdded.getSentimentOrientation() != Util.NEUTRAL) {
            this.opinionWords.add(tokenAdded);
        }
    }

    /**
     * @return the nounPhrases
     */
    public List<NounPhrase> getNounPhrases() {
        return nounPhrases;
    }

    /**
     * @param nounPhraseAdded the nounPhrases to set
     */
    public void addNounPhrase(NounPhrase nounPhraseAdded) {
        this.nounPhrases.add(nounPhraseAdded);
    }

    /**
     * @return the reviewId
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * @param reviewId the reviewId to set
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * @return the rawContent
     */
    public String getRawContent() {
        return rawContent;
    }

    /**
     * @param rawContent the rawContent to set
     */
    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    /**
     * @return the offsetBegin
     */
    public int getOffsetBegin() {
        return offsetBegin;
    }

    /**
     * @param offsetBegin the offsetBegin to set
     */
    public void setOffsetBegin(int offsetBegin) {
        this.offsetBegin = offsetBegin;
    }

    /**
     * @return the sentimentLevel
     */
    public int getSentimentLevel() {
        return sentimentLevel;
    }

    /**
     * @param sentimentLevel the sentimentLevel to set
     */
    public void setSentimentLevel(int sentimentLevel) {
        switch (sentimentLevel) {
            case 0:
            case 1:
                this.sentimentLevel = Util.NEGATIVE;
                break;
            case 2:
                this.sentimentLevel = Util.NEUTRAL;
                break;
            case 3:
            case 4:
                this.sentimentLevel = Util.POSITIVE;
                break;
            default:
                break;
        }
    }

    /**
     * @return the comparativeIndicatorTokens
     */
    public List<Token> getComparativeIndicatorTokens() {
        return comparativeIndicatorTokens;
    }

    /**
     * @param comparativeIndicatorTokens the comparativeIndicatorTokens to set
     */
    public void initComparatives(List<Token> comparativeIndicatorTokens) {
        this.comparativeIndicatorTokens = comparativeIndicatorTokens;
        if (!this.comparativeIndicatorTokens.isEmpty()) {
            this.comparativeSentence = true;            
        }
    }

    /**
     * @return the offsetEnd
     */
    public int getOffsetEnd() {
        return offsetEnd;
    }

    /**
     * @param offsetEnd the offsetEnd to set
     */
    public void setOffsetEnd(int offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    /**
     * @return the comparativeSentence
     */
    public boolean isComparativeSentence() {
        return comparativeSentence;
    }

    /**
     * Get 2 NPs relating to Comparative Phrase and assign to them SUPERIOR or
     * INFERIOR
     *
     * @return
     */
    public boolean initComparativeNPs() {
        //If the sentence has no NPs or no comparatives or has neutral sentiment, return
        if (this.nounPhrases.size() <= 0 || this.comparativeIndicatorTokens.isEmpty() || this.sentimentLevel == Util.NEUTRAL) {
            return false;
        }

        int comparativesOffsetBegin = this.comparativeIndicatorTokens.get(0).getOffsetBegin();
        int comparativesOffsetEnd = this.comparativeIndicatorTokens.get(comparativeIndicatorTokens.size() - 1).getOffsetEnd();
        int maxNearestSmallerNp = 0;
        int minNearestBiggerNp = nounPhrases.size() - 1;
        for (int i = 1; i < nounPhrases.size(); ++i) {
            NounPhrase curNp = nounPhrases.get(i);
            if (curNp.getOffsetEnd() < comparativesOffsetBegin && curNp.getOffsetEnd() > nounPhrases.get(maxNearestSmallerNp).getOffsetEnd()) {
                maxNearestSmallerNp = i;
            }
            if (curNp.getOffsetBegin() > comparativesOffsetEnd && curNp.getOffsetBegin() < nounPhrases.get(minNearestBiggerNp).getOffsetBegin()) {
                minNearestBiggerNp = i;
            }
        }

        if (minNearestBiggerNp != maxNearestSmallerNp) {
            nounPhrases.get(minNearestBiggerNp).setSuperior(sentimentLevel == Util.POSITIVE);
            nounPhrases.get(minNearestBiggerNp).setInferior(sentimentLevel == Util.NEGATIVE);
            nounPhrases.get(maxNearestSmallerNp).setSuperior(sentimentLevel == Util.NEGATIVE);
            nounPhrases.get(maxNearestSmallerNp).setInferior(sentimentLevel == Util.POSITIVE);
        }

        return true;
    }

    /**
     * @return the opinionWords
     */
    public List<Token> getOpinionWords() {
        return opinionWords;
    }

    /**
     * @param opinionWordAdded the opinionWord to add
     */
    public void addOpinionWord(Token opinionWordAdded) {
        this.opinionWords.add(opinionWordAdded);
    }

    public void setSentimentForNPs() {
        if (this.nounPhrases == null || this.opinionWords == null) {
            return;
        }

        for (NounPhrase np : this.nounPhrases) {
            double sentimentScore = 0;
            for (Token owToken : this.opinionWords) {
                double distanceNP_OW = 0;
                for (Token token : this.tokens) {
                    if (np.getOffsetEnd() < owToken.getOffsetBegin() && token.getOffsetBegin() > np.getOffsetEnd()
                            && token.getOffsetEnd() < owToken.getOffsetBegin()) {
                        ++distanceNP_OW;
                    } else if (np.getOffsetBegin() > owToken.getOffsetEnd() && token.getOffsetEnd() < np.getOffsetBegin()
                            && token.getOffsetBegin() > owToken.getOffsetEnd()) {
                        ++distanceNP_OW;
                    }
                }

                if (np.getOffsetBegin() <= owToken.getOffsetBegin() && owToken.getOffsetEnd() <= np.getOffsetEnd()) {
                    sentimentScore = (double) owToken.getSentimentOrientation() / 0.5;
                } else {
                    sentimentScore += (double) owToken.getSentimentOrientation() / (distanceNP_OW + 1.0);
                }
            }
            np.setSentimentOrientation(sentimentScore > 0 ? Util.POSITIVE : sentimentScore < 0 ? Util.NEGATIVE : Util.NEUTRAL);
        }
    }

    /**
     * @return the dependencies
     */
    public Collection<TypedDependency> getDependencies() {
        return dependencies;
    }

    /**
     * @param dependencies the dependencies to set
     */
    public void setDependencies(Collection dependencies) {
        this.dependencies = dependencies;
    }
}
