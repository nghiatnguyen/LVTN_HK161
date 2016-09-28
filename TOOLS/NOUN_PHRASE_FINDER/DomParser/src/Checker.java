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
	
	public boolean check_Proper_name(NounPhrase np){
		//if the first letter of word is lettercase -> false, else continure checking
		if (Character.isUpperCase(np.get_text().charAt(0))){
			//if the NP is single word
			if (np.get_text().indexOf(" ") == -1){
				//if the NP is in the Dictionary -> false, else -> True
				if (ReadXMLFile.sDict.contains(";" + np.get_text().toLowerCase() + ";")){
					return false;
				}
				else
					return true;
			}
			//if the NP is compound word
			else{
				// because "of" and "and" don't need to upper in Proper name, so check.
				String word = np.get_text();
				word.replace(" of ", " ");
				word.replace(" and ", " ");
				if (word.equals(upperCaseAllFirst(word)))
					return true;
				else
					return false;		
			}
		}
		else {
			return false;
		}
	}
	
	//Function upper all first letter of the word:
	//Input: one two
	//Output: One Two
	public static String upperCaseAllFirst(String value) {

		char[] array = value.toCharArray();
		// Uppercase first letter.
		array[0] = Character.toUpperCase(array[0]);

		// Uppercase all letters that follow a whitespace character.
		for (int i = 1; i < array.length; i++) {
		    if (Character.isWhitespace(array[i - 1])) {
			array[i] = Character.toUpperCase(array[i]);
		    }
		}

		// Result.
		return new String(array);
	    }
}
