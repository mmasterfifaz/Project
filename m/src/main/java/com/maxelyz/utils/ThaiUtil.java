/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author vee
 */
public class ThaiUtil {
    public static String bahtText(BigDecimal number) {
        DecimalFormat df = new DecimalFormat("###0.00");
        String textBath = df.format(number);
        for (int i = 0; i < textBath.length(); i++)
        {
            textBath = textBath.replace (",", ""); 
            textBath = textBath.replace (" ", "");
        }
        
        String[] txtNumArr = {"ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ"};
        String[] txtDigitArr = {"", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน"};
        String bahtText = "";
        String[] num = textBath.split("\\.");
        if (num[1].length() > 0)
        {
            num[1] = num[1].substring(0, 2);
        }
        
        int numberLen = num[0].length();
        for(int i = 0; i < numberLen; i++)
        {
            int tmp = Integer.valueOf(num[0].substring(i, i + 1));
            if (tmp != 0)
            {
                if ((i == (numberLen - 1)) && (tmp == 1) && (numberLen != 1)) {
                    bahtText += "เอ็ด";
                } else if ((i == (numberLen - 2)) && (tmp == 2)) {
                    bahtText += "ยี่";
                } else if ((i == (numberLen - 2)) && (tmp == 1)) {
                        bahtText += "";
                } else {
                    bahtText += txtNumArr[tmp];
                }
                bahtText += txtDigitArr[numberLen - i - 1];
            }
        }
        
        bahtText += "บาท";
        if ((num[1].equals("0")) || (num[1].equals("00")))
        {
            bahtText += "ถ้วน";
        } else  {
            int  decimalLen = num[1].length();
            for (int i = 0; i < decimalLen; i++)
            {
                int tmp = Integer.valueOf(num[1].substring(i, i + 1));
                if (tmp != 0)
                {
                    if ((i == (decimalLen - 1)) && (tmp == 1) && (decimalLen != 1)) {
                        bahtText += "เอ็ด";
                    } else if ((i == (decimalLen - 2)) && (tmp == 2)) {
                        bahtText += "ยี่";
                    } else if ((i == (decimalLen - 2)) && (tmp == 1)) {
                        bahtText += "";
                    } else {
                        bahtText += txtNumArr[tmp];
                    }
                    bahtText += txtDigitArr[decimalLen - i - 1];
                }
            }
            bahtText += "สตางค์";
        }
        return bahtText;
    }
    
        /** Creates a new instance of ThaiUtil */

    public static String Unicode2ASCII(String unicode) {

        StringBuffer ascii = new StringBuffer(unicode);

        int code;

        for(int i = 0; i < unicode.length(); i++) {

            code = (int)unicode.charAt(i);

            if ((0xE01<=code) && (code <= 0xE5B ))

                ascii.setCharAt( i, (char)(code - 0xD60));

        }

        return ascii.toString();

    }

    

    public static String ASCII2Unicode(String ascii) {

        StringBuffer unicode = new StringBuffer(ascii);

        int code;

        for(int i = 0; i < ascii.length(); i++) {

            code = (int)ascii.charAt(i);

            if ((0xA1 <= code) && (code <= 0xFB))

                unicode.setCharAt( i, (char)(code + 0xD60));

        }

        return unicode.toString();

    }
}
   

