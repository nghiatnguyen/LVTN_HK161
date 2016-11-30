/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

import coreferenceresolver.util.Util;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import java.util.Collection;

/**
 *
 * @author TRONGNGHIA
 */
public class Token {

    private String word;
    private String POS;
    private int offsetBegin;
    private int offsetEnd;
    private int sentimentOrientation = Util.NEUTRAL;
    private Tree tokenTree;

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the POS
     */
    public String getPOS() {
        return POS;
    }

    /**
     * @param POS the POS to set
     */
    public void setPOS(String POS) {
        this.POS = POS;
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
     * @return the sentimentOrientation
     */
    public int getSentimentOrientation() {
        return sentimentOrientation;
    }

    /**
     * @param newTokenSentiment the sentimentOrientation to set
     */
    public void setSentimentOrientation(int newTokenSentiment, Collection<TypedDependency> typedDeps) {
        if ((newTokenSentiment == Util.POSITIVE
                || newTokenSentiment == Util.NEGATIVE) && (!this.POS.equals("IN"))) {
            for (TypedDependency typedDependency : typedDeps) {
                if (typedDependency.reln().toString().equals("neg") && typedDependency.gov().value().equals(word)) {
                    newTokenSentiment = Util.reverseSentiment(newTokenSentiment);                    
                }
                if (typedDependency.reln().toString().equals("mark") && typedDependency.gov().value().equals(word)) {
                    newTokenSentiment = Util.reverseSentiment(newTokenSentiment);                    
                }
            }
        }
        this.sentimentOrientation = newTokenSentiment;
    }

    /**
     * @return the tokenTree
     */
    public Tree getTokenTree() {
        return tokenTree;
    }

    /**
     * @param tokenTree the tokenTree to set
     */
    public void setTokenTree(Tree tokenTree) {
        this.tokenTree = tokenTree;
    }
}
