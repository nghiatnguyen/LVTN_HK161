/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

import java.util.ArrayList;
import java.util.List;

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
    private int offsetBegin;
    private int offsetEnd;
    private int id;
    private int refId;
    private int type;
    private boolean isSuperior;
    private boolean isInferior;
    private ArrayList<String> opinionWords;
    private ArrayList<CRFToken> CRFTokens;
    private int sentimentOrientation;
    
    public NounPhrase(){
    	this.CRFTokens = new ArrayList<>();
        this.opinionWords =  new ArrayList<>();
    	this.opinionWords = new ArrayList<String>();
    }

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
        if (this.headNode != null){
            CoreLabel label =  (CoreLabel) headNode.label();
            this.setHeadLabel(label.get(CoreAnnotations.PartOfSpeechAnnotation.class));
        }        
    }

    /**
     * @param headLabel the headLabel to set
     */
    public void setHeadLabel(String headLabel) {
        this.headLabel = headLabel;
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
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public void addOpinionWord(String opnionWordAdded) {
        this.opinionWords.add(opnionWordAdded);
    }

    /**
     * @return the opinionWords
     */
    public List<String> getOpinionWords() {
        return opinionWords;
    }

    public void setOpinionWord() {
        this.opinionWords = new ArrayList<String>();
    }

    /**
     * @return the isSuperior
     */
    public boolean isSuperior() {
        return isSuperior;
    }

    /**
     * @param isSuperior the isSuperior to set
     */
    public void setSuperior(boolean isSuperior) {
        this.isSuperior = isSuperior;
    }

    /**
     * @return the isInferior
     */
    public boolean isInferior() {
        return isInferior;
    }

    /**
     * @param isInferior the isInferior to set
     */
    public void setInferior(boolean isInferior) {
        this.isInferior = isInferior;
    }

    /**
     * @return the refId -1 for the NP with no REF
     */
    public int getRefId() {
        return refId;
    }

    /**
     * @param refId the refId to set
     */
    public void setRefId(int refId) {
        this.refId = refId;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
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

    public void addCRFToken(CRFToken e){
    	this.CRFTokens.add(e);
    }
    
    public ArrayList<CRFToken> getCRFTokens(){
    	return this.CRFTokens;
    }
    
}
