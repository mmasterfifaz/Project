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
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@ViewScoped
public class ContactResultPlanEditController {

    private static Logger log = Logger.getLogger(CardExclusionEditController.class);
    private static String REDIRECT_PAGE = "contactresultplan.jsf";
    private static String SUCCESS = "contactresultplan.xhtml?faces-redirect=true";
    private static String FAILURE = "contactresultplanedit.xhtml";

    private List<ContactResult> contactResults;
    private List<ContactResultPlan> contactResultPlans;
    private List<Integer> selectedContactResult = new ArrayList<Integer>();
    private Map<String, Integer> contactResultList = new LinkedHashMap<String, Integer>();

    private ContactResultPlan contactResultPlan;
    private ContactResult contactResult;

    private String mode;
    private String message;
    private String messageDup;

    private Integer contactResultCount;
    private Integer isDefaultCount;

    private Boolean isDefault;

    @ManagedProperty(value = "#{contactResultPlanDAO}")
    private ContactResultPlanDAO contactResultPlanDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:contactresultplan:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        contactResultPlans = getContactResultPlanDAO().findContactResultPlanEntities();
        isDefaultCount = getContactResultPlanDAO().checkIsDefault();

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            contactResultPlan = new ContactResultPlan();
            if (isDefaultCount == 0) {
                contactResultPlan.setIsDefault(true);
            }
        } else {
            mode = "edit";
            
            contactResultPlan = getContactResultPlanDAO().findContactResultPlan(new Integer(selectedID));

            for (ContactResult cr : contactResultPlan.getContactResultCollection()) {
                this.selectedContactResult.add(cr.getId());
            }

        }
        setContactResultList(contactResultDAO.findSelectedContactResult());
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:contactresultplan:add");
        } else {
            return SecurityUtil.isPermitted("admin:contactresultplan:edit");
        }
    }

    public String saveAction() {

        messageDup = "";
        int countDefaultContectResultPlan = 0;
        if (checkCode(contactResultPlan)) {
            ContactResultPlanDAO dao = contactResultPlanDAO;
            contactResultPlan.setContactResultCollection(this.getSelectedContactResultCollection());

            List<Integer> contactID = new ArrayList<Integer>();
            for (int cid : selectedContactResult) {
                contactID.add(new Integer(cid));
            }
            contactResultCount = getContactResultDAO().findSelectedContactResult(contactID);

            for(int i=0;i<contactResultPlans.size();i++)
            {
                if(contactResultPlans.get(i).getIsDefault())
                {
                    countDefaultContectResultPlan++;
                }
            }
            
            if(contactResultPlan.getId()==null)
            {
                if(contactResultPlan.getIsDefault())
                {
                    countDefaultContectResultPlan++;
                }
            }
            
            if(countDefaultContectResultPlan>1)
            {
                contactResultPlan.setIsDefault(false);
                messageDup = "Default Plan is already exist";
                return null;
            }
            
            if(contactResultPlan.getIsDefault())
            {
                contactResultCount = 1;
            }

            if (contactResultCount != 0) {
                try {
                    if (getMode().equals("add")) {
                        contactResultPlan.setId(null);
                        contactResultPlan.setEnable(true);
                        contactResultPlan.setCreateBy(JSFUtil.getUserSession().getUserName());
                        contactResultPlan.setCreateDate(new Date());
                        dao.create(contactResultPlan);
                    } else {
                        contactResultPlan.setUpdateBy(JSFUtil.getUserSession().getUserName());
                        contactResultPlan.setUpdateDate(new Date());
                        dao.edit(contactResultPlan);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                messageDup = "Please select at lease 1 yes sale";
                return null;
            }

        } else {
            messageDup = "Name is already taken";
            //return null;
        }
        return SUCCESS;
    }

    public Boolean checkCode(ContactResultPlan contactResultPlan) {
        String name = contactResultPlan.getName();
        Integer id = 0;
        if (contactResultPlan.getId() != null) {
            id = contactResultPlan.getId();

            Integer cnt = contactResultPlanDAO.checkContactResultPlan(name, id);
            if (cnt == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public String backAction() {
        return SUCCESS;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
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

    public List<ContactResult> getContactResults() {
        return contactResults;
    }

    public void setContactResults(List<ContactResult> contactResults) {
        this.contactResults = contactResults;
    }

    public List<Integer> getSelectedContactResult() {
        return selectedContactResult;
    }

    public void setSelectedContactResult(List<Integer> selectedContactResult) {
        this.selectedContactResult = selectedContactResult;
    }

    public List<ContactResult> getSelectedContactResultCollection() {
        List<ContactResult> crGroups = new ArrayList<ContactResult>();
        for (int contactResultId : selectedContactResult) {
            ContactResult obj = new ContactResult();
            obj.setId(contactResultId);
            crGroups.add(obj);
        }
        return crGroups;
    }

    public Map<String, Integer> getContactResultList() {
        return contactResultList;
    }

    public void setContactResultList(List<ContactResult> crGroups) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ContactResult obj : crGroups) {
            values.put(obj.getName()+" ("+obj.getContactStatus()+")", obj.getId());           
        }
        this.contactResultList = values;
    }

    public Integer getContactResultCount() {
        return contactResultCount;
    }

    public void setContactResultCount(Integer contactResultCount) {
        this.contactResultCount = contactResultCount;
    }

    public List<ContactResult> getContactValue() {
        List<ContactResult> cr = new ArrayList<ContactResult>();
        for (int contactResultId : selectedContactResult) {
            ContactResult obj = new ContactResult();
            obj.setId(contactResultId);
            cr.add(obj);
        }
        return cr;
    }

    public Integer getIsDefaultCount() {
        return isDefaultCount;
    }

    public void setIsDefaultCount(Integer isDefaultCount) {
        this.isDefaultCount = isDefaultCount;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
