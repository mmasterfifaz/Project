///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.maxelyz.test;
//
//import com.concerto.UD.AgentService.AgentPortalException;
//import com.concerto.UD.AgentService.AgentWebService;
//import com.concerto.UD.AgentService.Client.UDAgent;
//import java.rmi.RemoteException;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class Task extends TimerTask {
//
//    private UDAgent _udAgent;
//    private AgentWebService _agentWebService;
//    // A string to output
//
//    /**
//     * Constructs the object, sets the string to be output in function run()
//     * @param str
//     */
//    Task(AgentWebService agentWS,UDAgent udAgent) {
//        this._agentWebService = agentWS;
//        this._udAgent = udAgent;
//    }
//
//    /**
//     * When the timer executes, this code is run.
//     */
//    public void run() {
//// Get current date/time and format it for output
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
//        String current_time = format.format(date);
//        try {
//            // Output to user the name of the objecet and the current time
//            _agentWebService.keepAlive(_udAgent);
//        } catch (RemoteException ex) {
//            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(" - Current time: " + current_time);
//    }
//}
