package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ObjectionDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Objection;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ObjectionController implements Serializable{
    private static Logger log = Logger.getLogger(ObjectionController.class);
    private static String REFRESH = "objection.xhtml?faces-redirect=true";
    private static String EDIT = "objectionedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Objection> objections;
    private Objection objection;
    
    @ManagedProperty(value="#{objectionDAO}")
    private ObjectionDAO objectionDAO;

    @PostConstruct
    public void initialize() {        
        if (!SecurityUtil.isPermitted("admin:objection:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        ObjectionDAO dao = getObjectionDAO();
        objections = dao.findObjectionEntities();
        
    }
       
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:objection:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:objection:delete");
    }
    
    public List<Objection> getList() {
        return getObjections();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() {
        ObjectionDAO dao = getObjectionDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    objection = dao.findObjection(item.getKey());
                    objection.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    objection.setUpdateDate(new Date());
                    objection.setEnable(false);
                    dao.edit(objection);
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

    public List<Objection> getObjections() {
        return objections;
    }

    public void setObjections(List<Objection> objections) {
        this.objections = objections;
    }

    public ObjectionDAO getObjectionDAO() {
        return objectionDAO;
    }

    public void setObjectionDAO(ObjectionDAO objectionDAO) {
        this.objectionDAO = objectionDAO;
    }
}
