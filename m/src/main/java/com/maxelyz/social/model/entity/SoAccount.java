/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_account")
@NamedQueries({
    @NamedQuery(name = "SoAccount.findAll", query = "SELECT s FROM SoAccount s"),
    @NamedQuery(name = "SoAccount.findById", query = "SELECT s FROM SoAccount s WHERE s.id = :id"),
    @NamedQuery(name = "SoAccount.findByCode", query = "SELECT s FROM SoAccount s WHERE s.code = :code"),
    @NamedQuery(name = "SoAccount.findByName", query = "SELECT s FROM SoAccount s WHERE s.name = :name"),
    @NamedQuery(name = "SoAccount.findByDescription", query = "SELECT s FROM SoAccount s WHERE s.description = :description"),
    @NamedQuery(name = "SoAccount.findByKeyword", query = "SELECT s FROM SoAccount s WHERE s.keyword = :keyword"),
    @NamedQuery(name = "SoAccount.findByEnable", query = "SELECT s FROM SoAccount s WHERE s.enable = :enable"),
    @NamedQuery(name = "SoAccount.findByStatus", query = "SELECT s FROM SoAccount s WHERE s.status = :status"),
    @NamedQuery(name = "SoAccount.findByCreateBy", query = "SELECT s FROM SoAccount s WHERE s.createBy = :createBy"),
    @NamedQuery(name = "SoAccount.findByCreateDate", query = "SELECT s FROM SoAccount s WHERE s.createDate = :createDate"),
    @NamedQuery(name = "SoAccount.findByUpdateBy", query = "SELECT s FROM SoAccount s WHERE s.updateBy = :updateBy"),
    @NamedQuery(name = "SoAccount.findByUpdateDate", query = "SELECT s FROM SoAccount s WHERE s.updateDate = :updateDate")})
public class SoAccount implements Serializable {
    @ManyToMany(mappedBy = "soAccountCollection")
    private Collection<SoService> soServiceCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "soAccount")
    private SoEmailAccount soEmailAccount;
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
    @Column(name = "pt_room")
    private String ptRoom;
    @Basic(optional = false)
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
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

    @JoinColumn(name = "so_channel_id", referencedColumnName = "id")
    @ManyToOne
    private SoChannel soChannel;

    public SoAccount() {
    }

    public SoAccount(Integer id) {
        this.id = id;
    }

    public SoAccount(Integer id, String code, String name, String keyword) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.keyword = keyword;
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

    public SoChannel getSoChannel() {
        return soChannel;
    }

    public void setSoChannel(SoChannel soChannel) {
        this.soChannel = soChannel;
    }

    public String getPtRoom() {
        return ptRoom;
    }

    public void setPtRoom(String ptRoom) {
        this.ptRoom = ptRoom;
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
        if (!(object instanceof SoAccount)) {
            return false;
        }
        SoAccount other = (SoAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoAccount[ id=" + id + " ]";
    }

    public SoEmailAccount getSoEmailAccount() {
        return soEmailAccount;
    }

    public void setSoEmailAccount(SoEmailAccount soEmailAccount) {
        this.soEmailAccount = soEmailAccount;
    }

    public Collection<SoService> getSoServiceCollection() {
        return soServiceCollection;
    }

    public void setSoServiceCollection(Collection<SoService> soServiceCollection) {
        this.soServiceCollection = soServiceCollection;
    }
}
