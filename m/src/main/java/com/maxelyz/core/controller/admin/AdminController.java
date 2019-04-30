/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ukritj
 */
@ManagedBean
@RequestScoped
public class AdminController {
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setMainPage("admin");
    }
}
