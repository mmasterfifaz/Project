package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.HolidayDAO;
import com.maxelyz.core.model.entity.Holiday;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
public class HolidayController implements Serializable {

    private static Logger log = Logger.getLogger(HolidayController.class);
    private static String REFRESH = "holiday.xhtml?faces-redirect=true";
    private static String EDIT = "holidayedit.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<Holiday> holidays;
    private String keyword;
    private Holiday holiday;
    @ManagedProperty(value = "#{holidayDAO}")
    private HolidayDAO holidayDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:holiday:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        HolidayDAO dao = getHolidayDAO();
        holidays = dao.findHolidayEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:holiday:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:holiday:delete");
    }
    
    public List<Holiday> getList() {
        return getHolidays();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        HolidayDAO dao = getHolidayDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    holiday = dao.findHoliday(item.getKey());
                    holiday.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    holiday.setUpdateDate(new Date());
                    holiday.setEnable(false);
                    dao.edit(holiday);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map getYears() {
        HolidayDAO dao = getHolidayDAO();
        List<Integer> years = dao.findYears();
        Map values = new LinkedHashMap();

        values.put("--All Years--", 0);
        for (Integer year : years) {
            values.put(year, year);
        }
        return values;
    }

    public void searchListener() {      //ActionEvent event
        HolidayDAO dao = getHolidayDAO();
        holidays = dao.findHolidayByYear(keyword);
        //return REFRESH;
    }

    public HolidayDAO getHolidayDAO() {
        return holidayDAO;
    }

    public void setHolidayDAO(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }
}
