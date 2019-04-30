package com.maxelyz.core.model.value.admin;

public class CollectionReportValue {

    private Integer paymentId;
    private Integer userId;
    private String name;
    private String paymentName;
    private Integer sales;
    private Double netPremium1;
    private Double AFYP1;
    private Integer collected;
    private Double netPremium2;
    private Double AFYP2;
    private Double percent;

    public CollectionReportValue() {
    }

    public CollectionReportValue(Integer paymentId, Integer userId, String name, String paymentName, Integer sales, Double np1, Double a1, Integer collected, Double np2, Double a2) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.name = name;
        this.paymentName = paymentName;
        this.sales = sales;
        this.netPremium1 = np1;
        this.AFYP1 = a1;
        this.collected = collected;
        this.netPremium2 = np2;
        this.AFYP2 = a2;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Double getNetPremium1() {
        return netPremium1;
    }

    public void setNetPremium1(Double netPremium1) {
        this.netPremium1 = netPremium1;
    }

    public Double getAFYP1() {
        return AFYP1;
    }

    public void setAFYP1(Double AFYP1) {
        this.AFYP1 = AFYP1;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Double getNetPremium2() {
        return netPremium2;
    }

    public void setNetPremium2(Double netPremium2) {
        this.netPremium2 = netPremium2;
    }

    public Double getAFYP2() {
        return AFYP2;
    }

    public void setAFYP2(Double AFYP2) {
        this.AFYP2 = AFYP2;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
