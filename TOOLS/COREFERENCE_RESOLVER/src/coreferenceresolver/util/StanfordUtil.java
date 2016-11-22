/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.util;

import coreferenceresolver.process.FeatureExtractor;
import coreferenceresolver.process.MarkupMain;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.OpinionWord;
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
//    private CollinsHeadFinder headFinder;
    public static List<NounPhrase> nounPhrases;
    public static List<Review> reviews;
    public static StanfordCoreNLP pipeline;

    public StanfordUtil(File documentFile) {
        this.documentFile = documentFile;
    }

    //Just for finding Noun Phrases
    public void simpleInit() throws FileNotFoundException, IOException {
        String posFilePath = "./input.txt.pos";
        FileWriter fw = new FileWriter(new File(posFilePath));
        BufferedWriter bw = new BufferedWriter(fw);

        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse");
        pipeline = new StanfordCoreNLP(props);

        reviews = new ArrayList<>();

        FileReader fileReader = new FileReader(documentFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String reviewLine;
        int sentenceId = 0;
        //read input file line by line and count the number sentences of each lines
        while ((reviewLine = bufferedReader.readLine()) != null) {
            Review newReview = new Review();
            sentenceId = 0;

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
                newSentence.setRawContent(sentence.toString());
                newSentence.setOffsetBegin(sentenceOffsetBegin);
                newSentence.setOffsetEnd(sentenceOffsetEnd);
                List<Tree> sentenceTreeLeaves = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).getLeaves();
                int i = 0;
                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    Token newToken = new Token();
                    
                    Tree tokenTree = sentenceTreeLeaves.get(i);
                    // this is the text of the token
                    String word = token.get(TextAnnotation.class);

                    // this is the POS tag of the token
                    String pos = token.get(PartOfSpeechAnnotation.class);
                    
                    newToken.setTokenTree(tokenTree);

                    int offsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
                    newToken.setOffsetBegin(offsetBegin);

                    int offsetEnd = token.get(CharacterOffsetEndAnnotation.class);
                    newToken.setOffsetEnd(offsetEnd);

                    newToken.setWord(word);
                    newToken.setPOS(pos);

                    newSentence.addToken(newToken);
                    bw.write(token.word() + "/" + token.tag() + " ");
                    ++i;
                }
                bw.newLine();

                newReview.addSentence(newSentence);
                ++sentenceId;
            }
            //Separate paragraphs
            bw.write("./.");
            bw.newLine();
            reviews.add(newReview);
        }
        bw.close();
    }

    public void init() throws FileNotFoundException, IOException {

        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        nounPhrases = new ArrayList<>();

        reviews = new ArrayList<>();

        FileReader fileReader = new FileReader(documentFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String reviewLine;
        int reviewId = 0;
        int sentenceId = 0;
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
                int sentimentLevel = RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
                Sentence newSentence = new Sentence();
                newSentence.setReviewId(reviewId);
                newSentence.setRawContent(sentence.toString());
                newSentence.setOffsetBegin(sentenceOffsetBegin);
                newSentence.setOffsetEnd(sentenceOffsetEnd);
                newSentence.setSentimentLevel(sentimentLevel);

                //Dependency Parsing
                SemanticGraph collCCDeps = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
                Collection<TypedDependency> typedDeps = collCCDeps.typedDependencies();
                newSentence.setDependencies(typedDeps);
                
                List<Tree> sentenceTreeLeaves = sentence.get(TreeCoreAnnotations.TreeAnnotation.class).getLeaves();
                int i = 0;

                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    Token newToken = new Token();
                    // this is the text of the token
                    String word = token.get(TextAnnotation.class);
                    
                    Tree tokenTree = sentenceTreeLeaves.get(i);
                    
                    newToken.setTokenTree(tokenTree);

                    //this is the opinion orientation of the token
                    if (FeatureExtractor.POSITIVE_WORDS.contains(";" + word.toLowerCase() + ";")) {
                        newToken.setOpinionOrientation(Token.POSITIVE);
                    } else if (FeatureExtractor.NEGATIVE_WORDS.contains(";" + word.toLowerCase() + ";")) {
                        newToken.setOpinionOrientation(Token.NEGATIVE);
                    }

                    // this is the POS tag of the token
                    String pos = token.get(PartOfSpeechAnnotation.class);

                    int offsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
                    newToken.setOffsetBegin(offsetBegin);

                    int offsetEnd = token.get(CharacterOffsetEndAnnotation.class);
                    newToken.setOffsetEnd(offsetEnd);

                    newToken.setWord(word);
                    newToken.setPOS(pos);

                    newSentence.addToken(newToken);

                    int newTokenSentiment = Util.retrieveOpinion(newToken);

                    if ((newTokenSentiment == Util.POSITIVE
                            || newTokenSentiment == Util.NEGATIVE) && (!pos.equals("IN"))) {
                        OpinionWord newOW = new OpinionWord();
                        newOW.setOffsetBegin(offsetBegin);
                        newOW.setOffsetEnd(offsetEnd);
                        newOW.setWord(word);
                        for (TypedDependency typedDependency : newSentence.getDependencies()) {
                            if (typedDependency.reln().toString().equals("neg") && typedDependency.gov().value().equals(word)) {
                                newTokenSentiment = Util.reverseSentiment(newTokenSentiment);
                                break;
                            }
                        }

                        newOW.setSentimentOrientation(newTokenSentiment);
                        newSentence.addOpinionWord(newOW);
                    }
                    ++i;
                }

                // Check if this token is a comparative keyword. If so, its sentence is a comparative sentence
                List<Token> comparatives = FeatureExtractor.findComparativeIndicator(newSentence, null, null);
                if (!comparatives.isEmpty()) {
                    newSentence.setComparativeIndicatorPhrases(comparatives);
                }

                //Check if there are superior or inferior nounphrases in sentence. If yes, assign them
                newSentence.initComparativeNPs();
                
                newSentence.setOpinionForNPs();

                newReview.addSentence(newSentence);

                ++sentenceId;

            }
            reviews.add(newReview);
            ++reviewId;
        }
    }

    public static void test(String outputFilePath) throws IOException {
        MarkupMain.set_sDataset("no.no");
        StanfordUtil su = new StanfordUtil(new File("E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input.txt"));
        su.init();
        FileWriter fw = new FileWriter(new File(outputFilePath));
        BufferedWriter bw = new BufferedWriter(fw);

        for (Review review : reviews) {
            Util.discardUnneccessaryNPs(review);
        }

        for (Review review : reviews) {
            bw.write("----New review: ");
            bw.newLine();
            int sentenceId = 0;
            for (Sentence sentence : review.getSentences()) {
                bw.write(sentence.getRawContent());
                bw.newLine();
                bw.write("Dependencies:");
                for (TypedDependency typedDependency : sentence.getDependencies()) {
                    bw.write(typedDependency.reln() + " ");
                    bw.write(typedDependency.gov().value() + " ");
                    bw.write(typedDependency.dep().value() + " ");
                    bw.newLine();
                }
                bw.newLine();
                bw.write("--Opinion Words in this sentence:");
                bw.newLine();
                for (OpinionWord ow : sentence.getOpinionWords()) {
                    bw.write(ow.getWord() + "\t" + ow.getOffsetBegin() + "\t" + ow.getSentimentOrientation());
                    bw.newLine();
                }
                bw.newLine();
                for (NounPhrase np : review.getNounPhrases()) {
                    if (np.getSentenceId() == sentenceId) {
                        bw.write(np.getNpNode().getLeaves().toString());
                        bw.newLine();
                        bw.write("Sentiment: " + np.getSentimentOrientation());
                        bw.newLine();
                        bw.newLine();
                    }
                }
                ++sentenceId;
            }
        }
        bw.close();
    }
}
