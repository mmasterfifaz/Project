/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.converter;

import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author DevTeam
 */
public class PaymentModeConverter {

    public static List<PaymentMethodStringCastor> convertToPaymentModeList(String data) throws Exception {
        List<PaymentMethodStringCastor> result = new ArrayList<PaymentMethodStringCastor>();
        if (data != null && !data.isEmpty() ) { 
            String regularExp = "\\{\\d*,\\{\\d{1,2},\\d{1,2},\\d{1,2},\\d{1,2}\\}\\}";//0 means N/A
            Pattern pattern = Pattern.compile(regularExp); 
            Matcher matcher = pattern.matcher(data);
            while (matcher.find()) {
                String pm = matcher.group();
                String pmRemove = pm.replace("{", "").replace("}", "");
                String[] list = pmRemove.split(",");
                PaymentMethodStringCastor a = new PaymentMethodStringCastor(new Integer(list[0]));
                a.setNoPayPeriodOfMonthlyMode(new Integer(list[1]));
                a.setNoPayPeriodOfQuarterlyMode(new Integer(list[2]));
                a.setNoPayPeriodOfHalfYearMode(new Integer(list[3]));
                a.setNoPayPeriodOfYearlyMode(new Integer(list[4]));
                result.add(a);
            }
            if (data.length()>=1 && result.isEmpty()) throw new Exception("Incorect payment mode format (data="+data+", pattern should be {paymentModeId,{NoOfFirstInstallmentOfMonthlyMode,NoOfFirstInstallmentOfQuarterlyMode,NoOfFirstInstallmentOfHalfYearMode,NoOfFirstInstallmentOfYearlyMode}}, ..");
        }
        
        return result;
    }

    public static String convertToPaymentModeStr(List<PaymentMethodStringCastor> list) {
        String result = null;
        if (list != null && !list.isEmpty()) {
            String[] data = new String[list.size()];
            int i = 0;
            for (PaymentMethodStringCastor o : list) {
                StringBuilder elem = new StringBuilder("{");
                if (o.getPaymentMethodId() == null) {
                    continue;   
                }
                elem.append(o.getPaymentMethodId().toString())
                        .append(",{")
                        .append(o.getNoPayPeriodOfMonthlyMode() == null ? "0" : o.getNoPayPeriodOfMonthlyMode().toString() + ",")
                        .append(o.getNoPayPeriodOfQuarterlyMode() == null ? "0" : o.getNoPayPeriodOfQuarterlyMode().toString() + ",")
                        .append(o.getNoPayPeriodOfHalfYearMode() == null ? "0" : o.getNoPayPeriodOfHalfYearMode().toString() + ",")
                        .append(o.getNoPayPeriodOfYearlyMode() == null ? "0" : o.getNoPayPeriodOfYearlyMode().toString() + "}")
                        .append("}");
                data[i++] = elem.toString();
            }
            result = StringUtils.join(data, ",");
        }
        return result;
    }
    
    /* return map of (name, id) of product's paymentmode  */
    public static LinkedHashMap<String,String> findProductPaymentMode(List<PaymentMethodStringCastor> list) {
        LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
        if (list != null && !list.isEmpty()) {
            boolean[] checks = {false, false, false, false}; // broker of payment mode
            for (PaymentMethodStringCastor o : list) {
                if ( o.getNoPayPeriodOfMonthlyMode()!=null && o.getNoPayPeriodOfMonthlyMode().intValue() != 0 ) checks[PaymentMethodStringCastor.PdPaymentMode.MONTHLY.getValue()] = true;
                if ( o.getNoPayPeriodOfQuarterlyMode()!=null && o.getNoPayPeriodOfQuarterlyMode().intValue() != 0 ) checks[PaymentMethodStringCastor.PdPaymentMode.QUARTERLY.getValue()] = true;
                if ( o.getNoPayPeriodOfHalfYearMode()!=null && o.getNoPayPeriodOfHalfYearMode().intValue() != 0 ) checks[PaymentMethodStringCastor.PdPaymentMode.HALFYEARLY.getValue()] = true;
                if ( o.getNoPayPeriodOfYearlyMode()!=null && o.getNoPayPeriodOfYearlyMode().intValue() != 0 ) checks[PaymentMethodStringCastor.PdPaymentMode.YEARLY.getValue()] = true;
            }  
            // to order the list
            if (checks[PaymentMethodStringCastor.PdPaymentMode.MONTHLY.getValue()]) result.put(PaymentMethodStringCastor.PdPaymentMode.MONTHLY.getName(),PaymentMethodStringCastor.PdPaymentMode.MONTHLY.getDbValue());
            if (checks[PaymentMethodStringCastor.PdPaymentMode.QUARTERLY.getValue()]) result.put(PaymentMethodStringCastor.PdPaymentMode.QUARTERLY.getName(),PaymentMethodStringCastor.PdPaymentMode.QUARTERLY.getDbValue());
            if (checks[PaymentMethodStringCastor.PdPaymentMode.HALFYEARLY.getValue()]) result.put(PaymentMethodStringCastor.PdPaymentMode.HALFYEARLY.getName(),PaymentMethodStringCastor.PdPaymentMode.HALFYEARLY.getDbValue());
            if (checks[PaymentMethodStringCastor.PdPaymentMode.YEARLY.getValue()]) result.put(PaymentMethodStringCastor.PdPaymentMode.YEARLY.getName(),PaymentMethodStringCastor.PdPaymentMode.YEARLY.getDbValue());
                    
        }
        return result;
    }
    

    public static List<PaymentMethodStringCastor> findPaymentMethodByDbPaymentModeId(List<PaymentMethodStringCastor> list,String dbPaymentModeId){
        List<PaymentMethodStringCastor> result = new ArrayList<PaymentMethodStringCastor>();
        if ( dbPaymentModeId!=null && list!=null ){
            for(PaymentMethodStringCastor o : list){
                try{
                    if ( o.getNoPayPeriod(dbPaymentModeId)>0 ) result.add(o);
                }catch(Exception e){}
            }
        }
        return result;
    }

}
