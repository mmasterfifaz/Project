/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ChildRegTypeDAO;
import com.maxelyz.core.model.entity.ChildRegType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author TBN
 */
@ManagedBean
@RequestScoped
public class ChildRegTypeEditController {
    public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(ChildRegFormEditController.class);
    private static String SUCCESS = "childregtype.xhtml?faces-redirect=true";
    private static String FAILURE = "childregtypeedit.xhtml";

    private ChildRegType childRegType;
    private List<ChildRegType> childRegTypes;
    private String mode;
    private String message;
    private String messageDup;
    @ManagedProperty(value = "#{childRegTypeDAO}")
    private ChildRegTypeDAO childRegTypeDAO;

    public int count_seqNo=0;
    public int count_seqNo2=0;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:childregtype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = JSFUtil.getRequestParameterMap("selectedID");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            childRegType = new ChildRegType();
            ChildRegTypeDAO dao = getChildRegTypeDAO();
            childRegTypes = dao.findChildRegTypeEntities();
           // declarationForm.setQuestionnaire(new Questionnaire());

        } else {
            mode = "edit";
            ChildRegTypeDAO dao = getChildRegTypeDAO();
            childRegType = dao.findChildRegType(new Integer(selectedID));
        }
        
        
        
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:childregtype:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:childregtype:edit"); 
       }
    }  
          

    public String saveAction() {
        messageDup = "";
        if(checkName(childRegType)) {
            ChildRegTypeDAO dao = getChildRegTypeDAO();
            try {
                String userName = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
//                declarationForm.setEnable(true); 
                childRegType.setUpdateBy(userName);
                childRegType.setUpdateDate(now);
                childRegType.setEnable(Boolean.TRUE);
                if(childRegType.getSeqNo() == null)
                {
                    childRegType.setSeqNo(1);
                }
                
                if (getMode().equals("add")) {
                    childRegType.setId(null);
                    childRegType.setCreateBy(userName);
                    childRegType.setCreateDate(now);
//                    childRegType.setSeqNo(childRegTypes.size());
                    childRegType.setRules("");
                    
                    dao.create(childRegType);
                } else {       
                    dao.edit(childRegType);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
         } else {
            messageDup = " Name is already taken";
            return null;
//            return FAILURE;            
        }
    }

    public Boolean checkName(ChildRegType childRegType) {
        String name = childRegType.getName();
        Integer id=0; 
        if(childRegType.getId() != null)
            id = childRegType.getId();
        ChildRegTypeDAO dao = getChildRegTypeDAO();
        
        Integer cnt = dao.checkChildRegTypeName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    
    public ChildRegType getChildRegType() {
        return childRegType;
    }

    public void setChildRegType(ChildRegType childRegType) {
        this.childRegType = childRegType;
    }
    
    public String getMessage() {
        return message;
    }

    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    //Managed Properties
    public ChildRegTypeDAO getChildRegTypeDAO() {
        return childRegTypeDAO;
    }

    public void setChildRegTypeDAO(ChildRegTypeDAO childRegTypeDAO) {
        this.childRegTypeDAO = childRegTypeDAO;
    }
/*
    public QuestionnaireDAO getQuestionnaireDAO() {
        return questionnaireDAO;
    }

    public void setQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }
*/

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
    public Map<String, Integer> getChildRegTypeList() {
        return this.getChildRegTypeDAO().getChildRegTypeList();
    }
}
