/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONArray;

import org.richfaces.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Transactional
public class TelephonyServiceImpl implements TelephonyService, Serializable {
    private static Logger log = Logger.getLogger(TelephonyServiceImpl.class);
    private static Logger ctiLog = Logger.getLogger("mxCtiLogger");   
    
    private class ThreadForNice implements Runnable {
        private String ctiServerURL;
        private String urlAction;
        private String callAction;
        private int ctiThreadSleep;
        
        public ThreadForNice(String server, String url, String action, int sleep)
        {
            ctiServerURL = server;
            urlAction = url;
            callAction = action;
            ctiThreadSleep = sleep;
        }
        
        @Override
        public void run() {
            System.out.println("Thread : " +  Thread.currentThread().getName() + " running");
            try {
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(ctiThreadSleep);
                    if (sendRequest(i).contains("SUCCESS")) {
                        break;
                    }
                }
            }catch (Exception e) {
                log.error(e);
            }
        }
        
        public synchronized String sendRequest(int number){
            System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Call Number " + number + " : " + new Date().toString());

            String updateResult = postCTIServer(ctiServerURL, urlAction, callAction);
            System.out.println("Thread : " +  Thread.currentThread().getName() + ", Request CTI Server : " + ctiServerURL + "," + urlAction + "," + callAction);
            if (updateResult == null) {
                System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Result : Null");
            }else{
                System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Result : " + updateResult);
            }
            return updateResult;
        }
    }

    // BEGIN FUNCTION FOR RECORDER NICE
    public String runThreadForNice(String extension, String urlAction, String callAction) {
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String ctiServerURL = ctiAdapter.getCtiServerUrl();
        String msg = "SUCCESS";
        /*
        _ctiServerURL = ctiServerURL;
        _urlAction = urlAction;
        _callAction = callAction;
        _ctiThreadSleep = ctiAdapter.getRecorderThreadSleep();
        
        Runnable runner = new Runnable() {
            public synchronized String sendRequest(int number){
                //try {
                //    for (int i = 0; i <= 5; i++) {
                //        Thread.sleep(_ctiThreadSleep);
                System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Call Number " + number + " : " + new Date().toString());

                String updateResult = postCTIServer(_ctiServerURL, _urlAction, _callAction);
                System.out.println("Thread : " +  Thread.currentThread().getName() + ", Request CTI Server : " + _ctiServerURL + "," + _urlAction + "," + _callAction);
                if (updateResult == null) {
                    System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Result : Null");
                }else{
                    System.out.println("Thread : " +  Thread.currentThread().getName() + ", Nice Result : " + updateResult);
                }
                return updateResult;
                //    }
                //}
                //catch (Exception e) {
                //    log.error(e);
                //}
            }

            @Override
            public void run() {
                System.out.println("Thread : " +  Thread.currentThread().getName() + " running");
                try {
                    for (int i = 1; i <= 5; i++) {
                        Thread.sleep(_ctiThreadSleep);
                        if (sendRequest(i).contains("SUCCESS")) {
                            break;
                        }
                    }
                }catch (Exception e) {
                    log.error(e);
                }
            }
        };
        */
        Runnable runner = new ThreadForNice(ctiServerURL, urlAction, callAction, ctiAdapter.getRecorderThreadSleep());
        Thread thread = new Thread(runner, extension);
        thread.start();

        return msg;
    }
    
    // POST CTI FOR THREAD
    public String postCTIServer(String ctiServerURL, String urlAction, String callAction) {
        String msg = "SUCCESS";
        HttpURLConnection conn = null;
        OutputStream os = null;
        BufferedReader br = null;
        try {
            URL url = new URL(ctiServerURL + urlAction);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            os = conn.getOutputStream();
            os.write(callAction.getBytes("UTF-8"));
            os.flush();
            os.close();
            
            if (conn.getResponseCode() != 200) {
                msg = conn.getResponseMessage();               
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            
            String result;
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((result = br.readLine()) != null) {
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("IsSuccess"); 
                    String resultMsg = "";
                    if (urlAction.equalsIgnoreCase("niceUpdateOpenCall") || urlAction.equalsIgnoreCase("niceUpdateAnyOpenCall")) {
                        resultMsg = jsonData.getString("ResponseMessage");
                        if (!resultMsg.contains("OK")) {
                            msg = resultMsg;
                        }
                    }else{
                        resultMsg = jsonData.getString("Message");
                        if(success.equals("false")) {
                            msg = resultMsg;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msg = ex.getMessage();
                }
            }

            br.close();
            conn.disconnect();
            
            return msg;
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }         
        return msg;
    }
    
    // END FUNCTION FOR RECORDER NICE
    
    public String postCTIServer(String urlAction, String callAction) {      
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String msg = "SUCCESS";
        if(ctiAdapter != null && ctiAdapter.getCtiServerUrl() != null) {            
            HttpURLConnection conn = null;
            OutputStream os = null;
            BufferedReader br = null;
            try {
                URL url = new URL(ctiAdapter.getCtiServerUrl() + urlAction);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                os = conn.getOutputStream();
                os.write(callAction.getBytes("UTF-8"));
                os.flush();
                os.close();

                if (conn.getResponseCode() != 200) {
                    msg = conn.getResponseMessage();               
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                String result;
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                while ((result = br.readLine()) != null) {
                    try {
                        JSONObject jsonData = new JSONObject(result);
                        String success = jsonData.getString("IsSuccess"); 
                        String resultMsg = "";
                        if (urlAction.equalsIgnoreCase("niceUpdateOpenCall") || urlAction.equalsIgnoreCase("niceUpdateAnyOpenCall")) {
                            resultMsg = jsonData.getString("ResponseMessage");
                            if (!resultMsg.contains("OK")) {
                                msg = resultMsg;
                            }
                        }else{
                            resultMsg = jsonData.getString("Message");
                            if(success.equals("false")) {
                                msg = resultMsg;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msg = ex.getMessage();
                    }
                }

                br.close();
                conn.disconnect();

                return msg;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
        } 
                 
        return msg;
    }
    
    public JSONObject getCTIServer(String urlAction, String callAction) {
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String msg = "SUCCESS";
        JSONObject jsonData = null;
        if(ctiAdapter != null && ctiAdapter.getCtiServerUrl() != null) {
            HttpURLConnection conn = null;
            BufferedReader br = null;
            
            try {
                URL url = new URL(ctiAdapter.getCtiServerUrl() + urlAction + callAction);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                if (conn.getResponseCode() != 200) {
                    msg = conn.getResponseMessage();  
                    System.out.println("ERROR MSG: "+msg);
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));   
                    if(br != null) {
                        jsonData = new JSONObject(br.readLine());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                br.close();
                conn.disconnect();

                return jsonData;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }     
        }
        return jsonData;
    }
    
    @Override
    public String loginTelephony(String userId, String password, String extension) throws Exception {
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String loginData = "";
        String msg = "";
        if(ctiAdapter.getCtiVendorCode().equals("aspect")) {
            loginData = "{\"userId\":\""+userId+"\",\"password\":\""+password+"\",\"extension\":\""+extension+"\",\"isForceLogin\":true}";
            msg = postCTIServer("ManualLogin", loginData);
        } else {
            loginData = "{\"userId\":\""+userId+"\",\"password\":\""+password+"\",\"extension\":\""+extension+"\"}";
            msg = postCTIServer("login", loginData);
        }
        
        return msg;        
    }
       
    @Override
    public String logoutTelephony(String userId, String extension) throws Exception {
        String logoutData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("logout", logoutData);  
        
        return msg;   
    }
    
    @Override
    public String logoutWithReason(String userId, String extension, String logoutReasonId) throws Exception {
        String logoutData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\",\"logoutReasonId\":\""+logoutReasonId+"\"}";
        String msg = postCTIServer("logoutWithReason", logoutData);  
        
        return msg;   
    }
        
    @Override
    public String dial(String userId, String extension, String outboundServiceId, String phoneNumber) throws Exception {
        String dialData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\",\"outboundServiceId\":\""+outboundServiceId+"\",\"phoneNumber\":\""+phoneNumber+"\"}";
        String msg = postCTIServer("dial", dialData);        
        
        return msg; 
    }
    
    @Override
    public String hangup(String userId, String extension) throws Exception {
        String hangupData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("hangup", hangupData);        
        
        return msg; 
    }
    
    @Override
    public String nextcall(String userId, String extension, String nextCallReasonCode) throws Exception {
        String nextcallData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\",\"nextCallReasonCode\":\""+nextCallReasonCode+"\"}";
        String msg = postCTIServer("nextcall", nextcallData);
        
        return msg; 
    }
                 
    @Override
    public String hold(String userId, String extension) throws Exception {
        String holdData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("hold", holdData);
        
        return msg; 
    }
        
    @Override
    public String unhold(String userId, String extension) throws Exception {
        String unholdData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("unhold", unholdData);
        
        return msg; 
    }
    
    private class ThreadForAspect implements Runnable {
        private String ctiServerURL;
        private String urlAction;
        private String callAction;
        
        public ThreadForAspect(String server, String url, String action) {
            ctiServerURL = server;
            urlAction = url;
            callAction = action;
        }
        
    @Override
        public void run() {
            ctiLog.info("Thread : " +  Thread.currentThread().getName() + " running "+callAction);
            try {
                Thread.sleep(3000);                       
                String updateResult = postCTIServer(ctiServerURL, urlAction, callAction);
                if (updateResult == null) {
                    ctiLog.info(new Date().toString()+" Extension: "+callAction+" Thread READY Result: "+updateResult);
                } else {
                    ctiLog.info(new Date().toString()+" Extension: "+callAction+" Thread READY Result: "+updateResult);
                }
            }catch (Exception e) {
                log.error(e);
            }
        }
    }
    
    // BEGIN FUNCTION FOR DELAY IDLE AFTER PARKED
    public String runThreadBeforeIdle(String urlAction, String readyData) { 
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String ctiServerURL = ctiAdapter.getCtiServerUrl();
        String msg = "SUCCESS";
        
        Runnable runner = new ThreadForAspect(ctiServerURL, urlAction, readyData);
        Thread thread = new Thread(runner);
        thread.start();
        
        
//        ctiLog.info(new Date()+" Begin Thread Ready Extension: "+ readyData);
//        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
//        String ctiServerURL = ctiAdapter.getCtiServerUrl();
//        String msg = "SUCCESS";
//        
//        _ctiServerURL = ctiServerURL;
//        _urlAction = "ready";
//        _callAction = readyData;
//                     
//        Runnable runner = new Runnable() {
//            @Override
//            public void run() {                
//                try {                    
//                    Thread.sleep(3000);                       
//                    String updateResult = postCTIServer(_ctiServerURL, _urlAction, _callAction);
//                    if (updateResult == null) {
//                        ctiLog.info(new Date().toString()+" Extension: "+_callAction+" Thread READY Result: "+updateResult);
////                        ctiLog.info("Thread : " +  Thread.currentThread().getName() + ", Ready Result : Null");
//                    } else {
//                        //ctiLog.info("Thread : " +  Thread.currentThread().getName() + ", Ready Result : " + updateResult);
//                        ctiLog.info(new Date().toString()+" Extension: "+_callAction+" Thread READY Result: "+updateResult);
//                    }
//                } catch (Exception e) {
//                    log.error(e);
//                }
//            }
//        };
//        Thread thread = new Thread(runner);
//        thread.start();
//        
//        ctiLog.info(new Date()+" End Thread Ready Extension: "+ readyData);
        return msg;
    }
    
    @Override
    public String ready(String userId, String extension) throws Exception {
        String readyData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
//        ctiLog.info(new Date()+" Begin Thread Ready Extension: "+ extension);
        String msg = runThreadBeforeIdle("ready", readyData);              
//        ctiLog.info(new Date()+" End Thread Ready Extension: "+ extension);
        return msg; 
    } 
    
    @Override
    public String notready(String userId, String extension, String notReadyReasonId) throws Exception {
        String notreadyData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\",\"notReadyReasonId\":\""+notReadyReasonId+"\"}";
        String msg = postCTIServer("notready", notreadyData);
        
        return msg; 
    } 
    
    @Override
    public String notreadypark(String userId, String extension) throws Exception {
        // FIX notReadyReasonId = 127 [PARK]
        String notreadyData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\",\"notReadyReasonId\":127}";
        String msg = postCTIServer("notreadypark", notreadyData);
        
        return msg; 
    } 
        
    @Override
    public String answer(String userId, String extension) throws Exception {
        String answerData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("answer", answerData);
        
        return msg; 
    } 
            
    @Override
    public String reject(String userId, String extension) throws Exception {
        String rejectData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\"}";
        String msg = postCTIServer("reject", rejectData);
        
        return msg; 
    } 
    
     // FOR ASPECT RECORDER
    @Override
    public String saveAspectParameter(String userId, String extension, String paramData) throws Exception {
        
        String parameterData = "{\"userId\":\""+userId+"\",\"extension\":\""+extension+"\","+paramData+"}";
        String msg = postCTIServer("saveparameter", parameterData);
        
        return msg; 
    } 
    
    @Override
    public JSONObject getDisposition(String userId, String extension) throws Exception {
        String dispositionData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("disposition", dispositionData);
        // Return result:{"Result":[{"code":"JKC","description":"Junk Call"},{"code":"JUNKCALL","description":"IB:JunkCall"}],"Message":null}
        
        return result; 
    } 
    
    @Override
    public JSONObject getNotReadyReason(String userId, String extension) throws Exception {
        String notReadyReasonData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("notreadyreason", notReadyReasonData);
        // Return result:{"Result":[{"description":"Toilet","id":104},{"description":"Break","id":105}],"Message":null}
        
        return result; 
    }
    
    @Override
    public JSONObject getLogOutReason(String userId, String extension) throws Exception {
        String logOutReasonData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("logoutreason", logOutReasonData);
        // Return result:{"Result":[{"description":"[Panic Logout]","id":-99},{"description":"End of Shift","id":101},],"Message":null}
        
        return result; 
    }
    
    @Override
    public JSONObject getCallInformationData(String userId, String extension) throws Exception {
        // GET TALK TIME
        String callInformationData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("callinformation", callInformationData);
        // Return result:{"Result":{"EndCallTime":"2016-08-02T22:04:07.419436+07:00","StartCallTime":"2016-08-02T22:04:01.0065051+07:00","TotalCallTime":6.4129309},"Message":null}
        
        return result; 
    }
    
    @Override
    public JSONObject getTrackid(String userId, String extension) throws Exception {
        String trackIDData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("trackid", trackIDData);
        // Return result:{"TrackId":"1070143","Message":null}
        
        return result; 
    }
    
    @Override
    public JSONObject getCurrentstate(String userId, String extension) throws Exception {
        String currentStateData = "?userId=" + userId + "&extension=" + extension;
        JSONObject result = getCTIServer("currentstate", currentStateData);
        
        return result; 
    }
    
    // FOR NICE RECORDER
    @Override
    public void callNiceUpdateOpenCall(String extension, String refCode, String phoneNo, String custName, String custSurname, String contactID, Users user) throws Exception {
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String tsrName;
        if(user.getSurname() != null && !user.getSurname().equals("")) {
            tsrName = user.getName() + " " + user.getSurname();
        } else {
            tsrName = user.getName();
        }
        
        System.out.println("Extension : " + extension + ", Call Nice Param: Extension="+extension+" - Flexfield1="+contactID+" - RefCode="+refCode+" - Phone="+phoneNo+" - Name="+custName+" - Surname="+custSurname+" - TsrName="+tsrName+" - TsrCode="+user.getLicenseName());
        String updateData;      
        if (ctiAdapter.getName().toLowerCase().contains("red")) {
            updateData = "{\"niceParam\":"
                          + "["
                          + "{\"BDNames\":\"JOB_TYPE\",\"BDValues\":\"MAXAR\"},"
                          + "{\"BDNames\":\"Additional_infomation\",\"BDValues\":\""+phoneNo+"\"},"
                          + "],"
                          + "\"userId\":\""+extension+"\"}";
        }else{
            updateData = "{\"niceParam\":"
                          + "["
                          + "{\"BDNames\":\"JOB_TYPE\",\"BDValues\":\"MAXAR\"},"
                          + "{\"BDNames\":\"Additional infomation\",\"BDValues\":\""+phoneNo+"\"},"
                          + "{\"BDNames\":\"CustomerName\",\"BDValues\":\""+custName+"\"},"
                          + "{\"BDNames\":\"CustomerLastName\",\"BDValues\":\""+custSurname+"\"},"
                          + "],"
                          + "\"userId\":\""+extension+"\"}";
        }
        
        String msg = runThreadForNice(extension, "niceUpdateOpenCall", updateData);
        System.out.println("Extension : " + extension + ", Call Nice Update Open Call Result: " + msg);
    }
      
    // FOR NICE RECORDER
    @Override
    public void callNiceUpdateAnyOpenCall(String extension, String refCode, String phoneNo, String custName, String custSurname, String contactID, Users user) throws Exception {
        CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
        String tsrName = "";
        if(user.getSurname() != null && !user.getSurname().equals("")) {
            tsrName = user.getName() + " " + user.getSurname();
        } else {
            tsrName = user.getName();
        }
        
        System.out.println("Extension : " + extension + ", Call Nice Param: Extension="+extension+" - Flexfield1="+contactID+" - RefCode="+refCode+" - Phone="+phoneNo+" - Name="+custName+" - Surname="+custSurname+" - TsrName="+tsrName+" - TsrCode="+user.getLicenseName());
        String updateData;      
        if (ctiAdapter.getName().toLowerCase().contains("red")) {
            updateData = "{\"niceParam\":"
                          + "["
                          + "{\"BDNames\":\"REF_CODE\",\"BDValues\":\""+refCode+"\"},"
                          + "{\"BDNames\":\"JOB_TYPE\",\"BDValues\":\"MAXAR\"},"
                          + "{\"BDNames\":\"ContactID\",\"BDValues\":\""+contactID+"\"}"
                          + "],"
                          + "\"userId\":\""+extension+"\"}";
        }else{
            updateData = "{\"niceParam\":"
                          + "["
                          + "{\"BDNames\":\"REF_CODE\",\"BDValues\":\""+refCode+"\"},"
                          + "{\"BDNames\":\"JOB_TYPE\",\"BDValues\":\"MAXAR\"},"
                          + "{\"BDNames\":\"ContactID\",\"BDValues\":\""+contactID+"\"},"
                          + "{\"BDNames\":\"TsrName\",\"BDValues\":\""+tsrName+"\"},"
                          + "{\"BDNames\":\"TsrCode\",\"BDValues\":\""+user.getLicenseName()+"\"}"
                          + "],"
                          + "\"userId\":\""+extension+"\"}";       
        }
        
        String msg = postCTIServer("niceUpdateAnyOpenCall", updateData);
        System.out.println("Extension : " + extension + ", Call Nice Update Any Open Call Result: " + msg);
    }
    
}
