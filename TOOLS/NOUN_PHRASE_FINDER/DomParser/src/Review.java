/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anime
 */
public class Review {
    private String content;
    private List<Token> tokens = new ArrayList<Token>() ;
    private int review_offset_begin;
    private int review_offset_end;

    /**
     * @return the content
     */
    public String get_content() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void set_content(String content) {
        this.content = content;
    }

    /**
     * @return the tokens
     */
    public List<Token> get_tokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void add_token(Token token) {
        this.tokens.add(token);
    }

    /**
     * @return the reviewOffsetBegin
     */
    public int get_review_offset_begin() {
        return review_offset_begin;
    }

    /**
     * @param review_offset_begin the reviewOffsetBegin to set
     */
    public void set_review_offset_begin(int review_offset_begin) {
        this.review_offset_begin = review_offset_begin;
    }

    /**
     * @return the reviewOffsetEnd
     */
    public int get_review_offset_end() {
        return review_offset_end;
    }

    /**
     * @param review_offset_end the reviewOffsetEnd to set
     */
    public void set_review_offset_end(int review_offset_end) {
        this.review_offset_end = review_offset_end;
    }
}
