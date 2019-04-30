package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProductSponsorDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ProductSponsor;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class ProductSponsorController implements Serializable {

    private static Logger log = Logger.getLogger(ProductSponsorController.class);
    private static String REFRESH = "productsponsor.xhtml?faces-redirect=true";
    private static String EDIT = "productsponsoredit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ProductSponsor> productSponsors;
    private String keyword;
    private ProductSponsor prod;
    @ManagedProperty(value = "#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productsource:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        ProductSponsorDAO dao = getProductSponsorDAO();
        productSponsors = dao.findProductSponsorEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:productsource:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:productsource:delete");
    }
    
    public List<ProductSponsor> getList() {
        return getProductSponsors();
    }

    public String searchAction() {
        ProductSponsorDAO dao = getProductSponsorDAO();
        productSponsors = dao.findProductSponsorByName(keyword);
        return REFRESH;
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        productSponsors = productSponsorDAO.findBy(keyword);
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        ProductSponsorDAO dao = getProductSponsorDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    prod = dao.findProductSponsor(item.getKey());
                    prod.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    prod.setUpdateDate(new Date());
                    prod.setEnable(false);
                    dao.edit(prod);
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

    public List<ProductSponsor> getProductSponsors() {
        return productSponsors;
    }

    public void setProductSponsors(List<ProductSponsor> productSponsors) {
        this.productSponsors = productSponsors;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ProductSponsorDAO getProductSponsorDAO() {
        return productSponsorDAO;
    }

    public void setProductSponsorDAO(ProductSponsorDAO productSponsorDAO) {
        this.productSponsorDAO = productSponsorDAO;
    }
}
