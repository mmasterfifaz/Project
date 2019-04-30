/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import com.maxelyz.social.model.dao.SclMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.faces.bean.ManagedProperty;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "scl_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SclUsers.findAll", query = "SELECT s FROM SclUsers s"),
    @NamedQuery(name = "SclUsers.findById", query = "SELECT s FROM SclUsers s WHERE s.id = :id"),
    @NamedQuery(name = "SclUsers.findByUserId", query = "SELECT s FROM SclUsers s WHERE s.userId = :userId"),
    @NamedQuery(name = "SclUsers.findByName", query = "SELECT s FROM SclUsers s WHERE s.name = :name"),
    @NamedQuery(name = "SclUsers.findByFirstName", query = "SELECT s FROM SclUsers s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "SclUsers.findByLastName", query = "SELECT s FROM SclUsers s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "SclUsers.findByUserName", query = "SELECT s FROM SclUsers s WHERE s.userName = :userName"),
    @NamedQuery(name = "SclUsers.findByBirthdayDate", query = "SELECT s FROM SclUsers s WHERE s.birthdayDate = :birthdayDate"),
    @NamedQuery(name = "SclUsers.findBySex", query = "SELECT s FROM SclUsers s WHERE s.sex = :sex"),
    @NamedQuery(name = "SclUsers.findByEmail", query = "SELECT s FROM SclUsers s WHERE s.email = :email"),
    @NamedQuery(name = "SclUsers.findByPic", query = "SELECT s FROM SclUsers s WHERE s.pic = :pic")})
public class SclUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private BigInteger userId;
    @Column(name = "name")
    private String name;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "sex")
    private String sex;
    @Column(name = "email")
    private String email;
    @Column(name = "pic")
    private String pic;
    @Column(name = "birthday_date")
    private Integer birthdayDate;
    @Column(name = "birthday_date_format")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthdayDateFormat;

    public SclUsers() {
    }

    public SclUsers(Integer id) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Integer birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
        if (!(object instanceof SclUsers)) {
            return false;
        }
        SclUsers other = (SclUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SclUsers[ id=" + id + " ]";
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }


    public Date getBirthdayDateFormat() {
        return birthdayDateFormat;
    }

    public void setBirthdayDateFormat(Date birthdayDateFormat) {
        this.birthdayDateFormat = birthdayDateFormat;
    }

}
