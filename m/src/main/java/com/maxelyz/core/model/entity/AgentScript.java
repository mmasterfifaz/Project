/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "agent_script")
@NamedQueries({
    @NamedQuery(name = "AgentScript.findAll", query = "SELECT a FROM AgentScript a"),
    @NamedQuery(name = "AgentScript.findById", query = "SELECT a FROM AgentScript a WHERE a.id = :id"),
    @NamedQuery(name = "AgentScript.findByPage", query = "SELECT a FROM AgentScript a WHERE a.page = :page"),
    @NamedQuery(name = "AgentScript.findByScript", query = "SELECT a FROM AgentScript a WHERE a.script = :script"),
    @NamedQuery(name = "AgentScript.findByUpdateBy", query = "SELECT a FROM AgentScript a WHERE a.updateBy = :updateBy"),
    @NamedQuery(name = "AgentScript.findByUpdateDate", query = "SELECT a FROM AgentScript a WHERE a.updateDate = :updateDate")})
public class AgentScript implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "page")
    private String page;
    @Basic(optional = false)
    @Column(name = "script")
    private String script;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public AgentScript() {
    }

    public AgentScript(Integer id) {
        this.id = id;
    }

    public AgentScript(Integer id, String page, String script) {
        this.id = id;
        this.page = page;
        this.script = script;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
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
        if (!(object instanceof AgentScript)) {
            return false;
        }
        AgentScript other = (AgentScript) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AgentScript[id=" + id + "]";
    }

}
