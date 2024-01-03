package org.example;

import javax.mail.Message;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MessagePage extends JPanel {
    JComponent mainComponentToBeAdded;
     static int MAXIMUM_AMOUNT_OF_EMAIL_LABELS =11  ;
    static final int MINIMUM_AMOUNT_OF_EMAIL_LABELS  = 1;
    ArrayList<EmailMessageJButton> list_of_labels;

int pageNumber ;

    Message[] messageArray;

JLabel pageNumberLabel;
    int firstEmailIndex;
    int lastEmailIndex;

    public MessagePage(int first, int last, Message[] userMessage, JComponent componentToBeAdded ,int page) {


        list_of_labels = new ArrayList<>();

        pageNumber = page;
        setBackground(new Color(219, 208, 208, 109));
        pageNumberLabel = new JLabel(String.valueOf(pageNumber));
        pageNumberLabel.setPreferredSize(new Dimension(30,10));
        mainComponentToBeAdded = componentToBeAdded;
        setLayout(new FlowLayout(FlowLayout.CENTER,3, (int) 5.5));
        add(pageNumberLabel);


         //Initialize the array from  the user messages

        for (int i =first; i<last ; i++) {
          add(new EmailMessageJButton(mainComponentToBeAdded,userMessage[i]));

        }

        // rightSidePanelScrollPane = new JScrollPane(messagesSpace,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       // rightSidePanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,3));
       // rightSidePanelScrollPane.getVerticalScrollBar().setBackground(Color.black);



    }
    public static  int getMaximumAmountOfEmailLabels(JComponent mainComponentToBeAdded){
        return 11;
    }
}
