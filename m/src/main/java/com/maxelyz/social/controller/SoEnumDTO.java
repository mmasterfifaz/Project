package com.maxelyz.social.controller;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: apichatt
 * Date: 25/10/2556
 * Time: 11:08 น.
 * To change this template use File | Settings | File Templates.
 */
public class SoEnumDTO {

    private String enum_id;
    private String keywords;
    private Boolean status;

    public String getEnum_id() {
        return enum_id;
    }

    public void setEnum_id(String enum_id) {
        this.enum_id = enum_id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
