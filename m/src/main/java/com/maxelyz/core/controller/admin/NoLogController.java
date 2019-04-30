package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.YesLogDAO;
import com.maxelyz.core.model.entity.YesLog;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
//@RequestScoped
@ViewScoped
public class NoLogController implements Serializable{

    private static String REFRESH = "nolog.xhtml?faces-redirect=true";
    private static String EDIT = "nolog.xhtml";
    private static String SELECT = "nolog.xhtml";
    private static Log log = LogFactory.getLog(NoLogController.class);

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
        if (!SecurityUtil.isPermitted("admin:exportnofile:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
//        yesLogs = yesLogDAO.findYesLogEntitiesDesc();
        yesLogs = yesLogDAO.findYesLogByTypeDesc(true, -1, -1, 2);
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
