/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ChildRegFieldDAO;
import com.maxelyz.core.model.dao.ChildRegFormDAO;
import com.maxelyz.core.model.entity.ChildRegField;
import com.maxelyz.core.model.entity.ChildRegForm;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author TBN
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ChildRegFormManageController {
    public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(DeclarationFormEditController.class);
    private static String SUCCESS = "childregform.xhtml?faces-redirect=true";
    private static String FAILURE = "childregformmanage.xhtml";
    private ChildRegField childRegField;
    private ChildRegForm childRegForm;
    @ManagedProperty(value = "#{childRegFormDAO}")
    private ChildRegFormDAO childRegFormDAO;
    @ManagedProperty(value = "#{childRegFieldDAO}")
    private ChildRegFieldDAO childRegFieldDAO;
    
    private List<ChildRegField> childRegFields;
    private List<ChildRegField> childRegFields1;
    private String mode;
    private String message;
    private String messageDup;
    private List<String> selectedField = new ArrayList<String>();
    private List<String>fieldList = new ArrayList<String>();
    private List<ArrayList<ChildRegField>> childRegFieldInfoValues = new ArrayList<ArrayList<ChildRegField>>();
        public int count_seqNo=0;
    public int count_seqNo2=0;
    private List<Number> orderSeqNo = new ArrayList<Number>();
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:childreg:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = JSFUtil.getRequestParameterMap("selectedID");
        String manageID = JSFUtil.getRequestParameterMap("ManageID");
        if (manageID == null || manageID.isEmpty()) {
            mode = "add";
            childRegForm = new ChildRegForm();
           // declarationForm.setQuestionnaire(new Questionnaire());

        } else {
            mode = "edit";
            //DeclarationFormDAO dao = getDeclarationFormDAO();
            ChildRegFieldDAO fielddao = getChildRegFieldDAO();
            //declarationForm = dao.findDeclarationForm(new Integer(selectedID));
            childRegFields = fielddao.getChildRegFieldByChildRegFormID(new Integer(manageID));
        }
        
        for (int i = 0;i<childRegFields.size();i++)
        {
            //fieldList.put(String.valueOf(i), (declarationFields.get(i).getCustomName()+" ("+declarationFields.get(i).getDisplayName()+")"));
            selectedField.add(i, childRegFields.get(i).getCustomName()+" (FlexField"+childRegFields.get(i).getName().substring(2)+")");
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

    private void setChildRegField(int fx, List<ChildRegField> childRegFields) {
        //int seqNo = 0;
        for (int i = 0+fx; i < MAX_FLEX_FIELD+fx; i++) {
            ChildRegField r = new ChildRegField();
            r.setGroupNo(1);
            r.setSeqNo(++count_seqNo);
            r.setDisplayName("Flex Field" + (i + 1));
            r.setName("fx" + (i + 1));
            r.setChildRegForm(childRegForm);
            childRegFields.add(r);
        }

    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:childreg:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:childreg:edit"); 
       }
    }  
          
    private List<ChildRegField> mergeChildRegField() {
        List<ChildRegField> dbFields = new ArrayList<ChildRegField>();
        int groupNo = 1;
        for (ArrayList<ChildRegField> infoValue : childRegFieldInfoValues) {
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
            for (ChildRegField field : infoValue) {
                if (field.getSelected()) {
                    ChildRegField rf = new ChildRegField();
                    rf.setGroupNo(groupNo);
                    rf.setSeqNo(++count_seqNo2);
                    rf.setName(field.getName());
                    rf.setRequired(field.getRequired());
                    rf.setCustomName(field.getCustomName());
                    rf.setControlType(field.getControlType());
                    rf.setItems(field.getItems());
                    rf.setChildRegForm(childRegForm);
                    dbFields.add(rf);
                }
            }
        }
        return dbFields;
    }
    
    private void fillChildRegField(int groupNo, List<ChildRegField> childRegFields) {
        List<ChildRegField> childRegField = (List<ChildRegField>) childRegForm.getChildRegFieldCollection();
        for (ChildRegField dbField : childRegField) {
            int dbGroupNo = dbField.getGroupNo();
            String dbFieldName = dbField.getName();
            if (dbGroupNo == groupNo) {
                for (ChildRegField group : childRegFields) {
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
            List<ChildRegField> orderFields = new ArrayList<ChildRegField>();
            int i=0;
            int j=0;
            message = "";
            String pageName = "customer_handling";
            for(String field : fieldList){
                String[] a = fieldList.get(j).split("\\(FlexField");
                String b = a[1].toString();
                int c = b.length()-1;

                ChildRegField orderField = new ChildRegField();
                orderField.setSeqNo(++i);
                orderField.setGroupNo(childRegFields.get(j++).getGroupNo());
                orderField.setName("fx"+b.substring(0, c));

                orderFields.add(orderField);
                this.getChildRegFieldDAO().edit(orderField); 
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

    
    
    public List<ChildRegField> getChildRegFields1() {
        return childRegFields1;
    }

    public void setChildRegFields1(List<ChildRegField> childRegFields1) {
        this.childRegFields1 = childRegFields1;
    }

    public List<String> getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(List<String> selectedField) {
        this.selectedField = selectedField;
    }
    
    public ChildRegField getChildRegField() {
        return childRegField;
    }

    public void setChildRegField(ChildRegField childRegField) {
        this.childRegField = childRegField;
    }

    public ChildRegForm getChildRegForm() {
        return childRegForm;
    }

    public void setChildRegForm(ChildRegForm childRegForm) {
        this.childRegForm = childRegForm;
    }

    public ChildRegFormDAO getChildRegFormDAO() {
        return childRegFormDAO;
    }

    public void setChildRegFormDAO(ChildRegFormDAO childRegFormDAO) {
        this.childRegFormDAO = childRegFormDAO;
    }

    public ChildRegFieldDAO getChildRegFieldDAO() {
        return childRegFieldDAO;
    }

    public void setChildRegFieldDAO(ChildRegFieldDAO childRegFieldDAO) {
        this.childRegFieldDAO = childRegFieldDAO;
    }

    public List<ChildRegField> getChildRegFields() {
        return childRegFields;
    }

    public void setChildRegFields(List<ChildRegField> childRegFields) {
        this.childRegFields = childRegFields;
    }

    public List<ArrayList<ChildRegField>> getChildRegFieldInfoValues() {
        return childRegFieldInfoValues;
    }

    public void setChildRegFieldInfoValues(List<ArrayList<ChildRegField>> childRegFieldInfoValues) {
        this.childRegFieldInfoValues = childRegFieldInfoValues;
    }
}
