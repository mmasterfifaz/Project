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
public class SoUserInfoDTO {

    private String user_id;
    private String name;
    private String first_name;
    private String last_name;
    private String username;
    private Integer birthday_date;
    private Date birthday_date_format;
    private String sex;
    private String email;
    private String pic;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBirthday_date() {
        return birthday_date;
    }

    public void setBirthday_date(Integer birthday_date) {
        this.birthday_date = birthday_date;
    }

    public Date getBirthday_date_format() {
//        return birthday_date_format;

        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        return DateUtils.addSeconds(calendar.getTime(), this.getBirthday_date());

    }

//    public void setBirthday_date_format(Date birthday_date_format) {
//        this.birthday_date_format = birthday_date_format;
//    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void equalize() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        if (this.birthday_date_format!=null) {
            this.birthday_date_format = calendar.getTime();
            this.birthday_date_format = DateUtils.addSeconds(this.birthday_date_format, this.birthday_date);
        }
    }
}
