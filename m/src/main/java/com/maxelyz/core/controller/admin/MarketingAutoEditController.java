/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.FileTemplateDAO;
import com.maxelyz.core.model.dao.MarketingAutoDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProspectlistSponsorDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.entity.MarketingAuto;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author support
 */
//@RequestScoped
@ManagedBean
@ViewScoped
public class MarketingAutoEditController {

    private static Logger log = Logger.getLogger(MarketingAutoEditController.class);
    private static final String REDIRECT_PAGE = "marketingauto.jsf";
    private static final String SUCCESS = "marketingauto.xhtml?faces-redirect=true";
    //private static String FAILURE = "campaignchanneledit.xhtml";
    private MarketingAuto marketingAuto;
    private String mode;
    private String message;
    private String messageDup;

    private Integer prospectlistSponsorId;
    private List<ProspectlistSponsor> prospeclistPponsorList;
    private Integer fileTemplateId;

    private List<Integer> selectedUserGroup = new ArrayList<Integer>();
    private Map<String, Integer> userGroupList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedProduct = new ArrayList<Integer>();
    private Map<String, Integer> productList = new LinkedHashMap<String, Integer>();

    @ManagedProperty(value = "#{marketingAutoDAO}")
    private MarketingAutoDAO marketingAutoDAO;
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;

    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:marketingauto:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            marketingAuto = new MarketingAuto();
            marketingAuto.setCheckFormatTelNo(Boolean.FALSE);
        } else {
            mode = "edit";
            MarketingAutoDAO dao = getMarketingAutoDAO();
            marketingAuto = dao.findMarketingAutoId(new Integer(selectedID));

            if (marketingAuto.getProspectlistSponsorId() != null) {
                prospectlistSponsorId = marketingAuto.getProspectlistSponsorId().getId();
            }

            if (marketingAuto.getFileTemplateId() != null) {
                fileTemplateId = marketingAuto.getFileTemplateId().getId();
            }

            for (String uid : this.showUserManager()) {
                int uid_2 = Integer.parseInt(uid);
                this.selectedUserGroup.add(uid_2);
            }

            for (String pid : this.showProduct()) {
                int pid_2 = Integer.parseInt(pid);
                this.selectedProduct.add(pid_2);
            }

//            for (UserGroup userGroup : marketingAuto.getUserGroupCollection()) {
//                this.selectedUserGroup.add(userGroup.getId());
//            }
//            for (Product product : marketingAuto.getProductCollection()) {
//                this.selectedProduct.add(product.getId());
//            }
        }

        prospeclistPponsorList = prospectlistSponsorDAO.findProspectListSponsorGetDropdown();
        setUserGroupList(userGroupDAO.findUserGroupBy("", 0, "CampaignManager", "enable"));
        
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:marketingauto:add");
        } else {
            return SecurityUtil.isPermitted("admin:marketingauto:edit");
        }
    }

    public String saveAction() {
        messageDup = "";
        if (checkCode(marketingAuto)) {
            MarketingAutoDAO dao = getMarketingAutoDAO();
            try {

//              marketingAuto.setUserGroupCollection(this.getSelectedUserGroupCollection());
//              marketingAuto.setProductCollection(this.getSelectedProductCollection());
                marketingAuto.setUserGroups(totalManager());
                marketingAuto.setProducts(totalProduct());

                if (prospectlistSponsorId != null && prospectlistSponsorId != 0) {
                    marketingAuto.setProspectlistSponsorId(new ProspectlistSponsor(prospectlistSponsorId));
                } else {
                    marketingAuto.setProspectlistSponsorId(null);
                }

                if (fileTemplateId != null && fileTemplateId != 0) {
                    marketingAuto.setFileTemplateId(new FileTemplate(fileTemplateId));
                } else {
                    marketingAuto.setFileTemplateId(null);
                }

                if (getMode().equals("add")) {

                    if (marketingAuto.getPeriodCustomer() != 0 && marketingAuto.getPeriodCustomer() != null) {
                        marketingAuto.setDupCustomer(true);
                    } else {
                        marketingAuto.setDupCustomer(false);
                    }

                    if (marketingAuto.getPeriodBlacklist() != 0 && marketingAuto.getPeriodBlacklist() != null) {
                        marketingAuto.setDupOpout(true);
                    } else {
                        marketingAuto.setDupOpout(false);
                    }

                    if (marketingAuto.getPeriodYessale() != 0 && marketingAuto.getPeriodYessale() != null) {
                        marketingAuto.setDupYes(true);
                    } else {
                        marketingAuto.setDupYes(false);
                    }

                    marketingAuto.setId(null);
                    marketingAuto.setCreateDate(new Date());
                    marketingAuto.setCreateBy(JSFUtil.getUserSession().getUserName());
                    marketingAuto.setEnable(true);
                    marketingAuto.setAutoDial(false);
                    marketingAuto.setDupType("never");
                    marketingAuto.setDupWithin(true);
                    dao.create(marketingAuto);

                } else {

                    marketingAuto.setUpdateDate(new Date());
                    marketingAuto.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    dao.edit(marketingAuto);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;
        }
    }

    public List<String> showUserManager() {
        ArrayList<String> userArr = new ArrayList<String>();
        String[] userGroup = marketingAuto.getUserGroups().split(",");
        for (int i = 0; i < userGroup.length; i++) {
            userArr.add(userGroup[i]);
        }
        return userArr;
    }

    public List<String> showProduct() {
        ArrayList<String> productArr = new ArrayList<String>();
        String[] product = marketingAuto.getProducts().split(",");
        for (int i = 0; i < product.length; i++) {
            productArr.add(product[i]);
        }
        return productArr;
    }

    public String totalManager() {
        String total = "";
        for (int i = 0; i < selectedUserGroup.size(); i++) {
            total += selectedUserGroup.get(i);
            if (i < selectedUserGroup.size() - 1) {
                total += ",";
            }
        }
        return total;
    }

    public String totalProduct() {
        String total = "";
        for (int i = 0; i < selectedProduct.size(); i++) {
            total += selectedProduct.get(i);
            if (i < selectedProduct.size() - 1) {
                total += ",";
            }
        }
        return total;
    }

    public Boolean checkCode(MarketingAuto marketingAuto) {
        String prefixCode = marketingAuto.getPrefixCode();
        Integer id = 0;
        if (marketingAuto.getId() != null) {
            id = marketingAuto.getId();
        }

        Integer cnt = marketingAutoDAO.checkMarketingAutoPrefixCode(prefixCode, id);
        if (cnt == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public MarketingAuto getMarketingAuto() {
        return marketingAuto;
    }

    public void setMarketingAuto(MarketingAuto marketingAuto) {
        this.marketingAuto = marketingAuto;
    }

    public MarketingAutoDAO getMarketingAutoDAO() {
        return marketingAutoDAO;
    }

    public void setMarketingAutoDAO(MarketingAutoDAO marketingAutoDAO) {
        this.marketingAutoDAO = marketingAutoDAO;
    }

    public Integer getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(Integer fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }

    public Integer getProspectlistSponsorId() {
        return prospectlistSponsorId;
    }

    public void setProspectlistSponsorId(Integer prospectlistSponsorId) {
        this.prospectlistSponsorId = prospectlistSponsorId;
    }

    public List<ProspectlistSponsor> getProspeclistPponsorList() {
        return prospeclistPponsorList;
    }

    public void setProspeclistPponsorList(List<ProspectlistSponsor> prospeclistPponsorList) {
        this.prospeclistPponsorList = prospeclistPponsorList;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public List<Integer> getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(List<Integer> selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }

//    public List<UserGroup> getSelectedUserGroupCollection() {
//        List<UserGroup> userGroups = new ArrayList<UserGroup>();
//        for (int groupid : selectedUserGroup) {
//            UserGroup obj = new UserGroup();
//            obj.setId(groupid);
//            userGroups.add(obj);
//        }
//        return userGroups;
//    }
    
    public Map<String, Integer> getProductList() {
        ProductDAO dao = getProductDAO();
        List<Product> products = dao.findProductEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product : products) {
            if (product.getProductCategory().getCategoryType().equals("motor")) {
                values.put(product.getName() + "(" + product.getModelType().getName() + ", " + product.getModelFromYear() + "-" + product.getModelToYear() + ", " + product.getProductCategory().getName() + ")", product.getId());
            } else {
                values.put(product.getName(), product.getId());
            }
        }

        for (Integer pid : selectedProduct) {
            Boolean exist = false;
            for (Map.Entry<String, Integer> item : values.entrySet()) {
                if (pid.intValue() == item.getValue()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                Product p = productDAO.findProduct(pid);
                values.put(p.getName(), p.getId());
            }
        }
        return values;
    }

    public void setProductList(Map<String, Integer> productList) {
        this.productList = productList;
    }

    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

//    public List<Product> getSelectedProductCollection() {
//        List<Product> products = new ArrayList<Product>();
//        for (int pid : selectedProduct) {
//            Product p = new Product();
//            p.setId(pid);
//            products.add(p);
//        }
//        return products;
//    }

    public void setUserGroupList(List<UserGroup> userGroups) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        this.userGroupList = values;
    }

    public Map<String, Integer> getUserGroupList() {
        return this.userGroupList;
    }
    
    public Map<String, Integer> getFileTemplates() {
        return this.getFileTemplateDAO().getFileTemplateMap();
    }
}
