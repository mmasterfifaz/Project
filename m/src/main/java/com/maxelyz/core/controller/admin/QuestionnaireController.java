package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaFormDAO;
import com.maxelyz.core.model.dao.QaSelectedCategoryDAO;
import com.maxelyz.core.model.entity.QaForm;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class QuestionnaireController implements Serializable {

    private static Logger log = Logger.getLogger(QuestionnaireController.class);
    private static String REFRESH = "questionnaire.xhtml?faces-redirect=true";
    private static String EDIT = "questionnaireedit.xhtml";
    private static String PREVIEW = "popupQaForm.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<QaForm> qaFormList;
    @ManagedProperty(value = "#{qaFormDAO}")
    private QaFormDAO qaFormDAO;
    @ManagedProperty(value = "#{qaSelectedCategoryDAO}")
    private QaSelectedCategoryDAO qaSelectedCategoryDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:questionnaire:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        qaFormList = this.getQaFormDAO().findQaFormEntities();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:questionnaire:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:questionnaire:delete");
    }
    
    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:questionnaire:edit");
    }
        
    public List<QaForm> getList() {
        return qaFormList;
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    QaForm qaForm = this.getQaFormDAO().findQaForm(item.getKey());
                    qaForm.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    qaForm.setUpdateDate(new Date());
                    qaForm.setEnable(false);
                    this.getQaFormDAO().edit(qaForm);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String copyAction() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        
        QaForm qaForm = this.getQaFormDAO().findQaForm(new Integer(selectedID));
        try {     
            QaForm qaFormCopy = new QaForm();
            qaFormCopy.setEnable(Boolean.TRUE);
            qaFormCopy.setName(qaForm.getName()+" (2)");
            qaFormCopy.setDescription(qaForm.getDescription());
            qaFormCopy.setCreateBy(username);
            qaFormCopy.setCreateDate(now);
            this.getQaFormDAO().create(qaFormCopy);
            List<QaSelectedCategory> qaSelectedCategoryList = new ArrayList<QaSelectedCategory>();
            for(QaSelectedCategory o: qaForm.getQaSelectedCategoryCollection()) {
                QaSelectedCategory qaSelected = new QaSelectedCategory(qaFormCopy.getId(),o.getQaCategory().getId());
                qaSelected.setSeqNo(o.getSeqNo());
                this.getQaSelectedCategoryDAO().create(qaSelected);
                qaSelectedCategoryList.add(qaSelected);
            }
            qaFormCopy.setQaSelectedCategoryCollection(qaSelectedCategoryList);
            this.getQaFormDAO().edit(qaFormCopy);
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String previewAction() {
        return PREVIEW;
    }
        
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public QaFormDAO getQaFormDAO() {
        return qaFormDAO;
    }

    public void setQaFormDAO(QaFormDAO qaFormDAO) {
        this.qaFormDAO = qaFormDAO;
    }

    public QaSelectedCategoryDAO getQaSelectedCategoryDAO() {
        return qaSelectedCategoryDAO;
    }

    public void setQaSelectedCategoryDAO(QaSelectedCategoryDAO qaSelectedCategoryDAO) {
        this.qaSelectedCategoryDAO = qaSelectedCategoryDAO;
    }
}
