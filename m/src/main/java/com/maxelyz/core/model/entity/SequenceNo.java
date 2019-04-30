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
 * @author oat
 */
@Entity
@Table(name = "sequence_no")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SequenceNo.findAll", query = "SELECT s FROM SequenceNo s")})
public class SequenceNo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "description", length = 1000)
    private String description;
    @Basic(optional = false)
    @Column(name = "abbr", nullable = false, length = 50)
    private String abbr;
    @Basic(optional = false)
    @Column(name = "running_length", nullable = false)
    private int runningLength;
    @Basic(optional = false)
    @Column(name = "next_runno", nullable = false)
    private int nextRunno;
    @Column(name = "reset_running_type", length = 50)
    private String resetRunningType;
    @Column(name = "format", length = 50)
    private String format;
    @Column(name = "month")
    private Integer month;
    @Column(name = "year")
    private Integer year;
    @Column(name = "year_era", length = 50)
    private String yearEra;
    @Column(name = "system")
    private Boolean system;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "generate_type", length = 50)
    private String generateType;

    public SequenceNo() {
    }

    public SequenceNo(Integer id) {
        this.id = id;
    }

    public SequenceNo(Integer id, String name, String abbr, int runningLength, int nextRunno) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
        this.runningLength = runningLength;
        this.nextRunno = nextRunno;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public int getRunningLength() {
        return runningLength;
    }

    public void setRunningLength(int runningLength) {
        this.runningLength = runningLength;
    }

    public int getNextRunno() {
        return nextRunno;
    }

    public void setNextRunno(int nextRunno) {
        this.nextRunno = nextRunno;
    }

    public String getResetRunningType() {
        return resetRunningType;
    }

    public void setResetRunningType(String resetRunningType) {
        this.resetRunningType = resetRunningType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getYearEra() {
        return yearEra;
    }

    public void setYearEra(String yearEra) {
        this.yearEra = yearEra;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getGenerateType() {
        return generateType;
    }

    public void setGenerateType(String generateType) {
        this.generateType = generateType;
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
        if (!(object instanceof SequenceNo)) {
            return false;
        }
        SequenceNo other = (SequenceNo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SequenceNo[ id=" + id + " ]";
    }
    
}
