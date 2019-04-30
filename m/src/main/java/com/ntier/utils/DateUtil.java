/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ntier.utils;

import java.util.Calendar;

/**
 *
 * @author Manop
 */
public class DateUtil {
    
    public DateUtil(){}

    public Long getDateDiffInMilliSec(Calendar date1, Calendar date2){
        return (date2.getTimeInMillis() - date1.getTimeInMillis());
    }

    public Long getDateDiffInSecond(Calendar date1, Calendar date2){
        return getDateDiffInMilliSec(date1, date2)/1000;
    }

    public Long getDateDiffInMinutes(Calendar date1, Calendar date2){
        return getDateDiffInSecond(date1, date2)/60;
    }

    public Long getDateDiffInHours(Calendar date1, Calendar date2){
        return getDateDiffInMinutes(date1, date2)/60;
    }

    public Long getDateDiffInDays(Calendar date1, Calendar date2){
        return getDateDiffInHours(date1, date2)/24;
    }

    public Long getDateDiffInYear(Calendar date1, Calendar date2){
        return getDateDiffInDays(date1, date2) * 10000/3652425;
    }

}
