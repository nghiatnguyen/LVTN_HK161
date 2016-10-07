/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.util.List;

/**
 *
 * @author TRONGNGHIA
 */
public class Review {

    private List<Sentence> sentences;

    public Review(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    /**
     * @return the sentences
     */
    public List<Sentence> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }
}
