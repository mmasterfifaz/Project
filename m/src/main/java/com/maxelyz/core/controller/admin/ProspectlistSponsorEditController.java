/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductSponsorDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProspectlistSponsorDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.ProductSponsorUserGroup;
import com.maxelyz.core.model.entity.ProductSponsorUserGroupPK;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ProspectlistSponsorEditController {

    private static Logger log = Logger.getLogger(ProspectlistSponsorEditController.class);
    private static String REDIRECT_PAGE = "listsponsor.jsf";
    private static String SUCCESS = "listsponsor.xhtml?faces-redirect=true";
    private static String FAILURE = "listsponsoredit.xhtml";
    private ProspectlistSponsor prospectlistSponsor;
    private String mode;
    private Integer refId;
    private String message;
    private Map<String, Integer> userGroupList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedUserGroup = new ArrayList<Integer>();
    
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:marketinglistsource:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        message = "";
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            prospectlistSponsor = new ProspectlistSponsor();
        } else {
            mode = "edit";
            ProspectlistSponsorDAO dao = getProspectlistSponsorDAO();
            prospectlistSponsor = dao.findProspectlistSponsor(new Integer(selectedID));
            if (prospectlistSponsor == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
            List<ProductSponsorUserGroup> ugP = this.getProductSponsorDAO().findProductSponsorUserGroupEntitiesByProspectSponsorId(prospectlistSponsor.getId());
            if(ugP != null && ugP.size() > 0){
                for(ProductSponsorUserGroup ug : ugP){
                    selectedUserGroup.add(ug.getProductSponsorUserGroupPK().getUserGroupId());
                }
            }
        }
        this.setUserGroupList();
    }
    
    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:marketinglistsource:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:marketinglistsource:edit"); 
       }
    } 
    
    public String saveAction() {
        message = "";
        if(checkName(prospectlistSponsor)) {
            ProspectlistSponsorDAO dao = getProspectlistSponsorDAO();
            try {
                prospectlistSponsor.setEnable(true);
                if (getMode().equals("add")) {
                    prospectlistSponsor.setId(null);//fix some bug
                    dao.create(prospectlistSponsor);
                } else {
                    dao.edit(prospectlistSponsor);
                    this.getProductSponsorDAO().destroyByProspectSponsorId(prospectlistSponsor.getId());
                }
                if(selectedUserGroup != null && selectedUserGroup.size() > 0){
                    for(Integer a : selectedUserGroup){
                        ProductSponsorUserGroup temp = new ProductSponsorUserGroup();
                        ProductSponsorUserGroupPK tempPk = new ProductSponsorUserGroupPK();
                        tempPk.setUserGroupId(a);
                        tempPk.setProductSponsorId(prospectlistSponsor.getId());
                        temp.setProductSponsorUserGroupPK(tempPk);
                        this.getProductSponsorDAO().create(temp);
                    }
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
    
    public Boolean checkName(ProspectlistSponsor prospectlistSponsor) {
        String name = prospectlistSponsor.getName();
        Integer id=0; 
        if(prospectlistSponsor.getId() != null)
            id = prospectlistSponsor.getId();
        ProspectlistSponsorDAO dao = getProspectlistSponsorDAO();
        
        Integer cnt = dao.checkProspectListName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public ProspectlistSponsor getProspectlistSponsor() {
        return prospectlistSponsor;
    }

    public void setUsers(ProspectlistSponsor prospectlistSponsor) {
        this.prospectlistSponsor = prospectlistSponsor;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        List<UserGroup> ugs = this.getUserGroupDAO().findUserGroupByRoleUwPaymentQc();
        if(ugs != null){
            for(UserGroup ug : ugs){
                values.put(ug.getName(), ug.getId());
            }
        }
        userGroupList = values;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public List<Integer> getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(List<Integer> selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }

    public ProductSponsorDAO getProductSponsorDAO() {
        return productSponsorDAO;
    }

    public void setProductSponsorDAO(ProductSponsorDAO productSponsorDAO) {
        this.productSponsorDAO = productSponsorDAO;
    }
    
}
