/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.process.MarkupMain;
import coreferenceresolver.util.CrfChunkerUtil;
import coreferenceresolver.util.ReadCrfChunkerUtil;
import coreferenceresolver.util.StanfordUtil;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import java.io.IOException;
import java.util.Properties;
import coreferenceresolver.util.Util;
import java.io.File;
import java.util.List;
import coreferenceresolver.element.CRFToken;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {    
    
    public static void main(String... args) throws IOException {
//        new StanfordUtil(new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input.txt")).simpleInit();
//        String text = "the phone";
//        CollinsHeadFinder headFinder = new CollinsHeadFinder();
//        Properties props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, parse");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        System.out.println(Util.findPhraseHead(text, headFinder, pipeline));
        StanfordUtil su = new StanfordUtil(new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input2.txt"));
        //Init every info
        su.simpleInit();

        //Call CRFChunker, result is in input.txt.pos.chk file
        CrfChunkerUtil.main(null);

        //Read from input.txt.pos.chk file. Get all NPs
        List<NounPhrase> nps = ReadCrfChunkerUtil.readCrfChunker();
        for (NounPhrase np: nps){
            System.out.println("New NP");
            System.out.println("NP: " + np.getId());
            System.out.println("NP review id: " + np.getReviewId());
            System.out.println("NP sentence id: " + np.getSentenceId());
            for (CRFToken token: np.getListToken()){
                System.out.println("Token word: " + token.getWord());
                System.out.println("Token id: " + token.getIdInSentence());
            }
            System.out.println();
        }
    }
}
