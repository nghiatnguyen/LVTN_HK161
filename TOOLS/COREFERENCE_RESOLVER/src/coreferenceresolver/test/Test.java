/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.weka.Weka;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {

    public static void main(String... args) throws IOException, Exception {
        StanfordUtil.reviews = new ArrayList<>();
        for (int i = 0; i < 50; ++i){
            Review review = new Review();
            StanfordUtil.reviews.add(review);
        }
        Weka.j48Classify(".\\test.arff", ".\\classified.txt");
        for (Review review: StanfordUtil.reviews){
            if (review.getCorefChains().size() > 0){
                System.out.println("NEW REVIEW: ");
            }            
            for (CorefChain cc: review.getCorefChains()){
                System.out.println("New chain: ");
                for (int coref: cc.getChain()){
                    System.out.print(coref + " ");
                }
                System.out.println();
            }
        }
    }
}
