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
@Table(name = "message_template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessageTemplate.findAll", query = "SELECT m FROM MessageTemplate m"),
    @NamedQuery(name = "MessageTemplate.findById", query = "SELECT m FROM MessageTemplate m WHERE m.id = :id"),
//    @NamedQuery(name = "MessageTemplate.findByMessageTemplateCategory", query = "SELECT m FROM MessageTemplate m WHERE m.messageTemplateCategory = :messageTemplateCategory"),
    @NamedQuery(name = "MessageTemplate.findByName", query = "SELECT m FROM MessageTemplate m WHERE m.name = :name"),
    @NamedQuery(name = "MessageTemplate.findByMessage", query = "SELECT m FROM MessageTemplate m WHERE m.message = :message"),
    @NamedQuery(name = "MessageTemplate.findByType", query = "SELECT m FROM MessageTemplate m WHERE m.type = :type"),
    @NamedQuery(name = "MessageTemplate.findByStatus", query = "SELECT m FROM MessageTemplate m WHERE m.status = :status"),
    @NamedQuery(name = "MessageTemplate.findByEnable", query = "SELECT m FROM MessageTemplate m WHERE m.enable = :enable"),
    @NamedQuery(name = "MessageTemplate.findByCreateBy", query = "SELECT m FROM MessageTemplate m WHERE m.createBy = :createBy"),
    @NamedQuery(name = "MessageTemplate.findByCreateDate", query = "SELECT m FROM MessageTemplate m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MessageTemplate.findByUpdateBy", query = "SELECT m FROM MessageTemplate m WHERE m.updateBy = :updateBy"),
    @NamedQuery(name = "MessageTemplate.findByUpdateDate", query = "SELECT m FROM MessageTemplate m WHERE m.updateDate = :updateDate")})
public class MessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
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

    @JoinColumn(name = "message_template_category_id", referencedColumnName = "id")
    @ManyToOne
    private MessageTemplateCategory messageTemplateCategory;

    public MessageTemplate() {
    }

    public MessageTemplate(Integer id) {
        this.id = id;
    }

    public MessageTemplate(Integer id, MessageTemplateCategory messageTemplateCategory, String name, String message, String type) {
        this.id = id;
        this.messageTemplateCategory = messageTemplateCategory;
        this.name = name;
        this.message = message;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageTemplateCategory getMessageTemplateCategory() {
        return messageTemplateCategory;
    }

    public void setMessageTemplateCategory(MessageTemplateCategory messageTemplateCategory) {
        this.messageTemplateCategory = messageTemplateCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(object instanceof MessageTemplate)) {
            return false;
        }
        MessageTemplate other = (MessageTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MessageTemplate[ id=" + id + " ]";
    }
    
}
