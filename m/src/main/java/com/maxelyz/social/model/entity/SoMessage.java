/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_message")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "SoMessage.findAll", query = "SELECT s FROM SoMessage s"),
        @NamedQuery(name = "SoMessage.findById", query = "SELECT s FROM SoMessage s WHERE s.id = :id"),
        @NamedQuery(name = "SoMessage.findByRelatedId", query = "SELECT s FROM SoMessage s WHERE s.related_id = :related_id"),
//        @NamedQuery(name = "SoMessage.findByUserId", query = "SELECT s FROM SoMessage s WHERE s.user_id = :user_id"),
        @NamedQuery(name = "SoMessage.findByUserName", query = "SELECT s FROM SoMessage s WHERE s.user_name = :user_name"),
        @NamedQuery(name = "SoMessage.findBySourceId", query = "SELECT s FROM SoMessage s WHERE s.source_id = :source_id"),
//        @NamedQuery(name = "SoMessage.findBySourceType", query = "SELECT s FROM SoMessage s WHERE s.source_type = :source_type"),
//        @NamedQuery(name = "SoMessage.findBySourceSubtype", query = "SELECT s FROM SoMessage s WHERE s.source_subtype = :source_subtype"),
        @NamedQuery(name = "SoMessage.findByActivityTime", query = "SELECT s FROM SoMessage s WHERE s.activity_time = :activity_time"),
        @NamedQuery(name = "SoMessage.findByActivityTimeFormat", query = "SELECT s FROM SoMessage s WHERE s.activity_time_format = :activity_time_format"),
        @NamedQuery(name = "SoMessage.findByCollectTime", query = "SELECT s FROM SoMessage s WHERE s.collect_time = :collect_time"),
        @NamedQuery(name = "SoMessage.findByCollectTimeFormat", query = "SELECT s FROM SoMessage s WHERE s.collect_time_format = :collect_time_format"),
        @NamedQuery(name = "SoMessage.findByPriority", query = "SELECT s FROM SoMessage s WHERE s.priority_enum_id = :priority_enum_id"),
        @NamedQuery(name = "SoMessage.findBySentimental", query = "SELECT s FROM SoMessage s WHERE s.sentimental_enum_id = :sentimental_enum_id"),
        @NamedQuery(name = "SoMessage.findByCaseType", query = "SELECT s FROM SoMessage s WHERE s.casetype_enum_id = :casetype_enum_id"),
//        @NamedQuery(name = "SoMessage.findByCaseTag", query = "SELECT s FROM SoMessage s WHERE s.case_tag = :case_tag"),
        @NamedQuery(name = "SoMessage.findByMessageStatus", query = "SELECT s FROM SoMessage s WHERE s.message_status = :message_status")})
public class SoMessage implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "soMessage")
    private Collection<SoMsgCasetypeMap> soMsgCasetypeMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "soMessage")
    private Collection<SoActivityLog> soActivityLogCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "soMessage")
    private Collection<SoMsgUserWorkingtime> soMsgUserWorkingtimeCollection;
    @Size(max = 500)
    @Column(name = "case_tag")
    private String caseTag;
    @Column(name = "post_type_id")
    private Integer postTypeId;
    @Column(name = "em_related_id")
    private BigInteger emRelatedId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "soMessage")
    private SoEmailMessage soEmailMessage;
    @JoinColumn(name = "workflow_id", referencedColumnName = "id")
    @ManyToOne
    private SoWorkflowInstance soWorkflowInstance;
    @JoinColumn(name = "so_service_id", referencedColumnName = "id")
    @ManyToOne
    private SoService soService;
    @JoinColumn(name = "so_account_id", referencedColumnName = "id")
    @ManyToOne
    private SoAccount soAccount;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "related_id")
    private BigInteger related_id;

    @Column(name = "fb_related_id")
    private BigInteger fb_related_id;
    @Column(name = "tw_related_id")
    private BigInteger tw_related_id;
    @Column(name = "ln_related_id")
    private BigInteger ln_related_id;
    @Column(name = "ig_related_id")
    private BigInteger ig_related_id;
    @Column(name = "pt_related_id")
    private BigInteger pt_related_id;

    @Column(name = "user_id")
    private String user_id;
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "user_pic")
    private String user_pic;
    @Lob
    @Column(name = "content")
    private String content;
    @Column(name = "source_id")
    private String source_id;
    @Column(name = "source_type_enum_id")
    private String source_type_enum_id;
    @Column(name = "source_subtype_enum_id")
    private String source_subtype_enum_id;
    @Column(name = "casetype_enum_id")
    private String casetype_enum_id;
    @Column(name = "priority_enum_id")
    private String priority_enum_id;
    @Column(name = "sentimental_enum_id")
    private String sentimental_enum_id;

    @Column(name = "activity_id")
    private String activity_id;
    @Column(name = "activity_time")
    private Integer activity_time;
    @Column(name = "collect_time")
    private Integer collect_time;
    @Column(name = "activity_time_format")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activity_time_format;
    @Column(name = "collect_time_format")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collect_time_format;

//    @Column(name = "case_tag")
//    private String case_tag;
    @Column(name = "message_status")
    private String message_status;
    @Column(name = "case_status")
    private String case_status;
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assign_date;
    @Column(name = "receive_by_id")
    private Integer receive_by_id;
    @Column(name = "receive_by_name")
    private String receive_by_name;
    @Column(name = "receive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receive_date;
    @Column(name = "unassign")
    private Boolean unassign;
    @Column(name = "unassign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unassign_date;
    @Column(name = "unassign_by_id")
    private Integer unassign_by_id;
    @Column(name = "unassign_by_name")
    private String unassign_by_name;
    @Column(name = "remark")
    private String remark;
    @Column(name = "campaign_id")
    private Integer campaign_id;
    @Column(name = "product_id")
    private Integer product_id;
//    @Column(name = "post_type_id")
//    private Integer post_type_id;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;
    @Column(name = "create_by")
    private Integer create_by;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_date;
    @Column(name = "update_by")
    private Integer update_by;

    @Column(name = "account_user_id")
    private Integer account_user_id;
    @Column(name = "account_user_name")
    private String account_user_name;
    
    @Column(name = "reply_cnt")
    private Integer replyCnt;
    @Column(name = "replyall_cnt")
    private Integer replyallCnt;
    @Column(name = "forward_cnt")
    private Integer forwardCnt;
    
    @Column(name = "resp_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date respEndDate;
    
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private SoMessage parentSoMessage;
    
    @JoinColumn(name = "last_receive_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users lastReceiveByUsers;
    @Column(name = "last_receive_by_name")
    private String lastReceiveByName;
    @Column(name = "last_receive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastReceiveDate;
    @Column(name = "last_assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAssignDate;
    @Column(name = "ticket_id")
    private String ticketId;

    public SoMessage() {
    }

    public SoMessage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public BigInteger getRelated_id() {
//        return related_id;
//    }

    public BigInteger getRelated_id() {

        if (related_id!=null && related_id.compareTo(BigInteger.ZERO)>0){
            return related_id;
        }
        else if (fb_related_id!=null && fb_related_id.compareTo(BigInteger.ZERO)>0){
            return fb_related_id;
        }
        else if (tw_related_id!=null && tw_related_id.compareTo(BigInteger.ZERO)>0){
            return tw_related_id;
        }
        else if (ln_related_id!=null && ln_related_id.compareTo(BigInteger.ZERO)>0){
            return ln_related_id;
        }
        else if (ig_related_id!=null && ig_related_id.compareTo(BigInteger.ZERO)>0){
            return ig_related_id;
        }
        else if (pt_related_id!=null && pt_related_id.compareTo(BigInteger.ZERO)>0){
            return pt_related_id;
        }

        return related_id;
    }

    public void setRelated_id(BigInteger related_id) {
        this.related_id = related_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getSource_type_enum_id() {
        return source_type_enum_id;
    }

    public void setSource_type_enum_id(String source_type_enum_id) {
        this.source_type_enum_id = source_type_enum_id;
    }

    public String getSource_subtype_enum_id() {
        return source_subtype_enum_id;
    }

    public void setSource_subtype_enum_id(String source_subtype_enum_id) {
        this.source_subtype_enum_id = source_subtype_enum_id;
    }

    public Integer getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(Integer activity_time) {
        this.activity_time = activity_time;
    }

    public Date getActivity_time_format() {
        return activity_time_format;
    }

    public void setActivity_time_format(Date activity_time_format) {
        this.activity_time_format = activity_time_format;
    }

    public Integer getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(Integer collect_time) {
        this.collect_time = collect_time;
    }

    public Date getCollect_time_format() {
        return collect_time_format;
    }

    public void setCollect_time_format(Date collect_time_format) {
        this.collect_time_format = collect_time_format;
    }

//    public String getCase_tag() {
//        return case_tag;
//    }
//
//    public void setCase_tag(String case_tag) {
//        this.case_tag = case_tag;
//    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getCase_status() {
        return case_status;
    }

    public void setCase_status(String case_status) {
        this.case_status = case_status;
    }

    public Date getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(Date assign_date) {
        this.assign_date = assign_date;
    }

    public Integer getReceive_by_id() {
        return receive_by_id;
    }

    public void setReceive_by_id(Integer receive_by_id) {
        this.receive_by_id = receive_by_id;
    }

    public String getReceive_by_name() {
        return receive_by_name;
    }

    public void setReceive_by_name(String receive_by_name) {
        this.receive_by_name = receive_by_name;
    }

    public Date getReceive_date() {
        return receive_date;
    }

    public void setReceive_date(Date receive_date) {
        this.receive_date = receive_date;
    }

    public Boolean getUnassign() {
        return unassign;
    }

    public void setUnassign(Boolean unassign) {
        this.unassign = unassign;
    }

    public Date getUnassign_date() {
        return unassign_date;
    }

    public void setUnassign_date(Date unassign_date) {
        this.unassign_date = unassign_date;
    }

    public Integer getUnassign_by_id() {
        return unassign_by_id;
    }

    public void setUnassign_by_id(Integer unassign_by_id) {
        this.unassign_by_id = unassign_by_id;
    }

    public String getUnassign_by_name() {
        return unassign_by_name;
    }

    public void setUnassign_by_name(String unassign_by_name) {
        this.unassign_by_name = unassign_by_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(Integer campaign_id) {
        this.campaign_id = campaign_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

//    public Integer getPost_type_id() {
//        return post_type_id;
//    }
//
//    public void setPost_type_id(Integer post_type_id) {
//        this.post_type_id = post_type_id;
//    }

    public String getCasetype_enum_id() {
        return casetype_enum_id;
    }

    public void setCasetype_enum_id(String casetype_enum_id) {
        this.casetype_enum_id = casetype_enum_id;
    }

    public String getPriority_enum_id() {
        return priority_enum_id;
    }

    public void setPriority_enum_id(String priority_enum_id) {
        this.priority_enum_id = priority_enum_id;
    }

    public String getSentimental_enum_id() {
        return sentimental_enum_id;
    }

    public void setSentimental_enum_id(String sentimental_enum_id) {
        this.sentimental_enum_id = sentimental_enum_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Integer getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public Integer getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(Integer update_by) {
        this.update_by = update_by;
    }

    public Integer getAccount_user_id() {
        return account_user_id;
    }

    public void setAccount_user_id(Integer account_user_id) {
        this.account_user_id = account_user_id;
    }

    public String getAccount_user_name() {
        return account_user_name;
    }

    public void setAccount_user_name(String account_user_name) {
        this.account_user_name = account_user_name;
    }

    public BigInteger getFb_related_id() {
        return fb_related_id;
    }

    public void setFb_related_id(BigInteger fb_related_id) {
        this.fb_related_id = fb_related_id;
    }

    public BigInteger getTw_related_id() {
        return tw_related_id;
    }

    public void setTw_related_id(BigInteger tw_related_id) {
        this.tw_related_id = tw_related_id;
    }

    public BigInteger getLn_related_id() {
        return ln_related_id;
    }

    public void setLn_related_id(BigInteger ln_related_id) {
        this.ln_related_id = ln_related_id;
    }

    public BigInteger getIg_related_id() {
        return ig_related_id;
    }

    public void setIg_related_id(BigInteger ig_related_id) {
        this.ig_related_id = ig_related_id;
    }

    public BigInteger getPt_related_id() {
        return pt_related_id;
    }

    public void setPt_related_id(BigInteger pt_related_id) {
        this.pt_related_id = pt_related_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoMessage[ id=" + id + " ]";
    }

    public void initial() {

        if (this.priority_enum_id==null || this.priority_enum_id.equalsIgnoreCase("n/a")) {
            this.priority_enum_id = "PRIORITY_MED";
        }
    }

    public void equalize() {
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        if (this.activity_time!=null) {
            this.activity_time_format = DateUtils.addSeconds(calendar.getTime(), this.activity_time);
        }
        if (this.collect_time!=null) {
            this.collect_time_format = DateUtils.addSeconds(calendar.getTime(), this.collect_time);
        }

        this.related_id = getRelated_id();
    }

    public String getCaseTag() {
        return caseTag;
    }

    public void setCaseTag(String caseTag) {
        this.caseTag = caseTag;
    }

    public Integer getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(Integer postTypeId) {
        this.postTypeId = postTypeId;
    }

    public BigInteger getEmRelatedId() {
        return emRelatedId;
    }

    public void setEmRelatedId(BigInteger emRelatedId) {
        this.emRelatedId = emRelatedId;
    }

    public SoEmailMessage getSoEmailMessage() {
        return soEmailMessage;
    }

    public void setSoEmailMessage(SoEmailMessage soEmailMessage) {
        this.soEmailMessage = soEmailMessage;
    }

    public SoWorkflowInstance getSoWorkflowInstance() {
        return soWorkflowInstance;
    }

    public void setSoWorkflowInstance(SoWorkflowInstance soWorkflowInstance) {
        this.soWorkflowInstance = soWorkflowInstance;
    }

    public SoAccount getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(SoAccount soAccount) {
        this.soAccount = soAccount;
    }

    public SoService getSoService() {
        return soService;
    }

    public void setSoService(SoService soService) {
        this.soService = soService;
    }

    public SoMessage getParentSoMessage() {
        return parentSoMessage;
    }

    public void setParentSoMessage(SoMessage parentSoMessage) {
        this.parentSoMessage = parentSoMessage;
    }

    public Integer getForwardCnt() {
        return forwardCnt;
    }

    public void setForwardCnt(Integer forwardCnt) {
        this.forwardCnt = forwardCnt;
    }

    public Integer getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(Integer replyCnt) {
        this.replyCnt = replyCnt;
    }

    public Integer getReplyallCnt() {
        return replyallCnt;
    }

    public void setReplyallCnt(Integer replyallCnt) {
        this.replyallCnt = replyallCnt;
    }

    public Collection<SoMsgCasetypeMap> getSoMsgCasetypeMapCollection() {
        return soMsgCasetypeMapCollection;
    }

    public void setSoMsgCasetypeMapCollection(Collection<SoMsgCasetypeMap> soMsgCasetypeMapCollection) {
        this.soMsgCasetypeMapCollection = soMsgCasetypeMapCollection;
    }

    public Date getRespEndDate() {
        return respEndDate;
    }

    public void setRespEndDate(Date respEndDate) {
        this.respEndDate = respEndDate;
    }

    public Collection<SoActivityLog> getSoActivityLogCollection() {
        return soActivityLogCollection;
    }

    public void setSoActivityLogCollection(Collection<SoActivityLog> soActivityLogCollection) {
        this.soActivityLogCollection = soActivityLogCollection;
    }

    public Collection<SoMsgUserWorkingtime> getSoMsgUserWorkingtimeCollection() {
        return soMsgUserWorkingtimeCollection;
    }

    public void setSoMsgUserWorkingtimeCollection(Collection<SoMsgUserWorkingtime> soMsgUserWorkingtimeCollection) {
        this.soMsgUserWorkingtimeCollection = soMsgUserWorkingtimeCollection;
    }

    public Users getLastReceiveByUsers() {
        return lastReceiveByUsers;
    }

    public void setLastReceiveByUsers(Users lastReceiveByUsers) {
        this.lastReceiveByUsers = lastReceiveByUsers;
    }

    public String getLastReceiveByName() {
        return lastReceiveByName;
    }

    public void setLastReceiveByName(String lastReceiveByName) {
        this.lastReceiveByName = lastReceiveByName;
    }

    public Date getLastReceiveDate() {
        return lastReceiveDate;
    }

    public void setLastReceiveDate(Date lastReceiveDate) {
        this.lastReceiveDate = lastReceiveDate;
    }

    public Date getLastAssignDate() {
        return lastAssignDate;
    }

    public void setLastAssignDate(Date lastAssignDate) {
        this.lastAssignDate = lastAssignDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
