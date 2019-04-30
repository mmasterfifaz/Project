/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
/**
 *
 * @author vee
 */
@ManagedBean
@ViewScoped
public class CustomerDisplayController {
    private static Logger log = Logger.getLogger(CustomerDisplayController.class);
    
    private static String EDIT = "customerdisplayedit.xhtml";
    private static String REDIRECT_PAGE = "customerlayout.jsf";
    private static String SUCCESS = "customerlayout.xhtml";
    
    private CustomerLayout customerLayout;
    private HtmlPanelGrid col1, col2, col3, col4;
    
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:customerlayout:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            //JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            CustomerLayoutDAO dao = getCustomerLayoutDAO();
            customerLayout = dao.findCustomerLayout(new Integer(selectedID));

            if (customerLayout == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                List<CustomerLayoutDetail> layoutPageList = (List) customerLayout.getCustomerLayoutDetailCollection();
                Application app = FacesContext.getCurrentInstance().getApplication();

                col1 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
                col1.setColumns(1);
                col1.setColumnClasses("gray03"); 
                col1.setStyle("text-align: left");  
                
                col2 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
                col2.setColumns(1);
                col2.setColumnClasses("gray03");
                col2.setStyle("text-align: left");  
                
                col3 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
                col3.setColumns(1);
                col3.setColumnClasses("gray03");
                col3.setStyle("text-align: left");  
                
                col4 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
                col4.setColumns(1);
                col4.setColumnClasses("gray03");
                col4.setStyle("text-align: left");  
                HtmlOutputText label; 
                
                if(layoutPageList != null) {   
                    for (CustomerLayoutDetail obj: layoutPageList) {
                        label = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
                        label.setId(obj.getFieldName());

                        if (obj.getFieldName().equals("customerName")) {
                            label.setValue(JSFUtil.getBundleValue("customerNameTitle"));
                        } else if (obj.getFieldName().equals("idcardTypeId")) {
                            label.setValue(JSFUtil.getBundleValue("idcardTypeTitle"));
                        } else if (obj.getFieldName().equals("idno")) {
                            label.setValue(JSFUtil.getBundleValue("idnoTitle"));
                        } else if (obj.getFieldName().equals("referenceno")) {
                            label.setValue(JSFUtil.getBundleValue("refnoTitle"));
                        } else if (obj.getFieldName().equals("gender")) {
                            label.setValue(JSFUtil.getBundleValue("genderTitle"));
                        } else if (obj.getFieldName().equals("email")) {
                            label.setValue(JSFUtil.getBundleValue("emailTitle"));
                        } else if (obj.getFieldName().equals("dob")) {
                            label.setValue(JSFUtil.getBundleValue("dobTitle"));
                        } else if (obj.getFieldName().equals("weight")) {
                            label.setValue(JSFUtil.getBundleValue("weightTitle"));
                        } else if (obj.getFieldName().equals("height")) {
                            label.setValue(JSFUtil.getBundleValue("heightTitle"));
                        } else if (obj.getFieldName().equals("nationality")) {
                            label.setValue(JSFUtil.getBundleValue("nationalityTitle"));
                        } else if (obj.getFieldName().equals("occupation")) {
                            label.setValue(JSFUtil.getBundleValue("occupationTitle"));
                        } else if (obj.getFieldName().equals("account")) {
                            label.setValue(JSFUtil.getBundleValue("accountTitle"));
                        } else if (obj.getFieldName().equals("customerType")) {
                            label.setValue(JSFUtil.getBundleValue("customerTypeTitle"));
                        } else if (obj.getFieldName().equals("privilege")) {
                            label.setValue(JSFUtil.getBundleValue("privilegeTitle"));
                        } else if (obj.getFieldName().equals("currentAddress")) {
                            label.setValue(JSFUtil.getBundleValue("currentAddressTitle"));
                        } else if (obj.getFieldName().equals("homeAddress")) {
                            label.setValue(JSFUtil.getBundleValue("homeAddressTitle"));
                        } else if (obj.getFieldName().equals("billingAddress")) {
                            label.setValue(JSFUtil.getBundleValue("billingAddressTitle"));
                        } else if (obj.getFieldName().equals("shippingAddress")) {
                            label.setValue(JSFUtil.getBundleValue("shippingAddressTitle"));
                        } else if (obj.getFieldName().contains("flexfield")) {
                            String customName = dao.findCustomName(customerLayout.getId(), obj.getFieldName());
                            label.setValue(customName);
                        }

                        // Display
                        if(obj.getColNo().equals(1)) {
                            col1.getChildren().add(label);       
                        } else if(obj.getColNo().equals(2)) {
                            col2.getChildren().add(label);       
                        } else if(obj.getColNo().equals(3)) {
                            col3.getChildren().add(label);       
                        } else if(obj.getColNo().equals(4)) {
                            col4.getChildren().add(label);       
                        }
                    }
                }
            }
        }
    }
    
    public boolean isEditPermitted() {
 	   return SecurityUtil.isPermitted("admin:customerlayout:edit"); 
    }  
    
    public String editAction() {
        return EDIT;
    }
         
    public String backAction() {
        return SUCCESS;
    }
    
    public HtmlPanelGrid getCol1() {
        return col1;
    }

    public HtmlPanelGrid getCol2() {
        return col2;
    }

    public void setCol1(HtmlPanelGrid col1) {
        this.col1 = col1;
    }

    public void setCol2(HtmlPanelGrid col2) {
        this.col2 = col2;
    }

    public HtmlPanelGrid getCol3() {
        return col3;
    }

    public void setCol3(HtmlPanelGrid col3) {
        this.col3 = col3;
    }

    public HtmlPanelGrid getCol4() {
        return col4;
    }

    public void setCol4(HtmlPanelGrid col4) {
        this.col4 = col4;
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
