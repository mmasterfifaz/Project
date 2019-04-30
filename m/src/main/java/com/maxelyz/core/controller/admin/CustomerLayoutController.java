/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutFxMapping;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
/**
 *
 * @author vee
 */
@ManagedBean
@RequestScoped
public class CustomerLayoutController implements Serializable {
    private static Logger log = Logger.getLogger(CustomerLayoutController.class);
    private static String REFRESH = "customerlayout.xhtml?faces-redirect=true";
    private static String EDIT = "customerlayoutedit.xhtml";
    private static String EDITFXLABEL = "customerflexfieldedit.xhtml";
    private static String EDITDISPLAY = "customerdisplay.xhtml";
     
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CustomerLayout> customerLayoutList;
    private CustomerLayout customerLayout;
    
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:customerlayout:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CustomerLayoutDAO dao = getCustomerLayoutDAO();
        customerLayoutList = dao.findCustomerLayoutEntities();        
    }

    public String editAction() {
       return EDIT;
    }    
        
    public String setCustomerDisplay() {
        return EDITDISPLAY;
    }
    
    public String setFlexFieldLabel() {
        return EDITFXLABEL;
    }
        
    public String copyAction() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        
        try {
            CustomerLayout copyCustomerLayout = customerLayoutDAO.findCustomerLayout(new Integer(selectedID));
            CustomerLayout newCustomerLayout = new CustomerLayout();

            newCustomerLayout.setId(null);
            newCustomerLayout.setName(copyCustomerLayout.getName() + " (2) ");            
            newCustomerLayout.setDescripion(copyCustomerLayout.getDescripion());
            newCustomerLayout.setEnable(Boolean.TRUE);
            newCustomerLayout.setIsDefault(Boolean.FALSE);
            newCustomerLayout.setCreateBy(username);
            newCustomerLayout.setCreateDate(now);
            newCustomerLayout.setUpdateBy(username);
            newCustomerLayout.setUpdateDate(now);            
            newCustomerLayout.setCustomerLayoutFxMappingCollection(null);
            newCustomerLayout.setCustomerLayoutDetailCollection(null);
            customerLayoutDAO.create(newCustomerLayout);
            
            //SET FX MAPPING COLLECTION
            List<CustomerLayoutFxMapping> newFxMappingCollection = new ArrayList<CustomerLayoutFxMapping>();
            for(CustomerLayoutFxMapping copyFX: copyCustomerLayout.getCustomerLayoutFxMappingCollection()) {
                CustomerLayoutFxMapping newFX = new CustomerLayoutFxMapping();
                newFX.setCustomName(copyFX.getCustomName());
                newFX.setFieldName(copyFX.getFieldName());
                newFX.setCustomerLayout(newCustomerLayout);
                newFxMappingCollection.add(newFX);
            }
            newCustomerLayout.setCustomerLayoutFxMappingCollection(newFxMappingCollection);
            
            //SET CUSTOMER DISPLAY COLLECTION
            List<CustomerLayoutDetail> newCustomerDisplay = new ArrayList<CustomerLayoutDetail>();
            for(CustomerLayoutDetail copyDisplay: copyCustomerLayout.getCustomerLayoutDetailCollection()) {
                CustomerLayoutDetail newDisplay = new CustomerLayoutDetail();
                newDisplay.setColNo(copyDisplay.getColNo());
                newDisplay.setSeqNo(copyDisplay.getSeqNo());                
                newDisplay.setFieldName(copyDisplay.getFieldName());
                newDisplay.setCustomerLayout(newCustomerLayout);
                newCustomerDisplay.add(newDisplay);
            }
            newCustomerLayout.setCustomerLayoutDetailCollection(newCustomerDisplay);
            
            customerLayoutDAO.edit(newCustomerLayout);
            
        } catch(Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String deleteAction() throws Exception {
        CustomerLayoutDAO dao = getCustomerLayoutDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    customerLayout = dao.findCustomerLayout(item.getKey());
                    customerLayout.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    customerLayout.setUpdateDate(new Date());
                    customerLayout.setEnable(false);
                    dao.edit(customerLayout);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:customerlayout:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:customerlayout:delete");
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<CustomerLayout> getCustomerLayoutList() {
        return customerLayoutList;
    }

    public void setCustomerLayoutList(List<CustomerLayout> customerLayoutList) {
        this.customerLayoutList = customerLayoutList;
    }
    
    //GET-SET DAO
    public CustomerLayoutDAO getCustomerLayoutDAO() {
        return customerLayoutDAO;
    }

    public void setCustomerLayoutDAO(CustomerLayoutDAO customerLayoutDAO) {
        this.customerLayoutDAO = customerLayoutDAO;
    }
    
}
