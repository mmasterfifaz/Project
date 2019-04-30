package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductCategoryDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.entity.YesLog;
import com.maxelyz.core.model.dao.YesLogDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.UserTransaction;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
//@RequestScoped
@ViewScoped
public class YesLogController implements Serializable{

    private static String REFRESH = "yeslog.xhtml?faces-redirect=true";
    private static String EDIT = "yeslog.xhtml";
    private static String SELECT = "yeslog.xhtml";
    private static Log log = LogFactory.getLog(YesLogController.class);

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<YesLog> yesLogs;

    private FileUploadBean fileUploadBean = new FileUploadBean();
    private String rootPath = JSFUtil.getRealPath()+"upload\\product\\";//"D:\\crm\\MaxarCRM\\web\\upload\\";

    @ManagedProperty(value="#{yesLogDAO}")
    private YesLogDAO yesLogDAO;
   /* @ManagedProperty(value="#{productPlanDetailDAO}")
    private ProductPlanDAO productPlanDetailDAO;*/

    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportyesfile:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
//        yesLogs = yesLogDAO.findYesLogEntitiesDesc();
        yesLogs = yesLogDAO.findYesLogByTypeDesc(true, -1, -1, 1);
    }

    public List<YesLog> getList() {
        return getYesLogs();
    }

     public String editAction() {
       return EDIT;
    }

     public String selectAction() {
       return SELECT;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<YesLog> getYesLogs() {
        return yesLogs;
    }

    public void setYesLogs(List<YesLog> yesLogs) {
        this.yesLogs = yesLogs;
    }

    public YesLogDAO getYesLogDAO() {
        return yesLogDAO;
    }

    public void setYesLogDAO(YesLogDAO yesLogDAO) {
        this.yesLogDAO = yesLogDAO;
    }

}
