/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.gui;

import coreferenceresolver.process.MarkupMain;
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
import org.apache.commons.io.FileUtils;

/**
 *
 * @author TRONGNGHIA
 */
public class MainGUI extends javax.swing.JFrame {
    
    private String defaulPath = "";

    /**
     * Creates new form MainGUI
     */
    public MainGUI() throws IOException {
        initComponents();
        defaulPath = FileUtils.readFileToString(new File(".\\src\\coreferenceresolver\\gui\\defaultpath"));                
        markupBtn.setEnabled(false);
        testBtn.setEnabled(false);
        trainingBtn.setEnabled(false);
        applyClassifierBtn.setEnabled(false);
        inputFilePathTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {

            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    markupBtn.setEnabled(false);
                    testBtn.setEnabled(false);
                    trainingBtn.setEnabled(false);
                    applyClassifierBtn.setEnabled(false);
                }
            }

            public void insertUpdate(DocumentEvent e) {
                markupBtn.setEnabled(true);
                if (markupFilePathTF.getText() != null && !markupFilePathTF.getText().equals("")) {
                    testBtn.setEnabled(true);
                    trainingBtn.setEnabled(true);
                }
                if (testingFilePathTF.getText() != null && !testingFilePathTF.getText().equals("")
                        && trainingFilePathTF.getText() != null && !trainingFilePathTF.getText().equals("")) {
                    applyClassifierBtn.setEnabled(true);
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
                    trainingBtn.setEnabled(false);
                }
            }

            public void insertUpdate(DocumentEvent e) {
                if (inputFilePathTF.getText() != null && !inputFilePathTF.getText().equals("")) {
                    testBtn.setEnabled(true);
                    trainingBtn.setEnabled(true);
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
                if (trainingFilePathTF.getText() != null && !trainingFilePathTF.getText().equals("")
                        && inputFilePathTF.getText() != null && !inputFilePathTF.getText().equals("")) {
                    applyClassifierBtn.setEnabled(true);
                }
            }
        });

        trainingFilePathTF.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods
            public void changedUpdate(DocumentEvent e) {

            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    applyClassifierBtn.setEnabled(false);
                }
            }

            public void insertUpdate(DocumentEvent e) {
                if (testingFilePathTF.getText() != null && !testingFilePathTF.getText().equals("")
                        && inputFilePathTF.getText() != null && !inputFilePathTF.getText().equals("")) {
                    applyClassifierBtn.setEnabled(true);
                }
            }
        });
        
        inputFilePathTF.setText(defaulPath + File.separatorChar + "input_test.txt");
        markupFilePathTF.setText(defaulPath + File.separatorChar + "input_test.txt.markup");
        trainingFilePathTF.setText(defaulPath + File.separatorChar + "train.arff");
        testingFilePathTF.setText(defaulPath + File.separatorChar + "test.arff");
        
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
        inputFileBtn = new javax.swing.JButton();
        markupFileBtn = new javax.swing.JButton();
        inputFilePathTF = new javax.swing.JTextField();
        noteTF = new javax.swing.JTextField();
        markupFilePathTF = new javax.swing.JTextField();
        testingFilePathTF = new javax.swing.JTextField();
        applyClassifierBtn = new javax.swing.JButton();
        resultFilePathTF1 = new javax.swing.JTextField();
        chooseTrainingFileBtn = new javax.swing.JButton();
        trainingBtn = new javax.swing.JButton();
        trainingFilePathTF = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Coreference Toolkit");

        Training.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Training.setToolTipText("");
        Training.setName(""); // NOI18N

        markupBtn.setBackground(new java.awt.Color(255, 255, 102));
        markupBtn.setText("Create markup file");
        markupBtn.setToolTipText("Create the markup file from input file. The \"input.txt.pos\" and \"input.txt.pos.chk\" files are also generated in the current directory.");
        markupBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markupBtnActionPerformed(evt);
            }
        });

        chooseTestingFileBtn.setBackground(new java.awt.Color(204, 255, 204));
        chooseTestingFileBtn.setText("Choose testing file");
        chooseTestingFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseTestingFileBtnActionPerformed(evt);
            }
        });

        testBtn.setBackground(new java.awt.Color(204, 255, 204));
        testBtn.setText("Create Testing file");
        testBtn.setToolTipText("Create Testing file from input file and markup file. The matching \"input.txt.pos\" and \"input.txt.pos.chk\" files are needed in current dir.");
        testBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testBtnActionPerformed(evt);
            }
        });

        inputFileBtn.setText("Choose input file");
        inputFileBtn.setToolTipText("Choose an input file");
        inputFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputFileBtnActionPerformed(evt);
            }
        });

        markupFileBtn.setBackground(new java.awt.Color(255, 255, 102));
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

        applyClassifierBtn.setBackground(new java.awt.Color(255, 0, 51));
        applyClassifierBtn.setText("Apply Classifier");
        applyClassifierBtn.setToolTipText("Apply Weka classifier to Testing file (default to J48). The matching \"input.txt.pos\" and \"input.txt.pos.chk\" files are needed in current dir.");
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

        chooseTrainingFileBtn.setBackground(new java.awt.Color(153, 255, 255));
        chooseTrainingFileBtn.setText("Choose training file");
        chooseTrainingFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseTrainingFileBtnActionPerformed(evt);
            }
        });

        trainingBtn.setBackground(new java.awt.Color(153, 255, 255));
        trainingBtn.setText("Create training file");
        trainingBtn.setToolTipText("Create Training file from input file and markup file. The matching \"input.txt.pos\" and \"input.txt.pos.chk\" files are needed in current dir.");
        trainingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingBtnActionPerformed(evt);
            }
        });

        trainingFilePathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingFilePathTFActionPerformed(evt);
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
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(markupFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(markupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(markupFilePathTF)
                            .addComponent(inputFilePathTF)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createSequentialGroup()
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(applyClassifierBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(trainingBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chooseTrainingFileBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chooseTestingFileBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(testBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(testingFilePathTF, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                            .addComponent(trainingFilePathTF)
                            .addComponent(resultFilePathTF1))))
                .addContainerGap())
        );
        TrainingLayout.setVerticalGroup(
            TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputFileBtn)
                    .addComponent(inputFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(markupFileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(markupBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(chooseTestingFileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testBtn)
                        .addGap(29, 29, 29))
                    .addGroup(TrainingLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(markupFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testingFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)))
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createSequentialGroup()
                        .addComponent(chooseTrainingFileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trainingBtn)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TrainingLayout.createSequentialGroup()
                        .addComponent(trainingFilePathTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addGroup(TrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyClassifierBtn)
                    .addComponent(resultFilePathTF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(noteTF, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
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
            .addComponent(Training, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Training.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void markupBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markupBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
        inputFileChooser.setDialogTitle("Choose where your markup file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String[] inputFilePath = inputFilePathTF.getText().split("\\\\");
            String inputFileName = inputFilePath[inputFilePath.length - 1];
            markupFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + inputFileName + ".markup");
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
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
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
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
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
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
        inputFileChooser.setDialogTitle("Choose an testing file");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            testingFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath());
        } else {
            noteTF.setText("No testing file selected");
        }
    }//GEN-LAST:event_chooseTestingFileBtnActionPerformed

    private void testBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
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
                } catch (Exception ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_testBtnActionPerformed

    private void testingFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testingFilePathTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_testingFilePathTFActionPerformed

    private void resultFilePathTF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultFilePathTF1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultFilePathTF1ActionPerformed

    private void applyClassifierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyClassifierBtnActionPerformed
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
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
                    WekaMain.run(inputFilePathTF.getText(), trainingFilePathTF.getText(), testingFilePathTF.getText(), resultFilePathTF1.getText());
                    noteTF.setText("Create result file done!");
                    String folderPathOpen = resultFilePathTF1.getText().substring(0, resultFilePathTF1.getText().lastIndexOf(File.separatorChar));
                    Desktop.getDesktop().open(new File(folderPathOpen));
                    //Open the window for predicted chains                    
                    ClassifiedResultGUI.render(true);
                    //Open the window for actual chains                    
                    ClassifiedResultGUI.render(false);
                } catch (Exception ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_applyClassifierBtnActionPerformed

    private void trainingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingBtnActionPerformed
        // TODO add your handling code here:
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
        inputFileChooser.setDialogTitle("Choose where your training file saved");
        inputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            trainingFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath() + File.separator + "train.arff");
            noteTF.setText("Create training file waiting ...");
        } else {
            noteTF.setText("No training file location selected");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TrainingMain.run(inputFilePathTF.getText(), markupFilePathTF.getText(), trainingFilePathTF.getText(), true);
                    noteTF.setText("Create training file done!");
                    String folderPathOpen = trainingFilePathTF.getText().substring(0, trainingFilePathTF.getText().lastIndexOf(File.separatorChar));
                    Desktop.getDesktop().open(new File(folderPathOpen));
                } catch (Exception ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_trainingBtnActionPerformed

    private void trainingFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingFilePathTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trainingFilePathTFActionPerformed

    private void chooseTrainingFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseTrainingFileBtnActionPerformed
        // TODO add your handling code here:
        JFileChooser inputFileChooser = new JFileChooser(defaulPath);
        inputFileChooser.setDialogTitle("Choose a training file");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            trainingFilePathTF.setText(inputFileChooser.getSelectedFile().getAbsolutePath());
        } else {
            noteTF.setText("No training file selected");
        }
    }//GEN-LAST:event_chooseTrainingFileBtnActionPerformed

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
                try {
                    new MainGUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Training;
    private javax.swing.JButton applyClassifierBtn;
    private javax.swing.JButton chooseTestingFileBtn;
    private javax.swing.JButton chooseTrainingFileBtn;
    private javax.swing.JButton inputFileBtn;
    private javax.swing.JTextField inputFilePathTF;
    private javax.swing.JButton markupBtn;
    private javax.swing.JButton markupFileBtn;
    private javax.swing.JTextField markupFilePathTF;
    private javax.swing.JTextField noteTF;
    private javax.swing.JTextField resultFilePathTF1;
    private javax.swing.JButton testBtn;
    private javax.swing.JTextField testingFilePathTF;
    private javax.swing.JButton trainingBtn;
    private javax.swing.JTextField trainingFilePathTF;
    // End of variables declaration//GEN-END:variables

}
