package com.maxelyz.core.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by support on 11/13/2017.
 */
@Entity
@Table(name = "sequence_no_file_detail")
@NamedQueries({@NamedQuery(name = "SequenceNoFileDetail.findAll", query = "SELECT s FROM SequenceNoFileDetail s")})
public class SequenceNoFileDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sequence_no")
    private String sequenceNo;

    @JoinColumn(name = "sequence_no_file_id", referencedColumnName = "id")
    @ManyToOne
    private SequenceNoFile sequenceNoFile;

    @Column(name = "status")
    private String status;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SequenceNoFile getSequenceNoFile() {
        return sequenceNoFile;
    }

    public void setSequenceNoFile(SequenceNoFile sequenceNoFile) {
        this.sequenceNoFile = sequenceNoFile;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof SequenceNoFileDetail)) {
            return false;
        }
        SequenceNoFileDetail other = (SequenceNoFileDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SequenceNoFileDetail[ id=" + id + " ]";
    }

}
