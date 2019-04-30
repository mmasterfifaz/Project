/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "registration_form")
@NamedQueries({
    @NamedQuery(name = "RegistrationForm.findAll", query = "SELECT r FROM RegistrationForm r")})
public class RegistrationForm implements Serializable {
    @JoinColumn(name = "questionnaire_id", referencedColumnName = "id")
    @ManyToOne
    private Questionnaire questionnaire;
    
    @JoinColumn(name = "qa_form_id", referencedColumnName = "id")
    @ManyToOne
    private QaForm qaForm;
    
    @JoinColumn(name = "qc_qa_form_id", referencedColumnName = "id")
    @ManyToOne
    private QaForm qcQaForm;
        
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationForm")
    private Collection<RegistrationQuestionnaire> registrationQuestionnaireCollection;
    @OneToMany(mappedBy = "registrationForm")
    private Collection<Product> productCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationForm")
    private Collection<RegistrationField> registrationFieldCollection;

    public RegistrationForm() {
    }

    public RegistrationForm(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
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
        if (!(object instanceof RegistrationForm)) {
            return false;
        }
        RegistrationForm other = (RegistrationForm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RegistrationForm[id=" + id + "]";
    }

    public Collection<RegistrationField> getRegistrationFieldCollection() {
        return registrationFieldCollection;
    }

    public void setRegistrationFieldCollection(Collection<RegistrationField> registrationFieldCollection) {
        this.registrationFieldCollection = registrationFieldCollection;
    }

    public Collection<RegistrationQuestionnaire> getRegistrationQuestionnaireCollection() {
        return registrationQuestionnaireCollection;
    }

    public void setRegistrationQuestionnaireCollection(Collection<RegistrationQuestionnaire> registrationQuestionnaireCollection) {
        this.registrationQuestionnaireCollection = registrationQuestionnaireCollection;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public QaForm getQaForm() {
        return qaForm;
    }

    public void setQaForm(QaForm qaForm) {
        this.qaForm = qaForm;
    }
    
    public QaForm getQcQaForm() {
        return qcQaForm;
    }

    public void setQcQaForm(QaForm qcQaForm) {
        this.qcQaForm = qcQaForm;
    }

}
