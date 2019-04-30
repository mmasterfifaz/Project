package com.maxelyz.core.controller.login;

import com.maxelyz.utils.Message;
import javax.faces.bean.*;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class LicenseController {
    public String getLocalKey() {
        return Message.getLocalKey();
    }    
}
