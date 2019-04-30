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
 * @author oat
 */
@Entity
@Table(name = "qa_trans_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaTransDetail.findAll", query = "SELECT q FROM QaTransDetail q")})
public class QaTransDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @Column(name = "qa_category_name", length = 100)
    private String qaCategoryName;
    @Column(name = "question_code")
    private String questionCode;
    @Basic(optional = false)
    @Column(name = "question_name", nullable = false, length = 1000)
    private String questionName;
    @Column(name = "answer", length = 500)
    private String answer;
    @Column(name = "answer_desc", length = 200)
    private String answerDesc;
    @Column(name = "na", length = 1)
    private String na;
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @JoinColumn(name = "qa_trans_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private QaTrans qaTrans;
    @JoinColumn(name = "qa_category_id", referencedColumnName = "id")
    @ManyToOne
    private QaCategory qaCategory;

    public QaTransDetail() {
    }

    public QaTransDetail(Integer id) {
        this.id = id;
    }

    public QaTransDetail(Integer id, int seqNo, String questionName) {
        this.id = id;
        this.seqNo = seqNo;
        this.questionName = questionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getQaCategoryName() {
        return qaCategoryName;
    }

    public void setQaCategoryName(String qaCategoryName) {
        this.qaCategoryName = qaCategoryName;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public QaTrans getQaTrans() {
        return qaTrans;
    }

    public void setQaTrans(QaTrans qaTrans) {
        this.qaTrans = qaTrans;
    }

    public QaCategory getQaCategory() {
        return qaCategory;
    }

    public void setQaCategory(QaCategory qaCategory) {
        this.qaCategory = qaCategory;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
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
        if (!(object instanceof QaTransDetail)) {
            return false;
        }
        QaTransDetail other = (QaTransDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaTransDetail[ id=" + id + " ]";
    }
    
}
