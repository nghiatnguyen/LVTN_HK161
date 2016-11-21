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
    private List<Token> comparativeIndicatorPhrases;
    private boolean comparativeSentence;
    private List<OpinionWord> opinionWords;
    private Collection dependencies;

    public static final int NEGATIVE_SENTIMENT = 0;
    public static final int NEUTRAL_SENTIMENT = 1;
    public static final int POSITIVE_SENTIMENT = 2;

    public Sentence() {
        tokens = new ArrayList<>();
        nounPhrases = new ArrayList<>();
        comparativeIndicatorPhrases = new ArrayList<>();
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
                this.sentimentLevel = NEGATIVE_SENTIMENT;
                break;
            case 2:
                this.sentimentLevel = NEUTRAL_SENTIMENT;
                break;
            case 3:
            case 4:
                this.sentimentLevel = POSITIVE_SENTIMENT;
                break;
            default:
                break;
        }
    }

    /**
     * @return the comparativeIndicatorPhrases
     */
    public List<Token> getComparativeIndicatorPhrases() {
        return comparativeIndicatorPhrases;
    }

    /**
     * @param comparativeIndicatorPhrases the comparativeIndicatorPhrases to set
     */
    public void setComparativeIndicatorPhrases(List<Token> comparativeIndicatorPhrases) {
        this.comparativeIndicatorPhrases = comparativeIndicatorPhrases;
        if (!this.comparativeIndicatorPhrases.isEmpty()) {
            comparativeSentence = true;
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
        //If the sentence has no sentences or no comparatives or has neutral sentiment, return
        if (nounPhrases.size() <= 0 || comparativeIndicatorPhrases.isEmpty() || sentimentLevel == NEUTRAL_SENTIMENT) {
            return false;
        }

        int comparativesOffsetBegin = comparativeIndicatorPhrases.get(0).getOffsetBegin();
        int comparativesOffsetEnd = comparativeIndicatorPhrases.get(comparativeIndicatorPhrases.size() - 1).getOffsetEnd();
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
            nounPhrases.get(minNearestBiggerNp).setSuperior(sentimentLevel == POSITIVE_SENTIMENT);
            nounPhrases.get(minNearestBiggerNp).setInferior(sentimentLevel == NEGATIVE_SENTIMENT);
            nounPhrases.get(maxNearestSmallerNp).setSuperior(sentimentLevel == NEGATIVE_SENTIMENT);
            nounPhrases.get(maxNearestSmallerNp).setInferior(sentimentLevel == POSITIVE_SENTIMENT);
        }

        return true;
    }

    /**
     * @return the opinionWords
     */
    public List<OpinionWord> getOpinionWords() {
        return opinionWords;
    }

    /**
     * @param opinionWordAdded the opinionWord to add
     */
    public void addOpinionWord(OpinionWord opinionWordAdded) {
        this.opinionWords.add(opinionWordAdded);
    }
    
    public void setOpinionForNPs(){            
        if (this.nounPhrases == null || this.opinionWords == null){            
            return;
        }                        
        
        for (NounPhrase np: this.nounPhrases){            
            double opinionScore = 0;
            for (OpinionWord ow: this.opinionWords){                
                double distanceNP_OW = 0;
                for (Token token: this.tokens){
                    if (np.getOffsetEnd() < ow.getOffsetBegin() && token.getOffsetBegin() > np.getOffsetEnd() 
                            && token.getOffsetEnd() < ow.getOffsetBegin()){
                        ++distanceNP_OW;
                    }
                    else if (np.getOffsetBegin() > ow.getOffsetEnd() && token.getOffsetEnd() < np.getOffsetBegin() 
                            && token.getOffsetBegin() > ow.getOffsetEnd()){
                        ++distanceNP_OW;
                    }
                    
                }
                
                if (np.getOffsetBegin() <= ow.getOffsetBegin() && ow.getOffsetEnd()<= np.getOffsetEnd()){
                    opinionScore = (double) ow.getSentimentOrientation()/0.5;
                }
                else {
                    opinionScore += (double) ow.getSentimentOrientation()/(distanceNP_OW + 1.0);
                }                
            }
            np.setSentimentOrientation(opinionScore > 0? Util.POSITIVE : opinionScore < 0 ? Util.NEGATIVE : Util.NEUTRAL);
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
