package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.PaymentMethodDAO;
import com.maxelyz.core.model.entity.PaymentMethod;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class PaymentMethodController {

    private static Logger log = Logger.getLogger(PaymentMethodController.class);
    private static String REFRESH = "paymentmethod.xhtml?faces-redirect=true";
    private static String EDIT = "paymentmethodedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<PaymentMethod> list;
    private PaymentMethod paymentMethod;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:paymentmethod:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        list = paymentMethodDAO.findPaymentMethodEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:paymentmethod:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:paymentmethod:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    paymentMethod = paymentMethodDAO.findPaymentMethod(item.getKey());
                    paymentMethod.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    paymentMethod.setUpdateDate(new Date());
                    paymentMethod.setEnable(false);
                    paymentMethodDAO.edit(paymentMethod);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<PaymentMethod> getList() {
        return list;
    }

    public void setList(List<PaymentMethod> list) {
        this.list = list;
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
}
