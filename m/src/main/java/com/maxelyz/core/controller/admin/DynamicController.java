package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.component.html.HtmlInputText;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class DynamicController implements Serializable {

    private static Logger log = Logger.getLogger(DynamicController.class);
    private javax.faces.component.UIPanel myPanel;
    HtmlInputText htmlInput[] = new HtmlInputText[10];

    @PostConstruct
    public void initialize() {
    }

    public javax.faces.component.UIPanel getMyPanel() {
        return myPanel;
    }

    public void setMyPanel(javax.faces.component.UIPanel myPanel) {
        this.myPanel = myPanel;
        if (myPanel.getChildCount() <= 1) {
            for (int i = 0; i < htmlInput.length; i++) {
                htmlInput[i] = new HtmlInputText();
                htmlInput[i].setValue("my dynamic text" + i);
                htmlInput[i].setId("input" + i);
                htmlInput[i].setStyleClass("textfield");
                myPanel.getChildren().add(htmlInput[i]);
            }


        }
    }

    public void saveAction() {
        for (int i = 0; i < htmlInput.length; i++) {
            System.out.println(htmlInput[i].getValue());
        }
    }
}
