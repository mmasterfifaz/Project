///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
package com.maxelyz.test;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
//
///**
// *
// * @author admin
// */
public class TestMain {
//
//

    public static void main(String[] args) throws Exception {
//        Integer age = 12;
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(new Date());
//        Integer d = calendar.get(Calendar.DAY_OF_MONTH);
//        Integer m = calendar.get(Calendar.MONTH);
//        Integer y = calendar.get(Calendar.YEAR) - age;
//        calendar.set(y, m, d);
//        
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//        //System.out.println(sdf1.format(calendar.getTime()));
//        
//        TimeZone defaultTimeZone = TimeZone.getDefault(); 
//        System.out.println(defaultTimeZone.getDisplayName());
        
       //display(new File("C:\\temp\\message.eml"));
       
       /*
       String b =  "<!--beginSignature-->";
       String e =  "<!--endSignature-->";
       String str = "aaa bbb ccc <!--beginSignature-->xxx yyy zzz<!--endSignature--> ddd eee fff";
       Integer s1 = str.indexOf(b);
       Integer s2 = str.indexOf(e);
       
       System.out.println(str.substring(0, str.indexOf(b)));
       System.out.println(str.substring(str.indexOf(e) + e.length(), str.length()));
       
       
        String cId = "<cid:image001.jpg@xxxxxyyz>";
        if(cId.indexOf("<") != -1 && cId.indexOf(">") != -1){
            cId = cId.substring(cId.indexOf("<")+1, cId.indexOf(">"));
        }
        System.out.println(cId);
        if(cId.indexOf("cid:") != -1 && cId.indexOf("@") != -1){
            cId = cId.substring(cId.indexOf("cid:")+4, cId.indexOf("@"));
        }
        System.out.println(cId);
        */
        int i = new Double(Math.floor(247/12)).intValue();
        
        System.out.println(i);
        Calendar dob = new GregorianCalendar(1976, 7, 1);
        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        //Date dob = calendar.getTime();
        
        Calendar today = Calendar.getInstance();
        //today.set(Calendar.MONTH, 1);
        //System.out.println(today.get(Calendar.JANUARY));
        Calendar calendar = Calendar.getInstance();
        int year = 2008;
        int month = Calendar.FEBRUARY;
        int date = 1;
        calendar.set(year, month, date);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("Number of Days: " + days);
        System.out.println("DIFF = " + differenceInMonths(dob, today));
        System.out.println("DIFF = " + getAge(dob, today));
        //dob.setTime(dobDate);
        //Calendar today = Calendar.getInstance();
        int doby = 1982;
        int dobm = 8;
        int dobd = 29;
        int tdy = 2015;
        int tdm = 1;
        int tdd = 29;
        int age = tdy - doby;
        
        //Calendar today = Calendar.getInstance();
        today.set(tdy, tdm, tdd);
        int maxDayInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("AGE1 = " + age);

        if(tdm > dobm){
            if(tdm - dobm == 6){
                if(tdd >= dobd){
                    age++;
                }
            }else if(tdm - dobm > 6){
                age++;
            }
        }else if(dobm > tdm) {
            int param = (12-dobm) + tdm;
            if(param > 6){
                age++;
            }else if(param == 6){
                if(tdd >= dobd){
                    age++;
                }
            }
        }
        System.out.println("AGE2 = " + age);
        

   }    
    
//    public static Integer differenceInMonths(Date beginningDate, Date endingDate) {
//        if (beginningDate == null || endingDate == null) {
//            return 0;
//        }
//        Calendar cal1 = new GregorianCalendar();
//        cal1.setTime(beginningDate);
//        Calendar cal2 = new GregorianCalendar();
//        cal2.setTime(endingDate);
//        return differenceInMonths(cal1, cal2);
//    }

    public static Integer getAge(Calendar beginningDate, Calendar endingDate) {
        if (beginningDate == null || endingDate == null) {
            return 0;
        }
        int m1 = beginningDate.get(Calendar.YEAR) * 12 + beginningDate.get(Calendar.MONTH);
        int m2 = endingDate.get(Calendar.YEAR) * 12 + endingDate.get(Calendar.MONTH);
        Integer m3 = m2-m1;
        
        return (m3/12);
    }

    public static Integer differenceInMonths(Calendar beginningDate, Calendar endingDate) {
        if (beginningDate == null || endingDate == null) {
            return 0;
        }
        int m1 = beginningDate.get(Calendar.YEAR) * 12 + beginningDate.get(Calendar.MONTH);
        int m2 = endingDate.get(Calendar.YEAR) * 12 + endingDate.get(Calendar.MONTH);
        Integer m3 = m2-m1;
        
        return (m3 % 12);
    }

   public static void display(File emlFile) throws Exception{
        Properties props = System.getProperties();
        props.put("mail.host", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");

        Session mailSession = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("ukrit@terrabit.co.th", "inczln09");
			}
		  });
        //Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(emlFile);
        MimeMessage message = new MimeMessage(mailSession, source);


        System.out.println("Subject : " + message.getSubject());
        System.out.println("From : " + message.getFrom()[0]);
        System.out.println("--------------");
        System.out.println("Body : " +  message.getContent());
    }
}
