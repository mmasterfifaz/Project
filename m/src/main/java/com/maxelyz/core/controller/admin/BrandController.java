package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class BrandController {

    private static Logger log = Logger.getLogger(BrandController.class);
    private static String REFRESH = "brand.xhtml?faces-redirect=true";
    private static String EDIT = "brandedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Brand> brandList;
    private Brand brand;
    @ManagedProperty(value = "#{brandDAO}")
    private BrandDAO brandDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:brand:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        brandList = brandDAO.findBrandEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:brand:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:brand:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    brand = brandDAO.findBrand(item.getKey());
                    brand.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    brand.setUpdateDate(new Date());
                    brand.setEnable(false);
                    brandDAO.edit(brand);
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public BrandDAO getBrandDAO() {
        return brandDAO;
    }

    public void setBrandDAO(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}
