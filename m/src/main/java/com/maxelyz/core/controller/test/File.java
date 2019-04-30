/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.test;

/**
 *
 * @author admin
 */
public class File {

    private String Name;
    private String ext;
    private String mime;
    private long length;
    private byte[] data;
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            ext = name.substring(extDot +1);
            if("bmp".equals(ext)){
                mime="image/bmp";
            } else if("jpg".equals(ext)){
                mime="image/jpeg";
            } else if("gif".equals(ext)){
                mime="image/gif";
            } else if("png".equals(ext)){
                mime="image/png";
            } else {
                mime = "image/unknown";
            }
        }
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }

    public String getMime(){
        return mime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static void main(String[] args){
        String str = "Dear #customerName #customerSurname Ja";
        String[] strA = {"#customerName", "#customerSurname", "#customerAge"};
//        Integer i = str.indexOf("lo");
//        System.out.println(i);
//        String str1 = str.replaceAll("lo", "ta");
//        System.out.println(str1);


//        String str1 = "";
//        for(Integer i = 0; i < strA.length; i++){
//            String s = strA[i];
//            if(str.indexOf(s) != -1){
//                if(s.equals("#customerName")) str = str.replaceAll(s, "Ukrit");
//                if(s.equals("#customerSurname")) str = str.replaceAll(s, "Juicharoen");
//            }
//        }

        String str1 = "Hello World";
        if(str1.indexOf("world") != -1){
            System.out.println("Match");
        }

        System.out.println(str1);
    }
}
