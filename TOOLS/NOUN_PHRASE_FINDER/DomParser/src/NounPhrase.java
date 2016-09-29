public class NounPhrase {
	private int id;
	private String text;
	private int sentence;
	private int review;
	private int position;
	private int old_position;
	
	void set_id(int id){
		this.id = id;
	}
	
	void set_text(String text){
		this.text = text;
	}
	
	void set_sentence(int sentence){
		this.sentence = sentence;
	}
	
	void set_review(int review){
		this.review = review;
	}
	
	void set_position(int position){
		this.position = position;
	}
	
	void set_old_position(int old_position){
		this.old_position = old_position;
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
	
	int get_review(){
		return this.review;
	}
	
	int get_position(){
		return this.position;
	}
	
	int get_old_position(){
		return this.old_position;
	}
}
