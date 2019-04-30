package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.OpOutDAO;
import com.maxelyz.core.model.entity.OpOut;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class OpOutController implements Serializable{
    private static Logger log = Logger.getLogger(OpOutController.class);
    private static String REFRESH = "opout.xhtml";
    private static String EDIT = "opoutedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<OpOut> opOuts;
    private OpOut opOut;
    private String name = "";
    private String surname = "";
    private String telephone = "";
    
    @ManagedProperty(value="#{opOutDAO}")
    private OpOutDAO opOutDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:listexclusion:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        OpOutDAO dao = getOpOutDAO();
        opOuts = dao.findOpOutEntities();  
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:listexclusion:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:listexclusion:delete");
    }
    
    public void initListener(ActionEvent event) {
        initialize();
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        opOuts = opOutDAO.findOpOutBySearch(name, surname, telephone);
    }
    
    public List<OpOut> getList() {
        return getOpOuts();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
    
        OpOutDAO dao = getOpOutDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    dao.destroy(item.getKey());
               //     opOut.setEnable(false);
                    //dao.edit(opOut);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        initialize();
        return REFRESH;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<OpOut> getOpOuts() {
        return opOuts;
    }

    public void setOpOuts(List<OpOut> opOuts) {
        this.opOuts = opOuts;
    }

    public OpOutDAO getOpOutDAO() {
        return opOutDAO;
    }

    public void setOpOutDAO(OpOutDAO opOutDAO) {
        this.opOutDAO = opOutDAO;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
