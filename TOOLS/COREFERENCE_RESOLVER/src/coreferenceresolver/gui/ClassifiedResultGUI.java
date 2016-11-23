/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.gui;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.util.StanfordUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.StyledDocument;

/**
 *
 * @author TRONGNGHIA
 */
public class ClassifiedResultGUI extends javax.swing.JFrame {

    private UnderlineHighlighter.UnderlineHighlightPainter searchHighlighter = new UnderlineHighlighter.UnderlineHighlightPainter(Color.red);
    private List<Object> highlightSearchTags;
    private Color[] COLORS = new Color[]{Color.YELLOW, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN,
        Color.LIGHT_GRAY, Color.ORANGE, Color.PINK, Color.RED};

    private List<DefaultHighlighter.DefaultHighlightPainter> highlightPainters;

    private String demoText = "";

    /**
     * Creates new form ClassifiedResultGUI
     */
    public ClassifiedResultGUI() throws BadLocationException {
        initComponents();

        this.highlightSearchTags = new ArrayList<>();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        searchTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {
                removeHighlights();
                int scrollOffset = search(searchTF.getText());
                if (scrollOffset != -1) {
                    try {
                        resultJTxtPane.scrollRectToVisible(resultJTxtPane.modelToView(scrollOffset));
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ClassifiedResultGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            public void removeUpdate(DocumentEvent e) {
                removeHighlights();
                int scrollOffset = search(searchTF.getText());
                if (scrollOffset != -1) {
                    try {
                        resultJTxtPane.scrollRectToVisible(resultJTxtPane.modelToView(scrollOffset));
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ClassifiedResultGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            public void insertUpdate(DocumentEvent e) {
                removeHighlights();
                int scrollOffset = search(searchTF.getText());
                if (scrollOffset != -1) {
                    try {
                        resultJTxtPane.scrollRectToVisible(resultJTxtPane.modelToView(scrollOffset));
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ClassifiedResultGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        highlightPainters = new ArrayList<>();

        for (int i = 0; i < COLORS.length; ++i) {
            DefaultHighlighter.DefaultHighlightPainter highlightPainter
                    = new DefaultHighlighter.DefaultHighlightPainter(COLORS[i]);
            highlightPainters.add(highlightPainter);
        }

        resultJTxtPane.setText(demoText);

        for (int i = 0; i < StanfordUtil.reviews.size(); ++i) {
            highlightReview(StanfordUtil.reviews.get(i));
        }
    }

    private void highlightReview(Review review) throws BadLocationException {
        StyledDocument doc = resultJTxtPane.getStyledDocument();
        int curLen = doc.getLength();
        doc.insertString(curLen, "\n\n" + review.getRawContent(), null);
        for (int i = 0; i < review.getCorefChainsPredict().size(); ++i) {
            CorefChain cc = review.getCorefChainsPredict().get(i);
            for (int j = 0; j < cc.getChain().size(); ++j) {
                NounPhrase np = review.getNounPhrases().get(cc.getChain().get(j));
                int npOffsetBegin = np.getOffsetBegin();
                int npOffsetEnd = np.getOffsetEnd();
                resultJTxtPane.getHighlighter().addHighlight(curLen + 2 + npOffsetBegin, curLen + 2 + npOffsetEnd,
                        highlightPainters.get(i));
            }
        }
    }

    private void removeHighlights() {
        for (Object highlightTag : highlightSearchTags) {
            resultJTxtPane.getHighlighter().removeHighlight(highlightTag);
        }
        this.highlightSearchTags.clear();
    }

    private int search(String keyword) {
        if (keyword.isEmpty()) {
            return -1;
        }
        int scrollOffset = -1;
        String text = this.resultJTxtPane.getText().replaceAll("\r\n", "\n");
        int i = 0;
        int lastIndex = 0;
        while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
            if (i == 0) {
                scrollOffset = lastIndex;
            }
            int endIndex = lastIndex + keyword.length();
            try {
                Object highlightTag = this.resultJTxtPane.getHighlighter().addHighlight(lastIndex, endIndex, searchHighlighter);
                this.highlightSearchTags.add(highlightTag);
            } catch (BadLocationException e) {
                // Nothing to do
            }
            lastIndex = endIndex;
            ++i;
        }
        return scrollOffset;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        resultJTxtSPane = new javax.swing.JScrollPane();
        resultJTxtPane = new javax.swing.JTextPane();
        searchTF = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        resultJTxtPane.setEditable(false);
        resultJTxtSPane.setViewportView(resultJTxtPane);

        searchTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTFActionPerformed(evt);
            }
        });

        jLabel1.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultJTxtSPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resultJTxtSPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTFActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(ClassifiedResultGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClassifiedResultGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClassifiedResultGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClassifiedResultGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClassifiedResultGUI()
                            .setVisible(true);
                } catch (BadLocationException ex) {
                    Logger.getLogger(ClassifiedResultGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextPane resultJTxtPane;
    private javax.swing.JScrollPane resultJTxtSPane;
    private javax.swing.JTextField searchTF;
    // End of variables declaration//GEN-END:variables
}
