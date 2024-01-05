package org.example;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public  class GmailManager  implements EmailManager{


    public GmailManager(String user_name, String password) throws MessagingException {
        this.user_name = user_name;
        this.password = password;



    }

    public static  Session  gmail_session ;
    public static EmailProperties gmail_properties = EmailProperties.GMAIL;
   public IMAPStore gmail_store;
    private String user_name ;
    private String password;

    GmailManager only_Manager ;

    public static Session getGmail_session() {
        return gmail_session;
    }

    public static void setGmail_session(Session gmail_session) {
        GmailManager.gmail_session = gmail_session;
    }

    public static EmailProperties getGmail_properties() {
        return gmail_properties;
    }

    public static void setGmail_properties(EmailProperties gmail_properties) {
        GmailManager.gmail_properties = gmail_properties;
    }

    public  IMAPStore getGmail_store() {
        return gmail_store;
    }

    public  void setGmail_store(IMAPStore gmail_store) {
     this.gmail_store = gmail_store;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }






    public  void createEmailSession(String user ,String password) throws NoSuchProviderException {

        gmail_session = Session.getDefaultInstance(gmail_properties.returnProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user,password);
            }
        });


    }



    public  void createEmailStoreFromServer() throws MessagingException {


            gmail_store = (IMAPStore) gmail_session.getStore("imaps");
            gmail_store.connect("imap.gmail.com", user_name,password);
    }


    public ArrayList<Folder> loadFoldersFromSession() throws MessagingException {

       Folder[] gmailStoreFolders =  gmail_store.getDefaultFolder().list("*");
        ArrayList<Folder> filteredgmailStoreFolders = new ArrayList<>();
        Arrays.stream(gmailStoreFolders).forEach((a)->{
            try {
                if (a.exists() ){
                    filteredgmailStoreFolders.add(a);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

         return filteredgmailStoreFolders;
    }

    public Message[] loadMessagesFromInboxFolders(int start , int finish) throws MessagingException {
        Folder user_inbox =  gmail_store.getFolder("Inbox");
        Message[] user_message;
      user_inbox.open(Folder.READ_ONLY);
      if (user_inbox.getMessageCount()>0){
      if(start > finish){
          user_message = user_inbox.getMessages(finish,start);
          Arrays.sort(user_message, Comparator.comparing(Message::getMessageNumber).reversed());
          return user_message;
      }
       user_message = user_inbox.getMessages(start,finish);
        return user_message;}
    return null;
    }


    public Message[] loadMessagesFromAFolder(String folderName, int start, int end) throws MessagingException {
        Folder folder = gmail_store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        Message[] user_message;
        if (folder.getMessageCount()<=0){
            return null;}



        if(start > end){
            user_message = folder.getMessages(end,start);
            Arrays.sort(user_message, Comparator.comparing(Message::getMessageNumber).reversed());
            return user_message;
        }
        user_message = folder.getMessages(start,end);
        return user_message;}




    public String loadTextVersionofMessage(Part e) throws MessagingException, IOException {
        String result = "";
        if (e.isMimeType("text/plain")){
            result = e.getContent().toString();
            return result;
        } else if (e.isMimeType("text/html")) {
            Document ee = Jsoup.parse(e.getContent().toString());
            Elements pTages = ee.getElementsByTag("p");
            for (Element ey:pTages) {
                result+= ey.text();
            }
            return result;
        }else if (e.isMimeType("multipart/*")){
            loadTextVersionofMessage(e);
        }
        return result;
    }

    public void initGmailManager() throws MessagingException {
       String user_namee = user_name;
       String user_password = password;

        createEmailSession(user_namee,user_password);
        createEmailStoreFromServer();
    }
    public  void sendTextEmail(String subject, String text, String toRecievers, String ccRecievers, String bccRecievers) throws MessagingException, InvalidEmailMessageException {
        Message user_message = new MimeMessage(gmail_session);
        user_message.setFrom(new InternetAddress(user_name) );
        if(toRecievers.length()>0){
            String [] toReciepents = toRecievers.split(",");
            InternetAddress [] reciepentTypeTo = new InternetAddress[toReciepents.length];
            for (int i = 0; i < reciepentTypeTo.length; i++) {
                reciepentTypeTo[i] = new InternetAddress(toReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.TO,reciepentTypeTo);

        }else { throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);

        }
        if (ccRecievers.length()>0) {
            String[] ccReciepents = ccRecievers.split(",");
            InternetAddress[] reciepentTypeCc = new InternetAddress[ccReciepents.length];
            for (int i = 0; i < reciepentTypeCc.length; i++) {
                reciepentTypeCc[i] = new InternetAddress(ccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.CC, reciepentTypeCc);
        }
        if (bccRecievers.length()>0) {
            String[] bccReciepents = bccRecievers.split(",");
            InternetAddress[] reciepentTypeBCc = new InternetAddress[bccReciepents.length];
            for (int i = 0; i < reciepentTypeBCc.length; i++) {
                reciepentTypeBCc[i] = new InternetAddress(bccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.BCC, reciepentTypeBCc);
        }
        if (subject.length()>0) {
            user_message.setSubject(subject);
        }else throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);

        user_message.setText(text);
        Transport.send(user_message);

        }


    @Override
    public void sendTextEmailWithAttachments(String subject, String text, File attachment,String toRecievers , String ccRecievers , String bccRecievers) throws MessagingException, InvalidEmailMessageException {

        Message user_message = new MimeMessage(gmail_session);
        user_message.setSubject(subject);
        user_message.setFrom(new InternetAddress(user_name));
        if(toRecievers.length()>0){
            String [] toReciepents = toRecievers.split(",");
            InternetAddress [] reciepentTypeTo = new InternetAddress[toReciepents.length];
            for (int i = 0; i < reciepentTypeTo.length; i++) {
                reciepentTypeTo[i] = new InternetAddress(toReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.TO,reciepentTypeTo);

        }else { throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);

        }
        if (ccRecievers.length()>0) {
            String[] ccReciepents = ccRecievers.split(",");
            InternetAddress[] reciepentTypeCc = new InternetAddress[ccReciepents.length];
            for (int i = 0; i < reciepentTypeCc.length; i++) {
                reciepentTypeCc[i] = new InternetAddress(ccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.CC, reciepentTypeCc);
        }
        if (bccRecievers.length()>0) {
            String[] bccReciepents = bccRecievers.split(",");
            InternetAddress[] reciepentTypeBCc = new InternetAddress[bccReciepents.length];
            for (int i = 0; i < reciepentTypeBCc.length; i++) {
                reciepentTypeBCc[i] = new InternetAddress(bccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.BCC, reciepentTypeBCc);
        }
        if (subject.length()>0) {
            user_message.setSubject(subject);
        }else throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);

        Multipart content_body = new MimeMultipart();
        BodyPart text_part = new MimeBodyPart();
        text_part.setText(text);
        BodyPart attachment_part = new MimeBodyPart();
        DataSource file_attachment = new FileDataSource(attachment);
        attachment_part.setDataHandler(new DataHandler(file_attachment));
        content_body.addBodyPart(text_part);
        content_body.addBodyPart(attachment_part);
        user_message.setContent(content_body);

        Transport.send(user_message);
    }

    public void sendTextEmailWithImage(String subject, String text, File image,String toRecievers, String ccRecievers, String bccRecievers) throws MessagingException, IOException, InvalidEmailMessageException {
        Message user_message = new MimeMessage(gmail_session);
        user_message.setSubject(subject);
        user_message.setFrom(new InternetAddress(user_name));

        if(toRecievers.length()>0){
            String [] toReciepents = toRecievers.split(",");
            InternetAddress [] reciepentTypeTo = new InternetAddress[toReciepents.length];
            for (int i = 0; i < reciepentTypeTo.length; i++) {
                reciepentTypeTo[i] = new InternetAddress(toReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.TO,reciepentTypeTo);

        }else { throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);

        }
        if (ccRecievers.length()>0) {
            String[] ccReciepents = ccRecievers.split(",");
            InternetAddress[] reciepentTypeCc = new InternetAddress[ccReciepents.length];
            for (int i = 0; i < reciepentTypeCc.length; i++) {
                reciepentTypeCc[i] = new InternetAddress(ccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.CC, reciepentTypeCc);
        }
        if (bccRecievers.length()>0) {
            String[] bccReciepents = bccRecievers.split(",");
            InternetAddress[] reciepentTypeBCc = new InternetAddress[bccReciepents.length];
            for (int i = 0; i < reciepentTypeBCc.length; i++) {
                reciepentTypeBCc[i] = new InternetAddress(bccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.BCC, reciepentTypeBCc);
        }
        if (subject.length()>0) {
            user_message.setSubject(subject);
        }else throw new InvalidEmailMessageException(InvalidEmailMessageException.INVALID_INPUT);


        Multipart content_body = new MimeMultipart();
        MimeBodyPart text_part = new MimeBodyPart();
        String full_text = text + "" +
                "<html>" +
                "<body>" +
                "<img src : \" cid:"+ image.getPath()+"\" />" +
                "</body> "+"</html>";
        text_part.setContent(full_text,"text/html");
        content_body.addBodyPart(text_part);
        MimeBodyPart imagepart = new MimeBodyPart();
        imagepart.setContentID(image.getPath());
        imagepart.attachFile(image);
        imagepart.setDisposition(MimeBodyPart.INLINE);
        content_body.addBodyPart(imagepart);
        user_message.setContent(content_body);

        Transport.send(user_message);
    }

    public  void sendTextEmailWithEverythingAndAnything(String subject, String text, File[] attachment, File[] image , String toRecievers, String ccRecievers,String bccRecievers) throws MessagingException, InvalidEmailMessageException {
        Message user_message = new MimeMessage(gmail_session);
        user_message.setSubject(subject);
        user_message.setFrom(new InternetAddress(user_name));
        if(toRecievers!=null&toRecievers.length()>0){
            String [] toReciepents = toRecievers.split(",");
            InternetAddress [] reciepentTypeTo = new InternetAddress[toReciepents.length];
            for (int i = 0; i < reciepentTypeTo.length; i++) {
                reciepentTypeTo[i] = new InternetAddress(toReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.TO,reciepentTypeTo);

        }else { throw new InvalidEmailMessageException("To is empty");
        }
        if (ccRecievers!=null&&ccRecievers.length()>0 ) {
            String[] ccReciepents = ccRecievers.split(",");
            InternetAddress[] reciepentTypeCc = new InternetAddress[ccReciepents.length];
            for (int i = 0; i < reciepentTypeCc.length; i++) {
                reciepentTypeCc[i] = new InternetAddress(ccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.CC, reciepentTypeCc);
        }
        if (bccRecievers!=null&&bccRecievers.length()>0 ) {
            String[] bccReciepents = bccRecievers.split(",");
            InternetAddress[] reciepentTypeBCc = new InternetAddress[bccReciepents.length];
            for (int i = 0; i < reciepentTypeBCc.length; i++) {
                reciepentTypeBCc[i] = new InternetAddress(bccReciepents[i]);
            }
            user_message.addRecipients(Message.RecipientType.BCC, reciepentTypeBCc);
        }
        if (subject.length()>0) {
            user_message.setSubject(subject);
        }else throw new InvalidEmailMessageException("Subject is empty");



        Multipart content_body = new MimeMultipart();
        MimeBodyPart text_part = new MimeBodyPart();
        if (text.length()>0) {
            text_part.setContent(text,"text/html");
            content_body.addBodyPart(text_part);
        }else {text_part.setContent("Error","text/html");}


        if (attachment !=null){
        for (File attach : attachment){
        if (attach!=null&&attach.exists()) {
            BodyPart attachment_part = new MimeBodyPart();
            attachment_part.setDisposition(BodyPart.ATTACHMENT);
            DataSource file_attachment = new FileDataSource(attach);
            attachment_part.setDataHandler(new DataHandler(file_attachment));

            content_body.addBodyPart(attachment_part);

        }}}
        if (image!=null){
        for ( File images: image){
        if (images!=null&&images.exists()){
            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource imageDataSource = new FileDataSource(images);
            imagePart.setDataHandler(new DataHandler(imageDataSource));
            String imageString = images.getName();
            imagePart.setContentID("<"+imageString.replaceAll("\\s","")+">");
            imagePart.setDisposition(Part.INLINE);
            content_body.addBodyPart(imagePart);

        }}}


        user_message.setContent(content_body);

        Transport.send(user_message);
    }


    public static void main(String[] args) {

}}

