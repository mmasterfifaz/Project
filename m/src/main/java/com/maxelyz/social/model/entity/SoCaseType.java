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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "so_case_type")
@NamedQueries({
    @NamedQuery(name = "SoCaseType.findAll", query = "SELECT s FROM SoCaseType s"),
    @NamedQuery(name = "SoCaseType.findById", query = "SELECT s FROM SoCaseType s WHERE s.id = :id"),
    @NamedQuery(name = "SoCaseType.findByCode", query = "SELECT s FROM SoCaseType s WHERE s.code = :code"),
    @NamedQuery(name = "SoCaseType.findByName", query = "SELECT s FROM SoCaseType s WHERE s.name = :name"),
    @NamedQuery(name = "SoCaseType.findByDescription", query = "SELECT s FROM SoCaseType s WHERE s.description = :description"),
    @NamedQuery(name = "SoCaseType.findByKeyword", query = "SELECT s FROM SoCaseType s WHERE s.keyword = :keyword"),
    @NamedQuery(name = "SoCaseType.findByEnable", query = "SELECT s FROM SoCaseType s WHERE s.enable = :enable"),
    @NamedQuery(name = "SoCaseType.findByStatus", query = "SELECT s FROM SoCaseType s WHERE s.status = :status"),
    @NamedQuery(name = "SoCaseType.findByDefaultPriority", query = "SELECT s FROM SoCaseType s WHERE s.defaultPriority = :defaultPriority"),
    @NamedQuery(name = "SoCaseType.findByCreateBy", query = "SELECT s FROM SoCaseType s WHERE s.createBy = :createBy"),
    @NamedQuery(name = "SoCaseType.findByCreateDate", query = "SELECT s FROM SoCaseType s WHERE s.createDate = :createDate"),
    @NamedQuery(name = "SoCaseType.findByUpdateBy", query = "SELECT s FROM SoCaseType s WHERE s.updateBy = :updateBy"),
    @NamedQuery(name = "SoCaseType.findByUpdateDate", query = "SELECT s FROM SoCaseType s WHERE s.updateDate = :updateDate")})
public class SoCaseType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Basic(optional = false)
    @Column(name = "default_priority")
    private String defaultPriority;
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
//    @Column(name = "parent_id")
//    private Integer parentId;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private SoCaseType soCaseType;
    
    public SoCaseType() {
    }

    public SoCaseType(Integer id) {
        this.id = id;
    }

    public SoCaseType(Integer id, String code, String name, String description, String keyword, String defaultPriority) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.keyword = keyword;
        this.defaultPriority = defaultPriority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(String defaultPriority) {
        this.defaultPriority = defaultPriority;
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

    //    public Integer getParentId() {
    //        return parentId;
    //    }
    //
    //    public void setParentId(Integer parentId) {
    //        this.parentId = parentId;
    //    }
    public SoCaseType getSoCaseType() {
        return soCaseType;
    }

    public void setSoCaseType(SoCaseType soCaseType) {
        this.soCaseType = soCaseType;
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
        if (!(object instanceof SoCaseType)) {
            return false;
        }
        SoCaseType other = (SoCaseType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoCaseType[ id=" + id + " ]";
    }
    
}
