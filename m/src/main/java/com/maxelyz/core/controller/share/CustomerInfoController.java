/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class CustomerInfoController {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");    
    private FileTemplate fileTemplate;
    private List<String> column1, column2, column3, column4;
    
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;
         
    @PostConstruct
    public void initialize() {
        
        // IF HAVE ASSIGNMENT DETAIL GET CUSTOMER LAYOUT TEMPLATE FROM MARKETING FILE TEMPLATE

        if(JSFUtil.getUserSession().getAssignmentDetail() != null && JSFUtil.getUserSession().getAssignmentDetail().getId() != null && JSFUtil.getUserSession().getAssignmentDetail().getAssignmentId() != null) {     
            fileTemplate = JSFUtil.getUserSession().getAssignmentDetail().getAssignmentId().getMarketing().getFileTemplate();
            if(fileTemplate != null) {
                displayCustomerLayout(fileTemplate.getCustomerLayout().getId());
            }
        } else { 
            // IF NO ASSIGNMENT DETAIL GET DEFAULT CUSTOMER LAYOUT FOR CUSTOMER SERVICE            
            displayCustomerLayout(this.getCustomerLayoutDAO().findDefaultCustomerLayout().getId());
        }

    }
    
    public void displayCustomerLayout(Integer customerLayoutID) {
        // PREVIEW CUSTOMER DISPLAY        
        List<CustomerLayoutDetail> layoutPageList = (List) this.getCustomerLayoutDAO().findCustomerLayout(customerLayoutID).getCustomerLayoutDetailCollection();

        column1 = new ArrayList<String>();
        column2 = new ArrayList<String>();
        column3 = new ArrayList<String>();
        column4 = new ArrayList<String>();
        
        if(JSFUtil.getUserSession().getCustomerDetail() != null) {
            CustomerInfoValue customer = JSFUtil.getUserSession().getCustomerDetail();
            
            if(layoutPageList != null) {   
                for (CustomerLayoutDetail obj: layoutPageList) {
                    String label = "";
                    String value = "";
                    if (obj.getFieldName().equals("customerName")) {
                        label = "Customer Name:";
                        if(customer.getInitial() != null) {
                            value = customer.getInitial() + " ";
                        } 
                        if(customer.getName() != null) {
                            value += customer.getName() + " ";
                        }
                        if(customer.getSurname() != null) {
                            value += customer.getSurname() + " ";
                        }
                    } 
                    else if (obj.getFieldName().equals("idcardTypeId")) {
                        label = "Card Type:";
                        value = customer.getCardType() != null ? customer.getCardType() : ""; 
                    } 
                    else if (obj.getFieldName().equals("idno")) {
                        label = "Id Card:";
                        value = customer.getIdno() != null ? customer.getIdno() : "";
                    } 
                    else if (obj.getFieldName().equals("referenceno")) {
                        label = "Reference No:";
                        value = customer.getReferenceNo()!= null ? customer.getReferenceNo() : "";
                    } 
                    else if (obj.getFieldName().equals("gender")) {
                        label = "Gender:";
                        value = customer.getGender() != null ? customer.getGender() : "";
                    }
                    else if (obj.getFieldName().equals("email")) {
                        label = "Email:";
                        value = customer.getEmail()!= null ? customer.getEmail() : "";
                    }
                    else if (obj.getFieldName().equals("dob")) {
                        label = "Date of Birth:";
                        value = customer.getDob() != null ? sdf.format(customer.getDob()) : "";
                    }
                    else if (obj.getFieldName().equals("weight")) {
                        label = "Weight:";
                        value = customer.getWeight() != null ? customer.getWeight().toString() : "";
                    }
                    else if (obj.getFieldName().equals("height")) {
                        label = "Height:";
                        value = customer.getHeight() != null ? customer.getHeight().toString() : "";
                    }
                    else if (obj.getFieldName().equals("nationality")) {
                        label = "Nationality:";
                        value = customer.getNationality() != null ? customer.getNationality() : "";
                    }
                    else if (obj.getFieldName().equals("occupation")) {
                        label = "Occupation:";
                        value = customer.getOccupation() != null ? customer.getOccupation() : "";
                    }
                    else if (obj.getFieldName().equals("account")) {
                        label = "Account:";
                        value = customer.getAccountName() != null ? customer.getAccountName() : "";
                    }
                    else if (obj.getFieldName().equals("customerType")) {
                        label = "Customer Type:";
                        value = customer.getCustomerType() != null ? customer.getCustomerType() : "";
                    }
                    else if (obj.getFieldName().equals("privilege")) {
                        label = "Privilege:";
                        value = customer.getPrivilege() != null ? customer.getPrivilege() : "";
                    }
                    else if (obj.getFieldName().equals("currentAddress")) {
                            String currentAddress = customer.getCurrentFullname() != null ? customer.getCurrentFullname() + " " : " ";
                            currentAddress += customer.getCurrentAddressLine1() != null ? customer.getCurrentAddressLine1() + " " : " ";                            
                            currentAddress += customer.getCurrentAddressLine2() != null ? customer.getCurrentAddressLine2() + " " : " ";
                            currentAddress += customer.getCurrentSubdistrictName() != null ? customer.getCurrentSubdistrictName() + " " : " ";
                            currentAddress += customer.getCurrentDistrictName() != null ? customer.getCurrentDistrictName() + " " : " ";
                            currentAddress += customer.getCurrentProvinceName() != null ? customer.getCurrentProvinceName() + " " : " ";
                            currentAddress += customer.getCurrentPostalCode() != null ? customer.getCurrentPostalCode() + " " : " ";
                            label = "Current Address:";
                            value = currentAddress; 
                    }
                    else if (obj.getFieldName().equals("homeAddress")) {
                            String homeAddress = customer.getHomeFullname() != null ? customer.getHomeFullname() + " " : " ";
                            homeAddress += customer.getHomeAddressLine1() != null ? customer.getHomeAddressLine1() + " " : " ";                            
                            homeAddress += customer.getHomeAddressLine2() != null ? customer.getHomeAddressLine2() + " " : " ";
                            homeAddress += customer.getHomeSubdistrictName() != null ? customer.getHomeSubdistrictName() + " " : " ";
                            homeAddress += customer.getHomeDistrictName() != null ? customer.getHomeDistrictName() + " " : " ";
                            homeAddress += customer.getHomeProvinceName() != null ? customer.getHomeProvinceName() + " " : " ";
                            homeAddress += customer.getHomePostalCode() != null ? customer.getHomePostalCode() + " " : " ";
                            label = "Home Address:";
                            value = homeAddress; 
                    }
                    else if (obj.getFieldName().equals("billingAddress")) {
                            String billingAddress = customer.getBillingFullname() != null ? customer.getBillingFullname() + " " : " ";
                            billingAddress += customer.getBillingAddressLine1() != null ? customer.getBillingAddressLine1() + " " : " ";                            
                            billingAddress += customer.getBillingAddressLine2() != null ? customer.getBillingAddressLine2() + " " : " ";
                            billingAddress += customer.getBillingSubdistrictName() != null ? customer.getBillingSubdistrictName() + " " : " ";
                            billingAddress += customer.getBillingDistrictName() != null ? customer.getBillingDistrictName() + " " : " ";
                            billingAddress += customer.getBillingProvinceName() != null ? customer.getBillingProvinceName() + " " : " ";
                            billingAddress += customer.getBillingPostalCode() != null ? customer.getBillingPostalCode() + " " : " ";
                            label = "Billing Address:";
                            value = billingAddress; 
                    }
                    else if (obj.getFieldName().equals("shippingAddress")) {
                            String shippingAddress = customer.getShippingFullname() != null ? customer.getShippingFullname() + " " : " ";
                            shippingAddress += customer.getShippingAddressLine1() != null ? customer.getShippingAddressLine1() + " " : " ";                            
                            shippingAddress += customer.getShippingAddressLine2() != null ? customer.getShippingAddressLine2() + " " : " ";
                            shippingAddress += customer.getShippingSubdistrictName() != null ? customer.getShippingSubdistrictName() + " " : " ";
                            shippingAddress += customer.getShippingDistrictName() != null ? customer.getShippingDistrictName() + " " : " ";
                            shippingAddress += customer.getShippingProvinceName() != null ? customer.getShippingProvinceName() + " " : " ";
                            shippingAddress += customer.getShippingPostalCode() != null ? customer.getShippingPostalCode() + " " : " ";
                            label = "Shipping Address:";
                            value = shippingAddress; 
                    }
                    else if (obj.getFieldName().contains("flexfield")) {
                            String customName = getCustomerLayoutDAO().findCustomName(customerLayoutID, obj.getFieldName());                            
                            label = customName+": ";
                            if (obj.getFieldName().equals("flexfield1")) {
                                value = customer.getFlexfield1() != null ? customer.getFlexfield1() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield2")) {                
                                value = customer.getFlexfield2() != null ? customer.getFlexfield2() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield3")) {                   
                                value = customer.getFlexfield3() != null ? customer.getFlexfield3() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield4")) {                  
                                value = customer.getFlexfield4() != null ? customer.getFlexfield4() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield5")) {
                                value = customer.getFlexfield5() != null ? customer.getFlexfield5() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield6")) {
                                value = customer.getFlexfield6() != null ? customer.getFlexfield6() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield7")) {
                                value = customer.getFlexfield7() != null ? customer.getFlexfield7() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield8")) {
                                value = customer.getFlexfield8() != null ? customer.getFlexfield8() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield9")) {                 
                                value = customer.getFlexfield9() != null ? customer.getFlexfield9() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield10")) {                  
                                value = customer.getFlexfield10() != null ? customer.getFlexfield10() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield11")) {               
                                value = customer.getFlexfield11() != null ? customer.getFlexfield11() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield12")) {                 
                                value = customer.getFlexfield12() != null ? customer.getFlexfield12() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield13")) {             
                                value = customer.getFlexfield13() != null ? customer.getFlexfield13() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield14")) {                  
                                value = customer.getFlexfield14() != null ? customer.getFlexfield14() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield15")) {          
                                value = customer.getFlexfield15() != null ? customer.getFlexfield15() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield16")) {          
                                value = customer.getFlexfield16() != null ? customer.getFlexfield16() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield17")) {          
                                value = customer.getFlexfield17() != null ? customer.getFlexfield17() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield18")) {          
                                value = customer.getFlexfield18() != null ? customer.getFlexfield18() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield19")) {          
                                value = customer.getFlexfield19() != null ? customer.getFlexfield19() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield20")) {          
                                value = customer.getFlexfield20() != null ? customer.getFlexfield20() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield21")) {
                                value = customer.getFlexfield21() != null ? customer.getFlexfield21() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield22")) {
                                value = customer.getFlexfield22() != null ? customer.getFlexfield22() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield23")) {
                                value = customer.getFlexfield23() != null ? customer.getFlexfield23() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield24")) {
                                value = customer.getFlexfield24() != null ? customer.getFlexfield24() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield25")) {
                                value = customer.getFlexfield25() != null ? customer.getFlexfield25() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield26")) {
                                value = customer.getFlexfield26() != null ? customer.getFlexfield26() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield27")) {
                                value = customer.getFlexfield27() != null ? customer.getFlexfield27() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield28")) {
                                value = customer.getFlexfield28() != null ? customer.getFlexfield28() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield29")) {
                                value = customer.getFlexfield29() != null ? customer.getFlexfield29() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield30")) {
                                value = customer.getFlexfield30() != null ? customer.getFlexfield30() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield31")) {
                                value = customer.getFlexfield31() != null ? customer.getFlexfield31() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield32")) {
                                value = customer.getFlexfield32() != null ? customer.getFlexfield32() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield33")) {
                                value = customer.getFlexfield33() != null ? customer.getFlexfield33() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield34")) {
                                value = customer.getFlexfield34() != null ? customer.getFlexfield34() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield35")) {
                                value = customer.getFlexfield35() != null ? customer.getFlexfield35() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield36")) {
                                value = customer.getFlexfield36() != null ? customer.getFlexfield36() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield37")) {
                                value = customer.getFlexfield37() != null ? customer.getFlexfield37() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield38")) {
                                value = customer.getFlexfield38() != null ? customer.getFlexfield38() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield39")) {
                                value = customer.getFlexfield39() != null ? customer.getFlexfield39() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield40")) {
                                value = customer.getFlexfield40() != null ? customer.getFlexfield40() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield41")) {
                                value = customer.getFlexfield41() != null ? customer.getFlexfield41() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield42")) {
                                value = customer.getFlexfield42() != null ? customer.getFlexfield42() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield43")) {
                                value = customer.getFlexfield43() != null ? customer.getFlexfield43() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield44")) {
                                value = customer.getFlexfield44() != null ? customer.getFlexfield44() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield45")) {
                                value = customer.getFlexfield45() != null ? customer.getFlexfield45() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield46")) {
                                value = customer.getFlexfield46() != null ? customer.getFlexfield46() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield47")) {
                                value = customer.getFlexfield47() != null ? customer.getFlexfield47() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield48")) {
                                value = customer.getFlexfield48() != null ? customer.getFlexfield48() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield49")) {
                                value = customer.getFlexfield49() != null ? customer.getFlexfield49() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield50")) {
                                value = customer.getFlexfield50() != null ? customer.getFlexfield50() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield51")) {
                                value = customer.getFlexfield51() != null ? customer.getFlexfield51() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield52")) {
                                value = customer.getFlexfield52() != null ? customer.getFlexfield52() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield53")) {
                                value = customer.getFlexfield53() != null ? customer.getFlexfield53() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield54")) {
                                value = customer.getFlexfield54() != null ? customer.getFlexfield54() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield55")) {
                                value = customer.getFlexfield55() != null ? customer.getFlexfield55() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield56")) {
                                value = customer.getFlexfield56() != null ? customer.getFlexfield56() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield57")) {
                                value = customer.getFlexfield57() != null ? customer.getFlexfield57() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield58")) {
                                value = customer.getFlexfield58() != null ? customer.getFlexfield58() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield59")) {
                                value = customer.getFlexfield59() != null ? customer.getFlexfield59() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield60")) {
                                value = customer.getFlexfield60() != null ? customer.getFlexfield60() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield61")) {
                                value = customer.getFlexfield61() != null ? customer.getFlexfield61() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield62")) {
                                value = customer.getFlexfield62() != null ? customer.getFlexfield62() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield63")) {
                                value = customer.getFlexfield63() != null ? customer.getFlexfield63() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield64")) {
                                value = customer.getFlexfield64() != null ? customer.getFlexfield64() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield65")) {
                                value = customer.getFlexfield65() != null ? customer.getFlexfield65() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield66")) {
                                value = customer.getFlexfield66() != null ? customer.getFlexfield66() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield67")) {
                                value = customer.getFlexfield67() != null ? customer.getFlexfield67() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield68")) {
                                value = customer.getFlexfield68() != null ? customer.getFlexfield68() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield69")) {
                                value = customer.getFlexfield69() != null ? customer.getFlexfield69() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield70")) {
                                value = customer.getFlexfield70() != null ? customer.getFlexfield70() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield71")) {
                                value = customer.getFlexfield71() != null ? customer.getFlexfield71() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield72")) {
                                value = customer.getFlexfield72() != null ? customer.getFlexfield72() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield73")) {
                                value = customer.getFlexfield73() != null ? customer.getFlexfield73() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield74")) {
                                value = customer.getFlexfield74() != null ? customer.getFlexfield74() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield75")) {
                                value = customer.getFlexfield75() != null ? customer.getFlexfield75() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield76")) {
                                value = customer.getFlexfield76() != null ? customer.getFlexfield76() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield77")) {
                                value = customer.getFlexfield77() != null ? customer.getFlexfield77() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield78")) {
                                value = customer.getFlexfield78() != null ? customer.getFlexfield78() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield79")) {
                                value = customer.getFlexfield79() != null ? customer.getFlexfield79() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield80")) {
                                value = customer.getFlexfield80() != null ? customer.getFlexfield80() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield81")) {
                                value = customer.getFlexfield81() != null ? customer.getFlexfield81() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield82")) {
                                value = customer.getFlexfield82() != null ? customer.getFlexfield82() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield83")) {
                                value = customer.getFlexfield83() != null ? customer.getFlexfield83() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield84")) {
                                value = customer.getFlexfield84() != null ? customer.getFlexfield84() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield85")) {
                                value = customer.getFlexfield85() != null ? customer.getFlexfield85() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield86")) {
                                value = customer.getFlexfield86() != null ? customer.getFlexfield86() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield87")) {
                                value = customer.getFlexfield87() != null ? customer.getFlexfield87() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield88")) {
                                value = customer.getFlexfield88() != null ? customer.getFlexfield88() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield89")) {
                                value = customer.getFlexfield89() != null ? customer.getFlexfield89() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield90")) {
                                value = customer.getFlexfield90() != null ? customer.getFlexfield90() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield91")) {
                                value = customer.getFlexfield91() != null ? customer.getFlexfield91() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield92")) {
                                value = customer.getFlexfield92() != null ? customer.getFlexfield92() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield93")) {
                                value = customer.getFlexfield93() != null ? customer.getFlexfield93() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield94")) {
                                value = customer.getFlexfield94() != null ? customer.getFlexfield94() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield95")) {
                                value = customer.getFlexfield95() != null ? customer.getFlexfield95() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield96")) {
                                value = customer.getFlexfield96() != null ? customer.getFlexfield96() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield97")) {
                                value = customer.getFlexfield97() != null ? customer.getFlexfield97() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield98")) {
                                value = customer.getFlexfield98() != null ? customer.getFlexfield98() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield99")) {
                                value = customer.getFlexfield99() != null ? customer.getFlexfield99() : "-";
                            }
                            else if (obj.getFieldName().equals("flexfield100")) {
                                value = customer.getFlexfield100() != null ? customer.getFlexfield100() : "-";
                            }
                    }
                    
                    // Display
                    if(obj.getColNo().equals(1)) {
                        column1.add(label+" "+value);    
                    } else if(obj.getColNo().equals(2)) {
                        column2.add(label+" "+value);       
                    } else if(obj.getColNo().equals(3)) {
                        column3.add(label+" "+value);       
                    } else if(obj.getColNo().equals(4)) {
                        column4.add(label+" "+value);       
                    }
                }
            }
        }
        
    }
     
    public CustomerLayoutDAO getCustomerLayoutDAO() {
        return customerLayoutDAO;
    }

    public void setCustomerLayoutDAO(CustomerLayoutDAO customerLayoutDAO) {
        this.customerLayoutDAO = customerLayoutDAO;
    }

    public List<String> getColumn1() {
        return column1;
    }

    public void setColumn1(List<String> column1) {
        this.column1 = column1;
    }

    public List<String> getColumn2() {
        return column2;
    }

    public void setColumn2(List<String> column2) {
        this.column2 = column2;
    }

    public List<String> getColumn3() {
        return column3;
    }

    public void setColumn3(List<String> column3) {
        this.column3 = column3;
    }

    public List<String> getColumn4() {
        return column4;
    }

    public void setColumn4(List<String> column4) {
        this.column4 = column4;
    }
    
    
}
