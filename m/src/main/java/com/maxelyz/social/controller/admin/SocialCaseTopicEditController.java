/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;


import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SocialCaseTopicEditController {
    private static Logger log = Logger.getLogger(SocialCaseTypeEditController.class);
    private static String REDIRECT_PAGE = "socialcasetopic.jsf";
    private static String SUCCESS = "socialcasetopic.xhtml?faces-redirect=true";
    private static String FAILURE = "socialcasetopicedit.xhtml";

    private SoCaseType soCaseType;
    private Integer parentId;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:socialcasetopic:view"))
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
            mode = "add";
            soCaseType = new SoCaseType();
            soCaseType.setEnable(Boolean.TRUE);
            soCaseType.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            SoCaseTypeDAO dao = getSoCaseTypeDAO();
            soCaseType = dao.findSoCaseType(new Integer(selectedID));
            if (soCaseType==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    parentId = soCaseType.getSoCaseType().getId();
                } catch (NullPointerException e) {
                    parentId = 0;
                }
            }
       }
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:socialcasetopic:add");
        } else {
            return SecurityUtil.isPermitted("admin:socialcasetopic:edit");
        }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(soCaseType)) {
            SoCaseTypeDAO dao = getSoCaseTypeDAO();
            String username = JSFUtil.getUserSession().getUserName();

            try {
                if (getMode().equals("add")) {
                    soCaseType.setId(null);
                    soCaseType.setSoCaseType(new SoCaseType(parentId));
                    soCaseType.setCreateBy(username);
                    soCaseType.setCreateDate(new Date());
                    dao.create(soCaseType);
                } else {
                    soCaseType.setSoCaseType(new SoCaseType(parentId));
                    soCaseType.setUpdateBy(username);
                    soCaseType.setUpdateDate(new Date());
                    dao.edit(soCaseType);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
//                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;
//            return FAILURE;
        }
    }

    public Boolean checkCode(SoCaseType soCaseType) {
        String code = soCaseType.getCode();
        Integer id=0;
        if(soCaseType.getId() != null)
            id = soCaseType.getId();
        SoCaseTypeDAO dao = getSoCaseTypeDAO();

        Integer cnt = dao.checkSoCaseTopicCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
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

    public SoCaseType getSoCaseType() {
        return soCaseType;
    }

    public void setSoCaseType(SoCaseType soCaseType) {
        this.soCaseType = soCaseType;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    
    public Map<String, Integer> getSoCaseTypeList() {
        SoCaseTypeDAO dao = getSoCaseTypeDAO();
        List<SoCaseType> soCaseTypeList = dao.findSoCaseTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoCaseType obj : soCaseTypeList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
