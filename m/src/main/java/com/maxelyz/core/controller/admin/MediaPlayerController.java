/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class MediaPlayerController {
    private static Logger log = Logger.getLogger(MediaPlayerController.class);

    private static String VIEW = "mediaplayer.xhtml";
    private String mediaPath;
    private String id;
    
    @PostConstruct
    public void initialize() {
        id = JSFUtil.getRequestParameterMap("trackId");
        mediaPath = JSFUtil.getRequestParameterMap("mediaPath");    
    }

    public String viewAction(){

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

}
