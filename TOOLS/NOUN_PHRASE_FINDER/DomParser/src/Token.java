/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Anime
 */
public class Token {
    private String word;
    private int character_offset_begin;
    private int character_offset_end;
    private int position;
    private int review;
    private String POS;

    /**
     * @return the word
     */
    public String get_word() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void set_word(String word) {
        this.word = word;
    }

    /**
     * @return the charactorOffsetBegin
     */
    public int get_character_offset_begin() {
        return character_offset_begin;
    }

    /**
     * @param chracter_offset_begin the character_offset_begin to set
     */
    public void set_character_offset_begin(int chracter_offset_begin) {
        this.character_offset_begin = chracter_offset_begin;
    }

    /**
     * @return the character_offset_end
     */
    public int get_character_offset_end() {
        return character_offset_end;
    }

    /**
     * @param character_offset_end the character_offset_end to set
     */
    public void set_character_offset_end(int character_offset_end) {
        this.character_offset_end = character_offset_end;
    }

    /**
     * @return the POS
     */
    public String get_POS() {
        return POS;
    }

    /**
     * @param POS the POS to set
     */
    public void set_POS(String POS) {
        this.POS = POS;
    }

    /**
     * @return the sentence
     */
    public int get_position() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void set_position(int position) {
        this.position = position;
    }

    /**
     * @return the review
     */
    public int get_review() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void set_review(int review) {
        this.review = review;
    }
}
