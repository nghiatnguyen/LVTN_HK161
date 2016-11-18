/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.gui;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
import coreferenceresolver.util.Util;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/**
 *
 * @author TRONGNGHIA
 */
public class MarkupGUI extends JFrame {

    private static List<Review> markupReviews;
    private static List<ReviewElement> reviewElements;
    private static DefaultHighlighter.DefaultHighlightPainter highlightPainter
            = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

    public MarkupGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        //create menu items
        JMenuItem importMenuItem = new JMenuItem("Import");

        JMenuItem exportMenuItem = new JMenuItem("Export");

        fileMenu.add(importMenuItem);
        fileMenu.add(exportMenuItem);

        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //IMPORT BUTTON
        importMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser markupFileChooser = new JFileChooser(".");
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
                        protected Void doInBackground() throws IOException {
                            readMarkupFile(markupFileChooser.getSelectedFile().getAbsolutePath());
                            for (int i = 0; i < markupReviews.size(); ++i) {
                                mainPanel.add(newReviewPanel(markupReviews.get(i), i));
                            }
                            return null;
                        }

                        protected void done() {
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
                JFileChooser markupFileChooser = new JFileChooser(".");
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
                            int i = 0;
                            for (ReviewElement reviewElement : reviewElements) {
                                int j = 0;
                                for (Element element : reviewElement.elements) {
                                    String newRef = element.refSpinner.getValue().toString();
                                    String newType = element.typeSpinner.getValue().toString();
                                    markupReviews.get(i).getNounPhrases().get(j).setRefId(Integer.valueOf(newRef));
                                    if (newType.equals("Object/Attribute")) {
                                        markupReviews.get(i).getNounPhrases().get(j).setType(0);
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
        scrollMainPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollMainPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollMainPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        scrollMainPane.setSize(this.getWidth(), this.getHeight());
        this.add(scrollMainPane, BorderLayout.CENTER);
        this.pack();
    }

    private static void readMarkupFile(String markupFilePath) throws IOException {
        File markupFile = new File(markupFilePath);
        BufferedReader br = new BufferedReader(new FileReader(markupFile));
        String line = "";

        while ((line = br.readLine()) != null) {
            Review review = new Review();
            review.setRawContent(line);
            int numberOfNps = 0;
            for (int i = 0; i < line.length(); ++i) {
                if (line.charAt(i) == '<') {
                    ++numberOfNps;
                    review.addMarkupOpen(i);
                    for (int j = i; j < line.length(); ++j) {
                        if (line.charAt(j) == '>') {
                            review.addMarkupClose(j);
                            break;
                        }
                    }
                }
            }
            for (int npId = 0; npId < numberOfNps; ++npId) {
                MarkupNounPhrase np = new MarkupNounPhrase();
                np.content = review.getRawContent().substring(review.getMarkupOpens().get(npId), review.getMarkupCloses().get(npId));
                review.addNounPhrase(np);
            }

            markupReviews.add(review);
        }
        Util.readMarkupFile(markupReviews, markupFile);
    }

    private static void initMarkupFile(String markupFilePath) throws IOException {
        FileWriter fw = new FileWriter(new File(markupFilePath));
        for (Review review : markupReviews) {
            List<String> newMarkupHeads = new ArrayList<>();
            String markupReviewContent = review.getRawContent();
            //Create output file for markup            
            for (int npId = 0; npId < review.getNounPhrases().size(); ++npId) {
                String markupHead = "";
                for (int i = review.getMarkupOpens().get(npId) + 1; i < markupReviewContent.length(); ++i) {
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

    private JPanel newReviewPanel(Review review, int reviewId) {
        //Model
        ReviewElement reviewElement = new ReviewElement();

        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.PAGE_AXIS));

        JTextField title = new JTextField("NEW REVIEW " + reviewId);
        title.setBackground(Color.pink);

        reviewPanel.add(title);

        JTextArea reviewContentTxtArea = new JTextArea();
        reviewContentTxtArea.setLineWrap(true);
        reviewContentTxtArea.setWrapStyleWord(true);
        reviewContentTxtArea.setEditable(false);
        reviewContentTxtArea.setText(review.getRawContent());

        for (int i = 0; i < review.getMarkupOpens().size(); ++i) {
            try {
                reviewContentTxtArea.getHighlighter().addHighlight(review.getMarkupOpens().get(i), review.getMarkupCloses().get(i), highlightPainter);
            } catch (BadLocationException ex) {
                Logger.getLogger(MarkupGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reviewPanel.add(reviewContentTxtArea);

        for (int i = 0; i < review.getNounPhrases().size(); ++i) {
            JPanel newMarkupPanel = newMarkupPanel(review.getNounPhrases().size(), review.getNounPhrases().get(i), reviewElement);
            reviewPanel.add(newMarkupPanel);
        }

        //MODEL
        reviewElements.add(reviewElement);

        reviewPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        reviewPanel.add(Box.createVerticalStrut(20));

        return reviewPanel;
    }

    private JPanel newMarkupPanel(int noNps, NounPhrase np, ReviewElement reviewElement) {
        //MODEL
        Element element = new Element();

        JPanel markupPanel = new JPanel();
        markupPanel.setLayout(new GridLayout(1, 1));

        JTextArea npContentTxtArea = new JTextArea();
        npContentTxtArea.setEditable(false);
        npContentTxtArea.setText(((MarkupNounPhrase) np).content);
        markupPanel.add(npContentTxtArea);

        //REF
        SpinnerModel refSpinnerModel = new SpinnerNumberModel(np.getRefId(), -1, noNps - 1, 1);
        JSpinner refSpinner = new JSpinner(refSpinnerModel);
        refSpinner.setValue(np.getRefId());
        element.refSpinner = refSpinner;

        //TYPE        
        String[] typeValues = {"Object/Attribute", "Other", "Candidate"};
        SpinnerModel typeSpinnerModel = new SpinnerListModel(typeValues);
        JSpinner typeSpinner = new JSpinner(typeSpinnerModel);
        if (np.getType() == 0) {
            typeSpinner.setValue(typeValues[0]);
        } else if (np.getType() == 1){
            typeSpinner.setValue(typeValues[1]);
        } else if (np.getType() == 2){
            typeSpinner.setValue(typeValues[2]);
        }
        element.typeSpinner = typeSpinner;

        //REF + TYPE
        JPanel spinners = new JPanel();
        spinners.setLayout(new GridLayout(1, 1));
        spinners.add(refSpinner);
        spinners.add(typeSpinner);
        markupPanel.add(spinners);

        reviewElement.addElement(element);

        return markupPanel;
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
                new MarkupGUI().setVisible(true);
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

    private class Element {

        public JSpinner refSpinner;
        public JSpinner typeSpinner;
    }

    private class ReviewElement {

        public List<Element> elements;

        public ReviewElement() {
            this.elements = new ArrayList<>();
        }

        public void addElement(Element element) {
            this.elements.add(element);
        }
    }
}
