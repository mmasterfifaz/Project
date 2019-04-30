/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class PopupSelectProduct {
    private static Logger log = Logger.getLogger(PopupSelectProduct.class);
    
    private Campaign campaign;
    private Integer campaignId = 0;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedIds1 = new ConcurrentHashMap<Integer, Boolean>();
    private Boolean checkProductCategoryAll;
    private Boolean checkProductPlanCategoryAll;
    private Brand brand;
    private Integer brandId;
    private Model model;
    private Integer modelId;
    private ModelType modelType;   
    private Integer modelTypeId;
    private Integer modelYear;
    private List<Brand> brandList;
    private List<Model> modelList = new ArrayList<Model>();
    private List<ModelType> modelTypeList = new ArrayList<ModelType>();
    private SelectItem[] modelYearList;
    private List<Object> productCategoryList;
    private List<ProductPlanCategory> productPlanCategoryList; 
    private List<Integer> productCategoryIdList = new ArrayList<Integer>();
    private List<Integer> productPlanCategoryIdList = new ArrayList<Integer>();
    private List<ProductPlan> dataList;
    private String defaultCategoryType = "motor";
    private AssignmentDetail assignmentDetail;
    
    private int page = 1;
    private Integer rows = 0;
    private Integer sumInsuredMin = 0;
    private Integer sumInsuredMax = 0;
    private String claimType = "";
    
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;    
    @ManagedProperty(value="#{brandDAO}")
    private BrandDAO brandDAO;    
    @ManagedProperty(value="#{modelDAO}")
    private ModelDAO modelDAO;    
    @ManagedProperty(value="#{modelTypeDAO}")
    private ModelTypeDAO modelTypeDAO;
    @ManagedProperty(value="#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value="#{productPlanCategoryDAO}")
    private ProductPlanCategoryDAO productPlanCategoryDAO;
    @ManagedProperty(value="#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;
    
    @PostConstruct
    public void initialize() {
        initAssignmentDetail(); 
        if (JSFUtil.getUserSession().getCustomerDetail() != null) {
            this.initCampaign();
        }
        productCategoryList = productCategoryDAO.getProductCateogryListBy(JSFUtil.getUserSession().getUsers().getId(), JSFUtil.getUserSession().getCustomerDetail().getId(), campaignId, defaultCategoryType);
        productPlanCategoryList = productPlanCategoryDAO.findProductPlanCategoryEntities();
        modelTypeList = modelTypeDAO.findModelTypeEntities();   
    }

    private void initAssignmentDetail() {
        if(JSFUtil.getUserSession().getAssignmentDetail() != null){
            assignmentDetail = JSFUtil.getUserSession().getAssignmentDetail();
        }else{
            Integer assignmentDetailId = 0;
            if (StringUtils.isNotEmpty(JSFUtil.getRequestParameterMap("selectedId")) && !StringUtils.equals(JSFUtil.getRequestParameterMap("selectedId"), "0")) {
                assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
            } else if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
                assignmentDetailId = JSFUtil.getUserSession().getAssignmentDetail().getId();
            } else {
                assignmentDetailId = 0;
            }

            if (assignmentDetailId != 0 && assignmentDetailId != null) {
                assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
                JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
            } else {
                assignmentDetail = null;
            }
        }
    }

    private void initCampaign() {
        if (JSFUtil.getUserSession().getCustomerDetail() != null) {
            if (JSFUtil.getUserSession().getCustomerDetail().getId() != null && JSFUtil.getUserSession().getCustomerDetail().getId() != 0) {
                campaign = assignmentDetail.getAssignmentId().getCampaign();
                campaignId = assignmentDetail.getAssignmentId().getCampaign().getId();
            }
        }
    }
    
    public void checkProductCategoryAllListener(ActionEvent event){
        checkProductCategoryAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkProductCategoryAll"));
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                item.setValue(checkProductCategoryAll);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void checkProductPlanCategoryAllListener(ActionEvent event){
        checkProductPlanCategoryAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkProductPlanCategoryAll"));
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds1.entrySet()) {
                item.setValue(checkProductPlanCategoryAll);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void modelTypeChangeListener(ValueChangeEvent event){
        brandId = 0;
        modelId = 0;
        modelYear = 0;
        modelTypeId = (Integer) event.getNewValue();
        findBrandList(modelTypeId);
    }
    
    private void findBrandList(Integer id){
        brandList = brandDAO.findByModelType(id);
        modelList = null;
    }
    
    public void searchActionListener(ActionEvent event){
        page = 1;
        getProductCategoryIdList();
        getProductPlanCategoryIdList();
        
        search();
    }
    
    private void search(){    
        int itemPerPage = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((page - 1) * itemPerPage);
        List<ProductPlan> list = null;
        if(campaign.getInbound()){
            list = productDAO.findProductByInbound(defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, firstResult, itemPerPage);
            rows = productDAO.countProductByInbound(defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType);
        }else{
            list = productDAO.findProductBy(JSFUtil.getUserSession().getUsers().getId(), JSFUtil.getUserSession().getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, firstResult, itemPerPage);
            rows = productDAO.countProductBy(JSFUtil.getUserSession().getUsers().getId(), JSFUtil.getUserSession().getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType);
        }
        dataList = list != null ? new PagedList(list, rows, itemPerPage) : null;
    }

    public List<Integer> getProductCategoryIdList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
            if (item.getValue()) {
                list.add(item.getKey());
            }
        }
        productCategoryIdList = list;
        return productCategoryIdList;
    }

    public List<Integer> getProductPlanCategoryIdList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Map.Entry<Integer, Boolean> item : selectedIds1.entrySet()) {
            if (item.getValue()) {
                list.add(item.getKey());
            }
        }
        productPlanCategoryIdList = list;
        return productPlanCategoryIdList;
    }
    
    public void brandChangeListener(ValueChangeEvent event){
        modelId = 0;
        modelYear = 0;
        brandId = (Integer) event.getNewValue();
        findModelList(modelTypeId, brandId);
    }
    
    private void findModelList(Integer modelTypeId, Integer brandId){
        modelList = modelDAO.findModelByModelTypeBrand(modelTypeId, brandId);
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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Boolean getCheckProductCategoryAll() {
        return checkProductCategoryAll;
    }

    public void setCheckProductCategoryAll(Boolean checkProductCategoryAll) {
        this.checkProductCategoryAll = checkProductCategoryAll;
    }

    public Boolean getCheckProductPlanCategoryAll() {
        return checkProductPlanCategoryAll;
    }

    public void setCheckProductPlanCategoryAll(Boolean checkProductPlanCategoryAll) {
        this.checkProductPlanCategoryAll = checkProductPlanCategoryAll;
    }

    public List<ProductPlan> getDataList() {
        return dataList;
    }

    public void setDataList(List<ProductPlan> dataList) {
        this.dataList = dataList;
    }

    public String getDefaultCategoryType() {
        return defaultCategoryType;
    }

    public void setDefaultCategoryType(String defaultCategoryType) {
        this.defaultCategoryType = defaultCategoryType;
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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
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

    public Integer getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Integer modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }

    public void setModelTypeList(List<ModelType> modelTypeList) {
        this.modelTypeList = modelTypeList;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public SelectItem[] getModelYearList() {        
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy",Locale.US);
        int year = Integer.parseInt(simpleDateformat.format(new Date()));
        modelYearList = new SelectItem[20];
        for(int i = 0 ; i < 20 ; i++){
            Integer y = year - i;
            modelYearList[i] = new SelectItem(y, y.toString());
        }
        return modelYearList;
    }

    public void setModelYearList(SelectItem[] modelYearList) {
        this.modelYearList = modelYearList;
    }

    public List<Object> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<Object> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<ProductPlanCategory> getProductPlanCategoryList() {
        return productPlanCategoryList;
    }

    public void setProductPlanCategoryList(List<ProductPlanCategory> productPlanCategoryList) {
        this.productPlanCategoryList = productPlanCategoryList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Map<Integer, Boolean> getSelectedIds1() {
        return selectedIds1;
    }

    public void setSelectedIds1(Map<Integer, Boolean> selectedIds1) {
        this.selectedIds1 = selectedIds1;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public ProductPlanCategoryDAO getProductPlanCategoryDAO() {
        return productPlanCategoryDAO;
    }

    public void setProductPlanCategoryDAO(ProductPlanCategoryDAO productPlanCategoryDAO) {
        this.productPlanCategoryDAO = productPlanCategoryDAO;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.search();
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getSumInsuredMax() {
        return sumInsuredMax;
    }

    public void setSumInsuredMax(Integer sumInsuredMax) {
        this.sumInsuredMax = sumInsuredMax;
    }

    public Integer getSumInsuredMin() {
        return sumInsuredMin;
    }

    public void setSumInsuredMin(Integer sumInsuredMin) {
        this.sumInsuredMin = sumInsuredMin;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
        
}
