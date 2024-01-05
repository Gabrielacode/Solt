
// Bardane Cardone Carridge Chalsyn Chitosa Chusarky Constanta Contaria Buke,

// the three panels need a particular thread to handle it
package org.example;

import org.jdesktop.swingx.JXLabel;


import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;



public class MainUI extends JFrame {

    Runnable frameColorAnimation;
      int rNow,rAfter;
      int gNow,gAfter;
      int bNow,bAfter;


    Random randomR, randomG,randomB;


    GmailManager mainManager ;
    // countOfEmailMessagePages tells us how much email message  pages are there
        int countOfEmailMessagePages ;
        //This states the current Folder
        String currentFolder;

        // This tells the main that a MessagePage from a Folder is already loading
    boolean isaMessagePageLoading;
    JPanel main ;

    GridBagLayout loginPanelGridBagLayout;
    GridBagConstraints loginPanelGridBagConstraints;
    JPanel leftSidePanel;


    ArrayList<Folder>  gmail_folders_List;
    JPanel folderSpace ;
    JScrollPane folderScrollPane;
    JTextField folderDescription;
    JButton folderButton;
    JPanel mainSidePanel;
    CardLayout mainSidePanelCardLayout;

    JPanel loginPanelInMainSidePanel;


    Runnable loadMessagesAfterLogin;

   SwingWorker<Void,Void> loginAccessOperation  ;
    JLabel loginLabel;
    RoundedJTextField loginEmailAddress;
    RoundedJPasswordField loginPassword;
    JLabel extraLoginInformation;



    JPanel mainPanelLoadingPanel;



    JLabel loadingInformation;

 Runnable loadingAnimation;
    JPanel mainPanelMessagePanel;
    JPanel headerBarMessagePanel;

    JButton messageSpaceBackButton;
    JButton messageSpaceFrontButton;
    JButton removeAllButton;
    JXLabel loadingMessagePageStatus;
    JPanel messagesSpace;
    // The RXCardLayout class  is from https://github.com/tips4java/tips4java/blob/main/source/RXCardLayout.java. You can star his repo
    RXCardLayout messageSpaceLayout;
    ArrayList<MessagePage> messagePages;


    Message[] userMessage;

    JPanel rightSidePanel;
    JPanel rightSideInformationPanel;
    JScrollPane rightSidePanelScrollPane;

    JLabel rightSideHeader;
    JLabel reciepentsLabel;
    JTextField reciepents;
    JLabel ccReciepentsLabel;
    JTextField ccReciepents;
    JLabel bccReciepentsLabel;
    JTextField bccReciepents;
    JLabel subjectLabel;
    JTextField user_subject;
    JLabel messageLabel;

    JTextArea messageArea;
    JScrollPane messageAreaScroller;

    JButton sendButton;
    Image sendImage;

    JButton attachmentButton;
    Image attachment_Image ;

   ArrayList<File> attachments ;

    JButton imageButton;
    Image imageButtonImage;

    ArrayList<File> images;
    JTextArea messageNotification;

    SwingWorker<Void,Void> sendMessageOperation;


    GridBagLayout rightPanelLayout;
    GridBagConstraints rightPanelConstraints;



    JPanel buttonsPanel;
    JPanel messageNotificationPanel;




    ExecutorService executors = Executors.newCachedThreadPool();

    public MainUI() throws MessagingException {

        //Set the GridBagLayout
        loginPanelGridBagLayout = new GridBagLayout();
        loginPanelGridBagConstraints = new GridBagConstraints();

        //Set the loadingAnyMessagePage boolean to false
        isaMessagePageLoading = false;



        setTitle("Solt");
        getContentPane().setBackground(Color.white);


        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       // setMaximumSize(new Dimension(1200,680));
        //setMinimumSize(new Dimension(1100,650));
        setSize(new Dimension(1280,650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        //Setting variables for the main frame color animation
          rNow =0; rAfter= 0;
         gNow =0; gAfter =0;
        bNow =0; bAfter =0;

        randomR =new Random(new Date().getTime());
        randomG = new Random(new Date().getTime());
        randomB =new Random(new Date().getTime());

        rAfter = Math.abs(randomR.nextInt(0,255));
        gAfter = Math.abs(randomG.nextInt(0,255));
        bAfter = Math.abs(randomB.nextInt(0,255));


        frameColorAnimation = () -> {
            while (true) {
                try {
                    colorAnimation(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           };


// Register New Fonts

        GraphicsEnvironment gee = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            gee.registerFont(Font.createFont(Font.TRUETYPE_FONT,new File("C:\\Users\\gabri\\OneDrive\\Documents\\Fonts\\Poiret_One\\PoiretOne-Regular.ttf")));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
        //Creation of the Gmail Manager
        images = new ArrayList<>();
        attachments  = new ArrayList<>();


        // Creation of Main Panel
        main = new JPanel();
        //main.setPreferredSize(new Dimension(800,400));
        mainManager = new GmailManager("....","....");
        main.setBackground(Color.BLACK);
        add(main,BorderLayout.CENTER);
        main.setBorder(BorderFactory.createLineBorder(Color.black,2));
        main.setLayout(new FlowLayout(FlowLayout.LEFT));

        executors.execute(frameColorAnimation);

        //Creation of leftside panel
        leftSidePanel = new JPanel();
        leftSidePanel.setPreferredSize(new Dimension(300,590));
        leftSidePanel.setBackground(new Color(0,0,0,70));
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel,BoxLayout.Y_AXIS));

        folderSpace = new JPanel();
        folderSpace.setPreferredSize(new Dimension(leftSidePanel.getPreferredSize().width,100000));

        folderSpace.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        folderScrollPane = new JScrollPane(folderSpace, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        folderScrollPane.setWheelScrollingEnabled(true);
        folderScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        folderScrollPane.getVerticalScrollBar().setBackground(Color.white);

        folderScrollPane.setAlignmentX(0.5f);
        leftSidePanel.add(folderScrollPane);

        folderDescription = new JTextField(20);


        folderDescription.setAlignmentX(0.5f);
        leftSidePanel.add(folderDescription);

        folderButton = new JButton("Submit");
        folderButton.setAlignmentX(0.5f);
        folderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!folderDescription.getText().isEmpty()){
                            //folderSpace.add(new Folders(leftSidePanel,false,folderDescription.getText()));
                        }else {JOptionPane.showMessageDialog(leftSidePanel,"Input your Folder Name","Error",JOptionPane.ERROR_MESSAGE);}

                    }
                });


            }
        });
        leftSidePanel.add(folderButton);



        main.add(leftSidePanel);
        Arrays.stream(leftSidePanel.getComponents()).forEach((a)->{a.setVisible(false);});




        mainSidePanel = new JPanel();
        mainSidePanel.setPreferredSize(new Dimension(637,590));
        mainSidePanel.setBackground(Color.white);
        mainSidePanelCardLayout = new CardLayout();
        mainSidePanel.setLayout(mainSidePanelCardLayout);

        main.add(mainSidePanel);


    loginAccessOperation = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            try {

                //At the start of the program we load the first three pages of emails

                mainManager.initGmailManager();
                gmail_folders_List = mainManager.loadFoldersFromSession();
                //userMessage = mainManager.loadMessagesFromInboxFolders(mainManager.gmail_store.getFolder("Inbox").getMessageCount(),mainManager.gmail_store.getFolder("Inbox").getMessageCount()-((3*MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS)));
                currentFolder = mainManager.gmail_store.getFolder("Inbox").getName();
                lazyLoadEmails(currentFolder,messagesSpace,messageSpaceLayout);


            } catch (MessagingException ex) {



            }

            return null;
        }

        @Override
        protected void done() {

        executors.execute(new Runnable() {
               @Override
             public void run() {


                   gmail_folders_List.forEach((a)->{
                       try {
                           if(!a.getName().equalsIgnoreCase("[Gmail]")){
                           folderSpace.add(new Folders(folderSpace,MainUI.this,a,true,a.getName()));}
                           System.out.println(a);
                           System.out.println(a.getName());
                       } catch (MessagingException e) {
                           System.out.println(a.getName());
                           throw new RuntimeException(e);
                       }
                   });

                   mainSidePanelCardLayout.show(mainSidePanel, "Message");

                   Arrays.stream(leftSidePanel.getComponents()).forEach((a) -> {
                        a.setVisible(true);
                    });
                    Arrays.stream(rightSidePanel.getComponents()).forEach((a) -> {
                        a.setVisible(true);
                    });
                }

            });


        }};



        //Creation of Login Panel in Main Panel

            loginPanelInMainSidePanel = new JPanel();
            //loginPanelInMainSidePanel.setPreferredSize(new Dimension(300, 300));
            loginPanelInMainSidePanel.setBackground(new Color(90,90,90,70));
            loginPanelInMainSidePanel.setLayout(loginPanelGridBagLayout);
            mainSidePanel.add(loginPanelInMainSidePanel,"Login Page");





            loginLabel = new JLabel("SOLT");


            loginLabel.setFont(new Font("FTP - Bardane", Font.ROMAN_BASELINE, 35));
            loginLabel.setForeground(new Color(255,255,255,220));
            loginLabel.setToolTipText("Click to submit");
            loginLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                 if (loginEmailAddress.getText().length()>10 & loginPassword.getPassword().length!=0){
                   mainSidePanelCardLayout.show(mainSidePanel,"Loading");
                    loadingAnimation = new Runnable() {
                        @Override
                        public void run () {
                           while (true) {
                                 if (loginAccessOperation.getState().equals(SwingWorker.StateValue.DONE)) break;
                                 for (String s : Arrays.asList("Loading.", "Loading..", "Loading...", "Loading....")) {
                                     loadingInformation.setText(s);
                                     try {
                                         Thread.sleep(1000);
                                     } catch (InterruptedException e) {
                                         e.printStackTrace();
                                     }

                                 }
                             }

                        }};
                    executors.execute(loadingAnimation);
                      try {
                          mainManager = new GmailManager(loginEmailAddress.getText(),String.valueOf(loginPassword.getPassword()));
                      } catch (MessagingException ex) {
                          ex.printStackTrace();
                      }


                      loginAccessOperation.execute();




                       }

            }});




            loginPanelGridBagConstraints.gridx = 2;
            loginPanelGridBagConstraints.gridy = 1;
           // loginPanelGridBagConstraints.gridheight = 1;
            loginPanelGridBagConstraints.insets = new Insets(1, 1, 70, 1);

            loginPanelInMainSidePanel.add(loginLabel, loginPanelGridBagConstraints);


            loginEmailAddress = new RoundedJTextField(25);


            loginEmailAddress.setForeground(new Color(20,20,20,215));
            loginEmailAddress.setBackground(new Color(255,255,255,220));


            loginEmailAddress.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (loginEmailAddress.getText().equals("@ :")) {
                        loginEmailAddress.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (loginEmailAddress.getText().equals("")){
                       loginEmailAddress.setText("@ :");
                        loginEmailAddress.setFont(new Font("Arial",Font.PLAIN,12));
                    }else{
                        try {
                            new InternetAddress(loginEmailAddress.getText()).validate();

                            loginEmailAddress.setForeground(Color.green);
                            loginEmailAddress.setFont(new Font("Sans Serif",Font.PLAIN,12));

                            loginEmailAddress.setBorder(BorderFactory.createLineBorder(Color.green,2,true));
                        } catch (AddressException ex) {
                            loginEmailAddress.setForeground(Color.red);
                            loginEmailAddress.setFont(new Font("Arial",Font.PLAIN,12));
                        }
                    }
                }
            });



            loginPanelGridBagConstraints.gridx = 2;
            loginPanelGridBagConstraints.gridy = 2;
            loginPanelGridBagConstraints.ipady = 8;
            loginPanelGridBagConstraints.insets = new Insets(3, 1, 8, 1);

            loginPanelInMainSidePanel.add(loginEmailAddress, loginPanelGridBagConstraints);

            loginPassword = new RoundedJPasswordField(25);

            //loginPassword.setOpaque(false);
        loginPassword.setForeground(new Color(20,20,20,215));
        loginPassword.setBackground(new Color(255,255,255,220));


            loginPassword.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (loginPassword.getPassword().equals("...")) {
                        loginPassword.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (loginPassword.getText().equals("")){
                        loginPassword.setText("...");
                       loginPassword.setFont(new Font("Arial",Font.PLAIN,12));
                    }
                }

            });

            loginPanelGridBagConstraints.gridx = 2;
            loginPanelGridBagConstraints.gridy = 3;
            loginPanelGridBagConstraints.ipady = 8;
            loginPanelGridBagConstraints.insets = new Insets(3, 1, 30, 1);


            loginPanelInMainSidePanel.add(loginPassword, loginPanelGridBagConstraints);

            String informationAboutGmail = "Use your Gmail App Password to Login";
            extraLoginInformation = new JLabel(informationAboutGmail);
            extraLoginInformation.setForeground(new Color(255,255,255,220));
            extraLoginInformation.setFont(new Font("FTP - Constata", Font.PLAIN, 12));
            loginPanelGridBagConstraints.gridx = 2;
            loginPanelGridBagConstraints.gridy = 4;
            loginPanelGridBagConstraints.insets = new Insets(4, 1, 1, 1);
            loginPanelInMainSidePanel.add(extraLoginInformation, loginPanelGridBagConstraints);




        //Making loading page for the main panel
        mainPanelLoadingPanel = new JPanel();
        mainPanelLoadingPanel.setLayout(loginPanelGridBagLayout);
        mainSidePanel.add(mainPanelLoadingPanel,"Loading");

        loadingInformation = new JLabel("Loading...");
        loginPanelGridBagConstraints.gridx=2;
        loginPanelGridBagConstraints.gridy = 2;
        loginPanelGridBagConstraints.insets= new Insets(0,5,5,20);
        mainPanelLoadingPanel.add(loadingInformation,loginPanelGridBagConstraints);


        //Creation of Main Message Panel
         mainPanelMessagePanel = new JPanel();
       // mainPanelMessagePanel.setBackground(Color.BLUE);
        mainPanelMessagePanel.setLayout(new BorderLayout());

       mainSidePanel.add(mainPanelMessagePanel,"Message");

       headerBarMessagePanel = new JPanel();
       headerBarMessagePanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
       headerBarMessagePanel.setLayout(new FlowLayout(FlowLayout.LEADING,5,1));
       headerBarMessagePanel.setPreferredSize(new Dimension(mainPanelMessagePanel.getWidth(),50));
       mainPanelMessagePanel.add(headerBarMessagePanel,BorderLayout.NORTH);


       messageSpaceBackButton = new JButton("Back");
       messageSpaceBackButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              if (Integer.parseInt(messageSpaceLayout.getCurrentCard().getName())<=1){
                  // Do Color Change
              }else messageSpaceLayout.previous(messagesSpace);
           }
       });
        headerBarMessagePanel.add(messageSpaceBackButton);
        messageSpaceFrontButton = new JButton("Forward");
        messageSpaceFrontButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                lazyLoadEmails(getCurrentFolder(),messagesSpace,messageSpaceLayout);

            }
        });
        headerBarMessagePanel.add(messageSpaceFrontButton);

        removeAllButton = new JButton("Remove All");
        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
 rightSidePanel.repaint();
 rightSidePanel.revalidate();
            }
        });
        headerBarMessagePanel.add(removeAllButton);

        loadingMessagePageStatus  = new JXLabel("No Message Page Loading");
        headerBarMessagePanel.add(loadingMessagePageStatus);

       messageSpaceLayout = new RXCardLayout();

       messagesSpace = new JPanel();
       messagesSpace.setBackground(new Color(0,0,0,11));
        messagesSpace.setLayout(messageSpaceLayout);
        mainPanelMessagePanel.add(messagesSpace, BorderLayout.CENTER);


        messagePages = new ArrayList<>();



            rightSidePanel = new JPanel();
        rightPanelLayout = new GridBagLayout();
        rightPanelConstraints = new GridBagConstraints();

        rightSidePanel.setPreferredSize(new Dimension(300,590));
        rightSidePanel.setBackground(new Color(0,0,0,70));

        rightSidePanel.setLayout(new BorderLayout());


        main.add(rightSidePanel);

        rightSideInformationPanel = new JPanel();
        rightSideInformationPanel.setLayout(rightPanelLayout);
        rightSidePanel.add(rightSideInformationPanel,BorderLayout.CENTER);



        //RightSide Panel is for sending messages for now

        //We will need a textfield for reciever email address for cc bcc and to ,subject
        rightSideHeader = new JLabel("New Message",SwingConstants.CENTER);
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =0;
        rightPanelConstraints.insets = new Insets(2,5,20,5);
        rightSideInformationPanel.add(rightSideHeader, rightPanelConstraints);

        reciepentsLabel = new JLabel("TO Reciepents");
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =1;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(reciepentsLabel, rightPanelConstraints);

        reciepents = new JTextField(30);

        reciepents.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));


    /**    reciepents.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (reciepents.getText().equalsIgnoreCase("TO :")){
               reciepents.setText("");
               if (!messageNotification.getText().equalsIgnoreCase("Use ',' to seperate different emails"))
               messageNotification.setText("Use ',' to seperate different emails ");
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (reciepents.getText().equals("")) {
                    reciepents.setText("TO :");
                    reciepents.setForeground(Color.lightGray);
                    reciepents.setFont(new Font("Arial", Font.PLAIN, 12));

                }else{
                    try {
                        new InternetAddress(reciepents.getText()).validate();

                       reciepents.setForeground(Color.green);
                        reciepents.setFont(new Font("Sans Serif",Font.PLAIN,12));
                        reciepents.setBorder(BorderFactory.createLineBorder(Color.green,2,true));
                    } catch (AddressException ex) {
                        reciepents.setForeground(Color.red);
                        reciepents.setFont(new Font("Arial",Font.PLAIN,12));
                    }
                }
            }
        });**/

        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy = 2;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(reciepents, rightPanelConstraints);

       ccReciepentsLabel = new JLabel("CC Reciepents");
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =3;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(ccReciepentsLabel, rightPanelConstraints);


        ccReciepents = new JTextField(30);

        ccReciepents.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        /**ccReciepents.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ccReciepents.getText().equals("CC :")) {
                    ccReciepents.setText("");
                    if (!messageNotification.getText().equalsIgnoreCase("Use ',' to seperate different emails"))
                        messageNotification.setText("Use ',' to seperate different emails");


                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ccReciepents.getText().equals("")){
               ccReciepents.setText("CC :");
               ccReciepents.setForeground(Color.lightGray);
               ccReciepents.setFont(new Font("Arial",Font.PLAIN,12));
            }else{
                    try {
                        new InternetAddress(ccReciepents.getText()).validate();

                        ccReciepents.setForeground(Color.green);
                        ccReciepents.setFont(new Font("Sans Serif",Font.PLAIN,12));

                        ccReciepents.setBorder(BorderFactory.createLineBorder(Color.green,2,true));
                    } catch (AddressException ex) {
                        ccReciepents.setForeground(Color.red);
                        ccReciepents.setFont(new Font("Arial",Font.PLAIN,12));
                    }
                }
            }
        });**/
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =4;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(ccReciepents, rightPanelConstraints);

        bccReciepentsLabel = new JLabel("BCC Reciepents");
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =5;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(bccReciepentsLabel, rightPanelConstraints);

        bccReciepents = new JTextField(30);
        bccReciepents.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        /*bccReciepents.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bccReciepents.getText().equals("BCC :")){
                bccReciepents.setText("");
                    if (!messageNotification.getText().equalsIgnoreCase("Use ',' to seperate different emails"))
                        messageNotification.setText("Use ',' to seperate different emails");

                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (bccReciepents.getText().equals("")) {
                    bccReciepents.setText("BCC :");
                    bccReciepents.setForeground(Color.lightGray);
                    bccReciepents.setFont(new Font("Arial", Font.PLAIN, 12));
                }else{
                    try {
                        new InternetAddress(ccReciepents.getText()).validate();

                        bccReciepents.setForeground(Color.green);
                        bccReciepents.setFont(new Font("Sans Serif",Font.PLAIN,12));

                        bccReciepents.setBorder(BorderFactory.createLineBorder(Color.green,2,true));
                    } catch (AddressException ex) {
                        bccReciepents.setForeground(Color.red);
                        bccReciepents.setFont(new Font("Arial",Font.PLAIN,12));
                    }
                }
            }

        });*/

        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =6;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(bccReciepents, rightPanelConstraints);


        subjectLabel = new JLabel("Subject of the Message");
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =7;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(subjectLabel, rightPanelConstraints);

        user_subject = new JTextField(30);
        user_subject.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
     /*user_subject.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (user_subject.getText().equals("SUBJECT :")){
                user_subject.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

                if (user_subject.getText().equals("")) {
                    user_subject.setText("SUBJECT :");
                    user_subject.setForeground(Color.lightGray);
                    user_subject.setFont(new Font("Sans Serif", Font.PLAIN, 12));
                }
            }
        });*/
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =8;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(user_subject, rightPanelConstraints);


        messageLabel = new JLabel("Message Content");
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =9;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(messageLabel, rightPanelConstraints);

        messageArea = new JTextArea();
        messageArea.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        messageArea.setLineWrap(true);
       // messageArea.setWrapStyleWord(true);
        messageArea.setAutoscrolls(true);


        messageArea.setComponentPopupMenu(new MessageSenderPopup(rightSidePanel,messageArea.getDocument(),images,attachments));
        messageAreaScroller = new JScrollPane(messageArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageAreaScroller.setPreferredSize(new Dimension(272,250));
        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =10;
        //rightPanelConstraints.ipadx = 100;
        //rightPanelConstraints.ipady = 100;
        rightPanelConstraints.insets = new Insets(2,5,5,5);
        rightSideInformationPanel.add(messageAreaScroller,rightPanelConstraints);



        buttonsPanel = new JPanel(new FlowLayout());
        //buttonsPanel.setBackground(Color.blue);

        rightPanelConstraints.gridx =2;
        rightPanelConstraints.gridy =11;
        rightPanelConstraints.insets = new Insets(5,5,2,5);
        rightSideInformationPanel.add(buttonsPanel,rightPanelConstraints);

        sendButton = new JButton();
        sendButton.setBackground(Color.white);
        sendButton.setPreferredSize(new Dimension(60,30));
        sendImage = new ImageIcon("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\send (1).png").getImage();

        ImageIcon finalSendIcon = new ImageIcon( sendImage.getScaledInstance(30,20,Image.SCALE_SMOOTH));
       sendButton.setIcon(finalSendIcon);
        sendButton.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Optional<String> recievers = Optional.ofNullable(reciepents.getText());
                String recieverss  = recievers.orElse(null);

                Optional<String> ccreceivers = Optional.ofNullable(ccReciepents.getText());
                String ccreceiverss = ccreceivers.orElse(null);

                Optional<String> bccreceivers = Optional.ofNullable(bccReciepents.getText());
                String bccrecieverss = bccreceivers.orElse(null);

                Optional<String> subject = Optional.ofNullable(user_subject.getText());
                String subjects  = subject.orElse(null);

                Optional<String> text = Optional.ofNullable(messageArea.getText());
                String texts  = text.orElse(null);



                sendMessageOperation = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        System.out.println(images + "In Main Ui");
                        File[]immages = new File[images.size()];
                        immages = images.toArray(immages);

                        System.out.println(attachments + "In Main Ui");
                        File [] attachh = new File[attachments.size()];
                        attachh = attachments.toArray(attachh);
                        Arrays.stream(attachh).forEach(System.out::println);

                        try {
                            mainManager.sendTextEmailWithEverythingAndAnything(subjects,texts,attachh,immages,recieverss,ccreceiverss,bccrecieverss);
                        } catch (MessagingException ex) {
                            throw new RuntimeException(ex);
                        } catch (InvalidEmailMessageException ex) {
                            if (ex.getMessage().equals(InvalidEmailMessageException.INVALID_INPUT)){
                                reciepents.setForeground(Color.red);
                                user_subject.setForeground(Color.red);
                            }
                            //throw new RuntimeException(ex);

                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        System.out.println("Message Sent");
                        attachments.clear();
                        images.clear();

                    }
                };
                sendMessageOperation.execute();



            }
        });
        buttonsPanel.add(sendButton);

        attachmentButton = new JButton();
        attachmentButton.setPreferredSize(  new Dimension(60,30));
        attachmentButton.setBackground(Color.white);
        attachmentButton.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        attachment_Image = new ImageIcon("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\paper-clip.png").getImage();

        ImageIcon finalAttachmentIcon = new ImageIcon( attachment_Image.getScaledInstance(30,20,Image.SCALE_SMOOTH));
        attachmentButton.setIcon(finalAttachmentIcon);



        attachmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               MessageAttachmentSendDialog mme = new MessageAttachmentSendDialog((Frame) SwingUtilities.getWindowAncestor(rightSidePanel),messageArea.getDocument(),attachments);


            }
        });
        buttonsPanel.add(attachmentButton);







        imageButton = new JButton();
        imageButton.setBorder(BorderFactory.createLineBorder(Color.lightGray,2,true));
        imageButton.setPreferredSize(  new Dimension(60,30));
        imageButtonImage = new ImageIcon("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\image (1).png").getImage();
        imageButton.setBackground(Color.white);


        ImageIcon finalImageIcon = new ImageIcon( imageButtonImage.getScaledInstance(30,20,Image.SCALE_SMOOTH));
        imageButton.setIcon(finalImageIcon);

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageImageSendDialog mme = new MessageImageSendDialog((Frame) SwingUtilities.getWindowAncestor(rightSidePanel),messageArea.getDocument(),images);
            }
        });
        buttonsPanel.add(imageButton);

        messageNotificationPanel = new JPanel();
        messageNotificationPanel.setLayout(new BorderLayout());


        rightSidePanelScrollPane = new JScrollPane(messageNotificationPanel);
        rightSidePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightSidePanelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        rightSidePanelScrollPane.setSize(rightSidePanel.getPreferredSize().width-5,500);


        rightSidePanel.add(rightSidePanelScrollPane,BorderLayout.SOUTH);
        messageNotification = new JTextArea("Welcome");
        messageNotification.setEditable(false);
        messageNotification.setLineWrap(true);
        messageNotification.setWrapStyleWord(true);
        messageNotification.setOpaque(false);
        messageNotification.setBorder(BorderFactory.createEmptyBorder());
        messageNotification.setFont(UIManager.getFont("Label.font"));

        messageNotificationPanel.add(messageNotification, BorderLayout.CENTER);

       Arrays.stream(rightSidePanel.getComponents()).forEach((a)->{a.setVisible(false);});


    }


    public void colorAnimation(boolean coloredMode) throws InterruptedException {
       if (coloredMode) {
           rAfter = randomR.nextInt(0, 255);
           gAfter = randomR.nextInt(0, 255);
           bAfter = randomR.nextInt(0, 255);
       } else {
           rAfter = randomR.nextInt(0, 255);
           gAfter = randomG.nextInt(0, 255);
           bAfter = randomB.nextInt(0, 255);
       }

        while ((rNow!=rAfter)&(gNow!=gAfter)&(bNow!=bAfter)){

      main.setBackground(new Color(rNow,gNow,bNow));
      Thread.sleep(60);

      if (rNow<rAfter) {
          rNow++;
      }else if (rNow>rAfter) rNow--; else if (rNow==rAfter) rNow = rNow;

      if (gNow<gAfter) {
          gNow++;
      }else if (gNow>rAfter) gNow--; else if (gNow==gAfter) gNow = gNow;

      if (bNow<bAfter) {
          bNow++;
      }else if (bNow>bAfter) bNow--; else if (bNow==bAfter) bNow = bNow;

      }
  }
  public  void insertFolderIntoFolderList(JTree tree, String newNodeDescription,DefaultMutableTreeNode parentNode){

      DefaultTreeModel tr = (DefaultTreeModel) tree.getModel();
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeDescription);
      tr.insertNodeInto( newNode, (MutableTreeNode) tr.getRoot(),tr.getChildCount(tr.getRoot()));

  }
  // This method allows you  to lazy load emails until the user is in a page before the email
  public  void lazyLoadEmails ( String nameOfCurrentFolder , JComponent componentToAddTo, RXCardLayout cardLayoutMain){
      if (isaMessagePageLoading){
          System.out.println("A MessagePage is already loading");
          return;

      }else isaMessagePageLoading =true;
        SwingWorker<MessagePage,Void> lazyLoader = new SwingWorker<MessagePage, Void>() {
            @Override
            protected MessagePage doInBackground() throws Exception {
               if (componentToAddTo.getComponentCount()<=0){
                   loadingMessagePageStatus.setText("Loading "+ currentFolder + ", MessagePage : "+ 1);
                   //If there is no MessagePage on MessageSpace load the first page from the folder
                   int start = mainManager.gmail_store.getFolder(nameOfCurrentFolder).getMessageCount();
                   if (start<1) {
                       this.cancel(true);
                   }
                   int end = start - MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS;
                   if (end <=1){
                       end =1;
                   }else  end = start -11;
                   Message [] messages = mainManager.loadMessagesFromAFolder(nameOfCurrentFolder,start,end);

                   if (start>end)  return  new MessagePage(0,start-end,messages,componentToAddTo,1);
                   return new MessagePage(0,end-start,messages,componentToAddTo,1);
               }else{
                   if (cardLayoutMain.isNextCardAvailable()){
                       return null;
                   }else {
                       // Get the index of the current MessagePage and makes a new MessagePage out of its values
                       // The index of the current MessagePage is stored in the name of the Component
                       int indexofCurrentPage = Integer.parseInt(cardLayoutMain.getCurrentCard().getName());
                       loadingMessagePageStatus .setText(currentFolder + " , Message Page : "+ (indexofCurrentPage+1));
                       int start = ((mainManager.gmail_store.getFolder(nameOfCurrentFolder).getMessageCount()) - (indexofCurrentPage*MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS));

                       if (start<1) {
                           this.cancel(true);
                       }
                       int end = start -11;
                       if (end <=1) {
                           end = 1;
                       }

                       Message [] messages = mainManager.loadMessagesFromAFolder(nameOfCurrentFolder,start,end);

                       if (start>end)  return  new MessagePage(0,start-end,messages,componentToAddTo,indexofCurrentPage+1);
                       return new MessagePage(0,end-start,messages,componentToAddTo,indexofCurrentPage+1);


                   }
               }

            }

            @Override
            protected void done() {
                try {
                    MessagePage product = get();
                    if (product!=null){
                    componentToAddTo.add(get());}

                    System.out.println("Done");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }catch (CancellationException e){
                    SwingUtilities.invokeLater(()->loadingMessagePageStatus.setText("Page  Not Available"));
                }
                if (cardLayoutMain.isNextCardAvailable()){
                    cardLayoutMain.next(componentToAddTo);
                }
                isaMessagePageLoading = false;
                loadingMessagePageStatus.setText("No Message Page Loading");
            }
        };

        lazyLoader.execute();

  }
    public  void lazyLoadEmailsFromFolder ( String nameOfCurrentFolder , JComponent componentToAddTo, RXCardLayout cardLayoutMain, boolean loadingFolder){
        if (isaMessagePageLoading){
            System.out.println("A MessagePage is already loading");
            return;

        }else isaMessagePageLoading =true;
        componentToAddTo.removeAll();
        loadingMessagePageStatus.setText("Loading "+ currentFolder + ", MessagePage : "+ 1);
        SwingWorker<MessagePage,Void> lazyLoader = new SwingWorker<MessagePage, Void>() {
            @Override
            protected MessagePage doInBackground() throws Exception {
                if (componentToAddTo.getComponentCount()<=0){
                    //If there is no MessagePage on MessageSpace load the first page from the folder
                    int start = mainManager.gmail_store.getFolder(nameOfCurrentFolder).getMessageCount();
                    loadingMessagePageStatus.setText("Loading "+ currentFolder + ", MessagePage : "+ 1);
                    if (start<1) {
                        this.cancel(true);
                    }
                    int end = start - MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS;


                    if (end <=1){
                        end =1;
                    }else  end = start -11;
                    Message [] messages = mainManager.loadMessagesFromAFolder(nameOfCurrentFolder,start,end);
                    if (start>end)  return  new MessagePage(0,start-end,messages,componentToAddTo,1);
                    return new MessagePage(0,end-start,messages,componentToAddTo,1);
                }else{
                    if (cardLayoutMain.isNextCardAvailable()){
                        return null;
                    }else {
                        // Get the index of the current MessagePage and makes a new MessagePage out of its values
                        // The index of the current MessagePage is stored in the name of the Component
                        int indexofCurrentPage = Integer.parseInt(cardLayoutMain.getCurrentCard().getName());

                        int start = ((mainManager.gmail_store.getFolder(nameOfCurrentFolder).getMessageCount()) - (indexofCurrentPage*MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS));
                        if (start<1) {
                            this.cancel(true);
                        }
                        int end =start-MessagePage.MAXIMUM_AMOUNT_OF_EMAIL_LABELS ;
                        if (end <=1){
                            end =1;
                        }else  end = start -11;
                        Message [] messages = mainManager.loadMessagesFromAFolder(nameOfCurrentFolder,start,end);

                        loadingMessagePageStatus .setText(currentFolder + " , Message Page : "+ (indexofCurrentPage+1));
                        if (start>end)  return  new MessagePage(0,start-end,messages,componentToAddTo,indexofCurrentPage+1);
                        return new MessagePage(0,end-start,messages,componentToAddTo,indexofCurrentPage+1);


                    }
                }

            }

            @Override
            protected void done() {
                try {


                    MessagePage product =get() ;
                    if (product != null){

                        componentToAddTo.add(product);}
                    System.out.println("Done");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                   throw new RuntimeException(e);
                }catch (CancellationException e){
                    SwingUtilities.invokeLater(()->loadingMessagePageStatus.setText("Page  Not Available"));
                }
                if (cardLayoutMain.isNextCardAvailable()){
                    cardLayoutMain.next(componentToAddTo);
                }
                isaMessagePageLoading = false;
                loadingMessagePageStatus.setText("No Message Page Loading");
            }
        };

        lazyLoader.execute();

    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder =  currentFolder;
    }

    public String getCurrentFolder() {
        if (currentFolder.equalsIgnoreCase("Inbox")) return currentFolder;
        return "[Gmail]/"+currentFolder;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainUI mainProgram = new MainUI();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        //Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()).forEach(System.out::println);
    }
}
