import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadXMLFile {
    
	public static void main(String argv[]) {
		
		
		// List of NounPhares
		ArrayList<NounPhrase> list_NPs = new ArrayList<NounPhrase>();
		//Regex for getting NP in each parser
	    String pattern = " [(]NP(([(]([^()]|([(]([^()]|([(]([^()])*[)])|\\s)*[)])|\\s)*[)])|\\s)*[)]";
	    //String pattern = "[(]NP(\\s|[(])*";
	    // Create a Pattern object
	    Pattern r = Pattern.compile(pattern);
	  //Regex for getting text of NP
		String pattern1 = "([(])([A-Z]|\\$)+(\\s([^()])*)([)])";
		Pattern r1 = Pattern.compile(pattern1);
		String parse;
		int count_id = 0;
		int count_sentence = 0;
		
		/*-----------------------------------------------*/
		/*Read XMLFile and output list parsers*/
	    try {

		File fXmlFile = new File("input.txt.out");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		String tam;

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("sentence");
		System.out.println(nList.getLength());


		System.out.println("----------------------------");
	
		
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			//String t = "(ROOT(S(NP (DT This))(VP (VBZ is)(NP (DT a)(ADJP (JJ fantastic))(NN case)))(. .))";
			count_sentence++;
			
			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				parse = eElement.getElementsByTagName("parse").item(0).getTextContent();
				System.out.println("Sentence id : " + eElement.getAttribute("id"));
				System.out.println("Parser : " + parse );
				Matcher m = r.matcher(parse);
				int start = 0;
				Matcher m1;
				while(m.find(start)) {
					count_id++;
					tam = "";
					System.out.println("Found value:  " + m.group(0) );
			        // System.out.println("Found value: " + m.group(1) );
			        // System.out.println("Found value: " + m.group(2) );
					
		
					m1 = r1.matcher(m.group(0));
					while(m1.find()) {
						System.out.println("Detack: " + m1.group(3) );
				        // System.out.println("Found value: " + m.group(1) );
				        // System.out.println("Found value: " + m.group(2) );
						tam = tam + m1.group(3);
						
					}
					NounPhrase np = new NounPhrase();
					np.set_id(count_id);
					np.set_sentence(count_sentence);
					np.set_text(tam);
					list_NPs.add(np);
					start = m.start() + 1;
				}
			}
			System.out.println("-----------------------");
		}
		
		write_TXT(list_NPs);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    
	  }
	
	static void write_TXT(ArrayList<NounPhrase> listNPs) throws FileNotFoundException, UnsupportedEncodingException{
		Checker check = new Checker();
		PrintWriter writer = new PrintWriter("NounPhrases.txt", "UTF-8");
		writer.println("PRONOUN\tDEF_NP\tDEM_NP\tSentence\tID\tNoun Phrase");
		for (NounPhrase np : listNPs){
			writer.println(check.check_Pronoun(np)+ "\t" + check.check_Definite_NP(np)+ "\t" + 
					check.check_Demonstrative_NP(np)+ "\t\t" + np.get_sentence() + "\t\t" + np.get_id() + "\t" 
					+ np.get_text() );
		}
		writer.close();
	}

}
