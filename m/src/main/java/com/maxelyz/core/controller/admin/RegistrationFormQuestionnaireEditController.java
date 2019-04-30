package com.maxelyz.core.controller.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.RegistrationFormDAO;
import com.maxelyz.core.model.entity.RegistrationForm;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class RegistrationFormQuestionnaireEditController {
    private static Log log = LogFactory.getLog(RegistrationFormQuestionnaireEditController.class);
    private static String SUCCESS = "registrationform.xhtml?faces-redirect=true";
    private static String FAILURE = "registrationform.xhtml";

    //private RegistrationForm registrationForm;
    private String mode;

    private RegistrationForm registrationForm;
    private int registrationFormId;

    @ManagedProperty(value="#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;

    @PostConstruct
    public void initialize() {
       String selectedID = (String) FacesContext.getCurrentInstance()
               .getExternalContext().getRequestParameterMap()
               .get("selectedID");

        String registrationFormID = (String) FacesContext.getCurrentInstance()
            .getExternalContext().getRequestParameterMap()
            .get("registrationFormID");
        

       if (registrationFormID==null || registrationFormID.isEmpty()) {
            registrationFormID = null;
            registrationFormId = 0;
       } else {
            RegistrationFormDAO daoRegistrationForm = getRegistrationFormDAO();
            registrationForm = daoRegistrationForm.findRegistrationForm(new Integer(registrationFormID));
            registrationFormId = registrationForm.getId();
       }
        /*
       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           registrationForm = new RegistrationForm();
       } else {
           mode = "edit";
           RegistrationFormDAO dao = getRegistrationFormDAO();
           registrationForm = dao.findRegistrationForm(new Integer(selectedID));
       }*/
    }

    public String saveAction() {
        RegistrationFormDAO dao = getRegistrationFormDAO();
        try {
            if (getMode().equals("add")) {
                getRegistrationForm().setId(null);//fix some bug
                getRegistrationForm().setEnable(true);
         //       dao.create(getRegistrationForm());
            } else {
                getRegistrationForm().setEnable(true);
           //     dao.edit(getRegistrationForm());
            }
        } catch (Exception e) {
            //log.error("error oat:"+e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
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
