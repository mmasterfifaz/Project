package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BankDAO;
import com.maxelyz.core.model.dao.CardIssuerDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Bank;
import com.maxelyz.core.model.entity.CardIssuer;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.faces.bean.*;
//import org.ajax4jsf.model.KeepAlive;

@ManagedBean
//@RequestScoped
@ViewScoped
public class BankEditController {
    private static Logger log = Logger.getLogger(BankEditController.class);
    private static String REDIRECT_PAGE = "bank.jsf";
    private static String SUCCESS = "bank.xhtml?faces-redirect=true";
    private static String FAILURE = "bankedit.xhtml";

    private Bank bank;
    private String mode;
    private String message;
    private String messageDup;
    
    private CardIssuer cardIssuerVisa;
    private CardIssuer cardIssuerMaster;

    private String visaCardNo = "";
    private String masterCardNo = "";
    
    @ManagedProperty(value="#{bankDAO}")
    private BankDAO bankDAO;
    @ManagedProperty(value="#{cardIssuerDAO}")
    private CardIssuerDAO cardIssuerDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:bank:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        cardIssuerVisa = new CardIssuer();
        cardIssuerMaster = new CardIssuer();

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            bank = new Bank();
            bank.setEnable(Boolean.TRUE);
            bank.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                bank = bankDAO.findBank(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (bank == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }else{
                if(bank.getCardIssuerCollection() != null && !bank.getCardIssuerCollection().isEmpty()){
                    for(CardIssuer cardIssuer : bank.getCardIssuerCollection()){
                        if(cardIssuer.getCardType().equals("VISA")){
                            cardIssuerVisa = cardIssuer;
                        }else if(cardIssuer.getCardType().equals("MASTER")){
                            cardIssuerMaster = cardIssuer;
                        }
                    }
                }
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:bank:add");
        } else {
            return SecurityUtil.isPermitted("admin:bank:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkName(bank)) {
            messageDup = "";
            try {
                if (getMode().equals("add")) {
                    bank.setId(null);
                    bank.setEnable(true);
                    bankDAO.create(bank);
                    
                    if(cardIssuerVisa != null){
                        cardIssuerVisa.setCode("V" + bank.getCode());
                        cardIssuerVisa.setName("V" + bank.getCode());
                        cardIssuerVisa.setBank(bank);
                        cardIssuerVisa.setIssuerName(bank.getName());
                        cardIssuerVisa.setIssuerCountry("THAILAND");
                        cardIssuerVisa.setCardType("VISA");
                        cardIssuerVisa.setCreateBy(JSFUtil.getUserSession().getUserName());
                        cardIssuerVisa.setCreateDate(new Date());
                        cardIssuerDAO.create(cardIssuerVisa);
                    }
                    
                    if(cardIssuerMaster != null){
                        cardIssuerMaster.setCode("M" + bank.getCode());
                        cardIssuerMaster.setName("M" + bank.getCode());
                        cardIssuerMaster.setBank(bank);
                        cardIssuerMaster.setIssuerName(bank.getName());
                        cardIssuerMaster.setIssuerCountry("THAILAND");
                        cardIssuerMaster.setCardType("MASTER");
                        cardIssuerMaster.setCreateBy(JSFUtil.getUserSession().getUserName());
                        cardIssuerMaster.setCreateDate(new Date());
                        cardIssuerDAO.create(cardIssuerMaster);
                    }
                } else {
                    bankDAO.edit(bank);
                    
                    cardIssuerVisa.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    cardIssuerVisa.setUpdateDate(new Date());
                    if(cardIssuerVisa.getId() != null){
                        cardIssuerDAO.edit(cardIssuerVisa);
                    } else {
                        cardIssuerVisa.setCode("V" + bank.getCode());
                        cardIssuerVisa.setName("V" + bank.getCode());
                        cardIssuerVisa.setBank(bank);
                        cardIssuerVisa.setIssuerName(bank.getName());
                        cardIssuerVisa.setIssuerCountry("THAILAND");
                        cardIssuerVisa.setCardType("VISA");
                        cardIssuerVisa.setCreateBy(JSFUtil.getUserSession().getUserName());
                        cardIssuerVisa.setCreateDate(new Date());
                        cardIssuerDAO.create(cardIssuerVisa);
                        
                    }
                    
                    cardIssuerMaster.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    cardIssuerMaster.setUpdateDate(new Date());
                    if(cardIssuerMaster.getId() != null){
                        cardIssuerDAO.edit(cardIssuerMaster);
                    } else {
                        cardIssuerMaster.setCode("M" + bank.getCode());
                        cardIssuerMaster.setName("M" + bank.getCode());
                        cardIssuerMaster.setBank(bank);
                        cardIssuerMaster.setIssuerName(bank.getName());
                        cardIssuerMaster.setIssuerCountry("THAILAND");
                        cardIssuerMaster.setCardType("MASTER");
                        cardIssuerMaster.setCreateBy(JSFUtil.getUserSession().getUserName());
                        cardIssuerMaster.setCreateDate(new Date());
                        cardIssuerDAO.create(cardIssuerMaster);
                    }
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkCode(Bank bank) {
        String code = bank.getCode();
        Integer id=0; 
        if(bank.getId() != null)
            id = bank.getId();
        
        Integer cnt = bankDAO.checkBankCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public Boolean checkName(Bank bank) {
        String name = bank.getName();
        Integer id=0; 
        if(bank.getId() != null)
            id = bank.getId();
        
        Integer cnt = bankDAO.checkBankName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getVisaCardNo() {
        return visaCardNo;
    }

    public void setVisaCardNo(String visaCardNo) {
        this.visaCardNo = visaCardNo;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public CardIssuerDAO getCardIssuerDAO() {
        return cardIssuerDAO;
    }

    public void setCardIssuerDAO(CardIssuerDAO cardIssuerDAO) {
        this.cardIssuerDAO = cardIssuerDAO;
    }

    public CardIssuer getCardIssuerVisa() {
        return cardIssuerVisa;
    }

    public void setCardIssuerVisa(CardIssuer cardIssuerVisa) {
        this.cardIssuerVisa = cardIssuerVisa;
    }

    public CardIssuer getCardIssuerMaster() {
        return cardIssuerMaster;
    }

    public void setCardIssuerMaster(CardIssuer cardIssuerMaster) {
        this.cardIssuerMaster = cardIssuerMaster;
    }

}
