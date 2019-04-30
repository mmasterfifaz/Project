/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;



import com.maxelyz.core.model.dao.DeclarationFormDAO;
import com.maxelyz.core.model.entity.DeclarationField;
import com.maxelyz.core.model.entity.DeclarationForm;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author User
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class DeclarationFormEditController {
    public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(DeclarationFormEditController.class);
    private static String SUCCESS = "declarationform.xhtml?faces-redirect=true";
    private static String FAILURE = "declarationformedit.xhtml";
//    private static String FIELD_NAME[][] = {
//        {"Group Title", "grouptitle"},
//        {"Initial", "initial"},
//        {"Name", "name"},
//        {"Surname", "surname"},
//        {"ID Card Type", "idcardtypeid"},
//        {"ID Card", "idno"},
//        {"ID Card Issue", "idcardissue"},
//        {"ID Card Expiry Date", "idcardexpirydate"},
//        {"Date of Birth", "dob"},
//        {"Gender", "gender"},
//        {"Weight", "weight"},
//        {"Height", "height"},
//        {"Marital Status", "maritalstatus"},
//        {"BMI", "bmi"},
//        {"Current Full Name", "currentaddressfullname"},
//        {"Current Addr1", "currentaddress1"},
//        {"Current Addr2", "currentaddress2"},
//        {"Current Addr3", "currentaddress3"},
//        {"Current Addr4", "currentaddress4"},
//        {"Current Addr5", "currentaddress5"},
//        {"Current Addr6", "currentaddress6"},
//        {"Current Addr7", "currentaddress7"},
//        {"Current Addr8", "currentaddress8"},
//        {"Current Province", "currentaddressprovince"},
//        {"Current Postal", "currentaddresspostal"},
//        {"Current Tel1", "currentaddresstelephone1"},
//        {"Current Tel2", "currentaddresstelephone2"},
//        
//        {"Home Full Name", "homeaddressfullname"},
//        {"Home Addr1", "homeaddress1"},
//        {"Home Addr2", "homeaddress2"},
//        {"Home Addr3", "homeaddress3"},
//        {"Home Addr4", "homeaddress4"},
//        {"Home Addr5", "homeaddress5"},
//        {"Home Addr6", "homeaddress6"},
//        {"Home Addr7", "homeaddress7"},
//        {"Home Addr8", "homeaddress8"},
//        {"Home Province", "homeaddressprovince"},
//        {"Home Postal", "homeaddresspostal"},
//        {"Home Tel1", "homeaddresstelephone1"},
//        {"Home Tel2", "homeaddresstelephone2"},
//        
//        {"Billing Full Name", "billingaddressfullname"},
//        {"Billing Addr1", "billingaddress1"},
//        {"Billing Addr2", "billingaddress2"},
//        {"Billing Addr3", "billingaddress3"},
//        {"Billing Addr4", "billingaddress4"},
//        {"Billing Addr5", "billingaddress5"},
//        {"Billing Addr6", "billingaddress6"},
//        {"Billing Addr7", "billingaddress7"},
//        {"Billing Addr8", "billingaddress8"},
//        {"Billing Province", "billingaddressprovince"},
//        {"Billing Postal", "billingaddresspostal"},
//        {"Billing Tel1", "billingaddresstelephone1"},
//        {"Billing Tel2", "billingaddresstelephone2"},
//
//        {"Shipping Full Name", "shippingaddressfullname"},
//        {"Shipping Addr1", "shippingaddress1"},
//        {"Shipping Addr2", "shippingaddress2"},
//        {"Shipping Addr3", "shippingaddress3"},
//        {"Shipping Addr4", "shippingaddress4"},
//        {"Shipping Addr5", "shippingaddress5"},
//        {"Shipping Addr6", "shippingaddress6"},
//        {"Shipping Addr7", "shippingaddress7"},
//        {"Shipping Addr8", "shippingaddress8"},
//        {"Shipping Province", "shippingaddressprovince"},
//        {"Shipping Postal", "shippingaddresspostal"},
//        {"Shipping Tel1", "shippingaddresstelephone1"},
//        {"Shipping Tel2", "shippingaddresstelephone2"},
//        
//        {"Home Phone", "homephone"},
//        {"Office Phone", "officephone"},
//        {"Mobile Phone", "mobilephone"},
//        {"Email", "email"},
//        {"Nationality", "nationality"},
//        {"Race", "race"},
//        {"Religion", "religion"},
//        {"Occupation", "occupation"},
//        {"Occupation List", "occupationlist"},
//        {"Job Description", "jobdescription"},
//        {"Job Position", "position"},
//        {"Business Type", "businesstype"},
//        {"Income", "income"}};
    private List<ArrayList<DeclarationField>> declarationFieldInfoValues = new ArrayList<ArrayList<DeclarationField>>();
//    private Map<String, Integer> questionnaireList;

    private DeclarationForm declarationForm;
    private String mode;
    private String message;
    private String messageDup;
    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;
   // @ManagedProperty(value = "#{questionnaireDAO}")
   // private QuestionnaireDAO questionnaireDAO;
    
    private UploadedFile item;
    private String extFileName;
    private String realFileName;
    private String fileName;
    private String uploadPath = JSFUtil.getuploadPath();  //JSFUtil.getRealPath() + "upload\\import\\";
    private String tmpPath = JSFUtil.getuploadPath()+"document/declaration/";   //JSFUtil.getRealPath() + "upload\\document\\declaration"; 
    public int count_seqNo=0;
    public int count_seqNo2=0;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:declaration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = JSFUtil.getRequestParameterMap("selectedID");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            declarationForm = new DeclarationForm();
           // declarationForm.setQuestionnaire(new Questionnaire());

        } else {
            mode = "edit";
            DeclarationFormDAO dao = getDeclarationFormDAO();
            declarationForm = dao.findDeclarationForm(new Integer(selectedID));
        }
        
        for (int i = 1; i <= 1; i++) {        
            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
            setDeclarationField(0, r);
            if (mode.equals("edit")) {
                fillDeclarationField(i, r);
            }
            declarationFieldInfoValues.add(r);
        }
        for (int i = 1; i <= 1; i++) {        
            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
            setDeclarationField(10, r);
            if (mode.equals("edit")) {
                fillDeclarationField(i, r);
            }
            declarationFieldInfoValues.add(r);
        }
        
        
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:declaration:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:declaration:edit"); 
       }
    }  
          
    private List<DeclarationField> mergeDeclarationField() {
        List<DeclarationField> dbFields = new ArrayList<DeclarationField>();
        int groupNo = 1;
        for (ArrayList<DeclarationField> infoValue : declarationFieldInfoValues) {
            int seqNo = 0;
            //groupNo++;
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
            for (DeclarationField field : infoValue) {
                if (field.getSelected()) {
                    DeclarationField rf = new DeclarationField();
                    rf.setGroupNo(groupNo);
                    rf.setSeqNo(++count_seqNo2);
                    rf.setName(field.getName());
                    rf.setRequired(field.getRequired());
                    rf.setCustomName(field.getCustomName());
                    rf.setControlType(field.getControlType());
                    rf.setItems(field.getItems());
                    rf.setDeclarationForm(declarationForm);
                    dbFields.add(rf);
                }
            }
        }
        return dbFields;
    }

    public String saveAction() {
        messageDup = "";
        if(checkName(declarationForm)) {
            DeclarationFormDAO dao = getDeclarationFormDAO();
            try {
                String userName = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
//                declarationForm.setEnable(true);
                declarationForm.setDeclarationFieldCollection(mergeDeclarationField()); 
                declarationForm.setUpdateBy(userName);
                declarationForm.setUpdateDate(now);

                if (getMode().equals("add")) {
                    declarationForm.setId(null);
                    declarationForm.setCreateBy(userName);
                    declarationForm.setCreateDate(now);
                    dao.create(declarationForm);
                } else {       
                    dao.edit(declarationForm);
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

    public Boolean checkName(DeclarationForm declarationForm) {
        String name = declarationForm.getName();
        Integer id=0; 
        if(declarationForm.getId() != null)
            id = declarationForm.getId();
        DeclarationFormDAO dao = getDeclarationFormDAO();
        
        Integer cnt = dao.checkDeclarationFormName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    private void setDeclarationField(int fx, List<DeclarationField> declarationFields) {
        //int seqNo = 0;
        for (int i = 0+fx; i < MAX_FLEX_FIELD+fx; i++) {
            DeclarationField r = new DeclarationField();
            r.setGroupNo(1);
            r.setSeqNo(++count_seqNo);
            r.setDisplayName("Flex Field" + (i + 1));
            r.setName("fx" + (i + 1));
            r.setDeclarationForm(declarationForm);
            declarationFields.add(r);
        }

    }

    private void fillDeclarationField(int groupNo, List<DeclarationField> declarationFields) {
        List<DeclarationField> declareField = (List<DeclarationField>) declarationForm.getDeclarationFieldCollection();
        for (DeclarationField dbField : declareField) {
            int dbGroupNo = dbField.getGroupNo();
            String dbFieldName = dbField.getName();
            if (dbGroupNo == groupNo) {
                for (DeclarationField group : declarationFields) {
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
/*
    public void setQuestionnaireList() {
        questionnaireList = this.questionnaireDAO.getQuestionnaireList();
    }

    public Map<String, Integer> getQuestionnaireList() {
        return questionnaireList;
    }*/
    
    
    public DeclarationForm getDeclarationForm() {
        return declarationForm;
    }

    public void setDeclarationForm(DeclarationForm declarationForm) {
        this.declarationForm = declarationForm;
    }

    public List<ArrayList<DeclarationField>> getDeclarationFieldInfoValues() {
        return declarationFieldInfoValues;
    }

    public void setDeclarationFieldInfoValues(List<ArrayList<DeclarationField>> declarationFieldInfoValues) {
        this.declarationFieldInfoValues = declarationFieldInfoValues;
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
    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
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
    
    public void uploadListener(FileUploadEvent event) throws Exception {
        Date date = new Date();
        item = event.getUploadedFile();

        String str = item.getName();

        int idx = str.lastIndexOf(File.separator);
        int idx1 = str.lastIndexOf("\\");
        if (idx != -1) {
            str = str.substring(idx + 1, str.length());
        } else if (idx1 != -1) {
            str = str.substring(idx1 + 1, str.length());
        }
        String[] fileNameAll = str.split("\\.");
        realFileName = fileNameAll[0];
        extFileName = fileNameAll[1];
        if (str.lastIndexOf("\\") != -1) {
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
        }
        if(!str.isEmpty() && str.indexOf(",") != -1)
            str = str.replace(",", "");
        fileName = realFileName + "_" + date.getTime() + "." + extFileName ;

        /*
        String str = item.getName();
        String[] fileNameAll = str.split("\\.");
        realFileName = fileNameAll[0];
        extFileName = fileNameAll[1];
        if (str.lastIndexOf("\\") != -1) {
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
        }
        if(!str.isEmpty() && str.indexOf(",") != -1)
            str = str.replace(",", "");
        fileName = realFileName + "_" + date.getTime() + "." + extFileName ;
        */
        declarationForm.setMerge_file(fileName);
        setFileName(fileName);
        str = tmpPath + fileName;
        File file = new File(str);
        saveToFile(item.getInputStream(),file);
        
    }
        
    private boolean saveToFile(InputStream inputStream, File file) {    
        FileOutputStream fos = null;
        BufferedInputStream bis = null;   
        boolean result = false;        
        try { 
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);            
            bis = new BufferedInputStream(inputStream, buffer.length);            
            int numRead = -1;            
            while ((numRead = bis.read(buffer, 0, buffer.length)) != -1) {                
                fos.write(buffer, 0, numRead);
            }          
            result = true;        
        } catch (IOException ex) {            
            log.error(ex, ex);
            log.error("Exception during download file " + file.getAbsolutePath());        
        } finally {            
            try {                
                fos.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing file output stream " + file.getAbsolutePath());            
            }            
            try {                
                bis.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing buffered input stream " + file.getAbsolutePath());            
            }   
        }    
        return result; 
    }

    public UploadedFile getItem() {
        return item;
    }

    public void setItem(UploadedFile item) {
        this.item = item;
    }

    public String getExtFileName() {
        return extFileName;
    }

    public void setExtFileName(String extFileName) {
        this.extFileName = extFileName;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }
    
    public void deleteFileNameListener(ActionEvent event) {
        declarationForm.setMerge_file(null);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public Map<String, String> getMergeDataViewList() {
        return this.getDeclarationFormDAO().getMergeDataViewList();
    }
        
    
}
