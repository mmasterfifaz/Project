package com.maxelyz.core.model.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by support on 11/10/2017.
 */
@Entity
@Table(name = "sequence_no_file")
@NamedQueries({@NamedQuery(name = "SequenceNoFile.findAll", query = "SELECT s FROM SequenceNoFile s")})
public class SequenceNoFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code", length = 50)
    private String code;

    @JoinColumn(name = "sequence_no_id", referencedColumnName = "id")
    @ManyToOne
    private SequenceNo sequenceNo;

    @Column(name = "file_name", length = 50)
    private String fileName;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "total_record")
    private Integer totalRecord;
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

    public SequenceNoFile(){}

    public SequenceNoFile(Integer id) {
        this.id = id;
    }

    public SequenceNoFile(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
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

    public SequenceNo getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(SequenceNo sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SequenceNoFile)) {
            return false;
        }
        SequenceNoFile other = (SequenceNoFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SequenceNoFile[ id=" + id + " ]";
    }

}
