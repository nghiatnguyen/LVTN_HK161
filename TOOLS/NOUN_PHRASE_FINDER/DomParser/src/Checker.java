import java.util.ArrayList;
import java.util.Arrays;



public class Checker {
	
	private ArrayList<String> list_Pronoun = new ArrayList<String>(
		    Arrays.asList("i", "you", "he", "she", "it", "we", "you", "they",
		    		"myself", "yourself", "himself", "herself", "itself", 
		    		"ourselves", "yourselves", "themselves", "mine", "yours",
		    		"hers", "his", "ours", "yours", "theirs",
		    		"me", "you", "him", "her", "us", "them"));
	
	
	public String get_first_letter(NounPhrase np){
		String tam;
		if (np.get_text().indexOf(' ')== -1)
			tam = np.get_text();
		else
			tam = np.get_text().substring(0,np.get_text().indexOf(' '));
		return tam.toLowerCase();
	}
	
	public String get_main_noun(NounPhrase np){
		if (get_first_letter(np).equals("this")
				|| get_first_letter(np).equals("that")
				|| get_first_letter(np).equals("these")
				|| get_first_letter(np).equals("those")
				|| get_first_letter(np).equals("a")
				|| get_first_letter(np).equals("an")
				|| get_first_letter(np).equals("the")){
			if (np.get_text().substring(get_first_letter(np).length()).equals(""))
				return "";
			else
				return np.get_text().substring(get_first_letter(np).length()+1);
		}
			
		else
			return np.get_text();
	}
	
	public Boolean check_Pronoun(NounPhrase np){
		return list_Pronoun.contains(np.get_text().toLowerCase());
	}
	
	public Boolean check_Definite_NP(NounPhrase np){	
		return get_first_letter(np).equals("the");
	}
	
	//this, that, these, those
	public Boolean check_Demonstrative_NP(NounPhrase np){
		//System.out.println("1"+get_first_letter(np)+"1");
		return ((get_first_letter(np).equals("this")) 
				|| (get_first_letter(np).equals("that")) 
				|| (get_first_letter(np).equals("these")) 
				|| (get_first_letter(np).equals("those")));
	}
	
	public int check_Distance(NounPhrase np1, NounPhrase np2){
		return Math.abs(np1.get_sentence() - np2.get_sentence());
	}
}
