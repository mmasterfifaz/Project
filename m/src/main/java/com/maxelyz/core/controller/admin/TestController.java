package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ActivityTypeDAO;
import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.service.OrderService;
import com.maxelyz.utils.JSFUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.DropEvent;

@ManagedBean
@ViewScoped
public class TestController implements Serializable {

    private static String REFRESH = "test.xhtml";
    private String radioValue;
    private String ajaxvalue = "";
    private Boolean showRadio2 = true;
    private boolean checkbox;
    private String myText;
    private String keyword;
    private String myTextChanged;
    private Long num;
    private HtmlPanelGrid parent;
    private String bblResult;

    List<SwingTreeNodeImpl> rootNodes = null;
    private List<Knowledgebase> knowledgebases;
    
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    
    @ManagedProperty(value = "#{orderService}")
    private OrderService orderService;
    
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    
    @PostConstruct
    public void init() {
        System.out.println(JSFUtil.getUserSession().getRoleKBApproval());
        System.out.println(JSFUtil.getUserSession().getRoleKBApproval());
        System.out.println(JSFUtil.getUserSession().getRoleKBManager());
        knowledgebases = this.getKnowledgebaseDAO().findTop();
    }

    public String clickAction() {
        
        if (radioValue.equals("2")) {
            showRadio2 = false;
        } else {
            showRadio2 = true;
        }
        checkbox = true;
        return null;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public String getAjaxvalue() {
        return radioValue;
    }

    public Boolean getShowRadio2() {
        return showRadio2;
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public List<ContactCase> getCaseList() {
        return this.getContactCaseDAO().findContactCaseEntities();
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public String getMyText() {
        return myText;
    }

    public void setMyText(String myText) {
        this.myText = myText;
    }

    public String getMyTextChanged() {
        return myTextChanged;
    }

    public void setMyTextChanged(String myTextChanged) {
        this.myTextChanged = myTextChanged;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void saveListener(ActionEvent event) {
        myTextChanged = "Hello "+keyword;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
    
    public HtmlPanelGrid getParent() {
        if (parent == null) {
            Application app = FacesContext.getCurrentInstance().getApplication();
            parent = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            parent.setColumns(2);

            HtmlOutputText text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
            text.setValue("text1 from managed bean");
            parent.getChildren().add(text);
            
            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
            text.setValue("text2 from managed bean");
            parent.getChildren().add(text);
            
            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
            text.setValue("text3 from managed bean");
            parent.getChildren().add(text);
            
            
            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
            text.setValue("text4 from managed bean");
            parent.getChildren().add(text);
            
           
            MethodExpression methodExpression = app.getExpressionFactory().createMethodExpression(
            FacesContext.getCurrentInstance().getELContext(), "#{testController.actionListener}", null, new Class[] { ActionEvent.class });

            
            HtmlCommandButton cmdBtn = (HtmlCommandButton) app.createComponent(HtmlCommandButton.COMPONENT_TYPE);
            cmdBtn.setValue("Button Test1");
            cmdBtn.addActionListener(new MethodExpressionActionListener(methodExpression));
            parent.getChildren().add(cmdBtn);
     
            cmdBtn = (HtmlCommandButton) app.createComponent(HtmlCommandButton.COMPONENT_TYPE);
            cmdBtn.setValue("Button Test2");
            cmdBtn.addActionListener(new MethodExpressionActionListener(methodExpression));
            parent.getChildren().add(cmdBtn);
        }
        return parent;
    }
    
     public void  setParent(HtmlPanelGrid parent1) {
         this.parent = parent1;
     }
    
    public void actionListener(ActionEvent event) {
        HtmlCommandButton x = (HtmlCommandButton)event.getComponent();
        System.out.println(x.getValue());
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    
    
    public void bblPostListener(ActionEvent e) {

        String postData = "merchantId=1089&orderRef=tvi00002&amount=1&currCode=764&lang=E&pMethod=VISA&epMonth=08&epYear=2013&cardNo=4907340700229814&securityCode=016&cardHolder=Chitramaikul&payType=N&remark=test_tvi2";
        
        try {
            Map<String, String> resultMap = orderService.bblPost(JSFUtil.getUserSession().getUserName(), postData, "https://psipay.bangkokbank.com/b2c/eng/directPay/payComp.jsp" );
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
        	bblResult += "Key : " + entry.getKey() + " Value : " + entry.getValue();
            }
 
        } catch (Exception ex) {
            bblResult = ex.getMessage();
        }
        
    }

    public String getBblResult() {
        return bblResult;
    }

    public void setBblResult(String bblResult) {
        this.bblResult = bblResult;
    }

    public static void main(String[] arg) {
        try {
            String str_date = "2012-01-01 09:10:11.0";
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
            date = (Date) formatter.parse(str_date);
            System.out.println("Today is " + date);
            System.out.println("Today is " + formatter.format(date));
            
            System.out.println(StringUtils.substring("1234567890123456", 0, 7));
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
        }

    }
    
    //Test Tree
    
//    private void findAndChildNode(SwingTreeNodeImpl parentNode, Knowledgebase parentKb) {
//        SwingTreeNodeImpl childNode;
//        for (Knowledgebase childKb : parentKb.getKnowledgebaseCollection()) {
//            if (childKb.getEnable()) {
//                childNode = new SwingTreeNodeImpl(childKb.getTopic());
//                if(!childKb.getKnowledgebaseCollection().isEmpty()) {
////                    SwingTreeNodeImpl childNode = new SwingTreeNodeImpl(childKb.getTopic());
//                    
//                    this.findAndChildNode(childNode, childKb);//Recursive
//                }
//                parentNode.addChild(childNode);
//            }
//        }
//    }
//            
//    private void addNodes(String path, List<SwingTreeNodeImpl> rootNodes, List<Knowledgebase> kb) {
//        SwingTreeNodeImpl node;
//        for (Knowledgebase kb1 : kb) {
//            node = new SwingTreeNodeImpl(kb1.getTopic());
//            if(!kb1.getKnowledgebaseCollection().isEmpty()) {
////                List<SwingTreeNodeImpl> node1 = new ArrayList<SwingTreeNodeImpl>(); 
//                findAndChildNode(node, kb1);
//                
//            } 
//            rootNodes.add(node);
//        }
//    }
//    
//    public void initNodes() {
//        rootNodes = new ArrayList<SwingTreeNodeImpl>(); 
//        addNodes(null, rootNodes, knowledgebases);
//    }
//    
//    public void setRootNodes(List<SwingTreeNodeImpl> rootNodes) {
//        this.rootNodes = rootNodes; 
//    }
//    
//    public List<SwingTreeNodeImpl> getRootNodes() { 
//        if (rootNodes == null){
//            initNodes(); 
//        }
//        return rootNodes; 
//    }
//    
//    public void dropListener(DropEvent event) {
//        System.out.println("drop");
//    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }
    
    
}
//package com.maxelyz.core.controller.admin;
//
//import java.io.Serializable;
//import java.util.*;
//import org.apache.log4j.Logger;
//import com.maxelyz.core.model.dao.ActivityTypeDAO;
//import com.maxelyz.core.model.dao.ContactCaseDAO;
//import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
//import com.maxelyz.core.model.entity.ActivityType;
//import com.maxelyz.core.model.entity.ContactCase;
//import com.maxelyz.core.service.OrderService;
//import com.maxelyz.utils.JSFUtil;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import javax.annotation.PostConstruct;
//import javax.el.MethodExpression;
//import javax.faces.application.Application;
//import javax.faces.bean.*;
//import javax.faces.component.html.HtmlCommandButton;
//import javax.faces.component.html.HtmlOutputText;
//import javax.faces.component.html.HtmlPanelGrid;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.faces.event.MethodExpressionActionListener;
//import org.apache.commons.lang3.StringUtils;
//import org.richfaces.event.DropEvent;
//
//@ManagedBean
//@ViewScoped
//public class TestController implements Serializable {
//
//    private static String REFRESH = "test.xhtml";
//    private String radioValue;
//    private String ajaxvalue = "";
//    private Boolean showRadio2 = true;
//    private boolean checkbox;
//    private String myText;
//    private String keyword;
//    private String myTextChanged;
//    private Long num;
//    private HtmlPanelGrid parent;
//    private String bblResult;
//
//    List<SwingTreeNodeImpl> rootNodes = null;
//    
//    @ManagedProperty(value = "#{contactCaseDAO}")
//    private ContactCaseDAO contactCaseDAO;
//    
//    @ManagedProperty(value = "#{orderService}")
//    private OrderService orderService;
//    
//    @PostConstruct
//    public void init() {
//        System.out.println(JSFUtil.getUserSession().getRoleKBApproval());
//        System.out.println(JSFUtil.getUserSession().getRoleKBApproval());
//        System.out.println(JSFUtil.getUserSession().getRoleKBManager());
//    }
//
//    public String clickAction() {
//        
//        if (radioValue.equals("2")) {
//            showRadio2 = false;
//        } else {
//            showRadio2 = true;
//        }
//        checkbox = true;
//        return null;
//    }
//
//    public String getRadioValue() {
//        return radioValue;
//    }
//
//    public void setRadioValue(String radioValue) {
//        this.radioValue = radioValue;
//    }
//
//    public String getAjaxvalue() {
//        return radioValue;
//    }
//
//    public Boolean getShowRadio2() {
//        return showRadio2;
//    }
//
//    public boolean getCheckbox() {
//        return checkbox;
//    }
//
//    public void setCheckbox(boolean checkbox) {
//        this.checkbox = checkbox;
//    }
//
//    public List<ContactCase> getCaseList() {
//        return this.getContactCaseDAO().findContactCaseEntities();
//    }
//
//    public ContactCaseDAO getContactCaseDAO() {
//        return contactCaseDAO;
//    }
//
//    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
//        this.contactCaseDAO = contactCaseDAO;
//    }
//
//    public String getMyText() {
//        return myText;
//    }
//
//    public void setMyText(String myText) {
//        this.myText = myText;
//    }
//
//    public String getMyTextChanged() {
//        return myTextChanged;
//    }
//
//    public void setMyTextChanged(String myTextChanged) {
//        this.myTextChanged = myTextChanged;
//    }
//
//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }
//
//    public void saveListener(ActionEvent event) {
//        myTextChanged = "Hello "+keyword;
//    }
//
//    public Long getNum() {
//        return num;
//    }
//
//    public void setNum(Long num) {
//        this.num = num;
//    }
//    
//    public HtmlPanelGrid getParent() {
//        if (parent == null) {
//            Application app = FacesContext.getCurrentInstance().getApplication();
//            parent = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
//            parent.setColumns(2);
//
//            HtmlOutputText text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
//            text.setValue("text1 from managed bean");
//            parent.getChildren().add(text);
//            
//            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
//            text.setValue("text2 from managed bean");
//            parent.getChildren().add(text);
//            
//            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
//            text.setValue("text3 from managed bean");
//            parent.getChildren().add(text);
//            
//            
//            text = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
//            text.setValue("text4 from managed bean");
//            parent.getChildren().add(text);
//            
//           
//            MethodExpression methodExpression = app.getExpressionFactory().createMethodExpression(
//            FacesContext.getCurrentInstance().getELContext(), "#{testController.actionListener}", null, new Class[] { ActionEvent.class });
//
//            
//            HtmlCommandButton cmdBtn = (HtmlCommandButton) app.createComponent(HtmlCommandButton.COMPONENT_TYPE);
//            cmdBtn.setValue("Button Test1");
//            cmdBtn.addActionListener(new MethodExpressionActionListener(methodExpression));
//            parent.getChildren().add(cmdBtn);
//     
//            cmdBtn = (HtmlCommandButton) app.createComponent(HtmlCommandButton.COMPONENT_TYPE);
//            cmdBtn.setValue("Button Test2");
//            cmdBtn.addActionListener(new MethodExpressionActionListener(methodExpression));
//            parent.getChildren().add(cmdBtn);
//        }
//        return parent;
//    }
//    
//     public void  setParent(HtmlPanelGrid parent1) {
//         this.parent = parent1;
//     }
//    
//    public void actionListener(ActionEvent event) {
//        HtmlCommandButton x = (HtmlCommandButton)event.getComponent();
//        System.out.println(x.getValue());
//    }
//
//    public OrderService getOrderService() {
//        return orderService;
//    }
//
//    public void setOrderService(OrderService orderService) {
//        this.orderService = orderService;
//    }
//    
//    
//    public void bblPostListener(ActionEvent e) {
//
//        String postData = "merchantId=1089&orderRef=tvi00002&amount=1&currCode=764&lang=E&pMethod=VISA&epMonth=08&epYear=2013&cardNo=4907340700229814&securityCode=016&cardHolder=Chitramaikul&payType=N&remark=test_tvi2";
//        
//        try {
//            Map<String, String> resultMap = orderService.bblPost(JSFUtil.getUserSession().getUserName(), postData, "https://psipay.bangkokbank.com/b2c/eng/directPay/payComp.jsp" );
//            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
//        	bblResult += "Key : " + entry.getKey() + " Value : " + entry.getValue();
//            }
// 
//        } catch (Exception ex) {
//            bblResult = ex.getMessage();
//        }
//        
//    }
//
//    public String getBblResult() {
//        return bblResult;
//    }
//
//    public void setBblResult(String bblResult) {
//        this.bblResult = bblResult;
//    }
//
//    public static void main(String[] arg) {
//        try {
//            String str_date = "2012-01-01 09:10:11.0";
//            DateFormat formatter;
//            Date date;
//            formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
//            date = (Date) formatter.parse(str_date);
//            System.out.println("Today is " + date);
//            System.out.println("Today is " + formatter.format(date));
//            
//            System.out.println(StringUtils.substring("1234567890123456", 0, 7));
//        } catch (ParseException e) {
//            System.out.println("Exception :" + e);
//        }
//
//    }
//    
//    //Test Tree
//    public void initNodes() {
//        rootNodes = new ArrayList<SwingTreeNodeImpl>(); 
//        SwingTreeNodeImpl node = new SwingTreeNodeImpl("Desktop Type"); 
//        node.addChild(new SwingTreeNodeImpl("Compact")); 
//        node.addChild(new SwingTreeNodeImpl("Everyday")); 
//        node.addChild(new SwingTreeNodeImpl("Gaming")); 
//        node.addChild(new SwingTreeNodeImpl("Premium")); 
//        rootNodes.add(node);
//        node = new SwingTreeNodeImpl("Customer Reviews"); 
//        node.addChild(new SwingTreeNodeImpl("Top Rated")); 
//        rootNodes.add(node);
//    }
//    
//    public void setRootNodes(List<SwingTreeNodeImpl> rootNodes) {
//        this.rootNodes = rootNodes; 
//    }
//    
//    public List<SwingTreeNodeImpl> getRootNodes() { 
//        if (rootNodes == null){
//            initNodes(); 
//        }
//        return rootNodes; 
//    }
//    
//    public void dropListener(DropEvent event) {
//        System.out.println("drop");
//    }
//}