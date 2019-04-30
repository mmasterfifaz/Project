/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.model.entity.KnowledgebaseAttfile;
import com.maxelyz.utils.JSFUtil; 
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
public class KnowledgeFaxPopupController {

    private static Logger log = Logger.getLogger(KnowledgeFaxPopupController.class);
    private String faxfrom = "+6622222222";
    private String faxto;
    private String faxsubj;
    private String faxdesc;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    private Knowledgebase knowledgebase;
    private List<KnowledgebaseAttfile> knowledgebaseAttfileList;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:kb:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        String kbid = (String) JSFUtil.getRequestParameterMap("id");
        knowledgebase = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(kbid));

        if (knowledgebase != null) {
            faxsubj = knowledgebase.getTopic();
            //faxdesc = knowledgebase.getDescription();
            
            knowledgebaseAttfileList = new ArrayList<KnowledgebaseAttfile>();
            if (knowledgebase.getId() != null) {
                List<KnowledgebaseAttfile> list = this.getKnowledgebaseDAO().findKnowledgebaseAttFileById(knowledgebase.getId());

                for (KnowledgebaseAttfile s : list) {
                    s.setDelbox(true);
                    knowledgebaseAttfileList.add(s);
                }

            }
        }

    }

    public String getFaxdesc() {
        return faxdesc;
    }

    public void setFaxdesc(String faxdesc) {
        this.faxdesc = faxdesc;
    }

    public String getFaxsubj() {
        return faxsubj;
    }

    public void setFaxsubj(String faxsubj) {
        this.faxsubj = faxsubj;
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

    public String getFaxfrom() {
        return faxfrom;
    }

    public void setFaxfrom(String faxfrom) {
        this.faxfrom = faxfrom;
    }

    public String getFaxto() {
        return faxto;
    }

    public void setFaxto(String faxto) {
        this.faxto = faxto;
    }
    
    
     public void sendFax() {  
         
         
        
    }
    
}
