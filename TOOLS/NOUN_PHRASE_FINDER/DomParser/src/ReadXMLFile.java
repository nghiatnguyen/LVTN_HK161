
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadXMLFile {

    static ArrayList<String> new_list_review = new ArrayList<String>();
  //List of reviews
    static ArrayList<String> list_reviews = new ArrayList<String>();
    // List of NounPhares
    static ArrayList<NounPhrase> list_NPs = new ArrayList<NounPhrase>();
    // List of Opinion words
    static ArrayList<Opinion_Word> list_OWs = new ArrayList<Opinion_Word>();
    private static String sDataset = null;
    private static BufferedReader buffReaderDict;
    private static BufferedReader bufferedReader;

    public static String get_sDataset(){
		return sDataset;
	}
    
    public static void main(String argv[]) {
        long startTime = System.currentTimeMillis();
        
        
        //Added by Nghia
        ArrayList<Token> list_Tokens = new ArrayList<Token>();
        //End of Adding
        //Regex for getting NP in each parser
        String pattern = "[(]NP(([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]|([(]([^()]*)[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)]";
        //String pattern = "[(]NP(\\s|[(])*";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        //Regex for getting text of NP
        String pattern1 = "([(])([^()\\.]|\\$)+(\\s([^()])*)([)])";
        Pattern r1 = Pattern.compile(pattern1);
        //Regex for getting Opinion_Word;
        String pattern2 = "([(]JJ )([A-Za-z]+)[)]";
        Pattern r2 = Pattern.compile(pattern2);
        // Regex for get the number of sentences in each reviews.
//        String pattern_sen = "[.!?]+";
//        Pattern r2 = Pattern.compile(pattern_sen);
//        ArrayList<String> new_list = new ArrayList<String>();
        String parse;

        int count_id = 0;
        int count_sentence = 0;

        /*-----------------------------------------------*/
 /*Read XMLFile and output list parsers*/
        try {

            File fXmlFile = new File("input.txt.out");
            File fInput = new File("input.txt");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            FileReader fileReader = new FileReader(fInput);
            bufferedReader = new BufferedReader(fileReader);
            

            String line;
            //read input file line by line and count the number sentences of each lines
            while ((line = bufferedReader.readLine()) != null) {
                list_reviews.add(line);
                // System.out.println(line);
            }

            //read dictionary
//		File fDict = new File("dict.txt");
//		FileReader fReaderDict = new FileReader(fDict);
//		buffReaderDict = new BufferedReader(fReaderDict);
//		while ((line = buffReaderDict.readLine()) != null){
//			sDict = sDict + line + "\n";
//		}
           // read the Dataset
    		File fData = new File("dataset.txt");
    		FileReader fReaderData = new FileReader(fData);
    		buffReaderDict = new BufferedReader(fReaderData);
    		while ((line = buffReaderDict.readLine()) != null){
    			sDataset = sDataset + line + "\n";
    		}
            String tam;

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("sentence");

            System.out.print("The number of sentences: (input.out.txt)");
            System.out.println(nList.getLength());

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                //String t = "(ROOT(S(NP (DT This))(VP (VBZ is)(NP (DT a)(ADJP (JJ fantastic))(NN case)))(. .))";
                count_sentence++;

                // System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    parse = eElement.getElementsByTagName("parse").item(0).getTextContent();
//				System.out.println("Sentence id : " + eElement.getAttribute("id"));
//				System.out.println("Parser : " + parse );

                    // Added by Nghia
                    // Get all tokens and save them in a list
                    NodeList tokenNodes = eElement.getElementsByTagName("token");
                    for (int count = 0; count < tokenNodes.getLength(); ++count) {
                        Token token = new Token();

                        NodeList tokenChildNodes = tokenNodes.item(count).getChildNodes();
                        token.set_word(tokenChildNodes.item(1).getTextContent());
                        token.set_character_offset_begin(Integer.valueOf(tokenChildNodes.item(5).getTextContent()));
                        token.set_character_offset_end(Integer.valueOf(tokenChildNodes.item(7).getTextContent()));
                        token.set_POS(tokenChildNodes.item(9).getTextContent());
                        list_Tokens.add(token);
                    }
                    //End of getting all tokens
                    //End adding

                    Matcher m = r.matcher(parse);
                    int start = 0;
                    Matcher m1;
                    //Regex for getting Opinion words
                    Matcher m2 = r2.matcher(parse);
                    int id_ow = 0;
                    while (m2.find()){
                    	 id_ow++;
                    	 if (Checker.check_adj_in_list(m2.group(2))){
//	                    	 System.out.println("Found value:" + m2.group(2) );
	                    	 Opinion_Word ow = new Opinion_Word();
	                    	 ow.set_id(id_ow);
	                    	 ow.set_index(count_sentence);
	                    	 ow.set_text(m2.group(2));
	                    	 list_OWs.add(ow);
                    	 }
                    }
                    while (m.find(start)) {
                        count_id++;
                        tam = "";
                        // System.out.println("Found value:  " + m.group(0) );
                        // System.out.println("Found value: " + m.group(1) );
                        // System.out.println("Found value: " + m.group(2) );

                        m1 = r1.matcher(m.group(0));
                        while (m1.find()) {
//						System.out.println("Detack: 1" + m1.group(3) );
                            // System.out.println("Found value: " + m.group(1) );
                            // System.out.println("Found value: " + m.group(2) );
                            if ((m1.group(3).indexOf(" ,") == 0)
                                    || (m1.group(3).indexOf(" '") == 0)
                                    || (m1.group(3).indexOf(" %") == 0)
                                    || (m1.group(3).indexOf(" &") == 0)) {
                                tam = tam + m1.group(3).substring(1);
                            } else if ((m1.group(3)).equals(" -LRB-")) {
                                tam = tam + " (";
                            } else if (((m1.group(3)).equals(" -RRB-"))) {
                                tam = tam + ")";
                            } else if (tam.length() > 0 && ((tam.substring(tam.length() - 1).equals("("))
                                    || (tam.substring(tam.length() - 1).equals("$"))
                                    || (tam.substring(tam.length() - 1).equals("&")))) {
                                tam = tam + m1.group(3).substring(1);
                            } else {
                                tam = tam + m1.group(3);
                                // System.out.print("dddd" + tam);
                            }

                        }
                        NounPhrase np = new NounPhrase();
                        np.set_id(count_id);
                        np.set_sentence(count_sentence);
                        np.set_index(count_sentence);
                        np.set_text(tam.substring(1));
                        list_NPs.add(np);
                        start = m.start() + 1;
                    }
                }
//			System.out.println("-----------------------");
            }
            annotation(list_reviews, list_NPs);
            //Added by Nghia
//            FeatureExtractor.initial_reviews_and_tokens(list_reviews, list_Tokens);
            //End of adding

            //check has-between feature
//            NounPhrase np1 = list_NPs.get(142);
//            NounPhrase np2 = list_NPs.get(143);
//		System.out.println("Has-between: " +  np1.get_text() + " --and-- " + np2.get_text());
//		System.out.println(create_has_between_feature(np1, np2));
            //Added by Nghia
            //Check is-between feature
//            System.out.println("Is-between: " + np1.get_text() + " --and-- " + np2.get_text());
//            System.out.println(FeatureExtractor.create_is_between_feature(list_reviews, np1, np2));
            //Check comparative indicator feature
//            System.out.println("Comparative indicator: " + np1.get_text() + " --and-- " + np2.get_text());
//            System.out.println(FeatureExtractor.create_comparative_indicator_feature(list_reviews, np1, np2));
            //End of Adding
            
          //check function checker.count_word
//    		Checker check1 = new Checker();
//    		check1.count_word("case");
//    		check1.count_NP_and_OW("i", "HEadset");
            
            //test review and position of opinion words
            set_np_liking_ow();

            System.out.println("Done!");
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Time: " + totalTime);

            write_TXT(list_NPs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void write_TXT(ArrayList<NounPhrase> listNPs) throws FileNotFoundException, UnsupportedEncodingException {
        Checker check = new Checker();
        PrintWriter writer = new PrintWriter("NounPhrases.txt", "UTF-8");
        writer.println("PRONOUN\tDEF_NP\tDEM_NP\tProper_name\tReview\tSentence\tOpinion\tPosition\tID\tNoun Phrase\tMain Noun");
        for (NounPhrase np : listNPs) {
            writer.println(check.check_Pronoun(np) + "\t" + check.check_Definite_NP(np) + "\t"
                    + check.check_Demonstrative_NP(np) + "\t\t" + check.check_Proper_name(np)
                    + "\t\t" + np.get_review() + "\t\t" + np.get_sentence()
                    + "\t\t" + np.get_opinion_word()
                    + "\t\t\t" + np.get_position() + "\t\t" + np.get_old_position() + "\t" + np.get_id() + "\t"
                    + "[" + np.get_text() + "]" + "\t[" + check.get_main_noun(np) + "]");
        }
        writer.close();
    }

    static void annotation(ArrayList<String> listreviews, ArrayList<NounPhrase> listNPs) throws FileNotFoundException, UnsupportedEncodingException {
        int order = 0;
        int position = 0;
        int count_sen = 0;
        int count_review = 1;
        String tam = listreviews.get(0);
        for (NounPhrase np : listNPs) {
//			System.out.println(np.get_id());
            if (tam.substring(position).indexOf(np.get_text()) == -1) {
                count_sen = np.get_sentence() - 1;
//				System.out.println("------"+ order +"-------");
//				System.out.println(tam);
                new_list_review.add(tam);
                order++;
                count_review++;
                position = 0;
                tam = listreviews.get(order);
            }

            position = position + tam.substring(position).indexOf(np.get_text());
            tam = tam.substring(0, position) + "<" + np.get_text() + ">"
                    + tam.substring(position + np.get_text().length());
            np.set_sentence(np.get_sentence() - count_sen);
            np.set_review(count_review);
            np.set_position(position);
            np.set_old_position(position - count_times(tam.substring(0, position), '<') - count_times(tam.substring(0, position), '>'));
            if (np.get_text().equals("it") || np.get_text().equals("I")
                    || np.get_text().equals("It")) {
                position = position + 2;
            }
        }
        new_list_review.add(tam);

        PrintWriter writer1 = new PrintWriter("raw_annotate_reviews.txt", "UTF-8");
        for (String s : new_list_review) {
            writer1.println(s);
        }
        writer1.close();

    }

    static boolean create_has_between_feature(NounPhrase np1, NounPhrase np2) {
        if (np1.get_review() == np2.get_review()) {
            if (np1.get_sentence() == np2.get_sentence()) {
                System.out.println(new_list_review.get(np1.get_review() - 1));
                if (np1.get_position() < np2.get_position()) {
                    if (new_list_review.get(np1.get_review() - 1).substring(np1.get_position(), np2.get_position()).contains(" has ")) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (new_list_review.get(np1.get_review() - 1).substring(np2.get_position(), np1.get_position()).contains(" has ")) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    static int count_times(String s, char t) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == t) {
                counter++;
            }
        }
        return counter;
    }
    
    //find a list of candidate nps liking with a opinion word
    static ArrayList<NounPhrase> list_candidate(Opinion_Word f_ow){
    	ArrayList<NounPhrase> f_list = new ArrayList<NounPhrase>();
    	for (NounPhrase np: list_NPs){
    		if (np.get_index() == f_ow.get_index()){
    			f_ow.set_review(np.get_review());
    			f_list.add(np);
    		}
    	}
    	return f_list;
    }
    
    //find a review and position of a opinion word, besides find a list of candidate nps liking with a opinion word
    static ArrayList<NounPhrase> set_review_of_ow(Opinion_Word f_ow){
//    	System.out.println(f_ow);
    	ArrayList<NounPhrase> f_list = new ArrayList<NounPhrase>();
    	for (NounPhrase np: list_NPs){
    		if (np.get_index() == f_ow.get_index()){
    			f_ow.set_review(np.get_review());
    			f_list.add(np);
    		}
    	}
    	Matcher m = Pattern.compile("(\\b" + f_ow.get_text() + "\\b)").matcher(list_reviews.get(f_ow.get_review()-1));
//    	System.out.println(f_ow.get_review());
    	while(m.find())
		    f_ow.set_position(m.start());
//    	System.out.println(list_reviews.get(f_ow.get_review()-1).indexOf((f_ow.get_text())));
//    	System.out.println(f_ow.get_position());
    	return f_list;
    }

    
    //find NP associating with OW, and set a review and position for each opinion word
    static void set_np_liking_ow(){
    	 for (Opinion_Word f_ow : list_OWs){
         	 ArrayList<NounPhrase> f_listnps = set_review_of_ow(f_ow);
         	 //if List of candidate NPs is empty
         	 if (f_listnps.isEmpty()){
         	 }
         	 else {
         		 //If all candidate NPs are after OW, get the nearest NP after OW 
         		if (f_listnps.get(0).get_old_position() > f_ow.get_position()){
    				 f_listnps.get(0).set_opinion_word(f_ow.get_text());
    				 f_ow.set_id_np(f_listnps.get(0).get_id());
    			 }
         		else {
         			//If OW is nested in NP, OW associates with that NP
         			boolean f_check = false;
//         			for (NounPhrase f_np : f_listnps){
//	          			 if (f_np.get_text().indexOf(f_ow.get_text()) != -1){
//	          				 f_np.set_opinion_word(f_ow.get_text());
//	          				 f_ow.set_id_np(f_np.get_id());
//	          				 f_check = true;
//	          				 f_np.
//	          			 }
//         			}
         			int f_tam = 0;
         			for (int i = 0; i< f_listnps.size(); i++){
         				if (f_listnps.get(i).get_text().indexOf(f_ow.get_text()) != -1){
         					f_tam = i;
         					f_check = true;	
         				}
         			}
         			if (f_check == true){
         				f_ow.set_id_np(f_listnps.get(f_tam).get_id());
        				f_listnps.get(f_tam).set_opinion_word(f_ow.get_text());
         			}
         			else {
         				//If the sentence is a exclamatory sentence 
             			if ((list_reviews.get(f_ow.get_review()-1).indexOf("how " + f_ow.get_text()) != -1)
            					 || (list_reviews.get(f_ow.get_review()-1).indexOf("How " + f_ow.get_text()) != -1)){
             				for (NounPhrase f_np : f_listnps){
             					//If OW is nested in NP, OW associates with that NP
    	               			 if (f_np.get_text().indexOf(f_ow.get_text()) != -1){
    	               				 f_np.set_opinion_word(f_ow.get_text());
    	               				 f_ow.set_id_np(f_np.get_id());
    	               				 break;
    	               			 }
    	         				 if (f_np.get_old_position() > f_ow.get_position()){
    	        					 f_np.set_opinion_word(f_ow.get_text());
    	            				 f_ow.set_id_np(f_np.get_id());
    	            				 break;
    	        				 }
             				}
            			 } else {
            				 	int f_sub = -1000;
            				 	int f_id = 0;
    	        				for (int i = 0; i < f_listnps.size(); i++){
    	        					int f_sub_tam = f_listnps.get(i).get_old_position() + f_listnps.get(i).get_text().length() - f_ow.get_position();
    	        					if ((f_sub_tam < 0) && (f_sub_tam) > f_sub){
    	        						 f_sub = f_sub_tam;
    	        						 f_id = i;
    	        					 }
    	        				}
    	        				if (f_id != 0){
	    	        				f_ow.set_id_np(f_listnps.get(f_id).get_id());
	    	        				f_listnps.get(f_id).set_opinion_word(f_ow.get_text());
    	        				}
            			}
         			}		 
         		 }
         	 } 
         	 
         }
    }
}
