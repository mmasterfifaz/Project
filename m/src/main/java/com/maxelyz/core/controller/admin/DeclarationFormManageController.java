/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import static com.maxelyz.core.controller.admin.DeclarationFormEditController.MAX_FLEX_FIELD;
import com.maxelyz.core.model.dao.DeclarationFieldDAO;
import com.maxelyz.core.model.dao.DeclarationFormDAO;
import com.maxelyz.core.model.entity.DeclarationField;
import com.maxelyz.core.model.entity.DeclarationForm;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author User
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class DeclarationFormManageController {
        public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(DeclarationFormEditController.class);
    private static String SUCCESS = "declarationform.xhtml?faces-redirect=true";
    private static String FAILURE = "declarationformmanage.xhtml";
    private DeclarationField declarationField;
    private DeclarationForm declarationForm;
    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;
    @ManagedProperty(value = "#{declarationFieldDAO}")
    private DeclarationFieldDAO declarationFieldDAO;
    
    private List<DeclarationField> declarationFields;
    private List<DeclarationField> declarationFields1;
    private String mode;
    private String message;
    private String messageDup;
    private List<String> selectedField = new ArrayList<String>();
    private List<String>fieldList = new ArrayList<String>();
    private List<ArrayList<DeclarationField>> declarationFieldInfoValues = new ArrayList<ArrayList<DeclarationField>>();
        public int count_seqNo=0;
    public int count_seqNo2=0;
    private List<Number> orderSeqNo = new ArrayList<Number>();
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:declaration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = JSFUtil.getRequestParameterMap("selectedID");
        String manageID = JSFUtil.getRequestParameterMap("ManageID");
        if (manageID == null || manageID.isEmpty()) {
            mode = "add";
            declarationForm = new DeclarationForm();
           // declarationForm.setQuestionnaire(new Questionnaire());

        } else {
            mode = "edit";
            //DeclarationFormDAO dao = getDeclarationFormDAO();
            DeclarationFieldDAO fielddao = getDeclarationFieldDAO();
            //declarationForm = dao.findDeclarationForm(new Integer(selectedID));
            declarationFields = fielddao.getDeclarationFieldByDeclarationFormID(new Integer(manageID));
        }
        
        for (int i = 0;i<declarationFields.size();i++)
        {
            //fieldList.put(String.valueOf(i), (declarationFields.get(i).getCustomName()+" ("+declarationFields.get(i).getDisplayName()+")"));
            selectedField.add(i, declarationFields.get(i).getCustomName()+" (FlexField"+declarationFields.get(i).getName().substring(2)+")");
        }
//        for (int i = 1; i <= 1; i++) {        
//            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
//            setDeclarationField(0, r);
//            if (mode.equals("edit")) {
//                fillDeclarationField(i, r);
//            }
//            declarationFieldInfoValues.add(r);
//        }
//        for (int i = 1; i <= 1; i++) {        
//            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
//            setDeclarationField(10, r);
//            if (mode.equals("edit")) {
//                fillDeclarationField(i, r);
//            }
//            declarationFieldInfoValues.add(r);
//        }

        //declarationFields = declarationFieldInfoValues.get(0);
        
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
    
    public String backAction() {
        return SUCCESS;
    }

public String saveAction() {

            try {
                List<DeclarationField> orderFields = new ArrayList<DeclarationField>();
                int i=0;
                int j=0;
                message = "";
                String pageName = "customer_handling";
                for(String field : fieldList){
                    String[] a = fieldList.get(j).split("\\(FlexField");
                    String b = a[1].toString();
                    int c = b.length()-1;

                    DeclarationField orderField = new DeclarationField();
                    orderField.setSeqNo(++i);
                    orderField.setGroupNo(declarationFields.get(j++).getGroupNo());
                    orderField.setName("fx"+b.substring(0, c));
                   
                    orderFields.add(orderField);
                    this.getDeclarationFieldDAO().edit(orderField); 
                }
                
                
            } catch(Exception e) {
                message = e.getMessage();
                return null;
            }
        
        return SUCCESS;
    }

    public List<Number> getOrderSeqNo() {
        return orderSeqNo;
    }

    public void setOrderSeqNo(List<Number> orderSeqNo) {
        this.orderSeqNo = orderSeqNo;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    
    
    public List<DeclarationField> getDeclarationFields1() {
        return declarationFields1;
    }

    public void setDeclarationFields1(List<DeclarationField> declarationFields1) {
        this.declarationFields1 = declarationFields1;
    }

    public List<String> getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(List<String> selectedField) {
        this.selectedField = selectedField;
    }
    
    public DeclarationField getDeclarationField() {
        return declarationField;
    }

    public void setDeclarationField(DeclarationField declarationField) {
        this.declarationField = declarationField;
    }

    public DeclarationForm getDeclarationForm() {
        return declarationForm;
    }

    public void setDeclarationForm(DeclarationForm declarationForm) {
        this.declarationForm = declarationForm;
    }

    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
    }

    public DeclarationFieldDAO getDeclarationFieldDAO() {
        return declarationFieldDAO;
    }

    public void setDeclarationFieldDAO(DeclarationFieldDAO declarationFieldDAO) {
        this.declarationFieldDAO = declarationFieldDAO;
    }

    public List<DeclarationField> getDeclarationFields() {
        return declarationFields;
    }

    public void setDeclarationFields(List<DeclarationField> declarationFields) {
        this.declarationFields = declarationFields;
    }

    public List<ArrayList<DeclarationField>> getDeclarationFieldInfoValues() {
        return declarationFieldInfoValues;
    }

    public void setDeclarationFieldInfoValues(List<ArrayList<DeclarationField>> declarationFieldInfoValues) {
        this.declarationFieldInfoValues = declarationFieldInfoValues;
    }
    
    
    
    
    
    
}
