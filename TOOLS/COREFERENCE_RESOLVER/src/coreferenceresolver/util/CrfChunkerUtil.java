/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.util;

import crf.chunker.*;

/**
 *
 * @author TRONGNGHIA
 */
public class CrfChunkerUtil {

    public static void main(String... args) {

        CRFChunker.displayCopyright();

        String modelDir = "C:\\Users\\TRONGNGHIA\\Documents\\NetBeansProjects\\CoreferenceResolver\\src\\model";
        String inputFile = ".\\input.txt.pos";

        CRFChunker.main(new String[]{"-modeldir", modelDir, "-inputfile", inputFile});
    }
}
