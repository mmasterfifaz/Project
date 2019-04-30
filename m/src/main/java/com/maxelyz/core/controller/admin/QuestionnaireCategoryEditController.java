package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaChoiceDAO;
import com.maxelyz.core.model.dao.QaQuestionDAO;
import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaQuestion;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
//@RequestScoped
@ViewScoped
public class QuestionnaireCategoryEditController {
 
    private static Logger log = Logger.getLogger(QuestionnaireCategoryEditController.class);
    private static String REDIRECT_PAGE = "questionnairecategory.jsf";
    private static String SUCCESS = "questionnairecategory.xhtml?faces-redirect=true";
    private static String FAILURE = "questionnairecategoryedit.xhtml";

    private QaCategory qaCategory;
    private List<QaQuestion> qaQuestionList;
    private List<QaChoice> qaChoiceList;
    private String message;
    private String messageDup;
    private String mode;
    public Boolean editUsed;
    
    @ManagedProperty(value = "#{qaCategoryDAO}")
    private QaCategoryDAO qaCategoryDAO;
    @ManagedProperty(value = "#{qaQuestionDAO}")
    private QaQuestionDAO qaQuestionDAO;
    @ManagedProperty(value = "#{qaChoiceDAO}")
    private QaChoiceDAO qaChoiceDAO;


    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:questionnairecategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            qaCategory = new QaCategory();
            qaCategory.setEnable(Boolean.TRUE);
            qaQuestionList = new ArrayList<QaQuestion>();
            for (int i=0;i<2;i++) {
                QaQuestion qaq = new QaQuestion();
                qaq.setSeqNo(i+1);
                qaq.setQaCategory(qaCategory);
                qaQuestionList.add(qaq);
            }
            editUsed = true;
        } else {
            mode = "edit";
            qaCategory = this.getQaCategoryDAO().findQaCategory(new Integer(selectedID));
            if(qaCategory == null){
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            }
            qaQuestionList = (List<QaQuestion>)qaCategory.getQaQuestionCollection();       
            
            editUsed = chkUsedQaCategory(qaCategory);   //Check Category used cannot edit
        }
    }
        
    public boolean chkUsedQaCategory(QaCategory qaCategory) {
        QaCategoryDAO dao = getQaCategoryDAO();
        if(dao.checkQaCategory(qaCategory).size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void createChoiceList() {
        for(QaQuestion q: qaQuestionList) {
            if( q.getQaChoiceCollection() != null){
                if(q.getQaChoiceCollection().isEmpty()) {
                    qaChoiceList = new ArrayList<QaChoice>();
                    for (int i=0;i<q.getNoChoice();i++) {
                        QaChoice qac = new QaChoice();  
                        qac.setSeqNo(i+1);
                        qaChoiceList.add(qac);
                    }
                } else if(q.getQaChoiceCollection().size() < q.getNoChoice()) {
                            qaChoiceList = (List<QaChoice>)q.getQaChoiceCollection();
                            for(int i=q.getQaChoiceCollection().size();i<q.getNoChoice();i++ ) {
                                QaChoice qac = new QaChoice();  
                                qac.setSeqNo(i+1);
                                qaChoiceList.add(qac);
                            }
                        } else if(q.getQaChoiceCollection().size() > q.getNoChoice()){
                                qaChoiceList = (List<QaChoice>)q.getQaChoiceCollection();
                                for (int i=q.getQaChoiceCollection().size() ;i>q.getNoChoice();i--) {
                                    qaChoiceList.remove(i-1);
                                }
                        } else if(q.getQaChoiceCollection().size() == q.getNoChoice()){
                                    qaChoiceList = (List<QaChoice>)q.getQaChoiceCollection();
                        }
                q.setQaChoiceCollection(qaChoiceList);
            } else {
                    qaChoiceList = new ArrayList<QaChoice>();
                    for (int i=0;i<q.getNoChoice();i++) {
                        QaChoice qac = new QaChoice();  
                        qac.setSeqNo(i+1);
                        qaChoiceList.add(qac);
                    }
                    q.setQaChoiceCollection(qaChoiceList);
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:questionnairecategory:add");  //qa permmit
       } else {
 	   return SecurityUtil.isPermitted("admin:questionnairecategory:edit");  //qa permmit
       }
    }

    public String saveAction() {
        messageDup = "";
        if(checkName(qaCategory)) {
            try {
                String username = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                if(mode.equals("add")) {
                    qaCategory.setEnable(true);
                    qaCategory.setCreateBy(username);
                    qaCategory.setCreateDate(now);
                    this.getQaCategoryDAO().create(qaCategory);

                    for(QaQuestion qaq: qaQuestionList) {
                        qaq.setQaCategory(qaCategory);
                        qaChoiceList = (List<QaChoice>)qaq.getQaChoiceCollection();
                        qaq.setQaChoiceCollection(null);
                        this.getQaQuestionDAO().create(qaq);

                        if(qaChoiceList != null) {
                            for (QaChoice qac : qaChoiceList) {
                                qac.setQaQuestion(qaq);
                                this.getQaChoiceDAO().create(qac);
                            }
                            qaq.setQaChoiceCollection(qaChoiceList);
                        }

                        this.getQaQuestionDAO().edit(qaq);
                    }
                    qaCategory.setQaQuestionCollection(qaQuestionList);
                    this.getQaCategoryDAO().edit(qaCategory);
                } else {
                    qaCategory.setUpdateBy(username);
                    qaCategory.setUpdateDate(now);

                    for(QaQuestion qaq: qaQuestionList) {
                        qaChoiceList = (List<QaChoice>)qaq.getQaChoiceCollection();
                        if(qaChoiceList != null) {
                            for(QaChoice qac: qaChoiceList){
                                qac.setQaQuestion(qaq);
                            }
                            qaq.setQaChoiceCollection(qaChoiceList);
                        }
                    }
                    qaCategory.setQaQuestionCollection(qaQuestionList);
                    this.getQaCategoryDAO().editQa(qaCategory);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
         } else {
            messageDup = " Name is already taken";
            return null;            
        }           
    }
   
    public Boolean checkName(QaCategory qaCategory) {
        String name = qaCategory.getName();
        Integer id=0; 
        if(qaCategory.getId() != null)
            id = qaCategory.getId();
        QaCategoryDAO dao = getQaCategoryDAO();
        
        Integer cnt = dao.checkQaCategoryName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    //Listener
    private void reCalculateSeqNo() {
        int seqNo=0;
        for (QaQuestion q: qaQuestionList) {
            q.setSeqNo(++seqNo);
        }
    }  
    
    public void addQuestionListener(ActionEvent event) {
        int size = qaQuestionList.size();
        QaQuestion q = new QaQuestion();
        q.setSeqNo(size+1);
        q.setQaCategory(qaCategory);
        qaQuestionList.add(q);
    }
            
    public void insertQuestionListener(ActionEvent event) {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        QaQuestion q = new QaQuestion();
        q.setQaCategory(qaCategory);
        qaQuestionList.add(seqNo-1,q);
        reCalculateSeqNo();        
    }
  
    public void deleteQuestionListener(ActionEvent event) {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        qaQuestionList.remove(seqNo - 1);
        reCalculateSeqNo();
    }
    
    public void changeTypeListener() {
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void chkDefault() {
        int questionNo = Integer.parseInt(JSFUtil.getRequestParameterMap("questionNo"));
        int choiceNo = Integer.parseInt(JSFUtil.getRequestParameterMap("choiceNo"));
        for(QaQuestion q: qaQuestionList) {
            if(q.getSeqNo() == questionNo) {
                for(QaChoice qac: q.getQaChoiceCollection()) {
                    if(qac.getSeqNo() != choiceNo)
                        qac.setDefault1(false);
                }
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    //List
    public Map<String,String> getTypeList() {
        return QaCategoryDAO.getTypeList();
    }
    
    public Map<String,Integer> getItemList() {
        return QaCategoryDAO.getItemList();
    }
    
    public QaCategory getQaCategory() {
        return qaCategory;
    }

    public void setQaCategory(QaCategory qaCategory) {
        this.qaCategory = qaCategory;
    }

    public List<QaQuestion> getQaQuestionList() {
        return qaQuestionList;
    }

    public void setQaQuestionList(List<QaQuestion> qaQuestionList) {
        this.qaQuestionList = qaQuestionList;
    }
  
    public List<QaChoice> getQaChoiceList() {
        return qaChoiceList;
    }

    public void setQaChoiceList(List<QaChoice> qaChoiceList) {
        this.qaChoiceList = qaChoiceList;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
 
    //Managed Properties
    public QaCategoryDAO getQaCategoryDAO() {
        return qaCategoryDAO;
    }

    public void setQaCategoryDAO(QaCategoryDAO qaCategoryDAO) {
        this.qaCategoryDAO = qaCategoryDAO;
    }
    
    public QaQuestionDAO getQaQuestionDAO() {
        return qaQuestionDAO;
    }

    public void setQaQuestionDAO(QaQuestionDAO qaQuestionDAO) {
        this.qaQuestionDAO = qaQuestionDAO;
    }
    
    public QaChoiceDAO getQaChoiceDAO() {
        return qaChoiceDAO;
    }

    public void setQaChoiceDAO(QaChoiceDAO qaChoiceDAO) {
        this.qaChoiceDAO = qaChoiceDAO;
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
