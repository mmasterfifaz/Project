/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;


import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.ContactTransferDAO;
import com.maxelyz.core.model.entity.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class PhoneDirectoryTelephoneController implements Serializable{
    private static Logger log = Logger.getLogger(PhoneDirectoryTelephoneController.class);
    private static String REFRESH = "popupphonedirectory.xhtml";
    
    private String selectedID;
    private String callCategory = null;
    private String phoneNo = "";
    private String extNo = "";
    private List<PhoneDirectoryCategory> phoneCategoryList;
    private PhoneDirectoryCategory phoneDirectoryCategory;
    private List<PhoneDirectory> phoneDirectoryList;
    private PhoneDirectory phoneDirectory;
    private List<Customer> customers = new ArrayList<Customer>();
    private List<PhoneDirectory> operators = new ArrayList<PhoneDirectory>();
    
    //Search
    private int phoneDirectoryCategoryId = 0;
    private String keyword = "";
    private String name = "";
    private String surname = "";
    private String phoneSearch = "";
    private String description = "";
    
    //contact transfer
    private Boolean transfer = false;
    private Boolean extension = false;
    private String dialStatus = "";
    private String transferType = "";  // 3 Way, Blind
    private String cancelReason = "";  // busy, no answer, wrong transfer
    private ContactTransfer contactTransfer;
    private Integer directoryId;
    
    @ManagedProperty(value="#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    @ManagedProperty(value="#{phoneDirectoryDAO}")
    private PhoneDirectoryDAO phoneDirectoryDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{contactTransferDAO}")
    private ContactTransferDAO contactTransferDAO;
        
    @PostConstruct
    public void initialize() {
        callCategory = null;
        dialStatus = "";
        phoneNo = "";
        transfer = false;
        extension = false;
        phoneCategoryList = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEntities();
        if(phoneCategoryList != null && !phoneCategoryList.isEmpty()) {
            phoneDirectoryCategory = phoneCategoryList.get(0);
            phoneDirectoryList = getPhoneDirectoryDAO().findPhoneDirectoryByCategory(phoneDirectoryCategory.getId());
        }
        dialStatus = JSFUtil.getUserSession().getDialStatus();  
        if(dialStatus != null && dialStatus.equals("active"))
            callCategory = "inbound";
    }

    public void selectPhoneDirectory() {
        selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if(selectedID != null) {
            phoneDirectoryCategory = phoneDirectoryCategoryDAO.findPhoneDirectoryCategory(new Integer(selectedID));
            phoneDirectoryList = getPhoneDirectoryDAO().findPhoneDirectoryByCategory(phoneDirectoryCategory.getId());
        }
        dialStatus = JSFUtil.getUserSession().getDialStatus();
    }
    
    public void selectTelephoneAction() {
        phoneNo = (String) JSFUtil.getRequestParameterMap("phoneNo");
        JSFUtil.getUserSession().setTransferNo(phoneNo);
        String id = (String) JSFUtil.getRequestParameterMap("directoryId");
        directoryId = new Integer(id);
    }
    
    public void clearDirectoryId() {
        directoryId = null;
    }
              
    public void dial() {
        transfer = false;
        extension = false;
        String callFromName="", callNo="", transferToName="";
        ContactHistory contactHistory = null;
        if(!phoneNo.isEmpty()) {
            if(JSFUtil.getUserSession().getDialStatus().equals("idle")) {
                extension = true;
                dialStatus = "dialing";
            } else {
                if(!transferType.isEmpty()) {
                    transfer = true;
                    dialStatus = "dialing consult";
                }
            }
            cancelReason = "";
            //create contact transfer
            if(transfer) {
                if(JSFUtil.getUserSession().getContactHistory() != null) {      //ani, aniName, 
                    contactHistory = JSFUtil.getUserSession().getContactHistory();
                    callNo = contactHistory.getContactTo();
                    if(callNo.length() < 9) {
                        callFromName = findOperator(callNo);
                    } else {
                        callFromName = findCustomer(callNo);
                    }
                }
                if(!phoneNo.isEmpty()) {     //dnis, dnisName,
                    if(directoryId == null)  {
                        transferToName = findOperator(phoneNo);
                    } else {
                        phoneDirectory = getPhoneDirectoryDAO().findPhoneDirectory(directoryId);
                        transferToName = phoneDirectory.getName() + " " + phoneDirectory.getSurname();
                    }
                }
                String uniqueId = JSFUtil.getUserSession().getUniqueId();
                String callCount = JSFUtil.getUserSession().getCallCount();
                Integer callCnt = new Integer(callCount);
                Date now = new Date();
                Users user = JSFUtil.getUserSession().getUsers();
                
                contactTransfer = new ContactTransfer();
                contactTransfer.setId(null);
                contactTransfer.setContactHistory(contactHistory);
                contactTransfer.setUniqueId(uniqueId);
                contactTransfer.setCallCount(new Integer(callCount));
                contactTransfer.setAni(callNo);
                contactTransfer.setAniName(callFromName);
                contactTransfer.setDnis(phoneNo);
                contactTransfer.setDnisName(transferToName);
                contactTransfer.setRequestDate(now);
                contactTransfer.setUsers(user);
                contactTransfer.setCreateBy(user.getName());
                contactTransfer.setCreateDate(now);
                if(transferType.equals("blind")) {
                    contactTransfer.setCompleteDate(now);
                    contactTransfer.setResult("blind");
                    try {
                        this.getContactTransferDAO().create(contactTransfer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    callCnt++;
                    JSFUtil.getUserSession().setCallCount(callCnt.toString());
                }
            }
        }
        updateContactNameOnContactHistory();
    }
    
    public void callExt() {
        if(!extNo.equals("")) {
            //extension = false;
            //dialStatus = "ext";
        }
    }
    
    public void transferCancel() {      //add call count
        if(!cancelReason.isEmpty()) {
            dialStatus = "active";  //JSFUtil.getUserSession().getDialStatus();
            saveContactTransfer(cancelReason);
        }
    }  
    
    public void transferComplete() {    //add call count
        transfer = false;
        dialStatus  = "";
        transferType = "";
        cancelReason = "";
        phoneNo = "";
        dialStatus = "idle";    //dialStatus = JSFUtil.getUserSession().getDialStatus();
        saveContactTransfer("complete");
    }  
    
    public void blindComplete() {    //for blind complete add call count by aspect
        transfer = false;
        dialStatus  = "";
        transferType = "";
        cancelReason = "";
        phoneNo = "";
        dialStatus = "idle";    //dialStatus = JSFUtil.getUserSession().getDialStatus();
//        saveContactTransfer("blind");
    } 
    
    public void saveContactTransfer(String result) {
        Date now = new Date();
        if(contactTransfer != null && !result.equals("blind")) {
            contactTransfer.setCompleteDate(now);
            contactTransfer.setResult(result);
            try {
                this.getContactTransferDAO().create(contactTransfer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
            
//    public void saveContactTransfer(String result) {
//        ContactHistory contactHistory = JSFUtil.getUserSession().getContactHistory();
//        Date now = new Date();
//        if(contactTransfer != null) {
//            String uniqueId = JSFUtil.getUserSession().getUniqueId();
//            contactTransfer.setUniqueId(uniqueId);
//            String callCount = JSFUtil.getUserSession().getCallCount();
//            contactTransfer.setCallCount(new Integer(callCount));
//            Integer callCnt = new Integer(callCount);
//            if(!result.equals("blind")) {
//                contactTransfer.setCompleteDate(now);
//                callCnt++;
//                JSFUtil.getUserSession().setCallCount(callCnt.toString());
//            }  
//            contactTransfer.setResult(result);
//            if(contactHistory != null) {
//                contactTransfer.setContactHistory(contactHistory);
////                JSFUtil.getUserSession().setContactToName(contactTransfer.getAniName());
//                contactHistory.setContactToName(contactTransfer.getAniName());
//                contactHistory.setUniqueId(uniqueId);
//                JSFUtil.getUserSession().setContactHistory(contactHistory);
//            }
//            try {
//                this.getContactTransferDAO().create(contactTransfer);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    
    public void updateContactNameOnContactHistory() {
        String contactToName = "";
        if(JSFUtil.getUserSession().getCustomerDetail() != null) {
            Customer customer = customerDAO.findCustomer(JSFUtil.getUserSession().getCustomerDetail().getId());
            JSFUtil.getUserSession().getContactHistory().setCustomer(customer);
            contactToName = customer.getName() + " " +customer.getSurname();
            JSFUtil.getUserSession().getContactHistory().setContactToName(contactToName);
        } else if(JSFUtil.getUserSession().getContactHistory() != null) {      //ani, aniName, 
                String contactTo = JSFUtil.getUserSession().getContactHistory().getContactTo();
                if(contactTo.length() < 9) {
                    contactToName = findOperator(contactTo);
                } else {
                    contactToName = findCustomer(contactTo);
                    if(customers.size() == 1) {
                        JSFUtil.getUserSession().getContactHistory().setCustomer(customers.get(0));
                    }
                }
                if(contactToName.equals("")) {
                    contactToName = "Unknown";
                }
                JSFUtil.getUserSession().getContactHistory().setContactToName(contactToName);
        } 
        JSFUtil.getUserSession().getContactHistoryDAO().updateContactToName(JSFUtil.getUserSession().getContactHistory());
    }
    
    public String findCustomer(String telephoneNo) {
        String name = "";
        if (telephoneNo != null) {
            customers = this.getCustomerDAO().findCustomerByTelephoneNo(telephoneNo);
            if (customers.size() == 1) {
                Customer c = customers.get(0);
                String custName="";
                if (c.getName()!=null) {
                    custName = c.getName();
                }
                if (c.getSurname()!=null) {
                    custName += " "+c.getSurname();
                }
                name = custName;
            } else if (customers.size() > 1) {
                name = "Multiple Customer";
            } else if (customers == null || customers.isEmpty()) {
                name = "Unknown Customer";
            }
        }
        return name;
    }
    
    public String findOperator(String telephoneNo) {
        String name = "";
        if (telephoneNo != null) {
            operators = this.getPhoneDirectoryDAO().findPhoneDirectorySearchTel(telephoneNo);
            if (operators.size() == 1) {
                PhoneDirectory p = operators.get(0);
                String operatorName="";
                if (p.getName()!=null) {
                    operatorName = p.getName();
                }
                if (p.getSurname()!=null) {
                    operatorName += " "+p.getSurname();
                }
                name = operatorName;
            } else if (operators.size() > 1) {
                name = "Multiple Operators";
            } else if (operators == null || operators.isEmpty()) {
                name = "Unknown Operators";
            }
        }
        return name;
    }
    
    public void searchListener() {
        phoneDirectoryCategory = null;
        phoneDirectoryList = getPhoneDirectoryDAO().findPhoneDirectoryBySearch(phoneDirectoryCategoryId, name, surname, phoneSearch, description, keyword);
    }

    // Get Set DAO
    public PhoneDirectoryCategoryDAO getPhoneDirectoryCategoryDAO() {
        return phoneDirectoryCategoryDAO;
    }

    public void setPhoneDirectoryCategoryDAO(PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO) {
        this.phoneDirectoryCategoryDAO = phoneDirectoryCategoryDAO;
    }

    public PhoneDirectoryDAO getPhoneDirectoryDAO() {
        return phoneDirectoryDAO;
    }

    public void setPhoneDirectoryDAO(PhoneDirectoryDAO phoneDirectoryDAO) {
        this.phoneDirectoryDAO = phoneDirectoryDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public ContactTransferDAO getContactTransferDAO() {
        return contactTransferDAO;
    }

    public void setContactTransferDAO(ContactTransferDAO contactTransferDAO) {
        this.contactTransferDAO = contactTransferDAO;
    }
    
    // Get Set Properties
    public boolean isFoundUniqueCustomer() {
        return customers.size() == 1;
    }
    
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExtNo() {
        return extNo;
    }

    public void setExtNo(String extNo) {
        this.extNo = extNo;
    }
    
    public List<PhoneDirectoryCategory> getPhoneCategoryList() {
        return phoneCategoryList;
    }

    public void setPhoneCategoryList(List<PhoneDirectoryCategory> phoneCategoryList) {
        this.phoneCategoryList = phoneCategoryList;
    }
    
    public List<PhoneDirectory> getPhoneDirectoryList() {
        return phoneDirectoryList;
    }

    public void setPhoneDirectoryList(List<PhoneDirectory> phoneDirectoryList) {
        this.phoneDirectoryList = phoneDirectoryList;
    }
    
    public PhoneDirectoryCategory getPhoneDirectoryCategory() {
        return phoneDirectoryCategory;
    }

    public void setPhoneDirectoryCategory(PhoneDirectoryCategory phoneDirectoryCategory) {
        this.phoneDirectoryCategory = phoneDirectoryCategory;
    }

    public PhoneDirectory getPhoneDirectory() {
        return phoneDirectory;
    }

    public void setPhoneDirectory(PhoneDirectory phoneDirectory) {
        this.phoneDirectory = phoneDirectory;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneDirectoryCategoryId() {
        return phoneDirectoryCategoryId;
    }

    public void setPhoneDirectoryCategoryId(int phoneDirectoryCategoryId) {
        this.phoneDirectoryCategoryId = phoneDirectoryCategoryId;
    }

    public String getPhoneSearch() {
        return phoneSearch;
    }

    public void setPhoneSearch(String phoneSearch) {
        this.phoneSearch = phoneSearch;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public Boolean getExtenstion() {
        return extension;
    }

    public void setExtenstion(Boolean extension) {
        this.extension = extension;
    }

    public String getDialStatus() {
        return dialStatus;
    }

    public void setDialStatus(String dialStatus) {
        this.dialStatus = dialStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
    
    public ContactTransfer getContactTransfer() {
        return contactTransfer;
    }

    public void setContactTransfer(ContactTransfer contactTransfer) {
        this.contactTransfer = contactTransfer;
    }

    public String getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }
}
