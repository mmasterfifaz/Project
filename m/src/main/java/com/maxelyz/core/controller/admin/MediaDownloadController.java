/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class MediaDownloadController {

    private static Logger log = Logger.getLogger(MediaDownloadController.class);

    private static String VIEW = "mediadownload.xhtml";
    private String mediaPath;
    private String id;
    private Integer contactHistoryId;
    
    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;

    @PostConstruct
    public void initialize() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String createVoiceDate = "";
        contactHistoryId = 0;
        id = JSFUtil.getRequestParameterMap("trackId");
        mediaPath = JSFUtil.getRequestParameterMap("mediaPath");
        
    }

    private String getURLVoice(int trackId, String createVoiceDate) {
        String url = "";
        com.maxelyz.webservice.VoiceConvert conv = new com.maxelyz.webservice.VoiceConvert();
        com.maxelyz.webservice.IVoiceConvert port = conv.getBasicHttpBindingIVoiceConvert();
        url = port.getMp3DownloadLink(trackId, createVoiceDate);
        return url;
    }

    //Listener
    public void downloadListener(ActionEvent event) {
        try {
            URL u = new URL(mediaPath);
            URLConnection uc = u.openConnection();
            int contentLength = uc.getContentLength();
            InputStream fis = u.openStream();
            byte[] buf = new byte[contentLength];
            int offset = 0;
            int numRead = 0;
            while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length - offset)) >= 0)) {
                offset += numRead;
            }
            fis.close();
            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + id + ".mp3");
            response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            
            /*URL u = new URL("http://192.168.50.65:81/2015/2015-01/2015-01-05/42779.mp3");
            URLConnection uc = u.openConnection();
            String contentType = uc.getContentType();
            int contentLength = uc.getContentLength();
            if (contentType.startsWith("text/") || contentLength == -1) {
                throw new IOException("This is not a binary file.");
            }
            InputStream raw = uc.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;
            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }
            in.close();

            if (offset != contentLength) {
                throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
            }

            String filename = id + ".mp3";
            FileOutputStream out = new FileOutputStream(filename);
            out.write(data);
            out.flush();
            out.close();*/
            
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MediaDownloadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String viewAction() {

        return VIEW;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getContactHistoryId() {
        return contactHistoryId;
    }

    public void setContactHistoryId(Integer contactHistoryId) {
        this.contactHistoryId = contactHistoryId;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

}
