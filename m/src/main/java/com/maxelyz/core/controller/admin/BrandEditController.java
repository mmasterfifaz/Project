package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class BrandEditController {
    private static Logger log = Logger.getLogger(BrandEditController.class);
    private static String REDIRECT_PAGE = "brand.jsf";
    private static String SUCCESS = "brand.xhtml?faces-redirect=true";
    private static String FAILURE = "brandedit.xhtml";

    private Brand brand;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{brandDAO}")
    private BrandDAO brandDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:brand:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            brand = new Brand();
            brand.setEnable(Boolean.TRUE);
            brand.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                brand = brandDAO.findBrand(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (brand == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:brand:add");
        } else {
            return SecurityUtil.isPermitted("admin:brand:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(brand)) {
            try {
                if (getMode().equals("add")) {
                    brand.setId(null);
                    brand.setEnable(true);
                    brandDAO.create(brand);
                } else {
                    brandDAO.edit(brand);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkCode(Brand brand) {
        String code = brand.getCode();
        Integer id=0; 
        if(brand.getId() != null)
            id = brand.getId();
        BrandDAO dao = getBrandDAO();
        
        Integer cnt = dao.checkBrandCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
