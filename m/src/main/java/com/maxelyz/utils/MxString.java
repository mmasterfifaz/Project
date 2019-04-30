package com.maxelyz.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: apichatt
 * Date: 29/5/2557
 * Time: 9:08 น.
 * To change this template use File | Settings | File Templates.
 */
public class MxString {
    public static String removeLastComma(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        else
            str = "";
        return str;
    }

    public static String chkIntNull(Integer i) {
        if (i==null) {
            return "null";
        } else {
            return Integer.toString(i);
        }
    }

    public static String chkIntNull(String i) {
        if (StringUtils.isBlank(i)) {
            return "null";
        } else {
            return i;
        }
    }

    public static String quotedStr(String str) {
        return "'" + str.replace("'", "''") + "'";
    }

    public static String quotedInStrNull(String str) {     //input format a,b,c
        String ret="";

        if (StringUtils.isBlank(str)) {
            return "null";
        }

        StringTokenizer st = new StringTokenizer(str, ",");

        while (st.hasMoreTokens()) {
            ret = stringAppend(ret, "'" + st.nextToken().replace("'", "''") + "'", ",", "");
        }

        return ret;
    }

    public static String quotedStrNull(String str) {
        if (StringUtils.isBlank(str)) {
            return "null";
        }
        return "'" + str.replace("'", "''") + "'";
    }

    public static String quotedStrEmpty(String str) {
        if (StringUtils.isBlank(str)) {
            return "''";
        }
        return "'" + str.replace("'", "''") + "'";
    }

    public static String indent(Integer n) {
        return StringUtils.repeat(" ", n);
    }

    public static void printIndent(Integer n, String s) {
        System.out.println(StringUtils.repeat(" ", n) + s);
    }

    public static boolean checkThaiLang(String ss) {

        Pattern p = Pattern.compile("[ก-ฮ]");

        for (int i=0; i<=ss.length()-1; i++) {
            String s = String.valueOf(ss.charAt(i));
            Matcher m = p.matcher(s);
//            boolean b = m.matches();
//            System.out.println(b);

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }

    public static String stringAppend(String sOriginal, String sValue, String sSign, String sKey) {
        String ret;

        if (StringUtils.isNotBlank(sOriginal)){
            ret = sOriginal + sSign + sKey + sValue;
        } else {
            ret = sKey + sValue;
        }

        return ret;
    }

    public static Boolean moreThanZero(Integer i){
        if (i!=null && i>0){
            return true;
        }
        return false;
    }
}
