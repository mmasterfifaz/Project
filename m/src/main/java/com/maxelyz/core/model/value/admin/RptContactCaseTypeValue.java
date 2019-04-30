
package com.maxelyz.core.model.value.admin;

import java.util.Date;


public class RptContactCaseTypeValue {
    private Date contactDate;
    private String caseType;
    private long total;
    private long pending;
    private long closed;
    private long firstCallResolution;

    public RptContactCaseTypeValue(Date contactDate, String caseType, long total, long pending, long closed, long fistCallResolution) {
        this.contactDate = contactDate;
        this.caseType = caseType;
        this.total = total;
        this.pending = pending;
        this.closed = closed;
        this.firstCallResolution = fistCallResolution;
    }

    public String getCaseType() {
        return caseType;
    }

    public long getClosed() {
        return closed;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public long getPending() {
        return pending;
    }

    public long getTotal() {
        return total;
    }

    public long getFirstCallResolution() {
        return firstCallResolution;
    }
    
}
