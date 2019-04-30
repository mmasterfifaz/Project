package com.maxelyz.core.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by support on 12/20/2017.
 */
@Entity
@Table(name = "contact_result_mapping")
@NamedQueries({
        @NamedQuery(name = "ContactResultMapping.findAll", query = "SELECT c FROM ContactResultMapping c")})
public class ContactResultMapping implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult contactResultId;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContactResult getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(ContactResult contactResultId) {
        this.contactResultId = contactResultId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        if (!(object instanceof ContactResultMapping)) {
            return false;
        }
        ContactResultMapping other = (ContactResultMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactResultMapping[id=" + id + "]";
    }

}
