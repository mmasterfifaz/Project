package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.admin.FileUploadBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class popupFileUpload {
    private FileUploadBean fileUploadBean;
    private Integer id;

    @PostConstruct
    public void initialize() {
         fileUploadBean = new FileUploadBean();
         fileUploadBean.setUploadsAvailable(1);
         fileUploadBean.setAutoUpload(true);
         fileUploadBean.setUseFlash(false);
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
}
