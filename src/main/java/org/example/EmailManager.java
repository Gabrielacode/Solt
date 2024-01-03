package org.example;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.File;

public interface EmailManager {
 public  void createEmailSession(String user, String password) throws NoSuchProviderException;



 public  void createEmailStoreFromServer() throws MessagingException ;



public void sendTextEmail(String subject, String text, String toReceivers, String ccReceivers, String bccReceivers ) throws MessagingException, InvalidEmailMessageException;

public  void sendTextEmailWithAttachments(String subject, String text, File attachment, String toReceivers, String ccReceivers, String bccReceivers) throws MessagingException, InvalidEmailMessageException;
}




