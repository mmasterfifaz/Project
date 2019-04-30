/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.dto;

import java.io.Serializable;

/**
 *
 * @author DevTeam
 */
public class KeyOptionDTO implements Serializable{
    private boolean selected = false;
    private String skey;
    private Integer ikey;
    private String svalue;
    private Integer ivalue;
    private boolean bvalue;
    private boolean dvalue;
    private Object ovalue;

    public KeyOptionDTO() {
    }

    public KeyOptionDTO(Integer ikey, String svalue, Integer ivalue, boolean bvalue) {
        this.ikey = ikey;
        this.svalue = svalue;
        this.ivalue = ivalue;
        this.bvalue = bvalue;
    }

    public KeyOptionDTO(Integer ikey, String svalue, Integer ivalue, boolean bvalue, boolean dvalue) {
        this.ikey = ikey;
        this.svalue = svalue;
        this.ivalue = ivalue;
        this.bvalue = bvalue;
        this.dvalue = dvalue;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+" [skey="+this.skey+", ikey="+this.ikey+", svalue="+this.svalue+", ivalue="+this.ivalue+", bvalue="+this.bvalue+", selected="+this.selected+" ]";
    }

    public void setBvalue(boolean bvalue) {
        this.bvalue = bvalue;
    }

    public boolean isBvalue() {
        return bvalue;
    }
    
    
    
    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the skey
     */
    public String getSkey() {
        return skey;
    }

    /**
     * @param skey the skey to set
     */
    public void setSkey(String skey) {
        this.skey = skey;
    }

    /**
     * @return the ikey
     */
    public Integer getIkey() {
        return ikey;
    }

    /**
     * @param ikey the ikey to set
     */
    public void setIkey(Integer ikey) {
        this.ikey = ikey;
    }

    /**
     * @return the svalue
     */
    public String getSvalue() {
        return svalue;
    }

    /**
     * @param svalue the svalue to set
     */
    public void setSvalue(String svalue) {
        this.svalue = svalue;
    }

    /**
     * @return the ivalue
     */
    public Integer getIvalue() {
        return ivalue;
    }

    /**
     * @param ivalue the ivalue to set
     */
    public void setIvalue(Integer ivalue) {
        this.ivalue = ivalue;
    }

    /**
     * @return the ovalue
     */
    public Object getOvalue() {
        return ovalue;
    }

    /**
     * @param ovalue the ovalue to set
     */
    public void setOvalue(Object ovalue) {
        this.ovalue = ovalue;
    }

    public boolean isDvalue() {
        return dvalue;
    }

    public void setDvalue(boolean dvalue) {
        this.dvalue = dvalue;
    }
    
    
}
