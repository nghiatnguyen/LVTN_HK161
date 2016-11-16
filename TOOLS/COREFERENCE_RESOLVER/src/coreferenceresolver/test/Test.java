/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.StanfordUtil;
import static coreferenceresolver.util.StanfordUtil.pipeline;
import coreferenceresolver.util.Util;
import coreferenceresolver.weka.Weka;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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
    }
}
