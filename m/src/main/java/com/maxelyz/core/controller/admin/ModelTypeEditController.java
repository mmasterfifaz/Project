package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import com.maxelyz.core.model.dao.ModelTypeDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.core.model.entity.ModelType;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ModelTypeEditController {
    private static Logger log = Logger.getLogger(BrandEditController.class);
    private static String REDIRECT_PAGE = "modeltype.jsf";
    private static String SUCCESS = "modeltype.xhtml?faces-redirect=true";
    private static String FAILURE = "modeltypeedit.xhtml";

    private ModelType modelType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{modelTypeDAO}")
    private ModelTypeDAO modelTypeDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:modeltype:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            modelType = new ModelType();
            modelType.setEnable(Boolean.TRUE);
            modelType.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                modelType = modelTypeDAO.findModelType(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (modelType == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:modeltype:add");
        } else {
            return SecurityUtil.isPermitted("admin:modeltype:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(modelType)) {
            try {
                if (getMode().equals("add")) {
                    modelType.setId(null);
                    modelType.setEnable(true);
                    modelTypeDAO.create(modelType);
                } else {
                    modelTypeDAO.edit(modelType);
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
    
    public Boolean checkCode(ModelType modelType) {
        String code = modelType.getCode();
        Integer id=0; 
        if(modelType.getId() != null)
            id = modelType.getId();
        ModelTypeDAO dao = getModelTypeDAO();
        
        Integer cnt = dao.checkModelTypeCode(code, id);
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
