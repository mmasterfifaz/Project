/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class CompanyLogoEditController {
    private static Logger log = Logger.getLogger(CompanyLogoEditController.class);
    private static String SUCCESS = "companylogo.xhtml?faces-redirect=true";
    private static String FAILURE = "companylogoedit.xhtml";
    private String companylogo = "companylogo/";
    private static File dataFile;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:companylogo:view")) {
            SecurityUtil.redirectUnauthorize();
        }
    }
    
    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("admin:companylogo:edit");
    }
    
    public String saveAction() {
        return SUCCESS;
    }
    
    public void uploadCompleteListener(FileUploadEvent event) {
        try {
            UploadedFile item = event.getUploadedFile();

            String fileName = "logo.png";
            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            if(!fileName.isEmpty() && fileName.indexOf(",") != -1)
                fileName = fileName.replace(",", "");
            
            if(item != null){
                this.dataFile = new File(JSFUtil.getuploadPath() + companylogo + fileName);
                clearFileUpload();
                this.copy(item.getInputStream(), this.dataFile);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    
    public String backAction() {
        return SUCCESS;
    }

    public static File getDataFile() {
        return dataFile;
    }

    public static void setDataFile(File dataFile) {
        CompanyLogoEditController.dataFile = dataFile;
    }

}
