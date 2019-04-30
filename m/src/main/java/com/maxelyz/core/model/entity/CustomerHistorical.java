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
@Table(name = "customer_historical")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerHistorical.findAll", query = "SELECT c FROM CustomerHistorical c")})
public class CustomerHistorical implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "customer_reference_no", nullable = false, length = 50)
    private String customerReferenceNo;
    @Column(name = "col1", length = 100)
    private String col1;
    @Column(name = "col2", length = 100)
    private String col2;
    @Column(name = "col3", length = 100)
    private String col3;
    @Column(name = "col4", length = 100)
    private String col4;
    @Column(name = "col5", length = 100)
    private String col5;
    @Column(name = "col6", length = 100)
    private String col6;
    @Column(name = "col7", length = 100)
    private String col7;
    @Column(name = "col8", length = 100)
    private String col8;
    @Column(name = "col9", length = 100)
    private String col9;
    @Column(name = "col10", length = 100)
    private String col10;
    @Column(name = "col11", length = 100)
    private String col11;
    @Column(name = "col12", length = 100)
    private String col12;
    @Column(name = "col13", length = 100)
    private String col13;
    @Column(name = "col14", length = 100)
    private String col14;
    @Column(name = "col15", length = 100)
    private String col15;
    @Column(name = "col16", length = 100)
    private String col16;
    @Column(name = "col17", length = 100)
    private String col17;
    @Column(name = "col18", length = 100)
    private String col18;
    @Column(name = "col19", length = 100)
    private String col19;
    @Column(name = "col20", length = 100)
    private String col20;
    @Column(name = "col21", length = 100)
    private String col21;
    @Column(name = "col22", length = 100)
    private String col22;
    @Column(name = "col23", length = 100)
    private String col23;
    @Column(name = "col24", length = 100)
    private String col24;
    @Column(name = "col25", length = 100)
    private String col25;
    @Column(name = "col26", length = 100)
    private String col26;
    @Column(name = "col27", length = 100)
    private String col27;
    @Column(name = "col28", length = 100)
    private String col28;
    @Column(name = "col29", length = 100)
    private String col29;
    @Column(name = "col30", length = 100)
    private String col30;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "customer_historical_group_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CustomerHistoricalGroup customerHistoricalGroup;

    public CustomerHistorical() {
    }

    public CustomerHistorical(Integer id) {
        this.id = id;
    }

    public CustomerHistorical(Integer id, String customerReferenceNo) {
        this.id = id;
        this.customerReferenceNo = customerReferenceNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerReferenceNo() {
        return customerReferenceNo;
    }

    public void setCustomerReferenceNo(String customerReferenceNo) {
        this.customerReferenceNo = customerReferenceNo;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public String getCol7() {
        return col7;
    }

    public void setCol7(String col7) {
        this.col7 = col7;
    }

    public String getCol8() {
        return col8;
    }

    public void setCol8(String col8) {
        this.col8 = col8;
    }

    public String getCol9() {
        return col9;
    }

    public void setCol9(String col9) {
        this.col9 = col9;
    }

    public String getCol10() {
        return col10;
    }

    public void setCol10(String col10) {
        this.col10 = col10;
    }

    public String getCol11() {
        return col11;
    }

    public void setCol11(String col11) {
        this.col11 = col11;
    }

    public String getCol12() {
        return col12;
    }

    public void setCol12(String col12) {
        this.col12 = col12;
    }

    public String getCol13() {
        return col13;
    }

    public void setCol13(String col13) {
        this.col13 = col13;
    }

    public String getCol14() {
        return col14;
    }

    public void setCol14(String col14) {
        this.col14 = col14;
    }

    public String getCol15() {
        return col15;
    }

    public void setCol15(String col15) {
        this.col15 = col15;
    }

    public String getCol16() {
        return col16;
    }

    public void setCol16(String col16) {
        this.col16 = col16;
    }

    public String getCol17() {
        return col17;
    }

    public void setCol17(String col17) {
        this.col17 = col17;
    }

    public String getCol18() {
        return col18;
    }

    public void setCol18(String col18) {
        this.col18 = col18;
    }

    public String getCol19() {
        return col19;
    }

    public void setCol19(String col19) {
        this.col19 = col19;
    }

    public String getCol20() {
        return col20;
    }

    public void setCol20(String col20) {
        this.col20 = col20;
    }

    public String getCol21() {
        return col21;
    }

    public void setCol21(String col21) {
        this.col21 = col21;
    }

    public String getCol22() {
        return col22;
    }

    public void setCol22(String col22) {
        this.col22 = col22;
    }

    public String getCol23() {
        return col23;
    }

    public void setCol23(String col23) {
        this.col23 = col23;
    }

    public String getCol24() {
        return col24;
    }

    public void setCol24(String col24) {
        this.col24 = col24;
    }

    public String getCol25() {
        return col25;
    }

    public void setCol25(String col25) {
        this.col25 = col25;
    }

    public String getCol26() {
        return col26;
    }

    public void setCol26(String col26) {
        this.col26 = col26;
    }

    public String getCol27() {
        return col27;
    }

    public void setCol27(String col27) {
        this.col27 = col27;
    }

    public String getCol28() {
        return col28;
    }

    public void setCol28(String col28) {
        this.col28 = col28;
    }

    public String getCol29() {
        return col29;
    }

    public void setCol29(String col29) {
        this.col29 = col29;
    }

    public String getCol30() {
        return col30;
    }

    public void setCol30(String col30) {
        this.col30 = col30;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public CustomerHistoricalGroup getCustomerHistoricalGroup() {
        return customerHistoricalGroup;
    }

    public void setCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        this.customerHistoricalGroup = customerHistoricalGroup;
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
        if (!(object instanceof CustomerHistorical)) {
            return false;
        }
        CustomerHistorical other = (CustomerHistorical) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerHistorical[ id=" + id + " ]";
    }
    
}
