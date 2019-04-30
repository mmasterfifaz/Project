/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.front.dispatch;

import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.QaTrans;
import javax.faces.context.FacesContext;

/**
 *
 * @author DevTeam
 */
public class RegistrationPouch extends  FrontPouch{
    
    public static enum SENDER_TYPE { SALEAPPROVAL, SALEAPPROACHING, SALEHISTORY, SHOPPINGCART, CONFIRMPURCHASEORDER, CUSTOMERDETAIL, NOTIFICATION, CONTACTRECORD, SELF };
    public static enum RECEIVER_MODE {NEW , EDIT, COPY };

    /****************** Pouch Receiver -PROPERTY  **************************/ 
    private static final String RECEIVER_PAGE = "/front/customerHandling/registrationpo.xhmtl";
    
    /****************** Pouch mode -PROPERTY  **************************/ 
    private SenderTR senderPage;
    private SENDER_TYPE senderType;  
    private RECEIVER_MODE receiverMode;

    /****************** Pouch data -PROPERTY  **************************/ 
    private Integer editPurchaseOrderId;
    private Integer copyFromPurchaseOrderId;
    private PurchaseOrder newPurchaseOrderObject;
    private PurchaseOrder puchaseOrder4Child;
    private QaTrans qaTrans4CollectData;
    private Integer age;
    private String paymentModeId;
    private String gender;
    
    /****************** Constructor  **************************/ 
    public RegistrationPouch() {
        registeredSender();
    }

    public void registeredPouchType(SENDER_TYPE senderType, RECEIVER_MODE receiverMode) {
        this.senderType=senderType;
        this.receiverMode=receiverMode;
    }
    
    public void putNewModeParameter(Integer age, String gender, String paymentModeId, PurchaseOrder newPurchaseOrderObject){
        this.age = age;
        this.gender = gender;
        this.paymentModeId = paymentModeId;
        this.newPurchaseOrderObject = newPurchaseOrderObject;
    }
    
    public void putCopyModeParameter(Integer copyFromPurchaseOrderId){
        this.copyFromPurchaseOrderId = copyFromPurchaseOrderId;
    }
    
    public void putEditModeParameter(Integer editPurchaseOrderId){
        this.editPurchaseOrderId = editPurchaseOrderId;
    }
    
    public void putPurchaseOrderForChild(PurchaseOrder puchaseOrder4Child, QaTrans qaTrans4CollectData ){
        this.puchaseOrder4Child = puchaseOrder4Child;
        this.qaTrans4CollectData = qaTrans4CollectData;
    }
    
    
    /****************** Implement - Method  **************************/ 
    @Override
    void validatePouchBeforeSending()throws Exception{
       if ( senderType==null || receiverMode==null ) throw new Exception("Registration pouch must registered before dispatching.");
       switch (receiverMode){
           case NEW  :  break;
           case COPY :  if (this.copyFromPurchaseOrderId==null) throw new Exception("Registration pouch must keep data for COPY mode before sending.");   break;
           case EDIT :  if (this.editPurchaseOrderId==null) throw new Exception("Registration pouch must keep data for EDIT mode before sending.");   break;
           default   : throw new Exception("Invalid Registration pouch mode");    
       }
       stampSendingExecution();
    }

    @Override  
    String getReceiverPage() {
        return RECEIVER_PAGE;
    }
    
    /****************** Getter - Method  **************************/ 
    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public PurchaseOrder getNewPurchaseOrderObject() {
        return newPurchaseOrderObject;
    }
    
    public Integer getCopyFromPurchaseOrderId() {
        return copyFromPurchaseOrderId;
    }

    public Integer getEditPurchaseOrderId() {
        return editPurchaseOrderId;
    }

    public PurchaseOrder getPuchaseOrder4Child() {
        return puchaseOrder4Child;
    }
    
    public RECEIVER_MODE getReceiverMode() {
        return receiverMode;
    }

    public SenderTR getSenderPage() {
        return senderPage;
    }

    public SENDER_TYPE getSenderType() {
        return senderType;
    }

    public QaTrans getQaTrans4CollectData() {
        return qaTrans4CollectData;
    }
    
    @Override
    public String toString() {
        return RegistrationPouch.class.getSimpleName()+": [senderType="+senderType+", receiverMode="+receiverMode+", senderPage="+senderPage+
              ", editPurchaseOrderId="+editPurchaseOrderId+", copyFromPurchaseOrderId="+copyFromPurchaseOrderId+", newPurchaseOrderObject="+newPurchaseOrderObject+", puchaseOrder4Child="+puchaseOrder4Child+", qaTrans4CollectData="+qaTrans4CollectData+"]";

    }
    
    

    /****************** Private - Method  **************************/ 
    private void registeredSender(){
        this.senderPage = new SenderTR();
        try{
            senderPage.page = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();    
            if (stacktrace!=null && stacktrace.length>3){
                senderPage.createExecution = stacktrace[3].getClassName()+"."+stacktrace[3].getMethodName()+"("+stacktrace[3].getLineNumber()+")";
            }
        }catch(Exception e){}    
    
    }
    private void stampSendingExecution(){
        try{
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();    
            if (stacktrace!=null && stacktrace.length>3){
                senderPage.sendingExecution = stacktrace[3].getClassName()+"."+stacktrace[3].getMethodName()+"("+stacktrace[3].getLineNumber()+")";
            }
        }catch(Exception e){}     
    }
    
    
    
}
class SenderTR{
    String page;
    String sendingExecution;
    String createExecution;

    public String getPage() {
        return page;
    }

    public String getSendingExecution() {
        return sendingExecution;
    }

    public String getCreateExecution() {
        return createExecution;
    }

    @Override
    public String toString() {
        return SenderTR.class.getSimpleName()+": [page="+page+", createExecution="+createExecution+", sendingExecution="+sendingExecution+"]";
    }
    
    

}    