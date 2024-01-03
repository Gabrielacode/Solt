package org.example;

import javax.swing.*;

public class FolderPopupMenuItem extends JMenuItem {
    JComponent mainPanel;
    public FolderPopupMenuItem(FolderPopupMenu mainMenu, String item) {
        super(item);
        mainPanel = mainMenu.mainPanel;
        setPreferredSize(mainMenu.getPreferredSize());
    }
}
