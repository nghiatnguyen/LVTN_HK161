/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasOffset;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private CollinsHeadFinder headFinder;
    public static List<NounPhrase> nounPhrases;
    public static List<Review> reviews;
    private StanfordCoreNLP pipeline;

    public StanfordUtil(File documentFile) {
        this.documentFile = documentFile;
    }

    //Just for finding Noun Phrases
    public void simpleInit() throws FileNotFoundException, IOException {
        headFinder = new CollinsHeadFinder();
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse");
        pipeline = new StanfordCoreNLP(props);

        reviews = new ArrayList<>();

        FileReader fileReader = new FileReader(documentFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String reviewLine;
        //read input file line by line and count the number sentences of each lines
        while ((reviewLine = bufferedReader.readLine()) != null) {
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
                // this is the parse tree of the current sentence
                Tree sentenceTree = sentence.get(TreeAnnotation.class);
                nounPhraseFindSimple(sentenceTree, newReview);
            }
            reviews.add(newReview);
        }
    }

    public void nounPhraseFindSimple(Tree rootNode, Review review) {
        if (rootNode == null || rootNode.isLeaf()) {
            return;
        }

        if (rootNode.value().equals("NP")) {
            List leaves = rootNode.getLeaves();
            CoreLabel firstLeafLabel = (CoreLabel) rootNode.getLeaves().get(0).label();
            HasOffset firstOfs = (HasOffset) firstLeafLabel;
            CoreLabel lastNodeLabel = (CoreLabel) rootNode.getLeaves().get(leaves.size() - 1).label();
            HasOffset lastOfs = (HasOffset) lastNodeLabel;
            NounPhrase np = new NounPhrase();
            np.setNpNode(rootNode);
            np.setHeadNode(rootNode.headTerminal(headFinder));
            np.setOffsetBegin(firstOfs.beginPosition());
            np.setOffsetEnd(lastOfs.endPosition());
            np.setId(review.getNounPhrases().size());
            review.addNounPhrase(np);
        }

        for (Tree child : rootNode.children()) {
            nounPhraseFindSimple(child, review);
        }
    }

    public void init() throws FileNotFoundException, IOException {

        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        headFinder = new CollinsHeadFinder();

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
                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    Token newToken = new Token();
                    // this is the text of the token
                    String word = token.get(TextAnnotation.class);

                    // this is the POS tag of the token
                    String pos = token.get(PartOfSpeechAnnotation.class);

                    int offsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
                    newToken.setOffsetBegin(offsetBegin);

                    int offsetEnd = token.get(CharacterOffsetEndAnnotation.class);
                    newToken.setOffsetEnd(offsetEnd);

                    newToken.setWord(word);
                    newToken.setPOS(pos);

                    newSentence.addToken(newToken);
                }

                // Check if this token is a comparative keyword. If so, its sentence is a comparative sentence
                List<Token> comparatives = FeatureExtractor.findComparativeIndicator(newSentence, null, null);
                if (!comparatives.isEmpty()) {
                    newSentence.setComparativeIndicatorPhrases(comparatives);
                }

                // this is the parse tree of the current sentence
                Tree sentenceTree = sentence.get(TreeAnnotation.class);
                nounPhraseFind(sentenceTree, newReview, newSentence, reviewId, sentenceId);

                //Check if there are superior or inferior nounphrases in sentence. If yes, assign them
                newSentence.initComparativeNPs();

                newReview.addSentence(newSentence);

                ++sentenceId;

            }
            reviews.add(newReview);
            ++reviewId;
        }
    }

    public void nounPhraseFind(Tree rootNode, Review review, Sentence sentence, int reviewId, int sentenceId) {
        if (rootNode == null || rootNode.isLeaf()) {
            return;
        }

        if (rootNode.value().equals("NP")) {
            List leaves = rootNode.getLeaves();
            CoreLabel firstLeafLabel = (CoreLabel) rootNode.getLeaves().get(0).label();
            HasOffset firstOfs = (HasOffset) firstLeafLabel;
            CoreLabel lastNodeLabel = (CoreLabel) rootNode.getLeaves().get(leaves.size() - 1).label();
            HasOffset lastOfs = (HasOffset) lastNodeLabel;
            NounPhrase np = new NounPhrase();
            np.setNpNode(rootNode);
            np.setHeadNode(rootNode.headTerminal(headFinder));
            np.setOffsetBegin(firstOfs.beginPosition());
            np.setOffsetEnd(lastOfs.endPosition());
            np.setReviewId(reviewId);
            np.setSentenceId(sentenceId);
            np.setOpinionWord();
            np.setId(review.getNounPhrases().size());
            nounPhrases.add(np);
            review.addNounPhrase(np);
            sentence.addNounPhrase(np);
        }

        for (Tree child : rootNode.children()) {
            nounPhraseFind(child, review, sentence, reviewId, sentenceId);
        }
    }

    public static void test(String outputFilePath) throws IOException {
        FileWriter fw = new FileWriter(new File(outputFilePath));
        BufferedWriter bw = new BufferedWriter(fw);
        for (Review review : reviews) {
            for (int i = 0; i < review.getNounPhrases().size(); ++i) {
                NounPhrase np1 = review.getNounPhrases().get(i);
                for (int j = i + 1; j < review.getNounPhrases().size(); ++j) {
                    NounPhrase np2 = review.getNounPhrases().get(j);
                    bw.write("-----------NP pair--------------");
                    bw.newLine();
                    bw.write(review.getRawContent());
                    bw.newLine();
                    bw.write("NP1 id: " + np1.getId());
                    bw.newLine();
                    bw.write("NP1 ref: " + np1.getRefId());
                    bw.newLine();
                    bw.write("NP1 type: " + np1.getType());
                    bw.newLine();
                    bw.write("NP1 words: " + np1.getNpNode().getLeaves());
                    bw.newLine();
                    bw.write("NP1 head label: " + np1.getHeadLabel());
                    bw.newLine();
                    bw.write("NP1 head: " + np1.getHeadNode());
                    bw.newLine();
                    bw.write("NP1 begin: " + np1.getOffsetBegin());
                    bw.newLine();
                    bw.write("NP1 review: " + np1.getReviewId());
                    bw.newLine();
                    bw.write("NP1 sentence: " + np1.getSentenceId());
                    bw.newLine();
                    bw.write("NP1 sentiment: " + StanfordUtil.reviews.get(np1.getReviewId()).getSentences().get(np1.getSentenceId()).getSentimentLevel());
                    bw.newLine();
//                System.out.print("Opinion words: ");
//
//                for (int k = 0; k < np1.getOpinionWords().size(); k++) {
//                    System.out.print(np1.getOpinionWords().get(k) + " ; ");
//                }
                    bw.newLine();
                    bw.write("------------");
                    bw.newLine();
                    bw.write("NP2 id: " + np2.getId());
                    bw.newLine();
                    bw.write("NP2 ref: " + np2.getRefId());
                    bw.newLine();
                    bw.write("NP2 type: " + np2.getType());
                    bw.newLine();
                    bw.write("NP2 words: " + np2.getNpNode().getLeaves());
                    bw.newLine();
                    bw.write("NP2 head label: " + np2.getHeadLabel());
                    bw.newLine();
                    bw.write("NP2 head: " + np2.getHeadNode());
                    bw.newLine();
                    bw.write("NP2 begin: " + np2.getOffsetBegin());
                    bw.newLine();
                    bw.write("NP2 review: " + np2.getReviewId());
                    bw.newLine();
                    bw.write("NP2 sentence: " + np2.getSentenceId());
                    bw.newLine();
                    bw.write("NP2 sentiment: " + StanfordUtil.reviews.get(np2.getReviewId()).getSentences().get(np2.getSentenceId()).getSentimentLevel());
                    bw.newLine();
                    bw.write("------------");
                    try {
                        bw.write("COREF: " + FeatureExtractor.isCoref(np1, np2));
                        bw.newLine();
                        bw.write("NP1 is Pronoun: " + FeatureExtractor.is_Pronoun(np1));
                        bw.newLine();
                        bw.write("NP2 is Pronoun: " + FeatureExtractor.is_Pronoun(np2));
                        bw.newLine();
                        bw.write("NP2 is Definite Noun Phrase: " + FeatureExtractor.is_Definite_NP(np2));
                        bw.newLine();
                        bw.write("NP2 is Demonstrative Noun Phrase: " + FeatureExtractor.is_Demonstrative_NP(np2));
                        bw.newLine();
                        bw.write("String similarity: " + FeatureExtractor.stringSimilarity(np1, np2, review.getSentences().get(np1.getSentenceId())));
                        bw.newLine();
                        bw.write("Distance Feature: " + FeatureExtractor.count_Distance(np1, np2));
                        bw.newLine();
                        bw.write("Number agreement: " + FeatureExtractor.numberAgreementExtract(np1, np2));
                        bw.newLine();
                        bw.write("comparative indicator-between: " + FeatureExtractor.comparativeIndicatorExtract(review, np1, np2));
                        bw.newLine();
                        bw.write("is-between: " + FeatureExtractor.isBetweenExtract(review, np1, np2));
                        bw.newLine();
                        bw.write("has-between: " + FeatureExtractor.has_Between_Extract(review, np1, np2));
                        bw.newLine();
                        bw.write("comparative indicator-between 2: " + FeatureExtractor.comparativeIndicator2Extract(review, np1, np2));
                        bw.newLine();
                        bw.write("is-between 2: " + FeatureExtractor.isBetween2Extract(review, np1, np2));
                        bw.newLine();
                        bw.write("has-between 2: " + FeatureExtractor.has_Between2_Extract(review, np1, np2));
                        bw.newLine();
                        bw.write("Sentiment Consistency: " + FeatureExtractor.sentimentConsistencyExtract(np1, np2));
                        bw.newLine();
//                    bw.write("***Entity and opinion words association*** ");
//                    bw.write("Probability of Opinion Word of NP1: " + FeatureExtractor.probability_opinion_word(np1));
//                    bw.write("Probability of NP2: " + FeatureExtractor.probability_noun_phrase(np2));
//                    bw.write("Probability of (NP2 and Opinion Word of NP1): " + FeatureExtractor.probability_NP_and_OW(np1, np2));

                    } catch (Exception e) {
                        bw.write(e.getMessage());
                        bw.newLine();
                        bw.write("Exception NP1 words: " + np1.getNpNode().getLeaves());
                        bw.newLine();
                        bw.write("Exception NP2 words: " + np2.getNpNode().getLeaves());
                        bw.newLine();
                    }

                    bw.write("------------End of NP pair--------------");
                    bw.newLine();
                }
            }
        }
        bw.close();
    }
}
