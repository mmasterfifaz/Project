/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.model.entity.KnowledgebaseAttfile;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.MailBean;
import com.maxelyz.utils.SecurityUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author test01
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class KnowledgeMailPopupController {

    private static Logger log = Logger.getLogger(KnowledgeMailPopupController.class);
    private String mailfrom = "CSR_CRC@terrabit.co.th";
    private String namefrom = "";
    private String mailto = "";
    private String nameto = "";
    private String mailsubj;
    private String maildesc;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    private Knowledgebase knowledgebase;
    private List<KnowledgebaseAttfile> knowledgebaseAttfileList;
    private List<Knowledgebase> relateknowledgebases;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:kb:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        try {
            String kbid = (String) JSFUtil.getRequestParameterMap("id");
            knowledgebase = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(kbid));

            if (knowledgebase != null) {
                if (!JSFUtil.getUserSession().getUsers().getEmail().isEmpty()) {
                    mailfrom = JSFUtil.getUserSession().getUsers().getEmail();
                    namefrom = JSFUtil.getUserSession().getUsers().getName() + " " + JSFUtil.getUserSession().getUsers().getSurname();
                } else {
                    namefrom = "Administrator";
                }

                if (JSFUtil.getUserSession().getCustomerDetail() != null) {
                    mailto = JSFUtil.getUserSession().getCustomerDetail().getEmail();
                    nameto = "คุณ"+JSFUtil.getUserSession().getCustomerDetail().getName() + " " + JSFUtil.getUserSession().getCustomerDetail().getSurname();
                } else {
                    nameto = "ท่านผู้มีอุปการะคุณ";
                }
                mailsubj = knowledgebase.getTopic();
                maildesc = "<p>เรียน " + nameto + "</p>"
                        + "<p> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<p>"
                        + "<p> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br />"
                        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<p>" + "<p><br />"
                        + "ขอแสดงความนับถือ</p>"
                        + "<p>" + namefrom + "</p>";

                knowledgebaseAttfileList = new ArrayList<KnowledgebaseAttfile>();
                relateknowledgebases = new ArrayList<Knowledgebase>();
                if (knowledgebase.getId() != null) {
                    List<KnowledgebaseAttfile> list = this.getKnowledgebaseDAO().findKnowledgebaseAttFileById(knowledgebase.getId());

                    for (KnowledgebaseAttfile s : list) {
                        s.setDelbox(true);
                        knowledgebaseAttfileList.add(s);
                    }
                    List<Knowledgebase> nlist = this.getKnowledgebaseDAO().findKnowledgebaseRelateId(knowledgebase.getId(), knowledgebase.getVersion());
                    for (Knowledgebase n : nlist) {
                        List<KnowledgebaseAttfile> nalist = this.getKnowledgebaseDAO().findKnowledgebaseAttFileById(n.getId());
                        List<KnowledgebaseAttfile> nblist = new ArrayList<KnowledgebaseAttfile>();
                        if(nalist!=null){
                            for (KnowledgebaseAttfile sa : nalist) {
                        sa.setDelbox(false);
                        nblist.add(sa);
                    }
                        n.setKnowledgebaseAttfile(nblist);
                        }
                        relateknowledgebases.add(n);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String getMaildesc() {
        return maildesc;
    }

    public void setMaildesc(String maildesc) {
        this.maildesc = maildesc;
    }

    public String getMailsubj() {
        return mailsubj;
    }

    public void setMailsubj(String mailsubj) {
        this.mailsubj = mailsubj;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public List<KnowledgebaseAttfile> getKnowledgebaseAttfileList() {
        return knowledgebaseAttfileList;
    }

    public void setKnowledgebaseAttfileList(List<KnowledgebaseAttfile> knowledgebaseAttfileList) {
        this.knowledgebaseAttfileList = knowledgebaseAttfileList;
    }

    public String getMailfrom() {
        return mailfrom;
    }

    public void setMailfrom(String mailfrom) {
        this.mailfrom = mailfrom;
    }

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public List<Knowledgebase> getRelateknowledgebases() {
        return relateknowledgebases;
    }

    public void setRelateknowledgebases(List<Knowledgebase> relateknowledgebases) {
        this.relateknowledgebases = relateknowledgebases;
    }

    public void sendMail() {
        MailBean mail = new MailBean();
        mail.setFrom(mailfrom);
        mail.setTo(mailto);
        mail.setSubject(mailsubj);
        mail.setContent(maildesc);
        mail.sendMail();
    }

    public void addContentKb() {
        maildesc += "<hr/>" + knowledgebase.getDescription();
    }

    public void addContentRI(String cont) {
        maildesc += "<hr/>" + cont;
    }
}
