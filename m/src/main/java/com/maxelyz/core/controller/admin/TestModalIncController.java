package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class TestModalIncController {
    private static Logger log = Logger.getLogger(TestModalIncController.class);
    private String name;


   
    @PostConstruct
    public void initialize() {
       name = (String) JSFUtil.getRequestParameterMap("name");
    
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String saveAction() {
        return "/front/campaign/campaignAssignment.xhtml";
    }



}
