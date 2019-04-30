// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CaseHandlingEditController.java

package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class CaseHandlingEditController extends BaseController
{
    private List<CaseType> caseTypeList = new ArrayList<CaseType>();
    private List<ActivityType> activityTypeList = new ArrayList<ActivityType>();
    private List<Channel> caseChannelList = new ArrayList<Channel>();
    private List<Channel> activityChannelList = new ArrayList<Channel>();
    private List<Relationship> relationshipList = new ArrayList<Relationship>();
    private List caseTopics = new ArrayList();
    private List caseDetails = new ArrayList();

    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Integer reasonRequestId;
    private Integer caseChannelId;
    private Integer priority;
    private Integer activityChannelId;
    private Integer activityTypeId;
    private Integer relationshipId;
    private String mode;
    private ContactCase contactCase;
    private Activity activity;

    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value="#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value="#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value="#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;
    @ManagedProperty(value="#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value="#{relationshipDAO}")
    private RelationshipDAO relationshipDAO;
    @ManagedProperty(value="#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value="#{customerDAO}")
    private CustomerDAO customerDAO;

    @PostConstruct
    public void initialize()
    {
        try
        {
            String selectId = getRequest("selectId");
            if(selectId == null)
            {
                mode = "add";
                setContactCase(new ContactCase());
                setActivity(new Activity());
                setCaseTypeList(getCaseTypeDAO().findCaseTypeEntities());
                setActivityTypeList(getActivityTypeDAO().findActivityTypeEntities());
                setCaseChannelList(getChannelDAO().findChannelEntities());
                setActivityChannelList(getCaseChannelList());
                setRelationshipList(getRelationshipDAO().findRelationshipEntities());
                setCaseTopics(Integer.valueOf(0));
            }
            JSFUtil.getUserSession().setRefNo(null);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("###################").append(e).toString());
        }
    }

    public void saveAction()
    {
        if("add".equals(mode))
        {
            contactCase.setCaseDetailId(getCaseDetailDAO().findCaseDetail(caseDetailId));
            contactCase.setRelationshipId(relationshipId);
            contactCase.setChannelId(getChannelDAO().findChannel(caseChannelId));
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
            CustomerInfoValue customerInfoValue = JSFUtil.getUserSession().getCustomerDetail();
            Customer customer = customerDAO.findCustomer(customerInfoValue.getId());
            contactCase.setCustomerId(customer);
            contactCase.setCode("0001");
            activity.setChannelId(getChannelDAO().findChannel(activityChannelId));
            activity.setActivityTypeId(getActivityTypeDAO().findActivityType(activityTypeId));
            contactCase.setActivityCollection(new ArrayList());
            contactCase.getActivityCollection().add(activity);
            if (contactCase.getCaseAttachmentCollection().size() > 0) {
                    contactCase.setAttachFile(true);
            } else {
                contactCase.setAttachFile(false);
            }
            getContactCaseDAO().create(contactCase);
        }
    }

    public List getCaseTypes()
    {
        List caseTypeValueList = new ArrayList();
        caseTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Iterator i$ = getCaseTypeList().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CaseType caseType = (CaseType)i$.next();
            if(caseType.getId() != null)
                caseTypeValueList.add(new SelectItem(caseType.getId(), caseType.getName()));
        } while(true);
        return caseTypeValueList;
    }

    public List getRelationships()
    {
        List relationshipValueList = new ArrayList();
        relationshipValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Iterator i$ = getRelationshipList().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Relationship relationship = (Relationship)i$.next();
            if(relationship.getId() != null)
                relationshipValueList.add(new SelectItem(relationship.getId(), relationship.getName()));
        } while(true);
        return relationshipValueList;
    }

    public List getCaseChannels()
    {
        List caseChannelValuseList = new ArrayList();
        caseChannelValuseList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Channel channel;
        for(Iterator i$ = caseChannelList.iterator(); i$.hasNext(); caseChannelValuseList.add(new SelectItem(channel.getId(), channel.getName())))
            channel = (Channel)i$.next();

        return caseChannelValuseList;
    }

    public List getActivityChannels()
    {
        List activityChannelValuseList = new ArrayList();
        activityChannelValuseList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Channel channel;
        for(Iterator i$ = activityChannelList.iterator(); i$.hasNext(); activityChannelValuseList.add(new SelectItem(channel.getId(), channel.getName())))
            channel = (Channel)i$.next();

        return activityChannelValuseList;
    }

    private List getCaseTopicList(Integer caseTypeId)
    {
        List caseTopicList = getCaseTopicDAO().findCaseTopicByCaseType(caseTypeId);
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        CaseTopic obj;
        for(Iterator i$ = caseTopicList.iterator(); i$.hasNext(); values.add(new SelectItem(obj.getId(), obj.getName())))
            obj = (CaseTopic)i$.next();

        return values;
    }

    private void setCaseTopics(Integer caseTypeId)
    {
        if(caseTopics != null)
            caseTopics.clear();
        if(caseDetails != null)
            caseDetails.clear();
        caseTopics = getCaseTopicList(caseTypeId);
        caseDetails = getCaseDetailList(Integer.valueOf(0));
    }

    public void caseTypeListener(ValueChangeEvent event)
    {
        Integer caseTypeIdChange = (Integer)event.getNewValue();
        setCaseTopics(caseTypeIdChange);
        FacesContext.getCurrentInstance().renderResponse();
    }

    private List getCaseDetailList(Integer caseTopicId)
    {
        List caseDetailList = getCaseDetailDAO().findCaseDetailByCaseTopic(caseTopicId);
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        CaseDetail obj;
        for(Iterator i$ = caseDetailList.iterator(); i$.hasNext(); values.add(new SelectItem(obj.getId(), obj.getName())))
            obj = (CaseDetail)i$.next();

        return values;
    }

    public void setCaseDetails(Integer caseTopicId)
    {
        if(caseDetails != null)
            caseDetails.clear();
        caseDetails = getCaseDetailList(caseTopicId);
    }

    public void caseTopicListener(ValueChangeEvent event)
    {
        Integer caseTopicIdChange = (Integer)event.getNewValue();
        setCaseDetails(caseTopicIdChange);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public List getActivityTypes()
    {
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        ActivityType activityType;
        for(Iterator i$ = getActivityTypeList().iterator(); i$.hasNext(); values.add(new SelectItem(activityType.getId(), activityType.getName())))
            activityType = (ActivityType)i$.next();

        return values;
    }

    public CaseTypeDAO getCaseTypeDAO()
    {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO)
    {
        this.caseTypeDAO = caseTypeDAO;
    }

    public CaseTopicDAO getCaseTopicDAO()
    {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO)
    {
        this.caseTopicDAO = caseTopicDAO;
    }

    public CaseDetailDAO getCaseDetailDAO()
    {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO)
    {
        this.caseDetailDAO = caseDetailDAO;
    }

    public ActivityTypeDAO getActivityTypeDAO()
    {
        return activityTypeDAO;
    }

    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO)
    {
        this.activityTypeDAO = activityTypeDAO;
    }

    public List getCaseTopics()
    {
        return caseTopics;
    }

    public Integer getCaseTypeId()
    {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId)
    {
        this.caseTypeId = caseTypeId;
    }

    public Integer getCaseTopicId()
    {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId)
    {
        this.caseTopicId = caseTopicId;
    }

    public Integer getRelationshipId()
    {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId)
    {
        this.relationshipId = relationshipId;
    }

    public List getCaseTypeList()
    {
        return caseTypeList;
    }

    public void setCaseTypeList(List caseTypeList)
    {
        this.caseTypeList = caseTypeList;
    }

    public List getActivityTypeList()
    {
        return activityTypeList;
    }

    public void setActivityTypeList(List activityTypeList)
    {
        this.activityTypeList = activityTypeList;
    }

    public List getCaseDetails()
    {
        return caseDetails;
    }

    public Integer getCaseDetailId()
    {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId)
    {
        this.caseDetailId = caseDetailId;
    }

    public Integer getReasonRequestId()
    {
        return reasonRequestId;
    }

    public void setReasonRequestId(Integer reasonRequestId)
    {
        this.reasonRequestId = reasonRequestId;
    }

    public Integer getCaseChannelId()
    {
        return caseChannelId;
    }

    public void setCaseChannelId(Integer caseChannelId)
    {
        this.caseChannelId = caseChannelId;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    public Integer getActivityChannelId()
    {
        return activityChannelId;
    }

    public void setActivityChannelId(Integer activityChannelId)
    {
        this.activityChannelId = activityChannelId;
    }

    public Integer getActivityTypeId()
    {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId)
    {
        this.activityTypeId = activityTypeId;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public ContactCase getContactCase()
    {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase)
    {
        this.contactCase = contactCase;
    }

    public ChannelDAO getChannelDAO()
    {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO)
    {
        this.channelDAO = channelDAO;
    }

    public RelationshipDAO getRelationshipDAO()
    {
        return relationshipDAO;
    }

    public void setRelationshipDAO(RelationshipDAO relationshipDAO)
    {
        this.relationshipDAO = relationshipDAO;
    }

    public ContactCaseDAO getContactCaseDAO()
    {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO)
    {
        this.contactCaseDAO = contactCaseDAO;
    }

    public List getCaseChannelList()
    {
        return caseChannelList;
    }

    private void setCaseChannelList(List caseChannelList)
    {
        this.caseChannelList = caseChannelList;
    }

    public List getActivityChannelList()
    {
        return activityChannelList;
    }

    private void setRelationshipList(List relationshipList)
    {
        this.relationshipList = relationshipList;
    }

    public List getRelationshipList()
    {
        return relationshipList;
    }

    private void setActivityChannelList(List activityChannelList)
    {
        this.activityChannelList = activityChannelList;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
