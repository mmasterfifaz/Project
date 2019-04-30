
package com.maxelyz.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class MaxarKeyGen {
 private static Logger log = Logger.getLogger(Message.class);
    public static String getKey(String clientKey, int concurrentUser) {
       String result=null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            String key = "Maxar#"+clientKey+concurrentUser;
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
        System.out.println(MaxarKeyGen.getKey(Message.getLocalKey(),0));
        
        
    }    
}
