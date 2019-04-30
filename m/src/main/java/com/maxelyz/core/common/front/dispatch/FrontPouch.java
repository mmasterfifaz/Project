/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.front.dispatch;

import java.io.Serializable;

/**
 *
 * @author DevTeam
 */
public abstract class FrontPouch implements Serializable{
    abstract void validatePouchBeforeSending()throws Exception;  
    abstract String getReceiverPage();
}
