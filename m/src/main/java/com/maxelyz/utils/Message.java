package com.maxelyz.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class Message {
    private static Logger log = Logger.getLogger(Message.class);
    public static String getLocalKey() {
       String result=null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            String key = "Maxelyz"+NetworkUtil.getMacAddress();
            md5.update(key.getBytes());
            byte[] messageDigest = md5.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i=0;i<messageDigest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            result = hexString.toString().toUpperCase();
            
        } catch (NoSuchAlgorithmException ex) {
            //log.error(ex);
        }
        return result;
   }
    public static String getLicenKey(String clientKey) {
       String result=null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            String key = "Maxar#"+clientKey;
            md5.update(key.getBytes());
            byte[] messageDigest = md5.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i=0;i<messageDigest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            result = hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            //log.error(ex);
        }
        return result;
    }
    
    public static void main(String d[]) {
        System.out.println(Message.getLocalKey());    
    }
}
