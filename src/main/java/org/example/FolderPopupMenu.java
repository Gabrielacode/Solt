package org.example;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FolderPopupMenu extends JPopupMenu {
    String[] ideas;
    JComponent mainPanel;
    Folders thisFolder;


    public void shows(Folders fold,JComponent panel, int x, int y) {



        mainPanel = panel;
        int xplace = x;
        int yplace = y + fold.getHeight();
        super.show(panel,xplace,yplace);


    }

    public FolderPopupMenu(HashMap<Folder,Boolean> subfolders, MainUI main,JComponent panel,Folders fold ) {
        super();
        thisFolder = fold;
        this.setPopupSize(new Dimension(panel.getWidth(), (int) (fold.getPreferredSize().height*(subfolders.size()*1.5))));

       subfolders.forEach((a,b)->{
           if (b.equals("true")){
               try {
                   add(new Folders(panel,main,a,true,a.getName()));
               } catch (MessagingException e) {
                   throw new RuntimeException(e);
               }
           }else {
               add(new FolderPopupMenuItem(FolderPopupMenu.this,a.getName()));
           }
       });


}}
