package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageLinkSendDialog extends JDialog {
    JPanel mainPanel ;
    GridBagLayout mainLayout;
    GridBagConstraints mainConstraints;

    JLabel informationAboutLink;

    JTextArea  textArea;
    JScrollPane textAreaScroller;
    JTextField linkField;
    JTextField descriptionField;
    JButton  submitButton;




    public MessageLinkSendDialog(Frame owner, Document textDocument) {
        super(owner, "Attach Link to Message ");

        setSize(500,500);
        setResizable(false);

        setLocationRelativeTo(this);
        setVisible(true);
        mainPanel = new JPanel();
        mainLayout = new GridBagLayout();
        mainConstraints =new GridBagConstraints();
        mainPanel.setLayout(mainLayout);

       this.add(mainPanel);

       informationAboutLink = new JLabel("Insert Link at Any Point of the Message ");
       mainConstraints.gridx = 2;
       mainConstraints.gridy = 0;
       mainConstraints.insets = new Insets(3,5,15,5);
       mainPanel.add(informationAboutLink,mainConstraints);

        textArea = new JTextArea();

        textArea.setBorder(new LineBorder(Color.LIGHT_GRAY,3));
        textArea.setLineWrap(true);
        //textArea.setMinimumSize(new Dimension(300,150));
        //textArea.setMaximumSize(new Dimension(500,150));
       // textArea.setWrapStyleWord(true);
        textArea.setAutoscrolls(true);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 1;
        mainConstraints.insets = new Insets(3,5,10,5);

        textArea.setDocument(textDocument);

        textAreaScroller = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       textAreaScroller.setPreferredSize(new Dimension(300,250));
        //textAreaScroller.setMinimumSize(new Dimension(300,150));
        //textAreaScroller.setMaximumSize(new Dimension(500,150));
        mainPanel.add(textAreaScroller,mainConstraints);
        linkField = new JTextField();

        linkField.setPreferredSize(new Dimension(200,10));
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 2;
        mainConstraints.ipady = 10;
        mainConstraints.ipadx = 20;


        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(linkField,mainConstraints);

        descriptionField = new JTextField();

        descriptionField.setPreferredSize(new Dimension(200,10));
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 3;
        mainConstraints.ipady = 10;
        mainConstraints.ipadx = 20;


        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(descriptionField,mainConstraints);


        submitButton = new JButton("Submit Link");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          if (linkField.getText().length()>0&descriptionField.getText().length()>0){
              String linkText = (" <a href=\""+linkField.getText()+"\">"+descriptionField.getText()+"</a>").trim();
              try {
                  textDocument.insertString(textArea.getCaretPosition(),linkText,new SimpleAttributeSet());
              } catch (BadLocationException ex) {
                  throw new RuntimeException(ex);
              }
          }
            }
        });
        submitButton.setOpaque(false);
        submitButton.setBackground(Color.white);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 4;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;

        mainPanel.add(submitButton,mainConstraints);

    }
}
