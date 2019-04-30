/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author test01
 */
@Entity
@Table(name = "knowledgebase_url")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseUrl.findAll", query = "SELECT k FROM KnowledgebaseUrl k")})
public class KnowledgebaseUrl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kburl_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer kburlId;
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;
    @Basic(optional = false)
    @Column(name = "knowledgebase_version")
    private String knowledgebaseVersion;
    @Column(name = "kburl_text")
    private String kburlText;
    @Column(name = "kburl_link")
    private String kburlLink;

    public KnowledgebaseUrl() {
    }

    public KnowledgebaseUrl(Integer kburlId) {
        this.kburlId = kburlId;
    }

    public KnowledgebaseUrl(Integer kburlId, int knowledgebaseId, String knowledgebaseVersion) {
        this.kburlId = kburlId;
        this.knowledgebaseId = knowledgebaseId;
        this.knowledgebaseVersion = knowledgebaseVersion;
    }

    public Integer getKburlId() {
        return kburlId;
    }

    public void setKburlId(Integer kburlId) {
        this.kburlId = kburlId;
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

    public String getKburlText() {
        return kburlText;
    }

    public void setKburlText(String kburlText) {
        this.kburlText = kburlText;
    }

    public String getKburlLink() {
        return kburlLink;
    }

    public void setKburlLink(String kburlLink) {
        this.kburlLink = kburlLink;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kburlId != null ? kburlId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseUrl)) {
            return false;
        }
        KnowledgebaseUrl other = (KnowledgebaseUrl) object;
        if ((this.kburlId == null && other.kburlId != null) || (this.kburlId != null && !this.kburlId.equals(other.kburlId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseUrl[ kburlId=" + kburlId + " ]";
    }
    
}
