/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class TrainingMain {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File inputFile = new File(".\\input.txt");
        File markupFile = new File(".\\markup.out.txt");
        StanfordUtil su = new StanfordUtil(inputFile);

        try {
            //Init every info
            su.init();

            int i = 0;
            //Begin create training set
            for (Review review : StanfordUtil.reviews) {

                //Discard all NPs that is Personal Pronoun
                Util.discardPersonalProNPs(review);

                //Read the hand-modified markup file
                Util.readMarkupFile(markupFile);

//                StanfordUtil.test();
                System.out.println("-----BEGIN REVIEW " + i + "-----");

                //Extract features
                Util.extractFeatures(review);
//                StanfordUtil.test();

                System.out.println("-----END REVIEW-----");

                ++i;
            }
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
