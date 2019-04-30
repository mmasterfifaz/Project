/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.SequenceNoDAO;
import com.maxelyz.core.model.dao.SequenceNoFileDetailDAO;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Transactional
public class NextSequenceServiceImpl implements NextSequenceService, Serializable {
    private static Logger log = Logger.getLogger(NextSequenceServiceImpl.class);
    private SequenceNoDAO sequenceNoDAO;
    private SequenceNoFileDetailDAO sequenceNoFileDetailDAO;
    private SequenceNo sequenceNo;
     
    @Override
    public synchronized String genRef(Integer sequenceId) throws Exception {  
        sequenceNo = getSequenceNoDAO().updateSequence(sequenceId);
        if(sequenceNo != null) {
            if (sequenceNo.getGenerateType().equalsIgnoreCase("Internal")) {
                String ref;
                Date now = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String today = dateFormat.format(now);        
                String year = today.substring(0, 4);
                String month = today.substring(5, 7);
                String day = today.substring(8, 10);

                //update next seq + 1
                getSequenceNoDAO().updateNextSeq(sequenceId);

                if(sequenceNo.getYearEra().equals("BE")) {
                    int yearIn = Integer.parseInt(year) + 543;
                    year = Integer.toString(yearIn);
                }

                //create reference key
                ref = sequenceNo.getFormat();

                //Replace n
                String running = "00000000000000000000" + Integer.toString(sequenceNo.getNextRunno());
                running = running.substring(running.length() - sequenceNo.getRunningLength(), running.length());
                ref = StringUtils.replace(ref, "n", running);
                ref = StringUtils.replace(ref, "x", sequenceNo.getAbbr());

                //Replace Year
                ref = StringUtils.replace(ref, "yyyy", year);
                ref = StringUtils.replace(ref, "yy", year.substring(2));

                //Replace Month
                if(ref.contains("mmm")) {
                    if(month.equals("01")) {
                        ref = StringUtils.replace(ref, "mmm", "Jan");
                    } else if(month.equals("02")) {
                        ref = StringUtils.replace(ref, "mmm", "Feb");
                    } else if(month.equals("03")) {
                        ref = StringUtils.replace(ref, "mmm", "Mar");
                    } else if(month.equals("04")) {
                        ref = StringUtils.replace(ref, "mmm", "Apr");
                    } else if(month.equals("05")) {
                        ref = StringUtils.replace(ref, "mmm", "May");
                    } else if(month.equals("06")) {
                        ref = StringUtils.replace(ref, "mmm", "Jun");
                    } else if(month.equals("07")) {
                        ref = StringUtils.replace(ref, "mmm", "Jul");
                    } else if(month.equals("08")) {
                        ref = StringUtils.replace(ref, "mmm", "Aug");
                    } else if(month.equals("09")) {
                        ref = StringUtils.replace(ref, "mmm", "Sep");
                    } else if(month.equals("10")) {
                        ref = StringUtils.replace(ref, "mmm", "Oct");
                    } else if(month.equals("11")) {
                        ref = StringUtils.replace(ref, "mmm", "Nov");
                    } else if(month.equals("12")) {
                        ref = StringUtils.replace(ref, "mmm", "Dec");
                    }
                } else if(ref.contains("mm")) {
                    ref = StringUtils.replace(ref, "mm", month);
                } else if(ref.contains("m")) {
                        if(month.startsWith("0")) {
                            ref = StringUtils.replace(ref, "m", month.substring(1));
                        } else {
                            ref = StringUtils.replace(ref, "m", month);
                        }
                }

                //Replace Day
                if(ref.contains("dd")) {
                    ref = StringUtils.replace(ref, "dd", day);
                } else if(ref.contains("d")) {
                        if(day.startsWith("0")) {
                            ref = StringUtils.replace(ref, "d", day.substring(1));
                        } else {
                            ref = StringUtils.replace(ref, "d", day);
                        }
                }
                return ref;
            }else{  // Generate Type is "File Upload"
                String ref;
                ref = getSequenceNoDAO().getNextSeqFromSequenceNoFile(sequenceId);
                getSequenceNoFileDetailDAO().updateUsedStatus(sequenceId, ref);
                
                return ref;
            }
        } else {
            return null;
        }
    }
    
    //DAO
    public SequenceNoDAO getSequenceNoDAO() {
        return sequenceNoDAO;
    }

    public void setSequenceNoDAO(SequenceNoDAO sequenceNoDAO) {
        this.sequenceNoDAO = sequenceNoDAO;
    }
    
    public SequenceNoFileDetailDAO getSequenceNoFileDetailDAO() {
        return sequenceNoFileDetailDAO;
    }

    public void setSequenceNoFileDetailDAO(SequenceNoFileDetailDAO sequenceNoFileDetailDAO) {
        this.sequenceNoFileDetailDAO = sequenceNoFileDetailDAO;
    }
    
}
