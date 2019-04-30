/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.test;

/**
 *
 * @author ukritj
 */
import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import com.sun.mail.pop3.POP3Store;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

public class ReceiveMail {
    
    public static boolean textIsHtml = false;

    public static void receiveEmail(String pop3Host, String storeType,
            String user, String password) {
        try {
            //1) get the session object  
            Properties properties = new Properties();
            properties.put("mail.host", "imap.gmail.com");
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.auth", "true");
            properties.put("mail.imap.port", "993");
            properties.put("mail.transport.protocol", "smtp");
            Session emailSession = Session.getDefaultInstance(properties);
            try{
                display(null, new File("/Users/ukritj/temp/from_yahoo1.eml"));
            }catch(Exception e){
                e.printStackTrace();
            }
            

//2) create the POP3 store object and connect with the pop server  
            Store emailStore = emailSession.getStore("imaps");
            //POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

            //3) create the folder object and open it  
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            System.out.println(emailFolder.getMessageCount());
            
            //Message messages[] = inbox.getMessages();
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

            //4) retrieve the messages from the folder in an array and print it  
            Message messages[] = emailFolder.search(ft);
            System.out.println(emailFolder.getNewMessageCount());
            
            /* Use a suitable FetchProfile    */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            emailFolder.fetch(messages, fp);
            
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                //System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                //System.out.println("From: " + message.getFrom()[0]);
                //System.out.println("Text: " + message.getContent().toString());
                //message.setFlag(Flags.Flag.SEEN, true);
                processSaveToFile(message, message.getSubject() + (i+1));
                
            }
            //emailFolder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);

            //5) close the store and folder objects  
            //emailFolder.close(false);
            //emailStore.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    public static void display(Session session, File emlFile) throws Exception{

        //Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(emlFile);
        MimeMessage msg = new MimeMessage(session, source);


        System.out.println("Subject : " + msg.getSubject());
        System.out.println("From : " + msg.getFrom()[0]);
        System.out.println("Reply to : " + InternetAddress.toString(msg.getAllRecipients()));
        System.out.println("Body : " +  msg.getContent());
        System.out.println("Content Type : " + msg.getContentType());
        
        if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
            Multipart mp = (Multipart) msg.getContent();
            int totalAttachments = mp.getCount();
            if (totalAttachments > 0) {
                for (int i = 0; i < totalAttachments; i++) {
                    textIsHtml = false;
                    Part part = mp.getBodyPart(i);
                    System.out.println("PART : " + (i+1));
                    System.out.println(part.getContentType());
                    String str = getText(part);
                    if(textIsHtml){
                        System.out.println(str);
                    }
                    String attachFileName = part.getFileName();
                    String disposition = part.getDisposition();
                    String contentType = part.getContentType();
                    
                    if(attachFileName != null && !attachFileName.isEmpty()){
                        DataInputStream fin = new DataInputStream(new BufferedInputStream(part.getInputStream()));
                        DataOutputStream fout = new DataOutputStream(new FileOutputStream(new File("/Users/ukritj/temp/", attachFileName), false));
                        byte[] data = new byte[part.getSize()];
                        int bytesRead = -1;
                        while ((bytesRead = fin.read(data)) != -1) {
                            fout.write(data, 0, bytesRead);
                        }
                        fout.close();
                        fin.close();
                    }
                    
                    
                    if ((attachFileName != null && attachFileName.endsWith(".ics")) || contentType.indexOf("text/calendar") >= 0) {
                        String[] dateHeader = msg.getHeader("date");
                    }
                }
            }
        }
        source.close();
    }
    
    private static String getText(Part p) throws
                MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            System.out.println("CONTENT TYPE : " + mp.getContentType());
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                System.out.println("CONTENT TYPE : " + bp.getContentType());
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            System.out.println("CONTENT TYPE : " + mp.getContentType());
            for (int i = 0; i < mp.getCount(); i++) {
                System.out.println("CONTENT TYPE : " + mp.getBodyPart(i).getContentType());
                String s = getText(mp.getBodyPart(i));
                
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    
    private static void processSaveToFile(javax.mail.Message msg, String subject)
            throws MessagingException, IOException {
        String whereToSave = "/Users/ukritj/temp/" + sanitizeFilename(subject) + ".eml";
        //logger.info("Sauvegarde vers " + whereToSave);
        OutputStream out = new FileOutputStream(new File(whereToSave));
        try {
            msg.writeTo(out);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
    private static String sanitizeFilename(String name) {
        return name.replaceAll("[:\\\\/*?|<> \"]", "_");
    }

    public static void main(String[] args) {

        String host = "imap.gmail.com";//change accordingly 
        String port = "995";
        String mailStoreType = "pop3s";
        String username = "ukrit@terrabit.co.th";
        String password = "inczln09";//change accordingly  

        //receiveEmail(host, mailStoreType, username, password);
        
        String str = "C:\\Users\\admin\\Pictures\\02.jpg";
        System.out.println(str.substring(str.lastIndexOf("\\")+1, str.length()));

    }
}
