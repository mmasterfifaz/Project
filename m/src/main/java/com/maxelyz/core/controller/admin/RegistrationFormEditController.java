package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.QaFormDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.RegistrationFormDAO;
//import com.maxelyz.core.model.entity.Questionnaire;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.RegistrationForm;
import com.maxelyz.core.model.entity.QaForm;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;

@ManagedBean
@ViewScoped
public class RegistrationFormEditController {

    public static int MAX_FLEX_FIELD = 50;
    private static Log log = LogFactory.getLog(RegistrationFormEditController.class);
    private static String SUCCESS = "registrationform.xhtml?faces-redirect=true";
    private static String FAILURE = "registrationformedit.xhtml";
    private static String FIELD_NAME[][] = {
        {"Group Title", "grouptitle"},
        {"Initial", "initial"},
        {"Name", "name"},
        {"Surname", "surname"},
        {"ID Card Type", "idcardtypeid"},
        {"ID Card", "idno"},
        {"ID Card Issue", "idcardissue"},
        {"ID Card Expiry Date", "idcardexpirydate"},
        {"Date of Birth", "dob"},
        {"Gender", "gender"},
        {"Weight", "weight"},
        {"Height", "height"},
        {"Marital Status", "maritalstatus"},
        {"BMI", "bmi"},
        {"Current Address", "currentaddress"},
        {"Current Full Name", "currentaddressfullname"},
        {"Current Addr1", "currentaddress1"},
        {"Current Addr2", "currentaddress2"},
        {"Current Addr3", "currentaddress3"},
        {"Current Addr4", "currentaddress4"},
        {"Current Addr5", "currentaddress5"},
        {"Current Addr6", "currentaddress6"},
        {"Current Addr7", "currentaddress7"},
        {"Current Addr8", "currentaddress8"},
        {"Current Province", "currentaddressprovince"},
        {"Current Postal", "currentaddresspostal"},
        {"Current Tel1", "currentaddresstelephone1"},
        {"Current Tel2", "currentaddresstelephone2"},
        
        {"Home Address", "homeaddress"},
        {"Home Full Name", "homeaddressfullname"},
        {"Home Addr1", "homeaddress1"},
        {"Home Addr2", "homeaddress2"},
        {"Home Addr3", "homeaddress3"},
        {"Home Addr4", "homeaddress4"},
        {"Home Addr5", "homeaddress5"},
        {"Home Addr6", "homeaddress6"},
        {"Home Addr7", "homeaddress7"},
        {"Home Addr8", "homeaddress8"},
        {"Home Province", "homeaddressprovince"},
        {"Home Postal", "homeaddresspostal"},
        {"Home Tel1", "homeaddresstelephone1"},
        {"Home Tel2", "homeaddresstelephone2"},
        
        {"Billing Address", "billingaddress"},
        {"Billing Full Name", "billingaddressfullname"},
        {"Billing Addr1", "billingaddress1"},
        {"Billing Addr2", "billingaddress2"},
        {"Billing Addr3", "billingaddress3"},
        {"Billing Addr4", "billingaddress4"},
        {"Billing Addr5", "billingaddress5"},
        {"Billing Addr6", "billingaddress6"},
        {"Billing Addr7", "billingaddress7"},
        {"Billing Addr8", "billingaddress8"},
        {"Billing Province", "billingaddressprovince"},
        {"Billing Postal", "billingaddresspostal"},
        {"Billing Tel1", "billingaddresstelephone1"},
        {"Billing Tel2", "billingaddresstelephone2"},

        {"Shipping Address", "shippingaddress"},
        {"Shipping Full Name", "shippingaddressfullname"},
        {"Shipping Addr1", "shippingaddress1"},
        {"Shipping Addr2", "shippingaddress2"},
        {"Shipping Addr3", "shippingaddress3"},
        {"Shipping Addr4", "shippingaddress4"},
        {"Shipping Addr5", "shippingaddress5"},
        {"Shipping Addr6", "shippingaddress6"},
        {"Shipping Addr7", "shippingaddress7"},
        {"Shipping Addr8", "shippingaddress8"},
        {"Shipping Province", "shippingaddressprovince"},
        {"Shipping Postal", "shippingaddresspostal"},
        {"Shipping Tel1", "shippingaddresstelephone1"},
        {"Shipping Tel2", "shippingaddresstelephone2"},
        
        {"Home Phone", "homephone"},
        {"Office Phone", "officephone"},
        {"Mobile Phone", "mobilephone"},
        {"Email", "email"},
        {"Nationality", "nationality"},
        {"Race", "race"},
        {"Religion", "religion"},
        {"Occupation", "occupation"},
        {"Occupation List", "occupationlist"},
        {"Job Description", "jobdescription"},
        {"Job Position", "position"},
        {"Business Type", "businesstype"},
        {"Income", "income"}};
    private List<ArrayList<RegistrationField>> registrationFieldInfoValues = new ArrayList<ArrayList<RegistrationField>>();
    private Map<String, Integer> qaFormList;
    private RegistrationForm registrationForm;
    private String mode;
    private String message;
    private String messageDup;
    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;
    
    @ManagedProperty(value = "#{qaFormDAO}")
    private QaFormDAO qaFormDAO;
        
    private Integer qaId;
    private Integer qcQaFormId;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:registration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = JSFUtil.getRequestParameterMap("selectedID");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            registrationForm = new RegistrationForm();
           // registrationForm.setQuestionnaire(new Questionnaire());
            registrationForm.setQaForm(new QaForm());
        } else {
            mode = "edit";
            RegistrationFormDAO dao = getRegistrationFormDAO();
            registrationForm = dao.findRegistrationForm(new Integer(selectedID));
        }
        for (int i = 1; i <= 6; i++) {        
            ArrayList<RegistrationField> r = new ArrayList<RegistrationField>();
            setRegistrationField(i, r);
            if (mode.equals("edit")) {
                fillRegistrationField(i, r);
            }
            registrationFieldInfoValues.add(r);
        }
        //qaId = registrationForm.getQuestionnaire() != null ? registrationForm.getQuestionnaire().getId() : null;
        //this.setQuestionnaireList();
        qaId = registrationForm.getQaForm() != null? registrationForm.getQaForm().getId() : null;
        qcQaFormId = registrationForm.getQcQaForm() != null? registrationForm.getQcQaForm().getId() : null;
        this.setQaFormList();
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:registration:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:registration:edit"); 
       }
    }  

    public void fieldSelectedListener(ArrayList<RegistrationField> fieldinfo) {
        String selectField = (String) JSFUtil.getRequestParameterMap("selectField");
        String selectValue = (String) JSFUtil.getRequestParameterMap("selectValue");
        
        if(selectValue.equals("false")) {
            // IF SELECT CHILD OF ADDRESS GROUP
            if((selectField.toUpperCase().startsWith("CURRENT") || selectField.toUpperCase().startsWith("HOME") || selectField.toUpperCase().startsWith("BILLING") || selectField.toUpperCase().startsWith("SHIPPING")) 
                && !selectField.equals("Current Address") && !selectField.equals("Home Address") && !selectField.equals("Billing Address") && !selectField.equals("Shipping Address")) {   
                for (RegistrationField field : fieldinfo) {                
                    if(selectField.toUpperCase().startsWith("CURRENT") && !selectField.equals("Current Address") && field.getDisplayName().equals("Current Address")) {
                        if(field.getSelected() == null || !field.getSelected()) {
                            field.setSelected(Boolean.TRUE);                        
                        }
                        break;
                    } else if(selectField.toUpperCase().startsWith("HOME") && !selectField.equals("Home Address") && field.getDisplayName().equals("Home Address")) {
                        if(field.getSelected() == null || !field.getSelected()) {
                            field.setSelected(Boolean.TRUE);                        
                        }
                        break;
                    } else if(selectField.toUpperCase().startsWith("BILLING") && !selectField.equals("Billing Address") && field.getDisplayName().equals("Billing Address")) {
                        if(field.getSelected() == null || !field.getSelected()) {
                            field.setSelected(Boolean.TRUE);                        
                        }
                        break;
                    } else if(selectField.toUpperCase().startsWith("SHIPPING") && !selectField.equals("Shipping Address") && field.getDisplayName().equals("Shipping Address")) {
                        if(field.getSelected() == null || !field.getSelected()) {
                            field.setSelected(Boolean.TRUE);                        
                        }
                        break;
                    }                 
                }
            }
        } else {
            // IF UNSELECT PARENT OF ADDRESS GROUP
             if(selectField.equals("Current Address") || selectField.equals("Home Address") || selectField.equals("Billing Address") || selectField.equals("Shipping Address")) {   
                Boolean chkCurrent = false, chkHome = false, chkBilling = false, chkShipping = false;
                for (RegistrationField field : fieldinfo) {           
                    if(selectField.equals("Current Address") && field.getDisplayName().toUpperCase().startsWith("CURRENT") && (field.getSelected() == null || field.getSelected())) {
                        chkCurrent = true;
                        break;
                    }
                    if(selectField.equals("Home Address") && field.getDisplayName().toUpperCase().startsWith("HOME") && !field.getDisplayName().equals("Home Phone") && (field.getSelected() == null || field.getSelected())) {
                        chkHome = true;
                        break;
                    }
                    if(selectField.equals("Billing Address") && field.getDisplayName().toUpperCase().startsWith("BILLING") && (field.getSelected() == null || field.getSelected())) {
                        chkBilling = true;
                        break;
                    }
                    if(selectField.equals("Shipping Address") && field.getDisplayName().toUpperCase().startsWith("SHIPPING") && (field.getSelected() == null || field.getSelected())) {
                        chkShipping = true;
                        break;
                    }
                }
                if(chkCurrent || chkHome || chkBilling || chkShipping) {
                    for (RegistrationField field : fieldinfo) {  
                        if(chkCurrent && field.getDisplayName().equals("Current Address") && (field.getSelected() == null || !field.getSelected())) {
                            field.setSelected(Boolean.TRUE);    
                            break;
                        }
                        if(chkHome && field.getDisplayName().equals("Home Address") && (field.getSelected() == null || !field.getSelected())) {
                            field.setSelected(Boolean.TRUE);    
                            break;
                        }
                        if(chkBilling && field.getDisplayName().equals("Billing Address") && (field.getSelected() == null || !field.getSelected())) {
                            field.setSelected(Boolean.TRUE);    
                            break;
                        }
                        if(chkShipping && field.getDisplayName().equals("Shipping Address") && (field.getSelected() == null || !field.getSelected())) {
                            field.setSelected(Boolean.TRUE);    
                            break;
                        }
                    }
                }                
             }
        }

    }
    
    private List<RegistrationField> mergeRegistrationField() {
        List<RegistrationField> dbFields = new ArrayList<RegistrationField>();
        int groupNo = 0;
        for (ArrayList<RegistrationField> infoValue : registrationFieldInfoValues) {
            int seqNo = 0;
            groupNo++;
            /*String groupTitle = infoValue.getGroupTitle();
            if (groupTitle!=null && !groupTitle.equals("")) {
                    RegistrationField rf = new RegistrationField();
                    rf.setGroupNo(groupNo);
                    rf.setSeqNo(++seqNo);
                    rf.setName(GROUP_TITLE_FIELD);
                    rf.setRequired(true);
                    rf.setCustomName(groupTitle);
                    rf.setRegistrationForm(registrationForm);
                    dbFields.add(rf);
            }*/
            for (RegistrationField field : infoValue) {
                if (field.getSelected()) {
                    RegistrationField rf = new RegistrationField();
                    rf.setGroupNo(groupNo);
                    rf.setSeqNo(++seqNo);
                    rf.setName(field.getName());
                    rf.setRequired(field.getRequired());
                    rf.setCustomName(field.getCustomName());
                    rf.setControlType(field.getControlType());
                    rf.setItems(field.getItems());
                    rf.setRegistrationForm(registrationForm);
                    dbFields.add(rf);
                }
            }
        }
        return dbFields;
    }

    public String saveAction() {
        messageDup = "";
        if(checkName(registrationForm)) {
            RegistrationFormDAO dao = getRegistrationFormDAO();
            try {
                String userName = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                registrationForm.setEnable(true);
                registrationForm.setRegistrationFieldCollection(mergeRegistrationField()); 
                registrationForm.setUpdateBy(userName);
                registrationForm.setUpdateDate(now);
                if(qaId != null && qaId != 0){
                    registrationForm.setQaForm(qaFormDAO.findQaForm(qaId));
                }else{
                    registrationForm.setQaForm(null);
                }
                if(qcQaFormId != null && qcQaFormId != 0){
                    registrationForm.setQcQaForm(qaFormDAO.findQaForm(qcQaFormId));
                }else{
                    registrationForm.setQcQaForm(null);
                }

                if (getMode().equals("add")) {
                    registrationForm.setId(null);
                    registrationForm.setCreateBy(userName);
                    registrationForm.setCreateDate(now);
                    dao.create(registrationForm);
                } else {       
                    dao.edit(registrationForm);
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

    public Boolean checkName(RegistrationForm registrationForm) {
        String name = registrationForm.getName();
        Integer id=0; 
        if(registrationForm.getId() != null)
            id = registrationForm.getId();
        RegistrationFormDAO dao = getRegistrationFormDAO();
        
        Integer cnt = dao.checkRegistrationFormName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    private void setRegistrationField(int groupNo, List<RegistrationField> registrationFields) {
        int seqNo = 0;
        for (String[] fieldName : FIELD_NAME) {
            RegistrationField r = new RegistrationField();
            r.setSelected(false);
            r.setGroupNo(groupNo);
            r.setSeqNo(++seqNo);
            r.setDisplayName(fieldName[0]);
            r.setName(fieldName[1]);
            r.setRegistrationForm(registrationForm);
            registrationFields.add(r);

        }
        for (int i = 0; i < MAX_FLEX_FIELD; i++) {
            RegistrationField r = new RegistrationField();
            r.setSelected(false);
            r.setGroupNo(groupNo);
            r.setSeqNo(++seqNo);
            r.setDisplayName("Flex Field" + (i + 1));
            r.setName("fx" + (i + 1));
            r.setRegistrationForm(registrationForm);
            registrationFields.add(r);
        }

    }

    private void fillRegistrationField(int groupNo, List<RegistrationField> registrationFields) {
        List<RegistrationField> registField = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (RegistrationField dbField : registField) {
            int dbGroupNo = dbField.getGroupNo();
            String dbFieldName = dbField.getName();
            if (dbGroupNo == groupNo) {
                for (RegistrationField group : registrationFields) {
                    String formFieldName = group.getName();
                    if (formFieldName.equals(dbFieldName)) {
                        group.setSelected(true);
                        group.setRequired(dbField.getRequired());
                        group.setCustomName(dbField.getCustomName());
                        group.setControlType(dbField.getControlType());
                        group.setItems(dbField.getItems());
                        break;
                    }
                }
            }
        }
    }

    public void setQaFormList() {
        qaFormList = this.qaFormDAO.getQaFormList();
    }

    public Map<String, Integer> getQaFormList() {
        return qaFormList;
    }
    
    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public List<ArrayList<RegistrationField>> getRegistrationFieldInfoValues() {
        return registrationFieldInfoValues;
    }

    public void setRegistrationFieldInfoValues(List<ArrayList<RegistrationField>> registrationFieldInfoValues) {
        this.registrationFieldInfoValues = registrationFieldInfoValues;
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
    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }

    public QaFormDAO getQaFormDAO() {
        return qaFormDAO;
    }

    public void setQaFormDAO(QaFormDAO qaFormDAO) {
        this.qaFormDAO = qaFormDAO;
    }
    
    public Integer getQaId() {
        return qaId;
    }
    
    public void setQaId(Integer qaId) {
        this.qaId = qaId;
    }
    
    public Integer getQcQaFormId() {
        return qcQaFormId;
    }

    public void setQcQaFormId(Integer qcQaFormId) {
        this.qcQaFormId = qcQaFormId;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
