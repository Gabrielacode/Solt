package org.example;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class Folders extends JButton {
    String name;
    boolean isFolder;
    JComponent mainPanel;
    Folder folderRefrenced;
    HashMap<Folder,Boolean> sub_folders =  new HashMap<>();
    FolderPopupMenu folderPopup;

    Folders(JComponent mainPanelToBeAdded, MainUI main ,Folder folder, boolean isFolder , String name) throws MessagingException {

        super();

        folderRefrenced = folder;
        setOpaque(true);
        setBackground(Color.white);

        mainPanel = mainPanelToBeAdded;
        if (folder.getType()==Folder.HOLDS_FOLDERS){
            setText(name);
        }else {String htmlPart = "<html> <div ><p>"+name+" &nbsp &nbsp &nbsp  <span style = \"color:blue\"><b>"+folder.getMessageCount()+"</b></span></div></p>";
            setText(htmlPart);
            System.out.println(folder.getMessageCount());}

        if(folder.getType()==Folder.HOLDS_FOLDERS){
            Arrays.asList(folder.list("*")).forEach((a)->{
                try {
                    if (a.getType()== Folder.HOLDS_FOLDERS){
                        sub_folders.put(a,true);
                    }else sub_folders.put(a,false);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });}
        setHorizontalAlignment(SwingConstants.LEFT);

        folderPopup = new FolderPopupMenu(sub_folders,main,mainPanelToBeAdded,this);
        setPreferredSize(new Dimension(mainPanelToBeAdded.getPreferredSize().width-20, 40));
         addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 main.setCurrentFolder(folder.getName());
                 System.out.println(main.getCurrentFolder());
                 try {
                     if (folder!=null&&folder.getType()==Folder.HOLDS_FOLDERS) {
                         JButton source = (JButton) e.getSource();
                         folderPopup.shows(Folders.this, mainPanelToBeAdded, source.getX(), source.getY());
                     }else {
                       loadMessagePageForFolder(main.mainManager,main,main.messagesSpace,main.executors,folder);
                     }
                 } catch (MessagingException ex) {
                     throw new RuntimeException(ex);
                 }
             }
         });

    }

    public  void loadMessagePageForFolder(GmailManager manager,MainUI mainInstance, JComponent messagesSpace, ExecutorService executors, Folder folder){
        SwingWorker<Void,Void> loadFolder = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                String folderName = folder.getName().equalsIgnoreCase("Inbox")?"Inbox":"[Gmail]/"+folder.getName();

                  /** if (messagesSpace.getComponentCount()>0) {
                       return null;
                   }else {
                       mainInstance.lazyLoadEmails(folderName,messagesSpace,mainInstance.messageSpaceLayout);}
                **/

                   mainInstance.lazyLoadEmailsFromFolder(folderName,messagesSpace,mainInstance.messageSpaceLayout,true);


                return null;
            }
        };
        System.out.println("Executing lazyloading" );

        loadFolder.execute();
    }
}
