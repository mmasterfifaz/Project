/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

/**
 * @author Ilya Shaikovsky
 *
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class TestUploadController{

    private String fileName;
    private String extName;
    private FileUploadBean1 fileUploadBean;
    
    @PostConstruct
    public void initialize() {
        fileUploadBean = new FileUploadBean1();
    }

    public TestUploadController() {

    }

//    public void listener(UploadEvent event) throws Exception{
//        Date date = new Date();
//        UploadItem item = event.getUploadItem();
//        setExtName(item.getFileName());
//        setFileName(date.getTime() + "." + extName);
//        java.io.File file = new java.io.File("D:\\Development\\Maxar\\build\\web\\upload\\temp\\" + fileName);
//        item.getFile().renameTo(file);
//    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String strFileName) {
        String str = "";
        int extDot = strFileName.lastIndexOf('.');
        if(extDot > 0){
            str = strFileName.substring(extDot +1);
        }
        this.extName = str;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileUploadBean1 getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean1 fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

}