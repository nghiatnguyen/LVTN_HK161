/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

/**
 *
 * @author TRONGNGHIA
 */
public class Token {    

    public static int NEGATIVE = -1;
    public static int POSITIVE = 1;
    public static int NEUTRAL = 0;
    
    private String word;
    private String POS;
    private int offsetBegin;
    private int offsetEnd;
    private int opinionOrientation = NEUTRAL;       

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
     * @return the opinionOrientation
     */
    public int getOpinionOrientation() {
        return opinionOrientation;
    }

    /**
     * @param opinionOrientation the opinionOrientation to set
     */
    public void setOpinionOrientation(int opinionOrientation) {
        this.opinionOrientation = opinionOrientation;
    }
}
