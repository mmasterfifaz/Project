/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import com.maxelyz.utils.MxzDateTime;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "scl_contact_stats")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SclContactStats.findAll", query = "SELECT s FROM SclContactStats s"),
    @NamedQuery(name = "SclContactStats.findById", query = "SELECT s FROM SclContactStats s WHERE s.id = :id"),
//    @NamedQuery(name = "SclContactStats.findByUserId", query = "SELECT s FROM SclContactStats s WHERE s.userId = :userId"),
    @NamedQuery(name = "SclContactStats.findByUserGroupId", query = "SELECT s FROM SclContactStats s WHERE s.userGroupId = :userGroupId"),
    @NamedQuery(name = "SclContactStats.findByWorkDate", query = "SELECT s FROM SclContactStats s WHERE s.workDate = :workDate"),
    @NamedQuery(name = "SclContactStats.findByChannelId", query = "SELECT s FROM SclContactStats s WHERE s.channelId = :channelId"),
    @NamedQuery(name = "SclContactStats.findByPostTypeId", query = "SELECT s FROM SclContactStats s WHERE s.postTypeId = :postTypeId"),
    @NamedQuery(name = "SclContactStats.findByCustFeelingId", query = "SELECT s FROM SclContactStats s WHERE s.custFeelingId = :custFeelingId"),
    @NamedQuery(name = "SclContactStats.findByReceivedPost", query = "SELECT s FROM SclContactStats s WHERE s.receivedPost = :receivedPost"),
    @NamedQuery(name = "SclContactStats.findByReplyPost", query = "SELECT s FROM SclContactStats s WHERE s.replyPost = :replyPost"),
    @NamedQuery(name = "SclContactStats.findByIgnorePost", query = "SELECT s FROM SclContactStats s WHERE s.ignorePost = :ignorePost"),
    @NamedQuery(name = "SclContactStats.findByClosed", query = "SELECT s FROM SclContactStats s WHERE s.closed = :closed"),
    @NamedQuery(name = "SclContactStats.findByPending", query = "SELECT s FROM SclContactStats s WHERE s.pending = :pending"),
    @NamedQuery(name = "SclContactStats.findByTransfer", query = "SELECT s FROM SclContactStats s WHERE s.transfer = :transfer"),
    @NamedQuery(name = "SclContactStats.findByTotalWorkingTime", query = "SELECT s FROM SclContactStats s WHERE s.totalWorkingTime = :totalWorkingTime"),
    @NamedQuery(name = "SclContactStats.findByTotalResponseTime", query = "SELECT s FROM SclContactStats s WHERE s.totalResponseTime = :totalResponseTime")})
public class SclContactStats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
//    @Column(name = "user_id")
//    private Integer userId;
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @ManyToOne
    private Users users;
    @Column(name = "user_group_id")
    private Integer userGroupId;
    @Column(name = "work_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workDate;
    @Column(name = "channel_id")
    private Integer channelId;
    @Column(name = "post_type_id")
    private Integer postTypeId;
    @Column(name = "cust_feeling_id")
    private Integer custFeelingId;
    @Column(name = "received_post")
    private Integer receivedPost;
    @Column(name = "transfer")
    private Integer transfer;
    @Column(name = "transfer_out")
    private Integer transferOut;
    @Column(name = "balance_post")
    private Integer balancePost;
    @Column(name = "reply_post")
    private Integer replyPost;
    @Column(name = "reply_all_post")
    private Integer replyAllPost;
    @Column(name = "forward_post")
    private Integer forwardPost;
    @Column(name = "ignore_post")
    private Integer ignorePost;
    @Column(name = "assign")
    private Integer assign;
    @Column(name = "opened")
    private Integer opened;
    @Column(name = "wait_approve")
    private Integer waitApprove;
    @Column(name = "pending")
    private Integer pending;
    @Column(name = "ignore")
    private Integer ignore;
    @Column(name = "closed")
    private Integer closed;
    @Column(name = "total_working_time")
    private Integer totalWorkingTime;
    @Column(name = "total_response_time")
    private Integer totalResponseTime;
    @Column(name = "total_working_time_str")
    private String totalWorkingTimeStr;
    @Column(name = "total_response_time_str")
    private String totalResponseTimeStr;

    public SclContactStats() {
    }

    public SclContactStats(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(Integer postTypeId) {
        this.postTypeId = postTypeId;
    }

    public Integer getCustFeelingId() {
        return custFeelingId;
    }

    public void setCustFeelingId(Integer custFeelingId) {
        this.custFeelingId = custFeelingId;
    }

    public Integer getReceivedPost() {
        return receivedPost;
    }

    public void setReceivedPost(Integer receivedPost) {
        this.receivedPost = receivedPost;
    }

    public Integer getReplyPost() {
        return replyPost;
    }

    public void setReplyPost(Integer replyPost) {
        this.replyPost = replyPost;
    }

    public Integer getIgnorePost() {
        return ignorePost;
    }

    public void setIgnorePost(Integer ignorePost) {
        this.ignorePost = ignorePost;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getTransfer() {
        return transfer;
    }

    public void setTransfer(Integer transfer) {
        this.transfer = transfer;
    }

    public Integer getTotalWorkingTime() {
        return totalWorkingTime;
    }

    public void setTotalWorkingTime(Integer totalWorkingTime) {
        this.totalWorkingTime = totalWorkingTime;
    }

    public Integer getTotalResponseTime() {
        return totalResponseTime;
    }

    public void setTotalResponseTime(Integer totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
    }

//customize field
    public String getAvgWorkingTime() {
        if (totalWorkingTime==null) return "";

        return com.maxelyz.utils.MxzDateTime.secToMmSs(totalWorkingTime / (receivedPost==0?1:receivedPost));
    }

    public String getAvgResponseTime() {
        if (totalResponseTime==null) return "";

        return com.maxelyz.utils.MxzDateTime.secToMmSs(totalResponseTime / (receivedPost==0?1:receivedPost));
    }

    public Integer getReplyAllPost() {
        return replyAllPost;
    }

    public void setReplyAllPost(Integer replyAllPost) {
        this.replyAllPost = replyAllPost;
    }

    public Integer getForwardPost() {
        return forwardPost;
    }

    public void setForwardPost(Integer forwardPost) {
        this.forwardPost = forwardPost;
    }

    public Integer getAssign() {
        return assign;
    }

    public void setAssign(Integer assign) {
        this.assign = assign;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    public Integer getWaitApprove() {
        return waitApprove;
    }

    public void setWaitApprove(Integer waitApprove) {
        this.waitApprove = waitApprove;
    }

    public Integer getTransferOut() {
        return transferOut;
    }

    public void setTransferOut(Integer transferOut) {
        this.transferOut = transferOut;
    }

    public Integer getBalancePost() {
        return balancePost;
    }

    public void setBalancePost(Integer balancePost) {
        this.balancePost = balancePost;
    }

    public Integer getIgnore() {
        return ignore;
    }

    public void setIgnore(Integer ignore) {
        this.ignore = ignore;
    }

    public String getTotalWorkingTimeStr() {
        return totalWorkingTimeStr;
    }

    public void setTotalWorkingTimeStr(String totalWorkingTimeStr) {
        this.totalWorkingTimeStr = totalWorkingTimeStr;
    }

    public String getTotalResponseTimeStr() {
        return totalResponseTimeStr;
    }

    public void setTotalResponseTimeStr(String totalResponseTimeStr) {
        this.totalResponseTimeStr = totalResponseTimeStr;
    }

    //    public String getAvgWorkingTimeDiv() {
//        return "" + totalWorkingTime / receivedPost / 60;
//    }
//
//    public String getAvgWorkingTimeMod() {
//        String text = "0" + totalWorkingTime / receivedPost % 60;
//        return text.substring(text.length() - 2, text.length());
//    }
//
//    public String getAvgResponseTimeDiv() {
//        return "" + totalResponseTime / receivedPost / 60;
//    }
//
//    public String getAvgResponseTimeMod() {
//        String text = "0" + totalResponseTime / receivedPost % 60;
//        return text.substring(text.length() - 2, text.length());
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SclContactStats)) {
            return false;
        }
        SclContactStats other = (SclContactStats) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SclContactStats[ id=" + id + " ]";
    }
    }
