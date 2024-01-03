package org.example;

import org.jdesktop.swingx.JXLabel;

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
import java.util.Arrays;
import java.util.Hashtable;

public class MessageAttachmentSendDialog extends JDialog {
    File[] attachmentFile;
    JPanel mainPanel ;
    GridBagLayout mainLayout;
    GridBagConstraints mainConstraints;

    JLabel informationAboutLink;

    JTextArea  textArea;
    JScrollPane textAreaScroller;
    JButton  attachmentButton;
    JXLabel attachmentFileLabel;

    JButton  submitButton;

    public MessageAttachmentSendDialog(Frame owner, Document textDocument, ArrayList<File> attach) {
        super(owner,"Attach Attachment into The Message" );
       setSize(600,450);

        setLocationRelativeTo(this);


        //setVisible(true);
        mainPanel = new JPanel();
        mainLayout = new GridBagLayout();
        mainConstraints =new GridBagConstraints();
        mainPanel.setLayout(mainLayout);
        attachmentFile = null;
        this.add(mainPanel);

        informationAboutLink = new JLabel("Insert Attachment Into The Message ");
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 0;
        mainConstraints.insets = new Insets(3,5,15,5);
        mainPanel.add(informationAboutLink,mainConstraints);

        textArea = new JTextArea(20,20);
       // textArea.setMinimumSize(new Dimension(500,150));
        //textArea.setMaximumSize(new Dimension(600,150));
        textArea.setBorder(new LineBorder(Color.LIGHT_GRAY,3));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 1;
        mainConstraints.insets = new Insets(3,5,10,5);
        //mainConstraints.ipadx = 10;
        //mainConstraints.ipady = 10;
        textArea.setDocument(textDocument);

        textAreaScroller = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textAreaScroller.setPreferredSize(new Dimension(400,250));
        mainPanel.add(textAreaScroller,mainConstraints);

        attachmentButton = new JButton("Insert Attachment Files");

        attachmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                if (fileChooser.showOpenDialog(owner)==JFileChooser.APPROVE_OPTION){
                    attachmentFile = fileChooser.getSelectedFiles();
                    attachmentFileLabel.setText("Files : ");
                    Arrays.stream(attachmentFile).forEach((a)->{
                        attachmentFileLabel.setText(attachmentFileLabel.getText()+ " "+ a.getName() +",");
                    });



                }

            }
        });
        attachmentButton.setOpaque(false);
        attachmentButton.setBackground(Color.white);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 2;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;
        mainConstraints.insets = new Insets(3,5,8,2);
        mainPanel.add(attachmentButton,mainConstraints);

        attachmentFileLabel = new JXLabel("Insert Files With Size Below 25MB");
        attachmentFileLabel.setLabelFor(attachmentButton);

        mainConstraints.gridx = 2;
        mainConstraints.gridy = 4;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;
        mainConstraints.insets = new Insets(3,5,8,5);
        mainPanel.add(attachmentFileLabel,mainConstraints);




        submitButton = new JButton("Submit ");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (attachmentFile!=null){
                    System.out.println(attach +"In Popup");
                    Arrays.stream(attachmentFile).forEach(attach::add);
                    System.out.println(attach + "In Popup after adding the file");

                    MessageAttachmentSendDialog.this.dispose();

                }
            }
        });
        submitButton.setOpaque(false);
        submitButton.setBackground(Color.white);
        mainConstraints.gridx = 2;
        mainConstraints.gridy = 3;
        mainConstraints.ipadx = 2;
        mainConstraints.ipady = 2;

        mainPanel.add(submitButton,mainConstraints);

        setVisible(true);
    }
}
