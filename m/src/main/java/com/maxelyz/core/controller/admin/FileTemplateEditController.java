package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerFieldDAO;
import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import java.io.File;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.FileTemplateDAO;
import com.maxelyz.core.model.dao.FileTemplateMappingDAO;
import com.maxelyz.core.model.entity.CustomerField;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.FileTemplateMappingPK;
import com.maxelyz.core.model.value.admin.FileMappingValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.FileUploadBean;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class FileTemplateEditController {

    private static Logger log = Logger.getLogger(FileTemplateEditController.class);
    private static String REDIRECT_PAGE = "filetemplate.jsf";
    private static String SUCCESS = "filetemplate.xhtml?faces-redirect=true";
    private static String FAILURE = "filetemplateedit.xhtml";
    private FileTemplate fileTemplate;
    private String mode;
    private String message;
    private String messageDup;
    private List<String> columnNames = new ArrayList<String>();
    private static File fileUpload;
    private Map<String, Integer> customerLayoutList;
    private List<String> col1, col2, col3, col4;

    private List<FileMappingValue> fileMappingValues = new ArrayList<FileMappingValue>();
    private Map<String, String> customerFields = new LinkedHashMap<String, String>();
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "temp");
    
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;
    @ManagedProperty(value = "#{fileTemplateMappingDAO}")
    private FileTemplateMappingDAO fileTemplateMappingDAO;
    @ManagedProperty(value = "#{customerFieldDAO}")
    private CustomerFieldDAO customerFieldDAO;
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:fileformat:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        CustomerLayout customerLayout = null;
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");        
        
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            fileTemplate = new FileTemplate();
            fileTemplate.setEnable(Boolean.TRUE);
            fileTemplate.setFieldEnclosed("none");
            
            customerLayout = customerLayoutDAO.findDefaultCustomerLayout();
            fileTemplate.setCustomerLayout(customerLayout);          
        } else {
            if (selectedID == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
            mode = "edit"; 
            fileTemplate = this.getFileTemplateDAO().findFileTemplate(new Integer(selectedID));
            List<FileTemplateMapping> fileTemplateMappings;
            fileTemplateMappings = this.getFileTemplateMappingDAO().findFileTemplateMappingEntitiesByFileTemplateId(fileTemplate.getId());
            fileMappingValues.clear();
            for (FileTemplateMapping value : fileTemplateMappings) {
                FileMappingValue fileMappingValue = new FileMappingValue();                
                
                fileMappingValue.setFieldMapping(value.getCustomerField().getMappingField());                
                fileMappingValue.setFieldType(value.getFieldType());
                fileMappingValue.setPattern(value.getPattern());
                fileMappingValue.setDefaultValue(value.getDefaultValue());
                
                fileMappingValue.setFileColumnNo(value.getFileColumnNo());
                fileMappingValue.setFileColumnName(value.getFileColumnName());
                fileMappingValue.setCustomerFieldId(value.getCustomerField().getId());
                fileMappingValues.add(fileMappingValue);
            }
        }
        
        customerFields.putAll(this.getCustomerFieldDAO().getMapCustomerFieldName());
        customerLayoutList = this.getCustomerLayoutDAO().getCustomerLayoutList();    
        
        if(fileTemplate != null) {
            displayCustomerLayout(fileTemplate.getCustomerLayout().getId());
        }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:fileformat:add");
       } else {
 	   return false; 
       }
    }
    
    public void customerLayoutChangeListener(ValueChangeEvent event) {
        Integer customerLayoutID = (Integer) event.getNewValue();
        displayCustomerLayout(customerLayoutID);
    }
    
    public void displayCustomerLayout(Integer customerLayoutID) {
        // PREVIEW CUSTOMER DISPLAY        
        List<CustomerLayoutDetail> layoutPageList = (List) this.getCustomerLayoutDAO().findCustomerLayout(customerLayoutID).getCustomerLayoutDetailCollection();

        col1 = new  ArrayList<String>();
        col2 = new  ArrayList<String>();
        col3 = new  ArrayList<String>();
        col4 = new  ArrayList<String>();        
        String label = "";
        
        if(layoutPageList != null) {   
            for (CustomerLayoutDetail obj: layoutPageList) {
                if (obj.getFieldName().equals("customerName")) {
                    label = JSFUtil.getBundleValue("customerNameTitle");
                } else if (obj.getFieldName().equals("idcardTypeId")) {
                    label = JSFUtil.getBundleValue("idcardTypeTitle");
                } else if (obj.getFieldName().equals("idno")) {
                    label = JSFUtil.getBundleValue("idnoTitle");
                } else if (obj.getFieldName().equals("referenceno")) {
                    label = JSFUtil.getBundleValue("refnoTitle");
                }  else if (obj.getFieldName().equals("gender")) {
                    label = JSFUtil.getBundleValue("genderTitle");
                } else if (obj.getFieldName().equals("email")) {
                    label = JSFUtil.getBundleValue("emailTitle");
                } else if (obj.getFieldName().equals("dob")) {
                    label = JSFUtil.getBundleValue("dobTitle");
                } else if (obj.getFieldName().equals("weight")) {
                    label = JSFUtil.getBundleValue("weightTitle");
                } else if (obj.getFieldName().equals("height")) {
                    label = JSFUtil.getBundleValue("customerNameTitle");
                } else if (obj.getFieldName().equals("nationality")) {
                    label = JSFUtil.getBundleValue("nationalityTitle");
                } else if (obj.getFieldName().equals("occupation")) {
                    label = JSFUtil.getBundleValue("occupationTitle");
                } else if (obj.getFieldName().equals("account")) {
                    label = JSFUtil.getBundleValue("accountTitle");
                } else if (obj.getFieldName().equals("customerType")) {
                    label = JSFUtil.getBundleValue("customerNameTitle");
                } else if (obj.getFieldName().equals("privilege")) {
                    label = JSFUtil.getBundleValue("privilegeTitle");
                } else if (obj.getFieldName().equals("currentAddress")) {
                    label = JSFUtil.getBundleValue("currentAddressTitle");
                } else if (obj.getFieldName().equals("homeAddress")) {
                    label = JSFUtil.getBundleValue("homeAddressTitle");
                } else if (obj.getFieldName().equals("billingAddress")) {
                    label = JSFUtil.getBundleValue("billingAddressTitle");
                } else if (obj.getFieldName().equals("shippingAddress")) {
                    label = JSFUtil.getBundleValue("shippingAddressTitle");
                } else if (obj.getFieldName().contains("flexfield")) {                    
                    String customName = this.getCustomerLayoutDAO().findCustomName(customerLayoutID, obj.getFieldName());
                    label = obj.getFieldName()+" ("+customName+")";
                }

                // Display
                if(obj.getColNo().equals(1)) {
                    col1.add(label);    
                } else if(obj.getColNo().equals(2)) {
                    col2.add(label);       
                } else if(obj.getColNo().equals(3)) {
                    col3.add(label);       
                } else if(obj.getColNo().equals(4)) {
                    col4.add(label);       
                }
            }
        }
    }

    private List<FileTemplateMapping> getFileTemplateMappingFromUI() {
        List<FileTemplateMapping> fileTemplateMappings = new ArrayList<FileTemplateMapping>();
        for (FileMappingValue value : fileMappingValues) {
            if(value.getFieldMapping() != null) {   // && !value.getFieldMapping().equals("0")) {
                CustomerField customerField = customerFieldDAO.findCustomerFieldIdByMappingField(value.getFieldMapping());            
                if (customerField != null) {
                    FileTemplateMapping fileTemplateMapping = new FileTemplateMapping();
                    fileTemplateMapping.setFileColumnNo(value.getFileColumnNo());
                    fileTemplateMapping.setFileColumnName(value.getFileColumnName());
                    fileTemplateMapping.setFieldType(value.getFieldType());
                    fileTemplateMapping.setPattern(value.getPattern());
                    fileTemplateMapping.setDefaultValue(value.getDefaultValue());
                    fileTemplateMapping.setCustomerField(customerField);
                    fileTemplateMapping.setFileTemplate(fileTemplate);
                    FileTemplateMappingPK fileTemplateMappingPK = new FileTemplateMappingPK(fileTemplate.getId(), value.getFileColumnNo());
                    fileTemplateMapping.setFileTemplateMappingPK(fileTemplateMappingPK);

                    fileTemplateMappings.add(fileTemplateMapping);
            }
        }
        }
        return fileTemplateMappings;
    }

    public String saveAction() {
        FileTemplateDAO dao = getFileTemplateDAO();
        messageDup = "";
        if(fileTemplate.getName() != null && !fileTemplate.getName().equals("")) {            
            if(checkName(fileTemplate)) {
                try {      
                    if (getMode().equals("add")) {
                        fileTemplate.setId(null);
                        fileTemplate.setEnable(true);
                        dao.create(fileTemplate);
                        fileTemplate.setFileTemplateMappingCollection(this.getFileTemplateMappingFromUI());
                        dao.edit(fileTemplate);
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
            }
        } else {
            messageDup = " Name is required";
            return null;  
        }
    }

    public Boolean checkName(FileTemplate fileTemplate) {
        String name = fileTemplate.getName();
        Integer id=0; 
        if(fileTemplate.getId() != null)
            id = fileTemplate.getId();
        FileTemplateDAO dao = getFileTemplateDAO();
        
        Integer cnt = dao.checkFileTemplateName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public List<FileMappingValue> getColumnList() {
        return fileMappingValues;
    } 
    
    private void copy(InputStream in, File file) {
        try {
            if(!file.exists()) {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadCompleteListener(FileUploadEvent event) {
        Date date = new Date();
        if(fileTemplate.getFieldEnclosed() != null && fileTemplate.getFieldDelimiter() != null) {        
        try {
            UploadedFile item = event.getUploadedFile();
            
                String fileName = date.getTime() + "_" + item.getName();
            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            if(!fileName.isEmpty() && fileName.indexOf(",") != -1)
                fileName = fileName.replace(",", "");
            
            this.fileUpload = new File(JSFUtil.getuploadPath() + fileName);
            this.copy(item.getInputStream(), this.fileUpload);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    }

    public void clearMapping() {
        if(fileUpload != null) {
            fileUpload.delete();
        }
        fileMappingValues.clear();
    }

    public void mappingListener() {
        if(fileTemplate.getFieldEnclosed() != null && fileTemplate.getFieldDelimiter() != null) {  
        try {
            this.fileMappingValues.clear();
            int i = 0;
            this.columnNames = FileTemplateDAO.readColumnNameFromFile(this.fileUpload, this.fileTemplate);
            for (String col : this.columnNames) {
                FileMappingValue fileMappingValue = new FileMappingValue();
                fileMappingValue.setFileColumnNo(++i);
                fileMappingValue.setFileColumnName(col);
                this.fileMappingValues.add(fileMappingValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
            if(fileUpload != null) {
                fileUpload.delete();
    }
        }
    }

    public Map<String, String> getCustomerFieldList() {
        return customerFields;
    }

    public FileTemplate getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(FileTemplate fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }

    public CustomerFieldDAO getCustomerFieldDAO() {
        return customerFieldDAO;
    }

    public void setCustomerFieldDAO(CustomerFieldDAO customerFieldDAO) {
        this.customerFieldDAO = customerFieldDAO;
    }

    public FileTemplateMappingDAO getFileTemplateMappingDAO() {
        return fileTemplateMappingDAO;
    }

    public void setFileTemplateMappingDAO(FileTemplateMappingDAO fileTemplateMappingDAO) {
        this.fileTemplateMappingDAO = fileTemplateMappingDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public Map<String, Integer> getCustomerLayoutList() {
        return customerLayoutList;
    }

    public void setCustomerLayoutList(Map<String, Integer> customerLayoutList) {
        this.customerLayoutList = customerLayoutList;
    }

    public CustomerLayoutDAO getCustomerLayoutDAO() {
        return customerLayoutDAO;
    }

    public void setCustomerLayoutDAO(CustomerLayoutDAO customerLayoutDAO) {
        this.customerLayoutDAO = customerLayoutDAO;
    }

    public List<String> getCol1() {
        return col1;
    }

    public void setCol1(List<String> col1) {
        this.col1 = col1;
    }

    public List<String> getCol2() {
        return col2;
    }

    public void setCol2(List<String> col2) {
        this.col2 = col2;
    }

    public List<String> getCol3() {
        return col3;
    }

    public void setCol3(List<String> col3) {
        this.col3 = col3;
    }

    public List<String> getCol4() {
        return col4;
    }

    public void setCol4(List<String> col4) {
        this.col4 = col4;
    }

    
}
