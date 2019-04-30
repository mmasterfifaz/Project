/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.entity.CustomerHistorical;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import com.maxelyz.core.service.CustomerService;
import javax.faces.event.ActionEvent;
import com.maxelyz.utils.JSFUtil;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.value.front.customerHandling.CustomerHistoricalValue;
import java.util.*;
import com.maxelyz.utils.SecurityUtil;
/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CustomerHistoricalSummaryController {
    private static Logger log = Logger.getLogger(CustomerHistoricalSummaryController.class);
    private static String REDIRECT_PAGE = "customerhandling.jsf";
    public CustomerHistoricalGroup groupList1, groupList2, groupList3, groupList4, groupList5;
    private HtmlPanelGrid data,data1,data2,data3,data4,data5;
    private HtmlCommandButton button;
    public List<Integer> highlightCol;
    public List<CustomerHistoricalColumn> highlightColList, colHeaderList;
    public Collection<CustomerHistorical> historicalList;
    public List<CustomerHistoricalValue> customerHistoricalDetail = new ArrayList<CustomerHistoricalValue>();
    public CustomerHistorical historicalDetail;
    public String selectedTab;
    public String tab;
    public String refCustomer;
    public Boolean showDetail = false;
    public Application app;
        
    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("customerhistorical:view")) {  
            SecurityUtil.redirectUnauthorize();  
        }
        
        tab = JSFUtil.getRequestParameterMap("tab");
        if(tab==null) {
            tab = "group1";
        }
        app = FacesContext.getCurrentInstance().getApplication();
        data1 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        data2 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        data3 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        data4 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        data5 = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        refCustomer = JSFUtil.getUserSession().getCustomerDetail().getReferenceNo();
        groupList1 = customerService.findCustomerHistoricalGroup(1);
        if(groupList1 != null) {
            data1 = createHighlightColumn(groupList1,refCustomer,"group1");
        }
        groupList2 = customerService.findCustomerHistoricalGroup(2);
        if(groupList2 != null) {
            data2 = createHighlightColumn(groupList2,refCustomer,"group2");
        }
        groupList3 = customerService.findCustomerHistoricalGroup(3);
        if(groupList3 != null) {
            data3 = createHighlightColumn(groupList3,refCustomer,"group3");
        }
        groupList4 = customerService.findCustomerHistoricalGroup(4);
        if(groupList4 != null) {
            data4 = createHighlightColumn(groupList4,refCustomer,"group4");
        }
        groupList5 = customerService.findCustomerHistoricalGroup(5);
        if(groupList5 != null) {
            data5 = createHighlightColumn(groupList5,refCustomer,"group5");
        }
        JSFUtil.getUserSession().setRefNo(null);
    }
    
    public void backAction() {
        showDetail = false;
        FacesContext.getCurrentInstance().renderResponse();
    }
   
    public void disableDetail() {
        showDetail = false;
    }
    
    public void actionListener(ActionEvent event) {
        HtmlCommandButton but = (HtmlCommandButton)event.getComponent();
        String detailId = but.getValue().toString();
        tab = but.getAlt().toString();
        
        if(tab.equals("group1")) {
            colHeaderList = customerService.findCustomerHistoricalColumnByGroup(groupList1);
        } else if(tab.equals("group2")) {
            colHeaderList = customerService.findCustomerHistoricalColumnByGroup(groupList2);
        } else if(tab.equals("group3")) {
            colHeaderList = customerService.findCustomerHistoricalColumnByGroup(groupList3);
        } else if(tab.equals("group4")) {
            colHeaderList = customerService.findCustomerHistoricalColumnByGroup(groupList4);
        } else if(tab.equals("group5")) {
            colHeaderList = customerService.findCustomerHistoricalColumnByGroup(groupList5);
        }
        
        historicalDetail = customerService.findCustomerHistoricalById(new Integer(detailId)); 
        if(customerHistoricalDetail != null) {
            customerHistoricalDetail.clear();
        }
        for(CustomerHistoricalColumn o: colHeaderList) {
            CustomerHistoricalValue values = new CustomerHistoricalValue();
            values.setId(o.getColumnNo());
            values.setColumnName(o.getColumnName());
            if(o.getColumnNo() == 1) {
                values.setDetail(historicalDetail.getCol1());                
            } else if(o.getColumnNo() == 2) {
                values.setDetail(historicalDetail.getCol2());
            } else if(o.getColumnNo() == 3) {
                values.setDetail(historicalDetail.getCol3());
            } else if(o.getColumnNo() == 4) {
                values.setDetail(historicalDetail.getCol4());
            } else if(o.getColumnNo() == 5) {
                values.setDetail(historicalDetail.getCol5());
            } else if(o.getColumnNo() == 6) {
                values.setDetail(historicalDetail.getCol6());
            } else if(o.getColumnNo() == 7) {
                values.setDetail(historicalDetail.getCol7());
            } else if(o.getColumnNo() == 8) {
                values.setDetail(historicalDetail.getCol8());
            } else if(o.getColumnNo() == 9) {
                values.setDetail(historicalDetail.getCol9());
            } else if(o.getColumnNo() == 10) {
                values.setDetail(historicalDetail.getCol10());
            } else if(o.getColumnNo() == 11) {
                values.setDetail(historicalDetail.getCol11());
            } else if(o.getColumnNo() == 12) {
                values.setDetail(historicalDetail.getCol12());
            } else if(o.getColumnNo() == 13) {
                values.setDetail(historicalDetail.getCol13());
            } else if(o.getColumnNo() == 14) {
                values.setDetail(historicalDetail.getCol14());
            } else if(o.getColumnNo() == 15) {
                values.setDetail(historicalDetail.getCol15());
            } else if(o.getColumnNo() == 16) {
                values.setDetail(historicalDetail.getCol16());
            } else if(o.getColumnNo() == 17) {
                values.setDetail(historicalDetail.getCol17());
            } else if(o.getColumnNo() == 18) {
                values.setDetail(historicalDetail.getCol18());
            } else if(o.getColumnNo() == 19) {
                values.setDetail(historicalDetail.getCol19());
            } else if(o.getColumnNo() == 20) {
                values.setDetail(historicalDetail.getCol20());
            } else if(o.getColumnNo() == 21) {
                values.setDetail(historicalDetail.getCol21());
            } else if(o.getColumnNo() == 22) {
                values.setDetail(historicalDetail.getCol22());
            } else if(o.getColumnNo() == 23) {
                values.setDetail(historicalDetail.getCol23());
            } else if(o.getColumnNo() == 24) {
                values.setDetail(historicalDetail.getCol24());
            } else if(o.getColumnNo() == 25) {
                values.setDetail(historicalDetail.getCol25());
            } else if(o.getColumnNo() == 26) {
                values.setDetail(historicalDetail.getCol26());
            } else if(o.getColumnNo() == 27) {
                values.setDetail(historicalDetail.getCol27());
            } else if(o.getColumnNo() == 28) {
                values.setDetail(historicalDetail.getCol28());
            } else if(o.getColumnNo() == 29) {
                values.setDetail(historicalDetail.getCol29());
            } else if(o.getColumnNo() == 30) {
                values.setDetail(historicalDetail.getCol30());
            } 
            customerHistoricalDetail.add(values);
        }
        showDetail = true;
        FacesContext.getCurrentInstance().renderResponse();
    }
       
    public HtmlPanelGrid createHighlightColumn(CustomerHistoricalGroup customerHistoricalGroup,String refCustomer,String tab) {
        highlightColList = customerService.findCustomerHistoricalHighlightColumnByGroup(customerHistoricalGroup,customerHistoricalGroup.getHighlightColumn());          
        historicalList = customerService.findCustomerHistoricalByReferenceCustomer(customerHistoricalGroup,refCustomer);
        HtmlOutputText value = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
        data = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        if(!historicalList.isEmpty()) {
            String style = "subh01";
            data.setColumns(highlightColList.size()+1);
            value.setValue("");
            data.getChildren().add(value);
            for(CustomerHistoricalColumn o: highlightColList) {
                value = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
                String idStr =o.getCustomerHistoricalGroup().getId().toString()+o.getColumnNo();
                value.setId(tab+idStr+1);
                value.setValue(o.getColumnName());
                data.getChildren().add(value);
            }
            ExpressionFactory expressionFactory = app.getExpressionFactory();
            MethodExpression methodExpression = expressionFactory.createMethodExpression(FacesContext.getCurrentInstance().getELContext(),"#{customerHistoricalSummaryController.actionListener}", null, new Class[] { javax.faces.event.ActionEvent.class });
            for(CustomerHistorical c: historicalList) {
                style += ",tableline01";
                button = (HtmlCommandButton) app.createComponent(HtmlCommandButton.COMPONENT_TYPE);
                button.setImage("../../img/b_view.png");
                button.setValue(c.getId());
                button.setAlt(tab);
                button.addActionListener(new MethodExpressionActionListener(methodExpression));
                data.getChildren().add(button);
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
            data.setRowClasses(style);
        }
        return data;
    }
    
    //List to UI
        
    public List<CustomerHistoricalValue> getCustomerHistoricalDetail() {
        return customerHistoricalDetail;
    }
          
    public CustomerHistoricalGroup getGroupList1() {
        return groupList1;
    }
    
    public CustomerHistoricalGroup getGroupList2() {
        return groupList2;
    }
    
    public CustomerHistoricalGroup getGroupList3() {
        return groupList3;
    }
    
    public CustomerHistoricalGroup getGroupList4() {
        return groupList4;
    }
    
    public CustomerHistoricalGroup getGroupList5() {
        return groupList5;
    }

    public HtmlPanelGrid getData1() {
        return data1;
    }

    public void setData1(HtmlPanelGrid data1) {        
      //  this.data1 = data1;
    }
    
    public HtmlPanelGrid getData2() {
        return data2;
    }

    public void setData2(HtmlPanelGrid data2) {
      //  this.data2 = data2;
    }

    public HtmlPanelGrid getData3() {
        return data3;
    }

    public void setData3(HtmlPanelGrid data) {
        // this.data3 = data;
    }

    public HtmlPanelGrid getData4() {
        return data4;
    }

    public void setData4(HtmlPanelGrid data) {
       // this.data4 = data;
    }

    public HtmlPanelGrid getData5() {
        return data5;
    }

    public void setData5(HtmlPanelGrid data) {
       // this.data5 = data;
    }
    
    //Set Properties
    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    public String getSelectedTab() {  
         return selectedTab;  
     }  
    
    public void setSelectedTab(String selectedTab) {  
         this.selectedTab = selectedTab;  
     }  
    
    public boolean isEnableDetail() {
        return showDetail;
    }

    public void setEnableDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }
}
