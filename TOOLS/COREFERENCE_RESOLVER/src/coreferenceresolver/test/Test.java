/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;


import coreferenceresolver.element.CRFToken;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.element.Sentence;
import coreferenceresolver.element.Token;
import coreferenceresolver.process.TrainingMain;
import coreferenceresolver.util.StanfordUtil;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {

    public static void main(String... args) throws Exception {      
        String inputFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\input_test.txt";
        String markupFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\input_test.txt.markup";
        String outputFile = "E:\\REPOSITORIES\\LVTN_HK161\\DATASET\\demo_test.txt";
        TrainingMain.run(inputFile, markupFile, outputFile, true);
//        new StanfordUtil(new File(inputFile)).init();
        for (Review review: StanfordUtil.reviews){
            System.out.println("NEW REVIEW");
            for (Sentence sentence: review.getSentences()){
                System.out.println(sentence.getRawContent());                
                System.out.println("Sentiment " + sentence.getSentimentLevel());
                System.out.println("Is Comparative Sentence " + sentence.isComparativeSentence());
                System.out.println("Opinion Words");
                for (Token tok: sentence.getOpinionWords()){
                    System.out.println(tok.getWord() + " " + tok.getSentimentOrientation());
                }  
                System.out.println("NP");
                for (NounPhrase np: sentence.getNounPhrases()){
                    for (CRFToken tok: np.getCRFTokens()){
                        System.out.print(tok.getWord() + " ");
                        System.out.println();
                    }
                    System.out.println(np.getSentimentOrientation() + " " + np.isSuperior() 
                            + " " + np.isInferior());
                }  
                System.out.println("----------");
            }
        }
    }        
}
