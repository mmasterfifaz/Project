package com.maxelyz.core.controller.admin;

/**
 *
 * @author Administrator
 */
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * @author Ilya Shaikovsky
 *
 */
public class FileUploadBean {

    private static Log log = LogFactory.getLog(FileUploadBean.class);
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 3;
    private boolean autoUpload = true;
    private boolean useFlash = false;
    private String fileName;
    private String dirName;
    private String rootPath;
    private String absolutePath;
    private String folderPath = "";

    public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }

    public FileUploadBean() {
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        // stream.write(getFiles().get((Integer)object).getData());
    }

//    public void listener(UploadEvent event) throws Exception {
//        UploadItem item = event.getUploadItem();
//
//        //RandomAccessFile file = new RandomAccessFile("01.jpg", "rw");
//        //file.setLength(item.getFileSize());
//        setFileName(item.getFileName());
//        File file = new java.io.File(getAbsolutePath());
//        item.getFile().renameTo(file);
//        this.fileName = file.getName();
//        this.files.add(file);
//        //log.info("Upload:"+file.getAbsolutePath());
//        //file.setName(item.getFileName());
//        //file.setData(item.getData());
//        uploadsAvailable--;
//    }

    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(1);
        return null;
    }

    public long getTimeStamp() {
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

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the dirName
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * @param dirName the dirName to set
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     * @return the rootPath
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * @param rootPath the rootPath to set
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return the absolutePath
     */
    public String getAbsolutePath() {
        absolutePath = this.getRootPath() + this.getDirName() + "\\" + getFileName();
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
        dirName = this.getRootPath() + this.getDirName() + "\\" + dirName;
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
}
