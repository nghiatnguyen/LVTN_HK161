/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

/**
 *
 * @author TRONGNGHIA
 */
public class NounPhrase {
    private Tree npNode;
    private Tree headNode;
    private String headLabel;
    private int sentenceId;
    private int reviewId;

    /**
     * @return the npNode
     */
    public Tree getNpNode() {
        return npNode;
    }

    /**
     * @param npNode the npNode to set
     */
    public void setNpNode(Tree npNode) {
        this.npNode = npNode;
    }

    /**
     * @return the headNode
     */
    public Tree getHeadNode() {
        return headNode;
    }

    /**
     * @param headNode the headNode to set
     */
    public void setHeadNode(Tree headNode) {
        this.headNode = headNode;
        CoreLabel label = (CoreLabel) headNode.label();
        this.headLabel = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);
    }

    /**
     * @return the headLabel
     */
    public String getHeadLabel() {
        return headLabel;
    }

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
