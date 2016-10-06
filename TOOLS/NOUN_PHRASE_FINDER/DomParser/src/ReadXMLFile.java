
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
    //List dataset of all reviews
    private static String sDataset = null;
    private static BufferedReader buffReaderDict;
    private static BufferedReader bufferedReader;

    public static String get_sDataset(){
		return sDataset;
	}
    
    public static void main(String argv[]) {
        long startTime = System.currentTimeMillis();
        // List of NounPhares
        ArrayList<NounPhrase> list_NPs = new ArrayList<NounPhrase>();
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
        // Regex for get the number of sentences in each reviews.
        String pattern_sen = "[.!?]+";
        Pattern r2 = Pattern.compile(pattern_sen);
        String parse;
        ArrayList<String> new_list = new ArrayList<String>();

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
            ArrayList<String> list_reviews = new ArrayList<String>();
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
                        np.set_text(tam.substring(1));
                        list_NPs.add(np);
                        start = m.start() + 1;
                    }
                }
//			System.out.println("-----------------------");
            }
//            annotation(list_reviews, list_NPs);
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
    		Checker check1 = new Checker();
    		check1.count_word("case");
    		check1.count_NP_and_OW("i", "HEadset");

            System.out.println("Done!");
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Time: " + totalTime);

//            write_TXT(list_NPs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void write_TXT(ArrayList<NounPhrase> listNPs) throws FileNotFoundException, UnsupportedEncodingException {
        Checker check = new Checker();
        PrintWriter writer = new PrintWriter("NounPhrases.txt", "UTF-8");
        writer.println("PRONOUN\tDEF_NP\tDEM_NP\tProper_name\tReview\tSentence\tPosition\tID\tNoun Phrase\tMain Noun");
        for (NounPhrase np : listNPs) {
            writer.println(check.check_Pronoun(np) + "\t" + check.check_Definite_NP(np) + "\t"
                    + check.check_Demonstrative_NP(np) + "\t\t" + check.check_Proper_name(np)
                    + "\t\t" + np.get_review() + "\t\t" + np.get_sentence()
                    + "\t\t\t" + np.get_position() + "\t\t" + np.get_old_position() + "\t" + np.get_id() + "\t"
                    + "[" + np.get_text() + "]" + "\t[" + check.get_main_noun(np) + "]");
        }
        writer.close();
    }

    static void annotation(ArrayList<String> listreviews, ArrayList<NounPhrase> listNPs) throws FileNotFoundException, UnsupportedEncodingException {
        int order = 0;
        int position = 0;
        int count_sentence = 0;
        int count_review = 1;
        String tam = listreviews.get(0);
        for (NounPhrase np : listNPs) {
//			System.out.println(np.get_id());
            if (tam.substring(position).indexOf(np.get_text()) == -1) {
                count_sentence = np.get_sentence() - 1;
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
            np.set_sentence(np.get_sentence() - count_sentence);
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
}
