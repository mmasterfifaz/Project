
package com.maxelyz.core.model.value.admin;

import java.util.Date;


public class RptContactChannelValue {
    private Date contactDate;
    private String channelName;
    private long total;
    private long pending;
    private long closed;

    public RptContactChannelValue(Date contactDate, String channelName, long total, long pending, long closed) {
        this.contactDate = contactDate;
        this.channelName = channelName;
        this.total = total;
        this.pending = pending;
        this.closed = closed;
    }

    public String getChannelName() {
        return channelName;
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
    
}
