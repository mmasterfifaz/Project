/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaQuestionDAO;
import com.maxelyz.core.model.dao.QaChoiceDAO;
import com.maxelyz.core.model.dao.QaFormDAO;
import com.maxelyz.core.model.dao.QaTransDAO;
import com.maxelyz.core.model.dao.QaTransDetailDAO;
import com.maxelyz.core.model.dao.QaSelectedCategoryDAO;
import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaForm;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaQuestion;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import com.maxelyz.core.model.entity.QaTrans;
import com.maxelyz.core.model.entity.QaTransDetail;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */

@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService{
    private static Logger log = Logger.getLogger(QuestionnaireServiceImpl.class);
    private QaCategoryDAO qaCategoryDAO;
    private QaQuestionDAO qaQuestionDAO;
    private QaChoiceDAO qaChoiceDAO;
    private QaFormDAO qaFormDAO;
    private QaTransDAO qaTransDAO;
    private QaTransDetailDAO qaTransDetaiDAO;
    private QaSelectedCategoryDAO qaSelectedCategoryDAO;
    
    @Override
    public void createQaForm(QaForm qaForm) throws Exception {
        
    }
    @Override
    public void createQaCategory(QaCategory qaCategory) throws Exception {
        
    }
    @Override
    public void editQaForm(QaForm qaForm) throws Exception {
        
    }
    @Override
    public void editQaCategory(QaCategory qaCategory) throws Exception {
        
    }
    @Override
    public List<QaForm> findQaForm() {
        return null;
    }
    @Override
    public QaForm findQaForm(Integer id) {
        return qaFormDAO.findQaForm(id);
    }
    @Override
    public List<QaCategory> findQaCategory(){
        return qaCategoryDAO.findQaCategoryEntities();
    }
    @Override
    public List<QaSelectedCategory> findQaSelectedCategoryByFormId(QaForm qaForm) {
        return qaSelectedCategoryDAO.findQaSelectedCategoryByFormId(qaForm);
    }
    @Override
    public List<QaQuestion> findQaQuestionByCategoryId(QaCategory qaCategory) {
        return qaQuestionDAO.findQaQuestionByCategoryId(qaCategory);
    }
    @Override
    public List<QaChoice> findQaChoiceByQuestionId(QaQuestion qaQuestion) {
        return qaChoiceDAO.findQaChoiceByQuestionId(qaQuestion);
    }
    //---DAO---
    public QaCategoryDAO getQaCategoryDAO() {
        return qaCategoryDAO;
    }

    public void setQaCategoryDAO(QaCategoryDAO qaCategoryDAO) {
        this.qaCategoryDAO = qaCategoryDAO;
    }

    public QaChoiceDAO getQaChoiceDAO() {
        return qaChoiceDAO;
    }

    public void setQaChoiceDAO(QaChoiceDAO qaChoiceDAO) {
        this.qaChoiceDAO = qaChoiceDAO;
    }

    public QaFormDAO getQaFormDAO() {
        return qaFormDAO;
    }

    public void setQaFormDAO(QaFormDAO qaFormDAO) {
        this.qaFormDAO = qaFormDAO;
    }

    public QaQuestionDAO getQaQuestionDAO() {
        return qaQuestionDAO;
    }

    public void setQaQuestionDAO(QaQuestionDAO qaQuestionDAO) {
        this.qaQuestionDAO = qaQuestionDAO;
    }

    public QaSelectedCategoryDAO getQaSelectedCategoryDAO() {
        return qaSelectedCategoryDAO;
    }

    public void setQaSelectedCategoryDAO(QaSelectedCategoryDAO qaSelectedCategoryDAO) {
        this.qaSelectedCategoryDAO = qaSelectedCategoryDAO;
    }

    public List<QaQuestion> findQaQuestion(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public QaTransDAO getQaTransDAO() {
        return qaTransDAO;
    }

    public void setQaTransDAO(QaTransDAO qaQuestionDAO) {
        this.qaTransDAO = qaQuestionDAO;
    }
    
    public QaTransDetailDAO getQaTransDetaiDAO() {
        return qaTransDetaiDAO;
    }

    public void setQaTransDetaiDAO(QaTransDetailDAO qaTransDetaiDAO) {
        this.qaTransDetaiDAO = qaTransDetaiDAO;
    }
}
