/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.mail.Message;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author admin
 */
public class MailBean {

    private static Log log = LogFactory.getLog(MailBean.class);
    private String host;
    private String subject;
    private String to;
    private String cc;
    private String bcc;
    private String from;
    private String alias;
    private String content;

    public void sendMail() {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        //String fromFix = "CSR_CRC@terrabit.co.th";
        Properties props = new Properties();
        props.put("mail.mime.charset", "UTF8");
        props.put("mail.smtp.host", JSFUtil.getApplication().getSmtpHost());
        props.put("mail.smtp.port", String.valueOf(JSFUtil.getApplication().getSmtpPort()));
        if (JSFUtil.getApplication().isSmtpSsl()) {
            props.put("mail.smtp.socketFactory.port", String.valueOf(JSFUtil.getApplication().getSmtpSslPort()));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        }
        Session session;
        if (JSFUtil.getApplication().isSmtpAuth()) {
            props.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(JSFUtil.getApplication().getSmtpAuthUsername(), JSFUtil.getApplication().getSmtpAuthPassword());
                    }
                });
        } else {
            session = Session.getDefaultInstance(props,null);
        }
        
        /*props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "false");
        props.put("mail.mime.charset", "UTF8");*/

        
        
        
        //Session session = Session.getDefaultInstance(props,null);
        

        try {
            // create the messge.
            MimeMessage mimeMessage = new MimeMessage(session);

            MimeMultipart rootMixedMultipart = new MimeMultipart("mixed");
            mimeMessage.setContent(rootMixedMultipart);


            MimeMultipart nestedRelatedMultipart = new MimeMultipart("related");
            MimeBodyPart relatedBodyPart = new MimeBodyPart();
            relatedBodyPart.setContent(nestedRelatedMultipart);
            rootMixedMultipart.addBodyPart(relatedBodyPart);

            MimeMultipart messageBody = new MimeMultipart("alternative");
            MimeBodyPart bodyPart = null;
            for (int i = 0; i < nestedRelatedMultipart.getCount(); i++) {
                BodyPart bp = nestedRelatedMultipart.getBodyPart(i);
                if (bp.getFileName() == null) {
                    bodyPart = (MimeBodyPart) bp;
                }
            }
            if (bodyPart == null) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                nestedRelatedMultipart.addBodyPart(mimeBodyPart);
                bodyPart = mimeBodyPart;
            }
            bodyPart.setContent(messageBody, "text/alternative");

            // Create the plain text part of the message.
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setText("This is plain text message", "TIS620");
            messageBody.addBodyPart(plainTextPart);

            // Create the HTML text part of the message.
            String html = "<html>"
                    + "<head>"
                    + "<title>" + subject + "</title>"
                    + "<head>"
                    + "<body>"
                    + "<p>" + content + "</p>"
                    + "</body>"
                    + "</html>";
            
            MimeBodyPart htmlTextPart = new MimeBodyPart();
            htmlTextPart.setContent(html, "text/html;charset=UTF-8");
            messageBody.addBodyPart(htmlTextPart);

            mimeMessage.setReplyTo(new InternetAddress[]{new InternetAddress(from)});
            try {
                if (alias==null) {
                    mimeMessage.setFrom(new InternetAddress(from));
                } else {
                    mimeMessage.setFrom(new InternetAddress(from, alias));
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MailBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            //setRecipients TO
            ArrayList recipientsArray = new ArrayList();
            StringTokenizer st = new StringTokenizer(to, ",");
            while (st.hasMoreTokens()) {
                recipientsArray.add(st.nextToken());
            }
            int sizeTo = recipientsArray.size();
            InternetAddress[] addressTo = new InternetAddress[sizeTo];
            for (int i = 0; i < sizeTo; i++) {
                addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, addressTo);
            
             //setRecipients CC
            if(cc != null) {
                ArrayList recipientsCCArray = new ArrayList();
                StringTokenizer stcc = new StringTokenizer(cc, ",");
                while (stcc.hasMoreTokens()) {
                    recipientsCCArray.add(stcc.nextToken());
                }
                int sizeCc = recipientsCCArray.size();
                InternetAddress[] addressCc = new InternetAddress[sizeCc];
                for (int i = 0; i < sizeCc; i++) {
                    addressCc[i] = new InternetAddress(recipientsCCArray.get(i).toString());
                }
                mimeMessage.setRecipients(Message.RecipientType.CC, addressCc);
            }
            
             //setRecipients BCC
            if(bcc != null) {
                ArrayList recipientsBccArray = new ArrayList();
                StringTokenizer stbcc = new StringTokenizer(bcc, ",");
                while (stbcc.hasMoreTokens()) {
                    recipientsBccArray.add(stbcc.nextToken());
                }
                int sizeBcc = recipientsBccArray.size();
                InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
                for (int i = 0; i < sizeBcc; i++) {
                    addressBcc[i] = new InternetAddress(recipientsBccArray.get(i).toString());
                }
                mimeMessage.setRecipients(Message.RecipientType.BCC, addressBcc);
            }
            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject(subject, "UTF-8");

            Transport.send(mimeMessage);
        } catch (MessagingException ex) {
            Logger.getLogger(MailBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
}
