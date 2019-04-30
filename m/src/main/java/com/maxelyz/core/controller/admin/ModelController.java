package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import com.maxelyz.core.model.dao.ModelDAO;
import com.maxelyz.core.model.entity.Brand;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Model;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class ModelController implements Serializable {

    private static Logger log = Logger.getLogger(ModelController.class);
    private static String REFRESH = "model.xhtml?faces-redirect=true";
    private static String EDIT = "modeledit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Model> modelList;
    private Model model;
    private Integer brandId;
    private String txtSearch;
    private List<Brand> brandList;
    
    @ManagedProperty(value = "#{modelDAO}")
    private ModelDAO modelDAO;
    @ManagedProperty(value = "#{brandDAO}")
    private BrandDAO brandDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:model:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        brandList = brandDAO.findAllBrand();
        modelList = modelDAO.findModelEntities();
        
    }
    
    public void searchListener(ActionEvent e){
        modelList = modelDAO.findBy(txtSearch, brandId);
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:model:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:model:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    model = modelDAO.findModel(item.getKey());
                    model.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    model.setUpdateDate(new Date());
                    model.setEnable(false);
                    modelDAO.edit(model);
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ModelDAO getModelDAO() {
        return modelDAO;
    }

    public void setModelDAO(ModelDAO modelDAO) {
        this.modelDAO = modelDAO;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch = txtSearch;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public BrandDAO getBrandDAO() {
        return brandDAO;
    }

    public void setBrandDAO(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }
}
