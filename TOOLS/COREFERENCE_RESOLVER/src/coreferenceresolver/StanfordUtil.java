/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasOffset;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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
        props.put("annotators", "tokenize, ssplit, pos, parse");
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
                Sentence newSentence = new Sentence();
                newSentence.setReviewId(reviewId);
                newSentence.setRawContent(sentence.toString());
                newSentence.setOffsetBegin(sentenceOffsetBegin);
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

                // this is the parse tree of the current sentence
                Tree sentenceTree = sentence.get(TreeAnnotation.class);
                nounPhraseFind(sentenceTree, newReview, reviewId, sentenceId);

                newReview.addSentence(newSentence);

                ++sentenceId;

            }
            reviews.add(newReview);
            ++reviewId;
        }

    }

    public void nounPhraseFind(Tree rootNode, Review review, int reviewId, int sentenceId) {
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
            nounPhrases.add(np);
            review.addNounPhrase(np);
        }

        for (Tree child : rootNode.children()) {
            nounPhraseFind(child, review,reviewId, sentenceId);
        }
    }

    public static void test() {
//        for (int i = 0; i < nounPhrases.size(); ++i){
//            System.out.println("------------");
//            System.out.println("NP words: " + nounPhrases.get(i).getNpNode().getLeaves());
//            System.out.println("NP head label: " + nounPhrases.get(i).getHeadLabel());
//            System.out.println("NP head: " + nounPhrases.get(i).getHeadNode());
//            System.out.println("NP begin: " + nounPhrases.get(i).getOffsetBegin());
//            System.out.println("NP end: " + nounPhrases.get(i).getOffsetEnd());
//            System.out.println("NP review: " + nounPhrases.get(i).getReviewId());
//            System.out.println("NP sentence: " + nounPhrases.get(i).getSentenceId());
//            System.out.println("------------");
//        }
        Review testReview = reviews.get(0);
        
//        for (NounPhrase np : testReview.getNounPhrases()){
//            NounPhrase np1;
//            NounPhrase np2;
//            if (np.getOffsetBegin() == 51){
//                np1 = np;
//            }
//            if (np.getOffsetBegin() == 75){
//                np2 = np;
//            }
//            FeatureExtr
//        }

//        System.out.println("Sentences size: " + testReview.getSentences().size());
//        List<Sentence> sentences = testReview.getSentences();
//        for (Sentence sen : sentences) {
//            System.out.println("-----------");
//            System.out.println(sen.getRawContent());
//            for (Token token : sen.getTokens()) {
//                System.out.println("Token Word: " + token.getWord());
//                System.out.println("Token POS: " + token.getPOS());
//                System.out.println("Token offset begin: " + token.getOffsetBegin());
//            }
//            System.out.println("-----------");
//        }        
    }
}
