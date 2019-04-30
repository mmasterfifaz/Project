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
 * @author oat
 */
@Entity
@Table(name = "qa_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaChoice.findAll", query = "SELECT q FROM QaChoice q")})
public class QaChoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @Basic(optional = false)
    @Column(name = "label", nullable = false, length = 500)
    private String label;
    @Basic(optional = false)
    @Column(name = "value", nullable = false)
    private int value;
    @Basic(optional = false)
    @Column(name = "default1", nullable = false)
    private boolean default1;
    @JoinColumn(name = "qa_question_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private QaQuestion qaQuestion;

    public QaChoice() {
    }

    public QaChoice(Integer id) {
        this.id = id;
    }

    public QaChoice(Integer id, int seqNo, String label, int value, boolean default1) {
        this.id = id;
        this.seqNo = seqNo;
        this.label = label;
        this.value = value;
        this.default1 = default1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getDefault1() {
        return default1;
    }

    public void setDefault1(boolean default1) {
        this.default1 = default1;
    }

    public QaQuestion getQaQuestion() {
        return qaQuestion;
    }

    public void setQaQuestion(QaQuestion qaQuestion) {
        this.qaQuestion = qaQuestion;
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
        if (!(object instanceof QaChoice)) {
            return false;
        }
        QaChoice other = (QaChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaChoice[ id=" + id + " ]";
    }
    
}
