/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.google.gson.Gson;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.FileMappingValue;
import com.maxelyz.social.model.entity.*;
import com.maxelyz.social.model.dao.SclUsersDAO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoAccountUserDAO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.dao.SoChannelDAO;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.dao.SoMessageHistoryDAO;
import com.maxelyz.social.model.entity.SclUsers;
import com.maxelyz.utils.FileUploadBean;

import java.io.*;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import javax.annotation.PostConstruct;

import com.maxelyz.utils.JSFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.maxelyz.utils.SecurityUtil;
import java.math.BigInteger;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.richfaces.component.UIFileUpload;
import org.richfaces.event.FileUploadEvent;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.model.UploadedFile;

@ManagedBean
//@RequestScoped
@ViewScoped
public class SclAssignmentController implements Serializable {

    private static Logger log = Logger.getLogger(SclAssignmentController.class);
    private static String REFRESH = "saleapproval.xhtml?faces-redirect=true";
    //private static String PATH = "/Users/ukritj/maxar/userfiles/emailgatewaystore/";
    private static String PATH = "D:\\emailgatewaystore\\";
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
    List<String> attachmentList;
    List<String> bodyList;
    
    private String pSubject;
    private String pTo;
    private String pCc;
    private String pBcc;
    private String pBody;
    private String pAction;
    
//    private List<SclMessage> sclMessageNews;
    private List<SoMessage> soMessageAssignments;
    private List<SoMessage> soMessageSelects;
    private List<SoMessage> soMessageUsers;
    private SoMessage soMessage;
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

    @PostConstruct
    public void initialize() {
        //if (!SecurityUtil.isPermitted("admin:exportsalesresult:view")) {
        //    SecurityUtil.redirectUnauthorize();
        //}
        JSFUtil.getUserSession().setMainPage("social");
//        List<SoAccountUser> sa = soAccountUserDAO.findAll();

//        System.out.println(JSFUtil.getRequestParameterMap("pagetype"));
        pageType = JSFUtil.getRequestParameterMap("pagetype");
        if(pageType == null) {
            pageType = "AS";
        }
        JSFUtil.getUserSession().setSubPage(pageType);

        fromDate = new Date();
        toDate = new Date();
        tmrName = "";

        resetSelect();
        resetProperty();

//        sclMessageNews = sclMessageDAO.findSclMessageEntities();
//        sclMessageSelects = sclMessageDAO.findSclMessageByRelatedId(BigInteger.valueOf(Long.parseLong("335099446607471")));

//        sclMessageNews = sclMessageDAO.findSclMessageByReceivedBy(1);
//        if (pageType==null){
//            pageType="1";
//
//            try {
//                readFromUrl("http://tipjer.com/applications/socialcrm_monitor/");
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            autoFetchautoAssign();
//        }
//        this.selectActionListener(null);
        soChannelList = soChannelDAO.findByUser(JSFUtil.getUserSession().getUsers().getId());
        Integer soChannelId = 0;
        if(soChannelList != null && !soChannelList.isEmpty()){
            soChannelId = soChannelList.get(0).getId();
        }
        if(soChannelId != null && soChannelId != 0) {
            soAccountList = soAccountDAO.findBySoChannelId(JSFUtil.getUserSession().getUsers().getId(), soChannelId);
        }else{
            soAccountList = soAccountDAO.findSoAccountByUser(JSFUtil.getUserSession().getUsers().getId());
        }
        Integer soAccountId = 0;
        if(soAccountList != null && !soAccountList.isEmpty()){
            soAccountId = soAccountList.get(0).getId();
        }
        if(soAccountId != null && soAccountId != 0) {
            if(pageType.equals("ST")){
                soMessageAssignments = soMessageDAO.findComposeByAccountId(JSFUtil.getUserSession().getUsers().getId(), soAccountId, pageType);
            }else{
                soMessageAssignments = soMessageDAO.findByAccountId(JSFUtil.getUserSession().getUsers().getId(), soAccountId, pageType);
            }
        }else{
            soMessageAssignments = new ArrayList<SoMessage>();
        }
        //else{
        //    soMessageAssignments = soMessageDAO.findSoMessageByReceivedById(JSFUtil.getUserSession().getUsers().getId(), pageType);
        //}
        //soMessageAssignments = soMessageDAO.findSoMessageByReceivedById(JSFUtil.getUserSession().getUsers().getId(), Integer.parseInt(pageType));

    }
    
    public void checkStatusComposeListener(){
        Integer id = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
        SoMessage sm = soMessageDAO.findSoMessage(id);
        if(JSFUtil.getUserSession().getSubPage() != null){ 
            if(JSFUtil.getUserSession().getSubPage().equals("OP") && !sm.getCase_status().equals("DF")){
                //initialize();
                JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=OP");
            } else if(JSFUtil.getUserSession().getSubPage().equals("WF") && !sm.getCase_status().equals("WF")){
                //initialize();
                JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=WF");
            }
        }
    }
    
    private void initAccountList(){
    
    }

    private void autoFetchautoAssign() {
        String strURLs;

        try {
            strURLs = "http://tipjer.com/applications/socialcrm_services/update_last_trans/50";
            //System.out.println(strURLs);

            JSONArray jss = readJsonArrayFromUrl(strURLs);
            List<SoMessage> soMessages = new ArrayList<SoMessage>();

//                for (int i = 0; i < 1; i++) {
            for (int i = 0; i < jss.length(); i++) {
                JSONObject js = jss.getJSONObject(i);
//                System.out.println(String.valueOf(i) + ':' + js.toString());

                Gson gson = new Gson();
//                  SoMessage soMessage = soMessageDAO.findSoMessage(5);
                SoMessage soMessage = gson.fromJson(js.toString(), SoMessage.class);

                soMessages.add(soMessage);

//                    int in = soMessageDAO.checkSoMessageDup(soMessage.getSource_id(), soMessage.getRelated_id(), soMessage.getActivity_time());
                int in = soMessageDAO.checkSoMessageDup(soMessage.getActivity_id());
                if (in==0) {
                    soMessage.initial();
                    soMessage.equalize();
                    soMessage.setCreate_date(new Date());
                    soMessage.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
                    soMessageDAO.create(soMessage);

                    //SoMessageHistory soMessageHistory = new SoMessageHistory();
//                        soMessageHistory = soMessage.toSoMessageHistory();
//                        soMessageHistory.initial();
                    //soMessageHistory = soMessage.toSoMessageHistoryForInsert();
                    //soMessageHistoryDAO.create(soMessageHistory);
                }

            }

            //for (SoMessage o : soMessages) {
                //System.out.println(o.toString());
            //}

            basicAssignment(null);


        } catch (Exception e) {
            log.error(e);
        }
    }

    private synchronized Connection getConnection() throws SQLException, NamingException {

//        Context initContext = new InitialContext();
//        Context envContext = (Context) initContext.lookup("java:/comp/env");
//        DataSource ds = (DataSource) envContext.lookup("jdbc/MaxarDB");

        return dataSource.getConnection();	// Allocate and use a connection from the pool
    }

    public void basicAssignment(ActionEvent event) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = getConnection();

            sql = "update so_message set receive_by_id = 1, case_status = 'AS', assign_date = getdate()  " +
//                    "where id%10=0 and receive_by_id is null";
                    "where receive_by_id is null";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }

        }
    }

    private void resetSelect(){
        selectedMsgID = "";
    }

    private void resetProperty(){
        templateId = "";
        replyMode = "";
        replyDesc = "";
        emotion = "";
        priority = "";
        caseStatus = "";
        caseType = "";
        kbTag = "";
//        attachment = "";
//        campaignId = "";
//        productId = "";
        accountUserId = "";
    }

    private void toForm() {
        replyMode = soMessage.getMessage_status()==null?"REPLY":soMessage.getMessage_status().toString();
        replyDesc = soMessage.getRemark();
        emotion = soMessage.getSentimental_enum_id()==null?"":soMessage.getSentimental_enum_id();
        priority = soMessage.getPriority_enum_id()==null?"":soMessage.getPriority_enum_id();
        caseStatus = soMessage.getCase_status()==null?"FL":soMessage.getCase_status();
        caseType = soMessage.getCasetype_enum_id()==null?"":soMessage.getCasetype_enum_id();
        //soMessage.getSoMessageKnowledgebaseCollection();

        //List<SoMessageKnowledgebase> list = (List<SoMessageKnowledgebase>) soMessage.getSoMessageKnowledgebaseCollection();
        //for(SoMessageKnowledgebase smk : list){
        //    kbTag = Integer.toString(smk.getSoMessageKnowledgebasePK().getKnowledgebaseId());
        //}


        //SoMessageKnowledgebase smk;
//        attachment = sclMessageAssignment.getAttachment();
//        campaignId = soMessage.getCampaign_id()==null?"":soMessage.getCampaign_id().toString();
//        productId = soMessage.getProduct_id()==null?"":soMessage.getProduct_id().toString();
        accountUserId = soMessage.getAccount_user_id()==null?"":soMessage.getAccount_user_id().toString();

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            System.out.println(jsonText);
            return jsonText;
        } finally {
            is.close();
        }
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            System.out.println(jsonText);
            JSONArray jArray = new JSONArray(jsonText);
            return jArray;
        } finally {
            is.close();
        }
    }
    
    private void initEmail(){
        
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
    
    public void replyActionListener() {
        try{
            this.pAction = "re";
            this.pTo = InternetAddress.toString(msg.getReplyTo());
            this.pCc = null;
            this.pSubject = "Re: " + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void replyAllActionListener() {
        try{
            this.pAction = "reAll";
            this.pTo = InternetAddress.toString(msg.getReplyTo());
            this.pCc = InternetAddress.toString(msg.getAllRecipients());
            this.pSubject = "Re: " + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void forwardActionListener() {
        try{
            this.pAction = "fw";
            this.pTo = null;
            this.pCc = null;
            this.pSubject = "Fw: " + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public String sendActionListener() {
        try {
            
            createMessage(pTo, pCc, pBcc, emailFrom, pSubject, pBody, null);
            
            //this.checkWorkflow();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "sclassignment.xhtml?faces-redirect=true";
    }
    
    private void checkWorkflow() {
        
        if(soMessage.getSoAccount() != null && soMessage.getSoAccount().getSoEmailAccount() != null) {
            
        }
        
        String txt = pSubject + " " + pBody;
        
    }
    
    public void selectedAccountActionListener(){
        if(JSFUtil.getRequestParameterMap("accountId") != null){
            Integer accountId = Integer.parseInt(JSFUtil.getRequestParameterMap("accountId"));
            if(accountId != 0){
                soMessageAssignments = soMessageDAO.findByAccountId(JSFUtil.getUserSession().getUsers().getId(), accountId, pageType);
            }
        }
    }
    
    public void selectedChannelActionListener(){
        if(JSFUtil.getRequestParameterMap("soChannelId") != null){
            Integer soChannelId = Integer.parseInt(JSFUtil.getRequestParameterMap("soChannelId"));
            if(soChannelId != 0){
                soAccountList = soAccountDAO.findBySoChannelId(JSFUtil.getUserSession().getUsers().getId(), soChannelId);
            }
            Integer soAccountId = 0;
            if(soAccountList != null && !soAccountList.isEmpty()){
                soAccountId = soAccountList.get(0).getId();
            }
            if(soAccountId != null && soAccountId != 0) {
                soMessageAssignments = soMessageDAO.findByAccountId(JSFUtil.getUserSession().getUsers().getId(), soAccountId, pageType);
            }else{
                soMessageAssignments = new ArrayList<SoMessage>();
            }
            
        }
    }
    
    public String selectActionEmailListener() {
        
        initEmail();
        Integer soAccountId = 0;
        
        selectedMsgID = JSFUtil.getRequestParameterMap("selectedMsgID");
        
        if(selectedMsgID != null && !selectedMsgID.isEmpty()){
            soMessage = soMessageDAO.findSoMessage(new Integer(selectedMsgID));
            if(soMessage != null && soMessage.getSoAccount() != null)
            soAccountId = soMessage.getSoAccount().getId();
        }        
        try {
            //soMessage.setReceive_by_name(JSFUtil.getUserSession().getUserName());
            //soMessage.setReceive_date(new Date());
            soMessage.setCase_status("PS");

            soMessageDAO.edit(soMessage);
        } catch (Exception e) {
            log.error(e);
        }
        
        try{
            displayEmail(new File(PATH + soAccountId + "\\" + soMessage.getId() + "\\" + soMessage.getId() + ".eml"));
            //displayEmail(new File(PATH + soAccountId + "/" + soMessage.getId() + "/" + soMessage.getId() + ".eml"));
            //displayEmail(new File("/Users/ukritj/temp/008.eml"));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return "/social/scldetail.xhtml";
    }
    
    private void displayEmail(File emlFile) throws Exception{
        emailBodyText = "";
        emailBodyHtml = "";
        InputStream source = new FileInputStream(emlFile);
        msg = new MimeMessage(null, source);


        emailSubject = msg.getSubject();
        emailDate = msg.getSentDate();
        emailFrom = InternetAddress.toString(msg.getFrom());
        emailTo = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
        emailCc = InternetAddress.toString(msg.getRecipients(Message.RecipientType.CC));
        
        attachmentList = new ArrayList<String>();
        String contentType = msg.getContentType();
        bodyList = new ArrayList<String>();
        if (msg.isMimeType("multipart/alternative")) {
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
            String tmpText = (String) msg.getContent();
            bodyList.add(tmpText);
        }
        
        source.close();
    }
    
    public void viewFileListener() {
        ServletOutputStream out = null;
        try {
            String fileName = JSFUtil.getRequestParameterMap("fileName");
            if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
                Multipart mp = (Multipart) msg.getContent();
                int totalAttachments = mp.getCount();
                if (totalAttachments > 0) {
                    for (int i = 0; i < totalAttachments; i++) {
                        Part part = mp.getBodyPart(i);
                        String attachFileName = part.getFileName();
                        String disposition = part.getDisposition();
                        String contentType = part.getContentType();

                        if (attachFileName != null && !attachFileName.isEmpty() && fileName.equals(attachFileName)) {
                            FacesContext context = FacesContext.getCurrentInstance();
                            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                            //response.setContentType("image/jpeg");
                            out = response.getOutputStream();

                            BufferedInputStream bin = new BufferedInputStream(part.getInputStream());
                            BufferedOutputStream bout = new BufferedOutputStream(out);
                            int ch = 0;;
                            while ((ch = bin.read()) != -1) {
                                bout.write(ch);
                            }

                            bin.close();
                            //fin.close();
                            bout.close();
                            out.flush();
                            out.close();
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(SclAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
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
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
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
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    
    private void createMessage(String to, String cc, String bcc, String from, String subject, String body, List<File> attachments) {
        try {
            Message message = new MimeMessage(Session.getInstance(System.getProperties()));
            message.setFrom(new InternetAddress(from));
            if(to != null && !to.isEmpty()){
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            if(cc != null && !cc.isEmpty()){
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if(bcc != null && !bcc.isEmpty()){
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
            if(this.pAction != null && !this.pAction.equalsIgnoreCase("fw")){
                String str1 = "";
                for(String s : bodyList){
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
            
            if(this.pAction != null && this.pAction.equalsIgnoreCase("fw")){
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
                } else if(msg.isMimeType("text/plain") || msg.isMimeType("text/html")) {
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
            som.setContent(pBody);
            som.setSource_type_enum_id("EM");
            som.setSource_subtype_enum_id("EM_OUT");
            som.setSoAccount(soMessage.getSoAccount());
            som.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
            som.setCreate_date(new Date());
            
            SoEmailMessage soem = new SoEmailMessage();
            soem.setSoMessage(som);
            soem.setEmailFrom("ukrit@terrabit.co.th");
            soem.setEmailTo(to);
            soem.setEmailCc(cc);
            soem.setEmailBcc(bcc);
            soem.setSubject(subject);
            soem.setBody(body);
            soem.setDirection("OUT");
            soem.setSendStatus("RD");
            
            som.setSoEmailMessage(soem);
            soMessageDAO.create(som);
            
            File f1 = new File(PATH + soMessage.getSoAccount().getId());
            if(!f1.exists()) {
                f1.mkdir();
            }
            File f2 = new File(PATH + soMessage.getSoAccount().getId() + "\\" + som.getId());
            //File f2 = new File(PATH + soMessage.getSoAccount().getId() + "/" + som.getId());
            if(!f2.exists()) {
                f2.mkdir();
            }
            if(f2.exists()) {
                message.writeTo(new FileOutputStream(new File(PATH + soMessage.getSoAccount().getId() + "\\" + som.getId() + "\\" + som.getId() + ".eml")));
                //message.writeTo(new FileOutputStream(new File(PATH + soMessage.getSoAccount().getId() + "/" + som.getId() + "/" + som.getId() + ".eml")));
            }
            
            soMessage.setCase_status("CL");
            soMessageDAO.edit(soMessage);
            
            try{
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    File f = item.getValue();
                    if(f.exists()){
                        f.delete();
                    }
                }
            }catch(Exception e){
            
            }
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SclAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectActionListener() {

//        sclMessageSelects = sclMessageDAO.findSclMessageRelatedById(296);
//        sclMessage = sclMessageDAO.findSclMessage(296);
//        sclUsers = sclUsersDAO.findSclUsersByUserId(sclMessage.getUserId());

        commentInfos.clear();
        selectedMsgID = JSFUtil.getRequestParameterMap("selectedMsgID");

        if (StringUtils.isNotBlank(selectedMsgID)) {
            soMessage = soMessageDAO.findSoMessage(new Integer(selectedMsgID));
//            soMessageSelects = soMessageDAO.findSoMessageRelatedById(new Integer(selectedMsgID));
            soMessageUsers = soMessageDAO.findSoMessageByUserId(soMessage.getUser_id(), soMessage.getSource_type_enum_id());
//            sclUsers = sclUsersDAO.findSclUsersByUserId(soMessage.getUser_id());

            try {
                String strURLs;
                JSONArray jsons;
//                strURLs = "http://tipjer.com/applications/socialcrm_services/content_detail/"+soMessage.getActivity_time().toString()+"/"+soMessage.getSource_type_enum_id();
                strURLs = "http://tipjer.com/applications/socialcrm_services/content_detail/"+soMessage.getActivity_id();
                JSONObject js1 = new JSONObject(readFromUrl(strURLs));
                SoContentDetailPackDTO soPack;
                Gson gs1 = new Gson();
                soPack = gs1.fromJson(js1.toString(), SoContentDetailPackDTO.class);

                List<SoContentDetailDTO> postInfos = soPack.getMaster();
                if (postInfos.size()>0){
                    postInfo = postInfos.get(0);
                }

                commentInfos = soPack.getChildren();

               /* strURLs = "http://tipjer.com/applications/socialcrm_services/postinfo/"+soMessage.getRelated_id().toString();
                jsons = readJsonArrayFromUrl(strURLs);
                for (int i = 0; i < jsons.length(); i++) {
                    JSONObject js = jsons.getJSONObject(i);
                    System.out.println("=== post " + i);
                    System.out.println(js.toString());

                    Gson gson = new Gson();
//                    SoPostInfoDTO p = gson.fromJson(js.toString(), SoPostInfoDTO.class);
//                    p.equalize();
//                    SoCommentInfoDTO c = new SoCommentInfoDTO();
//                    c.setId(p.getObject_id());
//                    c.setUser_name(p.getUser_name());
//                    c.setContent(p.getContent());
//                    c.setTime(p.getCreated_time());
//                    c.equalize();
//
//                    commentInfos.add(c);
                    postInfo = gson.fromJson(js.toString(), SoPostInfoDTO.class);
                    postInfo.equalize();
                }*/

               /* strURLs = "http://tipjer.com/applications/socialcrm_services/commentslist/"+soMessage.getRelated_id().toString();
                jsons = readJsonArrayFromUrl(strURLs);
                for (int i = 0; i < jsons.length(); i++) {
                    JSONObject js = jsons.getJSONObject(i);
                    System.out.println("=== comment " + i);
                    System.out.println(js.toString());

                    Gson gson = new Gson();
                    SoCommentInfoDTO commentInfo = gson.fromJson(js.toString(), SoCommentInfoDTO.class);
                    commentInfo.equalize();
                    commentInfos.add(commentInfo);
                }*/

                userInfo = null;
                strURLs = "http://tipjer.com/applications/socialcrm_services/user_info/"+soMessage.getUser_id().toString();
                jsons = readJsonArrayFromUrl(strURLs);
                for (int i = 0; i < jsons.length(); i++) {
                    JSONObject js = jsons.getJSONObject(i);
                    //System.out.println("=== user " + i);
                    //System.out.println(js.toString());

                    Gson gson = new Gson();
                    userInfo = gson.fromJson(js.toString(), SoUserInfoDTO.class);
                    userInfo.equalize();
                    break;
                }

                accountUserList =  getAccountUserList(soMessage.getSource_id());

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        try {
//            SclMessageAssignment sclMessageAssignment = new SclMessageAssignment();
//            soMessage.setReceive_by_id(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setReceive_by_name(JSFUtil.getUserSession().getUserName());
            soMessage.setReceive_date(new Date());
//            soMessage.setUpdateBy(JSFUtil.getUserSession().getUsers().getId());
//            soMessage.setUpdateDate(new Date());

            if (soMessage.getCase_status()==null){
                soMessage.setCase_status("AS");
            }

            soMessageDAO.edit(soMessage);
        } catch (Exception e) {
            log.error(e);
        }

        resetProperty();
        toForm();
        
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String urlEncode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return s;
    }

    public String sendFacebook() throws Exception {

        String postMessageId = soMessage.getRelated_id().toString();
        String strURLs = "http://tipjer.com/applications/socialcrm_services/postcomment/"+postMessageId+"/"+replyDesc;
        //System.out.println(strURLs);
        String response = readFromUrl(strURLs);
        //System.out.println(response);
        return response;

//        http://tipjer.com/applications/socialcrm_services/send_content/TW/TW_TWT/NONE/twitter_test1/ทดสอบจาก socgateway 1208
//        http://tipjer.com/applications/socialcrm_services/send_content/FB/FB_STM/NONE/AspectDemo_FanPage/ทดสอบส่ง post จาก socgateway 1236
//        http://tipjer.com/applications/socialcrm_servic
    }

    public String replySocial(String sourceType, String sourceSubType, String sourceId, String relatedId, String content) throws Exception {

        sourceSubType = sourceSubType.equalsIgnoreCase("FB_STM")?"FB_CMT":sourceSubType;
        String strURLs = String.format("http://tipjer.com/applications/socialcrm_services/send_content/%s/%s/%s/%s/%s"
                , urlEncode(sourceType)
                , urlEncode(sourceSubType)
                , urlEncode(relatedId)
                , urlEncode(sourceId)
                , urlEncode(content));

        //System.out.println(strURLs);
        String response = readFromUrl(strURLs);
        //System.out.println(response);
        return response;
    }

    public void save() {
        //System.out.println("templateId:"+templateId);
        //System.out.println("replyMode:"+replyMode);
        //System.out.println("replyDesc:"+replyDesc);
        //System.out.println("emotion:"+emotion);
        //System.out.println("priority:"+priority);
//        System.out.println("campaignId:"+campaignId);
//        System.out.println("productId:"+productId);
        //System.out.println("caseType:"+caseType);
        //System.out.println("kbTag:"+kbTag);
        //System.out.println("accountUserId:"+accountUserId);
        //System.out.println("caseStatus:"+caseStatus);
//        System.out.println("attachment:"+attachment);

        //System.out.println("soMessage:"+soMessage.getId());

        try {
//            SclMessageAssignment sclMessageAssignment = new SclMessageAssignment();
            soMessage.setMessage_status(StringUtils.isBlank(replyMode)?"IGNORE":replyMode);
            soMessage.setRemark(replyDesc);
            soMessage.setSentimental_enum_id(StringUtils.isBlank(emotion) ? null : emotion);
            soMessage.setPriority_enum_id(StringUtils.isBlank(priority) ? null : priority);
//            soMessage.setCampaign_id(StringUtils.isBlank(campaignId) ? null : Integer.parseInt(campaignId));
//            soMessage.setProduct_id(StringUtils.isBlank(productId) ? null : Integer.parseInt(productId));
            soMessage.setCasetype_enum_id(StringUtils.isBlank(caseType) ? null : caseType);
            soMessage.setCase_status(StringUtils.isBlank(caseStatus) ? "AS" : caseStatus);
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setUpdate_date(new Date());
            soMessage.setAccount_user_id(StringUtils.isBlank(accountUserId)?null:Integer.parseInt(accountUserId));
            soMessage.setAccount_user_name(StringUtils.isBlank(accountUserId)?null:soAccountUserDAO.findSoAccountUser(Integer.parseInt(accountUserId)).getUserName());
            soMessageDAO.edit(soMessage);

            soMessageHistoryDAO.deactiveById(soMessage.getId());
            //soMessageHistoryDAO.create(soMessage.toSoMessageHistoryForInsert());
        } catch (Exception e) {
            log.error(e);
        }

        //===== SoMessageKnowledgebase =====
//        try{
//            List<SoMessageKnowledgebase> list = soMessageDAO.findSoMessageKnowledgebase(soMessage.getId());
//            for(SoMessageKnowledgebase smk : list){
//                soMessageDAO.removeSoMessageKnowledgebase(smk.getSoMessageKnowledgebasePK());
//            }
//        }catch(Exception e){
//            log.error(e);
//        }

//        if (!StringUtils.isBlank(kbTag)){
////        for(SoMessageKnowledgebase smk : soMessageKnowledgebaseList){
//        SoMessageKnowledgebase smk = new SoMessageKnowledgebase(soMessage.getId(),Integer.parseInt(kbTag), JSFUtil.getUserSession().getUserName());
//        soMessageDAO.createSoMessageKnowledgebase(smk);
////        }
//        }

        if (replyMode.equals("REPLY")) {
            try {
//                sendFacebook();
                replySocial(soMessage.getSource_type_enum_id()
                        , soMessage.getSource_subtype_enum_id()
                        , soMessage.getSource_id()
                        , soMessage.getRelated_id().toString()
                        , soMessage.getRemark()
                        );
            } catch (Exception e) {
                log.error(e);
            }
        }

        resetSelect();
        resetProperty();

        String ret="";
        if (pageType.equals("1")) {
            ret = JSFUtil.getRequestContextPath()+"/social/sclassignment.jsf?pagetype="+pageType;
        } else if (pageType.equals("2")) {
            ret = JSFUtil.getRequestContextPath()+"/social/sclpending.jsf?pagetype="+pageType;
        } else if (pageType.equals("3")) {
            ret = JSFUtil.getRequestContextPath()+"/social/sclclosed.jsf?pagetype="+pageType;
        }
        JSFUtil.redirect(ret);
    }

    public void uploadCompleteListener() {
        File file = fileUploadBean.getFiles().get(0);
    }
    
    public void uploadFileListener(FileUploadEvent event) throws Exception {
        
        UploadedFile item = event.getUploadedFile();
        String fileName = item.getName();
        if(fileName.lastIndexOf("\\") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
        }
        InputStream inputStream = item.getInputStream();
        
        //File file = new File(PATH + "/temp/" + (new Date()).getTime() + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        File file = new File(PATH + "\\temp\\" + (new Date()).getTime() + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        this.copy(inputStream, file);
        listFileUpload.put(fileName, file);
        String[] stra = new String[2];
        stra[0] = fileName;
        stra[1] = file.getName();
        filesUpload.add(stra);
        

//        int idx = fileName.lastIndexOf(File.separator);
//        int idx1 = fileName.lastIndexOf("\\");
//        if (idx!=-1) {
//            fileName = fileName.substring(idx,fileName.length());
//        }else if(idx1 != -1){
//            fileName = fileName.substring(idx1+1,fileName.length());
//        }
        //File f = new File(this.getAbsolutePath(), fileName);
        //if(saveToFile(item.getInputStream(), f)) {                    
//            tempFile = f;
//            this.files.add(tempFile);
        //    this.files.add(f);
        //}
//        this.files.add(f);

    }  
    
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    public String getCampaignId() {
//        return campaignId;
//    }
//
//    public void setCampaignId(String campaignId) {
//        this.campaignId = campaignId;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }

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

    public String getFromId() {
        return accountUserId;
    }

    public void setFromId(String accountUserId) {
        this.accountUserId = accountUserId;
    }

    public String getReplyMode() {
        return replyMode;
    }

    public void setReplyMode(String replyMode) {
        this.replyMode = replyMode;
    }

//    public List<SclMessage> getSclMessageNews() {
//        return sclMessageNews;
//    }
//
//    public void setSclMessageNews(List<SclMessage> sclMessageNews) {
//        this.sclMessageNews = sclMessageNews;
//    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public String getReplyDesc() {
        return replyDesc;
    }

    public void setReplyDesc(String replyDesc) {
        this.replyDesc = replyDesc;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

//    public String getAttachment() {
//        return attachment;
//    }
//
//    public void setAttachment(String attachment) {
//        this.attachment = attachment;
//    }

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

    public Integer getMessageCount(String user_id, String src) {
        return soMessageDAO.getMessageCount(user_id, src);
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Map<String, Integer> getProductList() {
        return productDAO.getProductList();
    }

    public Map<String, String> getCaseTypeList() {
        return soCaseTypeDAO.getSoCaseTypeCodeList();
    }

    public Map<String, Integer> getSocialKBList() {
        return knowledgebaseDAO.getSocialKBList();
    }

    public Map<String, Integer> getAccountUserList(String code) {
        SoAccount sa = soAccountDAO.find1SoAccountByCode(code);
        return soAccountUserDAO.getSoAccountUserList(sa);
    }

    public Map<String, String> getMessageStatusList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put("Pending", "FL");
        values.put("Closed", "CL");
        return values;
    }

    public Map<String, Integer> getCampaignList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        values.put("Campaign1", 1);
        values.put("Campaign2", 2);
        values.put("Campaign3", 3);
        return values;
    }

    public Map<String, Integer> getPostTypeList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();

        String sql = "select * from t_scl_posttype";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                values.put(rs.getString(2), rs.getInt(1));
            }

        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

        return values;
    }
    public Map<String, Integer> getTemplateList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();

        String sql = "select * from t_scl_template";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                values.put(rs.getString(2), rs.getInt(1));
            }

        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

        return values;
    }
    
    public Date getNow() {
        return new Date();
    }
    

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void selectTemplateListener() {
        pBody = JSFUtil.getRequestParameterMap("dlgReturnValue");

        FacesContext.getCurrentInstance().renderResponse();
    }

    public void templateChangeListener(ActionEvent event) {
        String id = templateId;

        String sql = "select text from t_scl_template where id = "+id;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                replyDesc = rs.getString(1);
            }

        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public SoUserInfoDTO getSoUserInfoDTO() {
        return soUserInfoDTO;
    }

    public void setSoUserInfoDTO(SoUserInfoDTO soUserInfoDTO) {
        this.soUserInfoDTO = soUserInfoDTO;
    }

    public SclUsers getSclUsers() {
        return sclUsers;
    }

    public void setSclUsers(SclUsers sclUsers) {
        this.sclUsers = sclUsers;
    }

    public SclUsersDAO getSclUsersDAO() {
        return sclUsersDAO;
    }

    public void setSclUsersDAO(SclUsersDAO sclUsersDAO) {
        this.sclUsersDAO = sclUsersDAO;
    }

    public SoUserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SoUserInfoDTO userInfo) {
        this.userInfo = userInfo;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public String getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(String accountUserId) {
        this.accountUserId = accountUserId;
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

    public Map<String, Integer> getAccountUserList() {
        return accountUserList;
    }

    public void setAccountUserList(Map<String, Integer> accountUserList) {
        this.accountUserList = accountUserList;
    }

    public SoMessageHistoryDAO getSoMessageHistoryDAO() {
        return soMessageHistoryDAO;
    }

    public void setSoMessageHistoryDAO(SoMessageHistoryDAO soMessageHistoryDAO) {
        this.soMessageHistoryDAO = soMessageHistoryDAO;
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

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
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

    public MimeMessage getMsg() {
        return msg;
    }

    public void setMsg(MimeMessage msg) {
        this.msg = msg;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public List<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
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

    public List<String> getBodyList() {
        return bodyList;
    }

    public void setBodyList(List<String> bodyList) {
        this.bodyList = bodyList;
    }

    public boolean isTextIsHtml() {
        return textIsHtml;
    }

    public void setTextIsHtml(boolean textIsHtml) {
        this.textIsHtml = textIsHtml;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public Map<String, File> getListFileUpload() {
        return listFileUpload;
    }

    public void setListFileUpload(Map<String, File> listFileUpload) {
        this.listFileUpload = listFileUpload;
    }

    public String getpAction() {
        return pAction;
    }

    public void setpAction(String pAction) {
        this.pAction = pAction;
    }

    public String getFileNameToDelete() {
        return fileNameToDelete;
    }

    public void setFileNameToDelete(String fileNameToDelete) {
        this.fileNameToDelete = fileNameToDelete;
    }

    public List<String[]> getFilesUpload() {
        return filesUpload;
    }

    public void setFilesUpload(List<String[]> filesUpload) {
        this.filesUpload = filesUpload;
    }

    public javax.servlet.http.Part getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(javax.servlet.http.Part fileUpload) {
        this.fileUpload = fileUpload;
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

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }
}
