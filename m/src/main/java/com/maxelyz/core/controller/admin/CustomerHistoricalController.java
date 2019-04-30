/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;
import com.maxelyz.core.model.dao.CustomerHistoricalGroupDAO;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CustomerHistoricalController implements Serializable {
    private static Logger log = Logger.getLogger(CustomerHistoricalController.class);
    private static String REFRESH = "customerhistorical.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "customerhistorical.jsf";
    private static String EDIT = "customerhistoricaledit.xhtml";
    private static String IMPORT = "customerhistoricalimport.xhtml";
    private static String PREVIEW = "customerhistoricalpreview.xhtml";
    private List<CustomerHistoricalGroup> customerHistoricalGroupList;
    public CustomerHistoricalGroup customerHistoricalGroup;
    
    @ManagedProperty(value = "#{customerHistoricalGroupDAO}")
    private CustomerHistoricalGroupDAO customerHistoricalGroupDAO;
    
    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerhistorical:view")) {  
            SecurityUtil.redirectUnauthorize();  
        }
        customerHistoricalGroupList = this.getCustomerHistoricalGroupDAO().findCustomerHistoricalGroupEntities();
    }
    
    public String editAction() {
        return EDIT;
    }
    
    public String importAction() {
        return IMPORT;
    }
        
    public String purgeAction() {
        String selectedID  = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID==null) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            customerHistoricalGroup = getCustomerHistoricalGroupDAO().findCustomerHistoricalGroup(new Integer(selectedID));
            customerHistoricalGroup.setNoColumn(0);
            customerHistoricalGroup.setHighlightColumn("0");
            customerHistoricalGroup.setUpdateBy(username);
            customerHistoricalGroup.setUpdateDate(now);
            try {
                this.getCustomerHistoricalGroupDAO().editCustomerHistroricalGroup(customerHistoricalGroup);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return REFRESH;
    }

    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:customerhistorical:edit");
    }
            
    public String previewAction() {
        return PREVIEW;
    }
    //List to UI
    public List<CustomerHistoricalGroup> getList() {
        return customerHistoricalGroupList;
    }
        
    // Set properties
    public CustomerHistoricalGroupDAO getCustomerHistoricalGroupDAO() {
        return customerHistoricalGroupDAO;
    }

    public void setCustomerHistoricalGroupDAO(CustomerHistoricalGroupDAO customerHistoricalGroupDAO) {
        this.customerHistoricalGroupDAO = customerHistoricalGroupDAO;
    }
}
