/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import java.io.IOException;
import java.util.Properties;
import coreferenceresolver.util.Util;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {    
    
    public static void main(String... args) throws IOException {
//        new StanfordUtil(new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input.txt")).simpleInit();
        String text = "the phone";
        CollinsHeadFinder headFinder = new CollinsHeadFinder();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        System.out.println(Util.findPhraseHead(text, headFinder, pipeline));
    }
}
