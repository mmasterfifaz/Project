/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "qa_question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaQuestion.findAll", query = "SELECT q FROM QaQuestion q")})
public class QaQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 1000)
    private String name;
    @Column(name = "type", length = 100)
    private String type;
    @Basic(optional = false)
    @Column(name = "no_choice", nullable = false)
    private int noChoice;
    @Basic(optional = false)
    @Column(name = "show_na", nullable = false)
    private boolean showNa;
    @Basic(optional = false)
    @Column(name = "show_remark", nullable = false)
    private boolean showRemark;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qaQuestion")
    private Collection<QaChoice> qaChoiceCollection;
    @JoinColumn(name = "qa_category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private QaCategory qaCategory;

    public QaQuestion() {
    }

    public QaQuestion(Integer id) {
        this.id = id;
    }

    public QaQuestion(Integer id, String name, int noChoice, boolean showNa, boolean showRemark, int seqNo) {
        this.id = id;
        this.name = name;
        this.noChoice = noChoice;
        this.showNa = showNa;
        this.showRemark = showRemark;
        this.seqNo = seqNo;
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
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNoChoice() {
        return noChoice;
    }

    public void setNoChoice(int noChoice) {
        this.noChoice = noChoice;
    }

    public boolean getShowNa() {
        return showNa;
    }

    public void setShowNa(boolean showNa) {
        this.showNa = showNa;
    }

    public boolean getShowRemark() {
        return showRemark;
    }

    public void setShowRemark(boolean showRemark) {
        this.showRemark = showRemark;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @XmlTransient
    public Collection<QaChoice> getQaChoiceCollection() {
        return qaChoiceCollection;
    }

    public void setQaChoiceCollection(Collection<QaChoice> qaChoiceCollection) {
        this.qaChoiceCollection = qaChoiceCollection;
    }

    public QaCategory getQaCategory() {
        return qaCategory;
    }

    public void setQaCategory(QaCategory qaCategory) {
        this.qaCategory = qaCategory;
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
        if (!(object instanceof QaQuestion)) {
            return false;
        }
        QaQuestion other = (QaQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaQuestion[ id=" + id + " ]";
    }
    
}
