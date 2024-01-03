package org.example;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class EmailMessageJButton extends JButton{
    private Shape shape;
    private String subject;
    private String recievedDate;
    final static int MAXIMUM_HEIGHT = 40;
    private String sender;
    private int messageIndex;

    public EmailMessageJButton(JComponent mainComponent , Message userMessage) {
        super();

        try {
            this.subject=userMessage.getSubject();
            this.recievedDate= new SimpleDateFormat("yyyy-mm-dd").format(userMessage.getReceivedDate());
            this.sender = InternetAddress.toString(userMessage.getFrom());
            this.messageIndex = userMessage.getMessageNumber();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EmailPage me =new EmailPage(mainComponent,userMessage);





            }
        });

        String emailLabelText = "<html>"+"<p>"+messageIndex+"<span> &nbsp From : </span> "+sender+"<b>"+subject+"</b> <i> ~"+recievedDate+"</i></p></html>";

        setOpaque(true);
         setPreferredSize(new Dimension(mainComponent.getWidth()-20 ,MAXIMUM_HEIGHT));
         setBackground(Color.white);

         setText(emailLabelText);
         setHorizontalAlignment(SwingConstants.LEFT);


         setBorder(BorderFactory.createLineBorder(Color.lightGray,3,true));




/**/
}


}
