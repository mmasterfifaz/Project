/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SoRptPostDTO {
    private String title;
    private long totalPost;

    private long reply;
    private long ignore;
    private long noReply;

    private long pendingPost;
    private long closedPost;
    private long noStatusPost;

    private long negative;
    private long neutral;
    private long positive;
    private long noSentimental;

    public SoRptPostDTO() {
    }

    public SoRptPostDTO(String title) {
        this.title = title;
    }

    public SoRptPostDTO(String title, long totalPost, long reply, long ignore, long noReply) {
        this.title = title;
        this.totalPost = totalPost;
        this.reply = reply;
        this.ignore = ignore;
        this.noReply = noReply;
    }

    public SoRptPostDTO(String title, long totalPost, long reply, long ignore, long noReply, long pendingPost, long closedPost, long noStatusPost, long negative, long neutral, long positive, long noSentimental) {
        this.title = title;
        this.totalPost = totalPost;

        this.reply = reply;
        this.ignore = ignore;
        this.noReply = noReply;

        this.pendingPost = pendingPost;
        this.closedPost = closedPost;
        this.noStatusPost = noStatusPost;

        this.negative = negative;
        this.neutral = neutral;
        this.positive = positive;
        this.noSentimental = noSentimental;
    }
    
    public SoRptPostDTO(Date title, long totalPost, long reply, long ignore, long noReply, long pendingPost, long closedPost, long noStatusPost, long negative, long neutral, long positive, long noSentimental) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        this.title = sdf.format(title);
        this.totalPost = totalPost;

        this.reply = reply;
        this.ignore = ignore;
        this.noReply = noReply;

        this.pendingPost = pendingPost;
        this.closedPost = closedPost;
        this.noStatusPost = noStatusPost;

        this.negative = negative;
        this.neutral = neutral;
        this.positive = positive;
        this.noSentimental = noSentimental;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getReply() {
        return reply;
    }

    public void setReply(long reply) {
        this.reply = reply;
    }

    public long getIgnore() {
        return ignore;
    }

    public void setIgnore(long ignore) {
        this.ignore = ignore;
    }

    public long getPendingPost() {
        return pendingPost;
    }

    public void setPendingPost(long pendingPost) {
        this.pendingPost = pendingPost;
    }

    public long getClosedPost() {
        return closedPost;
    }

    public void setClosedPost(long closedPost) {
        this.closedPost = closedPost;
    }

    public long getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(long totalPost) {
        this.totalPost = totalPost;
    }

    public long getNegative() {
        return negative;
    }

    public void setNegative(long negative) {
        this.negative = negative;
    }

    public long getNeutral() {
        return neutral;
    }

    public void setNeutral(long neutral) {
        this.neutral = neutral;
    }

    public long getPositive() {
        return positive;
    }

    public void setPositive(long positive) {
        this.positive = positive;
    }

    public long getNoReply() {
        return noReply;
    }

    public void setNoReply(long noReply) {
        this.noReply = noReply;
    }

    public long getNoStatusPost() {
        return noStatusPost;
    }

    public void setNoStatusPost(long noStatusPost) {
        this.noStatusPost = noStatusPost;
    }

    public long getNoSentimental() {
        return noSentimental;
    }

    public void setNoSentimental(long noSentimental) {
        this.noSentimental = noSentimental;
    }
}
