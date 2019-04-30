/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ManageListValueDAO;
import com.maxelyz.core.model.dao.ListDetailDAO;
import com.maxelyz.core.model.entity.ListDetail;
import com.maxelyz.core.model.entity.ManageListValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author User
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ManageListValueController {
    public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(ManageListValueController.class);
    private static String SUCCESS = "managelistvalue.xhtml?faces-redirect=true";
    private static String FAILURE = "managelistvalue.xhtml";
    private static String REFRESH = "managelistvalue.xhtml?faces-redirect=true";
    private ManageListValue manageListValue;
    private ListDetail singleListDetail;
    private List<ManageListValue> listManageListValue = new ArrayList<ManageListValue>();
    @ManagedProperty(value = "#{manageListValueDAO}")
    private ManageListValueDAO manageListValueDAO;
    @ManagedProperty(value = "#{listDetailDAO}")
    private ListDetailDAO listDetailDAO;

    private String mode;
    private String message;
    private String messageDup;
//    private List<String> manageList = new ArrayList<String>();
    private SelectItem[] manageList;
    private List<String>fieldList = new ArrayList<String>();
    public int count_seqNo=0;
    public int count_seqNo2=0;
    private List<Number> orderSeqNo = new ArrayList<Number>();
    private List<ListDetail> listDetail = new ArrayList<ListDetail>();
    private SelectItem[] listOfDetailList;
    
    private String listSelected;
    private Integer listDetailId;
    private Boolean isAdd = false;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private String code;
    private String name;
    private Integer seqNo;
    
//    private FileUploadBean fileUploadBean = new FileUploadBean();
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:managelistvalue:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        this.setManageList();
//        for (int i = 1; i <= 1; i++) {        
//            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
//            setDeclarationField(0, r);
//            if (mode.equals("edit")) {
//                fillDeclarationField(i, r);
//            }
//            declarationFieldInfoValues.add(r);
//        }
//        for (int i = 1; i <= 1; i++) {        
//            ArrayList<DeclarationField> r = new ArrayList<DeclarationField>();
//            setDeclarationField(10, r);
//            if (mode.equals("edit")) {
//                fillDeclarationField(i, r);
//            }
//            declarationFieldInfoValues.add(r);
//        }

        //declarationFields = declarationFieldInfoValues.get(0);
        
    }
    
    public void onListSelectedListener(ValueChangeEvent event){
        try
        {
          this.setListOfDetailList(Integer.parseInt(event.getNewValue().toString()));
          isAdd = false;
        }
        catch(Exception ex)
        {
            log.error(ex);
        }
        
    }
    
    public String deleteAction() throws Exception {
        ListDetailDAO dao = getListDetailDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    singleListDetail = dao.findListDetail(item.getKey());
                    singleListDetail.setEnable(false);
                    dao.edit(singleListDetail);
                    setListOfDetailList(Integer.parseInt(listSelected));
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public ManageListValue getManageListValue() {
        return manageListValue;
    }

    public void setManageListValue(ManageListValue manageListValue) {
        this.manageListValue = manageListValue;
    }

    public ManageListValueDAO getManageListValueDAO() {
        return manageListValueDAO;
    }

    public void setManageListValueDAO(ManageListValueDAO manageListValueDAO) {
        this.manageListValueDAO = manageListValueDAO;
    }

    public ListDetailDAO getListDetailDAO() {
        return listDetailDAO;
    }

    public void setListDetailDAO(ListDetailDAO listDetailDAO) {
        this.listDetailDAO = listDetailDAO;
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

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public int getCount_seqNo() {
        return count_seqNo;
    }

    public void setCount_seqNo(int count_seqNo) {
        this.count_seqNo = count_seqNo;
    }

    public int getCount_seqNo2() {
        return count_seqNo2;
    }

    public void setCount_seqNo2(int count_seqNo2) {
        this.count_seqNo2 = count_seqNo2;
    }

    public List<Number> getOrderSeqNo() {
        return orderSeqNo;
    }

    public void setOrderSeqNo(List<Number> orderSeqNo) {
        this.orderSeqNo = orderSeqNo;
    }

    public List<ManageListValue> getListManageListValue() {
        return listManageListValue;
    }

    public void setListManageListValue(List<ManageListValue> listManageListValue) {
        this.listManageListValue = listManageListValue;
    }

    public void setManageList() {
        ManageListValueDAO dao = getManageListValueDAO();
        listManageListValue = dao.findManageListValueEntities();
        manageList = new SelectItem[listManageListValue.size()+1];
        int pos=0;
        manageList[pos++] = new SelectItem(null, "Select Group Name");
        for(ManageListValue ft : listManageListValue){
            manageList[pos++] = new SelectItem(ft.getId(), ft.getName()) ;
        }
    }
    
    public SelectItem[] getManageList() {
        return manageList;
    }

    public void setManageList(SelectItem[] manageList) {
        this.manageList = manageList;
    }
    
    public String getListSelected() {
        return listSelected;
    }

    public void setListSelected(String listSelected) {
        this.listSelected = listSelected;
    }

    public List<ListDetail> getListDetail() {
        return listDetail;
    }

    public void setListDetail(List<ListDetail> listDetail) {
        this.listDetail = listDetail;
    }

    public SelectItem[] getListOfDetailList() {
        return listOfDetailList;
    }

    public void setListOfDetailList(SelectItem[] listOfDetailList) {
        this.listOfDetailList = listOfDetailList;
    }
    
    public void setListOfDetailList(Integer listGroupId) {
        ListDetailDAO dao = getListDetailDAO();
        listDetail = dao.findListDetailByListGroupID(listGroupId);
        listOfDetailList = new SelectItem[listDetail.size()];
        int pos=0;
        for(ListDetail ft : listDetail){
            listOfDetailList[pos++] = new SelectItem(ft.getId(), ft.getCode()+"    "+ft.getName()+"    "+ft.getSeqNo()) ;
        }
        
    }
    
    public List<ListDetail> getList() {
        return getListDetail();
    }

    public Integer getListDetailId() {
        return listDetailId;
    }

    public void setListDetailId(Integer listDetailId) {
        this.listDetailId = listDetailId;
    }
    
    public void addAction()
    {
        messageDup = null;
        this.listDetailId = null;
        this.code= null;
        this.name = null;
        this.seqNo = null;
        isAdd = true;
    }
    
    public void editAction(Integer id,String code,String name,int seqNo)
    {
        messageDup = null;
        this.listDetailId = id;
        this.code= code;
        this.name = name;
        this.seqNo = seqNo;
        isAdd = true;
    }
    
    public void cancelAction()
    {
        messageDup = null;
        isAdd = false;
    }
    
//    public void deleteQuestionListener(ActionEvent event) {
//        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
//        qaQuestionList.remove(seqNo-1);
//        reCalculateSeqNo();
//    }
    
//    public String updateAction()
//    {
//        ListDetailDAO dao = getListDetailDAO();
//        singleListDetail = new ListDetail();
//        try {
//            
//            if(listDetailId != null)
//            {   singleListDetail = dao.findListDetail(listDetailId);
//                singleListDetail.setCode(code);
//                singleListDetail.setName(name);
//                singleListDetail.setSeqNo(seqNo);
//                dao.edit(singleListDetail);
//            }
//            else
//            {
//                singleListDetail.setCode(code);
//                singleListDetail.setName(name);
//                singleListDetail.setSeqNo(seqNo);
//                singleListDetail.setEnable(true);
//                dao.create(singleListDetail);
//            }
////              this.fileUploadBean.deleteDir(new java.io.File(this.rootPath+prod.getId()));
//            
//        } catch (Exception e) {
//            log.error(e);
//        }
//        return REFRESH;
//    }
    
    public void updateAction()
    {
        messageDup = null;
        ListDetailDAO dao = getListDetailDAO();
        singleListDetail = new ListDetail();
        Boolean isCodeDup = false;
        Boolean isSeqNoDup = false;
        try {
            
            if(listDetailId != null)
            {   
                for(int i=0;i<listDetail.size();i++)
                {
                    if(!(listDetail.get(i).getId().equals(listDetailId)))
                    {   if(listDetail.get(i).getCode().equals(code))
                        {
                            isCodeDup = true;
                        }
                    
                        if(listDetail.get(i).getSeqNo().equals(seqNo))
                        {
                            isSeqNoDup = true;
                        }
                    }
                }
                
                if(isCodeDup == true && isSeqNoDup == true)
                {
                    messageDup = "Code and Seq No are duplicate.";
                }
                else if(isCodeDup == true)
                {
                    messageDup = "Code is duplicate.";
                }
                else if(isSeqNoDup == true)
                {
                    messageDup = "Seq No is duplicate.";
                }
                else
                {
                    singleListDetail = dao.findListDetail(listDetailId);
                    singleListDetail.setName(name);
                    singleListDetail.setCode(code);
                    singleListDetail.setSeqNo(seqNo);
                    dao.edit(singleListDetail);
                    isAdd = false;
                    setListOfDetailList(Integer.parseInt(listSelected));
                }

            }
            else
            {
                
                for(int i=0;i<listDetail.size();i++)
                {
                        if(listDetail.get(i).getCode().equals(code))
                        {
                            isCodeDup = true;
                        }
                    
                        if(listDetail.get(i).getSeqNo().equals(seqNo))
                        {
                            isSeqNoDup = true;
                        }
                    
                }
                
                if(isCodeDup == true && isSeqNoDup == true)
                {
                    messageDup = "Code and Seq No are duplicate.";
                }
                else if(isCodeDup == true)
                {
                    messageDup = "Code is duplicate.";
                }
                else if(isSeqNoDup == true)
                {
                    messageDup = "Seq No is duplicate.";
                }
                else
                {
                    singleListDetail.setId(null);
                    singleListDetail.setCode(code);
                    singleListDetail.setName(name);
                    singleListDetail.setSeqNo(seqNo);
                    singleListDetail.setEnable(true);
                    singleListDetail.setListGroupId(Integer.parseInt(listSelected));
                    dao.create(singleListDetail);
                    isAdd = false;
                    setListOfDetailList(Integer.parseInt(listSelected));
                }
                FacesContext.getCurrentInstance().renderResponse();
                
            }
//              this.fileUploadBean.deleteDir(new java.io.File(this.rootPath+prod.getId()));
            
        } catch (Exception e) {
            log.error(e);
        }

    }

    public Boolean getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ListDetail getSingleListDetail() {
        return singleListDetail;
    }

    public void setSingleListDetail(ListDetail singleListDetail) {
        this.singleListDetail = singleListDetail;
    }
    
}
