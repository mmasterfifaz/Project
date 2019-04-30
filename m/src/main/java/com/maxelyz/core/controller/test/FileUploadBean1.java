/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
//
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

/**
 * @author Ilya Shaikovsky
 *
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class FileUploadBean1{

    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private String ext;
    private String fileName;
    public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        }else
        {
            return 0;
        }
    }

    public FileUploadBean1() {
    }

    public void paint(OutputStream stream, Object object) throws IOException {
//        stream.write(getFiles().get((Integer)object).getData());
    }
//    public void listener(UploadEvent event) throws Exception{
//        Date date = new Date();
//        UploadItem item = event.getUploadItem();
//        setExt(item.getFileName());
//        setFileName(date.getTime() + "." + ext);
//        java.io.File file = new java.io.File("D:\\Development\\Maxar\\build\\web\\upload\\temp\\" + fileName);
//        item.getFile().renameTo(file);
//    }

    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(5);
        return null;
    }

    public long getTimeStamp(){
        return System.currentTimeMillis();
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String fileName) {
        String str = "";
        int extDot = fileName.lastIndexOf('.');
        if(extDot > 0){
            str = fileName.substring(extDot +1);
        }
        this.ext = str;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
