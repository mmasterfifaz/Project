/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;


import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.core.model.entity.CustomerLayoutFxMapping;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class CustomerDisplayEditController {
    private static Logger log = Logger.getLogger(CustomerDisplayEditController.class);

    private static String SUCCESS = "customerdisplay.xhtml";
    private static String FAILURE = "customerdisplayedit.xhtml";
    private static String REDIRECT_PAGE = "customerdisplay.jsf";
    public Integer col;
    private String message;
    private CustomerLayout customerLayout;
    private Map<String, String> fieldList;
    private List<String> selectedField = new ArrayList<String>();
            
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;

    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerlayout:edit")) {
            SecurityUtil.redirectUnauthorize();  
       }

        String selectedCol = JSFUtil.getRequestParameterMap("col");
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        CustomerLayoutDAO dao = getCustomerLayoutDAO();
        
        if (selectedID == null || selectedID.isEmpty() || selectedCol == null || selectedCol.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            
            col = new Integer(selectedCol);
            customerLayout = dao.findCustomerLayout(new Integer(selectedID));
            
            Map<String, String> values = new LinkedHashMap<String, String>();
            values.put(JSFUtil.getBundleValue("customerNameTitle"),"customerName");
            values.put(JSFUtil.getBundleValue("idcardTypeTitle"),"idcardTypeId");
            values.put(JSFUtil.getBundleValue("idnoTitle"),"idno");
            values.put(JSFUtil.getBundleValue("refnoTitle"),"referenceno");
            values.put(JSFUtil.getBundleValue("genderTitle"),"gender");
            values.put(JSFUtil.getBundleValue("emailTitle"),"email");
            values.put(JSFUtil.getBundleValue("dobTitle"),"dob");
            values.put(JSFUtil.getBundleValue("weightTitle"),"weight");
            values.put(JSFUtil.getBundleValue("heightTitle"),"height");
            values.put(JSFUtil.getBundleValue("nationalityTitle"),"nationality");
            values.put(JSFUtil.getBundleValue("occupationTitle"),"occupation");
            values.put(JSFUtil.getBundleValue("accountTitle"),"account");
            values.put(JSFUtil.getBundleValue("customerTypeTitle"),"customerType");
            values.put(JSFUtil.getBundleValue("privilegeTitle"),"privilege");
            values.put(JSFUtil.getBundleValue("currentAddressTitle"),"currentAddress");
            values.put(JSFUtil.getBundleValue("homeAddressTitle"),"homeAddress");
            values.put(JSFUtil.getBundleValue("billingAddressTitle"),"billingAddress");
            values.put(JSFUtil.getBundleValue("shippingAddressTitle"),"shippingAddress");
            
            List<CustomerLayoutFxMapping> fieldLists = this.getCustomerLayoutDAO().findCustomerLayoutFxMapping(customerLayout.getId());
                    
            for (CustomerLayoutFxMapping o : fieldLists) {
                values.put(o.getCustomName()+" ("+o.getFieldName()+")", o.getFieldName());
            }
            fieldList = values;

            selectedField.clear();
            List<CustomerLayoutDetail> layoutPages = dao.findLayoutPageColByPageName(customerLayout.getId(), col);
            for(CustomerLayoutDetail o : layoutPages){
                selectedField.add(o.getFieldName());          
            }
       }
    }

    public String backAction() {
        return SUCCESS;
    }
    
    public String saveAction() {
        if((col == 1 || col == 2) && selectedField.size() > 10) {
            message = ("Maximum Field is 10");
            return null;
        } else {
            try {
                List<CustomerLayoutDetail> layoutPages = new ArrayList<CustomerLayoutDetail>();
                int i = 0;
                message = "";
                for(String field : selectedField) {
                    CustomerLayoutDetail customerLayoutDetail = new CustomerLayoutDetail();                    
                    customerLayoutDetail.setCustomerLayout(customerLayout);
                    customerLayoutDetail.setColNo(col);
                    customerLayoutDetail.setSeqNo(++i);
                    customerLayoutDetail.setFieldName(field);
                    layoutPages.add(customerLayoutDetail);
                }
                this.getCustomerLayoutDAO().createCustomerLayoutDetail(layoutPages, customerLayout, col);
            } catch(Exception e) {
                message = e.getMessage();
                return null;
            }
        }
        return SUCCESS;
    }     
     
    //List to UI
    public Map<String, String> getFieldList() {
        return fieldList;
    }
    
    //Properties
    public List<String> getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(List<String> selectedField) {
        this.selectedField = selectedField;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomerLayout getCustomerLayout() {
        return customerLayout;
    }

    public void setCustomerLayout(CustomerLayout customerLayout) {
        this.customerLayout = customerLayout;
    }
    
    public CustomerLayoutDAO getCustomerLayoutDAO() {
        return customerLayoutDAO;
    }

    public void setCustomerLayoutDAO(CustomerLayoutDAO customerLayoutDAO) {
        this.customerLayoutDAO = customerLayoutDAO;
    }
    
}
