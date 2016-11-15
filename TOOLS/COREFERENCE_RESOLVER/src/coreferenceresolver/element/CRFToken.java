package coreferenceresolver.element;

public class CRFToken {
	private String word;
    private int idInSentence;
    
    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }
    
    
    public void setIdInSentence(int id){
    	this.idInSentence = id;
    }
    
    public int getIdInSentence(){
    	return this.idInSentence;
    }
}
