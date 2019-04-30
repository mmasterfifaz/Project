package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.PaymentMethodDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.PaymentMethod;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class PaymentMethodEditController {
    private static Logger log = Logger.getLogger(PaymentMethodEditController.class);
    private static String REDIRECT_PAGE = "paymentmethod.jsf";
    private static String SUCCESS = "paymentmethod.xhtml?faces-redirect=true";
    private static String FAILURE = "paymentmethodedit.xhtml";

    private PaymentMethod paymentMethod;
    private String mode;
    private String message;
    private String messageDup;
    
    private String paymentType;

    @ManagedProperty(value="#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:paymentmethod:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            paymentMethod = new PaymentMethod();
            paymentMethod.setEnable(Boolean.TRUE);
            paymentMethod.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                paymentMethod = paymentMethodDAO.findPaymentMethod(new Integer(selectedID));
                if(paymentMethod.isCreditcard()== true && paymentMethod.isDebitcard()==false)
                {
                    paymentType = "credit";
                }
                else if(paymentMethod.isCreditcard()== false && paymentMethod.isDebitcard()==true)
                {
                    paymentType = "debit";
                }
                else
                {
                    paymentType = "N/A";
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if (paymentMethod == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:paymentmethod:add");
        } else {
            return SecurityUtil.isPermitted("admin:paymentmethod:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(paymentMethod)) {
            try {
                if (getMode().equals("add")) {
                    paymentMethod.setId(null);
                    paymentMethod.setEnable(true);
                    paymentMethod.setCreateDate(new Date());
                    paymentMethod.setCreateBy(JSFUtil.getUserSession().getUserName());
                    if(paymentType.equals("N/A"))
                    {
                        paymentMethod.setCreditcard(false);
                        paymentMethod.setDebitcard(false);
                    }
                    else if(paymentType.equals("credit"))
                    {
                        paymentMethod.setCreditcard(true);
                        paymentMethod.setDebitcard(false);
                    }
                    else
                    {
                        paymentMethod.setCreditcard(false);
                        paymentMethod.setDebitcard(true);
                    }
                    paymentMethodDAO.create(paymentMethod);
                } else {
                    paymentMethod.setUpdateDate(new Date());
                    paymentMethod.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    if(paymentType.equals("N/A"))
                    {
                        paymentMethod.setCreditcard(false);
                        paymentMethod.setDebitcard(false);
                    }
                    else if(paymentType.equals("credit"))
                    {
                        paymentMethod.setCreditcard(true);
                        paymentMethod.setDebitcard(false);
                    }
                    else
                    {
                        paymentMethod.setCreditcard(false);
                        paymentMethod.setDebitcard(true);
                    }
                    paymentMethodDAO.edit(paymentMethod);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkCode(PaymentMethod paymentMethod) {
        String code = paymentMethod.getCode();
        Integer id=0; 
        if(paymentMethod.getId() != null)
            id = paymentMethod.getId();
        PaymentMethodDAO dao = getPaymentMethodDAO();
        
        Integer cnt = dao.checkPaymentMethodCode(code, id);
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    
    

}
