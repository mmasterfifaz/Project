package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.model.entity.KnowledgebaseLocation;
//import com.maxelyz.core.model.entity.KnowledgebaseLocationPK;
import com.maxelyz.core.model.entity.KnowledgebaseDivision;
import com.maxelyz.core.model.entity.KnowledgebaseDivisionPK;
import com.maxelyz.core.model.entity.KnowledgebaseRelated;
import com.maxelyz.core.model.entity.KnowledgebaseRelatedPK;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import java.util.ArrayList;

import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.BusinessUnitValue;
import com.maxelyz.core.model.value.admin.LocationValue;
import com.maxelyz.core.model.value.admin.ServiceTypeValue;
import com.maxelyz.utils.FileUploadBean;
import java.io.*;
//import java.util.concurrent.ConcurrentHashMap;
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;

@ManagedBean (name="knowledgebaseEditController")
//@RequestScoped
@ViewScoped
public class KnowledgebaseEditController {

    private static Logger log = Logger.getLogger(KnowledgebaseEditController.class);
    private static String REDIRECT_PAGE = "knowledgebase.jsf";
    private static String SUCCESS = "knowledgebase.xhtml?faces-redirect=true";
    private static String FAILURE = "knowledgebaseedit.xhtml";
    private static String VIEWVERSION = "knowledgebaseversion.xhtml";
    private Knowledgebase knowledgebase;
    private KnowledgebaseDivision knowledgebaseDivision;
    private KnowledgebaseDivisionPK knowledgebaseDivisionPK;
    private KnowledgebaseRelated knowledgebaseRelated;
    private KnowledgebaseRelatedPK knowledgebaseRelatedPK;
    //FileUpload 
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "knowledgebase");
    private List<KnowledgebaseAttfile> knowledgebaseAttfileList;
    private String mode;
    private String message;
    private String messageDup;
    private String messageDate;
    private Integer refId;
    private String relatekeyword;
    private String version;
    private int displayvote;
    private boolean selected;
    private List<ServiceTypeValue> serviceTypeValueList;
    private List<KnowledgebaseDivision> knowledgebaseDivisionList; 
    private List<Integer> selectedRelate;
    private Map<String, Integer> relateList = new LinkedHashMap<String, Integer>();
    private List<KnowledgebaseBoard> knowledgebasesboard;
    private List<KnowledgebaseUrl> urlList;
//    private List<Users> usersList;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:kb:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        String selectedID = null;
        messageDup = "";
        try {
            selectedID = (String) JSFUtil.getRequestParameterMap("selectedId");
            mode = (String) JSFUtil.getRequestParameterMap("mode");
        } catch (NullPointerException e) {
        }
//        usersList = usersDAO.findUsersEntities();

        urlList = new ArrayList<KnowledgebaseUrl>();
        selectedRelate = new ArrayList<Integer>();
        if (mode == null || mode.equals("add")) {
            KnowledgebaseDAO dao = getKnowledgebaseDAO();
            knowledgebase = new Knowledgebase();
            knowledgebase.setId(null);
            knowledgebase.setContentown(JSFUtil.getUserSession().getUsers().getId());
            if (selectedID != null) {
                refId = new Integer(selectedID);
            }
        } else if (mode.equals("edit")) {
            KnowledgebaseDAO dao = getKnowledgebaseDAO();
            knowledgebase = dao.findKnowledgebase(new Integer(selectedID));
            version = knowledgebase.getVersion();
            try {
                refId = knowledgebase.getKnowledgebase().getId();
            } catch (Exception e) {
                refId = new Integer(selectedID);
            }           
            ////log.error(refId);
            if (knowledgebase == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
        setBoard();
        setRelateList();
        fillKbDivisionList();
        fillServiceType();
        fillKbAttFileList();
        fillKbUrlList();
    }

    private void fillServiceType() {
        serviceTypeValueList = new ArrayList<ServiceTypeValue>();
        List<ServiceType> list = serviceTypeDAO.findServiceTypeStatus();
        List<Integer> serviceTypeIdList = null;
        if (knowledgebase.getId() != null) {
            serviceTypeIdList = knowledgebaseDAO.findServiceId(knowledgebase.getId());
        }
        for (ServiceType s : list) {
            ServiceTypeValue serviceTypeValue = new ServiceTypeValue(s);
            serviceTypeValue.setBusinessUnitValueList(fillBu(s));
            if (knowledgebase.getId() != null) {
                for (Integer serviceTypeId : serviceTypeIdList) {
                    if (serviceTypeId.intValue() == s.getId().intValue()) {
                        serviceTypeValue.setSelected(true);
                        break;
                    }
                }
            } else {
                serviceTypeValue.setSelected(true);
            }
            serviceTypeValueList.add(serviceTypeValue);
        }
    }

    private List<BusinessUnitValue> fillBu(ServiceType serviceType) {
        List<BusinessUnitValue> businessUnitValueList = new ArrayList<BusinessUnitValue>();
        Collection<BusinessUnit> list = businessUnitDAO.findBusinessUnitByServiceTypeId(serviceType.getId());
        List<Integer> businessUnitIdList = null;
        if (knowledgebase.getId() != null) {
            businessUnitIdList = knowledgebaseDAO.findBusinessUnitId(knowledgebase.getId(), serviceType.getId());
        }
        for (BusinessUnit b : list) {
            BusinessUnitValue businessUnitValue = new BusinessUnitValue(b);
            businessUnitValue.setLocationValueList(fillLocation(serviceType.getId(), b));
            if (knowledgebase.getId() != null) {
                for (Integer businessUnitId : businessUnitIdList) {
                    if (b.getId().intValue() == businessUnitId.intValue()) {
                        businessUnitValue.setSelected(true);
                        break;
                    }
                }
            } else {
                businessUnitValue.setSelected(true);
            }
            businessUnitValueList.add(businessUnitValue);
        }
        return businessUnitValueList;
    }

    private List<LocationValue> fillLocation(Integer serviceTypeId, BusinessUnit businessUnit) {
        List<LocationValue> locationValueList = new ArrayList<LocationValue>();
        Collection<Location> list = locationDAO.findLocationByBusinessUnitId(businessUnit.getId());
        List<Integer> locationIdList = null;
        if (knowledgebase.getId() != null) {
            locationIdList = knowledgebaseDAO.findLocationId(knowledgebase.getId(), serviceTypeId, businessUnit.getId());
        }
        for (Location location : list) {
            LocationValue locationValue = new LocationValue(location);
            if (knowledgebase.getId() != null) {
                for (Integer locationId : locationIdList) {
                    if (location.getId().intValue() == locationId.intValue()) {
                        locationValue.setSelected(true);
                        break;
                    }
                }
            } else {
                locationValue.setSelected(true);
            }
            locationValueList.add(locationValue);
        }
        return locationValueList;
    }
    
    public void serviceTypeListener() {
        Integer param1 = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("serviceTypeStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            if (s.getServiceType().getId().intValue() == param1.intValue()) {
                List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
                for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                    if (param2) {
                        b.setSelected(true);
                    } else {
                        b.setSelected(false);
                    }
                    List<LocationValue> list2 = new ArrayList<LocationValue>();
                    for (LocationValue l : b.getLocationValueList()) {
                        if (param2) {
                            l.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                        list2.add(l);
                    }
                    b.setLocationValueList(list2);
                    list1.add(b);
                }
                s.setBusinessUnitValueList(list1);
                list.add(s);

            } else {
                list.add(s);
            }
        }
        serviceTypeValueList = list;
    }

    public void businessUnitListener() {
        Integer businessUnitId = Integer.parseInt(JSFUtil.getRequestParameterMap("businessUnitId"));
        Integer serviceTypeId = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("businessUnitStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
            for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                List<LocationValue> list2 = new ArrayList<LocationValue>();
                for (LocationValue l : b.getLocationValueList()) {
                    if (s.getServiceType().getId() == serviceTypeId.intValue()
                            && b.getBusinessUnit().getId().intValue() == businessUnitId.intValue()) {
                        if (param2) {
                            l.setSelected(true);
                            b.setSelected(true);
                            s.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                    }
                    list2.add(l);
                }
                b.setLocationValueList(list2);
                list1.add(b);
            }
            s.setBusinessUnitValueList(list1);
            list.add(s);
        }
        serviceTypeValueList = list;
    }

    public void locationListener() {
        Integer locationId = Integer.parseInt(JSFUtil.getRequestParameterMap("locationId"));
        Integer businessUnitId = Integer.parseInt(JSFUtil.getRequestParameterMap("businessUnitId"));
        Integer serviceTypeId = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("locationStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
            for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                List<LocationValue> list2 = new ArrayList<LocationValue>();
                for (LocationValue l : b.getLocationValueList()) {
                    if (s.getServiceType().getId() == serviceTypeId.intValue()
                            && b.getBusinessUnit().getId() == businessUnitId.intValue()
                            && l.getLocation().getId().intValue() == locationId.intValue()) {
                        if (param2) {
                            l.setSelected(true);
                            b.setSelected(true);
                            s.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                    }
                    list2.add(l);
                }
                b.setLocationValueList(list2);
                list1.add(b);
            }
            s.setBusinessUnitValueList(list1);
            list.add(s);
        }
        serviceTypeValueList = list;
    }

    public void listener(ActionEvent event) {
        initialize();
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:kb:add");
        } else {
            return SecurityUtil.isPermitted("admin:kb:edit");
        }
    }

    public String viewAction() {
        return VIEWVERSION;
    }

    public String ApprovalKB() {
        messageDate = "";
        messageDup = "";
        KnowledgebaseDAO dao = getKnowledgebaseDAO();
        Date toDate = new Date();
        knowledgebase.setEnable(true);
        knowledgebase.setApproval(true);
        knowledgebase.setApprovalBy(JSFUtil.getUserSession().getUserName());
        knowledgebase.setApprovalDate(toDate);
        try {
            if(checkVersion(knowledgebase)) {
                if(knowledgebase.getSchedule() == null || !knowledgebase.getSchedule() || (knowledgebase.getStartdate().before(knowledgebase.getEnddate()))) {
                    if (getMode().equals("add")) {
                        knowledgebase.setId(null);
                        if(refId != null) {
                            Knowledgebase refkb = dao.findKnowledgebase(refId);
                            knowledgebase.setKnowledgebase(refkb);
                        } else {
                            knowledgebase.setKnowledgebase(null);
                        }
                        knowledgebase.setCreateBy(JSFUtil.getUserSession().getUserName());
                        knowledgebase.setCreateDate(toDate);
                        dao.create(knowledgebase);
                    } else {
                        knowledgebase.setUpdateBy(JSFUtil.getUserSession().getUserName());
                        knowledgebase.setUpdateDate(toDate);
                        dao.edit(knowledgebase);
                    }
                    saveAttFile();
                    saveVersion("approve");
                    saveRelate();
                    saveServiceType();
                    saveURL();
                } else {
                    messageDate = " Display Date From over Date To";
                    return null;
                }
            } else {
                messageDup = " Duplicate Version";
                return null;  
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return SUCCESS;
    }
    
    public String saveAction() {
        messageDate = "";
        messageDup = "";
        KnowledgebaseDAO dao = getKnowledgebaseDAO();
        try {
            if(checkVersion(knowledgebase)) {
                if(knowledgebase.getSchedule() == null || !knowledgebase.getSchedule() || (knowledgebase.getStartdate().before(knowledgebase.getEnddate()))) {
                    if (getMode().equals("add")) {
                        knowledgebase.setId(null);
                        if(refId != null) {
                            Knowledgebase refkb = dao.findKnowledgebase(refId);
                            knowledgebase.setKnowledgebase(refkb);
                        } else {
                            knowledgebase.setKnowledgebase(null);
                        }                        
                        knowledgebase.setEnable(true);
                        knowledgebase.setCreateBy(JSFUtil.getUserSession().getUserName());
                        knowledgebase.setCreateDate(new Date());
                        knowledgebase.setApproval(false);
                        dao.create(knowledgebase);
                    } else {
                        knowledgebase.setUpdateBy(JSFUtil.getUserSession().getUserName());
                        knowledgebase.setUpdateDate(new Date());
                        knowledgebase.setEnable(true);
                        dao.edit(knowledgebase);
                    }
                    saveAttFile();
                    saveVersion("save");
                    saveRelate();
                    saveServiceType();
                    saveURL();
                } else {
                    messageDate = " Display Date From over Date To";
                    return null;
                }
            } else {
                messageDup = " Duplicate Version";
                return null;  
            }
         } catch (Exception e) {
            log.error(e);
            return null;
        }
        return SUCCESS;
    }
    
    private void saveVersion(String mode) {
         try {
            KnowledgebaseDivision kbVersion = null;
            Date toDate = new Date();
            if (knowledgebase != null && knowledgebase.getId() != null && knowledgebase.getVersion().trim() != null) {
                knowledgebaseDivision = new KnowledgebaseDivision();
                kbVersion = knowledgebaseDAO.findKnowledgebaseVersion(knowledgebase.getId(), knowledgebase.getVersion().trim());
                if (kbVersion != null) {    //update old division
                    knowledgebaseDivision.setKnowledgebaseDivisionPK(new KnowledgebaseDivisionPK(knowledgebase.getId(), knowledgebase.getVersion().trim()));
                    knowledgebaseDivision.setTopic(knowledgebase.getTopic());
                    knowledgebaseDivision.setDescription(knowledgebase.getDescription());
                    knowledgebaseDivision.setTopicLevel(knowledgebase.getTopicLevel());
                    knowledgebaseDivision.setEnable(knowledgebase.getEnable());
                    knowledgebaseDivision.setSchedule(knowledgebase.getSchedule());
                    knowledgebaseDivision.setStartdate(knowledgebase.getStartdate());
                    knowledgebaseDivision.setEnddate(knowledgebase.getEnddate());
                    knowledgebaseDivision.setViewcount(knowledgebase.getViewcount());
                    knowledgebaseDivision.setCreateBy(kbVersion.getCreateBy());
                    knowledgebaseDivision.setCreateDate(kbVersion.getCreateDate());
                    knowledgebaseDivision.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    knowledgebaseDivision.setUpdateDate(toDate);
                    knowledgebaseDivision.setApproval(knowledgebase.getApproval());
                    knowledgebaseDivision.setApprovalBy(knowledgebase.getApprovalBy());
                    knowledgebaseDivision.setApprovalDate(knowledgebase.getApprovalDate());
                    knowledgebaseDivision.setFaq(knowledgebase.getFaq());
                    knowledgebaseDivision.setContentown(knowledgebase.getContentown());
                    knowledgebaseDAO.editDivision(knowledgebaseDivision);
                } else {
                    //update old division
                    KnowledgebaseDivision oldKBDivision = knowledgebaseDAO.findKnowledgebaseVersion(knowledgebase.getId(), version);
                    if(oldKBDivision != null) {
                        oldKBDivision.setViewcount(knowledgebase.getViewcount());
                        knowledgebaseDAO.editDivision(oldKBDivision);
                    }
                    
                    //create new version
                    knowledgebaseDivision.setKnowledgebaseDivisionPK(new KnowledgebaseDivisionPK(knowledgebase.getId(), knowledgebase.getVersion().trim()));
                    knowledgebaseDivision.setTopic(knowledgebase.getTopic());
                    knowledgebaseDivision.setDescription(knowledgebase.getDescription());
                    knowledgebaseDivision.setTopicLevel(knowledgebase.getTopicLevel());
                    knowledgebaseDivision.setEnable(knowledgebase.getEnable());
                    knowledgebaseDivision.setSchedule(knowledgebase.getSchedule());
                    knowledgebaseDivision.setStartdate(knowledgebase.getStartdate());
                    knowledgebaseDivision.setEnddate(knowledgebase.getEnddate());
                    knowledgebaseDivision.setViewcount(0);
                    knowledgebaseDivision.setCreateBy(JSFUtil.getUserSession().getUserName());
                    knowledgebaseDivision.setCreateDate(toDate);
                    knowledgebaseDivision.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    knowledgebaseDivision.setUpdateDate(toDate);
                    if(mode.equals("approve")) {
                        knowledgebaseDivision.setApprovalBy(knowledgebase.getApprovalBy());
                        knowledgebaseDivision.setApprovalDate(knowledgebase.getApprovalDate());
                        knowledgebaseDivision.setApproval(true);
                        knowledgebaseDAO.updateApprovalKnowledgebaseById(knowledgebase.getId(), true);
                    } else {
                        knowledgebaseDivision.setApprovalBy(null);
                        knowledgebaseDivision.setApprovalDate(null);
                        knowledgebaseDivision.setApproval(false);
                        knowledgebaseDAO.updateApprovalKnowledgebaseById(knowledgebase.getId(), false);
                    }
                    knowledgebaseDivision.setFaq(knowledgebase.getFaq());
                    knowledgebaseDivision.setContentown(knowledgebase.getContentown());
                    knowledgebaseDAO.createDivision(knowledgebaseDivision);
                    
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void saveRelate() {
        try {
            if (knowledgebase != null && knowledgebase.getId() != null && knowledgebase.getVersion().trim() != null) {
                knowledgebaseDAO.deleteAllKnowledgebaseRelateId(knowledgebase.getId(), knowledgebase.getVersion().trim());
                List<Integer> list = selectedRelate;
                for (int s : list) { 
                    knowledgebaseRelated = new KnowledgebaseRelated();
                    knowledgebaseRelated.setKnowledgebaseRelatedPK(new KnowledgebaseRelatedPK(knowledgebase.getId(), knowledgebase.getVersion().trim(), s));
                    knowledgebaseDAO.createRelated(knowledgebaseRelated);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public Boolean checkVersion(Knowledgebase knowledgebase) {
        String versionKB = knowledgebase.getVersion();
        Integer id = 0, cnt = 0; 
        
        if(mode.equals("edit")) {
            if(version.equals(versionKB)) {
                cnt = 0;
            } else {
                id = knowledgebase.getId();
                cnt = this.getKnowledgebaseDAO().checkVersion(versionKB, id);
            }
        }
        if(cnt == 0)
            return true;
        else
            return false;
    }

    private void fillKbDivisionList() {
        try {
            knowledgebaseDivisionList = new ArrayList<KnowledgebaseDivision>();
            if (knowledgebase.getId() != null) {
                List<KnowledgebaseDivision> list = knowledgebaseDAO.findKnowledgebaseDivisionById(knowledgebase.getId());


                for (KnowledgebaseDivision s : list) {
                    knowledgebaseDivisionList.add(s);
                }

            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void setRelateList() {
        try {
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
          
            if (knowledgebase.getId() != null) {
                
                
                List<Knowledgebase> slist = knowledgebaseDAO.findKnowledgebaseRelateId(knowledgebase.getId(), knowledgebase.getVersion().trim());
                for (Knowledgebase s : slist) {
                    selectedRelate.add(s.getId());
                }
                
                List<Knowledgebase> nlist = knowledgebaseDAO.findKnowledgebaseNotId(knowledgebase.getId());
                for (Knowledgebase n : nlist) {
                    values.put(n.getTopic(), n.getId());
                }
            } else {
                List<Knowledgebase> nlist = knowledgebaseDAO.findKnowledgebaseNotId(0);
                for (Knowledgebase n : nlist) {
                    values.put(n.getTopic(), n.getId());
                }
            } 
            relateList = values;
        } catch (Exception e) {
            log.error(e);
        }

    }
    private void saveServiceType() {
        try {
            List<KnowledgebaseLocation> list = null;
            if (knowledgebase != null && knowledgebase.getId() != null) {
                list = knowledgebaseDAO.findKnowledgebaseLocationByKnowledgebaseId(knowledgebase.getId());
                for (KnowledgebaseLocation knowledgebaseLocation : list) {
                    knowledgebaseDAO.destroyKnowledgebaseLocation(knowledgebaseLocation.getKnowledgebaseLocationPK());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        try {
            for (ServiceTypeValue s : serviceTypeValueList) {
                for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                    for (LocationValue l : b.getLocationValueList()) {
                        if (l.isSelected()) {
                            KnowledgebaseLocation knowledgebaseLocation = new KnowledgebaseLocation(knowledgebase.getId(), s.getServiceType().getId(), b.getBusinessUnit().getId(), l.getLocation().getId());
                            knowledgebaseDAO.createLocation(knowledgebaseLocation);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setUsers(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    public List<ServiceTypeValue> getServiceTypeValueList() {
        return serviceTypeValueList;
    }

    public void setServiceTypeValueList(List<ServiceTypeValue> serviceTypeValueList) {
        this.serviceTypeValueList = serviceTypeValueList;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public List<KnowledgebaseDivision> getKnowledgebaseDivisionList() {
        return knowledgebaseDivisionList;
    }

    public void setKnowledgebaseDivisionList(List<KnowledgebaseDivision> knowledgebaseDivisionList) {
        this.knowledgebaseDivisionList = knowledgebaseDivisionList;
    }

    public String getRelatekeyword() {
        return relatekeyword;
    }

    public void setRelatekeyword(String relatekeyword) {
        this.relatekeyword = relatekeyword;
    }

    public List<KnowledgebaseBoard> getKnowledgebasesboard() {
        return knowledgebasesboard;
    }

    public void setKnowledgebasesboard(List<KnowledgebaseBoard> knowledgebasesboard) {
        this.knowledgebasesboard = knowledgebasesboard;
    }

    public int getDisplayvote() {
        return displayvote;
    }

    public void setDisplayvote(int displayvote) {
        this.displayvote = displayvote;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public List<KnowledgebaseAttfile> getKnowledgebaseAttfileList() {
        return knowledgebaseAttfileList;
    }

    public void setKnowledgebaseAttfileList(List<KnowledgebaseAttfile> knowledgebaseAttfileList) {
        this.knowledgebaseAttfileList = knowledgebaseAttfileList;
    }

    public List<KnowledgebaseUrl> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<KnowledgebaseUrl> urlList) {
        this.urlList = urlList;
    }

//    public List<Users> getUsersList() {
//        return usersList;
//    }
//
//    public void setUsersList(List<Users> usersList) {
//        this.usersList = usersList;
//    }

    public void findRelateList() {
        try {
            if (!relatekeyword.isEmpty()) {  
            }
        } catch (Exception e) {
            log.error(e);
        }

    }

    private void setBoard() {
        try {
            /////////////
            if (knowledgebase != null) {
                displayvote = new Integer(0);
                displayvote = knowledgebaseDAO.getKnowledgebaseVoteAverage(knowledgebase.getId(), knowledgebase.getVersion());

                knowledgebasesboard = new ArrayList<KnowledgebaseBoard>();
                if (knowledgebase.getId() != null) {
                    List<KnowledgebaseBoard> blist = knowledgebaseDAO.findKnowledgebaseBoardById(knowledgebase.getId(), knowledgebase.getVersion());
                    int i = 0;
                    for (KnowledgebaseBoard b : blist) {
                        b.setNo(blist.size() - i);
                        knowledgebasesboard.add(b);
                        i++;
                    }
                }
            }
            /////////
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void saveAttFile() {
        try {
            if (knowledgebase != null && knowledgebase.getId() != null && knowledgebase.getVersion().trim() != null) {

                if (knowledgebaseAttfileList != null) {
                    for (KnowledgebaseAttfile df : knowledgebaseAttfileList) {
                        if (df.getDelbox()) {
                            File f1 = new File(JSFUtil.getuploadPath() + "knowledgebase/" + knowledgebase.getId().toString() + "/" + df.getKbattfileFilename());;
                            knowledgebaseDAO.deleteKnowledgebaseAttFileById(df.getKbattfileId());
                            f1.delete();
                        }
                    }
                }
                if (fileUploadBean != null) {
                    List<File> fileList = fileUploadBean.getFiles();
                    for (File f : fileList) {
                        fileUploadBean.createDirName(knowledgebase.getId().toString());
                        fileUploadBean.moveFile(f, fileUploadBean.getFolderPath());
                        KnowledgebaseAttfile knowledgebaseAttfile = new KnowledgebaseAttfile();
                        knowledgebaseAttfile.setKnowledgebaseId(knowledgebase.getId());
                        knowledgebaseAttfile.setKbattfileFilename(f.getName());
                        knowledgebaseAttfile.setCreateBy(JSFUtil.getUserSession().getUserName());
                        knowledgebaseAttfile.setCreateDate(new Date());
                        knowledgebaseDAO.createAttFile(knowledgebaseAttfile);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void fillKbAttFileList() {
        try {
            knowledgebaseAttfileList = new ArrayList<KnowledgebaseAttfile>();
            if (knowledgebase.getId() != null) {
                List<KnowledgebaseAttfile> list = knowledgebaseDAO.findKnowledgebaseAttFileById(knowledgebase.getId());
                for (KnowledgebaseAttfile s : list) {
                    s.setDelbox(false);
                    knowledgebaseAttfileList.add(s);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }


//    public String RejectKB() {
//
//        KnowledgebaseDAO dao = getKnowledgebaseDAO();
//
//        try {
//            dao.updateApprovalKnowledgebaseById(knowledgebase.getId(), false, JSFUtil.getUserSession().getUserName(), new Date());
//        } catch (Exception e) {
//            log.error(e);
//            return FAILURE;
//        }
//        return SUCCESS;
//    }

    public void addUrlList(ActionEvent event) {
        try {
//            if (urlList != null) {
                KnowledgebaseUrl kburl = new KnowledgebaseUrl();
//                kburl.setKburlLink("http://");
                kburl.setKburlText("");
                urlList.add(kburl);
//            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void delUrlList(ActionEvent event) {
        try {
            if (urlList != null) {
                int seqNo = urlList.size() - 1;
                if(seqNo >= 0)
                    urlList.remove(seqNo);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public Map<String, Integer> getRelateList() {
        return relateList;
    }

    public void setRelateList(Map<String, Integer> relateList) {
        this.relateList = relateList;
    }

    public List<Integer> getSelectedRelate() { 
        return selectedRelate;
    }

    public void setSelectedRelate(List<Integer> selectedRelate) {
        this.selectedRelate = selectedRelate;
    }

    private void saveURL() {
        try {
            if (knowledgebase != null && knowledgebase.getId() != null && knowledgebase.getVersion().trim() != null) {
                knowledgebaseDAO.deleteAllKnowledgebaseURLById(knowledgebase.getId(), knowledgebase.getVersion().trim());
                List<KnowledgebaseUrl> list = urlList;
                for (KnowledgebaseUrl s : list) {
                    if (!s.getKburlText().isEmpty() && !s.getKburlLink().isEmpty()) {
                        if(!s.getKburlLink().startsWith("http://") && !s.getKburlLink().startsWith("https://")) {
                            s.setKburlLink("http://".concat(s.getKburlLink()));
                        }
                        s.setKburlId(null);
                        s.setKnowledgebaseId(knowledgebase.getId());
                        s.setKnowledgebaseVersion(knowledgebase.getVersion().trim());
                        knowledgebaseDAO.createUrl(s);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void fillKbUrlList() {
        try {
            urlList = new ArrayList<KnowledgebaseUrl>();
            if (knowledgebase.getId() != null) {
                List<KnowledgebaseUrl> list = knowledgebaseDAO.findKnowledgebaseUrlById(knowledgebase.getId(), knowledgebase.getVersion());
                for (KnowledgebaseUrl s : list) {
                    urlList.add(s);
                }
            }
//            if (urlList == null || urlList.isEmpty()) {
//                KnowledgebaseUrl kburl = new KnowledgebaseUrl();
////                kburl.setKburlLink("http://");
//                kburl.setKburlText("");
//                urlList.add(kburl);
//            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
