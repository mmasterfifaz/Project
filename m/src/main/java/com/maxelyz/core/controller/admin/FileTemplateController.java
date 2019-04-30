package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.FileTemplateDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.FileTemplate;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@RequestScoped
public class FileTemplateController implements Serializable {

    private static Logger log = Logger.getLogger(FileTemplateController.class);
    private static String REFRESH = "filetemplate.xhtml?faces-redirect=true";
    private static String EDIT = "filetemplateedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<FileTemplate> fileTemplates;
    private FileTemplate fileTemplate;
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:fileformat:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        FileTemplateDAO dao = getFileTemplateDAO();
        fileTemplates = dao.findFileTemplateEntities();

    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:fileformat:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:fileformat:delete");
    }

    public List<FileTemplate> getList() {
        return getFileTemplates();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        FileTemplateDAO dao = getFileTemplateDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    fileTemplate = dao.findFileTemplate(item.getKey());
                    fileTemplate.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    fileTemplate.setUpdateDate(new Date());
                    fileTemplate.setEnable(false);
                    dao.edit(fileTemplate);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<FileTemplate> getFileTemplates() {
        return fileTemplates;
    }

    public void setFileTemplates(List<FileTemplate> fileTemplates) {
        this.fileTemplates = fileTemplates;
    }

    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }
}
