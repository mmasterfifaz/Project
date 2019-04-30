/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.dto;

/**
 *
 * @author DevTeam
 */
public class FirstInstallmentOptions { 
    
 public enum InstallmentNumber { 
        NA(0), ONE(1),TWO(2), THREE(3), FOUR(4),FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),ELEVEN(11), TWELVE(12);
        private final int value;
        private InstallmentNumber(int value) {
            this.value=value;
        }

        public int getValue() {
            return value;
        }
        
        public String getLabel(){
            return (this.value==0)?"N/A":Integer.toString(this.value);
        }

        @Override
        public String toString() {
            return this.getClass().getName()+" [ value="+this.value+"] ";
        }
    }    
    
 public static final InstallmentNumber[] MONTHLY =   {InstallmentNumber.NA,InstallmentNumber.ONE,InstallmentNumber.TWO,
                                        InstallmentNumber.THREE,InstallmentNumber.FOUR,InstallmentNumber.FIVE,
                                        InstallmentNumber.SIX,InstallmentNumber.SEVEN,InstallmentNumber.EIGHT,
                                        InstallmentNumber.TEN,InstallmentNumber.ELEVEN,InstallmentNumber.TWELVE};
 public static final InstallmentNumber[] QUARTERLY =   {InstallmentNumber.NA,InstallmentNumber.ONE,InstallmentNumber.TWO,
                                        InstallmentNumber.THREE,InstallmentNumber.FOUR};
 public static final InstallmentNumber[] HALFYEAR =   {InstallmentNumber.NA,InstallmentNumber.ONE,InstallmentNumber.TWO};
 public static final InstallmentNumber[] YEARLY =   {InstallmentNumber.NA,InstallmentNumber.ONE};
 
    
   
    
}
