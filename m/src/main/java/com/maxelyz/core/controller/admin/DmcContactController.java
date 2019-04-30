package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.DmccontactReasonDAO;
import com.maxelyz.core.model.entity.DmccontactReason;
import com.maxelyz.utils.JSFUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class DmcContactController implements Serializable{
    private static Logger log = Logger.getLogger(DmcContactController.class);
    private static String REFRESH = "dmccontact.xhtml?faces-redirect=true";
    private static String EDIT = "dmccontactedit.xhtml";
    private String message="";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<DmccontactReason> dmccontactReasons;
    private String keyword;
    private DmccontactReason dmccontactReason;

    @ManagedProperty(value="#{dmccontactReasonDAO}")
    private DmccontactReasonDAO dmccontactReasonDAO;

    @PostConstruct
    public void initialize() {
        DmccontactReasonDAO dao = getDmccontactReasonDAO();
        dmccontactReasons = dao.findDmccontactReasonEntities();
    }

    public List<DmccontactReason> getList() {
        return getDmccontactReasons();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        DmccontactReasonDAO dao = getDmccontactReasonDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    dmccontactReason = dao.findDmccontactReason(item.getKey());
                    dmccontactReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    dmccontactReason.setUpdateDate(new Date());
                    dmccontactReason.setEnable(false);
                    dao.edit(dmccontactReason);
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

    public List<DmccontactReason> getDmccontactReasons() {
        return dmccontactReasons;
    }

    public void setDmccontactReasons(List<DmccontactReason> dmccontactReasons) {
        this.dmccontactReasons = dmccontactReasons;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public DmccontactReasonDAO getDmccontactReasonDAO() {
        return dmccontactReasonDAO;
    }

    public void setDmccontactReasonDAO(DmccontactReasonDAO dmccontactReasonDAO) {
        this.dmccontactReasonDAO = dmccontactReasonDAO;
    }


}
