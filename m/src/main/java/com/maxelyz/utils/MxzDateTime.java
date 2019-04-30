/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author apichatt
 */
public class MxzDateTime {
    private static Logger log = Logger.getLogger(NetworkUtil.class);
    public static int calulateAge(Date dobDate, Date referenceDate) {
        int age;
        Calendar dob = Calendar.getInstance();
        dob.setTime(dobDate);
        Calendar today = Calendar.getInstance();
        today.setTime(referenceDate);
        int y1 = dob.get(Calendar.YEAR);
        int y2 = today.get(Calendar.YEAR);
        int m1 = dob.get(Calendar.MONTH);
        int m2 = today.get(Calendar.MONTH);
        int d1 = dob.get(Calendar.DATE);
        int d2 = today.get(Calendar.DATE);
        age = y2 - y1;
        if ((m1 > m2) || (m1 == m2 && d1 > d2)) //ถ้าเดือนเกิดมากกว่าเดือนปัจจุบัน หรือวันเกิดมากกว่าวันปัจจุบัน -> ยังไม่ครบปี
        {
            age--;
        }
        return age;
    }

    public static String secToMmSs(Integer pSec) {
        String mm;
        String ss;

        if (pSec==null) pSec = 0;

        mm = "" + pSec / 60;
        
        String text = "0" + pSec % 60;
        ss = text.substring(text.length() - 2, text.length());
        
        return mm+":"+ss;
    }

    public static String secToMmSs(Long pSec) {
        String mm;
        String ss;

        mm = "" + pSec / 60;

        String text = "0" + pSec % 60;
        ss = text.substring(text.length() - 2, text.length());

        return mm+":"+ss;
    }
}
