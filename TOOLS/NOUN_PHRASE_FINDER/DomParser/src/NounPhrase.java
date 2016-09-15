public class NounPhrase {
	private int id;
	//NOTE: text start with character " ", example: " Trang"
	private String text;
	private int sentence;
	
	void set_id(int id){
		this.id = id;
	}
	
	void set_text(String text){
		this.text = text;
	}
	
	void set_sentence(int sentence){
		this.sentence = sentence;
	}
	
	int get_id(){
		return this.id;
	}
	
	int get_sentence(){
		return this.sentence;
	}
	
	String get_text(){
		return this.text;
	}
}
