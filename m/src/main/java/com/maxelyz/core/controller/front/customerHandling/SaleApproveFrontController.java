/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

/**
 *
 * @author admin
 */
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class SaleApproveFrontController {

    private Integer poId;
    private String ckSpouse;
    private Integer mainPoId;

    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {
        
        if (JSFUtil.getRequestParameterMap("poId") != null && !JSFUtil.getRequestParameterMap("poId").equals("")) {
            poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
        }

        if (JSFUtil.getRequestParameterMap("ckSpouse") != null && !JSFUtil.getRequestParameterMap("ckSpouse").equals("")) {
            ckSpouse = JSFUtil.getRequestParameterMap("ckSpouse");
        }
        
        if (JSFUtil.getRequestParameterMap("mainPoId") != null && !JSFUtil.getRequestParameterMap("mainPoId").equals("")) {
            mainPoId = Integer.parseInt(JSFUtil.getRequestParameterMap("mainPoId"));
        }
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public String getCkSpouse() {
        return ckSpouse;
    }

    public void setCkSpouse(String ckSpouse) {
        this.ckSpouse = ckSpouse;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    
    
    

    

}
