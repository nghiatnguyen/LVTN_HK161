/*
  * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.process;

import coreferenceresolver.util.Util;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.element.Review;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRONGNGHIA
 */
public class MarkupMain {

    private static String sDataset = null;
    private static BufferedReader buffReaderDict;

    public static String get_sDataset() {
        return sDataset;
    }

    public static void set_sDataset(String s) {
        sDataset = s;
    }
    
    public static void markup(String inputFilePath, String markupFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        File markupFile = new File(markupFilePath);
        FileWriter fw = new FileWriter(markupFile);
        StanfordUtil su = new StanfordUtil(inputFile);
        //Read the big database to find relation between NP and OW
        // read the Dataset
        File fData = new File(".\\dataset.txt");
        FileReader fReaderData = new FileReader(fData);
        buffReaderDict = new BufferedReader(fReaderData);
        String line;
        while ((line = buffReaderDict.readLine()) != null) {
            sDataset = sDataset + line + "\n";
        }

        try {
            //Init every info
            su.simpleInit();

            int i = 0;

            //Begin markup
            for (Review review : StanfordUtil.reviews) {
                
//                System.out.println("Mark up for review " + i);
                //Discard all NPs that is Personal Pronoun
                Util.discardUnneccessaryNPs(review);
                
//                //Create output file for markup
                Util.initMarkupFile(review, fw);

            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
