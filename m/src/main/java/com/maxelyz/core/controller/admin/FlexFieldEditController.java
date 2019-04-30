/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import com.maxelyz.core.model.dao.FxMappingDAO;
import com.maxelyz.core.model.dao.LayoutPageDAO;
import com.maxelyz.core.model.entity.FxMapping;
import com.maxelyz.core.model.entity.FxMappingPK;
import com.maxelyz.core.model.value.admin.FlexFieldValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author vee
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class FlexFieldEditController {
    private static Logger log = Logger.getLogger(FlexFieldEditController.class);
    
    private static String SUCCESS = "flexfield.xhtml?faces-redirect=true";
    private static String FAILURE = "flexfieldedit.xhtml";
    private static String REDIRECT_PAGE = "flexfield.jsf";
    private List<FlexFieldValue> flexFieldValueList;
    private String page;
    private String selectedPage;
    private String message;

    @ManagedProperty(value="#{fxMappingDAO}")
    private FxMappingDAO fxMappingDAO;        
    @ManagedProperty(value="#{layoutPageDAO}")
    private LayoutPageDAO layoutPageDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:flexfield:edit")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        message = "";
        selectedPage = JSFUtil.getRequestParameterMap("selectedPage");
           
       if (selectedPage == null || selectedPage.isEmpty()) {
           JSFUtil.redirect(REDIRECT_PAGE);
        } else {
           if(selectedPage.equals("customer"))
               page = "Customer";
           else if(selectedPage.equals("product:life")) 
               page = "Product:Life";
           else if(selectedPage.equals("product:nonlife")) 
               page = "Product:Non Life";
           else if(selectedPage.equals("product:motor")) 
               page = "Product:Motor";
           else if(selectedPage.equals("product:retail")) 
               page = "Product:Retail";         
 
            flexFieldValueList = new ArrayList<FlexFieldValue>();
            List<FxMapping> list = getFxMappingDAO().findFxMappingByTableName(selectedPage);
            if(list.isEmpty())
            {
                for(int i=1;i <= 20;i++){
                    FlexFieldValue flexFieldValue = new FlexFieldValue();
                    FxMapping value = new FxMapping();
                    FxMappingPK valuePK = new FxMappingPK();
                    valuePK.setFxName("flexfield"+i);
                    value.setCustomName("");
                    value.setFxMappingPK(valuePK);
                    flexFieldValue.setSelected(false);
                    flexFieldValue.setFxMapping(value);
                    flexFieldValueList.add(flexFieldValue);
                }
            }else {
                for(int i=1;i <= 20;i++){
                    FlexFieldValue flexFieldValue = new FlexFieldValue();
                    for (FxMapping f : list){
                        if(f.getFxMappingPK().getFxName().equals("flexfield"+i)){
                            flexFieldValue.setSelected(true);
                            flexFieldValue.setFxMapping(f);
                            break;
                        }
                        else {
                            FxMapping value = new FxMapping();
                            FxMappingPK valuePK = new FxMappingPK();
                            valuePK.setFxName("flexfield"+i);
                            value.setCustomName("");
                            value.setFxMappingPK(valuePK);
                            flexFieldValue.setSelected(false);
                            flexFieldValue.setFxMapping(value);
                        }
                    }
                    flexFieldValueList.add(flexFieldValue);
                }
            }
       }
    }
        
    public String backAction() {
        return SUCCESS;
    }
    
    public String saveAction() {
        List<FlexFieldValue> selectedFxFiedls = this.getFlexFieldValueFromCheckBox();
        
        try {
            if(selectedFxFiedls.isEmpty()) {
                message = "Please select Field";
                return null;
            } else {
                //Clear Data in Customer Layout
                if(page.equals("Customer")) {
                    layoutPageDAO.deleteLayoutPageName("customer_handling");
                }
                getFxMappingDAO().createFlexFieldValue(selectedFxFiedls, selectedPage);
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }
     
    private List<FlexFieldValue> getFlexFieldValueFromCheckBox() {
        List<FlexFieldValue> f = new ArrayList<FlexFieldValue>();
        if (flexFieldValueList != null) {
            for (FlexFieldValue obj : flexFieldValueList) {
                if (obj.isSelected()) {
                    f.add(obj);
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
    public FxMappingDAO getFxMappingDAO() {
        return fxMappingDAO;
    }

    public void setFxMappingDAO(FxMappingDAO fxMappingDAO) {
        this.fxMappingDAO = fxMappingDAO;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LayoutPageDAO getLayoutPageDAO() {
        return layoutPageDAO;
    }

    public void setLayoutPageDAO(LayoutPageDAO layoutPageDAO) {
        this.layoutPageDAO = layoutPageDAO;
    }
    
    
}
