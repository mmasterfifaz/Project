/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoEmailAccountDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.social.model.entity.SoEmailAccount;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.mail.*;
import org.apache.log4j.Logger;
/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class EmailAccountEditController {
    private static Logger log = Logger.getLogger(EmailAccountEditController.class);
    private static String REDIRECT_PAGE = "emailaccount.jsf";
    private static String SUCCESS = "emailaccount.xhtml?faces-redirect=true";
    private static String FAILURE = "emailaccountedit.xhtml";

    private SoAccount soAccount;
    private SoEmailAccount soEmailAccount;
    private String mode;
    private String message;
    private String messageIncError;
    private String messageIncSuccess;
    private String messageOutError;
    private String messageOutSuccess;
    private Integer accountId;

    @ManagedProperty(value="#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value="#{soEmailAccountDAO}")
    private SoEmailAccountDAO soEmailAccountDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:emailAccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageIncError = "";
       messageIncSuccess = "";
       messageOutError = "";
       messageOutSuccess = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soAccount = new SoAccount();
           soAccount.setEnable(Boolean.TRUE);
           soAccount.setStatus(Boolean.TRUE);
           
           soEmailAccount = new SoEmailAccount();
           soEmailAccount.setIncProtocal("pop3s");
           soEmailAccount.setOutProtocal("smtp");
           soEmailAccount.setEnable(Boolean.TRUE);
           soEmailAccount.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoEmailAccountDAO dao = getSoEmailAccountDAO();
           soEmailAccount = dao.findSoEmailAccountBySoAccountId(new Integer(selectedID));
           if(soEmailAccount == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
           } else {
               soAccount = soEmailAccount.getSoAccount();
           }
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:emailAccount:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:emailAccount:edit"); 
       }
    }  
        
    public String saveAction() {
        SoAccountDAO dao = getSoAccountDAO();
        try {                
            if (getMode().equals("add")) {
                soAccount.setId(null);
                soAccount.setCode(soAccount.getName());
                soAccount.setKeyword(soAccount.getName());
                soAccount.setSoChannel(new SoChannel(5));
                soAccount.setCreateBy(JSFUtil.getUserSession().getUserName());
                soAccount.setCreateDate(new Date());
                dao.create(soAccount);

                soEmailAccount.setId(null);
                soEmailAccount.setCreateBy(JSFUtil.getUserSession().getUserName());
                soEmailAccount.setCreateDate(new Date());
                soEmailAccount.setSoAccount(soAccount);
                this.getSoEmailAccountDAO().create(soEmailAccount);
            } else {
                soAccount.setCode(soAccount.getName());
                soAccount.setKeyword(soAccount.getName());
                soAccount.setUpdateBy(JSFUtil.getUserSession().getUserName());
                soAccount.setUpdateDate(new Date());
                dao.edit(soAccount);

                soEmailAccount.setUpdateBy(JSFUtil.getUserSession().getUserName());
                soEmailAccount.setUpdateDate(new Date());
                this.getSoEmailAccountDAO().edit(soEmailAccount);
            }
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return null;
        }
        return SUCCESS;
    }
    
    public void auhtOutMailAction()  {
        messageOutError = "";
        messageOutSuccess = "";
        
        if(soEmailAccount.getOutProtocal() != null && soEmailAccount.getOutPort() != null) {
            String protocal = soEmailAccount.getOutProtocal();
            String port = soEmailAccount.getOutPort().toString();

            Properties props = new Properties();

            props.put("mail.host", soEmailAccount.getOutServerName());
            props.put("mail.transport.protocol", soEmailAccount.getOutProtocal());
            props.put("mail.store.protocol", protocal);
            props.put("mail."+protocal+".port", port);

            props.put("mail."+protocal+".starttls.enable", String.valueOf(soEmailAccount.getOutTlsEnable()));

            if (soEmailAccount.getOutSslEnable()) {
                //SSL - Begin
                props.put("mail."+protocal+".ssl.enable", String.valueOf(soEmailAccount.getOutSslEnable()));
                props.put("mail."+protocal+".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail."+protocal+".socketFactory.port", port);
    //            props.put("mail."+protocal+".socketFactory.fallback", soEmailAccount.getOutFallbackEnable());

                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                sf.setTrustAllHosts(true);

                props.put("mail."+protocal+".ssl.socketFactory", sf);
            }

    //        props.put("mail.debug", enableDebug);

            props.put("mail."+protocal+".auth", String.valueOf(soEmailAccount.getOutAuthEnable()));
            Session session;
            if (soEmailAccount.getOutAuthEnable()) {
                session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(soEmailAccount.getUsername(), soEmailAccount.getPassword());
                            }
                        });
            } else {
                session = Session.getDefaultInstance(props,null);
            }

            try {
                Transport transport = session.getTransport();
                transport.connect();
                transport.close();
                messageOutSuccess = "Authentication Success";
            } catch (AuthenticationFailedException e) {
                messageOutSuccess = "";
                messageOutError = "Authentication Fail !!";
    //            System.out.println("FAIL "+e.getMessage());
            } catch (MessagingException e) {
                messageOutSuccess = "";
                System.out.println(e.getCause());
                messageOutError = "Authentication Fail !!";
    //            throw new RuntimeException("validate failed");
            }
        } else {
             messageOutError = "* Required Field";
        }
        
    }
    
    public void auhtIncMailAction()  {
        messageIncError = "";
        messageIncSuccess = "";
        
        if(soEmailAccount.getIncProtocal() != null && soEmailAccount.getIncPort() != null) {
            String protocal = soEmailAccount.getIncProtocal();
            String port = soEmailAccount.getIncPort().toString();

            Properties props = new Properties();

            props.put("mail.host", soEmailAccount.getIncServerName());
            props.put("mail.store.protocol", protocal);
            props.put("mail."+protocal+".port", port);

            props.put("mail."+protocal+".starttls.enable", String.valueOf(soEmailAccount.getIncTlsEnable()));

            if(protocal.equals("pop3s") || protocal.equals("imaps") ) {
                soEmailAccount.setIncSslEnable(Boolean.TRUE);
            } else {
                soEmailAccount.setIncSslEnable(Boolean.FALSE);
            }
            System.out.println(protocal+" - "+soEmailAccount.getIncSslEnable());

            if (soEmailAccount.getIncSslEnable()) {
                //SSL - Begin
                props.put("mail."+protocal+".ssl.enable", String.valueOf(soEmailAccount.getIncSslEnable()));
                props.put("mail."+protocal+".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail."+protocal+".socketFactory.port", port);
    //            props.put("mail."+protocal+".socketFactory.fallback", soEmailAccount.getIncFallbackEnable());

                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                sf.setTrustAllHosts(true);

                props.put("mail."+protocal+".ssl.socketFactory", sf);
            }

    //        props.put("mail.debug", enableDebug);

            props.put("mail."+protocal+".auth", String.valueOf(soEmailAccount.getIncAuthEnable()));
            Session session;
            if (soEmailAccount.getIncAuthEnable()) {
                session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(soEmailAccount.getUsername(), soEmailAccount.getPassword());
                            }
                        });
            } else {
                session = Session.getDefaultInstance(props,null);
            }

            try {
                Store store = session.getStore();
                store.connect();
                store.close();
                messageIncSuccess = "Authentication Success";
            } catch (AuthenticationFailedException e) {
                messageIncSuccess = "";
                messageIncError = "Authentication Fail !!";
    //            System.out.println("FAIL "+e.getMessage());
            } catch (MessagingException e) {
                messageIncSuccess = "";
                System.out.println(e.getCause());
                messageIncError = "Authentication Fail !!";
            }
        } else {
            messageIncError = "* Required Field";
        }
        
        
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageIncError() {
        return messageIncError;
    }

    public void setMessageIncError(String messageIncError) {
        this.messageIncError = messageIncError;
    }

    public String getMessageIncSuccess() {
        return messageIncSuccess;
    }

    public void setMessageIncSuccess(String messageIncSuccess) {
        this.messageIncSuccess = messageIncSuccess;
    }

    public String getMessageOutError() {
        return messageOutError;
    }

    public void setMessageOutError(String messageOutError) {
        this.messageOutError = messageOutError;
    }

    public String getMessageOutSuccess() {
        return messageOutSuccess;
    }

    public void setMessageOutSuccess(String messageOutSuccess) {
        this.messageOutSuccess = messageOutSuccess;
    }

    public SoAccount getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(SoAccount soAccount) {
        this.soAccount = soAccount;
    }

    public SoEmailAccount getSoEmailAccount() {
        return soEmailAccount;
    }

    public void setSoEmailAccount(SoEmailAccount soEmailAccount) {
        this.soEmailAccount = soEmailAccount;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public SoEmailAccountDAO getSoEmailAccountDAO() {
        return soEmailAccountDAO;
    }

    public void setSoEmailAccountDAO(SoEmailAccountDAO soEmailAccountDAO) {
        this.soEmailAccountDAO = soEmailAccountDAO;
    }




    
}
