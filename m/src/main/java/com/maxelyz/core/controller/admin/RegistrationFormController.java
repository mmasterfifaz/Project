package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.RegistrationFormDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.RegistrationForm;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.*;
import javax.transaction.UserTransaction;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@RequestScoped
public class RegistrationFormController implements Serializable {

    private static String REFRESH = "registrationform.xhtml?faces-redirect=true";
    private static String EDIT = "registrationformedit.xhtml";
    private static String FIELD = "registrationformfield.xhtml";
    private static String CUSTOMFIELD = "registrationformcustomfield.xhtml";
    private static String QUESTIONNAIRE = "registrationformquestionnaireedit.xhtml";
    private static Log log = LogFactory.getLog(RegistrationFormController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<RegistrationForm> registrationForms;
    private String keyword;
    private RegistrationForm reg;
    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:registration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        RegistrationFormDAO dao = getRegistrationFormDAO();
        registrationForms = dao.findRegistrationFormEntities();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:registration:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:registration:delete");
    }

    public List<RegistrationForm> getList() {
        return getRegistrationForms();
    }

    public String searchAction() {
        RegistrationFormDAO dao = getRegistrationFormDAO();
        registrationForms = dao.findRegistrationFormByName(keyword);
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String registrationFieldAction() {
        return FIELD;
    }

    public String questionnaireAction() {
        return QUESTIONNAIRE;
    }

    public String deleteAction() throws Exception {
        RegistrationFormDAO dao = getRegistrationFormDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    reg = dao.findRegistrationForm(item.getKey());
                    reg.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    reg.setUpdateDate(new Date());
                    reg.setEnable(false);
                    dao.edit(reg);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String copyAction() {
        RegistrationFormDAO dao = getRegistrationFormDAO();
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            
            RegistrationForm registrationForm = this.getRegistrationFormDAO().findRegistrationForm(new Integer(selectedID));
            RegistrationForm registrationFormCopy = new RegistrationForm();
            registrationFormCopy.setDescription(registrationForm.getDescription());
            registrationFormCopy.setEnable(registrationForm.getEnable());
            registrationFormCopy.setName(registrationForm.getName()+" (2) ");
            registrationFormCopy.setQaForm(registrationForm.getQaForm());
            registrationFormCopy.setCreateBy(username);
            registrationFormCopy.setCreateDate(now);
            
            ArrayList<RegistrationField> registrationFieldList = new ArrayList<RegistrationField>();
            for(RegistrationField field: registrationForm.getRegistrationFieldCollection()) {
                RegistrationField fieldCopy = new RegistrationField();
                fieldCopy.setRegistrationForm(registrationFormCopy);
                fieldCopy.setName(field.getName());
                fieldCopy.setRequired(field.getRequired());
                fieldCopy.setCustomName(field.getCustomName());
                fieldCopy.setSeqNo(field.getSeqNo());
                fieldCopy.setGroupNo(field.getGroupNo());
                fieldCopy.setControlType( field.getControlType());
                fieldCopy.setItems(field.getItems());
                registrationFieldList.add(fieldCopy);
            }
            registrationFormCopy.setRegistrationFieldCollection(registrationFieldList);
            dao.create(registrationFormCopy);
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
 
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<RegistrationForm> getRegistrationForms() {
        return registrationForms;
    }

    public void setRegistrationForms(List<RegistrationForm> registrationForms) {
        this.registrationForms = registrationForms;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the registrationFormDAO
     */
    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    /**
     * @param registrationFormDAO the registrationFormDAO to set
     */
    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }
}
