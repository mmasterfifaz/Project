package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CallsuccessReasonDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CallsuccessReason;
import com.maxelyz.utils.JSFUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.*;
import javax.transaction.UserTransaction;

@ManagedBean
@RequestScoped
public class CallSuccessController implements Serializable{
    private static Logger log = Logger.getLogger(CallSuccessController.class);
    private static String REFRESH = "callsuccess.xhtml?faces-redirect=true";
    private static String EDIT = "callsuccessedit.xhtml";
    private String message="";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<CallsuccessReason> callsuccessReasons;
    private String keyword;
    private CallsuccessReason callsuccessReason;

    @ManagedProperty(value="#{callsuccessReasonDAO}")
    private CallsuccessReasonDAO callsuccessReasonDAO;

    @PostConstruct
    public void initialize() {
        CallsuccessReasonDAO dao = getCallsuccessReasonDAO();
        callsuccessReasons = dao.findCallsuccessReasonEntities();
    }

    public List<CallsuccessReason> getList() {
        return getCallsuccessReasons();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() {
        CallsuccessReasonDAO dao = getCallsuccessReasonDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    callsuccessReason = dao.findCallsuccessReason(item.getKey());
                    callsuccessReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    callsuccessReason.setUpdateDate(new Date());
                    callsuccessReason.setEnable(false);
                    dao.edit(callsuccessReason);
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

    public List<CallsuccessReason> getCallsuccessReasons() {
        return callsuccessReasons;
    }

    public void setCallsuccessReasons(List<CallsuccessReason> callsuccessReasons) {
        this.callsuccessReasons = callsuccessReasons;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public CallsuccessReasonDAO getCallsuccessReasonDAO() {
        return callsuccessReasonDAO;
    }

    public void setCallsuccessReasonDAO(CallsuccessReasonDAO callsuccessReasonDAO) {
        this.callsuccessReasonDAO = callsuccessReasonDAO;
    }


}
