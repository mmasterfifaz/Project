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
@Table(name = "knowledgebase_board")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseBoard.findAll", query = "SELECT k FROM KnowledgebaseBoard k")})
public class KnowledgebaseBoard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kbboard_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer kbboardId;
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;
    @Column(name = "knowledgebase_version")
    private String knowledgebaseVersion;
    @Lob
    @Column(name = "kbboard_comment")
    private String kbboardComment;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    private Integer no;

    public KnowledgebaseBoard() {
    }

    public KnowledgebaseBoard(Integer kbboardId) {
        this.kbboardId = kbboardId;
    }

    public KnowledgebaseBoard(Integer kbboardId, int knowledgebaseId) {
        this.kbboardId = kbboardId;
        this.knowledgebaseId = knowledgebaseId;
    }

    public Integer getKbboardId() {
        return kbboardId;
    }

    public void setKbboardId(Integer kbboardId) {
        this.kbboardId = kbboardId;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    public String getKnowledgebaseVersion() {
        return knowledgebaseVersion;
    }

    public void setKnowledgebaseVersion(String knowledgebaseVersion) {
        this.knowledgebaseVersion = knowledgebaseVersion;
    }

    public String getKbboardComment() {
        return kbboardComment;
    }

    public void setKbboardComment(String kbboardComment) {
        this.kbboardComment = kbboardComment;
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

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kbboardId != null ? kbboardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseBoard)) {
            return false;
        }
        KnowledgebaseBoard other = (KnowledgebaseBoard) object;
        if ((this.kbboardId == null && other.kbboardId != null) || (this.kbboardId != null && !this.kbboardId.equals(other.kbboardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseBoard[ kbboardId=" + kbboardId + " ]";
    }
    
}
