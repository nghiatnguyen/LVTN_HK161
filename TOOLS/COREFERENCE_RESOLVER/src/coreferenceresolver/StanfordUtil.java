/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author TRONGNGHIA
 */
public class StanfordUtil {

    private String documentText;
    private Properties props;
    private Annotation document;
    private CollinsHeadFinder headFinder;
    private List nounPhrases;
    private List reviews;

    public StanfordUtil(String documentText) {
        this.documentText = documentText;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        document = new Annotation(documentText);

        // run all Annotators on this text
        pipeline.annotate(document);

        headFinder = new CollinsHeadFinder();

        nounPhrases = new ArrayList<>();
        
        reviews = new ArrayList<>();
    }

    public void init() {
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        //Begin extracting from paragraphs
        List<Sentence> convertedSentences = new ArrayList<>();
        for (CoreMap sentence : sentences) {
            int sentenceOffsetStart = sentence.get(CharacterOffsetBeginAnnotation.class);
            //Check for BREAK CHARACTER, the end of a paragraph
            if (sentenceOffsetStart > 1 && documentText.substring(sentenceOffsetStart - 1, sentenceOffsetStart).equals("\n") && !convertedSentences.isEmpty()) {
                Review current = new Review(convertedSentences);
                reviews.add(current);
                convertedSentences = new ArrayList<>();
            }
            convertedSentences.add(new Sentence());                        
        }
        
        Review current = new Review(convertedSentences);
        reviews.add(current);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
            }

            // this is the parse tree of the current sentence
            Tree sentenceTree = sentence.get(TreeAnnotation.class);
            nounPhraseFind(sentenceTree);
        }
        int test = 0;
    }

    public void nounPhraseFind(Tree rootNode) {
        if (rootNode == null || rootNode.isLeaf()) {
            return;
        }

        if (rootNode.value().equals("NP")) {                        
            CoreLabel rootNodeLabel = (CoreLabel) rootNode.getLeaves().get(0).label();
            HasOffset ofs = (HasOffset) rootNodeLabel;
            System.out.println("Offset Begin = " + ofs.beginPosition());
            NounPhrase np = new NounPhrase();
            np.setNpNode(rootNode);
            np.setHeadNode(rootNode.headTerminal(headFinder));
            nounPhrases.add(rootNode);
            System.out.println("Noun Phrase detected");
            System.out.println("Text = " + rootNode.yieldWords());
            System.out.println("Head = " + rootNode.headTerminal(headFinder));
            CoreLabel headLabel = (CoreLabel) rootNode.headTerminal(headFinder).label();
            System.out.println("Head Label = " + headLabel.get(PartOfSpeechAnnotation.class));
            System.out.println("---------------------------------");
        }

        for (Tree child : rootNode.children()) {
            nounPhraseFind(child);
        }
    }
}
