package org.example;

import java.util.Properties;

public enum EmailProperties {
    GMAIL,
    YAHOO,
    OUTLOOK;


    public Properties returnProperties(){
        switch(this ){
            case GMAIL -> {
                Properties gmail = new Properties();
                gmail.setProperty("mail.smtp.host","smtp.gmail.com");
                gmail.setProperty("mail.smtp.port","587");
                gmail.setProperty("mail.smtp.starttls.enable","true");
                gmail.setProperty("mail.smtp.auth","true");
                gmail.setProperty("mail.store.protocol","imap");
                gmail.setProperty("mail.imap.host","imap.gmail.com");
                gmail.setProperty("mail.imap.port","993");
                gmail.setProperty("mail.imap.starttls.enable","true");
                gmail.setProperty("mail.imap.auth","true");

                return gmail;
            }
            case YAHOO -> {
                return new Properties();
            }
            case OUTLOOK -> {
                return new Properties();
            }
        }
        return null;
    }
}
