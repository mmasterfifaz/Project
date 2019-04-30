package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.entity.Objection;
import com.maxelyz.core.model.dao.ObjectionDAO;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class ObjectionPopupController implements Serializable {
    private static Logger log = Logger.getLogger(ObjectionPopupController.class);
    private static String REFRESH = "objectionpopup.xhtml";
    private Map<Long, Boolean> selectedIds = new ConcurrentHashMap<Long, Boolean>(); //use for checkbox
    private List<Objection> objectionList;
    private Objection objection;
    private String keyword="";
    private String selectedID;

    @ManagedProperty(value = "#{objectionDAO}")
    private ObjectionDAO objectionDAO;

    @PostConstruct
    public void initialize() {
        selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            objectionList = this.getObjectionDAO().findObjectionEntities();
        } else {
            objectionList = this.getObjectionDAO().findObjectionEntities();
            objection = this.getObjectionDAO().findObjection(new Integer(selectedID));
        }
    }

    public List<Objection> getList() {
        return getObjections();
    }

    public String searchAction() {
        //log.error(keyword);
        return REFRESH;
    }


    public void searchListener() {
        objectionList = this.getObjectionDAO().findObjectionByName(keyword);
        if (!objectionList.isEmpty()) {
            selectedID = "0";
        }
    }

    public String showAction() {
        return REFRESH;
    }

    public Map<Long, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Long, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Objection> getObjections() {
        return objectionList;
    }

    public void setObjection(Objection objections) {
        this.objection = objections;
    }

    public Objection getObjection() {
        return objection;
    }

    public void setObjections(List<Objection> objections) {
        this.objectionList = objections;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

  
    //Managed Properties
    public ObjectionDAO getObjectionDAO() {
        return objectionDAO;
    }

    public void setObjectionDAO(ObjectionDAO objectionDAO) {
        this.objectionDAO = objectionDAO;
    }
}
