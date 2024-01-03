package org.example;


import com.sun.mail.util.BASE64DecoderStream;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.xml.parsers.DocumentBuilder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;

public class EmailPage extends JDialog {

    Message thisMessage;
    JPanel filler;
    String recievedDate;
    JLabel recievedDateLabel;
    JPanel headerPanel;
    GridBagLayout emailPageLayout;
    GridBagConstraints emailPageLayoutConstraints;


    JPanel messagePanel;
    JScrollPane messagePanelScrollPane;
    JEditorPane messageArea;
    String messageText;
    JLabel messageLabel;

    String subject;
    JLabel subjectLabel;

    String sender;
    JLabel senderLabel;

    public EmailPage(JComponent owner, Message mainMessage) {
        super(((JFrame) SwingUtilities.getWindowAncestor(owner)));
        thisMessage = mainMessage;
        ;
        try {
            subject = mainMessage.getSubject();
            recievedDate = mainMessage.getReceivedDate().toString();
            sender = InternetAddress.toString(mainMessage.getFrom());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        /** addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });**/
        emailPageLayout = new GridBagLayout();
        emailPageLayoutConstraints = new GridBagConstraints();



        setBackground(Color.black);
        setSize(700, 600);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
        filler = new JPanel();
        //filler.setBorder(new DropShadowBorder(Color.black, 10, 0.9f, 20, true, true, true, true));
        this.add(filler);
        filler.setLayout(new BoxLayout(filler,BoxLayout.Y_AXIS));


        headerPanel = new JPanel();
        headerPanel.setMaximumSize( new Dimension(695, 100));
        headerPanel.setBorder(new LineBorder(Color.LIGHT_GRAY,5,false));
        headerPanel.setBackground(Color.cyan);
        filler.add(headerPanel);

        senderLabel = new JLabel("From : " + sender);
        emailPageLayoutConstraints.gridx = 0;
        emailPageLayoutConstraints.gridy = 0;
        emailPageLayoutConstraints.anchor = GridBagConstraints.CENTER;
        headerPanel.add(senderLabel, emailPageLayoutConstraints);

        subjectLabel = new JLabel("Subject : " + subject);
        emailPageLayoutConstraints.gridx = 1;
        emailPageLayoutConstraints.gridy = 0;
        emailPageLayoutConstraints.anchor = GridBagConstraints.CENTER;
        headerPanel.add(subjectLabel, emailPageLayoutConstraints);

        recievedDateLabel = new JLabel("Date : " + recievedDate);
        emailPageLayoutConstraints.gridx = 2;
        emailPageLayoutConstraints.gridy = 0;
        emailPageLayoutConstraints.anchor = GridBagConstraints.CENTER;
        headerPanel.add(recievedDateLabel, emailPageLayoutConstraints);



        messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        messagePanel.setPreferredSize(new Dimension());
        messagePanel.setMaximumSize(new Dimension(690, 430));

        messagePanel.setBackground(Color.blue);

        filler.add(messagePanel);
        messageArea = new JEditorPane();

        //messageArea.setMaximumSize(new Dimension(670,420));
        messageArea.setPreferredSize(new Dimension(670,420));
        messageArea.setContentType("text/html");
        messageArea.setText("<html><body id = 'body'></body></html>");

        messagePanelScrollPane = new JScrollPane(messageArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagePanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8,0));
        messagePanelScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,10));
        messagePanelScrollPane.setPreferredSize(new Dimension(680,420));
        messagePanelScrollPane.setMaximumSize(new Dimension(680,420));
        messagePanel.add(messagePanelScrollPane);

        messageArea.setEditable(false);
        messageArea.setAutoscrolls(true);
        messageArea.setBorder(new LineBorder(new Color(12,12,10,123),3,true));

        messageArea.addHyperlinkListener(new HyperlinkListener() {


            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType()== HyperlinkEvent.EventType.ACTIVATED){






                    if (Desktop.isDesktopSupported()){
                    try {
                        if (e.getURL()==null){
                            System.out.println(e.getDescription());
                            MimeBodyPart bodyPartofEvent = (MimeBodyPart) ((Multipart)mainMessage.getContent()).getBodyPart(Integer.parseInt(e.getDescription()));
                            downloadAttachmentfromBodyPart(bodyPartofEvent);



                        }else Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (URISyntaxException ex) {
                        System.out.println("Error");
                    } catch (MessagingException ex) {
                        throw new RuntimeException(ex);
                    } catch (MimeTypeException ex) {
                        throw new RuntimeException(ex);
                    }
                    }}
            }
        });

        //messagePanel.add(messageArea);

       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               try {

                   getContentFromBodyPart(mainMessage);
               } catch (MessagingException e) {
                   throw new RuntimeException(e);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               } catch (BadLocationException e) {
                   throw new RuntimeException(e);
               } catch (MimeTypeException e) {
                   throw new RuntimeException(e);
               }
           }
       });



    }
    public void getContentFromBodyPart (Part e) throws MessagingException, IOException, BadLocationException, MimeTypeException {




            if (e.isMimeType("text/plain")) {
                messageArea.setContentType("text/html");
                HTMLDocument documentMessageArea = (HTMLDocument) messageArea.getDocument();
                Element bodyTag = documentMessageArea.getElement("body");
                String text = "<div><p>" + e.getContent().toString() + "</p></div>";
                documentMessageArea.insertBeforeEnd(bodyTag, text);

            } else if (e.isMimeType("text/html")) {
                Safelist mme = Safelist.relaxed();
                mme.removeTags("html", "body");
                String aa = Jsoup.clean(e.getContent().toString(), mme);

                StringBuilder messageContent = new StringBuilder();
                messageContent.append(aa);
                HTMLDocument documentMessageArea = (HTMLDocument) messageArea.getDocument();
                Element bodyTag = documentMessageArea.getElement("body");
                documentMessageArea.insertBeforeEnd(bodyTag, messageContent.toString());


            } else if (e.isMimeType("image/*")) {
                    BufferedInputStream mmer =  new BufferedInputStream(e.getInputStream());
                    String mimeType = new Tika().detect(mmer);
                    MimeType fileType = MimeTypes.getDefaultMimeTypes().forName(mimeType);
                    String fileExtension = fileType.getExtension();
                    File imageFile;
                    imageFile = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\" + e.getContent().toString() + fileExtension);
                    //   File imageFile = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\"+e.getContent().toString()+fileExtension);
                    BufferedOutputStream mme = new BufferedOutputStream(new FileOutputStream(imageFile));
                    System.out.println("A file has been downloaded2");
                    int aas;
                    while ((aas = mmer.read()) != -1) {
                        mme.write(aas);
                    }
                    mme.flush();
                System.out.println(aas);

                    String imgsrc = imageFile.toURI().toURL().toExternalForm();
                    String imgg = "<img src='" + imgsrc + "' widthField=200height=200></img>";
                    HTMLDocument documentMessageArea = (HTMLDocument) messageArea.getDocument();
                    Element bodyTag = documentMessageArea.getElement("body");
                    documentMessageArea.insertBeforeEnd(bodyTag, imgg);




            } else if (e.isMimeType("message/rfc822")) {
                getContentFromBodyPart((Part) e.getContent());

            }else if (e.getContent() instanceof BASE64DecoderStream) {

                BASE64DecoderStream decoderStream = (BASE64DecoderStream) e.getContent();
                String mimeType = new Tika().detect(decoderStream);


                String fileExtension = "";
                MimeType fileMimetype = MimeTypes.getDefaultMimeTypes().forName(mimeType);
                fileExtension = fileMimetype.getExtension();
                System.out.println("A file has been downloaded 4");

                File attachmentStorageLocation;
                if (e.getFileName() != null) {
                    attachmentStorageLocation = new File("C:\\Users\\gabri\\OneDrive\\Pictures\\Solt attachments\\" + e.getFileName() + fileExtension);
                } else
                    attachmentStorageLocation = new File("C:\\Users\\gabri\\OneDrive\\Pictures\\Solt attachments\\" + e.getContent().toString() + fileExtension);


                FileOutputStream ffre = new FileOutputStream(attachmentStorageLocation);
                int readed;
                while ((readed = decoderStream.read()) != -1) {
                    ffre.write(readed);
                }


            }else if (e.isMimeType("multipart/*")){
                Multipart multipartMessage = (Multipart) e.getContent();
                int noOfBodyPart = multipartMessage.getCount();
                for (int i = 0; i < noOfBodyPart; i++) {
                    MimeBodyPart partOfWhole = (MimeBodyPart) multipartMessage.getBodyPart(i);
                    if (partOfWhole.getDisposition()!=null){
                        System.out.println("Disposition");
                        if (partOfWhole.getDisposition().equalsIgnoreCase(Part.INLINE)){
                            getContentFromBodyPart(partOfWhole);
                            System.out.println("Disposition Inline");
                        }else if (partOfWhole.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)){
                            System.out.println("Disposition Attachment");
                            String downloadLinkFortheAttachment = "<a href = '"+i+"'>"+getSpecificIconForAttachment(partOfWhole)+"</a>";
                            HTMLDocument documentMessageArea = (HTMLDocument) messageArea.getDocument();
                            Element bodyTag = documentMessageArea.getElement("body");
                            documentMessageArea.insertBeforeEnd(bodyTag, downloadLinkFortheAttachment);
                        }
                    }else getContentFromBodyPart(partOfWhole);

                    String lineBreak = "<br>";
                    HTMLDocument documentMessageArea = (HTMLDocument) messageArea.getDocument();
                    Element bodyTag = documentMessageArea.getElement("body");
                    documentMessageArea.insertBeforeEnd(bodyTag, lineBreak);
                }



            } else {
                BufferedInputStream messageInput = new BufferedInputStream(e.getInputStream());
                String mimetypeofMessageContent = new Tika().detect(messageInput);
                System.out.println("A file has been downloaded from the last else");

                MimeType fileMimetype = MimeTypes.getDefaultMimeTypes().forName(mimetypeofMessageContent);
                String fileExtension = fileMimetype.getExtension();
                File attachmentStorage;
                if (e.getFileName() != null) {
                    attachmentStorage = new File("C:\\Users\\gabri\\OneDrive\\Pictures\\Solt attachments\\" + e.getFileName() + fileExtension);
                } else
                    attachmentStorage = new File("C:\\Users\\gabri\\OneDrive\\Pictures\\Solt attachments\\" + e.getContent().toString() + fileExtension);
                BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(attachmentStorage));
                int aas;
                while ((aas = messageInput.read()) != -1) {
                    fileOutputStream.write(aas);
                }
                fileOutputStream.flush();

                }

     }
            public  void downloadAttachmentfromBodyPart (Part e) throws MessagingException, IOException, MimeTypeException {
              SwingWorker<Void,Void> downloadActivity = new SwingWorker<Void, Void>() {
                  @Override
                  protected Void doInBackground() throws Exception {
                      MimeBodyPart ee = (MimeBodyPart) e;
                      BufferedInputStream messageInput = new BufferedInputStream(e.getInputStream());
                      String mimetypeofMessageContent = new Tika().detect(messageInput);
                      System.out.println("A file has been downloaded from the last else");

                      MimeType fileMimetype = MimeTypes.getDefaultMimeTypes().forName(mimetypeofMessageContent);
                      System.out.println(fileMimetype.toString());
                      String fileExtension = fileMimetype.getExtension();
                     String attachmentdownloadPath = null;
                      JFileChooser fileChosser = new JFileChooser();
                      fileChosser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                      if (fileChosser.showOpenDialog(SwingUtilities.getRoot(EmailPage.this))==JFileChooser.APPROVE_OPTION){
                          attachmentdownloadPath = fileChosser.getSelectedFile().getAbsolutePath();
                      }
                      File attachmentStorage = null;


                      if (e.getFileName() != null) {
                          attachmentStorage = new File(attachmentdownloadPath+"\\" + e.getFileName() );
                      } else
                          attachmentStorage = new File(attachmentdownloadPath +"\\"+ e.getContent().toString() + fileExtension);
                      System.out.println();
                      BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(attachmentStorage));
                      int aas;
                      while ((aas = messageInput.read()) != -1) {
                          fileOutputStream.write(aas);
                      }
                      fileOutputStream.flush();

                      return null;
                  }
              };
              downloadActivity.execute();


            }
            public String getSpecificIconForAttachment(Part e) throws MessagingException, IOException, MimeTypeException {
            InputStream partInputStream = e.getInputStream();
            String mimetypeofMessageContent = new Tika().detect(partInputStream);
            MimeTypes groupbelongs = MimeTypes.getDefaultMimeTypes();
            String fileextensions = groupbelongs.forName(mimetypeofMessageContent).getExtension();

            String result = "";

           if (fileextensions.equalsIgnoreCase(".pdf")|groupbelongs.forName(mimetypeofMessageContent).toString().equalsIgnoreCase("application/pdf")){
               File imgsrc = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\Asset 1@3x.png");

               result = "<div style = \"text-align:center\"><img src='" +imgsrc.toURI().toURL().toExternalForm()+ "' widthField=100 heightField=100></img></div>";
               return result;
           }else if (groupbelongs.forName(mimetypeofMessageContent).toString().startsWith("image")){
               File imgsrc = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\Asset 5@3x.png");

               result = "<div style = \"text-align:center\" ><img src='" +imgsrc.toURI().toURL().toExternalForm() + "' widthField=100 heightField=100></img></div>";
               return result;
           }else if (fileextensions.equalsIgnoreCase(".docx")|fileextensions.equalsIgnoreCase(".txt")){
               File imgsrc = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\Asset 1@3x.png");

               result = "<div style = \"text-align:center\"><img src='" + imgsrc.toURI().toURL().toExternalForm() + "' widthField=100 heightField=100></img></div>";
               return result;
           }
           return result;



            }
            }




