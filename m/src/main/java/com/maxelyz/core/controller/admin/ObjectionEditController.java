package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ObjectionDAO;
import com.maxelyz.core.model.entity.Objection;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ObjectionEditController {
    private static Logger log = Logger.getLogger(ObjectionEditController.class);
    private static String REDIRECT_PAGE = "objection.jsf";
    private static String SUCCESS = "objection.xhtml?faces-redirect=true";
    private static String FAILURE = "objectionedit.xhtml";

    private Objection objection;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{objectionDAO}")
    private ObjectionDAO objectionDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:objection:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           objection = new Objection();
           objection.setEnable(Boolean.TRUE);
           objection.setStatus(Boolean.TRUE);

       } else {
           mode = "edit";
           ObjectionDAO dao = getObjectionDAO();
           objection = dao.findObjection(new Integer(selectedID));
           if (objection==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:objection:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:objection:edit"); 
       }
    }  
        
    public String saveAction() {
         messageDup = "";
        if(checkCode(objection)) {
            ObjectionDAO dao = getObjectionDAO();
            try {
                if (getMode().equals("add")) {
                    objection.setId(null);
                    objection.setEnable(true);
                    objection.setStatus(true);
                    dao.create(objection);
                } else {
                    dao.edit(objection);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Topic is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkCode(Objection objection) {
        String topic = objection.getTopic();
        Integer id=0; 
        if(objection.getId() != null)
            id = objection.getId();
        ObjectionDAO dao = getObjectionDAO();
        
        Integer cnt = dao.checkObjectionTopic(topic, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public Objection getObjection() {
        return objection;
    }

    public void setObjection(Objection objection) {
        this.objection = objection;
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

    public ObjectionDAO getObjectionDAO() {
        return objectionDAO;
    }

    public void setObjectionDAO(ObjectionDAO objectionDAO) {
        this.objectionDAO = objectionDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
