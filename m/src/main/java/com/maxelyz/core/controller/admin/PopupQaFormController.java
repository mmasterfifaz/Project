/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaForm;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaQuestion;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import com.maxelyz.utils.JSFUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import com.maxelyz.core.service.QuestionnaireService;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author vee
 */
@ManagedBean(name="popupQaFormController")
@RequestScoped
@ViewScoped
public class PopupQaFormController {
    private static Logger log = Logger.getLogger(PopupQaFormController.class);
    private static String REDIRECT_PAGE = "questionnaire.jsf";
    private QaForm qaForm;
    private List<QaSelectedCategory> qaSelectedCategoryList;
    private List<QaQuestion> qaQuestionList;
    private List<QaChoice> qaChoiceList;
    @ManagedProperty(value = "#{questionnaireService}")
    private QuestionnaireService questionnaireService;
    
    @PostConstruct
    public void initialize() {

    }
    
    public void qaFormgListener(ActionEvent event){
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);    
        } else {
            qaForm = questionnaireService.findQaForm(new Integer(selectedID));
            qaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qaForm);
        }
    }
       
   //Managed Properties
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }
    
    //List to UI
    public QaForm getQaForm() {
        return qaForm;
    }

    public void setQaForm(QaForm qaForm) {
        this.qaForm = qaForm;
    }
    
    public List<QaSelectedCategory> getQaSelectedCategoryList() {
        return qaSelectedCategoryList;
    }

    public void setQaSelectedCategory(List<QaSelectedCategory> qaSelectedCategoryList) {
        this.qaSelectedCategoryList = qaSelectedCategoryList;
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
    
    
}
