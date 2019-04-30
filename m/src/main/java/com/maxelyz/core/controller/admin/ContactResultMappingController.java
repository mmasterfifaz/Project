package com.maxelyz.core.controller.admin;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.maxelyz.core.model.dao.ContactResultMappingDAO;
import com.maxelyz.core.model.dao.FollowupsaleReasonDAO;
import com.maxelyz.core.model.entity.ContactResultMapping;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

/**
 * Created by support on 12/20/2017.
 */
@ManagedBean
@ViewScoped
public class ContactResultMappingController implements Serializable {
    private static Logger log = Logger.getLogger(ContactResultMappingController.class);
    private static String REFRESH = "contactresultmapping.xhtml?faces-redirect=true";
    private static String EDIT = "contactresultmappingedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ContactResultMapping> contactResultMappingList;
    private ContactResultMapping contactResultMapping;
    @ManagedProperty(value = "#{contactResultMappingDAO}")
    private ContactResultMappingDAO contactResultMappingDAO;

    @PostConstruct
    public void initialize() {
    if (!SecurityUtil.isPermitted("admin:contactresultmapping:view")) {
        SecurityUtil.redirectUnauthorize();
    }
        ContactResultMappingDAO dao = getContactResultMappingDAO();
        contactResultMappingList = dao.findContactResultMappingEntities();
    }

    public List<ContactResultMapping> getList(){
        return getContactResultMappingList();
    }

    public String deleteAction() throws Exception {
        ContactResultMappingDAO dao = getContactResultMappingDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    contactResultMapping = dao.findContactResultMapping(item.getKey());
                    contactResultMapping.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    contactResultMapping.setUpdateDate(new Date());
                    contactResultMapping.setEnable(false);
                    dao.edit(contactResultMapping);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:contactresultmapping:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:contactresultmapping:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ContactResultMapping> getContactResultMappingList() {
        return contactResultMappingList;
    }

    public void setContactResultMappingList(List<ContactResultMapping> contactResultMappingList) {
        this.contactResultMappingList = contactResultMappingList;
    }

    public ContactResultMapping getContactResultMapping() {
        return contactResultMapping;
    }

    public void setContactResultMapping(ContactResultMapping contactResultMapping) {
        this.contactResultMapping = contactResultMapping;
    }

    public ContactResultMappingDAO getContactResultMappingDAO() {
        return contactResultMappingDAO;
    }

    public void setContactResultMappingDAO(ContactResultMappingDAO contactResultMappingDAO) {
        this.contactResultMappingDAO = contactResultMappingDAO;
    }
}
