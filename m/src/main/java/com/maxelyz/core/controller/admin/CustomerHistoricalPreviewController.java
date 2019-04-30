/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerHistoricalGroupDAO;
import com.maxelyz.core.model.entity.CustomerHistorical;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import com.maxelyz.core.service.CustomerService;
import com.maxelyz.utils.JSFUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.Collection;
import javax.faces.application.Application;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CustomerHistoricalPreviewController implements Serializable {
    private static Logger log = Logger.getLogger(CustomerHistoricalPreviewController.class);
    private static String REFRESH = "customerhistoricalpreview.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "customerhistorical.jsf";
    private static String BACK = "customerhistorical.xhtml";
    public CustomerHistoricalGroup customerHistoricalGroup;
    private HtmlPanelGrid header,data;
    public List<Integer> highlightCol;
    public List<CustomerHistoricalColumn> highlightColList;
    public Collection<CustomerHistorical> historicalList;
     
    @ManagedProperty(value = "#{customerHistoricalGroupDAO}")
    private CustomerHistoricalGroupDAO customerHistoricalGroupDAO;
    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;
    
    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerhistorical:view")) {  
            SecurityUtil.redirectUnauthorize();  
        }
       
        String selectedID  = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null) {
//            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            customerHistoricalGroup = customerService.findCustomerHistoricalGroup(new Integer(selectedID));
            highlightColList = customerService.findCustomerHistoricalHighlightColumnByGroup(customerHistoricalGroup,customerHistoricalGroup.getHighlightColumn());          
            Application app = FacesContext.getCurrentInstance().getApplication();
            HtmlOutputText value; 
            header = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            header.setColumns(highlightColList.size());
            header.setStyle("text-align: center");  
            data = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            data.setColumns(highlightColList.size());
            data.setStyle("text-align: left");  
            for(CustomerHistoricalColumn o: highlightColList) {
                value = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
                String idStr =o.getCustomerHistoricalGroup().getId().toString()+o.getColumnNo();
                value.setId("header"+idStr+1);
                value.setValue(o.getColumnName());
                header.getChildren().add(value);
            }

            historicalList = customerHistoricalGroup.getCustomerHistoricalCollection();
            for(CustomerHistorical c: historicalList) {
                Integer i = 1;
                for(CustomerHistoricalColumn o: highlightColList) {
                    value = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
                    value.setId("col"+c.getId()+i);
                    i=i+1;
                    if(o.getColumnNo() == 1) {
                        value.setValue(c.getCol1());
                    } else if(o.getColumnNo() == 2) {
                        value.setValue(c.getCol2());
                    } else if(o.getColumnNo() == 3) {
                        value.setValue(c.getCol3());
                    } else if(o.getColumnNo() == 4) {
                        value.setValue(c.getCol4());
                    } else if(o.getColumnNo() == 5) {
                        value.setValue(c.getCol5());
                    } else if(o.getColumnNo() == 6) {
                        value.setValue(c.getCol6());
                    } else if(o.getColumnNo() == 7) {
                        value.setValue(c.getCol7());
                    } else if(o.getColumnNo() == 8) {
                        value.setValue(c.getCol8());
                    } else if(o.getColumnNo() == 9) {
                        value.setValue(c.getCol9());
                    } else if(o.getColumnNo() == 10) {
                        value.setValue(c.getCol10());
                    } else if(o.getColumnNo() == 11) {
                        value.setValue(c.getCol11());
                    } else if(o.getColumnNo() == 12) {
                        value.setValue(c.getCol12());
                    } else if(o.getColumnNo() == 13) {
                        value.setValue(c.getCol13());
                    } else if(o.getColumnNo() == 14) {
                        value.setValue(c.getCol14());
                    } else if(o.getColumnNo() == 15) {
                        value.setValue(c.getCol15());
                    } else if(o.getColumnNo() == 16) {
                        value.setValue(c.getCol16());
                    } else if(o.getColumnNo() == 17) {
                        value.setValue(c.getCol17());
                    } else if(o.getColumnNo() == 18) {
                        value.setValue(c.getCol18());
                    } else if(o.getColumnNo() == 19) {
                        value.setValue(c.getCol19());
                    } else if(o.getColumnNo() == 20) {
                        value.setValue(c.getCol20());
                    } else if(o.getColumnNo() == 21) {
                        value.setValue(c.getCol21());
                    } else if(o.getColumnNo() == 22) {
                        value.setValue(c.getCol22());
                    } else if(o.getColumnNo() == 23) {
                        value.setValue(c.getCol23());
                    } else if(o.getColumnNo() == 24) {
                        value.setValue(c.getCol24());
                    } else if(o.getColumnNo() == 25) {
                        value.setValue(c.getCol25());
                    } else if(o.getColumnNo() == 26) {
                        value.setValue(c.getCol26());
                    } else if(o.getColumnNo() == 27) {
                        value.setValue(c.getCol27());
                    } else if(o.getColumnNo() == 28) {
                        value.setValue(c.getCol28());
                    } else if(o.getColumnNo() == 29) {
                        value.setValue(c.getCol29());
                    } else if(o.getColumnNo() == 30) {
                        value.setValue(c.getCol30());
                    }
                    data.getChildren().add(value);
                }
            }
        }
    }
      
    public String backAction() {
        return BACK;
    }
    
    //List to UI
    public CustomerHistoricalGroup getCustomerHistoricalGroup() {
        return customerHistoricalGroup;
    }
        
    // Set properties
    public CustomerHistoricalGroupDAO getCustomerHistoricalGroupDAO() {
        return customerHistoricalGroupDAO;
    }

    public void setCustomerHistoricalGroupDAO(CustomerHistoricalGroupDAO customerHistoricalGroupDAO) {
        this.customerHistoricalGroupDAO = customerHistoricalGroupDAO;
    }
    
    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    public HtmlPanelGrid getHeader() {
        return header;
    }

    public void setHeader(HtmlPanelGrid header) {
        this.header = header;
    }

    public HtmlPanelGrid getData() {
        return data;
    }

    public void setData(HtmlPanelGrid data) {
        this.data = data;
    }
}
