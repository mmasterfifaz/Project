/**
 *
 * @author Creatorz
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ContactResultPlanDAO;
import com.maxelyz.core.model.entity.ContactResultPlan;
import com.maxelyz.core.model.dao.ContactResultDAO;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ContactResultPlanController implements Serializable {

    private static Logger log = Logger.getLogger(ContactResultPlanController.class);
    private static String REFRESH = "contactresultplan.xhtml?faces-redirect=true";
    private static String EDIT = "contactresultplanedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ContactResultPlan> contactResultPlans;
    private List<ContactResult> contactResults;
    private List<Product> products;

    private Collection<ContactResultPlan> contactResultPlanCollection;
    private Collection<ContactResult> contactResultCollection;

    private ContactResultPlan contactResultPlan;
    private ContactResult contactResult;
    private Product product;

    private int productNameCount;
    private String messageDup;

    @ManagedProperty(value = "#{contactResultPlanDAO}")
    private ContactResultPlanDAO contactResultPlanDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:contactresultplan:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        contactResultPlans = getContactResultPlanDAO().findContactResultPlanEntities();
        contactResults = getContactResultDAO().findContactResultEntities();

    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:contactresultplan:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:contactresultplan:delete");
    }

    public List<ContactResult> getContactResultList() {
        return contactResults;
    }

    public List<ContactResultPlan> getContactResultPlanList() {
        return contactResultPlans;
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        ContactResultPlanDAO dao = contactResultPlanDAO;
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    contactResultPlan = dao.findContactResultPlan(item.getKey());
                    if(!contactResultPlan.getIsDefault())
                    {
                        contactResultPlan.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                        contactResultPlan.setUpdateDate(new Date());
                        contactResultPlan.setEnable(false);
                        dao.edit(contactResultPlan);
                    }
                    else
                    {
                        messageDup="Cannot delete Default Plan";
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public void linkProductAction() {
        String contactID = (String) JSFUtil.getRequestParameterMap("id");
        products = getProductDAO().linkProduct(new Integer(contactID));
    }

    public int countProductName(int contactResultPlanID) {
        productNameCount = getProductDAO().productNameCount(contactResultPlanID);

        if (productNameCount > 0) {
            return productNameCount;
        } else {
            return 0;
        }
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ContactResultPlan> getContactResultPlans() {
        return contactResultPlans;
    }

    public void setContactResultPlans(List<ContactResultPlan> contactResultPlans) {
        this.contactResultPlans = contactResultPlans;
    }

    public List<ContactResult> getContactResults() {
        return contactResults;
    }

    public void setContactResults(List<ContactResult> contactResults) {
        this.contactResults = contactResults;
    }

    public ContactResultPlan getContactResultPlan() {
        return contactResultPlan;
    }

    public void setContactResultPlan(ContactResultPlan contactResultPlan) {
        this.contactResultPlan = contactResultPlan;
    }

    public ContactResult getContactResult() {
        return contactResult;
    }

    public void setContactResult(ContactResult contactResult) {
        this.contactResult = contactResult;
    }

    public ContactResultPlanDAO getContactResultPlanDAO() {
        return contactResultPlanDAO;
    }

    public void setContactResultPlanDAO(ContactResultPlanDAO contactResultPlanDAO) {
        this.contactResultPlanDAO = contactResultPlanDAO;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public Collection<ContactResultPlan> getContactResultPlanCollection() {
        return contactResultPlanCollection;
    }

    public void setContactResultPlanCollection(Collection<ContactResultPlan> contactResultPlanCollection) {
        this.contactResultPlanCollection = contactResultPlanCollection;
    }

    public Collection<ContactResult> getContactResultCollection() {
        return contactResultCollection;
    }

    public void setContactResultCollection(Collection<ContactResult> contactResultCollection) {
        this.contactResultCollection = contactResultCollection;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public int getProductNameCount() {
        return productNameCount;
    }

    public void setProductNameCount(int productNameCount) {
        this.productNameCount = productNameCount;
    }
}
