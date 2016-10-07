
public class Opinion_Word {
	private int id;
	private String text;
	private int review;
	//position in the old review
	private int position;
	private int id_np;
	//position of ow's sentence in all review
	private int index;
	
	public Opinion_Word(){
		id = 0;
		text = "";
		review = 0;
		position = 0;
		id_np = 0;
		index = 0;
	}
	
	void set_id(int id){
		this.id = id;
	}
	
	void set_text(String text){
		this.text = text;
	}
	
	void set_review(int review){
		this.review = review;
	}
	
	void set_position(int position){
		this.position = position;
	}
	
	void set_id_np(int id_np){
		this.id_np = id_np;
	}
	
	int get_id(){
		return this.id;
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
	
	int get_id_np(){
		return this.id_np;
	}
	
	void set_index(int index){
		this.index = index;
	}
	
	int get_index(){
		return this.index;
	}
}
