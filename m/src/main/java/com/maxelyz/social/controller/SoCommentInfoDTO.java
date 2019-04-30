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
public class SoCommentInfoDTO {

    private String id;
    private String user_name;
    private String user_id;
    private String user_pic;
    private String content;
    private Integer time;
    private Date time_format;

//	private List<String> list = new ArrayList<String>() {
//	  {
//		add("String 1");
//		add("String 2");
//		add("String 3");
//	  }
//	};

    //getter and setter methods

    @Override
    public String toString() {
//        return "DataObject [id=" + id + ", user_name=" + user_name + ", content="+ content + ", time="+ time + "]";
//        return "DataObject [id=" + id + ", user_name=" + user_name + ", content="+ content + ", time="+ time + ", time_format="+ time_format + "]";
        return "DataObject [id=" + id + ", user_name=" + user_name + ", content="+ content + ", time="+ time + ", time_format="+ getTime_format() + "]";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Date getTime_format() {
//        return time_format;

        if (this.getTime()!=null) {
            Calendar calendar = new GregorianCalendar();
            calendar.clear();
            return DateUtils.addSeconds(calendar.getTime(), this.getTime());
        } else {
            return null;
        }
    }

//    public void setTime_format(Date time_format) {
//        this.time_format = time_format;
//    }

    public void equalize() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        if (this.time!=null) {
            this.time_format = calendar.getTime();
            this.time_format = DateUtils.addSeconds(this.time_format, this.time);
        }

    }

}
