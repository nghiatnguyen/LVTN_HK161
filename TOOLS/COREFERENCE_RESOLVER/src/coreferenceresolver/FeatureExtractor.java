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
public class FeatureExtractor {

    private String SINGULAR_PRONOUNS = ";it;";
    private String PLURAL_PRONOUNS = ";they;";

    /**
     * Check if two Noun Phrases agree in NUMBER
     *
     * @param np1
     * @param np2
     * @return true if agree, otherwise false
     */
    public boolean numberAgreementExtract(NounPhrase np1, NounPhrase np2) {
        if (SINGULAR_PRONOUNS.contains(";" + np1.getHeadNode().value() + ";")
                && SINGULAR_PRONOUNS.contains(";" + np2.getHeadNode().value() + ";")) {
            return true;
        }
        if (PLURAL_PRONOUNS.contains(";" + np1.getHeadNode().value() + ";")
                && PLURAL_PRONOUNS.contains(";" + np2.getHeadNode().value() + ";")) {
            return true;
        }

        if ((np1.getHeadLabel().equals("NN") || np1.getHeadLabel().equals("NNP"))
                && (np2.getHeadLabel().equals("NN") || np1.getHeadLabel().equals("NNP"))) {
            return true;
        }

        if ((np1.getHeadLabel().equals("NNS") || np1.getHeadLabel().equals("NNPS"))
                && (np2.getHeadLabel().equals("NNS") || np1.getHeadLabel().equals("NNPS"))) {
            return true;
        }

        return false;
    }
    
    /**
     * Check if there is a TOBE between two Noun Phrases
     * @param np1
     * @param np2
     * @return true if is-between and no comparative indicator between, otherwise false
     */
    public boolean isBetweenExtract(NounPhrase np1, NounPhrase np2){
        return false;
    }
    
    /**
     * Check if there is a comparative indicator between two Noun Phrases
     * @param np1
     * @param np2
     * @return true if there is a comparative indicator between, otherwise false
     */
    public boolean comparativeIndicatorExtract(NounPhrase np1, NounPhrase np2){
        return false;
    }
}
