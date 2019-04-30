/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.*;
import java.util.List;

/**
 *
 * @author oat
 */
public interface QuestionnaireService {
    public void createQaForm(QaForm qaForm) throws Exception;
    public void createQaCategory(QaCategory qaCategory) throws Exception;
    public void editQaForm(QaForm qaForm) throws Exception;
    public void editQaCategory(QaCategory qaCategory) throws Exception;
    public List<QaForm> findQaForm();
    public List<QaCategory> findQaCategory();
    public QaForm findQaForm(Integer id);
    public List<QaSelectedCategory> findQaSelectedCategoryByFormId(QaForm qaForm);
    public List<QaQuestion> findQaQuestion(Integer id);
    public List<QaQuestion> findQaQuestionByCategoryId(QaCategory qaCategory);
    public List<QaChoice> findQaChoiceByQuestionId(QaQuestion qaQuestion);
}
