/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_channel")
@NamedQueries({
    @NamedQuery(name = "SoChannel.findAll", query = "SELECT s FROM SoChannel s"),
    @NamedQuery(name = "SoChannel.findById", query = "SELECT s FROM SoChannel s WHERE s.id = :id"),
    @NamedQuery(name = "SoChannel.findByEnable", query = "SELECT s FROM SoChannel s WHERE s.enable = :enable"),
    @NamedQuery(name = "SoChannel.findByStatus", query = "SELECT s FROM SoChannel s WHERE s.status = :status"),
    @NamedQuery(name = "SoChannel.findByName", query = "SELECT s FROM SoChannel s WHERE s.name = :name")})
public class SoChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "picture_small")
    private String pictureSmall;
    @Column(name = "picture_large")
    private String pictureLarge;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "seq_no")
    private Integer seqNo;

    public SoChannel() {
    }

    public SoChannel(Integer id) {
        this.id = id;
    }

    public SoChannel(Integer id, String code, String name) {
        this.id = id;
        this.name = name;
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

    public String getPictureSmall() {
        return pictureSmall;
    }

    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }

    public String getPictureLarge() {
        return pictureLarge;
    }

    public void setPictureLarge(String pictureLarge) {
        this.pictureLarge = pictureLarge;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
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
        if (!(object instanceof SoChannel)) {
            return false;
        }
        SoChannel other = (SoChannel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoChannel[ id=" + id + " ]";
    }
    
}
