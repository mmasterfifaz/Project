/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "message_template_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessageTemplateCategory.findAll", query = "SELECT m FROM MessageTemplateCategory m"),
    @NamedQuery(name = "MessageTemplateCategory.findById", query = "SELECT m FROM MessageTemplateCategory m WHERE m.id = :id"),
    @NamedQuery(name = "MessageTemplateCategory.findByName", query = "SELECT m FROM MessageTemplateCategory m WHERE m.name = :name"),
    @NamedQuery(name = "MessageTemplateCategory.findByStatus", query = "SELECT m FROM MessageTemplateCategory m WHERE m.status = :status"),
    @NamedQuery(name = "MessageTemplateCategory.findByEnable", query = "SELECT m FROM MessageTemplateCategory m WHERE m.enable = :enable"),
    @NamedQuery(name = "MessageTemplateCategory.findByCreateBy", query = "SELECT m FROM MessageTemplateCategory m WHERE m.createBy = :createBy"),
    @NamedQuery(name = "MessageTemplateCategory.findByCreateDate", query = "SELECT m FROM MessageTemplateCategory m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MessageTemplateCategory.findByUpdateBy", query = "SELECT m FROM MessageTemplateCategory m WHERE m.updateBy = :updateBy"),
    @NamedQuery(name = "MessageTemplateCategory.findByUpdateDate", query = "SELECT m FROM MessageTemplateCategory m WHERE m.updateDate = :updateDate")})
public class MessageTemplateCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public MessageTemplateCategory() {
    }

    public MessageTemplateCategory(Integer id) {
        this.id = id;
    }

    public MessageTemplateCategory(Integer id, String name) {
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
        if (!(object instanceof MessageTemplateCategory)) {
            return false;
        }
        MessageTemplateCategory other = (MessageTemplateCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MessageTemplateCategory[ id=" + id + " ]";
    }
    
}
