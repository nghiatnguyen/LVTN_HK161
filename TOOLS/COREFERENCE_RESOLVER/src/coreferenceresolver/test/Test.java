/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.util.CrfChunkerUtil;
import coreferenceresolver.util.StanfordUtil;
import java.io.IOException;
import java.io.File;
import java.util.List;
import coreferenceresolver.element.CRFToken;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {

    public static void main(String... args) throws IOException {
        StanfordUtil su = new StanfordUtil(new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input2.txt"));
        //Init every info
        su.simpleInit();

        //Call CRFChunker, result is in input.txt.pos.chk file
        CrfChunkerUtil.runChunk();

        //Read from input.txt.pos.chk file. Get all NPs
        List<NounPhrase> nps = CrfChunkerUtil.readCrfChunker();
        for (NounPhrase np : nps) {
            System.out.println("New NP");
            System.out.println("NP: " + np.getId());
            System.out.println("NP review id: " + np.getReviewId());
            System.out.println("NP sentence id: " + np.getSentenceId());
            for (CRFToken token : np.getCRFTokens()) {
                System.out.println("Token word: " + token.getWord());
                System.out.println("Token id: " + token.getIdInSentence());
            }
            System.out.println();
        }
    }
}
