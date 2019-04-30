package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProductSponsorDAO;
import com.maxelyz.core.model.entity.ProductSponsor;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ProductSponsorEditController {

    private static Logger log = Logger.getLogger(ProductSponsorEditController.class);
    private static String SUCCESS = "productsponsor.xhtml?faces-redirect=true";
    private static String FAILURE = "productsponsoredit.xhtml";
    private ProductSponsor productSponsor;
    private String mode;
    private String message;
    
    @ManagedProperty(value = "#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productsource:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        message = "";
        String selectedID = null;
        try {
            selectedID = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestParameterMap()
                    .get("selectedID");
        } catch (NullPointerException e) {}

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            productSponsor = new ProductSponsor();
        } else {
            mode = "edit";
            ProductSponsorDAO dao = getProductSponsorDAO();
            productSponsor = dao.findProductSponsor(new Integer(selectedID));
        }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:productsource:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:productsource:edit"); 
       }
    }  

    public String saveAction() {
        message = "";
        if(checkName(productSponsor)) {
            ProductSponsorDAO dao = getProductSponsorDAO();
            try {
                productSponsor.setEnable(true);
                if (getMode().equals("add")) {
                    productSponsor.setId(null);//fix some bug
                    dao.create(productSponsor);
                } else {
                    dao.edit(productSponsor);
                }
            } catch (Exception e) {
                log.error(e);
                return FAILURE;
            }
            return SUCCESS;
        } else {
            message = " Name is already taken";
            return FAILURE;    
        }
    }

    public Boolean checkName(ProductSponsor productSponsor) {
        String name = productSponsor.getName();
        Integer id=0; 
        if(productSponsor.getId() != null)
            id = productSponsor.getId();
        ProductSponsorDAO dao = getProductSponsorDAO();
        
        Integer cnt = dao.checkProductName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public ProductSponsor getProductSponsor() {
        return productSponsor;
    }

    public void setProductSponsor(ProductSponsor productSponsor) {
        this.productSponsor = productSponsor;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    public ProductSponsorDAO getProductSponsorDAO() {
        return productSponsorDAO;
    }

    public void setProductSponsorDAO(ProductSponsorDAO productSponsorDAO) {
        this.productSponsorDAO = productSponsorDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
