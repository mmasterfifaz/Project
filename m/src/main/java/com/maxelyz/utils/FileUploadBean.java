package com.maxelyz.utils;

import java.util.ArrayList;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.richfaces.component.UIFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * @author Ilya Shaikovsky
 *
 */
public class FileUploadBean {

    private static Log log = LogFactory.getLog(FileUploadBean.class);
    private ArrayList<File> files = new ArrayList<File>();
    private String dirName;
    private String rootPath;
    private String absolutePath;
    private String folderPath = "";
//    private UIFileUpload fileUpload;    
//    private File tempFile;    
//    private UploadedFile uploadedFile;

    public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }

    public FileUploadBean(String rootPath) {
        this(rootPath, "");
    }

    public FileUploadBean(String rootPath, String subDirectoryName) {
        this.dirName = subDirectoryName;
        this.rootPath = rootPath;
        File file = new File(rootPath + File.separator + dirName);
        if(!file.exists()){
            file.mkdir();
        }
    }

//    public void listener(UploadEvent event) throws Exception {
//        UploadItem item = event.getUploadItem();
//        File sourceFile = item.getFile();
//
//        String fileName = item.getFileName();
//        int idx = fileName.lastIndexOf(File.separator);
//        int idx1 = fileName.lastIndexOf("\\");
//        if (idx!=-1) {
//            fileName = fileName.substring(idx,fileName.length());
//        }else if(idx1 != -1){
//            fileName = fileName.substring(idx1+1,fileName.length());
//        }
//        File targetFile = new File(this.getAbsolutePath(),  fileName);
//        sourceFile.renameTo(targetFile);
//        this.files.add(targetFile);
//
//    }

    public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();
        String fileName = item.getName();

        int idx = fileName.lastIndexOf(File.separator);
        int idx1 = fileName.lastIndexOf("\\");
        if (idx!=-1) {
            fileName = fileName.substring(idx,fileName.length());
        }else if(idx1 != -1){
            fileName = fileName.substring(idx1+1,fileName.length());
        }
        File f = new File(this.getAbsolutePath(), fileName);
        if(saveToFile(item.getInputStream(), f)) {                    
//            tempFile = f;
//            this.files.add(tempFile);
            this.files.add(f);
        }
//        this.files.add(f);

    }
    
    public void clearUploadData() {
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
            files.clear();
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return the absolutePath
     */
    public String getAbsolutePath() {
        absolutePath = this.getRootPath() + this.getDirName() + File.separator;
        return absolutePath;
    }

    /**
     * @param absolutePath the absolutePath to set
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean moveFile(File file, String to) {
        boolean success = file.renameTo(new File(to, file.getName()));
        return success;
    }

    public boolean createDirName(String dirName) {
        dirName = this.getRootPath() + this.getDirName() + File.separator + dirName + File.separator;
        boolean success = false;
        if (((new File(dirName)).exists()) == false) {
            success = (new File(dirName)).mkdir();
        }
        this.folderPath = dirName;
        return success;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    
//    private File prepareTempFile() {        
//        try {            
//            return File.createTempFile(System.currentTimeMillis() + "", "tmp");
//        } catch (IOException ex) {         
//            log.error("Could not prepare temp file "+ex);
//        }        
//        return null;    
//    }

    private boolean saveToFile(InputStream inputStream, File file) {    
        FileOutputStream fos = null;
        BufferedInputStream bis = null;   
        boolean result = false;        
        try { 
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);            
            bis = new BufferedInputStream(inputStream, buffer.length);            
            int numRead = -1;            
            while ((numRead = bis.read(buffer, 0, buffer.length)) != -1) {                
                fos.write(buffer, 0, numRead);
            }          
            result = true;        
        } catch (IOException ex) {            
            log.error(ex, ex);
            log.error("Exception during download file " + file.getAbsolutePath());        
        } finally {            
            try {                
                fos.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing file output stream " + file.getAbsolutePath());            
            }            
            try {                
                bis.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing buffered input stream " + file.getAbsolutePath());            
            }   
        }    
        return result; 
    }

//    public void listener(FileUploadEvent event) {        
//        uploadedFile = event.getUploadedFile();
//        File temp = prepareTempFile();   
//        if(temp != null && temp.exists()) {            
//            try {                
//                if(saveToFile(uploadedFile.getInputStream(), temp)) {                    
//                    tempFile = temp;
//                    this.files.add(tempFile);
//                }
//            } catch (IOException ex) {                
//                log.error(ex, ex);
//            } 
//        }
//    }

}
