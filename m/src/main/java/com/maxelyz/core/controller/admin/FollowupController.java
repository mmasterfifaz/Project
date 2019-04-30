package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.FollowupsaleReasonDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class FollowupController implements Serializable {

    private static Logger log = Logger.getLogger(FollowupsaleReason.class);
    private static String REFRESH = "followup.xhtml?faces-redirect=true";
    private static String EDIT = "followupedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<FollowupsaleReason> followups;
    private FollowupsaleReason followup;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;

    @PostConstruct
    public void initialize() {

    if (!SecurityUtil.isPermitted("admin:followup:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        FollowupsaleReasonDAO dao = getFollowupsaleReasonDAO();
        followups = dao.findFollowupsaleReasonEntities();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:followup:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:followup:delete");
    }
    
    public List<FollowupsaleReason> getList() {
        return getFollowups();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        FollowupsaleReasonDAO dao = getFollowupsaleReasonDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    followup = dao.findFollowupsaleReason(item.getKey());
                    followup.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    followup.setUpdateDate(new Date());
                    followup.setEnable(false);
                    dao.edit(followup);
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

    public List<FollowupsaleReason> getFollowups() {
        return followups;
    }

    public void setFollowups(List<FollowupsaleReason> followups) {
        this.followups = followups;
    }

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }
}
