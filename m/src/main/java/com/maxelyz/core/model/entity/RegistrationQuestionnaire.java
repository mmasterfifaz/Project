/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "registration_questionnaire")
@NamedQueries({
    @NamedQuery(name = "RegistrationQuestionnaire.findAll", query = "SELECT r FROM RegistrationQuestionnaire r")})
public class RegistrationQuestionnaire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "no")
    private int no;
    @Basic(optional = false)
    @Column(name = "question")
    private String question;
    @JoinColumn(name = "registration_form_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegistrationForm registrationForm;

    public RegistrationQuestionnaire() {
    }

    public RegistrationQuestionnaire(Integer id) {
        this.id = id;
    }

    public RegistrationQuestionnaire(Integer id, int no, String question) {
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

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationFormId(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
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
        if (!(object instanceof RegistrationQuestionnaire)) {
            return false;
        }
        RegistrationQuestionnaire other = (RegistrationQuestionnaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RegistrationQuestionnaire[id=" + id + "]";
    }

}
