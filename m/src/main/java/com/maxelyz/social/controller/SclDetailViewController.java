/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;
import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.social.model.dao.SclUsersDAO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoAccountUserDAO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.dao.SoChannelDAO;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.dao.SoMessageHistoryDAO;
import com.maxelyz.social.model.entity.SclUsers;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.social.model.entity.SoEmailMessage;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author ukritj
 */
@ManagedBean
@ViewScoped
public class SclDetailViewController {

    private Integer soMessageId;
    private static Logger log = Logger.getLogger(SclAssignmentController.class);
    private static String REFRESH = "saleapproval.xhtml?faces-redirect=true";
    //private static String PATH = "/Users/ukritj/maxar/userfiles/emailgatewaystore/";
    private static String PATH = "D:\\emailgatewaystore\\";
    
    private String emailPath = JSFUtil.getApplication().getEmailFilePath();
    private String separate = JSFUtil.getApplication().getSeparatePath();
    private String pageType;
    private Date fromDate, toDate;
    private String tmrName;
    private String templateId;
    private String replyMode;
    private String replyDesc;
    private String emotion;
    private String priority;
    private String caseStatus;
    private String caseType;
    private String kbTag;
//    private String attachment;

//    private String campaignId;
//    private String productId;
    private String accountUserId;

    private String userNote;
    private String selectedTab;
    private String selectedMsgID;

    private MimeMessage msg;
    private boolean textIsHtml;

    private String emailSubject;
    private String emailFrom;
    private String emailTo;
    private String emailCc;
    private String emailBody;
    private String emailBodyText;
    private String emailBodyHtml;
    private Date emailDate;
    private List<String> attachmentList;
    private List<String> bodyList;

    private String pSubject;
    private String pTo;
    private String pCc;
    private String pBcc;
    private String pBody;
    private String pAction;

    private String emailBodyTextParent;
    private String emailBodyHtmlParent;
    private MimeMessage msgParent;
    private String emailSubjectParent;
    private Date emailDateParent;
    private String emailFromParent;
    private String emailToParent;
    private String emailCcParent;
    private List<String> attachmentListParent;
    private List<String> bodyListParent;
    private boolean textIsHtmlParent;

    //Address Book
    private String selectedEmailString;
    private boolean TO = false;
    private boolean CC = false;
    private boolean BCC = false;

//    private List<SclMessage> sclMessageNews;
    private List<SoMessage> soMessageAssignments;
    private List<SoMessage> soMessageSelects;
    private List<SoMessage> soMessageUsers;
    private SoMessage soMessage;
    private SoMessage soMessageParent;
    private SoMessage soMessageDraft;
    private SclUsers sclUsers;
    private SoUserInfoDTO soUserInfoDTO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    @ManagedProperty(value = "#{soMessageHistoryDAO}")
    private SoMessageHistoryDAO soMessageHistoryDAO;
    @ManagedProperty(value = "#{sclUsersDAO}")
    private SclUsersDAO sclUsersDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{soAccountUserDAO}")
    private SoAccountUserDAO soAccountUserDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;

//    List<SoCommentInfoDTO> commentInfos = new ArrayList<SoCommentInfoDTO>();
//    SoPostInfoDTO postInfo;
    List<SoContentDetailDTO> commentInfos = new ArrayList<SoContentDetailDTO>();
    SoContentDetailDTO postInfo;
    SoUserInfoDTO userInfo;

    Map<String, Integer> accountUserList;
    private Map<String, File> listFileUpload;
    private javax.servlet.http.Part fileUpload;
    private List<String[]> filesUpload;
    private String fileNameToDelete;

    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "temp");

    private List<SoAccount> soAccountList = new ArrayList<SoAccount>();
    private List<SoChannel> soChannelList = new ArrayList<SoChannel>();
    
    public static boolean showStructure = true;
    public static boolean saveAttachments = true;
    public static int attnum = 1;
    
    private List<String[]> contentIdList;

    @PostConstruct
    public void initialize() {
        if (JSFUtil.getRequestParameterMap("soMessageId") != null) {
            soMessageId = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
        }

        initEmail();
        Integer soAccountId = 0;
        Integer soAccountParentId = 0;

        if (soMessageId != null && soMessageId != 0) {
            soMessage = soMessageDAO.findSoMessage(soMessageId);
            if (soMessage != null && soMessage.getSoAccount() != null) {
                soAccountId = soMessage.getSoAccount().getId();
            }
            if (soMessage.getParentSoMessage() != null) {
                soMessageParent = soMessage.getParentSoMessage();
                if (soMessageParent != null && soMessageParent.getSoAccount() != null) {
                    soAccountParentId = soMessageParent.getSoAccount().getId();
                }
            }
            if (soMessage.getCase_status().equals("DF")) {
                soMessageDraft = soMessageDAO.findDraft(soMessage.getId());
                SoEmailMessage soem = soMessageDraft.getSoEmailMessage();
                pTo = soem.getEmailTo();
                pCc = soem.getEmailCc();
                pBcc = soem.getEmailBcc();
                pSubject = soem.getSubject();
                pBody = soem.getBody();
            }
        }
        try {
            //if (soMessage.getCase_status().equals("AS")) {
            //    soMessage.setReceive_by_name(JSFUtil.getUserSession().getUserName());
            //    soMessage.setReceive_date(new Date());
            //    soMessage.setCase_status("PS");
                //soMessageDAO.edit(soMessage);
            //}
        } catch (Exception e) {
            log.error(e);
        }

        try {
            displayEmail(new File(emailPath + soAccountId + separate + soMessage.getId() + separate + soMessage.getId() + ".eml"));
            if (soMessageParent != null) {
                displayEmailParent(new File(emailPath + soAccountParentId + separate + soMessageParent.getId() + separate + soMessageParent.getId() + ".eml"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEmail() {

        commentInfos.clear();
        userInfo = new SoUserInfoDTO();
        soMessageUsers = new ArrayList<SoMessage>();
        listFileUpload = new LinkedHashMap<String, File>();
        filesUpload = new ArrayList<String[]>();

        attachmentList = null;
        emailSubject = null;
        emailFrom = null;
        emailTo = null;
        emailCc = null;
        emailBody = null;
        emailBodyText = null;
        emailBodyHtml = null;
        emailDate = null;

        pTo = null;
        pCc = null;
        pBcc = null;
        pSubject = null;
        pBody = null;

        bodyList = null;
        pAction = null;
    }

    private void displayEmail(File emlFile) throws Exception {
        emailBodyText = "";
        emailBodyHtml = "";
        InputStream source = new FileInputStream(emlFile);
        msg = new MimeMessage(null, source);

        emailSubject = msg.getSubject() != null ? MimeUtility.decodeText(msg.getSubject()) : "";
        emailDate = msg.getSentDate();
        emailFrom = msg.getFrom() != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getFrom())) : "";
        emailTo = msg.getRecipients(Message.RecipientType.TO) != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO))) : "";
        emailCc = msg.getRecipients(Message.RecipientType.CC) != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.CC))) : "";
          
        attachmentList = new ArrayList<String>();
        String contentType = msg.getContentType();
        bodyList = new ArrayList<String>();
        /*if (msg.isMimeType("multipart/alternative")) {
            String txt = this.getText(msg);
            bodyList.add(txt);
        } else if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
            Multipart mp = (Multipart) msg.getContent();
            int totalAttachments = mp.getCount();
            if (totalAttachments > 0) {
                for (int i = 0; i < totalAttachments; i++) {
                    textIsHtml = false;
                    BodyPart part = mp.getBodyPart(i);
                    String txt = this.getText(part);
                    if(txt != null && !txt.isEmpty()) bodyList.add(txt);
                    String attachFileName = part.getFileName();
                    if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && attachFileName != null && !attachFileName.isEmpty()){
                        attachmentList.add(attachFileName);
                    }
                }
            }
        } else if(msg.isMimeType("text/plain") || msg.isMimeType("text/html")) {
            String tmpText = this.getText(msg);
            bodyList.add(tmpText);
        }*/
        
        if(soMessage.getSoEmailMessage().getAttachment() != null){
            String str = soMessage.getSoEmailMessage().getAttachment();
            if(str.indexOf(",") != -1){
                StringTokenizer stk = new StringTokenizer(str, ",");
                while (stk.hasMoreTokens()) {
                    String fName = (String) stk.nextToken();
                    attachmentList.add(fName);
                }
            }else{
                attachmentList.add(str);
            }
        }
        
        dumpPart(msg, bodyList, soMessage.getSoAccount().getId(), soMessage.getId());
        
        source.close();
    }

    private void displayEmailParent(File emlFile) throws Exception {
        emailBodyTextParent = "";
        emailBodyHtmlParent = "";
        InputStream source = new FileInputStream(emlFile);
        msgParent = new MimeMessage(null, source);

        emailSubjectParent = msgParent.getSubject() != null ? MimeUtility.decodeText(msgParent.getSubject()) : "";
        emailDateParent = msgParent.getSentDate();
        emailFromParent = msgParent.getFrom() != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getFrom())) : "";
        emailToParent = msgParent.getRecipients(Message.RecipientType.TO) != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.TO))) : "";
        emailCcParent = msgParent.getRecipients(Message.RecipientType.CC) != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.CC))) : "";
        
        attachmentListParent = new ArrayList<String>();
        String contentType = msgParent.getContentType();
        bodyListParent = new ArrayList<String>();
        /*if (msgParent.isMimeType("multipart/alternative")) {
            String txt = this.getText(msgParent);
            bodyListParent.add(txt);
        } else if (msgParent.isMimeType("multipart/*") || msgParent.isMimeType("MULTIPART/*")) {
            Multipart mp = (Multipart) msgParent.getContent();
            int totalAttachments = mp.getCount();
            if (totalAttachments > 0) {
                for (int i = 0; i < totalAttachments; i++) {
                    textIsHtmlParent = false;
                    BodyPart part = mp.getBodyPart(i);
                    String attachFileName = part.getFileName();
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && attachFileName != null && !attachFileName.isEmpty()) {
                        attachmentListParent.add(attachFileName);
                    } else {
                        String txt = this.getText(part);
                        if (txt != null && !txt.isEmpty()) {
                            bodyListParent.add(txt);
                        }
                    }
                }
            }
        } else if(msgParent.isMimeType("text/plain") || msgParent.isMimeType("text/html")) {
            String tmpText = this.getText(msgParent);
            bodyListParent.add(tmpText);
        }*/
        
        if(soMessageParent.getSoEmailMessage().getAttachment() != null){
            String str = soMessageParent.getSoEmailMessage().getAttachment();
            if(str.indexOf(",") != -1){
                StringTokenizer stk = new StringTokenizer(str, ",");
                while (stk.hasMoreTokens()) {
                    String fName = (String) stk.nextToken();
                    attachmentListParent.add(fName);
                }
            }else{
                attachmentListParent.add(str);
            }
        }
        
        dumpPart(msgParent, bodyListParent, soMessageParent.getSoAccount().getId(), soMessageParent.getId());
        
        source.close();
    }

    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }
//        if (p.isMimeType("text/plain")) {
//            String s = (String)p.getContent();
//            emailBodyText += s;
//            return s;
//        } else if (p.isMimeType("text/html")) {
//            String s = (String)p.getContent();
//            emailBodyHtml += s;
//            return s;
//        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private void createMessage(String to, String cc, String bcc, String from, String subject, String body, List<File> attachments, String actionStatus) {
        try {
            Message message = new MimeMessage(Session.getInstance(System.getProperties()));
            message.setFrom(new InternetAddress(from));
            if (to != null && !to.isEmpty()) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            if (cc != null && !cc.isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if (bcc != null && !bcc.isEmpty()) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }

            message.setSubject(subject);
            // create the message part 
            MimeBodyPart content = new MimeBodyPart();
            // fill message
            String str = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<table width='100%' border='0' cellpadding='0' cellspacing='0'>"
                    + "<tr>"
                    + "<td>"
                    + body
                    + "</td>"
                    + "</tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "</table>"
                    + "</body>"
                    + "</html>";
            if (this.pAction != null && !this.pAction.equalsIgnoreCase("fw")) {
                String str1 = "";
                for (String s : bodyList) {
                    str1 += s + "<br/><br/><br/>";
                }
                str += str1;
            }

            //content.setText(str);
            Multipart multipart = new MimeMultipart("mixed");

            MimeBodyPart html = new MimeBodyPart();
            html.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            //text.setText(body.replaceAll("\\<[^>]*>","").replaceAll("&nbsp;"," ").replaceAll("&amp;","&"));  //set the text/plain version
            html.setContent(str, "text/html; charset=utf-8");     //set the text/html version

            //multiPart.addBodyPart(text);
            multipart.addBodyPart(html);

            //multipart.addBodyPart(content);
            /*if (msg != null) {
             if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
             Multipart mp = (Multipart) msg.getContent();
             int totalAttachments = mp.getCount();
             if (totalAttachments > 0) {
             for (int i = 0; i < totalAttachments; i++) {
             BodyPart bodyPart = mp.getBodyPart(i);
             multipart.addBodyPart(bodyPart);
             }
             }
             }else{
             MimeBodyPart c = new MimeBodyPart();
             c.setText((String) msg.getContent());
             multipart.addBodyPart(c);
             }
             }*/
            // add attachments
            /*for (File file : attachments) {
             MimeBodyPart attachment = new MimeBodyPart();
             DataSource source = new FileDataSource(file);
             attachment.setDataHandler(new DataHandler(source));
             attachment.setFileName(file.getName());
             multipart.addBodyPart(attachment);
             }*/
            DataHandler dataHandler = null;
            for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                MimeBodyPart attachment = new MimeBodyPart();
                FileDataSource source = new FileDataSource(item.getValue());
                dataHandler = new DataHandler(source);
                attachment.setDataHandler(dataHandler);
                attachment.setFileName(item.getKey());
                multipart.addBodyPart(attachment);
            }

            if (this.pAction != null && this.pAction.equalsIgnoreCase("fw")) {
                if (msg.isMimeType("multipart/alternative")) {
                    String txt = this.getText(msg);
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                    bodyPart.setContent(txt, "text/html; charset=utf-8");
                    multipart.addBodyPart(bodyPart);
                } else if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
                    Multipart mp = (Multipart) msg.getContent();
                    int totalAttachments = mp.getCount();
                    if (totalAttachments > 0) {
                        for (int i = 0; i < totalAttachments; i++) {
                            BodyPart part = mp.getBodyPart(i);
                            multipart.addBodyPart(part);
                        }
                    }
                } else if (msg.isMimeType("text/plain") || msg.isMimeType("text/html")) {
                    String tmpText = (String) msg.getContent();
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                    bodyPart.setContent(tmpText, "text/html; charset=utf-8");
                    multipart.addBodyPart(bodyPart);
                }
            }

            //add original mail
            if (msgParent != null) {
                if (msgParent.isMimeType("multipart/alternative")) {
                    String txt = this.getText(msgParent);
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                    bodyPart.setContent(txt, "text/html; charset=utf-8");
                    multipart.addBodyPart(bodyPart);
                } else if (msgParent.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
                    Multipart mp = (Multipart) msgParent.getContent();
                    int totalAttachments = mp.getCount();
                    if (totalAttachments > 0) {
                        for (int i = 0; i < totalAttachments; i++) {
                            BodyPart part = mp.getBodyPart(i);
                            multipart.addBodyPart(part);
                        }
                    }
                } else if (msgParent.isMimeType("text/plain") || msgParent.isMimeType("text/html")) {
                    String tmpText = (String) msg.getContent();
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                    bodyPart.setContent(tmpText, "text/html; charset=utf-8");
                    multipart.addBodyPart(bodyPart);
                }
            }

            // integration
            message.setContent(multipart);
            // store file
            //message.writeTo(new FileOutputStream(new File("/Users/ukritj/temp/test_save_email.eml")));

            SoMessage som = new SoMessage();
            if (soMessageDraft.getId() != null) {
                som = soMessageDraft;
            }
            som.setContent(pBody);
            som.setSource_type_enum_id("EM");
            som.setSource_subtype_enum_id("EM_OUT");
            som.setSoAccount(soMessage.getSoAccount());
            som.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
            som.setCreate_date(new Date());
            som.setParentSoMessage(soMessage);

            SoEmailMessage soem = new SoEmailMessage();
            if (som != null && som.getSoEmailMessage() != null && som.getSoEmailMessage().getId() != null) {
                soem = som.getSoEmailMessage();
            }
            soem.setSoMessage(som);
            soem.setEmailFrom("ukrit@terrabit.co.th");
            soem.setEmailTo(to);
            soem.setEmailCc(cc);
            soem.setEmailBcc(bcc);
            soem.setSubject(subject);
            soem.setBody(body);
            soem.setDirection("OUT");
            if (actionStatus.equals("send")) {
                soem.setSendStatus("RD");
            } else if (actionStatus.equals("saveDraft")) {
                soem.setSendStatus("NO");
            }
            som.setSoEmailMessage(soem);
            if (soMessageDraft.getId() != null) {
                soMessageDAO.edit(som);
            } else {
                soMessageDAO.create(som);
            }

            File f1 = new File(PATH + soMessage.getSoAccount().getId());
            if (!f1.exists()) {
                f1.mkdir();
            }
            File f2 = new File(PATH + soMessage.getSoAccount().getId() + "\\" + som.getId());
            //File f2 = new File(PATH + soMessage.getSoAccount().getId() + "/" + som.getId());
            if (!f2.exists()) {
                f2.mkdir();
            }
            if (f2.exists()) {
                message.writeTo(new FileOutputStream(new File(PATH + soMessage.getSoAccount().getId() + "\\" + som.getId() + "\\" + som.getId() + ".eml")));
                //message.writeTo(new FileOutputStream(new File(PATH + soMessage.getSoAccount().getId() + "/" + som.getId() + "/" + som.getId() + ".eml")));
            }

            if (actionStatus.equals("send")) {
                soMessage.setCase_status("FL");
            } else if (actionStatus.equals("saveDraft")) {
                soMessage.setCase_status("DF");
            }
            soMessageDAO.edit(soMessage);

            try {
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    File f = item.getValue();
                    if (f.exists()) {
                        f.delete();
                    }
                }
            } catch (Exception e) {

            }

        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SclAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String convertContentId(String cId){
        String str = "";
        if(cId.indexOf("<") != -1 && cId.indexOf(">") != -1){
            str = cId.substring(cId.indexOf("<") + 1, cId.indexOf(">"));
        }
        return str;
    }
    
    private String convertContentIdToFileName(String cId){
        String str = "";
        if(cId.indexOf("@") != -1){
            str = cId.substring(0, cId.indexOf("@"));
        }
        return str;
    }
    
    public void dumpPart(Part p, List<String> stra, Integer accountId, Integer soMessageId) throws Exception {
	//if (p instanceof Message)
	//    dumpEnvelope((Message)p);

	/** Dump input stream .. 

	InputStream is = p.getInputStream();
	// If "is" is not already buffered, wrap a BufferedInputStream
	// around it.
	if (!(is instanceof BufferedInputStream))
	    is = new BufferedInputStream(is);
	int c;
	while ((c = is.read()) != -1)
	    System.out.write(c);

	**/
        
        String[] sa = p.getHeader("Content-ID");
        if(sa != null){
            String contentId = convertContentId(sa[0]);
            String fileName = convertContentIdToFileName(contentId);
            String[] a = new String[2];
            a[0] = contentId;
            a[1] = fileName;
            contentIdList.add(a);
        }
        
	String ct = p.getContentType();
	try {
	    pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
	} catch (ParseException pex) {
	    pr("BAD CONTENT-TYPE: " + ct);
	}
	String filename = p.getFileName();
	if (filename != null)
	    pr("FILENAME: " + filename);

	/*
	 * Using isMimeType to determine the content type avoids
	 * fetching the actual content data until we need it.
	 */
	if (p.isMimeType("text/plain")) {
	    pr("This is plain text");
	    pr("---------------------------");
	    if (showStructure){
		//System.out.println((String)p.getContent());
                stra.add((String)p.getContent());
            }
	}else if (p.isMimeType("multipart/*")) {
            if (p.isMimeType("multipart/alternative")) {
                pr("This is a Multipart/Alternative");
                pr("---------------------------");
                Multipart mp = (Multipart) p.getContent();
                level++;
                int count = mp.getCount();
                boolean havePlain = false;
                boolean haveHtml = false;
                for (int i = 0; i < count; i++) {
                    if (mp.getBodyPart(i).isMimeType("text/plain")) {
                        havePlain = true;
                    }
                    if (mp.getBodyPart(i).isMimeType("text/html")) {
                        haveHtml = true;
                    }
                }
                for (int i = 0; i < count; i++) {
                    if (haveHtml && mp.getBodyPart(i).isMimeType("text/html")) {
                        dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                        break;
                    } else if (!haveHtml && havePlain && mp.getBodyPart(i).isMimeType("text/plain")) {
                        dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                        break;
                    }
                }
                level--;
            } else {
                pr("This is a Multipart");
                pr("---------------------------");
                Multipart mp = (Multipart) p.getContent();
                level++;
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                }
                level--;
            }
	} else if (p.isMimeType("message/rfc822")) {
	    pr("This is a Nested Message");
	    pr("---------------------------");
	    level++;
	    dumpPart((Part)p.getContent(), stra, accountId, soMessageId);
	    level--;
	} else {
            //System.out.println(p.getContentType());
	    if (showStructure) {
		/*
		 * If we actually want to see the data, and it's not a
		 * MIME type we know, fetch it and check its Java type.
		 */
		Object o = p.getContent();
		if (o instanceof String) {
		    pr("This is a string");
		    pr("---------------------------");
		    //System.out.println((String)o);
                    stra.add((String)o);
		} else if (o instanceof InputStream) {
		    pr("This is just an input stream");
		    pr("---------------------------");
		    InputStream is = (InputStream)o;
		    int c;
		    while ((c = is.read()) != -1)
			System.out.write(c);
		} else {
		    pr("This is an unknown type");
		    pr("---------------------------");
		    pr(o.toString());
                    //stra.add((String)o);
		}
	    } else {
		// just a separator
		pr("---------------------------");
	    }
	}

	/*
	 * If we're saving attachments, write out anything that
	 * looks like an attachment into an appropriately named
	 * file.  Don't overwrite existing files to prevent
	 * mistakes.
	 */
	if (saveAttachments && p instanceof MimeBodyPart &&
		!p.isMimeType("multipart/*")) {
	    String disp = p.getDisposition();
	    // many mailers don't include a Content-Disposition
            if (disp != null){
                if (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
                    if (filename == null)
                        filename = "Attachment" + attnum++;
                    pr("Saving attachment to file " + filename);
                    try {
                        File f = new File(PATH + accountId + "/" + soMessageId + "/" + filename);
                        if (f.exists())
                            // XXX - could try a series of names
                            throw new IOException("file exists");
                        ((MimeBodyPart)p).saveFile(f);
                    } catch (IOException ex) {
                        pr("Failed to save attachment: " + ex);
                    }
                    pr("---------------------------");
                }
            }
	}
    }

    public static void dumpEnvelope(Message m) throws Exception {
	pr("This is the message envelope");
	pr("---------------------------");
	Address[] a;
	// FROM 
	if ((a = m.getFrom()) != null) {
	    for (int j = 0; j < a.length; j++)
		pr("FROM: " + a[j].toString());
	}

	// REPLY TO
	if ((a = m.getReplyTo()) != null) {
	    for (int j = 0; j < a.length; j++)
		pr("REPLY TO: " + a[j].toString());
	}

	// TO
	if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
	    for (int j = 0; j < a.length; j++) {
		pr("TO: " + a[j].toString());
		InternetAddress ia = (InternetAddress)a[j];
		if (ia.isGroup()) {
		    InternetAddress[] aa = ia.getGroup(false);
		    for (int k = 0; k < aa.length; k++)
			pr("  GROUP: " + aa[k].toString());
		}
	    }
	}

	// SUBJECT
	pr("SUBJECT: " + m.getSubject());

	// DATE
	Date d = m.getSentDate();
	pr("SendDate: " +
	    (d != null ? d.toString() : "UNKNOWN"));

	// FLAGS
	Flags flags = m.getFlags();
	StringBuffer sb = new StringBuffer();
	Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

	boolean first = true;
	for (int i = 0; i < sf.length; i++) {
	    String s;
	    Flags.Flag f = sf[i];
	    if (f == Flags.Flag.ANSWERED)
		s = "\\Answered";
	    else if (f == Flags.Flag.DELETED)
		s = "\\Deleted";
	    else if (f == Flags.Flag.DRAFT)
		s = "\\Draft";
	    else if (f == Flags.Flag.FLAGGED)
		s = "\\Flagged";
	    else if (f == Flags.Flag.RECENT)
		s = "\\Recent";
	    else if (f == Flags.Flag.SEEN)
		s = "\\Seen";
	    else
		continue;	// skip it
	    if (first)
		first = false;
	    else
		sb.append(' ');
	    sb.append(s);
	}

	String[] uf = flags.getUserFlags(); // get the user flag strings
	for (int i = 0; i < uf.length; i++) {
	    if (first)
		first = false;
	    else
		sb.append(' ');
	    sb.append(uf[i]);
	}
	pr("FLAGS: " + sb.toString());

	// X-MAILER
	String[] hdrs = m.getHeader("X-Mailer");
	if (hdrs != null)
	    pr("X-Mailer: " + hdrs[0]);
	else
	    pr("X-Mailer NOT available");
    }

    static String indentStr = "                                               ";
    static int level = 0;

    /**
     * Print a, possibly indented, string.
     */
    public static void pr(String s) {
	//if (showStructure)
	    //System.out.print(indentStr.substring(0, level * 2));
	//System.out.println(s);
    }

    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        SclDetailViewController.log = log;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getReplyMode() {
        return replyMode;
    }

    public void setReplyMode(String replyMode) {
        this.replyMode = replyMode;
    }

    public String getReplyDesc() {
        return replyDesc;
    }

    public void setReplyDesc(String replyDesc) {
        this.replyDesc = replyDesc;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getKbTag() {
        return kbTag;
    }

    public void setKbTag(String kbTag) {
        this.kbTag = kbTag;
    }

    public String getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(String accountUserId) {
        this.accountUserId = accountUserId;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public String getSelectedMsgID() {
        return selectedMsgID;
    }

    public void setSelectedMsgID(String selectedMsgID) {
        this.selectedMsgID = selectedMsgID;
    }

    public MimeMessage getMsg() {
        return msg;
    }

    public void setMsg(MimeMessage msg) {
        this.msg = msg;
    }

    public boolean isTextIsHtml() {
        return textIsHtml;
    }

    public void setTextIsHtml(boolean textIsHtml) {
        this.textIsHtml = textIsHtml;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailBodyText() {
        return emailBodyText;
    }

    public void setEmailBodyText(String emailBodyText) {
        this.emailBodyText = emailBodyText;
    }

    public String getEmailBodyHtml() {
        return emailBodyHtml;
    }

    public void setEmailBodyHtml(String emailBodyHtml) {
        this.emailBodyHtml = emailBodyHtml;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    public List<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<String> getBodyList() {
        return bodyList;
    }

    public void setBodyList(List<String> bodyList) {
        this.bodyList = bodyList;
    }

    public String getpSubject() {
        return pSubject;
    }

    public void setpSubject(String pSubject) {
        this.pSubject = pSubject;
    }

    public String getpTo() {
        return pTo;
    }

    public void setpTo(String pTo) {
        this.pTo = pTo;
    }

    public String getpCc() {
        return pCc;
    }

    public void setpCc(String pCc) {
        this.pCc = pCc;
    }

    public String getpBcc() {
        return pBcc;
    }

    public void setpBcc(String pBcc) {
        this.pBcc = pBcc;
    }

    public String getpBody() {
        return pBody;
    }

    public void setpBody(String pBody) {
        this.pBody = pBody;
    }

    public String getpAction() {
        return pAction;
    }

    public void setpAction(String pAction) {
        this.pAction = pAction;
    }

    public List<SoMessage> getSoMessageAssignments() {
        return soMessageAssignments;
    }

    public void setSoMessageAssignments(List<SoMessage> soMessageAssignments) {
        this.soMessageAssignments = soMessageAssignments;
    }

    public List<SoMessage> getSoMessageSelects() {
        return soMessageSelects;
    }

    public void setSoMessageSelects(List<SoMessage> soMessageSelects) {
        this.soMessageSelects = soMessageSelects;
    }

    public List<SoMessage> getSoMessageUsers() {
        return soMessageUsers;
    }

    public void setSoMessageUsers(List<SoMessage> soMessageUsers) {
        this.soMessageUsers = soMessageUsers;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public SclUsers getSclUsers() {
        return sclUsers;
    }

    public void setSclUsers(SclUsers sclUsers) {
        this.sclUsers = sclUsers;
    }

    public SoUserInfoDTO getSoUserInfoDTO() {
        return soUserInfoDTO;
    }

    public void setSoUserInfoDTO(SoUserInfoDTO soUserInfoDTO) {
        this.soUserInfoDTO = soUserInfoDTO;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public SoMessageHistoryDAO getSoMessageHistoryDAO() {
        return soMessageHistoryDAO;
    }

    public void setSoMessageHistoryDAO(SoMessageHistoryDAO soMessageHistoryDAO) {
        this.soMessageHistoryDAO = soMessageHistoryDAO;
    }

    public SclUsersDAO getSclUsersDAO() {
        return sclUsersDAO;
    }

    public void setSclUsersDAO(SclUsersDAO sclUsersDAO) {
        this.sclUsersDAO = sclUsersDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public SoAccountUserDAO getSoAccountUserDAO() {
        return soAccountUserDAO;
    }

    public void setSoAccountUserDAO(SoAccountUserDAO soAccountUserDAO) {
        this.soAccountUserDAO = soAccountUserDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }

    public List<SoContentDetailDTO> getCommentInfos() {
        return commentInfos;
    }

    public void setCommentInfos(List<SoContentDetailDTO> commentInfos) {
        this.commentInfos = commentInfos;
    }

    public SoContentDetailDTO getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(SoContentDetailDTO postInfo) {
        this.postInfo = postInfo;
    }

    public SoUserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SoUserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public Map<String, Integer> getAccountUserList() {
        return accountUserList;
    }

    public void setAccountUserList(Map<String, Integer> accountUserList) {
        this.accountUserList = accountUserList;
    }

    public Map<String, File> getListFileUpload() {
        return listFileUpload;
    }

    public void setListFileUpload(Map<String, File> listFileUpload) {
        this.listFileUpload = listFileUpload;
    }

    public javax.servlet.http.Part getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(javax.servlet.http.Part fileUpload) {
        this.fileUpload = fileUpload;
    }

    public List<String[]> getFilesUpload() {
        return filesUpload;
    }

    public void setFilesUpload(List<String[]> filesUpload) {
        this.filesUpload = filesUpload;
    }

    public String getFileNameToDelete() {
        return fileNameToDelete;
    }

    public void setFileNameToDelete(String fileNameToDelete) {
        this.fileNameToDelete = fileNameToDelete;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public List<SoAccount> getSoAccountList() {
        return soAccountList;
    }

    public void setSoAccountList(List<SoAccount> soAccountList) {
        this.soAccountList = soAccountList;
    }

    public List<SoChannel> getSoChannelList() {
        return soChannelList;
    }

    public void setSoChannelList(List<SoChannel> soChannelList) {
        this.soChannelList = soChannelList;
    }

    public String getEmailBodyTextParent() {
        return emailBodyTextParent;
    }

    public void setEmailBodyTextParent(String emailBodyTextParent) {
        this.emailBodyTextParent = emailBodyTextParent;
    }

    public String getEmailBodyHtmlParent() {
        return emailBodyHtmlParent;
    }

    public void setEmailBodyHtmlParent(String emailBodyHtmlParent) {
        this.emailBodyHtmlParent = emailBodyHtmlParent;
    }

    public MimeMessage getMsgParent() {
        return msgParent;
    }

    public void setMsgParent(MimeMessage msgParent) {
        this.msgParent = msgParent;
    }

    public String getEmailSubjectParent() {
        return emailSubjectParent;
    }

    public void setEmailSubjectParent(String emailSubjectParent) {
        this.emailSubjectParent = emailSubjectParent;
    }

    public Date getEmailDateParent() {
        return emailDateParent;
    }

    public void setEmailDateParent(Date emailDateParent) {
        this.emailDateParent = emailDateParent;
    }

    public String getEmailFromParent() {
        return emailFromParent;
    }

    public void setEmailFromParent(String emailFromParent) {
        this.emailFromParent = emailFromParent;
    }

    public String getEmailToParent() {
        return emailToParent;
    }

    public void setEmailToParent(String emailToParent) {
        this.emailToParent = emailToParent;
    }

    public String getEmailCcParent() {
        return emailCcParent;
    }

    public void setEmailCcParent(String emailCcParent) {
        this.emailCcParent = emailCcParent;
    }

    public List<String> getAttachmentListParent() {
        return attachmentListParent;
    }

    public void setAttachmentListParent(List<String> attachmentListParent) {
        this.attachmentListParent = attachmentListParent;
    }

    public List<String> getBodyListParent() {
        return bodyListParent;
    }

    public void setBodyListParent(List<String> bodyListParent) {
        this.bodyListParent = bodyListParent;
    }

    public boolean isTextIsHtmlParent() {
        return textIsHtmlParent;
    }

    public void setTextIsHtmlParent(boolean textIsHtmlParent) {
        this.textIsHtmlParent = textIsHtmlParent;
    }

    public SoMessage getSoMessageParent() {
        return soMessageParent;
    }

    public void setSoMessageParent(SoMessage soMessageParent) {
        this.soMessageParent = soMessageParent;
    }

    //Address Book
    public String getSelectedEmailString() {
        return selectedEmailString;
    }

    public void setSelectedEmailString(String selectedEmailString) {
        this.selectedEmailString = selectedEmailString;
    }

    public void selectUserActionListener() {
        if (TO) {
            if (pTo != null && !pTo.isEmpty()) {
                pTo += "," + selectedEmailString;
            } else {
                pTo = selectedEmailString;
            }
        } else if (CC) {
            if (pCc != null && !pCc.isEmpty()) {
                pCc += "," + selectedEmailString;
            } else {
                pCc = selectedEmailString;
            }
        } else if (BCC) {
            if (pBcc != null && !pBcc.isEmpty()) {
                pBcc += "," + selectedEmailString;
            } else {
                pBcc = selectedEmailString;
            }
        }
    }

    public void checkTO() {
        TO = true;
        CC = false;
        BCC = false;
    }

    public void checkCC() {
        TO = false;
        CC = true;
        BCC = false;
    }

    public void checkBCC() {
        TO = false;
        CC = false;
        BCC = true;
    }

    public boolean isTO() {
        return TO;
    }

    public void setTO(boolean TO) {
        this.TO = TO;
    }

    public boolean isCC() {
        return CC;
    }

    public void setCC(boolean CC) {
        this.CC = CC;
    }

    public boolean isBCC() {
        return BCC;
    }

    public void setBCC(boolean BCC) {
        this.BCC = BCC;
    }

    public SoMessage getSoMessageDraft() {
        return soMessageDraft;
    }

    public void setSoMessageDraft(SoMessage soMessageDraft) {
        this.soMessageDraft = soMessageDraft;
    }

    public String getEmailPath() {
        return emailPath;
    }

    public void setEmailPath(String emailPath) {
        this.emailPath = emailPath;
    }

    public String getSeparate() {
        return separate;
    }

    public void setSeparate(String separate) {
        this.separate = separate;
    }
}
