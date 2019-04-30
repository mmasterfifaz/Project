package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ModelTypeDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.ModelType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ModelTypeController implements Serializable {

    private static Logger log = Logger.getLogger(BrandController.class);
    private static String REFRESH = "modeltype.xhtml?faces-redirect=true";
    private static String EDIT = "modeltypeedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ModelType> modelTypeList;
    private ModelType modelType;
    @ManagedProperty(value = "#{modelTypeDAO}")
    private ModelTypeDAO modelTypeDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:modeltype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        modelTypeList = modelTypeDAO.findModelTypeEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:modeltype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:modeltype:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    modelType = modelTypeDAO.findModelType(item.getKey());
                    modelType.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    modelType.setUpdateDate(new Date());
                    modelType.setEnable(false);
                    modelTypeDAO.edit(modelType);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        modelTypeList = modelTypeDAO.findModelTypeEntities();
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public ModelTypeDAO getModelTypeDAO() {
        return modelTypeDAO;
    }

    public void setModelTypeDAO(ModelTypeDAO modelTypeDAO) {
        this.modelTypeDAO = modelTypeDAO;
    }

    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }

    public void setModelTypeList(List<ModelType> modelTypeList) {
        this.modelTypeList = modelTypeList;
    }
}
