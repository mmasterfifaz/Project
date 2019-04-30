package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.SequenceNoDAO;
import com.maxelyz.core.model.dao.SequenceNoFileDAO;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.core.model.entity.SequenceNoFile;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by support on 11/10/2017.
 */
@ManagedBean
@ViewScoped
public class SequenceNoFileEditController {
    private static Logger log = Logger.getLogger(SequenceNoFileEditController.class);
    private static String REDIRECT_PAGE = "followup.jsf";
    private static String SUCCESS = "sequencenofile.xhtml?faces-redirect=true";
    private static String FAILURE = "sequencenofileedit.xhtml";
    private SequenceNoFile sequenceNoFile;
    private String mode;

    private Integer selectSequenceId;
    private List<SequenceNo> sequenceNoList;
    private static File dataFile;
    private String selectedID;

    private Integer fileTemplateId;

    @ManagedProperty(value = "#{sequenceNoFileDAO}")
    private SequenceNoFileDAO sequenceNoFileDAO;

    @ManagedProperty(value = "#{sequenceNoDAO}")
    private SequenceNoDAO sequenceNoDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:sequencenumberfile:view")) {
            SecurityUtil.redirectUnauthorize();
        }
       selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            sequenceNoFile = new SequenceNoFile();
            sequenceNoFile.setEnable(Boolean.TRUE);
            sequenceNoFile.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            SequenceNoFileDAO dao = getSequenceNoFileDAO();
            sequenceNoFile = dao.findSequenceNoFIle(new Integer(selectedID));

            if (sequenceNoFile==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }

            if (sequenceNoFile.getSequenceNo() != null) {
                selectSequenceId = sequenceNoFile.getSequenceNo().getId();
            }
        }
        sequenceNoList = getSequenceNoDAO().findSequenceNoByGenerateType();
    }

    public String saveAction() {
            SequenceNoFileDAO dao = getSequenceNoFileDAO();
            try {

                if (selectSequenceId != null && selectSequenceId != 0) {
                    sequenceNoFile.setSequenceNo(new SequenceNo(selectSequenceId));
                } else {
                    sequenceNoFile.setSequenceNo(null);
                }

                for(SequenceNo sn :sequenceNoList){
                    if(sn.getId().equals(selectSequenceId)){
                        sequenceNoFile.setFileName(sn.getName());
                        break;
                    }
                }

                if (getMode().equals("add")) {
                    SimpleDateFormat dt = new SimpleDateFormat("YYYY-mm-dd hh:mm:ss", Locale.US);
                    String date = dt.format(new Date());
                    sequenceNoFile.setCode(date.replace("-","").replace(":","").replace(" ",""));
                    sequenceNoFile.setId(null);
                    sequenceNoFile.setCreateDate(new Date());
                    sequenceNoFile.setCreateBy(JSFUtil.getUserSession().getUserName());
                    dao.create(sequenceNoFile,dataFile);
                } else {
                    sequenceNoFile.setUpdateDate(new Date());
                    sequenceNoFile.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    dao.edit(sequenceNoFile);
                }
            } catch (Exception e) {
                log.error(e);
                return FAILURE;
            }
            return SUCCESS;
        }

    public void uploadCompleteListener(FileUploadEvent event) {
        Date date = new Date();
        try {
            UploadedFile item = event.getUploadedFile();

            String fileName = item.getName();
            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            if(!fileName.isEmpty() && fileName.indexOf(",") != -1)
                fileName = fileName.replace(",", "");

            this.dataFile = new File(JSFUtil.getuploadPath() + date.getTime() + "_" + fileName);
            this.copy(item.getInputStream(), this.dataFile);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void dropdownChangListener(ValueChangeEvent event){
        Integer selectID = (Integer) event.getNewValue();
        selectSequenceId = selectID;
    }

    private void copy(InputStream in, File file) {
        try {
            if(!file.exists()){
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFileUpload() {
        if(dataFile != null) {
            dataFile.delete();
        }
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:sequencenumberfile:add");
        } else {
            return SecurityUtil.isPermitted("admin:sequencenumberfile:edit");
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public Integer getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(Integer fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public String getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(String selectedID) {
        this.selectedID = selectedID;
    }
}
