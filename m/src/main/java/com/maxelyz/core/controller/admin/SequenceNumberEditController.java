/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.SequenceNoDAO;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang3.StringUtils;
//import org.ajax4jsf.model.KeepAlive;
//import org.hibernate.tool.hbm2x.StringUtils;

@ManagedBean
//@RequestScoped
//@KeepAlive
@ViewScoped
public class SequenceNumberEditController {
    private static Logger log = Logger.getLogger(SequenceNumberEditController.class);
    private static String REDIRECT_PAGE = "sequencenumber.jsf";
    private static String SUCCESS = "sequencenumber.xhtml?faces-redirect=true";
    private static String FAILURE = "sequencenumberedit.xhtml";
    private SequenceNo sequenceNo;
    private String mode;
    private String message;
    private String messageDup;
    private String sample;
    private Boolean typeSequenceRender = true;
    private String selectGenerateType;
    
    @ManagedProperty(value = "#{sequenceNoDAO}")
    private SequenceNoDAO sequenceNoDAO;
    
    @PostConstruct
    public void initialize() {
        
        if (!SecurityUtil.isPermitted("admin:sequencenumber:view")) {       
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            sequenceNo = new SequenceNo();
            sequenceNo.setId(null);
            sequenceNo.setRunningLength(4);
            sequenceNo.setNextRunno(1);
            sequenceNo.setGenerateType("Internal");
            sequenceNo.setEnable(Boolean.TRUE);
            sequenceNo.setSystem(Boolean.FALSE);
            sequenceNo.setYearEra("BE");
        } else {
            mode = "edit";
            SequenceNoDAO dao = this.getSequenceNoDAO();
            sequenceNo = dao.findSequenceNo(new Integer(selectedID));
            if(sequenceNo == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }  else {
                createSample();
            }
            if(sequenceNo != null && sequenceNo.getGenerateType().equals("Internal")) {
                selectGenerateType = "Internal";
                typeSequenceRender = true;

            }  else {
                selectGenerateType = "File Upload";
                typeSequenceRender = false;
            }
        }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:sequencenumber:add");            
       } else {
 	   return SecurityUtil.isPermitted("admin:sequencenumber:edit");           
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkName(sequenceNo)) {
            SequenceNoDAO dao = getSequenceNoDAO();
            try {
                String username = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                if(selectGenerateType.equals("File Upload")){
                    sequenceNo.setAbbr(" ");
                    sequenceNo.setRunningLength(0);
                    sequenceNo.setNextRunno(0);
                }
                if (getMode().equals("add")) {
                    sequenceNo.setId(null);
                    sequenceNo.setGenerateType(selectGenerateType);
                    sequenceNo.setCreateBy(username);
                    sequenceNo.setCreateDate(now);
                    dao.create(sequenceNo);
                } else {
                    sequenceNo.setGenerateType(selectGenerateType);
                    sequenceNo.setUpdateBy(username);
                    sequenceNo.setUpdateDate(now);
                    dao.edit(sequenceNo);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            if(selectGenerateType.equals("File Upload")){
                typeSequenceRender = false;
            }else {
                typeSequenceRender = true;
            }
            messageDup = " Name is already taken";
            return null;
        }
    }
    
    public Boolean checkName(SequenceNo sequenceNo) {
        String name = sequenceNo.getName();
        Integer id = 0;
        if(sequenceNo.getId() != null) 
            id = sequenceNo.getId();
            
        Integer cnt = getSequenceNoDAO().checkSequenceNoName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public void changeTypeListenerr(ValueChangeEvent event){
        String type = (String) event.getNewValue();
        if(type.equals("Internal")){
            typeSequenceRender= true;
        }else if(type.equals("File Upload")){
            typeSequenceRender= false;
        }
    }

    public void createSample() {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(now);        
        String year = today.substring(0, 4);
        String month = today.substring(5, 7);
        String day = today.substring(8, 10);
        
        if(sequenceNo.getYearEra().equals("BE")) {
            int yearIn = Integer.parseInt(year) + 543;
            year = Integer.toString(yearIn);
        }
        
        sample = sequenceNo.getFormat();
        
        if(sample != null) {
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
        }
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    //Managed Properties
    public SequenceNoDAO getSequenceNoDAO() {
        return sequenceNoDAO;
    }

    public void setSequenceNoDAO(SequenceNoDAO sequenceNoDAO) {
        this.sequenceNoDAO = sequenceNoDAO;
    }

    public SequenceNo getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(SequenceNo sequenceNo) {
        this.sequenceNo = sequenceNo;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSelectGenerateType() {
        return selectGenerateType;
    }

    public void setSelectGenerateType(String selectGenerateType) {
        this.selectGenerateType = selectGenerateType;
    }

    public Boolean getTypeSequenceRender() {
        return typeSequenceRender;
    }

    public void setTypeSequenceRender(Boolean typeSequenceRender) {
        this.typeSequenceRender = typeSequenceRender;
    }
}
