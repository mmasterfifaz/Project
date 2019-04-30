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
@Table(name = "questionnaire_detail")
@NamedQueries({
    @NamedQuery(name = "QuestionnaireDetail.findAll", query = "SELECT q FROM QuestionnaireDetail q")})
public class QuestionnaireDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "no")
    private int no;
    @Basic(optional = false)
    @Column(name = "question")
    private String question;
    @Column(name = "answer1")
    private String answer1;
    @Column(name = "answer2")
    private String answer2;
    @Column(name = "answer3")
    private String answer3;
    @Column(name = "answer4")
    private String answer4;
    @Column(name = "answer5")
    private String answer5;
    @Column(name = "remark1")
    private Boolean remark1;
    @Column(name = "remark2")
    private Boolean remark2;
    @Column(name = "remark3")
    private Boolean remark3;
    @Column(name = "remark4")
    private Boolean remark4;
    @Column(name = "remark5")
    private Boolean remark5;
    @Column(name = "requireremark1")
    private Boolean requireremark1;
    @Column(name = "requireremark2")
    private Boolean requireremark2;
    @Column(name = "requireremark3")
    private Boolean requireremark3;
    @Column(name = "requireremark4")
    private Boolean requireremark4;
    @Column(name = "requireremark5")
    private Boolean requireremark5;
    @JoinColumn(name = "questionnaire_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Questionnaire questionnaire;

    public QuestionnaireDetail() {
    }

    public QuestionnaireDetail(Integer id) {
        this.id = id;
    }

    public QuestionnaireDetail(Integer id, int no, String question) {
        this.id = id;
        this.no = no;
        this.question = question;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public Boolean getRemark1() {
        return remark1;
    }

    public void setRemark1(Boolean remark1) {
        this.remark1 = remark1;
    }

    public Boolean getRemark2() {
        return remark2;
    }

    public void setRemark2(Boolean remark2) {
        this.remark2 = remark2;
    }

    public Boolean getRemark3() {
        return remark3;
    }

    public void setRemark3(Boolean remark3) {
        this.remark3 = remark3;
    }

    public Boolean getRemark4() {
        return remark4;
    }

    public void setRemark4(Boolean remark4) {
        this.remark4 = remark4;
    }

    public Boolean getRemark5() {
        return remark5;
    }

    public void setRemark5(Boolean remark5) {
        this.remark5 = remark5;
    }

    public Boolean getRequireremark1() {
        return requireremark1;
    }

    public void setRequireremark1(Boolean requireremark1) {
        this.requireremark1 = requireremark1;
    }

    public Boolean getRequireremark2() {
        return requireremark2;
    }

    public void setRequireremark2(Boolean requireremark2) {
        this.requireremark2 = requireremark2;
    }

    public Boolean getRequireremark3() {
        return requireremark3;
    }

    public void setRequireremark3(Boolean requireremark3) {
        this.requireremark3 = requireremark3;
    }

    public Boolean getRequireremark4() {
        return requireremark4;
    }

    public void setRequireremark4(Boolean requireremark4) {
        this.requireremark4 = requireremark4;
    }

    public Boolean getRequireremark5() {
        return requireremark5;
    }

    public void setRequireremark5(Boolean requireremark5) {
        this.requireremark5 = requireremark5;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
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
        if (!(object instanceof QuestionnaireDetail)) {
            return false;
        }
        QuestionnaireDetail other = (QuestionnaireDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QuestionnaireDetail[id=" + id + "]";
    }

}
