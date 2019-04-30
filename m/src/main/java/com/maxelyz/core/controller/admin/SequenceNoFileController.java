package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.SequenceNoDAO;
import com.maxelyz.core.model.dao.SequenceNoFileDAO;
import com.maxelyz.core.model.dao.SequenceNoFileDetailDAO;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.core.model.entity.SequenceNoFile;
import com.maxelyz.core.model.entity.SequenceNoFileDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by support on 11/10/2017.
 */
@ManagedBean
@ViewScoped
public class SequenceNoFileController implements Serializable {
    private static Logger log = Logger.getLogger(SequenceNumberController.class);
    private static String REFRESH = "sequencenofile.xhtml?faces-redirect=true";
    private static String EDIT = "sequencenofileedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedSequenceNoFileDetailId = new ConcurrentHashMap<Integer, Boolean>();
    private List<SequenceNoFile> sequenceNoFileList;
    private SequenceNoFile sequenceNoFile;

    private Integer selectSequenceId;
    private List<SequenceNo> sequenceNoList;

    private Integer total =0;
    private Integer available=0;
    private List<SequenceNoFileDetail> sequenceNoFileDetailList;
    private SequenceNoFileDetail sequenceNoFileDetail;

    private Integer totalSequenceDetail;
    private Integer availableSequenceDetail;
    private Integer cancelSequenceDetail;
    private Integer usedSequenceDetail;

    private String messageDup;
    private String disableMassageDup;

    private Integer sequenceNoFileId;
    private Integer sequenceId;
    private Boolean checked =false;
    private String codeName;

    @ManagedProperty(value = "#{sequenceNoFileDAO}")
    private SequenceNoFileDAO sequenceNoFileDAO;
    @ManagedProperty(value = "#{sequenceNoDAO}")
    private SequenceNoDAO sequenceNoDAO;
    @ManagedProperty(value = "#{sequenceNoFileDetailDAO}")
    private SequenceNoFileDetailDAO sequenceNoFileDetailDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:sequencenumberfile:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        sequenceNoList = getSequenceNoDAO().findSequenceNoByGenerateType();
        if(JSFUtil.getRequestParameterMap("selectSequence")!= null && JSFUtil.getRequestParameterMap("selectSequence")!=""){
            sequenceId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectSequence"));
            selectSequenceId = sequenceId;
            sequenceNoFileList = this.getSequenceNoFileDAO().findSequenceNoFileBySequenceNoId(new Integer(sequenceId));
            total = this.getSequenceNoFileDAO().findTotalRecordBySequenceNoId(new Integer(sequenceId));
            available = this.getSequenceNoFileDAO().findAvailableBySequenceNoId(new Integer(sequenceId));
        }
    }
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumberfile:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumberfile:delete");
    }

    public String deleteAction() throws Exception {
        SequenceNoFileDAO dao = getSequenceNoFileDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    sequenceNoFile = dao.findSequenceNoFIle(item.getKey());
                    sequenceNoFile.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    sequenceNoFile.setUpdateDate(new Date());
                    sequenceNoFile.setEnable(false);
                    dao.edit(sequenceNoFile);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH + "&selectSequence="+selectSequenceId;
    }

    public void cancelAction() throws Exception {
        disableMassageDup = "";
        checked =false;
        SequenceNoFileDetailDAO dao = getSequenceNoFileDetailDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedSequenceNoFileDetailId.entrySet()) {
                if (item.getValue()) {
                    sequenceNoFileDetail = dao.findSequenceNoFileDetail(item.getKey());
                    sequenceNoFileDetail.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    sequenceNoFileDetail.setUpdateDate(new Date());
                    sequenceNoFileDetail.setStatus("Cancel");
                    dao.edit(sequenceNoFileDetail);
                    checked = true;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        if(checked.equals(false)) {
            disableMassageDup = "Please select checkbox first";
        }else{
            disableMassageDup = "";
            selectedSequenceNoFileDetailId = new ConcurrentHashMap<Integer, Boolean>();
            detailAction(sequenceNoFileId,codeName);
        }
    }

    public void detailAction(Integer sequenceNoFile,String code){
        codeName = code;
        this.sequenceNoFileId =sequenceNoFile;
        disableMassageDup ="";
        total = this.getSequenceNoFileDAO().findTotalRecordBySequenceNoId(new Integer(sequenceId));
        available = this.getSequenceNoFileDAO().findAvailableBySequenceNoId(new Integer(sequenceId));
        sequenceNoFileDetailList = getSequenceNoFileDetailDAO().findSequenceNoDetailBySeauenceNoFileId(sequenceNoFileId);
        totalSequenceDetail = this.getSequenceNoFileDAO().findTotalRecordBySequenceNoFileId(sequenceNoFileId);
        usedSequenceDetail = this.getSequenceNoFileDAO().findUsedRecordBySequenceNoFileId(sequenceNoFileId);
        availableSequenceDetail = this.getSequenceNoFileDAO().findAvailableRecordBySequenceNoFileId(sequenceNoFileId);
        cancelSequenceDetail = this.getSequenceNoFileDAO().findCancelRecordBySequenceNoFileId(sequenceNoFileId);
    }

    public void sequenceChangeListener(ValueChangeEvent event){
        sequenceId = (Integer) event.getNewValue();
        sequenceNoFileList = this.getSequenceNoFileDAO().findSequenceNoFileBySequenceNoId(new Integer(sequenceId));
        if(sequenceId == -1){
            messageDup ="Please select";
        }else if(sequenceNoFileList.size()<1){
            messageDup ="No result. Please add data this type first ";
        }else{
            messageDup="";
        }
        total = this.getSequenceNoFileDAO().findTotalRecordBySequenceNoId(new Integer(sequenceId));
        available = this.getSequenceNoFileDAO().findAvailableBySequenceNoId(new Integer(sequenceId));
    }

    public String editAction() {
        return EDIT;
    }

    public SequenceNoFileDAO getSequenceNoFileDAO() {
        return sequenceNoFileDAO;
    }

    public void setSequenceNoFileDAO(SequenceNoFileDAO sequenceNoFileDAO) {
        this.sequenceNoFileDAO = sequenceNoFileDAO;
    }

    public SequenceNoFile getSequenceNoFile() {
        return sequenceNoFile;
    }

    public void setSequenceNoFile(SequenceNoFile sequenceNoFile) {
        this.sequenceNoFile = sequenceNoFile;
    }

    public List<SequenceNoFile> getSequenceNoFileList() {
        return sequenceNoFileList;
    }

    public void setSequenceNoFileList(List<SequenceNoFile> sequenceNoFileList) {
        this.sequenceNoFileList = sequenceNoFileList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SequenceNo> getSequenceNoList() {
        return sequenceNoList;
    }

    public void setSequenceNoList(List<SequenceNo> sequenceNoList) {
        this.sequenceNoList = sequenceNoList;
    }

    public Integer getSelectSequenceId() {
        return selectSequenceId;
    }

    public void setSelectSequenceId(Integer selectSequenceId) {
        this.selectSequenceId = selectSequenceId;
    }

    public SequenceNoDAO getSequenceNoDAO() {
        return sequenceNoDAO;
    }

    public void setSequenceNoDAO(SequenceNoDAO sequenceNoDAO) {
        this.sequenceNoDAO = sequenceNoDAO;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public SequenceNoFileDetailDAO getSequenceNoFileDetailDAO() {
        return sequenceNoFileDetailDAO;
    }

    public void setSequenceNoFileDetailDAO(SequenceNoFileDetailDAO sequenceNoFileDetailDAO) {
        this.sequenceNoFileDetailDAO = sequenceNoFileDetailDAO;
    }

    public List<SequenceNoFileDetail> getSequenceNoFileDetailList() {
        return sequenceNoFileDetailList;
    }

    public void setSequenceNoFileDetailList(List<SequenceNoFileDetail> sequenceNoFileDetailList) {
        this.sequenceNoFileDetailList = sequenceNoFileDetailList;
    }

    public Map<Integer, Boolean> getSelectedSequenceNoFileDetailId() {
        return selectedSequenceNoFileDetailId;
    }

    public void setSelectedSequenceNoFileDetailId(Map<Integer, Boolean> selectedSequenceNoFileDetailId) {
        this.selectedSequenceNoFileDetailId = selectedSequenceNoFileDetailId;
    }

    public SequenceNoFileDetail getSequenceNoFileDetail() {
        return sequenceNoFileDetail;
    }

    public void setSequenceNoFileDetail(SequenceNoFileDetail sequenceNoFileDetail) {
        this.sequenceNoFileDetail = sequenceNoFileDetail;
    }

    public Integer getTotalSequenceDetail() {
        return totalSequenceDetail;
    }

    public void setTotalSequenceDetail(Integer totalSequenceDetail) {
        this.totalSequenceDetail = totalSequenceDetail;
    }

    public Integer getAvailableSequenceDetail() {
        return availableSequenceDetail;
    }

    public void setAvailableSequenceDetail(Integer availableSequenceDetail) {
        this.availableSequenceDetail = availableSequenceDetail;
    }

    public Integer getCancelSequenceDetail() {
        return cancelSequenceDetail;
    }

    public void setCancelSequenceDetail(Integer cancelSequenceDetail) {
        this.cancelSequenceDetail = cancelSequenceDetail;
    }

    public Integer getUsedSequenceDetail() {
        return usedSequenceDetail;
    }

    public void setUsedSequenceDetail(Integer usedSequenceDetail) {
        this.usedSequenceDetail = usedSequenceDetail;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getDisableMassageDup() {
        return disableMassageDup;
    }

    public void setDisableMassageDup(String disableMassageDup) {
        this.disableMassageDup = disableMassageDup;
    }

    public Integer getSequenceNoFileId() {
        return sequenceNoFileId;
    }

    public void setSequenceNoFileId(Integer sequenceNoFileId) {
        this.sequenceNoFileId = sequenceNoFileId;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
