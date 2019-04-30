/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BusinessUnitDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
//@RequestScoped
@ViewScoped
public class KnowledgebaseVersionController {

    private static Logger log = Logger.getLogger(KnowledgebaseVersionController.class);
    private KnowledgebaseDivision knowledgebaseDivision;
    private KnowledgebaseDivisionPK knowledgebaseDivisionPK;
    private List<KnowledgebaseAttfile> knowledgebaseAttfileList;
    private List<Knowledgebase> relateknowledgebases;
    private List<KnowledgebaseBoard> knowledgebasesboard;
    private List<KnowledgebaseUrl> knowledgebasesUrlList;
    private static String VIEWVERSION = "knowledgebaseversion.xhtml";
    private Long vote;
    private int displayvote;
    private Users users;
    private UserGroup usergroup; 
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO; 
    @PostConstruct
    public void initialize() {

        String sid = null;
        String sver = null;

        try {
            sid = (String) JSFUtil.getRequestParameterMap("sid");
            sver = (String) JSFUtil.getRequestParameterMap("sver");

            KnowledgebaseDAO dao = getKnowledgebaseDAO();

            knowledgebaseDivision = dao.findKnowledgebaseDivision(Integer.parseInt(sid), sver);
            users = usersDAO.findUsers(knowledgebaseDivision.getContentown());
             
            relateknowledgebases = new ArrayList<Knowledgebase>();
            List<Knowledgebase> nlist = dao.findKnowledgebaseRelateId(knowledgebaseDivision.getKnowledgebaseDivisionPK().getKnowledgebaseId(), knowledgebaseDivision.getKnowledgebaseDivisionPK().getVersion());
            for (Knowledgebase n : nlist) {
                relateknowledgebases.add(n);
            }
            knowledgebasesboard = new ArrayList<KnowledgebaseBoard>();
            List<KnowledgebaseBoard> blist = dao.findKnowledgebaseBoardById(knowledgebaseDivision.getKnowledgebaseDivisionPK().getKnowledgebaseId(), knowledgebaseDivision.getKnowledgebaseDivisionPK().getVersion());
            int i = 0;
            for (KnowledgebaseBoard b : blist) {
                b.setNo(blist.size() - i);
                knowledgebasesboard.add(b);
                i++;
            }

            knowledgebasesUrlList = new ArrayList<KnowledgebaseUrl>();
            List<KnowledgebaseUrl> list = this.getKnowledgebaseDAO().findKnowledgebaseUrlById(knowledgebaseDivision.getKnowledgebaseDivisionPK().getKnowledgebaseId(), knowledgebaseDivision.getKnowledgebaseDivisionPK().getVersion());


            for (KnowledgebaseUrl s : list) {
                knowledgebasesUrlList.add(s);
            }


            fillKbAttFileList();
            loadVate();
        } catch (NullPointerException e) {
            log.error(e);
        }



    }
 
    public UserGroup getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(UserGroup usergroup) {
        this.usergroup = usergroup;
    }

    
    public List<KnowledgebaseUrl> getKnowledgebasesUrlList() {
        return knowledgebasesUrlList;
    }

    public void setKnowledgebasesUrlList(List<KnowledgebaseUrl> knowledgebasesUrlList) {
        this.knowledgebasesUrlList = knowledgebasesUrlList;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }
 

    public KnowledgebaseDivision getKnowledgebaseDivision() {
        return knowledgebaseDivision;
    }

    public void setKnowledgebaseDivision(KnowledgebaseDivision knowledgebaseDivision) {
        this.knowledgebaseDivision = knowledgebaseDivision;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public void listener(ActionEvent event) {
        initialize();
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }

    public int getDisplayvote() {
        return displayvote;
    }

    public void setDisplayvote(int displayvote) {
        this.displayvote = displayvote;
    }

    public List<KnowledgebaseAttfile> getKnowledgebaseAttfileList() {
        return knowledgebaseAttfileList;
    }

    public void setKnowledgebaseAttfileList(List<KnowledgebaseAttfile> knowledgebaseAttfileList) {
        this.knowledgebaseAttfileList = knowledgebaseAttfileList;
    }

    public List<KnowledgebaseBoard> getKnowledgebasesboard() {
        return knowledgebasesboard;
    }

    public void setKnowledgebasesboard(List<KnowledgebaseBoard> knowledgebasesboard) {
        this.knowledgebasesboard = knowledgebasesboard;
    }

    public List<Knowledgebase> getRelateknowledgebases() {
        return relateknowledgebases;
    }

    public void setRelateknowledgebases(List<Knowledgebase> relateknowledgebases) {
        this.relateknowledgebases = relateknowledgebases;
    }

    public String viewAction() {
        return VIEWVERSION;
    }

    private void fillKbAttFileList() {
        try {
            knowledgebaseAttfileList = new ArrayList<KnowledgebaseAttfile>();
            if (knowledgebaseDivision.getKnowledgebaseDivisionPK() != null) {
                List<KnowledgebaseAttfile> list = this.getKnowledgebaseDAO().findKnowledgebaseAttFileById(knowledgebaseDivision.getKnowledgebaseDivisionPK().getKnowledgebaseId());


                for (KnowledgebaseAttfile s : list) {
                    knowledgebaseAttfileList.add(s);
                }

            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void loadVate() {
        try {
            vote = new Long(0);
            displayvote = new Integer(0);
            displayvote = this.getKnowledgebaseDAO().getKnowledgebaseVoteAverage(knowledgebaseDivision.getKnowledgebaseDivisionPK().getKnowledgebaseId(), knowledgebaseDivision.getKnowledgebaseDivisionPK().getVersion());

        } catch (Exception e) {
            log.error(e);
        }
    }
}
