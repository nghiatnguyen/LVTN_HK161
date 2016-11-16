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
    private List<CorefChain> corefChains;

    public Review() {
        sentences = new ArrayList<>();
        nounPhrases = new ArrayList<>();
        corefChains = new ArrayList<>();
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
    public List<CorefChain> getCorefChains() {
        return corefChains;
    }

    /**
     * @param np1Id, np2Id
     */
    public void addCorefChain(int np1Id, int np2Id) {
        int np1ChainExisted = -1;
        int np2ChainExisted = -1;
        int chainNo = 0;
        for (CorefChain cc: this.corefChains){
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
            this.corefChains.get(np1ChainExisted).addCoref(np2Id);
        }
        else if (np2ChainExisted != -1 && np1ChainExisted == -1){
            this.corefChains.get(np2ChainExisted).addCoref(np1Id);
        }
        else if (np2ChainExisted == -1 && np1ChainExisted == -1){
            CorefChain newCC = new CorefChain();
            newCC.addCoref(np1Id);
            newCC.addCoref(np2Id);
            this.corefChains.add(newCC);
        }        
    }
}
