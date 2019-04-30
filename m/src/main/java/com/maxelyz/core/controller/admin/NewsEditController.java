/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.NewsDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.News;
import com.maxelyz.core.model.entity.NewsUser;
import com.maxelyz.core.model.entity.NewsUserPK;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author admin
 */
@ManagedBean (name="newsEditController")
//@RequestScoped
@ViewScoped
public class NewsEditController implements Serializable {

    private static Log log = LogFactory.getLog(NewsEditController.class);
    private static String NEWS_EDIT = "newsedit.xhtml";
    private static String NEWS = "news.xhtml?faces-redirect=true";

    private News news;
    private String messageDup;
    private String message;
    //Delete Picture
    private SelectItem[] deletePicThumbnail;
    private String[] deletePicThumbnailString;
    private SelectItem[] deletePicLarge;
    private String[] deletePicLargeString;

    //FileUpload
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "news");
    private FileUploadBean fileUploadBeanLarge = new FileUploadBean(JSFUtil.getuploadPath(), "news");

    private String selectingUser;
    private int userGroupId;
    private Map<Integer, Boolean> selectedUserIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Users> selectedUser;
    
    @ManagedProperty(value = "#{newsDAO}")
    private NewsDAO newsDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:news:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        messageDup = "";
        if (JSFUtil.getRequestParameterMap("id") != null) {
            Integer id = Integer.parseInt(JSFUtil.getRequestParameterMap("id"));
            news = newsDAO.findNews(id);
            
            List<NewsUser> users = (List)news.getNewsUserCollection();
            selectedUser = new ArrayList<Users>();
            for(NewsUser u : users) {
                selectedUser.add(u.getUsers());
                selectedUserIds.put(u.getUsers().getId(), Boolean.TRUE);
            }
        } else {
            news = new News();
            news.setTransDate(new Date());
            news.setEnable(true);
            news.setStatus(Boolean.TRUE);
        }
    }

    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:news:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:news:edit"); 
       }
    }
        
    public String saveAction() {
        message = "";
        messageDup = "";
        if(checkTitle(news)) {
            if(news.getStartDate().before(news.getEndDate())) {
                try{
                    news.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    news.setUpdateDate(new Date());
                    if(news.getId() != null && news.getId() != 0){
                        //picture thumbnail
                        if (this.getDeletePicThumbnailString() == null) {
                            if (this.fileUploadBean.getFiles().size() > 0) {
                                this.fileUploadBean.createDirName(news.getId().toString());
                                this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0),this.fileUploadBean.getFolderPath());
                                news.setPicThumbnail(this.fileUploadBean.getFiles().get(0).getName());
                            }
                        } else if (this.getDeletePicThumbnailString().length > 0) {
                            news.setPicThumbnail(null);
                        }

                        //picture large
                        if (this.getDeletePicLargeString() == null) {
                            if (this.fileUploadBeanLarge.getFiles().size() > 0) {
                                this.fileUploadBeanLarge.createDirName(news.getId().toString());
                                this.fileUploadBeanLarge.moveFile(this.fileUploadBeanLarge.getFiles().get(0),this.fileUploadBeanLarge.getFolderPath());
                                news.setPicLarge(this.fileUploadBeanLarge.getFiles().get(0).getName());
                            }
                        } else if (this.getDeletePicLargeString().length > 0) {
                            news.setPicLarge(null);
                        }
                        newsDAO.edit(news);
                    }else{
                        news.setId(null);
                        news.setCreateByUser(JSFUtil.getUserSession().getUsers());
                        news.setCreateBy(JSFUtil.getUserSession().getUserName());
                        news.setCreateDate(new Date());
                        //picture thumbnail
                        if (this.fileUploadBean.getFiles().size() > 0) {
                            news.setPicThumbnail(this.fileUploadBean.getFiles().get(0).getName());
                        } else {
                            news.setPicThumbnail(null);
                        }
                        //picture large
                        if (this.fileUploadBeanLarge.getFiles().size() > 0) {
                            news.setPicLarge(this.fileUploadBeanLarge.getFiles().get(0).getName());
                        } else {
                            news.setPicLarge(null);
                        }

                        //save user
                        newsDAO.create(news);
                        List<NewsUser> users = new ArrayList<NewsUser>();
                        if (selectedUser != null && selectedUserIds != null) {
                            for (Users dataItem : selectedUser) {
                                NewsUser u = new NewsUser();
                                NewsUserPK uPK = new NewsUserPK();
                                if (selectedUserIds.get(dataItem.getId()).booleanValue()) {
                                    uPK.setNewsId(news.getId());
                                    uPK.setUsersId(dataItem.getId());
                                    u.setNewsUserPK(uPK);
                                    u.setViewStatus(Boolean.FALSE);
                                    users.add(u);
                                }
                            }
                        }
                        news.setNewsUserCollection(users);
                        newsDAO.edit(news);

                        if (this.fileUploadBean.getFiles().size() > 0) {
                            this.fileUploadBean.createDirName(news.getId().toString());
                            this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0),this.fileUploadBean.getFolderPath());
                        }
                        if (this.fileUploadBeanLarge.getFiles().size() > 0) {
                            this.fileUploadBeanLarge.createDirName(news.getId().toString());
                            this.fileUploadBeanLarge.moveFile(this.fileUploadBeanLarge.getFiles().get(0),this.fileUploadBeanLarge.getFolderPath());
                        }
                    }
                }catch(Exception e){
                    log.error(e);
                }
                return NEWS;
            } else {
                message=" Display Date From over Date To";
                return null;
            }
        } else {
            messageDup = " Title is already taken";
            return null;
        }
    }
        
    public Boolean checkTitle(News news) {
        String title = news.getTitle();
        Integer id=0; 
        if(news.getId() != null)
            id = news.getId();
        NewsDAO dao = getNewsDAO();
        
        Integer cnt = dao.checkTitleName(title, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public void deletePicThumbnailListener() {
        news.setPicThumbnail(null);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void deletePicLargeListener() {
        news.setPicLarge(null);
        FacesContext.getCurrentInstance().renderResponse();
    }
        
    public String backAction(){
        return NEWS;
    }
 
    public void userSelectingChangeListener() {
        userGroupId=0;
        if (selectingUser.equals("all")) {
                selectedUser = this.getUsersDAO().findAgentUnderParentId(JSFUtil.getUserSession().getUsers());
        } else if(selectingUser.equals("group")){
                if(selectedUser != null)
                    selectedUser.clear();
                getUserGroupList();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void userGroupChangeListener() {
        if (userGroupId == 0) {
            selectedUser = null;
        } else {
            selectedUser = this.getUsersDAO().findUsersEntitiesByUserGroup(userGroupId);
        }
    }

    public Map<String, Integer> getUserGroupList() {
        return getUserGroupDAO().getUserGroupListByAgent();
//        return getUserGroupDAO().getUserGroupListUnderParent(JSFUtil.getUserSession().getUsers());
    }

    public Map<Integer, Boolean> getSelectedUserIds() {
        return selectedUserIds;
    }

    public void setSelectedUserIds(Map<Integer, Boolean> selectedUserIds) {
        this.selectedUserIds = selectedUserIds;
    }
        
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public NewsDAO getNewsDAO() {
        return newsDAO;
    }

    public void setNewsDAO(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    /**
     * @return the deleteThumbnail
     */
    public SelectItem[] getDeletePicThumbnail() {
        deletePicThumbnail = new SelectItem[1];
        deletePicThumbnail[0] = new SelectItem("D", "Delete Thumbnail");
        return deletePicThumbnail;
    }

    /**
     * @param deleteThumbnail the deleteThumbnail to set
     */
    public void setDeletePicThumbnail(SelectItem[] deletePicThumbnail) {
        this.deletePicThumbnail = deletePicThumbnail;
    }

    /**
     * @return the deleteThumbnailString
     */
    public String[] getDeletePicThumbnailString() {
        return deletePicThumbnailString;
    }

    /**
     * @param deleteThumbnailString the deleteThumbnailString to set
     */
    public void setDeletePicThumbnailString(String[] deletePicThumbnailString) {
        this.deletePicThumbnailString = deletePicThumbnailString;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public SelectItem[] getDeletePicLarge() {
        deletePicLarge = new SelectItem[1];
        deletePicLarge[0] = new SelectItem("D", "Delete Large Picture");
        return deletePicLarge;
    }

    public void setDeletePicLarge(SelectItem[] deletePicLarge) {
        this.deletePicLarge = deletePicLarge;
    }

    public String[] getDeletePicLargeString() {
        return deletePicLargeString;
    }

    public void setDeletePicLargeString(String[] deletePicLargeString) {
        this.deletePicLargeString = deletePicLargeString;
    }

    public FileUploadBean getFileUploadBeanLarge() {
        return fileUploadBeanLarge;
    }

    public void setFileUploadBeanLarge(FileUploadBean fileUploadBeanLarge) {
        this.fileUploadBeanLarge = fileUploadBeanLarge;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public String getSelectingUser() {
        return selectingUser;
    }

    public void setSelectingUser(String selectingUser) {
        this.selectingUser = selectingUser;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public List<Users> getUserList() {
        return selectedUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
