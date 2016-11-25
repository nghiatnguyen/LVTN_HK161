package coreferenceresolver.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import coreferenceresolver.element.CRFToken;
import coreferenceresolver.element.NounPhrase;
import crf.chunker.CRFChunker;

public class CrfChunkerUtil {

    public static void runChunk() {

        CRFChunker.displayCopyright();

        String modelDir = ".\\src\\model";
        String inputFile = ".\\input.txt.pos";

        CRFChunker.main(new String[]{"-modeldir", modelDir, "-inputfile", inputFile});
    }
    
    public static ArrayList<NounPhrase> readCrfChunker() throws FileNotFoundException, IOException {       
        String inputFilePath = ".\\input.txt.pos.chk";
        ArrayList<NounPhrase> listNP = new ArrayList<>();

        // Open the file
        FileInputStream fstream = new FileInputStream(inputFilePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
        int sentenceId = 0;
        int reviewId = 0;
        int id = 0;
        int idNP = 0;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            ArrayList<CRFToken> listTam = new ArrayList<CRFToken>();
            if (strLine.equals("././O ")) {
                reviewId++;
                sentenceId = 0;
            }
            else {
            while (strLine.indexOf(" ") != -1) {
                String str = strLine.substring(0, strLine.indexOf(" "));
                strLine = strLine.substring(strLine.indexOf(" ") + 1);
                if (str.indexOf("B-NP") != -1) {
                    if (listTam.size() == 0) {
                        CRFToken tk = new CRFToken();
                        tk.setWord(str.substring(0, str.indexOf("/")));
                        tk.setIdInSentence(id);
                        listTam.add(tk);
                    } else {
                        NounPhrase np = new NounPhrase();
                        np.setSentenceId(sentenceId);
                        np.setId(idNP);
                        np.setReviewId(reviewId);
                        if ((listTam.size() != 0) 
                        		&& (listTam.get(listTam.size() - 1).getWord().equals("-RRB-")
                        				|| listTam.get(listTam.size() - 1).getWord().equals("-LRB-")
                        				|| listTam.get(listTam.size() - 1).getWord().equals("so")))
                        				listTam.remove(listTam.size() - 1);
                        
                        if ((listTam.get(0).getWord().equals("'s") || listTam.get(0).getWord().toLowerCase().equals("even")) && (listTam.size() >= 2))
                        	listTam.remove(0);
                        for (CRFToken s : listTam) {
                            np.addCRFToken(s);
                        }
                        listNP.add(np);
                        idNP++;
                        listTam.clear();
                        CRFToken tk = new CRFToken();
                        tk.setWord(str.substring(0, str.indexOf("/")));
                        tk.setIdInSentence(id);
                        listTam.add(tk);
                    }
                } else if (str.indexOf("I-NP") != -1) {
                    CRFToken tk = new CRFToken();
                    tk.setWord(str.substring(0, str.indexOf("/")));
                    tk.setIdInSentence(id);
                    listTam.add(tk);
                }

                id = id + 1;

                if (str.indexOf("././O") != -1 || str.indexOf("!/./O") != -1 || str.indexOf("?/./O") != -1) {
                    if (listTam.size() == 0){}
                    else{
                        NounPhrase np = new NounPhrase();
                        np.setSentenceId(sentenceId);
                        np.setId(idNP);
                        np.setReviewId(reviewId);
                        if ((listTam.size() != 0) 
                        		&& (listTam.get(listTam.size() - 1).getWord().equals("-RRB-")
                        				|| listTam.get(listTam.size() - 1).getWord().equals("-LRB-")))
                        				listTam.remove(listTam.size() - 1);
                        for (CRFToken s : listTam) {
                            np.addCRFToken(s);
                        }
                        listNP.add(np);
                        idNP++;
                        listTam.clear();
                    }
                    id = 0;
                    sentenceId++;
                }
            }

            }  
        }
        //Close the input stream
        br.close();
        return listNP;
    }

}
