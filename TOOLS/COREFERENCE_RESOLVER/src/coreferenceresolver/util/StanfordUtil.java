/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.util;

import coreferenceresolver.process.FeatureExtractor;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Token;
import coreferenceresolver.element.Review;
import coreferenceresolver.element.Sentence;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author TRONGNGHIA
 */
public class StanfordUtil {

    private File documentFile;
    private Properties props;
    private Annotation document;
    public static List<NounPhrase> nounPhrases;
    public static List<Review> reviews;
    public static StanfordCoreNLP pipeline;

    public StanfordUtil(File documentFile) {
        this.documentFile = documentFile;
    }    

    public void init(boolean simpleInit) throws FileNotFoundException, IOException {
        String outPosFilePath = "./input.txt.pos";
        FileWriter fw = new FileWriter(new File(outPosFilePath));
        BufferedWriter bw = new BufferedWriter(fw);
        props = new Properties();
        if (simpleInit){
            props.put("annotators", "tokenize, ssplit, pos, parse");
        }
        else {
            props.put("annotators", "tokenize, ssplit, pos, parse, sentiment");
        }
        pipeline = new StanfordCoreNLP(props);        

        reviews = new ArrayList<>();

        FileReader fileReader = new FileReader(documentFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String reviewLine;
        int reviewId = 0;
        int sentenceId;
        //read input file line by line and count the number sentences of each lines
        while ((reviewLine = bufferedReader.readLine()) != null) {
            sentenceId = 0;
            Review newReview = new Review();

            //Add to reviews list
            newReview.setRawContent(reviewLine);

            // create an empty Annotation just with the given text
            document = new Annotation(reviewLine);

            // run all Annotators on this text
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);

            //Begin extracting from paragraphs
            for (CoreMap sentence : sentences) {
                int sentenceOffsetBegin = sentence.get(CharacterOffsetBeginAnnotation.class);
                int sentenceOffsetEnd = sentence.get(CharacterOffsetEndAnnotation.class);
                Sentence newSentence = new Sentence();
                newSentence.setReviewId(reviewId);
                newSentence.setRawContent(sentence.toString());
                newSentence.setOffsetBegin(sentenceOffsetBegin);
                newSentence.setOffsetEnd(sentenceOffsetEnd);
                
                if (!simpleInit){
                    int sentimentLevel = RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
                    newSentence.setSentimentLevel(sentimentLevel);
                    
                    //Dependency Parsing
                    SemanticGraph collCCDeps = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
                    Collection<TypedDependency> typedDeps = collCCDeps.typedDependencies();
                    newSentence.setDependencies(typedDeps);
                }                                                                

                List<Tree> sentenceTreeLeaves = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).getLeaves();
                
                int i = 0;
                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    Token newToken = new Token();                                                                     
                    
                    Tree tokenTree = sentenceTreeLeaves.get(i);
                    newToken.setTokenTree(tokenTree);

                    String word = token.get(TextAnnotation.class);
                    newToken.setWord(word);
                    
                    String pos = token.get(PartOfSpeechAnnotation.class);
                    newToken.setPOS(pos);

                    int offsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
                    newToken.setOffsetBegin(offsetBegin);

                    int offsetEnd = token.get(CharacterOffsetEndAnnotation.class);
                    newToken.setOffsetEnd(offsetEnd);  
                                        
                    if(!simpleInit){                                                
                         //Calculate sentiment for this token
                        int newTokenSentiment = Util.retrieveOriginalSentiment(newToken.getWord());
                        newToken.setSentimentOrientation(newTokenSentiment, newSentence.getDependencies());
                    } 

                    newSentence.addToken(newToken);
                    bw.write(token.word() + "/" + token.tag() + " ");
                    ++i;
                }
                bw.newLine();

                if (!simpleInit){
                    //Check if this sentence contains a comparative indicator. 
                    //If yes, it is a comparative sentence. Identify which NP is superior or inferior in this sentence
                    List<Token> comparativeTokens = FeatureExtractor.findComparativeIndicator(newSentence, null, null);
                    //TODO
                    //Check special comparative samples
                    if (!comparativeTokens.isEmpty()) {
                        newSentence.initComparatives(comparativeTokens);
                    }                                      
                }
                
                newReview.addSentence(newSentence);
                
                ++sentenceId;
            }

            bw.write("./.");
            bw.newLine();

            reviews.add(newReview);
            ++reviewId;
        }
        bw.close();
    }
}
