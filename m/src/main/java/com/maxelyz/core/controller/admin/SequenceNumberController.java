/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.SequenceNoDAO;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import org.apache.commons.lang3.StringUtils;
//import org.hibernate.tool.hbm2x.StringUtils;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class SequenceNumberController implements Serializable {
    private static Logger log = Logger.getLogger(SequenceNumberController.class);
    private static String REFRESH = "sequencenumber.xhtml?faces-redirect=true";
    private static String EDIT = "sequencenumberedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SequenceNo> sequenceNos;
    private SequenceNo sequenceNo;
    
    @ManagedProperty(value = "#{sequenceNoDAO}")
    private SequenceNoDAO sequenceNoDAO;
 
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:sequencenumber:view")) {    
            SecurityUtil.redirectUnauthorize();  
        }
        sequenceNos = this.getSequenceNoDAO().findSequenceNoEntities();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumber:add");   
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumber:delete");     
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        SequenceNoDAO dao = getSequenceNoDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    sequenceNo = dao.findSequenceNo(item.getKey());
                    sequenceNo.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    sequenceNo.setUpdateDate(new Date());
                    sequenceNo.setEnable(false);
                    dao.edit(sequenceNo);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String createSample(SequenceNo sequenceNo) {
        String sample;
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = dateFormat.format(now);        
        String year = today.substring(0, 4);
        String month = today.substring(5, 7);
        String day = today.substring(8, 10);
        
        if(sequenceNo.getYearEra().equals("BE")) {
            int yearIn = Integer.parseInt(year) + 543;
            year = Integer.toString(yearIn);
        }
        
        sample = sequenceNo.getFormat();
        
        //Replace n
        String running = "00000000000000000000" + Integer.toString(sequenceNo.getNextRunno());
        running = running.substring(running.length() - sequenceNo.getRunningLength(), running.length());
        sample = StringUtils.replace(sample, "n", running);
        sample = StringUtils.replace(sample, "x", sequenceNo.getAbbr());
        
        //Replace Year
        sample = StringUtils.replace(sample, "yyyy", year);
        sample = StringUtils.replace(sample, "yy", year.substring(2));

        //Replace Month
        if(sample.contains("mmm")) {
            if(month.equals("01")) {
                sample = StringUtils.replace(sample, "mmm", "Jan");
            } else if(month.equals("02")) {
                sample = StringUtils.replace(sample, "mmm", "Feb");
            } else if(month.equals("03")) {
                sample = StringUtils.replace(sample, "mmm", "Mar");
            } else if(month.equals("04")) {
                sample = StringUtils.replace(sample, "mmm", "Apr");
            } else if(month.equals("05")) {
                sample = StringUtils.replace(sample, "mmm", "May");
            } else if(month.equals("06")) {
                sample = StringUtils.replace(sample, "mmm", "Jun");
            } else if(month.equals("07")) {
                sample = StringUtils.replace(sample, "mmm", "Jul");
            } else if(month.equals("08")) {
                sample = StringUtils.replace(sample, "mmm", "Aug");
            } else if(month.equals("09")) {
                sample = StringUtils.replace(sample, "mmm", "Sep");
            } else if(month.equals("10")) {
                sample = StringUtils.replace(sample, "mmm", "Oct");
            } else if(month.equals("11")) {
                sample = StringUtils.replace(sample, "mmm", "Nov");
            } else if(month.equals("12")) {
                sample = StringUtils.replace(sample, "mmm", "Dec");
            }
        } else if(sample.contains("mm")) {
            sample = StringUtils.replace(sample, "mm", month);
        } else if(sample.contains("m")) {
                if(month.startsWith("0")) {
                    sample = StringUtils.replace(sample, "m", month.substring(1));
                } else {
                    sample = StringUtils.replace(sample, "m", month);
                }
        }
        
        //Replace Day
        if(sample.contains("dd")) {
            sample = StringUtils.replace(sample, "dd", day);
        } else if(sample.contains("d")) {
                if(day.startsWith("0")) {
                    sample = StringUtils.replace(sample, "d", day.substring(1));
                } else {
                    sample = StringUtils.replace(sample, "d", day);
                }
        }
        
        return sample;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SequenceNo> getSequenceNoList() {
        return getSequenceNos();
    }

    public List<SequenceNo> getSequenceNos() {
        return sequenceNos;
    }

    public void setSequenceNos(List<SequenceNo> sequenceNos) {
        this.sequenceNos = sequenceNos;
    }

    public SequenceNoDAO getSequenceNoDAO() {
        return sequenceNoDAO;
    }

    public void setSequenceNoDAO(SequenceNoDAO sequenceNoDAO) {
        this.sequenceNoDAO = sequenceNoDAO;
    }

}
