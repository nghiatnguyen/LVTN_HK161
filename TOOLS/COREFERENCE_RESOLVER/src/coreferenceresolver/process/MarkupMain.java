/*
  * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.process;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.util.Util;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.CrfChunkerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
        // Read the Dataset
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

            //Call CRFChunker, result is in input.txt.pos.chk file
            CrfChunkerUtil.runChunk();

            //Read from input.txt.pos.chk file. Get all NPs
            List<NounPhrase> nounPhrases = CrfChunkerUtil.readCrfChunker();

            Util.assignNounPhrases(nounPhrases, StanfordUtil.reviews);

            for (NounPhrase np : nounPhrases) {
                System.out.print(np.getNpNode().getLeaves() + "\t");
                if (np.getHeadNode() != null) {
                    System.out.print("Head: " + np.getHeadNode().getLeaves() + "\t");
                    System.out.print("Head Label: " + np.getHeadLabel());
                }
                System.out.println();
            }

            //Begin markup
            for (Review review : StanfordUtil.reviews) {

                //Discard all NPs that is Personal Pronoun
                Util.discardUnneccessaryNPs(review);

                //Create output file for markup
                Util.initMarkupFile(review, fw);
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MarkupMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
