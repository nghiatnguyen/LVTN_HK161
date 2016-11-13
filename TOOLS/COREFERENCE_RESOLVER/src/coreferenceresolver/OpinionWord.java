/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

/**
 *
 * @author TRONGNGHIA
 */
public class OpinionWord {

    private String word;
    private int sentimentOrientation;
    private int offsetBegin;
    private int offsetEnd;

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
     * @return the sentimentOrientation
     */
    public int getSentimentOrientation() {
        return sentimentOrientation;
    }

    /**
     * @param sentimentOrientation the sentimentOrientation to set
     */
    public void setSentimentOrientation(int sentimentOrientation) {
        this.sentimentOrientation = sentimentOrientation;
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
}
