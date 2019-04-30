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
public class SoPostInfoDTO {

    private String object_id;
    private String user_name;
    private String user_id;
    private String user_pic;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Date getCreated_time_format() {
//        return created_time_format;

        if (this.getCreated_time()!=null) {
            Calendar calendar = new GregorianCalendar();
            calendar.clear();
            return DateUtils.addSeconds(calendar.getTime(), this.getCreated_time());
        } else {
            return null;
        }
    }

//    public void setCreated_time_format(Date created_time_format) {
//        this.created_time_format = created_time_format;
//    }

    public void equalize() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        if (this.created_time!=null) {
            this.created_time_format = calendar.getTime();
            this.created_time_format = DateUtils.addSeconds(this.created_time_format, this.created_time);
        }
    }

}
