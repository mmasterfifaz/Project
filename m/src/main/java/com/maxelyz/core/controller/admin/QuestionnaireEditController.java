package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.QuestionnaireDAO;
import com.maxelyz.core.model.dao.QaFormDAO;
import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaSelectedCategoryDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.core.service.QuestionnaireService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;

@ManagedBean
//@RequestScoped
@ViewScoped
public class QuestionnaireEditController {

    private static Logger log = Logger.getLogger(QuestionnaireEditController.class);
    private static String REDIRECT_PAGE = "questionnaire.jsf";
    private static String SUCCESS = "questionnaire.xhtml?faces-redirect=true";
    private static String FAILURE = "questionnaireedit.xhtml";
    private String message;
    private String messageDup;
    @ManagedProperty(value = "#{qaSelectedCategoryDAO}")
    private QaSelectedCategoryDAO qaSelectedCategoryDAO;
    @ManagedProperty(value = "#{qaCategoryDAO}")
    private QaCategoryDAO qaCategoryDAO;
    @ManagedProperty(value = "#{qaFormDAO}")
    private QaFormDAO qaFormDAO;
    @ManagedProperty(value = "#{questionnaireService}")
    private QuestionnaireService questionnaireService;
    private Map<String, Integer> qaCatergoryList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedCategory;
    private List<QaSelectedCategory> qaSelectedCategoryList;
    private QaForm qaForm;
    private String mode;
    public Integer i;
    public Boolean editUsed;
 
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:questionnaire:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            qaForm = new QaForm();
            qaForm.setEnable(Boolean.TRUE);
            List<QaCategory> qaCategorys = questionnaireService.findQaCategory();
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
            for (QaCategory o : qaCategorys) {
                values.put(o.getName(), o.getId());
            }
            qaCatergoryList = values;
            
            editUsed = true;
        } else {
            mode = "edit";
            qaForm = this.getQaFormDAO().findQaForm(new Integer(selectedID));
            List<Integer> val = new ArrayList<Integer>();
            qaSelectedCategoryList = this.getQaSelectedCategoryDAO().findQaSelectedCategoryByFormId(qaForm);
            for(QaSelectedCategory o: qaSelectedCategoryList){
                val.add(o.getQaCategory().getId());
            }
            selectedCategory = val;
            
            List<QaCategory> qaCategorys = questionnaireService.findQaCategory();
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
            for (QaCategory o : qaCategorys) {
                values.put(o.getName(), o.getId());
            }
            qaCatergoryList = values;
            
            editUsed = chkUsedQaForm(qaForm);   //Check Form used cannot edit
            
            if (qaForm==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            } 
        }
    }
    
    public boolean chkUsedQaForm(QaForm qaForm) {
        QaFormDAO dao = getQaFormDAO();
        if(dao.checkQaForm(qaForm).size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:questionnaire:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:questionnaire:edit"); 
       }
    }  
      
    public String saveAction() {
        messageDup = "";
        if(checkName(qaForm)) {
            try {
                String username = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                if (mode.equals("add")) {
                    qaForm.setCreateBy(username);
                    qaForm.setCreateDate(now);
                    this.getQaFormDAO().create(qaForm);
                    i = 1;
                    qaSelectedCategoryList = new ArrayList<QaSelectedCategory>();
                    for(Integer o: selectedCategory) {
                        QaSelectedCategory qaSelected = new QaSelectedCategory(qaForm.getId(),qaCategoryDAO.findQaCategory(o).getId());
                        qaSelected.setSeqNo(i++);
                        this.getQaSelectedCategoryDAO().create(qaSelected);
                        qaSelectedCategoryList.add(qaSelected);
                    }
                    qaForm.setQaSelectedCategoryCollection(qaSelectedCategoryList);
                    this.getQaFormDAO().edit(qaForm);
                }
                else {
                    qaForm.setUpdateBy(username);
                    qaForm.setUpdateDate(now);
                    i = 1;
                    qaSelectedCategoryList = new ArrayList<QaSelectedCategory>();
                    for(Integer o: selectedCategory) {
                        QaSelectedCategory qaSelected = new QaSelectedCategory(qaForm.getId(),qaCategoryDAO.findQaCategory(o).getId());
                        qaSelected.setSeqNo(i++);
                        qaSelectedCategoryList.add(qaSelected);
                    }
                    qaForm.setQaSelectedCategoryCollection(qaSelectedCategoryList);
                    this.getQaFormDAO().editQaForm(qaForm);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            message = " Name is already taken";
            return null;            
        }
    }

    public Boolean checkName(QaForm qaForm) {
        String name = qaForm.getName();
        Integer id=0; 
        if(qaForm.getId() != null)
            id = qaForm.getId();
        QaFormDAO dao = getQaFormDAO();
        
        Integer cnt = dao.checkQaFormName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }   
    
    //List
    public QaForm getQaForm() {
        return qaForm;
    }

    public void setQaForm(QaForm qaForm) {
        this.qaForm = qaForm;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //Managed Properties
    
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }
    
    public QaFormDAO getQaFormDAO() {
        return qaFormDAO;
    }

    public void setQaFormDAO(QaFormDAO qaFormDAO) {
        this.qaFormDAO = qaFormDAO;
    }
    
    public QaCategoryDAO getQaCategoryDAO() {
        return qaCategoryDAO;
    }

    public void setQaCategoryDAO(QaCategoryDAO qaCategoryDAO) {
        this.qaCategoryDAO = qaCategoryDAO;
    }
        
    public QaSelectedCategoryDAO getQaSelectedCategoryDAO() {
        return qaSelectedCategoryDAO;
    }

    public void setQaSelectedCategoryDAO(QaSelectedCategoryDAO qaSelectedCategoryDAO) {
        this.qaSelectedCategoryDAO = qaSelectedCategoryDAO;
    }
    //list to UI

    public Map<String, Integer> getQaCatergoryList() {
        return qaCatergoryList;
    }
    
    public List<Integer> getSelectedCategory() {
        return selectedCategory;
    }
    
    public void setSelectedCategory(List<Integer> selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Boolean getEditUsed() {
        return editUsed;
    }

    public void setEditUsed(Boolean editUsed) {
        this.editUsed = editUsed;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
