/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DevTeam
 */
public class PaymentMethodStringCastor {
    public enum PdPaymentMode{
        MONTHLY(0,"Monthly","1"), QUARTERLY(1,"Quarterly","2"), HALFYEARLY(2,"Half Year","3") , YEARLY(3,"Yearly","4") ;
        private int value ;
        private String name;
        private String dbValue ;
        private PdPaymentMode(int value, String name, String dbValue) {
            this.value = value;
            this.name = name;
            this.dbValue=dbValue;
        }
 
        public String getName() {
            return name;
        }
        
        public int getValue() {
            return value;
        } 
        public String getDbValue(){
            return dbValue;
        }
    }
    
    private Integer paymentMethodId;
    private String paymentMethodName;
    private final Integer[] paymentModeId = new Integer []{0,0,0,0}; 

    public PaymentMethodStringCastor(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public PaymentMethodStringCastor(Integer paymentMethodId, String paymentMethodName) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }
    
    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    public void setNoPayPeriodOfMonthlyMode(Integer noPayPeriodAtFirstInstallment){
        paymentModeId[PdPaymentMode.MONTHLY.value] = noPayPeriodAtFirstInstallment;
    }

    public void setNoPayPeriodOfQuarterlyMode(Integer noPayPeriodAtFirstInstallment){
        paymentModeId[PdPaymentMode.QUARTERLY.value] = noPayPeriodAtFirstInstallment;
    }
    
    public void setNoPayPeriodOfHalfYearMode(Integer noPayPeriodAtFirstInstallment){
        paymentModeId[PdPaymentMode.HALFYEARLY.value] = noPayPeriodAtFirstInstallment;
    }
    
    public void setNoPayPeriodOfYearlyMode(Integer noPayPeriodAtFirstInstallment){
        paymentModeId[PdPaymentMode.YEARLY.value] = noPayPeriodAtFirstInstallment;
    }
    
    public int getNoPayPeriod(String selectedDbPaymentModeId) throws Exception{ // db value
        int result = 0;
        if ( PdPaymentMode.MONTHLY.dbValue.equalsIgnoreCase(selectedDbPaymentModeId)  ) result = this.paymentModeId[PdPaymentMode.MONTHLY.value];
        else if ( PdPaymentMode.QUARTERLY.dbValue.equalsIgnoreCase(selectedDbPaymentModeId)  ) result = this.paymentModeId[PdPaymentMode.QUARTERLY.value];
        else if ( PdPaymentMode.HALFYEARLY.dbValue.equalsIgnoreCase(selectedDbPaymentModeId)  ) result = this.paymentModeId[PdPaymentMode.HALFYEARLY.value];
        else if ( PdPaymentMode.YEARLY.dbValue.equalsIgnoreCase(selectedDbPaymentModeId)  ) result = this.paymentModeId[PdPaymentMode.YEARLY.value];
        else{     
            throw new Exception("Not found payment mode id = "+selectedDbPaymentModeId);
        }
        return result;
    }

    public Integer getNoPayPeriodOfMonthlyMode(){
        return paymentModeId[PdPaymentMode.MONTHLY.value];
    } 

    public Integer getNoPayPeriodOfQuarterlyMode(){
        return paymentModeId[PdPaymentMode.QUARTERLY.value];
    } 

    public Integer getNoPayPeriodOfHalfYearMode(){
        return paymentModeId[PdPaymentMode.HALFYEARLY.value];
    } 

    public Integer getNoPayPeriodOfYearlyMode(){
        return paymentModeId[PdPaymentMode.YEARLY.value];
    } 

    public void clearInstallmentNo(){
        paymentModeId[PdPaymentMode.MONTHLY.value] = 0;//N/A
        paymentModeId[PdPaymentMode.QUARTERLY.value] = 0;//N/A
        paymentModeId[PdPaymentMode.HALFYEARLY.value] = 0;//N/A
        paymentModeId[PdPaymentMode.YEARLY.value] = 0;//N/A
        
    }
    
    public List<PdPaymentMode> getActivePaymentMode(){
        List<PdPaymentMode> result = new ArrayList<PdPaymentMode>();
        if ( paymentModeId[PdPaymentMode.MONTHLY.value]>0 ) result.add(PdPaymentMode.MONTHLY);
        if ( paymentModeId[PdPaymentMode.QUARTERLY.value]>0 ) result.add(PdPaymentMode.QUARTERLY);
        if ( paymentModeId[PdPaymentMode.HALFYEARLY.value]>0 ) result.add(PdPaymentMode.HALFYEARLY);
        if ( paymentModeId[PdPaymentMode.YEARLY.value]>0 ) result.add(PdPaymentMode.YEARLY);
        return result;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName()+" : paymentMethodId="+this.paymentMethodId
                +", "+PdPaymentMode.MONTHLY.getName()+"="+this.paymentModeId[PdPaymentMode.MONTHLY.getValue()]
                +", "+PdPaymentMode.QUARTERLY.getName()+"="+this.paymentModeId[PdPaymentMode.QUARTERLY.getValue()]
                +", "+PdPaymentMode.HALFYEARLY.getName()+"="+this.paymentModeId[PdPaymentMode.HALFYEARLY.getValue()]
                +", "+PdPaymentMode.YEARLY.getName()+"="+this.paymentModeId[PdPaymentMode.YEARLY.getValue()]
                +" ] ";
    }    
    
}
