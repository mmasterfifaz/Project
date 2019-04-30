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
public class PostInfo {

    private String object_id;
    private String user_name;
    private String content;
    private Integer created_time;
    private Date created_time_format;

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Date getCreated_time_format() {
//        return created_time_format;

        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        return DateUtils.addSeconds(calendar.getTime(), this.getCreated_time());
    }

//    public void setCreated_time_format(Date created_time_format) {
//        this.created_time_format = created_time_format;
//    }

    public void refresh() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        this.created_time_format = calendar.getTime();
        this.created_time_format = DateUtils.addSeconds(this.created_time_format, this.created_time);
    }

}
