/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CardExclusionDAO;
import com.maxelyz.core.model.entity.CardExclusion;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@ViewScoped
public class CardExclusionEditController {

    private static Logger log = Logger.getLogger(CardExclusionEditController.class);
    private static String REDIRECT_PAGE = "cardexclusion.jsf";
    private static String SUCCESS = "cardexclusion.xhtml?faces-redirect=true";
    private static String FAILURE = "cardexclusionedit.xhtml";

    private CardExclusion cardExclusion;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value = "#{cardExclusionDAO}")
    private CardExclusionDAO cardExclusionDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:cardexclusion:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            cardExclusion = new CardExclusion();
            cardExclusion.setEnable(Boolean.TRUE);

        } else {
            mode = "edit";
            CardExclusionDAO dao = cardExclusionDAO;
            cardExclusion = dao.findCardExclusion(new Integer(selectedID));
            if (cardExclusion == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:cardexclusion:add");
        } else {
            return SecurityUtil.isPermitted("admin:cardexclusion:edit");
        }
    }

    public String saveAction() {
        messageDup = "";
        if (checkCode(cardExclusion)) {
            CardExclusionDAO dao = cardExclusionDAO;
            try {
                if (getMode().equals("add")) {
                    cardExclusion.setId(null);
                    cardExclusion.setEnable(true);
                    cardExclusion.setCreateBy(JSFUtil.getUserSession().getUserName());
                    cardExclusion.setCreateDate(new Date());
                    dao.create(cardExclusion);
                } else {
                    cardExclusion.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    cardExclusion.setUpdateDate(new Date());
                    dao.edit(cardExclusion);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = "Name is already taken";
            return FAILURE;
        }
    }

    public Boolean checkCode(CardExclusion cardExclusion) {
        String topic = cardExclusion.getName();
        Integer id = 0;
        if (cardExclusion.getId() != null) {
            id = cardExclusion.getId();

            Integer cnt = cardExclusionDAO.checkCardExclusionName(topic, id);
            if (cnt == 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public String backAction() {
        return SUCCESS;
    }

    public CardExclusion getCardExclusion() {
        return cardExclusion;
    }

    public void setCardExclusion(CardExclusion cardExclusion) {
        this.cardExclusion = cardExclusion;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CardExclusionDAO getCardExclusionDAO() {
        return cardExclusionDAO;
    }

    public void setCardExclusionDAO(CardExclusionDAO cardExclusionDAO) {
        this.cardExclusionDAO = cardExclusionDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
