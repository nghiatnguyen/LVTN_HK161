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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        headFinder = new CollinsHeadFinder();

        nounPhrases = new ArrayList<>();

        reviews = new ArrayList<>();
    }

    public void init() throws FileNotFoundException, IOException {

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
            nounPhrases.add(np);
            review.addNounPhrase(np);
            sentence.addNounPhrase(np);
        }

        for (Tree child : rootNode.children()) {
            nounPhraseFind(child, review, sentence, reviewId, sentenceId);
        }
    }

    public static void test() {
        for (Review testReview : reviews) {
            for (Sentence sentence : testReview.getSentences()) {
                System.out.println(sentence.getRawContent());
                switch (sentence.getSentimentLevel()) {
                    case Sentence.NEGATIVE_SENTIMENT:
                        System.out.println("Negative");
                        break;
                    case Sentence.NEUTRAL_SENTIMENT:
                        System.out.println("Neutral");
                        break;
                    case Sentence.POSITIVE_SENTIMENT:
                        System.out.println("Positive");
                        break;
                    default:
                        break;

                }

                System.out.println("NPs");
                for (NounPhrase np : sentence.getNounPhrases()) {
                    System.out.println(np.getNpNode().getLeaves());
                }
                if (!sentence.getComparativeIndicatorPhrases().isEmpty()) {
                    System.out.println("Comparative NPs");
                    for (NounPhrase np : sentence.getNounPhrases()) {
                        if (np.isSuperior()) {
                            System.out.println("NP Superior " + np.getNpNode().getLeaves());
                        }
                        if (np.isInferior()) {
                            System.out.println("NP Inferior " + np.getNpNode().getLeaves());
                        }
                    }
                }
                System.out.println();
            }
        }
    }
}
