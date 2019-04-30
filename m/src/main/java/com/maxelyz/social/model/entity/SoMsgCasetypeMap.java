/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_msg_casetype_map")
@NamedQueries({
    @NamedQuery(name = "SoMsgCasetypeMap.findAll", query = "SELECT s FROM SoMsgCasetypeMap s")})
public class SoMsgCasetypeMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "case_seq")
    private int caseSeq;
    @JoinColumn(name = "case_type_id", referencedColumnName = "id")
    @ManyToOne
    private SoCaseType soCaseType;
    //@Column(name = "case_type_id")
    //private Integer caseTypeId;
    @Size(max = 50)
    @Column(name = "case_type_code")
    private String caseTypeCode;
    @JoinColumn(name = "case_subtype_id", referencedColumnName = "id")
    @ManyToOne
    private SoCaseType soCaseTypeSub;
    //@Column(name = "case_subtype_id")
    //private Integer caseSubtypeId;
    @Size(max = 50)
    @Column(name = "case_subtype_code")
    private String caseSubtypeCode;
    @JoinColumn(name = "so_message_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SoMessage soMessage;

    public SoMsgCasetypeMap() {
    }

    public SoMsgCasetypeMap(Integer id) {
        this.id = id;
    }

    public SoMsgCasetypeMap(Integer id, int caseSeq) {
        this.id = id;
        this.caseSeq = caseSeq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCaseSeq() {
        return caseSeq;
    }

    public void setCaseSeq(int caseSeq) {
        this.caseSeq = caseSeq;
    }

    public SoCaseType getSoCaseType() {
        return soCaseType;
    }

    public void setSoCaseType(SoCaseType soCaseType) {
        this.soCaseType = soCaseType;
    }

    public SoCaseType getSoCaseTypeSub() {
        return soCaseTypeSub;
    }

    public void setSoCaseTypeSub(SoCaseType soCaseTypeSub) {
        this.soCaseTypeSub = soCaseTypeSub;
    }

    public String getCaseTypeCode() {
        return caseTypeCode;
    }

    public void setCaseTypeCode(String caseTypeCode) {
        this.caseTypeCode = caseTypeCode;
    }

    public String getCaseSubtypeCode() {
        return caseSubtypeCode;
    }

    public void setCaseSubtypeCode(String caseSubtypeCode) {
        this.caseSubtypeCode = caseSubtypeCode;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SoMsgCasetypeMap)) {
            return false;
        }
        SoMsgCasetypeMap other = (SoMsgCasetypeMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoMsgCasetypeMap[ id=" + id + " ]";
    }
    
}
