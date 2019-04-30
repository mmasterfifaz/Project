package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CardIssuerDAO;
import com.maxelyz.core.model.dao.VoiceFilePasswordDAO;
import com.maxelyz.core.model.entity.VoiceFilePassword;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;
//import org.ajax4jsf.model.KeepAlive;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ExportVoiceFilePasswordEditController {
    private static Logger log = Logger.getLogger(ExportVoiceFilePasswordEditController.class);
    private static String REDIRECT_PAGE = "exportvoicefilepassword.jsf";
    private static String SUCCESS = "exportvoicefilepassword.xhtml?faces-redirect=true";
    private static String FAILURE = "exportvoicefilepasswordedit.xhtml";

    private VoiceFilePassword voiceFilePassword;
    private String mode;
    private String message;

    private String visaCardNo = "";
    private String masterCardNo = "";
    
    @ManagedProperty(value="#{voiceFilePasswordDAO}")
    private VoiceFilePasswordDAO voiceFilePasswordDAO;
    @ManagedProperty(value="#{cardIssuerDAO}")
    private CardIssuerDAO cardIssuerDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:exportvoicefilepassword:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            voiceFilePassword = new VoiceFilePassword();
            voiceFilePassword.setEnable(Boolean.TRUE);
            voiceFilePassword.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                voiceFilePassword = voiceFilePasswordDAO.findVoiceFilePassword(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (voiceFilePassword == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:exportvoicefilepassword:add");
        } else {
            return SecurityUtil.isPermitted("admin:exportvoicefilepassword:edit");
        }
    }
        
    public String saveAction() {
        try {
            try {
                if (getMode().equals("add")) {
                    voiceFilePassword.setId(null);
                    voiceFilePassword.setEnable(true);
                    voiceFilePassword.setCreateBy(JSFUtil.getUserSession().getUserName());
                    voiceFilePassword.setCreateDate(new Date());
                    voiceFilePasswordDAO.create(voiceFilePassword);
                } else {
                    voiceFilePassword.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    voiceFilePassword.setUpdateDate(new Date());
                    voiceFilePasswordDAO.edit(voiceFilePassword);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
            return FAILURE;
        }
    }

    public String backAction() {
        return SUCCESS;
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

    public String getVisaCardNo() {
        return visaCardNo;
    }

    public void setVisaCardNo(String visaCardNo) {
        this.visaCardNo = visaCardNo;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public CardIssuerDAO getCardIssuerDAO() {
        return cardIssuerDAO;
    }

    public void setCardIssuerDAO(CardIssuerDAO cardIssuerDAO) {
        this.cardIssuerDAO = cardIssuerDAO;
    }

    public VoiceFilePassword getVoiceFilePassword() {
        return voiceFilePassword;
    }

    public void setVoiceFilePassword(VoiceFilePassword voiceFilePassword) {
        this.voiceFilePassword = voiceFilePassword;
    }

    public VoiceFilePasswordDAO getVoiceFilePasswordDAO() {
        return voiceFilePasswordDAO;
    }

    public void setVoiceFilePasswordDAO(VoiceFilePasswordDAO voiceFilePasswordDAO) {
        this.voiceFilePasswordDAO = voiceFilePasswordDAO;
    }
}
