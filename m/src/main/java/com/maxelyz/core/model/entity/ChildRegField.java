/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name = "child_reg_field")
@NamedQueries({@NamedQuery(name = "ChildRegField.findAll", query = "SELECT c FROM ChildRegField c")})
public class ChildRegField implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "required")
    private boolean required;
    @Basic(optional = false)
    @Column(name = "custom_name")
    private String customName;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "group_no")
    private Integer groupNo;
    @Column(name = "control_type")
    private String controlType;
    @Column(name = "validation")
    private String validation;
    @Lob
    @Column(name = "items")
    private String items;
    @Column(name = "default_value")
    private String defaultValue;
        
    @JoinColumn(name = "child_reg_form_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ChildRegForm childRegForm;
    transient private Boolean selected;
    transient private String displayName;

    public ChildRegField() {
    }

    public ChildRegField(Integer id) {
        this.id = id;
    }

    public ChildRegField(Integer id, boolean required, String customName) {
        this.id = id;
        this.required = required;
        this.customName = customName;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }
    
    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public ChildRegForm getChildRegForm() {
        return childRegForm;
    }

    public void setChildRegForm(ChildRegForm childRegForm) {
        this.childRegForm = childRegForm;
    }
    
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
}