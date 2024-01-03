package org.example;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                 try {
                     if (folder!=null&&folder.getType()==Folder.HOLDS_FOLDERS) {
                         JButton source = (JButton) e.getSource();
                         folderPopup.shows(Folders.this, mainPanelToBeAdded, source.getX(), source.getY());
                     }else {
                       loadMessagePageForFolder(main.mainManager,main.messagesSpace,main.executors,folder);
                     }
                 } catch (MessagingException ex) {
                     throw new RuntimeException(ex);
                 }
             }
         });

    }

    public  void loadMessagePageForFolder(GmailManager manager, JComponent messagesSpace, ExecutorService executors, Folder folder){
        SwingWorker<Message[],Void> loadFolder = new SwingWorker<>() {
            @Override
            protected Message[] doInBackground() throws Exception {
                if (folder.getName().equalsIgnoreCase("Inbox")) {
                    return manager.loadMessagesFromInboxFolders(1,20);
                }else return manager.loadMessagesFromAFolder("[Gmail]/"+folder.getName());

            }

            @Override
            protected void done() {
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        messagesSpace.removeAll();
                        ArrayList<MessagePage> messagePages = new ArrayList<>();
                        Message[] result;
                        try {
                            result = get();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }

                        int userMessageLength = result.length;
                        int countOfPages =0;
                        for (int i = userMessageLength; i > 0; i--) {
                            if ((userMessageLength-i)%MessagePage.getMaximumAmountOfEmailLabels(messagesSpace)==0){
                                if (i-11<=0){
                                    countOfPages++;
                                    messagePages.add(new MessagePage(i,0,result,messagesSpace,countOfPages) );

                                }else {
                                    countOfPages++;
                                    messagePages.add(new MessagePage(i, i - MessagePage.getMaximumAmountOfEmailLabels(messagesSpace), result, messagesSpace, countOfPages));

                                }}else continue;


                        }

                        messagePages.forEach((a) -> messagesSpace.add(a));

                    }
                });
            }
        };

        loadFolder.execute();
    }
}
