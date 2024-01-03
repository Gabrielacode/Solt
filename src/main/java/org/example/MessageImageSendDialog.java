package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class MessageImageSendDialog  extends JDialog {
    File imageFile;
    JPanel mainPanel ;
    GridBagLayout mainLayout;
    GridBagConstraints mainConstraints;

    JLabel informationAboutLink;

    JTextArea  textArea;
    JScrollPane textAreaScroller;
    JButton  imageButton;
    JLabel heightLabel;
    JSpinner heightField;
    JLabel widthLabel;
    JSpinner widthField;
    JButton  submitButton;

    public MessageImageSendDialog(Frame owner, Document textDocument, ArrayList<File> images) {
        super(owner,"Attach Image into The Message" );
        setSize(550,550);

        setLocationRelativeTo(this);
        setVisible(true);
        mainPanel = new JPanel();
        mainLayout = new GridBagLayout();
        mainConstraints =new GridBagConstraints();
        mainPanel.setLayout(mainLayout);
        imageFile = null;
        this.add(mainPanel);

        informationAboutLink = new JLabel("Insert Image at Any Point of the Message ");
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 0;
        mainConstraints.insets = new Insets(3,5,15,5);
        mainPanel.add(informationAboutLink,mainConstraints);

        textArea = new JTextArea(20,20);
       // textArea.setMinimumSize(new Dimension(500,150));
        //textArea.setMaximumSize(new Dimension(600,150));
        textArea.setBorder(new LineBorder(Color.LIGHT_GRAY,3));
        textArea.setLineWrap(true);

        mainConstraints.gridx = 2;
        mainConstraints.gridy = 1;
        mainConstraints.insets = new Insets(3,5,10,5);
        //mainConstraints.ipadx = 10;
        //mainConstraints.ipady = 10;
        textArea.setDocument(textDocument);

        textAreaScroller = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textAreaScroller.setPreferredSize(new Dimension(350,200));
        mainPanel.add(textAreaScroller,mainConstraints);

        imageButton = new JButton("Insert  Inline Image File");

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JFileChooser fileChooser = new JFileChooser();
               if (fileChooser.showOpenDialog(owner)==JFileChooser.APPROVE_OPTION){
                   imageFile = fileChooser.getSelectedFile();
               }
               if (imageFile.exists())imageButton.setForeground(Color.green);

            }
        });
        imageButton.setOpaque(false);
        imageButton.setBackground(Color.white);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 2;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;
        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(imageButton,mainConstraints);

        heightLabel = new JLabel("Height :");
        heightLabel.setLabelFor(heightField);

        mainConstraints.gridx = 1;
        mainConstraints.gridy = 3;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;
        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(heightLabel,mainConstraints);



        heightField = new JSpinner(new SpinnerNumberModel(1,1,2000,1));


        heightField.setMinimumSize(new Dimension(100,10));
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 3;
        mainConstraints.ipady = 10;
        mainConstraints.ipadx = 20;


        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(heightField,mainConstraints);

        widthLabel = new JLabel("Width :");
        widthLabel.setLabelFor(widthField);

        mainConstraints.gridx = 1;
        mainConstraints.gridy = 4;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;
        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(widthLabel,mainConstraints);


        widthField = new JSpinner(new SpinnerNumberModel(1,1,2000,1));
        widthField.setMinimumSize(new Dimension(100,10));
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 4;
        mainConstraints.ipady = 10;
        mainConstraints.ipadx = 20;


        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(widthField,mainConstraints);

        submitButton = new JButton("Submit Link");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              String imageString = imageFile.getName();

              // Image Cid needs no space So Remember to replace all white spaces

                   images.add(imageFile);
                     String linkText = " <div><img src = \"cid:"+imageString.replaceAll("\\s","")+"\" height ="+heightField.getValue()+" width = "+widthField.getValue()+"/></div>";
                     try {
                         textDocument.insertString(textArea.getCaretPosition(),linkText,new SimpleAttributeSet());
                     } catch (BadLocationException ex) {
                         throw new RuntimeException(ex);

                 }
                     imageButton.setForeground(Color.black);
            }
        });
        submitButton.setOpaque(false);
        submitButton.setBackground(Color.white);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 5;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;

        mainPanel.add(submitButton,mainConstraints);


    }
}
