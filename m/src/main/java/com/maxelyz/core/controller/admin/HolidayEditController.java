package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.HolidayDAO;
import com.maxelyz.core.model.entity.Holiday;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class HolidayEditController {
    private static Logger log = Logger.getLogger(HolidayEditController.class);
    private static String REDIRECT_PAGE = "holiday.jsf";
    private static String SUCCESS = "holiday.xhtml?faces-redirect=true";
    private static String FAILURE = "holidayedit.xhtml";

    private Holiday holiday;
    private String mode;

    @ManagedProperty(value="#{holidayDAO}")
    private HolidayDAO holidayDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:holiday:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           holiday = new Holiday();
       } else {
           mode = "edit";
           HolidayDAO dao = getHolidayDAO();
           holiday = dao.findHoliday(new Integer(selectedID));
           if (holiday==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

    public boolean isSavePermitted() {
   	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:holiday:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:holiday:edit"); 
       }
    } 
          
    public String saveAction() {
        HolidayDAO dao = getHolidayDAO();
        try {
            if (getMode().equals("add")) {
                holiday.setId(null);//fix some bug
                holiday.setEnable(true);
                dao.create(holiday);
            } else {
                holiday.setEnable(true);
                dao.edit(holiday);
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public HolidayDAO getHolidayDAO() {
        return holidayDAO;
    }

    public void setHolidayDAO(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }


}
