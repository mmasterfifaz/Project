/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.log;

import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.RollbackException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.transaction.TransactionSystemException;

/**
 *
 * @author DevTeam
 */
public class RegistrationLogging {
    private static Logger mxregLog = Logger.getLogger("mxRtxLogger");
    private static final String ERROR_TYPE = new String("ERROR");
    private static final String INFO_TYPE = new String("INFO");
    private static final String DEBUG_TYPE = new String("DEBUG");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.US);
    private static final String SIMPLE_CLASSNAME = RegistrationLogging.class.getSimpleName();
    
    public static void logError(String errrorMessage, Exception e){
        mxregLog.error(getErrorTypePrefix()+getSeparator()+getGeneralPrefixTemplate(2)+getSeparator()+"Message: "+errrorMessage+getSeparator()+"ExceptionMessage: "+(e==null?"N/A":e.getMessage()) );
        if (e ==null  || e instanceof LazyInitializationException ) return;
        //mxregLog.error(getExceptionStackString(e.getStackTrace()));
        mxregLog.error(e);
    }
   
    public static void logInfo(String message){
        mxregLog.info(   getInfoTypePrefix()+getSeparator()+getGeneralPrefixTemplate()+getSeparator()+"Message:"+message);
    } 

    public static void debugData(String message, Object ... trackingObjsValue){
        mxregLog.debug(getDebugTypePrefix()+" DATA"+getSeparator()+getGeneralPrefixTemplate()+getSeparator()+"Message: "+message);
        if (trackingObjsValue!=null){
            for (int i=0; i<trackingObjsValue.length;i=i+2){
                try{
                    mxregLog.debug(getPadding4MessageDetail()+getObjectValueToString((String)trackingObjsValue[i], trackingObjsValue[i+1]) );
                }catch(Exception e){}
            }
        }
    }
    
    public static void debugCaller(){
        try{
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            if ( stacktrace!=null ){
                mxregLog.debug(getDebugTypePrefix()+" EXECUTION"+getSeparator()+getGeneralPrefixTemplate());
                for(int i=0; i<stacktrace.length;i++){
                   mxregLog.debug(getPadding4MessageDetail()+getCallerDetail( stacktrace[i] ));
                }
            }   
        }catch(Exception e){}
    }
    
    public static void debugElapseTime(TimeCollector tc, String infoMessage){
        try{
            String time = null;
            if (tc!=null){
                Long durationTime = tc.getDurationInMillisec();
                time = ", [Start time: "+tc.startTime+", Finished time: "+tc.finishedTime+", Duration: "+durationTime+" ms.]";
            }
            mxregLog.debug(getDebugTypePrefix()+" TIME"+getSeparator()+getGeneralPrefixTemplate()+getSeparator()+"Message:"+infoMessage+getSeparator()+time);
        }catch(Exception e){}
    }
        
    public static void debugMemoryUsage(){
        ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        mxregLog.debug("Heap = "+ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        mxregLog.debug("NonHeap = "+ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean bean: beans) {
            mxregLog.debug(bean.getName()+"="+bean.getUsage());
        }

        for (GarbageCollectorMXBean bean: ManagementFactory.getGarbageCollectorMXBeans()) {
            mxregLog.debug(bean.getName()+" count="+bean.getCollectionCount()+" time="+bean.getCollectionTime());
        }    
    
    }
   
    public static String getObjectValueToString(String name,Object obj){
        try{
            return name+getSeparator()+ReflectionToStringBuilder.toString(obj,ToComStringStyle.NO_RECURSIVE_STYLE );
        }catch(Exception e){}
        return null;
    }
    
    /************************* PROTECTED **********************************/

    protected static String getGeneralPrefixTemplate(){
        return  getGeneralPrefixTemplate(0);
    }
    
    protected static String getGeneralPrefixTemplate(int callerStep){
        return  getCurrentTime()+getSeparator()+ "Actor: "+getLoginName()+getSeparator()+"Execute: "+getCaller(callerStep) ;
    }
    
    protected static String getGeneralPrefixTemplateWith3Tracking(int callerStep){
        return  getCurrentTime()+getSeparator()+ "Actor: "+getLoginName()+getSeparator()+"Execute: "+get3TrackingCaller(callerStep) ;
    }
        
    protected static String get3TrackingCaller(int callerStep){
        StringBuffer sb = new StringBuffer();
        try{
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();            
            int counting = 0;
            if ( stacktrace!=null ){
                for (int i=2 + callerStep ; i<stacktrace.length && counting < 3 ;i++){
                    if ( stacktrace[i].getClassName().contains( SIMPLE_CLASSNAME ) ){ continue; }
                    sb.append("-> "+getCallerDetail(stacktrace[i]));
                    counting++;
                }
            }   
        }catch(Exception e){}
        return sb.toString();
    }
    
    protected static String getCaller(int callerStep){
        try{
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();            
            if ( stacktrace!=null ){
                for (int i=2 + callerStep ; i<stacktrace.length;i++){
                    if ( stacktrace[i].getClassName().contains( SIMPLE_CLASSNAME ) ){ continue; }
                    return getCallerDetail(stacktrace[i]);
                }
            }   
        }catch(Exception e){}
        return null;
    }
    
    protected static String getExceptionStackString(StackTraceElement[] steList){
        StringBuilder sb = new StringBuilder();
        try{
            if ( steList!=null ){
                for(int i=0; i<steList.length && i<=5 ;i++){
                   sb.append(getPadding4MessageDetail()+getCallerDetail( steList[i] )+"\n");
                   
                }
            }   
        }catch(Exception e){}
       return sb.toString();
    }
    
    protected static String getCallerDetail(StackTraceElement tracking) throws Exception{
        String classFullName = tracking.getClassName();
        String classShortName = (classFullName==null)?null:((classFullName.lastIndexOf('.')==-1)?classFullName:classFullName.substring(classFullName.lastIndexOf('.')+1, classFullName.length()));
        return classShortName+"."+ tracking.getMethodName()+" line("+tracking.getLineNumber()+")";
    }
    
    protected static String getLoginName(){
        Users actor = (JSFUtil.getUserSession()==null)?null:JSFUtil.getUserSession().getUsers();
        return (actor==null)?"N/A":actor.getLoginName();
    }
    
    protected static String getSeparator(){
        return "\t";
    }
    
    protected static String getPadding4MessageDetail(){
        return "\t\t\t\t";
    }
    protected static String getErrorTypePrefix(){
        return ERROR_TYPE;
    }
    
    protected static String getDebugTypePrefix(){
        return DEBUG_TYPE;
    }
    
    protected static String getInfoTypePrefix(){
        return INFO_TYPE;
    }
    
    protected static String getCurrentTime(){
        return sdf.format(Calendar.getInstance().getTime());
    }
}
 /************************* CLASS **********************************/
class ToComStringStyle extends ToStringStyle{
    public static final ToStringStyle NO_RECURSIVE_STYLE = new ToComStringStyle();
    
    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
        if ( coll!=null ) buffer.append(coll.size());
        else buffer.append("null");
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
        if ( map!=null ) buffer.append(map.size());
        else buffer.append("null");
    }
    
   
    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value instanceof String || value instanceof Integer || value instanceof Double || value instanceof Long || value instanceof java.util.Date ){
            buffer.append(value);
        }else{ 
            if ( value instanceof Collection)
               super.appendDetail(buffer, fieldName, value); //To change body of generated methods, choose Tools | Templates.
            else if (value==null)
                buffer.append("null");
            else 
               buffer.append(value.toString());
                
        } 
        
    }
    
    
}