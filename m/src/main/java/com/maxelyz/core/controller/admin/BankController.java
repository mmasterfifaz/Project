package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BankDAO;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Bank;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class BankController {

    private static Logger log = Logger.getLogger(BankController.class);
    private static String REFRESH = "bank.xhtml?faces-redirect=true";
    private static String EDIT = "bankedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Bank> bankList;
    private Bank bank;
    
    private String txtSearch = "";
    private String status = "1";
    
    @ManagedProperty(value = "#{bankDAO}")
    private BankDAO bankDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:bank:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        this.search();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:bank:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:bank:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    bank = bankDAO.findBank(item.getKey());
                    bank.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    bank.setUpdateDate(new Date());
                    bank.setEnable(false);
                    bankDAO.edit(bank);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public void searchActionListener(ActionEvent event){
        
        this.search();
    }
    
    private void search(){
        try{
            bankList = bankDAO.findByFilter(txtSearch, status);
        }catch(Exception e){
            log.error(e);
        }
    
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public BankDAO getBankDAO() {
        return bankDAO;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public String getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch = txtSearch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
