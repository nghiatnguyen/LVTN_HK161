/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

/**
 *
 * @author TRONGNGHIA
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String text = "I have brought my laptop back. It is a cheap laptop that I bought 4 years ago. There are so many problems with it now.\n" +
"But if it still works, I will use it for a time to save some cost.\n" +
"We are still slow. Perhaps no one knows what will happen to us. But I think we will lose!";
        StanfordUtil su = new StanfordUtil(text);
        su.init();
    }
    
}
