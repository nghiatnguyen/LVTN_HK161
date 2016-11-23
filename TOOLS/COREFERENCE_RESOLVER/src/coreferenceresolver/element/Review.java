/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TRONGNGHIA
 */
public class Review {    

    private List<Sentence> sentences;
    private List<NounPhrase> nounPhrases;
    private String rawContent;
    private List<CorefChain> corefChainsPredict;
    private List<CorefChain> corefChainsActual;
    private List<Integer> markupOpens;
    private List<Integer> markupCloses;
    private List<Integer> simpleNpTypes;

    public Review() {
        sentences = new ArrayList<>();
        nounPhrases = new ArrayList<>();
        corefChainsPredict = new ArrayList<>();
        corefChainsActual = new ArrayList<>();
        markupOpens = new ArrayList<>();
        markupCloses = new ArrayList<>();
        simpleNpTypes = new ArrayList<>();
    }

    /**
     * @return the sentences
     */
    public List<Sentence> getSentences() {
        return sentences;
    }

    /**
     * @param sentenceAdded the sentences to set
     */
    public void addSentence(Sentence sentenceAdded) {
        this.sentences.add(sentenceAdded);
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
     * @return the nounPhrases
     */
    public List<NounPhrase> getNounPhrases() {
        return nounPhrases;
    }

    /**
     * @param nounPhraseAdded the nounPhrase to set
     */
    public void addNounPhrase(NounPhrase nounPhraseAdded) {
        this.nounPhrases.add(nounPhraseAdded);
    }

    /**
     * @return the corefChains
     */
    public List<CorefChain> getCorefChainsPredict() {
        return corefChainsPredict;
    }
    
    public List<CorefChain> getCorefChainsActual(){
    	return corefChainsActual;
    }

    /**
     * @param np1Id, np2Id
     */
    public void addCorefChainPredict(int np1Id, int np2Id) {
        int np1ChainExisted = -1;
        int np2ChainExisted = -1;
        int chainNo = 0;
        for (CorefChain cc: this.corefChainsPredict){
            for (int npId: cc.getChain()){
                if (npId == np1Id){
                    np1ChainExisted = chainNo;
                }
                else if (npId == np2Id){
                    np2ChainExisted = chainNo;
                }
            }
            ++chainNo;
        }
        if (np1ChainExisted != -1 && np2ChainExisted == -1){
            this.corefChainsPredict.get(np1ChainExisted).addCoref(np2Id);
        }
        else if (np2ChainExisted != -1 && np1ChainExisted == -1){
            this.corefChainsPredict.get(np2ChainExisted).addCoref(np1Id);
        }
        else if (np2ChainExisted == -1 && np1ChainExisted == -1){
            CorefChain newCC = new CorefChain();
            newCC.addCoref(np1Id);
            newCC.addCoref(np2Id);
            this.corefChainsPredict.add(newCC);
        }        
    }
    
    
    public void addCorefChainActual(int np1Id, int np2Id) {
        int np1ChainExisted = -1;
        int np2ChainExisted = -1;
        int chainNo = 0;
        for (CorefChain cc: this.corefChainsActual){
            for (int npId: cc.getChain()){
                if (npId == np1Id){
                    np1ChainExisted = chainNo;
                }
                else if (npId == np2Id){
                    np2ChainExisted = chainNo;
                }
            }
            ++chainNo;
        }
        if (np1ChainExisted != -1 && np2ChainExisted == -1){
            this.corefChainsActual.get(np1ChainExisted).addCoref(np2Id);
        }
        else if (np2ChainExisted != -1 && np1ChainExisted == -1){
            this.corefChainsActual.get(np2ChainExisted).addCoref(np1Id);
        }
        else if (np2ChainExisted == -1 && np1ChainExisted == -1){
            CorefChain newCC = new CorefChain();
            newCC.addCoref(np1Id);
            newCC.addCoref(np2Id);
            this.corefChainsActual.add(newCC);
        }        
    }

    /**
     * @return the markupOpens
     */
    public List<Integer> getMarkupOpens() {
        return markupOpens;
    }

    /**
     * @param markupOpen the markupOpen to add
     */
    public void addMarkupOpen(int markupOpen) {
        this.markupOpens.add(markupOpen);
    }

    /**
     * @return the markupCloses
     */
    public List<Integer> getMarkupCloses() {
        return markupCloses;
    }

    /**
     * @param markupClose the markupClose to add
     */
    public void addMarkupClose(int markupClose) {
        this.markupCloses.add(markupClose);
    }
    
    /**
     * @return the markupCloses
     */
    public List<Integer> getSimpleNpTypes() {
        return this.simpleNpTypes;
    }

    /**
     * @param simpleNpType the simpleNpType to add
     */
    public void addSimpleNpType(int simpleNpType) {
        this.simpleNpTypes.add(simpleNpType);
    }
}
