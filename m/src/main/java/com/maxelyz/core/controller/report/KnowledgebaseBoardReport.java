/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.utils.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;


  
@ManagedBean
@RequestScoped
public class KnowledgebaseBoardReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(KnowledgebaseBoardReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:commentkb:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
         super.init(); 
          
    }

    @Override
    public String getReportPath() {
        return "/report/knowledgebaseboard.jasper";
    }

    @Override
    public Map getParameterMap() {  
        Map parameterMap = new LinkedHashMap(); 
        parameterMap.put("knowledgebaseId", knowledgebaseId);
        parameterMap.put("knowledgebaseName", knowledgebaseName);
        parameterMap.put("knowledgebaseVersion", knowledgebaseVersion);
        parameterMap.put("knowledgebaseUpdate_by", knowledgebaseUpdate_by);
        parameterMap.put("knowledgebaseUpdate_date", knowledgebaseUpdate_date);
        
        return parameterMap;
    }

    @Override
    public String getQuery() { 
        String query = " SELECT  b.kbboard_comment as comment ,b.create_date as adddate ,b.create_by as addby "
                + "FROM  knowledgebase_board  b , knowledgebase_division  d "
                + "where b.knowledgebase_id = d.knowledgebase_id and  b.knowledgebase_version = d.version " ;
        if(knowledgebaseId > 0){
              query   += "and b.knowledgebase_id = '"+knowledgebaseId+"' and b.knowledgebase_version = '"+knowledgebaseVersion+"'";
        }
        query   += " order by b.create_date ";
        return query;
    }

}
