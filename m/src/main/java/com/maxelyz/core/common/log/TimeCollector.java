/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.log;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author DevTeam
 */
public class TimeCollector {
    protected Date startTime = null;
    protected Date finishedTime = null;

    public TimeCollector() {
    }
    
    public TimeCollector(boolean stampStartTime) {
        this();
        if (stampStartTime) this.stampStartTime();
    }
    public void stampStartTime(){
       this.startTime = Calendar.getInstance().getTime();
    }
    public void stampFinishedTime(){
       this.finishedTime = Calendar.getInstance().getTime();
    }
    
    public Date getStartTime() {
        return startTime;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }
    
    public Long getDurationInMillisec(){
        Long duration = null;
        try{
            if ( this.startTime != null ){
                if ( this.finishedTime==null) stampFinishedTime();
                duration = this.finishedTime.getTime() - this.startTime.getTime();
            }
        }catch(Exception e){}
        return duration;
    }
}
