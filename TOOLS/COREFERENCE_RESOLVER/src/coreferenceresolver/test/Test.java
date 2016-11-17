/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import static coreferenceresolver.util.StanfordUtil.pipeline;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.weka.Weka;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {

    public static void main(String... args) throws IOException, Exception {
        StanfordUtil.reviews = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            Review review = new Review();
            StanfordUtil.reviews.add(review);
        }
        Weka.j48Classify(".\\test.arff", ".\\classified.txt");
        int reviewNo = 0;
        for (Review review : StanfordUtil.reviews) {
            if (review.getCorefChains().size() > 0) {
                System.out.println("--REVIEW " + reviewNo + "--");
            }
            for (CorefChain cc : review.getCorefChains()) {
                System.out.println("New chain: ");
                for (int coref : cc.getChain()) {
                    System.out.print(coref + " ");
                }
                System.out.println();
            }
            ++reviewNo;
        }        
//        CollinsHeadFinder headFinder = new CollinsHeadFinder();
//        Properties props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, parse");
//        pipeline = new StanfordCoreNLP(props);
//        Annotation document = new Annotation("less than two hours");
//        Annotation emptyDocument = new Annotation("Dog");
//
//        // run all Annotators on this text
//        pipeline.annotate(document);
//        pipeline.annotate(emptyDocument);
//        
//        Tree node = null;
//        
//        List<CoreMap> sentences = emptyDocument.get(CoreAnnotations.SentencesAnnotation.class);
//        
//        for (CoreMap sentence : sentences) {
//            node = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).children()[0];            
//        }                
//        
//        node.removeChild(0);
//        
//        System.out.println(node.value());
//        for (Tree c: node.children()){
//            System.out.println(c.value());
//        }
//        
//        sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//        
//        //Begin extracting from paragraphs
//        for (CoreMap sentence : sentences) {  
//            List<Tree> sentenceTreeLeaves = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).getLeaves();
//            for (Tree tokenTree : sentenceTreeLeaves) {                
//                node.addChild(tokenTree);
//            }
//        }                
//        
//        System.out.println("Head " + node.headTerminal(headFinder));  
//        
//        for (Tree c: node.children()){
//            System.out.println(c.value());
//        }
    }
}
