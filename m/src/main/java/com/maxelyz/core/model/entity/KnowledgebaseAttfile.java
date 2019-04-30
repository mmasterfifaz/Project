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
 * @author test01
 */
@Entity
@Table(name = "knowledgebase_attfile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseAttfile.findAll", query = "SELECT k FROM KnowledgebaseAttfile k")})
public class KnowledgebaseAttfile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kbattfile_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer kbattfileId;
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;
    @Column(name = "kbattfile_template_id")
    private Integer kbattfileTemplateId;
    @Column(name = "kbattfile_filename")
    private String kbattfileFilename;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @JoinColumn(name = "knowledgebase_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Knowledgebase knowledgebase;
private Boolean delbox;

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }


    public KnowledgebaseAttfile() {
    }

    public KnowledgebaseAttfile(Integer kbattfileId) {
        this.kbattfileId = kbattfileId;
    }

    public Integer getKbattfileId() {
        return kbattfileId;
    }

    public void setKbattfileId(Integer kbattfileId) {
        this.kbattfileId = kbattfileId;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    public Integer getKbattfileTemplateId() {
        return kbattfileTemplateId;
    }

    public void setKbattfileTemplateId(Integer kbattfileTemplateId) {
        this.kbattfileTemplateId = kbattfileTemplateId;
    }

    public String getKbattfileFilename() {
        return kbattfileFilename;
    }

    public void setKbattfileFilename(String kbattfileFilename) {
        this.kbattfileFilename = kbattfileFilename;
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

    public Boolean getDelbox() {
        return delbox;
    }

    public void setDelbox(Boolean delbox) {
        this.delbox = delbox;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kbattfileId != null ? kbattfileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseAttfile)) {
            return false;
        }
        KnowledgebaseAttfile other = (KnowledgebaseAttfile) object;
        if ((this.kbattfileId == null && other.kbattfileId != null) || (this.kbattfileId != null && !this.kbattfileId.equals(other.kbattfileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseAttfile[ kbattfileId=" + kbattfileId + " ]";
    }
    
}
