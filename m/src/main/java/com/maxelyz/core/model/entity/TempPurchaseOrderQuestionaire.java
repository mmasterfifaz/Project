/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "temp_purchase_order_questionaire")
@NamedQueries({
    @NamedQuery(name = "TempPurchaseOrderQuestionaire.findAll", query = "SELECT t FROM TempPurchaseOrderQuestionaire t")})
public class TempPurchaseOrderQuestionaire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_no")
    private Integer seqNo;   
    @Basic(optional = false)
    @Column(name = "question")
    private String question;
    @Basic(optional = false)
    @Column(name = "answer_choice")
    private Integer answerChoice;
    @Basic(optional = false)
    @Column(name = "answer")
    private String answer;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "purchase_order_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TempPurchaseOrderDetail tempPurchaseOrderDetail;

    public TempPurchaseOrderQuestionaire() {
    }

    public TempPurchaseOrderQuestionaire(Integer id) {
        this.id = id;
    }

    public TempPurchaseOrderQuestionaire(Integer id, String question, String answer, String description) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getAnswerChoice() {
        return answerChoice;
    }

    public void setAnswerChoice(Integer answerChoice) {
        this.answerChoice = answerChoice;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TempPurchaseOrderDetail getTempPurchaseOrderDetail() {
        return tempPurchaseOrderDetail;
    }

    public void setTempPurchaseOrderDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) {
        this.tempPurchaseOrderDetail = tempPurchaseOrderDetail;
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
        if (!(object instanceof TempPurchaseOrderQuestionaire)) {
            return false;
        }
        TempPurchaseOrderQuestionaire other = (TempPurchaseOrderQuestionaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.TempPurchaseOrderQuestionaire[id=" + id + "]";
    }

}
