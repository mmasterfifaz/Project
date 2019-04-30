package com.maxelyz.social.controller;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apichatt
 * Date: 25/10/2556
 * Time: 11:08 น.
 * To change this template use File | Settings | File Templates.
 */
public class SoContentDetailPackDTO {

    private List<SoContentDetailDTO> master;
    private List<SoContentDetailDTO> children;

//	private List<String> list = new ArrayList<String>() {
//	  {
//		add("String 1");
//		add("String 2");
//		add("String 3");
//	  }
//	};

    //getter and setter methods


    public List<SoContentDetailDTO> getMaster() {
        return master;
    }

    public void setMaster(List<SoContentDetailDTO> master) {
        this.master = master;
    }

    public List<SoContentDetailDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SoContentDetailDTO> children) {
        this.children = children;
    }
}
