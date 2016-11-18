/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.gui;

import coreferenceresolver.process.MarkupMain;
import coreferenceresolver.util.StanfordUtil;
import coreferenceresolver.process.TrainingMain;
import coreferenceresolver.process.WekaMain;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author TRONGNGHIA
 */
public class MainGUI extends javax.swing.JFrame {

    /**
     * Creates new form MainGUI
     */
    public MainGUI() {
        initComponents();
        markupBtn.setEnabled(false);        
        testBtn.setEnabled(false);
        applyClassifierBtn.setEnabled(false);
        inputFilePathTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {

            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    markupBtn.setEnabled(false);
                    testBtn.setEnabled(false);                    
                }
            }

            public void insertUpdate(DocumentEvent e) {
                markupBtn.setEnabled(true);
                if (markupFilePathTF.getText() != null && !markupFilePathTF.getText().equals("")) {
                    testBtn.setEnabled(true);                    
                }
            }
        });

        markupFilePathTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {

            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    testBtn.setEnabled(false);                    
                }
            }

            public void insertUpdate(DocumentEvent e) {                
                if (inputFilePathTF.getText() != null && !inputFilePathTF.getText().equals("")) {
                    testBtn.setEnabled(true);                    
            }
            }            
        });

        testingFilePathTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {

            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    applyClassifierBtn.setEnabled(false);
                }
            }

            public void insertUpdate(DocumentEvent e) {
                applyClassifierBtn.setEnabled(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Training = new javax.swing.JPanel();
        markupBtn = new javax.swing.JButton();
        chooseTestingFileBtn = new javax.swing.JButton();
        testBtn = new javax.swing.JButton();
        functionTestBtn = new javax.swing.JButton();
        inputFileBtn = new javax.swing.JButton();
        markupFileBtn = new javax.swing.JButton();
        inputFilePathTF = new javax.swing.JTextField();
        noteTF = new javax.swing.JTextField();
        markupFilePathTF = new javax.swing.JTextField();
        testingFilePathTF = new javax.swing.JTextField();
        unitTestPathTF = new javax.swing.JTextField();
        applyClassifierBtn = new javax.swing.JButton();
        resultFilePathTF1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coreference Toolkit");

        Training.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Training.setToolTipText("");
        Training.setName(""); // NOI18N

        markupBtn.setText("Create markup file");
        markupBtn.setToolTipText("Create the markup file from input file");
        markupBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markupBtnActionPerformed(evt);
            }
        });

        chooseTestingFileBtn.setText("Choose testing file");
        chooseTestingFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseTestingFileBtnActionPerformed(evt);
            }
        });

        testBtn.setText("Create Testing file");
        testBtn.setToolTipText("Create Testing file from input file and markup file");
        testBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testBtnActionPerformed(evt);
            }
        });

        functionTestBtn.setText("Function Test StanfordUtil.test()");
        functionTestBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionTestBtnActionPerformed(evt);
            }
        });

        inputFileBtn.setText("Choose input file");
        inputFileBtn.setToolTipText("Choose an input file");
        inputFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputFileBtnActionPerformed(evt);
            }
        });

        markupFileBtn.setText("Choose markup file");
        markupFileBtn.setToolTipText("Choose a markup file");
        markupFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markupFileBtnActionPerformed(evt);
            }
        });

        inputFilePathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputFilePathTFActionPerformed(evt);
            }
        });

        noteTF.setEditable(false);
        noteTF.setBackground(new java.awt.Color(204, 204, 204));

        markupFilePathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markupFilePathTFActionPerformed(evt);
            }
        });

        testingFilePathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testingFilePathTFActionPerformed(evt);
            }
        });

        unitTestPathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitTestPathTFActionPerformed(evt);
            }
        });

        applyClassifierBtn.setText("Apply Classifier");
        applyClassifierBtn.setToolTipText("Apply Weka classifier to Testing file (default to J48)");
        applyClassifierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyClassifierBtnActionPerformed(evt);
            }
        });

        resultFilePathTF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultFilePathTF1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TrainingLayout = new javax.swing.GroupLayout(Training);
        Training.setLayout(TrainingLayout);
        TrainingLayout.setHorizontalGroup(
            TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noteTF)
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addComponent(functionTestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(unitTestPathTF))
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(markupFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(markupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(markupFilePathTF)
                            .addComponent(inputFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createSequentialGroup()
                            .addComponent(applyClassifierBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(resultFilePathTF1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(testBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(chooseTestingFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addComponent(testingFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        TrainingLayout.setVerticalGroup(
            TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputFileBtn)
                    .addComponent(inputFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(markupFileBtn)
                        .addGap(5, 5, 5)
                        .addComponent(markupBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chooseTestingFileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE))
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(markupFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testingFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)))
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyClassifierBtn)
                    .addComponent(resultFilePathTF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(noteTF, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(functionTestBtn)
                    .addComponent(unitTestPathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        markupBtn.getAccessibleContext().setAccessibleName("markupBtn");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Training, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Training, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        Training.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void markupBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markupBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose where your markup file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            markupFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "markup.out.txt");
            noteTF.setText("Markup waiting ...");
        } else {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MarkupMain.markup(inputFilePathTF.getText(), markupFilePathTF.getText());
                    noteTF.setText("Markup done!");
                    String folderPathOpen = markupFilePathTF.getText().substring(0, markupFilePathTF.getText().lastIndexOf(File.separatorChar));                    
                    Desktop.getDesktop().open(new File(folderPathOpen));
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_markupBtnActionPerformed

    private void inputFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputFileBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose an input file");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inputFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath());
        } else {
            noteTF.setText("No input file selected");
        }
    }//GEN-LAST:event_inputFileBtnActionPerformed

    private void inputFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputFilePathTFActionPerformed
    }//GEN-LAST:event_inputFilePathTFActionPerformed

    private void markupFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markupFileBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose an markup file");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            markupFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath());
        } else {
            noteTF.setText("No markup file selected");
        }
    }//GEN-LAST:event_markupFileBtnActionPerformed

    private void markupFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markupFilePathTFActionPerformed
    }//GEN-LAST:event_markupFilePathTFActionPerformed

    private void chooseTestingFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseTestingFileBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose an testing file");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            testingFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath());
        } else {
            noteTF.setText("No testing file selected");
        }
    }//GEN-LAST:event_chooseTestingFileBtnActionPerformed

    private void testBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose where your testing file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            testingFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "test.arff");
            noteTF.setText("Create testing file waiting ...");
        } else {
            noteTF.setText("No testing file location selected");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TrainingMain.run(inputFilePathTF.getText(), markupFilePathTF.getText(), testingFilePathTF.getText(), false);
                    noteTF.setText("Create testing file done!");
                    String folderPathOpen = testingFilePathTF.getText().substring(0, testingFilePathTF.getText().lastIndexOf(File.separatorChar));                    
                    Desktop.getDesktop().open(new File(folderPathOpen));
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_testBtnActionPerformed

    private void functionTestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_functionTestBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose where your testing file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            unitTestPathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "unit_test.txt");
            noteTF.setText("Create unit test file waiting");
        } else {
            noteTF.setText("No unit test file location selected");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StanfordUtil.test(unitTestPathTF.getText());
                    noteTF.setText("Create unit test file done!");
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_functionTestBtnActionPerformed

    private void testingFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testingFilePathTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_testingFilePathTFActionPerformed

    private void unitTestPathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitTestPathTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitTestPathTFActionPerformed

    private void resultFilePathTF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultFilePathTF1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultFilePathTF1ActionPerformed

    private void applyClassifierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyClassifierBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(".");
        inputFileChooser.setDialogTitle("Choose where your classified result file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            resultFilePathTF1.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "classified_result.txt");
            noteTF.setText("Create classified result file waiting ...");
        } else {
            noteTF.setText("No classified result file location selected");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Weka.j48Classify(testingFilePathTF.getText(), resultFilePathTF1.getText());
                    WekaMain.run(inputFilePathTF.getText(), testingFilePathTF.getText(), resultFilePathTF1.getText());
                    noteTF.setText("Create result file done!");
                    String folderPathOpen = resultFilePathTF1.getText().substring(0, resultFilePathTF1.getText().lastIndexOf(File.separatorChar));                    
                    Desktop.getDesktop().open(new File(folderPathOpen));
                    ClassifiedResultGUI.main(null);
                } catch (Exception ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }               
            }
        }).start();
    }//GEN-LAST:event_applyClassifierBtnActionPerformed

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
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Training;
    private javax.swing.JButton applyClassifierBtn;
    private javax.swing.JButton chooseTestingFileBtn;
    private javax.swing.JButton functionTestBtn;
    private javax.swing.JButton inputFileBtn;
    private javax.swing.JTextField inputFilePathTF;
    private javax.swing.JButton markupBtn;
    private javax.swing.JButton markupFileBtn;
    private javax.swing.JTextField markupFilePathTF;
    private javax.swing.JTextField noteTF;
    private javax.swing.JTextField resultFilePathTF1;
    private javax.swing.JButton testBtn;
    private javax.swing.JTextField testingFilePathTF;
    private javax.swing.JTextField unitTestPathTF;
    // End of variables declaration//GEN-END:variables

}
