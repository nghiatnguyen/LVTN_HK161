package coreferenceresolver.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import coreferenceresolver.element.CRFToken;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Token;

public class ReadCrfChunkerUtil {
	public static ArrayList<NounPhrase> readCrfChunker() throws FileNotFoundException, IOException{
		String inputDir = "C:\\Users\\Dang Trang\\Downloads\\CRFChunker-1.0\\CRFChunker\\samples\\";
		String inputFile = "input.txt.pos.chk";
		ArrayList<NounPhrase> listNP = new ArrayList<NounPhrase>();
		
		// Open the file
		FileInputStream fstream = new FileInputStream(inputDir + inputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		int sentenceId = 0;
		int reviewId = 0;
		int id = 0;
		int idNP = 0;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
			ArrayList<CRFToken> listTam = new ArrayList<CRFToken>();
			while (strLine.indexOf(" ") != -1){
				String str = strLine.substring(0,strLine.indexOf(" "));
				strLine = strLine.substring(strLine.indexOf(" ") + 1);
				if (str.indexOf("B-NP") != -1){	
					if (listTam.size() == 0){
						CRFToken tk = new CRFToken();
						tk.setWord(str.substring(0,str.indexOf("/")));
						tk.setIdInSentence(id);
						listTam.add(tk);
					}
					else{
						NounPhrase np = new NounPhrase();
						np.setSentenceId(sentenceId);
						np.setId(idNP);
						np.setReviewId(reviewId);
						for (CRFToken s : listTam){
							np.addToken(s);
						}
						listNP.add(np);
						idNP ++;
						listTam.clear();
						CRFToken tk = new CRFToken();
						tk.setWord(str.substring(0,str.indexOf("/")));
						tk.setIdInSentence(id);
						listTam.add(tk);
					}
				}
				else if (str.indexOf("I-NP") != -1){
					CRFToken tk = new CRFToken();
					
					tk.setWord(str.substring(0,str.indexOf("/")));
					tk.setIdInSentence(id);
					listTam.add(tk);
				}
				
				id = id + 1;
				
				if (str.indexOf("././O") != -1){
					NounPhrase np = new NounPhrase();
					np.setSentenceId(sentenceId);
					np.setId(idNP);
					np.setReviewId(reviewId);
					for (CRFToken s : listTam){
						np.addToken(s);
					}
					listNP.add(np);
					idNP ++;
					id = 0;
					sentenceId ++;
				}
				}
			if (strLine.equals("././O"))
				reviewId ++;
		}
		//Close the input stream
		br.close();
		return listNP;
	}
	
	
}
