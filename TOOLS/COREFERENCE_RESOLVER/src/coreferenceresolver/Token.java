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
public class Token {
    private int sentenceId;
    private int reviewId;

    /**
     * @return the sentenceId
     */
    public int getSentenceId() {
        return sentenceId;
    }

    /**
     * @param sentenceId the sentenceId to set
     */
    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
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
}
