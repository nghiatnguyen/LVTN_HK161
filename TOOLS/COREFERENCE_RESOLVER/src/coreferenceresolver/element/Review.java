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

    public Review() {
        sentences = new ArrayList<>();
        nounPhrases = new ArrayList<>();
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
}
