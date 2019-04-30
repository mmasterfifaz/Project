package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import com.maxelyz.core.model.dao.ModelDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.core.model.entity.Model;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.List;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ModelEditController {
    private static Logger log = Logger.getLogger(BrandEditController.class);
    private static String REDIRECT_PAGE = "model.jsf";
    private static String SUCCESS = "model.xhtml?faces-redirect=true";
    private static String FAILURE = "modeledit.xhtml";

    private Model model;
    private String mode;
    private String message;
    private String messageDup;
    
    private List<Brand> brandList;

    @ManagedProperty(value="#{brandDAO}")
    private BrandDAO brandDAO;
    @ManagedProperty(value="#{modelDAO}")
    private ModelDAO modelDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:model:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            Brand brand = new Brand();
            model = new Model();
            model.setBrand(brand);
            model.setEnable(Boolean.TRUE);
            model.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                model = modelDAO.findModel(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (model == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
        brandList = brandDAO.findBrandEntities();
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:model:add");
        } else {
            return SecurityUtil.isPermitted("admin:model:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(model)) {
            try {
                if (getMode().equals("add")) {
                    model.setId(null);
                    model.setEnable(true);
                    modelDAO.create(model);
                } else {
                    modelDAO.edit(model);
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

    public Boolean checkCode(Model model) {
        String code = model.getCode();
        Integer id=0; 
        if(model.getId() != null)
            id = model.getId();
        ModelDAO dao = getModelDAO();
        
        Integer cnt = dao.checkModelCode(code, id);
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

    public BrandDAO getBrandDAO() {
        return brandDAO;
    }

    public void setBrandDAO(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
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

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
