package org.example;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class MessageSenderPopup extends JPopupMenu {

    Hashtable<String, File> filesAndAttachments ;
    JMenuItem attachmentMenu;

    JMenuItem imageMenu ;
    JMenuItem linkMenu ;


    public  MessageSenderPopup(JPanel mainComponent , Document messageAreaDocument , ArrayList<File> imagesFiles ,ArrayList<File> attachFiles ) {
        super();
        filesAndAttachments = new Hashtable<>();

        Action linkAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageLinkSendDialog linkSendDialog = new MessageLinkSendDialog((Frame) SwingUtilities.getWindowAncestor(mainComponent),messageAreaDocument);



            }
        };
        Action imageAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageImageSendDialog imageSendDialog = new MessageImageSendDialog((Frame) SwingUtilities.getWindowAncestor(mainComponent),messageAreaDocument,imagesFiles);

            }
        };
        Action attachmentAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageAttachmentSendDialog attachmentSendDialog = new MessageAttachmentSendDialog((Frame) SwingUtilities.getWindowAncestor(mainComponent),messageAreaDocument,attachFiles);


            }
        };

        setPopupSize(new Dimension(150,150));
        attachmentMenu = new JMenuItem();
        attachmentMenu.setAction(attachmentAction);
        attachmentMenu.setText("Insert Attachment");
        add(attachmentMenu);
        linkMenu = new JMenuItem();
        linkMenu.setAction(linkAction);

        linkMenu.setText("Insert Link");
        add(linkMenu);

        imageMenu = new JMenuItem();
        imageMenu.setAction(imageAction);
        imageMenu.setText("Insert Image");
        add(imageMenu);





    }



}
