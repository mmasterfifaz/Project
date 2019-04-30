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
public class SoContentDetailDTO {

    private String activity_id;
    private Integer activity_time;
    private String source_subtype_enum_id;
    private String content;
    private String user_name;
    private String user_id;
    private String user_pic;
    private Date activity_time_format;

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
        return "DataObject [activity_time=" + activity_time + ", source_subtype_enum_id=" + source_subtype_enum_id + ", content="+ content + ", user_name="+ user_name + ", time_format="+ getActivity_time_format() + "]";

    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public Integer getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(Integer activity_time) {
        this.activity_time = activity_time;
    }

    public String getSource_subtype_enum_id() {
        return source_subtype_enum_id;
    }

    public void setSource_subtype_enum_id(String source_subtype_enum_id) {
        this.source_subtype_enum_id = source_subtype_enum_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
//        return user_pic==null?"http://profile.ak.fbcdn.net/static-ak/rsrc.php/v1/yh/r/C5yt7Cqf3zU.jpg":user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public void setActivity_time_format(Date activity_time_format) {
        this.activity_time_format = activity_time_format;
    }

    public Date getActivity_time_format() {
//        return activity_time_format;

        if (this.getActivity_time()!=null) {
            Calendar calendar = new GregorianCalendar();
            calendar.clear();
            return DateUtils.addSeconds(calendar.getTime(), this.getActivity_time());
        } else {
            return null;
        }
    }

    public void equalize() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        if (this.activity_time!=null) {
            this.activity_time_format = calendar.getTime();
            this.activity_time_format = DateUtils.addSeconds(this.activity_time_format, this.activity_time);
        }

//        if (this.user_pic==null) {
//            this.user_pic = "http://profile.ak.fbcdn.net/static-ak/rsrc.php/v1/yh/r/C5yt7Cqf3zU.jpg";
//        }
    }

}
