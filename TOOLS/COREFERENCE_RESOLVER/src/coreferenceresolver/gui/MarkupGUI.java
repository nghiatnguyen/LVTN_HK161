/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.gui;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author TRONGNGHIA
 */
public class MarkupGUI extends JFrame {

    private String defaulPath = "";
    private static List<Review> markupReviews;
    private static List<ReviewElement> reviewElements;

    private static final Color LIGHT_SALMON = new Color(255, 160, 122);
    private static final Color PALE_GOLDEN_ROD = new Color(238, 232, 170);
    private static final Color TAN = new Color(210, 180, 140);
    private static final Color TURQOUISE = new Color(64, 224, 208);
    private static final Color BLUE_VIOLET = new Color(138, 43, 226);
    private static final Color WHEAT = new Color(245, 222, 179);
    private static final Color CHOCOLATE = new Color(210, 105, 30);
    private static final Color MAROON = new Color(128, 0, 0);

    private Color[] COLORS = new Color[]{Color.YELLOW, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN,
        Color.LIGHT_GRAY, Color.ORANGE, Color.PINK, MAROON, Color.MAGENTA, LIGHT_SALMON, PALE_GOLDEN_ROD, Color.RED, TAN, CHOCOLATE,
        TURQOUISE, BLUE_VIOLET, WHEAT};

    private List<DefaultHighlighter.DefaultHighlightPainter> highlightPainters;

    public MarkupGUI() throws IOException {
        highlightPainters = new ArrayList<>();

        for (int i = 0; i < COLORS.length; ++i) {
            DefaultHighlighter.DefaultHighlightPainter highlightPainter
                    = new DefaultHighlighter.DefaultHighlightPainter(COLORS[i]);
            highlightPainters.add(highlightPainter);
        }

        defaulPath = FileUtils.readFileToString(new File(".\\src\\coreferenceresolver\\gui\\defaultpath"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        //create menu items
        JMenuItem importMenuItem = new JMenuItem("Import");

        JMenuItem exportMenuItem = new JMenuItem("Export");

        fileMenu.add(importMenuItem);
        fileMenu.add(exportMenuItem);

        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        ScrollablePanel mainPanel = new ScrollablePanel();
        mainPanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.NONE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //IMPORT BUTTON
        importMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                MarkupGUI.reviewElements.clear();
//                MarkupGUI.markupReviews.clear();                
                JFileChooser markupFileChooser = new JFileChooser(defaulPath);
                markupFileChooser.setDialogTitle("Choose your markup file");
                markupFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (markupFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final JDialog d = new JDialog();
                    JPanel p1 = new JPanel(new GridBagLayout());
                    p1.add(new JLabel("Please Wait..."), new GridBagConstraints());
                    d.getContentPane().add(p1);
                    d.setSize(100, 100);
                    d.setLocationRelativeTo(null);
                    d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    d.setModal(true);

                    SwingWorker<?, ?> worker = new SwingWorker<Void, Void>() {
                        protected Void doInBackground() throws IOException, BadLocationException {
                            readMarkupFile(markupFileChooser.getSelectedFile().getAbsolutePath());
                            for (int i = 0; i < markupReviews.size(); ++i) {
                                mainPanel.add(newReviewPanel(markupReviews.get(i), i));
                            }
                            return null;
                        }

                        protected void done() {
                            MarkupGUI.this.revalidate();
                            d.dispose();
                        }
                    };
                    worker.execute();
                    d.setVisible(true);
                } else {
                    return;
                }
            }
        });

        //EXPORT BUTTON: GET NEW VALUE (REF, TYPE) OF NPs      
        exportMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser markupFileChooser = new JFileChooser(defaulPath);
                markupFileChooser.setDialogTitle("Choose where your markup file saved");
                markupFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (markupFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final JDialog d = new JDialog();
                    JPanel p1 = new JPanel(new GridBagLayout());
                    p1.add(new JLabel("Please Wait..."), new GridBagConstraints());
                    d.getContentPane().add(p1);
                    d.setSize(100, 100);
                    d.setLocationRelativeTo(null);
                    d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    d.setModal(true);

                    SwingWorker<?, ?> worker = new SwingWorker<Void, Void>() {
                        protected Void doInBackground() throws IOException {
                            for (Review review : markupReviews) {
                                generateNPsRef(review);
                            }
                            int i = 0;
                            for (ReviewElement reviewElement : reviewElements) {
                                int j = 0;
                                for (Element element : reviewElement.elements) {
                                    String newType = element.typeSpinner.getValue().toString();
                                    if (newType.equals("Object")) {
                                        markupReviews.get(i).getNounPhrases().get(j).setType(0);
                                    } else if (newType.equals("Attribute")) {
                                        markupReviews.get(i).getNounPhrases().get(j).setType(3);
                                    } else if (newType.equals("Other")) {
                                        markupReviews.get(i).getNounPhrases().get(j).setType(1);
                                    } else if (newType.equals("Candidate")) {
                                        markupReviews.get(i).getNounPhrases().get(j).setType(2);
                                    }
                                    ++j;
                                }
                                ++i;
                            }
                            initMarkupFile(markupFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "markup.out.txt");
                            return null;
                        }

                        protected void done() {
                            d.dispose();
                            try {
                                Desktop.getDesktop().open(new File(markupFileChooser.getSelectedFile().getAbsolutePath()));
                            } catch (IOException ex) {
                                Logger.getLogger(MarkupGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    worker.execute();
                    d.setVisible(true);
                } else {
                    return;
                }
            }
        });

        JScrollPane scrollMainPane = new JScrollPane(mainPanel);
        scrollMainPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollMainPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        scrollMainPane.setSize(this.getWidth(), this.getHeight());
        this.setResizable(false);
        this.add(scrollMainPane, BorderLayout.CENTER);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
    }

    private static void readMarkupFile(String markupFilePath) throws IOException {
        File markupFile = new File(markupFilePath);
        BufferedReader br = new BufferedReader(new FileReader(markupFile));
        String line = "";

        int reviewId = 0;
        while ((line = br.readLine()) != null) {
            Review review = new Review();
            review.setRawContent(line);
            for (int i = 0; i < line.length(); ++i) {
                if (line.charAt(i) == '<') {
                    MarkupNounPhrase np = new MarkupNounPhrase();
                    np.setOffsetBegin(i);
                    boolean checkSpaceChar = false;
                    for (int j = i; j < line.length(); ++j) {
                        if (!checkSpaceChar && line.charAt(j) == ' ') {
                            String info = line.substring(i + 1, j);
                            String[] infos = info.split(",");
                            np.setId(Integer.valueOf(infos[0]));
                            np.setRefId(Integer.valueOf(infos[1]));
                            np.setType(Integer.valueOf(infos[2]));
                            checkSpaceChar = true;
                        }
                        if (line.charAt(j) == '>') {
                            np.setOffsetEnd(j);
                            break;
                        }
                    }
                    np.content = review.getRawContent().substring(np.getOffsetBegin(), np.getOffsetEnd());
                    np.setReviewId(reviewId);
                    review.addNounPhrase(np);
                }
            }

            generateCorefChains(review);
            markupReviews.add(review);
            ++reviewId;
        }
    }

    private static void initMarkupFile(String markupFilePath) throws IOException {
        FileWriter fw = new FileWriter(new File(markupFilePath));
        for (Review review : markupReviews) {
            List<String> newMarkupHeads = new ArrayList<>();
            String markupReviewContent = review.getRawContent();
            //Create output file for markup            
            for (int npId = 0; npId < review.getNounPhrases().size(); ++npId) {
                String markupHead = "";
                for (int i = review.getNounPhrases().get(npId).getOffsetBegin() + 1; i < markupReviewContent.length(); ++i) {
                    if (markupReviewContent.charAt(i) == ' ') {
                        break;
                    }
                    markupHead += markupReviewContent.charAt(i);
                }
                String[] markups = markupHead.split(",");
                markupHead = markups[0] + "," + review.getNounPhrases().get(npId).getRefId() + ","
                        + review.getNounPhrases().get(npId).getType();
                newMarkupHeads.add(markupHead);
            }

            int charId = 0;
            int openNPchar = 0;
            while (charId < markupReviewContent.length()) {
                if (markupReviewContent.charAt(charId) == '<') {
                    int nearestSpaceCharId = -1;
                    for (int i = charId; i < markupReviewContent.length(); ++i) {
                        if (markupReviewContent.charAt(i) == ' ') {
                            nearestSpaceCharId = i;
                            break;
                        }
                    }
                    markupReviewContent = markupReviewContent.substring(0, charId) + "<" + newMarkupHeads.get(openNPchar) + markupReviewContent.substring(nearestSpaceCharId);
                    ++openNPchar;
                }
                ++charId;
            }

            fw.write(markupReviewContent);
            fw.write("\n");
        }
        fw.close();
    }

    private JScrollPane newReviewPanel(Review review, int reviewId) throws BadLocationException {
        //Model
        ReviewElement reviewElement = new ReviewElement();

        ScrollablePanel reviewPanel = new ScrollablePanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.PAGE_AXIS));

        JTextField title = new JTextField("NEW REVIEW " + reviewId);
        title.setBackground(Color.pink);

        reviewPanel.add(title);

        JTextArea reviewContentTxtArea = new JTextArea();
        reviewContentTxtArea.setLineWrap(true);
        reviewContentTxtArea.setWrapStyleWord(true);
        reviewContentTxtArea.setEditable(false);
        reviewContentTxtArea.setText(review.getRawContent());

        int chainId = 0;
        for (CorefChain cc : review.getCorefChains()) {
            for (int npId : cc.getChain()) {
                NounPhrase np = review.getNounPhrases().get(npId);
                Object highlighTag = reviewContentTxtArea.getHighlighter().addHighlight(np.getOffsetBegin(), np.getOffsetEnd() + 1,
                        highlightPainters.get(chainId));
                this.markupReviews.get(reviewId).getNounPhrases().get(npId).highlighterTag = highlighTag;
            }
            ++chainId;
        }

        reviewPanel.add(reviewContentTxtArea);

        ScrollablePanel markupsPanel = new ScrollablePanel();
        markupsPanel.setLayout(new BoxLayout(markupsPanel, BoxLayout.PAGE_AXIS));

        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            JScrollPane newMarkupPanel = newMarkupPanel(review.getNounPhrases().get(i), reviewElement);
            markupsPanel.add(newMarkupPanel);
        }

        JScrollPane scrollMarkupsPanel = new JScrollPane(markupsPanel);

        //Add Dimension for scrolling
        Dimension curScreenDimen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        scrollMarkupsPanel.setPreferredSize(new Dimension((int) curScreenDimen.getWidth() - 50, 400));

        reviewPanel.add(scrollMarkupsPanel);

        //MODEL
        reviewElement.reviewTextArea = reviewContentTxtArea;
        reviewElements.add(reviewElement);

        reviewPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        reviewPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollReviewPanel = new JScrollPane(reviewPanel);

        return scrollReviewPanel;
    }

    private JScrollPane newMarkupPanel(NounPhrase np, ReviewElement reviewElement) {
        //MODEL
        Element element = new Element();

        //Newly added
        ScrollablePanel markupPanel = new ScrollablePanel();
        markupPanel.setLayout(new BoxLayout(markupPanel, BoxLayout.X_AXIS));
        markupPanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);

        JTextArea npContentTxtArea = new JTextArea();
        npContentTxtArea.setEditable(false);
        npContentTxtArea.setText(((MarkupNounPhrase) np).content);
        markupPanel.add(npContentTxtArea);

        //REF
        SpinnerModel refSpinnerModel = new SpinnerNumberModel(np.getChainId(), -1, COLORS.length - 1, 1);
        JSpinner refSpinner = new JSpinner(refSpinnerModel);
        refSpinner.setValue(np.getChainId());

        refSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                np.setChainId((int) refSpinner.getValue());
                try {
                    rePaint(reviewElements.get(np.getReviewId()), np);
                } catch (BadLocationException ex) {
                    Logger.getLogger(MarkupGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        element.refSpinner = refSpinner;

        //TYPE        
        String[] typeValues = {"Object", "Other", "Candidate", "Attribute"};
        SpinnerModel typeSpinnerModel = new SpinnerListModel(typeValues);
        JSpinner typeSpinner = new JSpinner(typeSpinnerModel);
        typeSpinner.setValue(typeValues[np.getType()]);

        element.typeSpinner = typeSpinner;

        //REF + TYPE
        ScrollablePanel spinners = new ScrollablePanel();
        spinners.setLayout(new BoxLayout(spinners, BoxLayout.X_AXIS));
        spinners.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
        spinners.add(refSpinner);
        spinners.add(typeSpinner);
        markupPanel.add(spinners);

        reviewElement.addElement(element);

        JScrollPane scrollMarkupPanel = new JScrollPane(markupPanel);

        return scrollMarkupPanel;
    }

    private void rePaint(ReviewElement reviewElement, NounPhrase np) throws BadLocationException {
        if (np.highlighterTag != null) {
            reviewElement.reviewTextArea.getHighlighter().removeHighlight(np.highlighterTag);
        }
        if (np.getChainId() != -1) {
            Object newHlTag = reviewElement.reviewTextArea.getHighlighter().addHighlight(np.getOffsetBegin(), np.getOffsetEnd() + 1, this.highlightPainters.get(np.getChainId()));
            np.highlighterTag = newHlTag;
        }
    }

    private static void generateNPsRef(Review review) {
        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            NounPhrase curNp = review.getNounPhrases().get(i);
            if (curNp.getType() == 1 || curNp.getRefId() == -1) {
                continue;
            }
            boolean hasAntecedent = false;
            for (int j = 0; j < i; ++j) {
                NounPhrase checkedNp = review.getNounPhrases().get(j);
                if (checkedNp.getChainId() == curNp.getChainId()) {
                    curNp.setRefId(checkedNp.getId());
                    hasAntecedent = true;
                    break;
                }
            }
            if (!hasAntecedent) {
                curNp.setRefId(-1);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        init();
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MarkupGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MarkupGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MarkupGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MarkupGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>        
        //</editor-fold>        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MarkupGUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MarkupGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private static void init() {
        markupReviews = new ArrayList<>();
        reviewElements = new ArrayList<>();
    }

    private static class MarkupNounPhrase extends NounPhrase {

        protected String content;
    }

    private static void generateCorefChains(Review review) {
        for (NounPhrase np : review.getNounPhrases()) {
            review.addCorefChain(np);
        }
    }

    private class Element {

        public JSpinner refSpinner;
        public JSpinner typeSpinner;
        public Object highlighterTag;
    }

    private class ReviewElement {

        public JTextArea reviewTextArea;

        public List<Element> elements;

        public ReviewElement() {
            this.elements = new ArrayList<>();
        }

        public void addElement(Element element) {
            this.elements.add(element);
        }
    }
}
