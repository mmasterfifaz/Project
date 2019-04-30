/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SoRptPostAccountByDateDTO {
    private String title;

    private long account1;
    private long account2;
    private long account3;
    private long account4;
    private long account5;
    private long account6;
    private long account7;
    private long account8;
    private long account9;
    private long account10;

    public SoRptPostAccountByDateDTO() {
    }

    public SoRptPostAccountByDateDTO(String title) {
        this.title = title;
    }

    public SoRptPostAccountByDateDTO(Date title, long account1, long account2, long account3) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        this.title = sdf.format(title);
        this.account1 = account1;
        this.account2 = account2;
        this.account3 = account3;
    }

    public SoRptPostAccountByDateDTO(Date title, long account1, long account2, long account3, long account4) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        this.title = sdf.format(title);
        this.account1 = account1;
        this.account2 = account2;
        this.account3 = account3;
        this.account4 = account4;
    }

    public SoRptPostAccountByDateDTO(Date title, long account1, long account2, long account3, long account4, long account5) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        this.title = sdf.format(title);
        this.account1 = account1;
        this.account2 = account2;
        this.account3 = account3;
        this.account4 = account4;
        this.account5 = account5;
    }

    public SoRptPostAccountByDateDTO(Date title, long account1, long account2, long account3, long account4, long account5, long account6) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        this.title = sdf.format(title);
        this.account1 = account1;
        this.account2 = account2;
        this.account3 = account3;
        this.account4 = account4;
        this.account5 = account5;
        this.account6 = account6;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAccount1() {
        return account1;
    }

    public void setAccount1(long account1) {
        this.account1 = account1;
    }

    public long getAccount2() {
        return account2;
    }

    public void setAccount2(long account2) {
        this.account2 = account2;
    }

    public long getAccount3() {
        return account3;
    }

    public void setAccount3(long account3) {
        this.account3 = account3;
    }

    public long getAccount4() {
        return account4;
    }

    public void setAccount4(long account4) {
        this.account4 = account4;
    }

    public long getAccount5() {
        return account5;
    }

    public void setAccount5(long account5) {
        this.account5 = account5;
    }

    public long getAccount6() {
        return account6;
    }

    public void setAccount6(long account6) {
        this.account6 = account6;
    }

    public long getAccount7() {
        return account7;
    }

    public void setAccount7(long account7) {
        this.account7 = account7;
    }

    public long getAccount8() {
        return account8;
    }

    public void setAccount8(long account8) {
        this.account8 = account8;
    }

    public long getAccount9() {
        return account9;
    }

    public void setAccount9(long account9) {
        this.account9 = account9;
    }

    public long getAccount10() {
        return account10;
    }

    public void setAccount10(long account10) {
        this.account10 = account10;
    }
}
