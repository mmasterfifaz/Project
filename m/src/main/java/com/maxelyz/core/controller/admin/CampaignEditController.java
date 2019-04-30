package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CampaignChannelDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.ContactResultDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.ProductCategoryDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.CampaignChannel;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignEditController {

    private static Logger log = Logger.getLogger(CampaignEditController.class);
    private Campaign campaign;
    private String mode;
    private List<Integer> selectedProduct = new ArrayList<Integer>();
    private String productCategoryType = "";
    private Integer marketingInboundId;
    private String message = "";
    private String messageDup = "";
    private Map<String, String> productCategoryTypeList = new LinkedHashMap<String, String>();
    private Map<String, Integer> marketingList = new LinkedHashMap<String, Integer>();
    private static String SUCCESS = "campaign.xhtml?faces-redirect=true";
    private static String FAILURE = "campaignedit.xhtml";
    private List<ContactResult> contactResultList;
    private Integer maxCallContactResultId;
    private Integer maxCall2ContactResultId;
    
    private Integer campaignChannelId;
    private List<CampaignChannel> campaignChannelList;
    private Integer defaultManagerUserId;
    private List<Users> defaultManagerUsersList;  

    @ManagedProperty(value = "#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{campaignChannelDAO}")
    private CampaignChannelDAO campaignChannelDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaignmanage:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        message = "";
        messageDup = "";
        String selectedID = null;
        try {
            selectedID = JSFUtil.getRequestParameterMap("id");
        } catch (NullPointerException e) {

        }
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            campaign = new Campaign();
            campaign.setMaxCall(JSFUtil.getApplication().getMaxcall1());
            campaign.setMaxCall2(JSFUtil.getApplication().getMaxcall2());
            campaign.setEnable(Boolean.TRUE);
            campaign.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            CampaignDAO dao = getCampaignDAO();//new CampaignDAO();
            campaign = dao.findCampaign(new Integer(selectedID));

            if (campaign.getMaxCallContactResult() != null) {
                maxCallContactResultId = campaign.getMaxCallContactResult().getId();
            }

            if (campaign.getMaxCall2ContactResult() != null) {
                maxCall2ContactResultId = campaign.getMaxCall2ContactResult().getId();
            }

            if (campaign.getMarketingInbound() != null) {
                marketingInboundId = campaign.getMarketingInbound().getId();
            }
            
            if (campaign.getCampaignChannel() != null) {
                campaignChannelId = campaign.getCampaignChannel().getId();
            }
            
            if (campaign.getUsers() != null) {
                defaultManagerUserId = campaign.getUsers().getId();
            }

            for (Product product : campaign.getProductCollection()) {
                selectedProduct.add(product.getId());
            }
        }
        productCategoryTypeList = productCategoryDAO.getProductCateogryTypeList();
//      marketingList = marketingDAO.getMarketList();
        marketingList = getMarketingListFromFlag();
        campaignChannelList = campaignChannelDAO.findCampaignChannelDropDown();
        defaultManagerUsersList = usersDAO.findManagerUserByCampaginManage();
        contactResultList = contactResultDAO.findContactResultEntities();
    }

    private Map<String, Integer> getMarketingListFromFlag() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
            values = marketingDAO.getMarketList();
        } else {
            List<Marketing> marL = marketingDAO.findMarketingByUserGroup(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
            if (marL != null && marL.size() > 0) {
                for (Marketing obj : marL) {
                    values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
                }
            } else {
                values = new LinkedHashMap<String, Integer>();
            }
        }
        return values;
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:campaign:add");
        } else {
            return SecurityUtil.isPermitted("admin:campaign:edit");
        }
    }

    public void clearMarketingInboundListener() {
        marketingInboundId = 0;
    }
    
    public String saveAction() {
        message = "";
        messageDup = "";
        String loginName = JSFUtil.getUserSession().getUsers().getLoginName();
        if (checkCode(campaign)) {
            if (campaign.getStartDate().before(campaign.getEndDate())) {
                CampaignDAO dao = getCampaignDAO();//new CampaignDAO();
                try {
                    campaign.setProductCollection(this.getSelectedProductCollection());

                    if(!campaign.getInbound()) {
                        campaign.setMarketingInbound(null);
                        campaign.setAssignmentInbound(null);
                    } else {
                        if (marketingInboundId != null && marketingInboundId != 0) {
                            campaign.setMarketingInbound(new Marketing(marketingInboundId));                        
                            campaign.setAssignmentInbound(null);
                        }
                    }

                    if (maxCallContactResultId != null && maxCallContactResultId != 0) {
                        campaign.setMaxCallContactResult(new ContactResult(maxCallContactResultId));
                    } else {
                        campaign.setMaxCallContactResult(null);
                    }

                    if (maxCall2ContactResultId != null && maxCall2ContactResultId != 0) {
                        campaign.setMaxCall2ContactResult(new ContactResult(maxCall2ContactResultId));
                    } else {
                        campaign.setMaxCall2ContactResult(null);
                    }
                    
                    if(campaignChannelId !=null && campaignChannelId !=0){
                        campaign.setCampaignChannel(new CampaignChannel(campaignChannelId));
                    } else {
                        campaign.setCampaignChannel(null);
                    }
                    
                    if(defaultManagerUserId !=null && defaultManagerUserId !=0){
                        campaign.setUsers(new Users(defaultManagerUserId));
                    } else {
                        campaign.setUsers(null);
                    }
                    
                    if (getMode().equals("add")) {
                        campaign.setId(null);
                        campaign.setEnable(true);
                        campaign.setCreateBy(loginName);
                        campaign.setCreateDate(new Date());

                        dao.create(campaign);
                    } else {
                        campaign.setUpdateBy(loginName);
                        campaign.setUpdateDate(new Date());
                        dao.edit(campaign);
                    }
                } catch (Exception e) {
                    log.error(e);
                    return null;
                }
                return SUCCESS;
            } else {
                message = " Effective Date From over Date To";
                return null;
            }
        } else {
            messageDup = " Code is already taken";
            return null;
        }
    }

    public Boolean checkCode(Campaign campaign) {
        String code = campaign.getCode();
        Integer id = 0;
        if (campaign.getId() != null) {
            id = campaign.getId();
        }
        CampaignDAO dao = getCampaignDAO();

        Integer cnt = dao.checkCampaignCode(code, id);
        if (cnt == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setProductSponsor(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<Integer, Integer> getMaxCallList() {
        Map<Integer, Integer> values = new LinkedHashMap<Integer, Integer>();
        for (int i = 1; i <= 100; i++) {
            values.put(i, i);
        }
        return values;
    }

    public Map<String, Integer> getProductList() {
        ProductDAO dao = getProductDAO();
        List<Product> products = dao.findProductEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product : products) {
            if (product.getProductCategory().getCategoryType().equals(productCategoryType)) {
                if (product.getProductCategory().getCategoryType().equals("motor")) {
                    values.put(product.getName() + "(" + product.getModelType().getName() + ", " + product.getModelFromYear() + "-" + product.getModelToYear() + ", " + product.getProductCategory().getName() + ")", product.getId());
                } else {
                    values.put(product.getName(), product.getId());
                }
            } else if (productCategoryType.equals("")) {
                if (product.getProductCategory().getCategoryType().equals("motor")) {
                    values.put(product.getName() + "(" + product.getModelType().getName() + ", " + product.getModelFromYear() + "-" + product.getModelToYear() + ", " + product.getProductCategory().getName() + ")", product.getId());
                } else {
                    values.put(product.getName(), product.getId());
                }
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

    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Product> getSelectedProductCollection() {
        List<Product> products = new ArrayList<Product>();
        for (int pid : selectedProduct) {
            Product p = new Product();
            p.setId(pid);
            products.add(p);
        }
        return products;
    }

    public void productCategoryTypeValueChange(ActionEvent event) {
        //campaign.setProductCategoryType(JSFUtil.getRequestParameterMap("paramProductCategoryType"));        
    }

    public void pickListValueChange() {
        List<Integer> list = selectedProduct;
    }

    public Integer getMarketingInboundId() {
        return marketingInboundId;
    }

    public void setMarketingInboundId(Integer marketingInboundId) {
        this.marketingInboundId = marketingInboundId;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public Map<String, Integer> getMarketingList() {
        return marketingList;
    }

    public void setMarketingList(Map<String, Integer> marketingList) {
        this.marketingList = marketingList;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Map<String, String> getProductCategoryTypeList() {
        return productCategoryTypeList;
    }

    public void setProductCategoryTypeList(Map<String, String> productCategoryTypeList) {
        this.productCategoryTypeList = productCategoryTypeList;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public String getProductCategoryType() {
        return productCategoryType;
    }

    public void setProductCategoryType(String productCategoryType) {
        this.productCategoryType = productCategoryType;
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

    public List<ContactResult> getContactResultList() {
        return contactResultList;
    }

    public Integer getMaxCallContactResultId() {
        return maxCallContactResultId;
    }

    public void setMaxCallContactResultId(Integer maxCallContactResultId) {
        this.maxCallContactResultId = maxCallContactResultId;
    }

    public Integer getMaxCall2ContactResultId() {
        return maxCall2ContactResultId;
    }

    public void setMaxCall2ContactResultId(Integer maxCall2ContactResultId) {
        this.maxCall2ContactResultId = maxCall2ContactResultId;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public Integer getCampaignChannelId() {
        return campaignChannelId;
    }

    public void setCampaignChannelId(Integer campaignChannelId) {
        this.campaignChannelId = campaignChannelId;
    }

    public List<CampaignChannel> getCampaignChannelList() {
        return campaignChannelList;
    }

    public void setCampaignChannelList(List<CampaignChannel> campaignChannelList) {
        this.campaignChannelList = campaignChannelList;
    }

    public CampaignChannelDAO getCampaignChannelDAO() {
        return campaignChannelDAO;
    }

    public void setCampaignChannelDAO(CampaignChannelDAO campaignChannelDAO) {
        this.campaignChannelDAO = campaignChannelDAO;
    }

    public Integer getDefaultManagerUserId() {
        return defaultManagerUserId;
    }

    public void setDefaultManagerUserId(Integer defaultManagerUserId) {
        this.defaultManagerUserId = defaultManagerUserId;
    }

    public List<Users> getDefaultManagerUsersList() {
        return defaultManagerUsersList;
    }

    public void setDefaultManagerUsersList(List<Users> defaultManagerUsersList) {
        this.defaultManagerUsersList = defaultManagerUsersList;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    


    
    
    
    
    
    
    
    
    
    
    

}
