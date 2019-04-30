/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutFxMapping;
import com.maxelyz.core.model.value.admin.FlexFieldValue;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class CustomerFlexFieldEditController {
    private static Logger log = Logger.getLogger(CustomerFlexFieldEditController.class);
    
    private static String SUCCESS = "customerlayout.xhtml?faces-redirect=true";
    private static String FAILURE = "customerflexfieldedit.xhtml";
    private static String REDIRECT_PAGE = "customerlayout.jsf";
    
    private CustomerLayout customerLayout;
    private String message;
    private int maxFlexField = 100;
    private List<FlexFieldValue> flexFieldValueList;
                    
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;
    
    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerlayout:edit")) {
            SecurityUtil.redirectUnauthorize();  
       }
                      
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       if (selectedID == null || selectedID.isEmpty()) {
           JSFUtil.redirect(REDIRECT_PAGE);
       } else {
           CustomerLayoutDAO dao = getCustomerLayoutDAO();
           customerLayout = dao.findCustomerLayout(new Integer(selectedID));
           if (customerLayout == null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           } else {
                flexFieldValueList = new ArrayList<FlexFieldValue>();
                List<CustomerLayoutFxMapping> list = (List) customerLayout.getCustomerLayoutFxMappingCollection();// dao.findCustomerLayoutFxMapping(customerLayout.getId());
                if(list.isEmpty()) {
                    for(int i=1; i <= maxFlexField; i++) {
                        
                        CustomerLayoutFxMapping value = new CustomerLayoutFxMapping();
                        value.setFieldName("flexfield"+i);
                        value.setCustomName("");
                        value.setCustomerLayout(customerLayout);
                        
                        FlexFieldValue flexFieldValue = new FlexFieldValue();
                        flexFieldValue.setSelected(false);
                        flexFieldValue.setCustomerLayoutFxMapping(value);
                        
                        flexFieldValueList.add(flexFieldValue);
                    }
                } else {
                    for(int i=1; i <= maxFlexField; i++) {
                        FlexFieldValue flexFieldValue = new FlexFieldValue();
                        for (CustomerLayoutFxMapping c : list){
                            if(c.getFieldName().equals("flexfield"+i)){
                                flexFieldValue.setSelected(true);
                                flexFieldValue.setCustomerLayoutFxMapping(c);
                                break;
                            } else {
                                CustomerLayoutFxMapping value = new CustomerLayoutFxMapping();
                                value.setFieldName("flexfield"+i);
                                value.setCustomName("");
                                value.setCustomerLayout(customerLayout);
                                
                                flexFieldValue.setSelected(false);
                                flexFieldValue.setCustomerLayoutFxMapping(value);
                            }
                        }
                        flexFieldValueList.add(flexFieldValue);
                    }
                }
            }
       }
    }
        
    public String backAction() {
        return SUCCESS;
    }
    
    public String saveAction() {
        message = "";
        List<FlexFieldValue> selectedFxFiedls = this.getFlexFieldValueFromCheckBox();
        CustomerLayoutDAO dao = getCustomerLayoutDAO();
        try {
            if(!message.equals(""))  {
                return null;
            } else {            
                if(selectedFxFiedls.isEmpty()) {
                    message = "Please select Field";
                    return null;
                } else {
                    dao.createCustomerLayoutFxMapping(selectedFxFiedls, customerLayout);
                }
            }
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return null;
        }
        return SUCCESS;
    }
     
    private List<FlexFieldValue> getFlexFieldValueFromCheckBox() {
        List<FlexFieldValue> f = new ArrayList<FlexFieldValue>();
        if (flexFieldValueList != null) {
            for (FlexFieldValue obj : flexFieldValueList) {
                if (obj.isSelected()) {
                    if(obj.getCustomerLayoutFxMapping().getCustomName().isEmpty() || obj.getCustomerLayoutFxMapping().getCustomName().equals("")) {
                        message = "Please fill all Custom Name of Selected Flexfield.";
                        break;
                    } else {
                        f.add(obj);
                    }
                }
            }
        }
        return f;
    }
    
    //List to UI
    public List<FlexFieldValue> getFlexFieldValueList() {
        return flexFieldValueList;
    }

    public void setFlexFieldValueList(List<FlexFieldValue> flexFieldValueList) {
       this.flexFieldValueList = flexFieldValueList;
    }

   //Properties    
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
