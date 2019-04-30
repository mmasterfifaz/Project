/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;


public class NetworkUtil {
    private static Logger log = Logger.getLogger(NetworkUtil.class);
    public static String getMacAddress() {
        StringBuilder macaddress = new StringBuilder();
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                
                if (mac!=null) {
                    for (int i=0;i<mac.length;i++) {
                        macaddress.append(String.format("%02X%s", mac[i], (i<mac.length -1 )? "-":""));                      
                    }             
                }
            }
        } catch (SocketException ex) {
            //log.error(ex);
        } catch (UnknownHostException ex) {
            //log.error(ex);
        }
        return macaddress.toString();
    }
    public static void main(String argd[] ) {
        NetworkUtil.getMacAddress();
    }
}
