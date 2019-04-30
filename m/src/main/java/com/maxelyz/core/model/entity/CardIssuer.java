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
@Table(name = "card_issuer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CardIssuer.findAll", query = "SELECT c FROM CardIssuer c"),
    @NamedQuery(name = "CardIssuer.findById", query = "SELECT c FROM CardIssuer c WHERE c.id = :id"),
    @NamedQuery(name = "CardIssuer.findByCode", query = "SELECT c FROM CardIssuer c WHERE c.code = :code"),
    @NamedQuery(name = "CardIssuer.findByName", query = "SELECT c FROM CardIssuer c WHERE c.name = :name"),
    @NamedQuery(name = "CardIssuer.findByIssuerName", query = "SELECT c FROM CardIssuer c WHERE c.issuerName = :issuerName"),
    @NamedQuery(name = "CardIssuer.findByIssuerCountry", query = "SELECT c FROM CardIssuer c WHERE c.issuerCountry = :issuerCountry"),
    @NamedQuery(name = "CardIssuer.findByCardType", query = "SELECT c FROM CardIssuer c WHERE c.cardType = :cardType"),
    @NamedQuery(name = "CardIssuer.findByCreateBy", query = "SELECT c FROM CardIssuer c WHERE c.createBy = :createBy"),
    @NamedQuery(name = "CardIssuer.findByCreateDate", query = "SELECT c FROM CardIssuer c WHERE c.createDate = :createDate"),
    @NamedQuery(name = "CardIssuer.findByUpdateBy", query = "SELECT c FROM CardIssuer c WHERE c.updateBy = :updateBy"),
    @NamedQuery(name = "CardIssuer.findByUpdateDate", query = "SELECT c FROM CardIssuer c WHERE c.updateDate = :updateDate")})
public class CardIssuer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 6)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic(optional = false)
    @Column(name = "issuer_name", nullable = false, length = 100)
    private String issuerName;
    @Basic(optional = false)
    @Column(name = "issuer_country", nullable = false, length = 100)
    private String issuerCountry;
    @Column(name = "payment_card", length = 1)
    private String paymentCard; //C=Credit, D=Debit
    @Column(name = "card_type", length = 100)
    private String cardType;
    @Column(name = "card_list", length = 1000)
    private String cardList;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bank bank;
    
    public CardIssuer() {
    }

    public CardIssuer(Integer id) {
        this.id = id;
    }

    public CardIssuer(Integer id, String code, String name, String issuerName, String issuerCountry) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.issuerName = issuerName;
        this.issuerCountry = issuerCountry;
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

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssuerCountry() {
        return issuerCountry;
    }

    public void setIssuerCountry(String issuerCountry) {
        this.issuerCountry = issuerCountry;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCardList() {
        return cardList;
    }

    public void setCardList(String cardList) {
        this.cardList = cardList;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(String paymentCard) {
        this.paymentCard = paymentCard;
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
        if (!(object instanceof CardIssuer)) {
            return false;
        }
        CardIssuer other = (CardIssuer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CardIssuer[ id=" + id + " ]";
    }
    
}
