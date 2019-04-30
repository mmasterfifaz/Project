/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.utils.JSFUtil;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public class MarketingDAO {

    private static Logger log = Logger.getLogger(MarketingDAO.class);
    @PersistenceContext
    private EntityManager em;

    public void create(Marketing marketing) throws PreexistingEntityException, Exception {
        em.persist(marketing);
    }

    public void createMarketingCriteria(MarketingCriteria marketingCriteria) throws PreexistingEntityException, Exception {
        em.persist(marketingCriteria);
    }

    public void create(String sessionId, Marketing marketing, File dataFile, List<FileTemplateMapping> fileTemplateMappings) throws PreexistingEntityException, Exception {
        System.out.println("Start Delete MarketingTempCustomer from Session : " + sessionId + " ," + new Date());
        this.deleteMarketingTempCustomerFromSessionId(sessionId);
        System.out.println("Start Insert MarketingTempCustomer from Session : " + sessionId + " ," + new Date());
        this.insertMarketingTempCustomerFromFile(marketing, dataFile, fileTemplateMappings);

        // CHECK DUPLICATE WITHIN IMPORT LIST BY NAME AND SURNAME OF CUSTOMER
        System.out.println("Start Check DupWithin from Session : " + sessionId + " ," + new Date());
        this.checkDupWithin(sessionId);
        
        // CHECK IMPORT LIST WITH YES SALE(PO-ALL APPROVED STATUS) IN EXISTING CUSTOMER BY NAME AND SURNAME
        if(marketing.getPeriodYessale() != null && marketing.getPeriodYessale() != 0) {
            System.out.println("Start Check Yes Sale from Session : " + sessionId + " ," + new Date());
            this.checkYesSale(sessionId, marketing);
        }
        
        // CHECK IMPORT LIST WITH OP_OUT TABLE
        if (marketing.getPeriodBlacklist() != null && marketing.getPeriodBlacklist() != 0) {
            System.out.println("Start Check Op-out from Session : " + sessionId + " ," + new Date());
            this.checkOpout(sessionId, marketing);
        }
        
        // ALWAY CHECK IMPORT LIST WITH BLACK LIST IN OP_OUT TABLE
        System.out.println("Start Check Blacklist from Session : " + sessionId + " ," + new Date());
        this.checkBlacklist(sessionId, marketing);
        
        // CHECK FORMAT OF PHONE NO IN IMPORT LIST (MOBILE 10 LENGTH 08, 09, 06, 01) (HOME 9 LENGTH BEGIN WITH 0) (OFFICE 9 LENGTH BEGIN WITH 0)
        if (marketing.getCheckFormatTelNo()) {
            System.out.println("Start Check Format TelNo from Session : " + sessionId + " ," + new Date());
            this.checkTelephoneNo(sessionId);
        }
        
        // CHECK IN STORE PROCEDURE FOR DRTV PROJECT
        if (JSFUtil.getApplication().isExternalDnc()) {
            System.out.println("Start Check DNC from Session : " + sessionId + " ," + new Date());
            this.checkDnc(sessionId, marketing);
            System.out.println("Start Check MIB from Session : " + sessionId + " ," + new Date());
            this.checkMib(sessionId, marketing);
        }
        
        // CHECK DUPLICATE CUSTOMER BETWEEN IMPORT LIST AND CUSTOMER BY NAME AND SURNAME
        if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() != 0) {
            System.out.println("Start Check Duplicate Customer from Session : " + sessionId + " ," + new Date());
            this.checkDupCustomer(sessionId, marketing);
        }
        
        // ALWAYS CHECK DUPLICATE CUSTOMER BETWEEN IMPORT LIST AND CUSTOMER BY NAME AND SURNAME FOR MERGE PARENT CHILD
        System.out.println("Start Check Merge Duplicate Customer from Session : " + sessionId + " ," + new Date());
        this.checkMergeDupCustomer(sessionId, marketing);
        
        System.out.println("End Create MarketingTempCustomer from Session : " + sessionId + " ," + new Date());
    }    
    
    public void insertMarketingTempCustomerFromFile(Marketing marketing, File dataFile, List<FileTemplateMapping> fileTemplateMappings) throws Exception {
        String charset, fieldEnclosed, fieldDelimiter;
        Long totalDup = 0l;
        Long totalOpOut = 0l;
        FileTemplate fileTemplate = marketing.getFileTemplate();
        List<MarketingTempCustomer> mtcList = new ArrayList<MarketingTempCustomer>();

        Map<Integer, FileTemplateMapping> fileMap = new TreeMap<Integer, FileTemplateMapping>();
        Map<FileTemplateMapping, String> dataMap = new HashMap<FileTemplateMapping, String>(); //customer_field_name, datafromfile

        for (FileTemplateMapping fileTemplateMapping : fileTemplateMappings) { //sort by FileColumnNo
            fileMap.put(fileTemplateMapping.getFileColumnNo(), fileTemplateMapping);
        }

        if (fileTemplate.getUnicode()) {
            charset = "utf-8";
        } else {
            charset = "tis-620";
        }
        
        fieldDelimiter = FileTemplateDAO.getFieldDelimiter(fileTemplate);

        BufferedReader dataIns = null;
        CSVReader reader = null;
        try {
            dataIns = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), charset));
            String data, column;
            int recordNo = 0;
          
            char DEFAULT_SEPARATOR =  fieldDelimiter.charAt(0);    //'\t';
            char DEFAULT_QUOTE = '"';
            
            reader = new CSVReader(new FileReader(dataFile), DEFAULT_SEPARATOR, DEFAULT_QUOTE);
            String[] line;

            //Skip first line
            if (fileTemplate.getHasColumnName()) {
                reader.readNext();  //skip
            }
            while ((line = reader.readNext()) != null) {
                int noofColumn = line.length;

                if(noofColumn < fileMap.size()) {    
                    // When File Format and Data is not Compatible
                    IndexOutOfBoundsException e = new IndexOutOfBoundsException();
                    throw e;
                }

                Integer columnNo = 0;

                dataMap.clear();
                //get column
                while (columnNo < noofColumn) {
                    column = line[columnNo]; ////read column data ->idx begin at 0
                    columnNo++;                    
                    if (fileMap.get(columnNo) != null) { //if has column in FileTemplateMapping ->begin at 1
                        dataMap.put(fileMap.get(columnNo), column);
                    }
                }
                MarketingTempCustomer mtc = new MarketingTempCustomer();
                mtc.setSessionId(JSFUtil.getSession().getId());
                mtc.setListStatus("new");
                mtc.setCustomerType(1); //Prospect Type
                mtc.setGender("");
                mtc.setHomePhone("");
                mtc.setOfficePhone("");
                mtc.setMobilePhone("");
                mtc.setDupWithin(Boolean.FALSE);
                mtc.setDupYes(Boolean.FALSE);
                mtc.setDup(Boolean.FALSE);
                mtc.setOpout(Boolean.FALSE);
                mtc.setBlacklist(Boolean.FALSE);
                mtc.setBadphone(Boolean.FALSE);
                mtc.setBadformat(Boolean.FALSE);
                mtc.setDnc(Boolean.FALSE);
                mtc.setMib(Boolean.FALSE);
          
                for (Map.Entry<FileTemplateMapping, String> e : dataMap.entrySet()) {
                    String customerFieldName = e.getKey().getCustomerField().getMappingField();
                    String type = e.getKey().getFieldType();
                    String pattern = e.getKey().getPattern();
                    String defaultValue = e.getKey().getDefaultValue();

                    //Check Bad Format For Name is blank
                    if (customerFieldName.equals("customer.name")) {     // || customerFieldName.equals("customer.surname")) {
                        if (e.getValue() == null || e.getValue().equals("")) {
                            mtc.setBadformat(Boolean.TRUE);
                        }
                    }

                    //Check Bad Format From template mapping
                    if (customerFieldName.startsWith("customer.flexfield")) {
                        if (e.getValue() == null || e.getValue().equals("")) {
                            e.setValue(defaultValue);
                        }

                        if (type.equals("Date")) {
                            try {                      
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                                if(e.getValue().toUpperCase().equals("TODAY")) {                                      
                                    Date today = new Date();                                
                                    e.setValue(sdf.format(today));
                                } else {
                                    Date dateCheck = sdf.parse(e.getValue());
                                    e.setValue(sdf.format(dateCheck));
                                }
                            } catch (Exception exn) {
                                mtc.setBadformat(Boolean.TRUE);
                            }
                        } else if (type.equals("Number")) {
                            try {
                                Double.parseDouble(e.getValue());
                            } catch (Exception exn) {
                                mtc.setBadformat(Boolean.TRUE);
                            }
                        }
                    }
                    this.setMarketingTempCustomerValue(mtc, customerFieldName, e.getValue());      // Mapping Value into field
                }
                recordNo++;
                try {
                    mtcList.add(mtc);
                } catch (Exception e) {
                    System.out.println(recordNo + " : " + mtc.getName() + " " + mtc.getSurname() + " : " + mtc.getMobilePhone() + " : " + mtc.getHomePhone());

                }
            }
            marketing.setTotalTempRecord(recordNo);
            for (MarketingTempCustomer obj : mtcList) {
                em.persist(obj); //insert new customer
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataIns != null) {
                    dataIns.close();
                }
                if(reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public int deleteMarketingTempCustomerFromSessionId(String sessionId) {
        Query q = em.createQuery("delete from MarketingTempCustomer as m where m.sessionId = :sessionId and m.marketingId is null ");
        q.setParameter("sessionId", sessionId);
        int c = 0;
        try {
            c = q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        return c;
    }

    private void checkDupWithin(String sessionId) {
        /*
        String sql = "update marketing_temp_customer set dup_within = 1"
                + " from marketing_temp_customer"
                + " where session_id = '" + sessionId + "'"
                + " and marketing_id is null"
                + " and (case when (surname is not null and surname <> '') then (LTRIM(RTRIM((name))) + ' ' + LTRIM(RTRIM((surname)))) else LTRIM(RTRIM((name))) end)"
                + " in (select (case when (surname is not null and surname <> '') then (LTRIM(RTRIM((name))) + ' ' + LTRIM(RTRIM((surname)))) else LTRIM(RTRIM((name))) end) from marketing_temp_customer"
                + " where badformat = 0 and session_id = '" + sessionId + "'"
                + " and marketing_id is null"
                + " group by (case when (surname is not null and surname <> '') then (LTRIM(RTRIM((name))) + ' ' + LTRIM(RTRIM((surname)))) else LTRIM(RTRIM((name))) end)"
                + " having COUNT(*) > 1)";

        Query q = em.createNativeQuery(sql);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        sql = "update marketing_temp_customer set dup_within = 0"
                + " where marketing_id is null and session_id = '" + sessionId + "' and id in ("
                + " select min(id) from marketing_temp_customer"
                + " where session_id = '" + sessionId + "' and marketing_id is null and dup_within = 1"
                + " group by (case when (surname is not null and surname <> '') then (LTRIM(RTRIM((name))) + ' ' + LTRIM(RTRIM((surname)))) else LTRIM(RTRIM((name))) end)"
                + " having COUNT(*) > 1)";

        q = em.createNativeQuery(sql);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
        
        String sql = "EXEC spFilter_DupWithin :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

    }

    private void checkYesSale(String sessionId, Marketing marketing) {
        /*
        String sql = "update marketing_temp_customer set dup_yes = 1"
                + " from marketing_temp_customer a"
                + " inner join customer b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end)"
                + " inner join purchase_order c on (c.customer_id = b.id and c.sale_result = 'Y' and c.approval_status = 'approved' and c.payment_status = 'approved' and c.qc_status = 'approved')"
                + " where a.badformat = 0 and a.dup_within = 0 and a.session_id = :sessionId and a.marketing_id is null";
             
        if (marketing.getPeriodYessale() != null && marketing.getPeriodYessale() > 0) {
            sql += " and DATEDIFF(DAY, c.sale_date, GETDATE()) + 1 <= :period";
        }

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        if (marketing.getPeriodYessale() != null && marketing.getPeriodYessale() > 0) {
            q.setParameter("period", marketing.getPeriodYessale());
        }
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
                
        String sql = "EXEC spFilter_CheckYesSale :sessionId, :marketingId, :period";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);
        q.setParameter("period", marketing.getPeriodYessale());

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    private void checkExistingCustomer(String sessionId, Marketing marketing) {
        String sql = "";
        if (marketing.getDupType().equals("never") || marketing.getDupType().equals("time")) {
            sql = "update marketing_temp_customer set dup = 1, customer_id = b.id, list_status = 'merge' ";
        } else if (marketing.getDupType().equals("alway")) {
            sql = "update marketing_temp_customer set dup = 1, customer_id = b.id, list_status = 'skip' ";
        }

        sql += " from marketing_temp_customer a"
            //+ " inner join customer b on a.reference_no = ISNULL(b.reference_no,'') and b.parent_id is not null "
                + " inner join customer b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.blacklist = 0 and a.session_id = :sessionId and a.marketing_id is null"
            + " where 1=1";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        
        if (marketing.getDupType().equals("time")) {
            String sql2 = "update marketing_temp_customer set list_status = 'skip' "
                    + " from marketing_temp_customer a"
                    //+ " inner join customer b on a.reference_no = b.reference_no "
                    + " inner join customer b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                    + " and a.dup = 1 and a.session_id = :sessionId and a.marketing_id is null"
                    + " where 1=1 ";
            if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() != 0) {
                sql2 += " and (DATEDIFF(DAY, b.update_date, GETDATE()) + 1) <= :period";
            }
            Query q2 = em.createNativeQuery(sql2);
            q2.setParameter("sessionId", sessionId);
            if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() != 0) {
                q2.setParameter("period", marketing.getPeriodCustomer());
            }
            try {
                q2.executeUpdate();
            } catch (Exception e) {
                log.error(e);
            }
        }

    }
      
    private void checkMergeDupCustomer(String sessionId, Marketing marketing) {
        String sql = "EXEC spFilter_MergeDupCustomer :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);
        
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        /*
        String sql = "update marketing_temp_customer set customer_id = b.id, list_status = 'merge'";
        //if(marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() == -1){
        //    sql = "update marketing_temp_customer set dup = 1, customer_id = b.id, list_status = 'skip' ";
        //} else if(marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() >= 0) {
        //    sql = "update marketing_temp_customer set dup = 1, customer_id = b.id, list_status = 'merge' ";
        //}

        sql += " from marketing_temp_customer a"
             + " inner join (select MIN(id) as id, name, surname from customer group by name, surname) b"
             + " on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end)"
             + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.opout = 0 and a.blacklist = 0 and a.dnc = 0 and a.mib = 0 and a.dup = 0 and a.session_id = :sessionId and a.marketing_id is null"
             + " where 1=1";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
        /*
        if(marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() > 0) {
            String sql2 = "update marketing_temp_customer set list_status = 'skip' "
                    + " from marketing_temp_customer a"
                    //+ " inner join customer b on a.reference_no = b.reference_no "
                    //+ " inner join customer b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                    + " inner join (select MIN(id) as id, name, surname, MAX(ISNULL(update_date, create_date)) as update_date from customer group by name, surname) b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                    + " and a.dup = 1 and a.session_id = :sessionId and a.marketing_id is null"
                    + " where 1=1 ";
            if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() > 0) {
                sql2 += " and (DATEDIFF(DAY, b.update_date, GETDATE()) + 1) <= :period";
            }
            Query q2 = em.createNativeQuery(sql2);
            q2.setParameter("sessionId", sessionId);
            q2.setParameter("period", marketing.getPeriodCustomer());
            //if(marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() != 0) {        
                //q2.setParameter("period", marketing.getPeriodCustomer());
            //}    
            try {
                q2.executeUpdate();
            } catch (Exception e) {
                log.error(e);
            }
        }
        */
    }

    private void checkDupCustomer(String sessionId, Marketing marketing) {
        /*
        String sql = "update marketing_temp_customer set dup = 1, list_status = 'skip'"
                + " from marketing_temp_customer a"
                + " inner join customer b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end)"
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.opout = 0 and a.blacklist = 0 and a.badphone = 0 and a.dnc = 0 and a.mib = 0 and a.session_id = :sessionId and a.marketing_id is null"
                + " where 1=1";
        if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() > 0) {
            sql += " and (DATEDIFF(DAY, b.create_date, GETDATE()) + 1) <= :period";
        }
        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        if (marketing.getPeriodCustomer() != null && marketing.getPeriodCustomer() > 0) {
            q.setParameter("period", marketing.getPeriodCustomer());
        }
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
                
        String sql = "EXEC spFilter_DupCustomer :sessionId, :marketingId, :period";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);
        q.setParameter("period", marketing.getPeriodCustomer());
        
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkOpout(String sessionId) {
//        String sql = "update marketing_temp_customer set opout = 1"
//                + " from marketing_temp_customer a"
//                + " inner join op_out b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
//                + " and ( b.op_out_period <> 0 and DATEDIFF(DAY, b.last_activity_date, GETDATE()) <= b.op_out_period ) and a.dup_within = 0 and a.dup_yes = 0 and a.dup = 0 and a.session_id = :sessionId and a.marketing_id is null";
        String sql = "update marketing_temp_customer set opout = 1"
                + " from marketing_temp_customer a"
                //                + " inner join op_out b on a.reference_no = b.reference_no and"
                + " inner join op_out b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                + " and ( b.op_out_period <> 0 and DATEDIFF(DAY, b.last_activity_date, GETDATE()) <= b.op_out_period )"
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.session_id = :sessionId and a.marketing_id is null";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkOpout(String sessionId, Marketing marketing) {
        /*
        String sql = "update marketing_temp_customer set opout = 1"
                + " from marketing_temp_customer a"
                + " inner join op_out b"
                + " on case when b.check_name = 1 and ISNULL(b.name,'') <> '' then LTRIM(RTRIM(ISNULL(b.name,''))) else '' end = case when b.check_name = 1 and ISNULL(b.name,'') <> '' then LTRIM(RTRIM(ISNULL(a.name,''))) else '' end"
                + " and case when b.check_surname = 1 and ISNULL(b.surname,'') <> '' then LTRIM(RTRIM(ISNULL(b.surname,''))) else '' end = case when b.check_surname = 1 and ISNULL(a.surname,'') <> '' then LTRIM(RTRIM(ISNULL(a.surname,''))) else '' end"
                + " and (case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.home_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.home_phone,''))) else '' end"
                + "     or case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.office_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.office_phone,''))) else '' end"
                + "     or case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.mobile_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.mobile_phone,''))) else '' end)"
                + " and b.op_out_period = 1"
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0"
                + " and a.session_id = :sessionId"
                + " and a.marketing_id is null"
                + " where 1=1";
        if (marketing.getPeriodBlacklist() != null && marketing.getPeriodBlacklist() > 0) {
            sql += " and DATEDIFF(DAY, b.last_activity_date, GETDATE()) + 1 <= :period";
        }

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        if (marketing.getPeriodBlacklist() != null && marketing.getPeriodBlacklist() > 0) {
            q.setParameter("period", marketing.getPeriodBlacklist());
        }
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
        
        String sql = "EXEC spFilter_CheckOpOut :sessionId, :marketingId, :period";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);
        q.setParameter("period", marketing.getPeriodBlacklist());

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkBlacklist(String sessionId) {
//        String sql = "update marketing_temp_customer set blacklist = 1"
//                + " from marketing_temp_customer a"
//                + " inner join op_out b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) and ( b.op_out_period = 0 ) and a.dup_within = 0 and a.dup_yes = 0 and a.dup = 0 and a.session_id = :sessionId and a.marketing_id is null";
        String sql = "update marketing_temp_customer set blacklist = 1"
                + " from marketing_temp_customer a"
                + " inner join op_out b on a.reference_no = b.reference_no"
                //                + " inner join op_out b on (case when (a.surname is not null and a.surname <> '') then (LTRIM(RTRIM((a.name))) + ' ' + LTRIM(RTRIM((a.surname)))) else LTRIM(RTRIM((a.name))) end) = (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end) "
                + " and b.op_out_period = 0"
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.opout = 0"
                + " and a.session_id = :sessionId and a.marketing_id is null"
                + " or ((ltrim(rtrim(a.name)) + ' ' + ltrim(rtrim(a.surname)))= (case when (b.surname is not null and b.surname <> '') then (LTRIM(RTRIM((b.name))) + ' ' + LTRIM(RTRIM((b.surname)))) else LTRIM(RTRIM((b.name))) end))"
                + " or (b.telephone1 <> '' and (a.home_phone = b.telephone1 or a.office_phone = b.telephone1  or a.mobile_phone = b.telephone1))";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkBlacklist(String sessionId, Marketing marketing) {
        /*
        String sql = "update marketing_temp_customer set blacklist = 1"
                + " from marketing_temp_customer a"
                + " inner join op_out b"
                + " on case when b.check_name = 1 and ISNULL(b.name,'') <> '' then LTRIM(RTRIM(ISNULL(b.name,''))) else '' end = case when b.check_name = 1 and ISNULL(b.name,'') <> '' then LTRIM(RTRIM(ISNULL(a.name,''))) else '' end"
                + " and case when b.check_surname = 1 and ISNULL(b.surname,'') <> '' then LTRIM(RTRIM(ISNULL(b.surname,''))) else '' end = case when b.check_surname = 1 and ISNULL(a.surname,'') <> '' then LTRIM(RTRIM(ISNULL(a.surname,''))) else '' end"
                + " and (case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.home_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.home_phone,''))) else '' end"
                + "     or case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.office_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.office_phone,''))) else '' end"
                + "     or case when b.check_telephone = 1 and ISNULL(b.telephone1,'') <> '' then LTRIM(RTRIM(ISNULL(b.telephone1,''))) else '' end = case when b.check_telephone = 1 and ISNULL(a.mobile_phone,'') <> '' then LTRIM(RTRIM(ISNULL(a.mobile_phone,''))) else '' end)"
                + " and b.op_out_period = 0"
                + " and a.badformat = 0 and a.dup_within = 0 and a.dup_yes = 0 and a.opout = 0"
                + " and a.session_id = :sessionId"
                + " and a.marketing_id is null";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
        
        String sql = "EXEC spFilter_CheckBlackList :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkTelephoneNo(String sessionId) {
        /*
        String sql = "update marketing_temp_customer set badphone = 1"
                + " where badformat = 0 and dup_within = 0 and dup_yes = 0 and opout = 0 and blacklist = 0 and session_id = :sessionId and marketing_id is null"
                + " and id not in"
                + " (select id from marketing_temp_customer where badformat = 0 and dup_within = 0 and dup_yes = 0 and session_id = :sessionId and marketing_id is null"
                + " and ((LEN(RIGHT(ltrim(rtrim(mobile_phone)), 10)) = 10 and (LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '08' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '09' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '01' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '06'))"
                + " or (LEN(RIGHT(ltrim(rtrim(office_phone)), 9)) = 9 and LEFT(RIGHT(ltrim(rtrim(office_phone)), 9), 1) = '0')"
                + " or (LEN(RIGHT(ltrim(rtrim(home_phone)), 9)) = 9 and LEFT(RIGHT(ltrim(rtrim(home_phone)), 9), 1) = '0')"
                + " ))";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        */
        
        String sql = "EXEC spFilter_CheckFormatTelNo :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        sql = "update marketing_temp_customer set mobile_phone = RIGHT(ltrim(rtrim(mobile_phone)), 10)"
                + " where badformat = 0 and dup_within = 0 and dup_yes = 0 and opout = 0 and blacklist = 0 and session_id = :sessionId and marketing_id is null"
                + " and LEN(RIGHT(ltrim(rtrim(mobile_phone)), 10)) = 10"
                + " and (LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '08' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '09' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '01' or LEFT(RIGHT(ltrim(rtrim(mobile_phone)), 10), 2) = '06')";
        q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        sql = "update marketing_temp_customer set office_phone = RIGHT(ltrim(rtrim(office_phone)), 9)"
                + " where badformat = 0 and dup_within = 0 and dup_yes = 0 and opout = 0 and blacklist = 0 and session_id = :sessionId and marketing_id is null"
                + " and LEN(RIGHT(ltrim(rtrim(office_phone)), 9)) = 9 and LEFT(RIGHT(ltrim(rtrim(office_phone)), 9), 1) = '0'";
        q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        sql = "update marketing_temp_customer set home_phone = RIGHT(ltrim(rtrim(home_phone)), 9)"
                + " where badformat = 0 and dup_within = 0 and dup_yes = 0 and opout = 0 and blacklist = 0 and session_id = :sessionId and marketing_id is null"
                + " and LEN(RIGHT(ltrim(rtrim(home_phone)), 9)) = 9 and LEFT(RIGHT(ltrim(rtrim(home_phone)), 9), 1) = '0'";
        q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

    }

    private void checkDnc(String sessionId, Marketing marketing){
        String sql = "EXEC spFilter_DNC :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        
    }
    
    private void checkMib(String sessionId, Marketing marketing){
        String sql = "EXEC spFilter_MIB :sessionId, :marketingId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", null);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
        
    }
    
    public Customer findExistingParentCustomer(String refNo) {
        String sql = "select object(o) from Customer as o where parent_id is null and o.referenceNo = ?1";
        Query q = em.createQuery(sql);
        q.setParameter(1, refNo);
        try {
            q.setMaxResults(1);
            return (Customer) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public void createCustomer(String sessionId) {
        Date now = new Date();
        String sql = "insert into customer "
                + "(reference_no,initial,name,surname,customer_type,privilege,idno,gender,dob,weight,height,nationality,occupation,"
                + "home_phone,home_ext,office_phone,office_ext,mobile_phone,fax,fax_ext,email,current_fullname,current_address_line1,"
                + "current_address_line2,current_address_line3,current_address_line4,current_address_line5,current_address_line6,"
                + "current_address_line7,current_address_line8,current_district_name,current_province_name,current_postal_code,home_fullname,"
                + "home_address_line1,home_address_line2,home_address_line3,home_address_line4,home_address_line5,home_address_line6,"
                + "home_address_line7,home_address_line8,home_district_name,home_province_name,home_postal_code,billing_fullname,"
                + "billing_address_line1,billing_address_line2,billing_address_line3,billing_address_line4,billing_address_line5,"
                + "billing_address_line6,billing_address_line7,billing_address_line8,billing_district_name,billing_province_name,"
                + "billing_postal_code,shipping_fullname,shipping_address_line1,shipping_address_line2,shipping_address_line3,shipping_address_line4,"
                + "shipping_address_line5,shipping_address_line6,shipping_address_line7,shipping_address_line8,shipping_district_name,"
                + "shipping_province_name,shipping_postal_code,contact_phone1,contact_phone2,contact_phone3,contact_phone4,contact_phone5,"
                + "remark,flexfield1,flexfield2,flexfield3,flexfield4,flexfield5,flexfield6,flexfield7,flexfield8,flexfield9,flexfield10,"
                + "flexfield11,flexfield12,flexfield13,flexfield14,flexfield15,flexfield16,flexfield17,flexfield18,flexfield19,flexfield20,"
                + "flexfield21,flexfield22,flexfield23,flexfield24,flexfield25,flexfield26,flexfield27,flexfield28,flexfield29,flexfield30,"
                + "flexfield31,flexfield32,flexfield33,flexfield34,flexfield35,flexfield36,flexfield37,flexfield38,flexfield39,flexfield40,"
                + "flexfield41,flexfield42,flexfield43,flexfield44,flexfield45,flexfield46,flexfield47,flexfield48,flexfield49,flexfield50,"
                + "flexfield51,flexfield52,flexfield53,flexfield54,flexfield55,flexfield56,flexfield57,flexfield58,flexfield59,flexfield60,"
                + "flexfield61,flexfield62,flexfield63,flexfield64,flexfield65,flexfield66,flexfield67,flexfield68,flexfield69,flexfield70," 
                + "flexfield71,flexfield72,flexfield73,flexfield74,flexfield75,flexfield76,flexfield77,flexfield78,flexfield79,flexfield80,"
                + "flexfield81,flexfield82,flexfield83,flexfield84,flexfield85,flexfield86,flexfield87,flexfield88,flexfield89,flexfield90,"
                + "flexfield91,flexfield92,flexfield93,flexfield94,flexfield95,flexfield96,flexfield97,flexfield98,flexfield99,flexfield100," 
                + "car_brand,car_model,car_year,car_character_group,car_number,car_province,create_date,create_by,update_date,update_by,parent_id)"
                + "select "
                + "mtc.reference_no,mtc.initial,mtc.name,mtc.surname,mtc.customer_type,mtc.privilege,mtc.idno,mtc.gender,mtc.dob,mtc.weight,"
                + "mtc.height,mtc.nationality,mtc.occupation,mtc.home_phone,mtc.home_ext,mtc.office_phone,mtc.office_ext,mtc.mobile_phone,"
                + "mtc.fax,mtc.fax_ext,mtc.email,mtc.current_fullname,mtc.current_address_line1,mtc.current_address_line2,mtc.current_address_line3,"
                + "mtc.current_address_line4,mtc.current_address_line5,mtc.current_address_line6,mtc.current_address_line7,mtc.current_address_line8,"
                + "mtc.current_district_name,mtc.current_province_name,mtc.current_postal_code,mtc.home_fullname,mtc.home_address_line1,"
                + "mtc.home_address_line2,mtc.home_address_line3,mtc.home_address_line4,mtc.home_address_line5,mtc.home_address_line6,"
                + "mtc.home_address_line7,mtc.home_address_line8,mtc.home_district_name,mtc.home_province_name,mtc.home_postal_code,"
                + "mtc.billing_fullname,mtc.billing_address_line1,mtc.billing_address_line2,mtc.billing_address_line3,mtc.billing_address_line4,"
                + "mtc.billing_address_line5,mtc.billing_address_line6,mtc.billing_address_line7,mtc.billing_address_line8,mtc.billing_district_name,"
                + "mtc.billing_province_name,mtc.billing_postal_code,mtc.shipping_fullname,mtc.shipping_address_line1,mtc.shipping_address_line2,"
                + "mtc.shipping_address_line3,mtc.shipping_address_line4,mtc.shipping_address_line5,mtc.shipping_address_line6,mtc.shipping_address_line7,"
                + "mtc.shipping_address_line8,mtc.shipping_district_name,mtc.shipping_province_name,mtc.shipping_postal_code,mtc.contact_phone1,"
                + "mtc.contact_phone2,mtc.contact_phone3,mtc.contact_phone4,mtc.contact_phone5,mtc.remark,mtc.flexfield1,mtc.flexfield2,mtc.flexfield3,"
                + "mtc.flexfield4,mtc.flexfield5,mtc.flexfield6,mtc.flexfield7,mtc.flexfield8,mtc.flexfield9,mtc.flexfield10,mtc.flexfield11,"
                + "mtc.flexfield12,mtc.flexfield13,mtc.flexfield14,mtc.flexfield15,mtc.flexfield16,mtc.flexfield17,mtc.flexfield18,mtc.flexfield19,"
                + "mtc.flexfield20,mtc.flexfield21,mtc.flexfield22,mtc.flexfield23,mtc.flexfield24,mtc.flexfield25,mtc.flexfield26,mtc.flexfield27,"
                + "mtc.flexfield28,mtc.flexfield29,mtc.flexfield30,mtc.flexfield31,mtc.flexfield32,mtc.flexfield33,mtc.flexfield34,mtc.flexfield35,"
                + "mtc.flexfield36,mtc.flexfield37,mtc.flexfield38,mtc.flexfield39,mtc.flexfield40,mtc.flexfield41,mtc.flexfield42,mtc.flexfield43,"
                + "mtc.flexfield44,mtc.flexfield45,mtc.flexfield46,mtc.flexfield47,mtc.flexfield48,mtc.flexfield49,mtc.flexfield50,mtc.flexfield51,"
                + "mtc.flexfield52,mtc.flexfield53,mtc.flexfield54,mtc.flexfield55,mtc.flexfield56,mtc.flexfield57,mtc.flexfield58,mtc.flexfield59,"
                + "mtc.flexfield60,mtc.flexfield61,mtc.flexfield62,mtc.flexfield63,mtc.flexfield64,mtc.flexfield65,mtc.flexfield66,mtc.flexfield67,"
                + "mtc.flexfield68,mtc.flexfield69,mtc.flexfield70,mtc.flexfield71,mtc.flexfield72,mtc.flexfield73,mtc.flexfield74,mtc.flexfield75,"
                + "mtc.flexfield76,mtc.flexfield77,mtc.flexfield78,mtc.flexfield79,mtc.flexfield80,mtc.flexfield81,mtc.flexfield82,mtc.flexfield83,"
                + "mtc.flexfield84,mtc.flexfield85,mtc.flexfield86,mtc.flexfield87,mtc.flexfield88,mtc.flexfield89,mtc.flexfield90,mtc.flexfield91,"
                + "mtc.flexfield92,mtc.flexfield93,mtc.flexfield94,mtc.flexfield95,mtc.flexfield96,mtc.flexfield97,mtc.flexfield98,mtc.flexfield99,"
                + "mtc.flexfield100,mtc.car_brand,mtc.car_model,mtc.car_year,mtc.car_character_group,mtc.car_number,mtc.car_province,:createDate,:createBy,:createDate,:createBy,mtc.customer_id "
                + "from marketing_temp_customer mtc "
                + "where mtc.session_id = :sessionId and mtc.marketing_id is null and mtc.badformat = 0 and mtc.dup_within = 0 "
                + "and mtc.dup_yes = 0 and mtc.opout = 0 and mtc.blacklist = 0 and mtc.badphone = 0 and mtc.list_status <> 'skip' and mtc.dup = 0 and mtc.dnc = 0 and mtc.mib = 0";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("createDate", now);
        q.setParameter("createBy", JSFUtil.getUserSession().getUsers().getLoginName());
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMarketingCustomer(String sessionId, Integer marketingId) {
        Date now = new Date();
        /*
        String sql = "insert into marketing_customer "
                + "(marketing_id, customer_id, new_list, create_date, assign, "
                + "list_value_001, list_value_002, list_value_003, list_value_004, list_value_005, list_value_006, list_value_007, list_value_008, list_value_009, list_value_010, "
                + "list_value_011, list_value_012, list_value_013, list_value_014, list_value_015, list_value_016, list_value_017, list_value_018, list_value_019, list_value_020, "
                + "list_value_021, list_value_022, list_value_023, list_value_024, list_value_025, list_value_026, list_value_027, list_value_028, list_value_029, list_value_030, "
                + "list_value_031, list_value_032, list_value_033, list_value_034, list_value_035, list_value_036, list_value_037, list_value_038, list_value_039, list_value_040, "
                + "list_value_041, list_value_042, list_value_043, list_value_044, list_value_045, list_value_046, list_value_047, list_value_048, list_value_049, list_value_050, "
                + "list_value_051, list_value_052, list_value_053, list_value_054, list_value_055, list_value_056, list_value_057, list_value_058, list_value_059, list_value_060, "
                + "list_value_061, list_value_062, list_value_063, list_value_064, list_value_065, list_value_066, list_value_067, list_value_068, list_value_069, list_value_070, "
                + "list_value_071, list_value_072, list_value_073, list_value_074, list_value_075, list_value_076, list_value_077, list_value_078, list_value_079, list_value_080, "
                + "list_value_081, list_value_082, list_value_083, list_value_084, list_value_085, list_value_086, list_value_087, list_value_088, list_value_089, list_value_090, "
                + "list_value_091, list_value_092, list_value_093, list_value_094, list_value_095, list_value_096, list_value_097, list_value_098, list_value_099, list_value_100) "
                + "select :marketingId, cus.id, 1, :createDate, 0, "
                + "mtc.list_value_001, mtc.list_value_002, mtc.list_value_003, mtc.list_value_004, mtc.list_value_005, mtc.list_value_006, mtc.list_value_007, mtc.list_value_008, mtc.list_value_009, mtc.list_value_010, "
                + "mtc.list_value_011, mtc.list_value_012, mtc.list_value_013, mtc.list_value_014, mtc.list_value_015, mtc.list_value_016, mtc.list_value_017, mtc.list_value_018, mtc.list_value_019, mtc.list_value_020, "
                + "mtc.list_value_021, mtc.list_value_022, mtc.list_value_023, mtc.list_value_024, mtc.list_value_025, mtc.list_value_026, mtc.list_value_027, mtc.list_value_028, mtc.list_value_029, mtc.list_value_030, "
                + "mtc.list_value_031, mtc.list_value_032, mtc.list_value_033, mtc.list_value_034, mtc.list_value_035, mtc.list_value_036, mtc.list_value_037, mtc.list_value_038, mtc.list_value_039, mtc.list_value_040, "
                + "mtc.list_value_041, mtc.list_value_042, mtc.list_value_043, mtc.list_value_044, mtc.list_value_045, mtc.list_value_046, mtc.list_value_047, mtc.list_value_048, mtc.list_value_049, mtc.list_value_050, "
                + "mtc.list_value_051, mtc.list_value_052, mtc.list_value_053, mtc.list_value_054, mtc.list_value_055, mtc.list_value_056, mtc.list_value_057, mtc.list_value_058, mtc.list_value_059, mtc.list_value_060, "
                + "mtc.list_value_061, mtc.list_value_062, mtc.list_value_063, mtc.list_value_064, mtc.list_value_065, mtc.list_value_066, mtc.list_value_067, mtc.list_value_068, mtc.list_value_069, mtc.list_value_070, "
                + "mtc.list_value_071, mtc.list_value_072, mtc.list_value_073, mtc.list_value_074, mtc.list_value_075, mtc.list_value_076, mtc.list_value_077, mtc.list_value_078, mtc.list_value_079, mtc.list_value_080, "
                + "mtc.list_value_081, mtc.list_value_082, mtc.list_value_083, mtc.list_value_084, mtc.list_value_085, mtc.list_value_086, mtc.list_value_087, mtc.list_value_088, mtc.list_value_089, mtc.list_value_090, "
                + "mtc.list_value_091, mtc.list_value_092, mtc.list_value_093, mtc.list_value_094, mtc.list_value_095, mtc.list_value_096, mtc.list_value_097, mtc.list_value_098, mtc.list_value_099, mtc.list_value_100 "
                + "from marketing_temp_customer mtc "
                //+ "join customer cus "
                + "inner join (select max(id) as id, LTRIM(RTRIM(name)) as name, LTRIM(RTRIM(surname)) as surname from customer cus group by LTRIM(RTRIM(name)), LTRIM(RTRIM(surname))) cus "
                + "on (case when (mtc.surname is not null and mtc.surname <> '') then (LTRIM(RTRIM((mtc.name))) + ' ' + LTRIM(RTRIM((mtc.surname)))) else LTRIM(RTRIM((mtc.name))) end) = (case when (cus.surname is not null and cus.surname <> '') then (LTRIM(RTRIM((cus.name))) + ' ' + LTRIM(RTRIM((cus.surname)))) else LTRIM(RTRIM((cus.name))) end) "
                + "where mtc.session_id = :sessionId and mtc.marketing_id is null and mtc.badformat = 0 and mtc.dup_within = 0 "
                + "and mtc.dup_yes = 0 and mtc.opout = 0 and mtc.blacklist = 0 and mtc.badphone = 0 and mtc.list_status <> 'skip' and mtc.dup = 0 and mtc.dnc = 0 and mtc.mib = 0";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", marketingId);
        q.setParameter("createDate", now);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        
        String sql = "EXEC spMarketingList_CreateMarketingCustomer :sessionId, :marketingId, :createDate";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("marketingId", marketingId);
        q.setParameter("createDate", now);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void createCustomerFromMarketingTempCustomer(String sessionId, Marketing marketing) {
        try {
            create(marketing);
            createCustomer(sessionId);
            /*
            List<MarketingTempCustomer> list = this.getMergeList(sessionId);
            Customer mergeCust = null;
            Date now = new Date();
            for (MarketingTempCustomer mtc : list) {
                try {
                    mergeCust = mtc.getCustomer();
                    //Merge Value
                    mergeCust = this.mergeCustomerValueFromMarketingTempCustomer(mtc, mergeCust);
                    mergeCust.setUpdateDate(now);
                    mergeCust.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    em.merge(mergeCust);
                } catch (Exception e) {
                    log.error(e);
                }
            }
            */
            Integer marketingId = marketing.getId();
            this.createMarketingCustomer(sessionId, marketingId);
            this.updateMarketingList(sessionId, marketingId);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public List<MarketingTempCustomer> getMergeList(String sessionId) {
        List<MarketingTempCustomer> list = null;
        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null"
                + " and o.badformat = 0 and o.dupWithin = 0 and o.dupYes = 0 and o.opout = 0 and o.blacklist = 0 and badphone = 0 and dnc = 0 and mib = 0"
                + " and list_status = 'merge'";

        Query q = em.createQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<MarketingTempCustomer> getLoadedList(String sessionId) {
        List<MarketingTempCustomer> list = null;
        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null"
                + " and o.badformat = 0 and o.dupWithin = 0 and o.dupYes = 0 and o.opout = 0 and o.blacklist = 0 and badphone = 0 and dnc = 0 and mib = 0"
                + " and (list_status = 'new' or list_status = 'merge') and o.dnc = 0 and o.mib = 0";

        Query q = em.createQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    private Customer setParentCustomerValueFromMarketingTempCustomer(MarketingTempCustomer mtc) {
        Customer parentCust = new Customer();
        try {
            parentCust.setReferenceNo(mtc.getListValue001());
            parentCust.setInitial(mtc.getListValue002());
            parentCust.setName(mtc.getListValue003());
            parentCust.setSurname(mtc.getListValue004());
            parentCust.setCustomerType(new CustomerType(mtc.getCustomerType()));
            parentCust.setMobilePhone(mtc.getListValue005());
            parentCust.setCurrentAddressLine1(mtc.getListValue006());
            parentCust.setCurrentAddressLine8(mtc.getListValue007());
            parentCust.setCurrentDistrictName(mtc.getListValue008());
            parentCust.setCurrentProvinceName(mtc.getListValue009());
            parentCust.setFlexfield1(mtc.getFlexfield1());
            parentCust.setPrivilege(mtc.getPrivilege());
        } catch (Exception e) {
            log.error(e);
        }
        return parentCust;
    }

    private Customer setCustomerValueFromMarketingTempCustomer(MarketingTempCustomer mtc) {
        Customer customer = new Customer();
        try {
            customer.setReferenceNo(mtc.getReferenceNo());
            customer.setInitial(mtc.getInitial());
            customer.setName(mtc.getName());
            customer.setSurname(mtc.getSurname());
            customer.setCustomerType(new CustomerType(mtc.getCustomerType()));
            customer.setPrivilege(mtc.getPrivilege());
            customer.setIdno(mtc.getIdno());
            customer.setGender(mtc.getGender());
            customer.setDob(mtc.getDob());
            customer.setWeight(mtc.getWeight());
            customer.setHeight(mtc.getHeight());
            customer.setNationality(mtc.getNationality());
            customer.setOccupation(mtc.getOccupation());
            customer.setHomePhone(mtc.getHomePhone());
            customer.setHomeExt(mtc.getHomeExt());
            customer.setOfficePhone(mtc.getOfficePhone());
            customer.setOfficeExt(mtc.getOfficeExt());
            customer.setMobilePhone(mtc.getMobilePhone());
            customer.setFax(mtc.getFax());
            customer.setEmail(mtc.getEmail());
            customer.setContactPhone1(mtc.getContactPhone1());
            customer.setContactPhone2(mtc.getContactPhone2());
            customer.setContactPhone3(mtc.getContactPhone3());
            customer.setContactPhone4(mtc.getContactPhone4());
            customer.setContactPhone5(mtc.getContactPhone5());
            customer.setRemark(mtc.getRemark());
            customer.setFlexfield1(mtc.getFlexfield1());
            customer.setFlexfield2(mtc.getFlexfield2());
            customer.setFlexfield3(mtc.getFlexfield3());
            customer.setFlexfield4(mtc.getFlexfield4());
            customer.setFlexfield5(mtc.getFlexfield5());
            customer.setFlexfield6(mtc.getFlexfield6());
            customer.setFlexfield7(mtc.getFlexfield7());
            customer.setFlexfield8(mtc.getFlexfield8());
            customer.setFlexfield9(mtc.getFlexfield9());
            customer.setFlexfield10(mtc.getFlexfield10());
            customer.setFlexfield11(mtc.getFlexfield11());
            customer.setFlexfield12(mtc.getFlexfield12());
            customer.setFlexfield13(mtc.getFlexfield13());
            customer.setFlexfield14(mtc.getFlexfield14());
            customer.setFlexfield15(mtc.getFlexfield15());
            customer.setFlexfield16(mtc.getFlexfield16());
            customer.setFlexfield17(mtc.getFlexfield17());
            customer.setFlexfield18(mtc.getFlexfield18());
            customer.setFlexfield19(mtc.getFlexfield19());
            customer.setFlexfield20(mtc.getFlexfield20());
            customer.setCurrentFullname(mtc.getCurrentFullname());
            customer.setCurrentAddressLine1(mtc.getCurrentAddressLine1());
            customer.setCurrentAddressLine2(mtc.getCurrentAddressLine2());
            customer.setCurrentAddressLine3(mtc.getCurrentAddressLine3());
            customer.setCurrentAddressLine4(mtc.getCurrentAddressLine4());
            customer.setCurrentAddressLine5(mtc.getCurrentAddressLine5());
            customer.setCurrentAddressLine6(mtc.getCurrentAddressLine6());
            customer.setCurrentAddressLine7(mtc.getCurrentAddressLine7());
            customer.setCurrentAddressLine8(mtc.getCurrentAddressLine8());
            customer.setCurrentDistrictName(mtc.getCurrentDistrictName());
            customer.setCurrentProvinceName(mtc.getCurrentProvinceName());
            customer.setCurrentPostalCode(mtc.getCurrentPostalCode());
            customer.setHomeFullname(mtc.getHomeFullname());
            customer.setHomeAddressLine1(mtc.getHomeAddressLine1());
            customer.setHomeAddressLine2(mtc.getHomeAddressLine2());
            customer.setHomeAddressLine3(mtc.getHomeAddressLine3());
            customer.setHomeAddressLine4(mtc.getHomeAddressLine4());
            customer.setHomeAddressLine5(mtc.getHomeAddressLine5());
            customer.setHomeAddressLine6(mtc.getHomeAddressLine6());
            customer.setHomeAddressLine7(mtc.getHomeAddressLine7());
            customer.setHomeAddressLine8(mtc.getHomeAddressLine8());
            customer.setHomeDistrictName(mtc.getHomeDistrictName());
            customer.setHomeProvinceName(mtc.getHomeProvinceName());
            customer.setHomePostalCode(mtc.getHomePostalCode());
            customer.setBillingFullname(mtc.getBillingFullname());
            customer.setBillingAddressLine1(mtc.getBillingAddressLine1());
            customer.setBillingAddressLine2(mtc.getBillingAddressLine2());
            customer.setBillingAddressLine3(mtc.getBillingAddressLine3());
            customer.setBillingAddressLine4(mtc.getBillingAddressLine4());
            customer.setBillingAddressLine5(mtc.getBillingAddressLine5());
            customer.setBillingAddressLine6(mtc.getBillingAddressLine6());
            customer.setBillingAddressLine7(mtc.getBillingAddressLine7());
            customer.setBillingAddressLine8(mtc.getBillingAddressLine8());
            customer.setBillingDistrictName(mtc.getBillingDistrictName());
            customer.setBillingProvinceName(mtc.getBillingProvinceName());
            customer.setBillingPostalCode(mtc.getBillingPostalCode());
            customer.setShippingFullname(mtc.getShippingFullname());
            customer.setShippingAddressLine1(mtc.getShippingAddressLine1());
            customer.setShippingAddressLine2(mtc.getShippingAddressLine2());
            customer.setShippingAddressLine3(mtc.getShippingAddressLine3());
            customer.setShippingAddressLine4(mtc.getShippingAddressLine4());
            customer.setShippingAddressLine5(mtc.getShippingAddressLine5());
            customer.setShippingAddressLine6(mtc.getShippingAddressLine6());
            customer.setShippingAddressLine7(mtc.getShippingAddressLine7());
            customer.setShippingAddressLine8(mtc.getShippingAddressLine8());
            customer.setShippingDistrictName(mtc.getShippingDistrictName());
            customer.setShippingProvinceName(mtc.getShippingProvinceName());
            customer.setShippingPostalCode(mtc.getShippingPostalCode());
            customer.setCarBrand(mtc.getCarBrand());
            customer.setCarModel(mtc.getCarModel());
            customer.setCarYear(mtc.getCarYear());
            customer.setCarCharacterGroup(mtc.getCarCharacterGroup());
            customer.setCarNumber(mtc.getCarNumber());
            customer.setCarProvince(mtc.getCarProvince());
        } catch (Exception e) {
            log.error(e);
        }
        return customer;
    }

    private Customer mergeCustomerValueFromMarketingTempCustomer(MarketingTempCustomer mtc, Customer customer) {
//        Customer customer = new Customer();
        try {
//            customer.setReferenceNo(mtc.getReferenceNo());
            customer.setInitial(mtc.getInitial());
            customer.setName(mtc.getName());
            customer.setSurname(mtc.getSurname());
//            customer.setCustomerType(new CustomerType(mtc.getCustomerType()));
            customer.setPrivilege(mtc.getPrivilege());
            customer.setIdno(mtc.getIdno());
            customer.setGender(mtc.getGender());
            customer.setDob(mtc.getDob());
            customer.setWeight(mtc.getWeight());
            customer.setHeight(mtc.getHeight());
            customer.setNationality(mtc.getNationality());
            customer.setOccupation(mtc.getOccupation());
            customer.setHomePhone(mtc.getHomePhone());
            customer.setHomeExt(mtc.getHomeExt());
            customer.setOfficePhone(mtc.getOfficePhone());
            customer.setOfficeExt(mtc.getOfficeExt());
            customer.setMobilePhone(mtc.getMobilePhone());
            customer.setFax(mtc.getFax());
            customer.setEmail(mtc.getEmail());
            customer.setContactPhone1(mtc.getContactPhone1());
            customer.setContactPhone2(mtc.getContactPhone2());
            customer.setContactPhone3(mtc.getContactPhone3());
            customer.setContactPhone4(mtc.getContactPhone4());
            customer.setContactPhone5(mtc.getContactPhone5());
            customer.setRemark(mtc.getRemark());
            customer.setFlexfield1(mtc.getFlexfield1());
            customer.setFlexfield2(mtc.getFlexfield2());
            customer.setFlexfield3(mtc.getFlexfield3());
            customer.setFlexfield4(mtc.getFlexfield4());
            customer.setFlexfield5(mtc.getFlexfield5());
            customer.setFlexfield6(mtc.getFlexfield6());
            customer.setFlexfield7(mtc.getFlexfield7());
            customer.setFlexfield8(mtc.getFlexfield8());
            customer.setFlexfield9(mtc.getFlexfield9());
            customer.setFlexfield10(mtc.getFlexfield10());
            customer.setFlexfield11(mtc.getFlexfield11());
            customer.setFlexfield12(mtc.getFlexfield12());
            customer.setFlexfield13(mtc.getFlexfield13());
            customer.setFlexfield14(mtc.getFlexfield14());
            customer.setFlexfield15(mtc.getFlexfield15());
            customer.setFlexfield16(mtc.getFlexfield16());
            customer.setFlexfield17(mtc.getFlexfield17());
            customer.setFlexfield18(mtc.getFlexfield18());
            customer.setFlexfield19(mtc.getFlexfield19());
            customer.setFlexfield20(mtc.getFlexfield20());            
            customer.setFlexfield21(mtc.getFlexfield21());
            customer.setFlexfield22(mtc.getFlexfield22());
            customer.setFlexfield23(mtc.getFlexfield23());
            customer.setFlexfield24(mtc.getFlexfield24());
            customer.setFlexfield25(mtc.getFlexfield25());
            customer.setFlexfield26(mtc.getFlexfield26());
            customer.setFlexfield27(mtc.getFlexfield27());
            customer.setFlexfield28(mtc.getFlexfield28());
            customer.setFlexfield29(mtc.getFlexfield29());
            customer.setFlexfield30(mtc.getFlexfield30());
            customer.setFlexfield31(mtc.getFlexfield31());
            customer.setFlexfield32(mtc.getFlexfield32());
            customer.setFlexfield33(mtc.getFlexfield33());
            customer.setFlexfield34(mtc.getFlexfield34());
            customer.setFlexfield35(mtc.getFlexfield35());
            customer.setFlexfield36(mtc.getFlexfield36());
            customer.setFlexfield37(mtc.getFlexfield37());
            customer.setFlexfield38(mtc.getFlexfield38());
            customer.setFlexfield39(mtc.getFlexfield39());
            customer.setFlexfield40(mtc.getFlexfield40());
            customer.setFlexfield41(mtc.getFlexfield41());
            customer.setFlexfield42(mtc.getFlexfield42());
            customer.setFlexfield43(mtc.getFlexfield43());
            customer.setFlexfield44(mtc.getFlexfield44());
            customer.setFlexfield45(mtc.getFlexfield45());
            customer.setFlexfield46(mtc.getFlexfield46());
            customer.setFlexfield47(mtc.getFlexfield47());
            customer.setFlexfield48(mtc.getFlexfield48());
            customer.setFlexfield49(mtc.getFlexfield49());
            customer.setFlexfield50(mtc.getFlexfield50());
            customer.setFlexfield51(mtc.getFlexfield51());
            customer.setFlexfield52(mtc.getFlexfield52());
            customer.setFlexfield53(mtc.getFlexfield53());
            customer.setFlexfield54(mtc.getFlexfield54());
            customer.setFlexfield55(mtc.getFlexfield55());
            customer.setFlexfield56(mtc.getFlexfield56());
            customer.setFlexfield57(mtc.getFlexfield57());
            customer.setFlexfield58(mtc.getFlexfield58());
            customer.setFlexfield59(mtc.getFlexfield59());
            customer.setFlexfield60(mtc.getFlexfield60());
            customer.setFlexfield61(mtc.getFlexfield61());
            customer.setFlexfield62(mtc.getFlexfield62());
            customer.setFlexfield63(mtc.getFlexfield63());
            customer.setFlexfield64(mtc.getFlexfield64());
            customer.setFlexfield65(mtc.getFlexfield65());
            customer.setFlexfield66(mtc.getFlexfield66());
            customer.setFlexfield67(mtc.getFlexfield67());
            customer.setFlexfield68(mtc.getFlexfield68());
            customer.setFlexfield69(mtc.getFlexfield69());
            customer.setFlexfield70(mtc.getFlexfield70());
            customer.setFlexfield71(mtc.getFlexfield71());
            customer.setFlexfield72(mtc.getFlexfield72());
            customer.setFlexfield73(mtc.getFlexfield73());
            customer.setFlexfield74(mtc.getFlexfield74());
            customer.setFlexfield75(mtc.getFlexfield75());
            customer.setFlexfield76(mtc.getFlexfield76());
            customer.setFlexfield77(mtc.getFlexfield77());
            customer.setFlexfield78(mtc.getFlexfield78());
            customer.setFlexfield79(mtc.getFlexfield79());
            customer.setFlexfield80(mtc.getFlexfield80());
            customer.setFlexfield81(mtc.getFlexfield81());
            customer.setFlexfield82(mtc.getFlexfield82());
            customer.setFlexfield83(mtc.getFlexfield83());
            customer.setFlexfield84(mtc.getFlexfield84());
            customer.setFlexfield85(mtc.getFlexfield85());
            customer.setFlexfield86(mtc.getFlexfield86());
            customer.setFlexfield87(mtc.getFlexfield87());
            customer.setFlexfield88(mtc.getFlexfield88());
            customer.setFlexfield89(mtc.getFlexfield89());
            customer.setFlexfield90(mtc.getFlexfield90());
            customer.setFlexfield91(mtc.getFlexfield91());
            customer.setFlexfield92(mtc.getFlexfield92());
            customer.setFlexfield93(mtc.getFlexfield93());
            customer.setFlexfield94(mtc.getFlexfield94());
            customer.setFlexfield95(mtc.getFlexfield95());
            customer.setFlexfield96(mtc.getFlexfield96());
            customer.setFlexfield97(mtc.getFlexfield97());
            customer.setFlexfield98(mtc.getFlexfield98());
            customer.setFlexfield99(mtc.getFlexfield99());
            customer.setFlexfield100(mtc.getFlexfield100()); 
            customer.setCurrentFullname(mtc.getCurrentFullname());
            customer.setCurrentAddressLine1(mtc.getCurrentAddressLine1());
            customer.setCurrentAddressLine2(mtc.getCurrentAddressLine2());
            customer.setCurrentAddressLine3(mtc.getCurrentAddressLine3());
            customer.setCurrentAddressLine4(mtc.getCurrentAddressLine4());
            customer.setCurrentAddressLine5(mtc.getCurrentAddressLine5());
            customer.setCurrentAddressLine6(mtc.getCurrentAddressLine6());
            customer.setCurrentAddressLine7(mtc.getCurrentAddressLine7());
            customer.setCurrentAddressLine8(mtc.getCurrentAddressLine8());
            customer.setCurrentDistrictName(mtc.getCurrentDistrictName());
            customer.setCurrentProvinceName(mtc.getCurrentProvinceName());
            customer.setCurrentPostalCode(mtc.getCurrentPostalCode());
            customer.setHomeFullname(mtc.getHomeFullname());
            customer.setHomeAddressLine1(mtc.getHomeAddressLine1());
            customer.setHomeAddressLine2(mtc.getHomeAddressLine2());
            customer.setHomeAddressLine3(mtc.getHomeAddressLine3());
            customer.setHomeAddressLine4(mtc.getHomeAddressLine4());
            customer.setHomeAddressLine5(mtc.getHomeAddressLine5());
            customer.setHomeAddressLine6(mtc.getHomeAddressLine6());
            customer.setHomeAddressLine7(mtc.getHomeAddressLine7());
            customer.setHomeAddressLine8(mtc.getHomeAddressLine8());
            customer.setHomeDistrictName(mtc.getHomeDistrictName());
            customer.setHomeProvinceName(mtc.getHomeProvinceName());
            customer.setHomePostalCode(mtc.getHomePostalCode());
            customer.setBillingFullname(mtc.getBillingFullname());
            customer.setBillingAddressLine1(mtc.getBillingAddressLine1());
            customer.setBillingAddressLine2(mtc.getBillingAddressLine2());
            customer.setBillingAddressLine3(mtc.getBillingAddressLine3());
            customer.setBillingAddressLine4(mtc.getBillingAddressLine4());
            customer.setBillingAddressLine5(mtc.getBillingAddressLine5());
            customer.setBillingAddressLine6(mtc.getBillingAddressLine6());
            customer.setBillingAddressLine7(mtc.getBillingAddressLine7());
            customer.setBillingAddressLine8(mtc.getBillingAddressLine8());
            customer.setBillingDistrictName(mtc.getBillingDistrictName());
            customer.setBillingProvinceName(mtc.getBillingProvinceName());
            customer.setBillingPostalCode(mtc.getBillingPostalCode());
            customer.setShippingFullname(mtc.getShippingFullname());
            customer.setShippingAddressLine1(mtc.getShippingAddressLine1());
            customer.setShippingAddressLine2(mtc.getShippingAddressLine2());
            customer.setShippingAddressLine3(mtc.getShippingAddressLine3());
            customer.setShippingAddressLine4(mtc.getShippingAddressLine4());
            customer.setShippingAddressLine5(mtc.getShippingAddressLine5());
            customer.setShippingAddressLine6(mtc.getShippingAddressLine6());
            customer.setShippingAddressLine7(mtc.getShippingAddressLine7());
            customer.setShippingAddressLine8(mtc.getShippingAddressLine8());
            customer.setShippingDistrictName(mtc.getShippingDistrictName());
            customer.setShippingProvinceName(mtc.getShippingProvinceName());
            customer.setShippingPostalCode(mtc.getShippingPostalCode());
            customer.setCarBrand(mtc.getCarBrand());
            customer.setCarModel(mtc.getCarModel());
            customer.setCarYear(mtc.getCarYear());
            customer.setCarCharacterGroup(mtc.getCarCharacterGroup());
            customer.setCarNumber(mtc.getCarNumber());
            customer.setCarProvince(mtc.getCarProvince());
        } catch (Exception e) {
            log.error(e);
        }
        return customer;
    }

    private void updateMarketingList(String sessionId, Integer marketingId) {
        String sql = "update marketing_temp_customer set marketing_id = :marketingId"
                + " where session_id = :sessionId and marketing_id is null";

        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void setMarketingTempCustomerValue(MarketingTempCustomer mtc, String key, String value) {
        if (value != null) {
            value = value.trim();
        }
        try {
            if (key.equals("customer.reference_no")) {
                mtc.setReferenceNo(value);
            } else if (key.equals("customer.initial")) {
                mtc.setInitial(value);
            } else if (key.equals("customer.name")) {
                mtc.setName(value);
            } else if (key.equals("customer.surname")) {
                mtc.setSurname(value);
            } else if (key.equals("customer.idno")) {
                mtc.setIdno(value);
            } else if (key.equals("customer.gender")) {
                if ("male".equalsIgnoreCase(value) || "m".equalsIgnoreCase(value) || "ชาย".equalsIgnoreCase(value)) {
                    mtc.setGender("Male");
                } else if ("female".equalsIgnoreCase(value) || "f".equalsIgnoreCase(value) || "หญิง".equalsIgnoreCase(value)) {
                    mtc.setGender("Female");
                }
            } else if (key.equals("customer.dob")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date dob = sdf.parse(value);
                mtc.setDob(dob);
            } else if (key.equals("customer.weight")) {
                mtc.setWeight(new Double(value));
            } else if (key.equals("customer.height")) {
                mtc.setHeight(new Double(value));
            } else if (key.equals("customer.nationality")) {
                mtc.setNationality(value);
            } else if (key.equals("customer.occupation")) {
                mtc.setOccupation(value);
            } else if (key.equals("customer.home_phone")) {
                mtc.setHomePhone(value);
            } else if (key.equals("customer.home_ext")) {
                mtc.setHomeExt(value);
            } else if (key.equals("customer.office_phone")) {
                mtc.setOfficePhone(value);
            } else if (key.equals("customer.office_ext")) {
                mtc.setOfficeExt(value);
            } else if (key.equals("customer.mobile_phone")) {
                mtc.setMobilePhone(value);
            } else if (key.equals("customer.fax")) {
                mtc.setFax(value);
            } else if (key.equals("customer.fax_ext")) {
                mtc.setFaxExt(value);
            } else if (key.equals("customer.email")) {
                mtc.setEmail(value);
            } else if (key.equals("customer.privilege")) {
                mtc.setPrivilege(value);
            } else if (key.equals("customer.contact_phone1")) {
                mtc.setContactPhone1(value);
            } else if (key.equals("customer.contact_phone2")) {
                mtc.setContactPhone2(value);
            } else if (key.equals("customer.contact_phone3")) {
                mtc.setContactPhone3(value);
            } else if (key.equals("customer.contact_phone4")) {
                mtc.setContactPhone4(value);
            } else if (key.equals("customer.contact_phone5")) {
                mtc.setContactPhone5(value);
            } else if (key.equals("customer.remark")) {
                mtc.setRemark(value);
            } else if (key.equals("customer.flexfield1")) {
                mtc.setFlexfield1(value);
            } else if (key.equals("customer.flexfield2")) {
                mtc.setFlexfield2(value);
            } else if (key.equals("customer.flexfield3")) {
                mtc.setFlexfield3(value);
            } else if (key.equals("customer.flexfield4")) {
                mtc.setFlexfield4(value);
            } else if (key.equals("customer.flexfield5")) {
                mtc.setFlexfield5(value);
            } else if (key.equals("customer.flexfield6")) {
                mtc.setFlexfield6(value);
            } else if (key.equals("customer.flexfield7")) {
                mtc.setFlexfield7(value);
            } else if (key.equals("customer.flexfield8")) {
                mtc.setFlexfield8(value);
            } else if (key.equals("customer.flexfield9")) {
                mtc.setFlexfield9(value);
            } else if (key.equals("customer.flexfield10")) {
                mtc.setFlexfield10(value);
            } else if (key.equals("customer.flexfield11")) {
                mtc.setFlexfield11(value);
            } else if (key.equals("customer.flexfield12")) {
                mtc.setFlexfield12(value);
            } else if (key.equals("customer.flexfield13")) {
                mtc.setFlexfield13(value);
            } else if (key.equals("customer.flexfield14")) {
                mtc.setFlexfield14(value);
            } else if (key.equals("customer.flexfield15")) {
                mtc.setFlexfield15(value);
            } else if (key.equals("customer.flexfield16")) {
                mtc.setFlexfield16(value);
            } else if (key.equals("customer.flexfield17")) {
                mtc.setFlexfield17(value);
            } else if (key.equals("customer.flexfield18")) {
                mtc.setFlexfield18(value);
            } else if (key.equals("customer.flexfield19")) {
                mtc.setFlexfield19(value);
            } else if (key.equals("customer.flexfield20")) {
                mtc.setFlexfield20(value);
            } else if (key.equals("customer.flexfield21")) {	                
                mtc.setFlexfield21(value);
            } else if (key.equals("customer.flexfield22")) {	                
                mtc.setFlexfield22(value);
            } else if (key.equals("customer.flexfield23")) {	                
                mtc.setFlexfield23(value);
            } else if (key.equals("customer.flexfield24")) {	                
                mtc.setFlexfield24(value);
            } else if (key.equals("customer.flexfield25")) {	                
                mtc.setFlexfield25(value);
            } else if (key.equals("customer.flexfield26")) {	                
                mtc.setFlexfield26(value);
            } else if (key.equals("customer.flexfield27")) {	                
                mtc.setFlexfield27(value);
            } else if (key.equals("customer.flexfield28")) {	                
                mtc.setFlexfield28(value);
            } else if (key.equals("customer.flexfield29")) {	                
                mtc.setFlexfield29(value);
            } else if (key.equals("customer.flexfield30")) {	                
                mtc.setFlexfield30(value);
            } else if (key.equals("customer.flexfield31")) {	                
                mtc.setFlexfield31(value);
            } else if (key.equals("customer.flexfield32")) {	                
                mtc.setFlexfield32(value);
            } else if (key.equals("customer.flexfield33")) {	                
                mtc.setFlexfield33(value);
            } else if (key.equals("customer.flexfield34")) {	                
                mtc.setFlexfield34(value);
            } else if (key.equals("customer.flexfield35")) {	                
                 mtc.setFlexfield35(value);
            } else if (key.equals("customer.flexfield36")) {	                
                mtc.setFlexfield36(value);
            } else if (key.equals("customer.flexfield37")) {	                
                mtc.setFlexfield37(value);
            } else if (key.equals("customer.flexfield38")) {	                
                mtc.setFlexfield38(value);
            } else if (key.equals("customer.flexfield39")) {	                
                mtc.setFlexfield39(value);
            } else if (key.equals("customer.flexfield40")) {	                
                mtc.setFlexfield40(value);
            } else if (key.equals("customer.flexfield41")) {	                
                mtc.setFlexfield41(value);
            } else if (key.equals("customer.flexfield42")) {	                
                mtc.setFlexfield42(value);
            } else if (key.equals("customer.flexfield43")) {	                
                mtc.setFlexfield43(value);
            } else if (key.equals("customer.flexfield44")) {	                
                mtc.setFlexfield44(value);
            } else if (key.equals("customer.flexfield45")) {	                
                mtc.setFlexfield45(value);
            } else if (key.equals("customer.flexfield46")) {	                
                mtc.setFlexfield46(value);
            } else if (key.equals("customer.flexfield47")) {	                
                mtc.setFlexfield47(value);
            } else if (key.equals("customer.flexfield48")) {	                
                mtc.setFlexfield48(value);
            } else if (key.equals("customer.flexfield49")) {	                
                mtc.setFlexfield49(value);
            } else if (key.equals("customer.flexfield50")) {	                
                mtc.setFlexfield50(value);
            } else if (key.equals("customer.flexfield51")) {	                
                mtc.setFlexfield51(value);
            } else if (key.equals("customer.flexfield52")) {	                
                mtc.setFlexfield52(value);
            } else if (key.equals("customer.flexfield53")) {	                
                mtc.setFlexfield53(value);
            } else if (key.equals("customer.flexfield54")) {	                
                mtc.setFlexfield54(value);
            } else if (key.equals("customer.flexfield55")) {	                
                mtc.setFlexfield55(value);
            } else if (key.equals("customer.flexfield56")) {	                
                mtc.setFlexfield56(value);
            } else if (key.equals("customer.flexfield57")) {	                
                mtc.setFlexfield57(value);
            } else if (key.equals("customer.flexfield58")) {	                
                mtc.setFlexfield58(value);
            } else if (key.equals("customer.flexfield59")) {	                
                mtc.setFlexfield59(value);
            } else if (key.equals("customer.flexfield60")) {	                
                mtc.setFlexfield60(value);
            } else if (key.equals("customer.flexfield61")) {	                
                mtc.setFlexfield61(value);
            } else if (key.equals("customer.flexfield62")) {	                
                mtc.setFlexfield62(value);
            } else if (key.equals("customer.flexfield63")) {	                
                mtc.setFlexfield63(value);
            } else if (key.equals("customer.flexfield64")) {	                
                mtc.setFlexfield64(value);
            } else if (key.equals("customer.flexfield65")) {	                
                mtc.setFlexfield65(value);
            } else if (key.equals("customer.flexfield66")) {	                
                mtc.setFlexfield66(value);
            } else if (key.equals("customer.flexfield67")) {	                
                mtc.setFlexfield67(value);
            } else if (key.equals("customer.flexfield68")) {	                
                mtc.setFlexfield68(value);
            } else if (key.equals("customer.flexfield69")) {	                
                mtc.setFlexfield69(value);
            } else if (key.equals("customer.flexfield70")) {	                
                mtc.setFlexfield70(value);
            } else if (key.equals("customer.flexfield71")) {	                
                mtc.setFlexfield71(value);
            } else if (key.equals("customer.flexfield72")) {	                
                mtc.setFlexfield72(value);
            } else if (key.equals("customer.flexfield73")) {	                
                mtc.setFlexfield73(value);
            } else if (key.equals("customer.flexfield74")) {	                
                mtc.setFlexfield74(value);
            } else if (key.equals("customer.flexfield75")) {	                
                mtc.setFlexfield75(value);
            } else if (key.equals("customer.flexfield76")) {	                
                mtc.setFlexfield76(value);
            } else if (key.equals("customer.flexfield77")) {	                
                mtc.setFlexfield77(value);
            } else if (key.equals("customer.flexfield78")) {	                
                mtc.setFlexfield78(value);
            } else if (key.equals("customer.flexfield79")) {	                
                mtc.setFlexfield79(value);
            } else if (key.equals("customer.flexfield80")) {	                
                mtc.setFlexfield80(value);
            } else if (key.equals("customer.flexfield81")) {	                
                mtc.setFlexfield81(value);
            } else if (key.equals("customer.flexfield82")) {	                
                mtc.setFlexfield82(value);
            } else if (key.equals("customer.flexfield83")) {	                
                mtc.setFlexfield83(value);
            } else if (key.equals("customer.flexfield84")) {	                
                mtc.setFlexfield84(value);
            } else if (key.equals("customer.flexfield85")) {	                
                mtc.setFlexfield85(value);
            } else if (key.equals("customer.flexfield86")) {	                
                mtc.setFlexfield86(value);
            } else if (key.equals("customer.flexfield87")) {	                
                mtc.setFlexfield87(value);
            } else if (key.equals("customer.flexfield88")) {	                
                mtc.setFlexfield88(value);
            } else if (key.equals("customer.flexfield89")) {	                
                mtc.setFlexfield89(value);
            } else if (key.equals("customer.flexfield90")) {	                
                mtc.setFlexfield90(value);
            } else if (key.equals("customer.flexfield91")) {	                
                mtc.setFlexfield91(value);
            } else if (key.equals("customer.flexfield92")) {	                
                mtc.setFlexfield92(value);
            } else if (key.equals("customer.flexfield93")) {	                
                mtc.setFlexfield93(value);
            } else if (key.equals("customer.flexfield94")) {	                
                mtc.setFlexfield94(value);
            } else if (key.equals("customer.flexfield95")) {	                
                mtc.setFlexfield95(value);
            } else if (key.equals("customer.flexfield96")) {	                
                mtc.setFlexfield96(value);
            } else if (key.equals("customer.flexfield97")) {	                
                mtc.setFlexfield97(value);
            } else if (key.equals("customer.flexfield98")) {	                
                mtc.setFlexfield98(value);
            } else if (key.equals("customer.flexfield99")) {	                
                mtc.setFlexfield99(value);
            } else if (key.equals("customer.flexfield100")) {	                
                mtc.setFlexfield100(value);
            } else if (key.equals("customer.current_fullname")) {
                mtc.setCurrentFullname(value);
            } else if (key.equals("customer.current_address_line1")) {
                mtc.setCurrentAddressLine1(value);
            } else if (key.equals("customer.current_address_line2")) {
                mtc.setCurrentAddressLine2(value);
            } else if (key.equals("customer.current_address_line3")) {
                mtc.setCurrentAddressLine3(value);
            } else if (key.equals("customer.current_address_line4")) {
                mtc.setCurrentAddressLine4(value);
            } else if (key.equals("customer.current_address_line5")) {
                mtc.setCurrentAddressLine5(value);
            } else if (key.equals("customer.current_address_line6")) {
                mtc.setCurrentAddressLine6(value);
            } else if (key.equals("customer.current_address_line7")) {
                mtc.setCurrentAddressLine7(value);
            } else if (key.equals("customer.current_address_line8")) {
                mtc.setCurrentAddressLine8(value);
            } else if (key.equals("customer.current_district_name")) {
                mtc.setCurrentDistrictName(value);
            } else if (key.equals("customer.current_province_name")) {
                mtc.setCurrentProvinceName(value);
            } else if (key.equals("customer.current_postal_code")) {
                mtc.setCurrentPostalCode(value);
            } else if (key.equals("customer.home_fullname")) {
                mtc.setHomeFullname(value);
            } else if (key.equals("customer.home_address_line1")) {
                mtc.setHomeAddressLine1(value);
            } else if (key.equals("customer.home_address_line2")) {
                mtc.setHomeAddressLine2(value);
            } else if (key.equals("customer.home_address_line3")) {
                mtc.setHomeAddressLine3(value);
            } else if (key.equals("customer.home_address_line4")) {
                mtc.setHomeAddressLine4(value);
            } else if (key.equals("customer.home_address_line5")) {
                mtc.setHomeAddressLine5(value);
            } else if (key.equals("customer.home_address_line6")) {
                mtc.setHomeAddressLine6(value);
            } else if (key.equals("customer.home_address_line7")) {
                mtc.setHomeAddressLine7(value);
            } else if (key.equals("customer.home_address_line8")) {
                mtc.setHomeAddressLine8(value);
            } else if (key.equals("customer.home_district_name")) {
                mtc.setHomeDistrictName(value);
            } else if (key.equals("customer.home_province_name")) {
                mtc.setHomeProvinceName(value);
            } else if (key.equals("customer.home_postal_code")) {
                mtc.setHomePostalCode(value);
            } else if (key.equals("customer.billing_fullname")) {
                mtc.setBillingFullname(value);
            } else if (key.equals("customer.billing_address_line1")) {
                mtc.setBillingAddressLine1(value);
            } else if (key.equals("customer.billing_address_line2")) {
                mtc.setBillingAddressLine2(value);
            } else if (key.equals("customer.billing_address_line3")) {
                mtc.setBillingAddressLine3(value);
            } else if (key.equals("customer.billing_address_line4")) {
                mtc.setBillingAddressLine4(value);
            } else if (key.equals("customer.billing_address_line5")) {
                mtc.setBillingAddressLine5(value);
            } else if (key.equals("customer.billing_address_line6")) {
                mtc.setBillingAddressLine6(value);
            } else if (key.equals("customer.billing_address_line7")) {
                mtc.setBillingAddressLine7(value);
            } else if (key.equals("customer.billing_address_line8")) {
                mtc.setBillingAddressLine8(value);
            } else if (key.equals("customer.billing_district_name")) {
                mtc.setBillingDistrictName(value);
            } else if (key.equals("customer.billing_province_name")) {
                mtc.setBillingProvinceName(value);
            } else if (key.equals("customer.billing_postal_code")) {
                mtc.setBillingPostalCode(value);
            } else if (key.equals("customer.shipping_fullname")) {
                mtc.setShippingFullname(value);
            } else if (key.equals("customer.shipping_address_line1")) {
                mtc.setShippingAddressLine1(value);
            } else if (key.equals("customer.shipping_address_line2")) {
                mtc.setShippingAddressLine2(value);
            } else if (key.equals("customer.shipping_address_line3")) {
                mtc.setShippingAddressLine3(value);
            } else if (key.equals("customer.shipping_address_line4")) {
                mtc.setShippingAddressLine4(value);
            } else if (key.equals("customer.shipping_address_line5")) {
                mtc.setShippingAddressLine5(value);
            } else if (key.equals("customer.shipping_address_line6")) {
                mtc.setShippingAddressLine6(value);
            } else if (key.equals("customer.shipping_address_line7")) {
                mtc.setShippingAddressLine7(value);
            } else if (key.equals("customer.shipping_address_line8")) {
                mtc.setShippingAddressLine8(value);
            } else if (key.equals("customer.shipping_district_name")) {
                mtc.setShippingDistrictName(value);
            } else if (key.equals("customer.shipping_province_name")) {
                mtc.setShippingProvinceName(value);
            } else if (key.equals("customer.shipping_postal_code")) {
                mtc.setShippingPostalCode(value);
            } else if (key.equals("customer.car_brand")) {
                mtc.setCarBrand(value);
            } else if (key.equals("customer.car_model")) {
                mtc.setCarModel(value);
            } else if (key.equals("customer.car_year")) {
                if (value != null && !value.equals("") && !value.equals("0")) {
                    mtc.setCarYear(new Integer(value));
                }
            } else if (key.equals("customer.car_character_group")) {
                mtc.setCarCharacterGroup(value);
            } else if (key.equals("customer.car_number")) {
                mtc.setCarNumber(value);
            } else if (key.equals("customer.car_province")) {
                mtc.setCarProvince(value);
            } else if (key.equals("marketing_customer.list_value_001")) {
                mtc.setListValue001(value);
            } else if (key.equals("marketing_customer.list_value_002")) {
                mtc.setListValue002(value);
            } else if (key.equals("marketing_customer.list_value_003")) {
                mtc.setListValue003(value);
            } else if (key.equals("marketing_customer.list_value_004")) {
                mtc.setListValue004(value);
            } else if (key.equals("marketing_customer.list_value_005")) {
                mtc.setListValue005(value);
            } else if (key.equals("marketing_customer.list_value_006")) {
                mtc.setListValue006(value);
            } else if (key.equals("marketing_customer.list_value_007")) {
                mtc.setListValue007(value);
            } else if (key.equals("marketing_customer.list_value_008")) {
                mtc.setListValue008(value);
            } else if (key.equals("marketing_customer.list_value_009")) {
                mtc.setListValue009(value);
            } else if (key.equals("marketing_customer.list_value_010")) {
                mtc.setListValue010(value);
            } else if (key.equals("marketing_customer.list_value_011")) {
                mtc.setListValue011(value);
            } else if (key.equals("marketing_customer.list_value_012")) {
                mtc.setListValue012(value);
            } else if (key.equals("marketing_customer.list_value_013")) {
                mtc.setListValue013(value);
            } else if (key.equals("marketing_customer.list_value_014")) {
                mtc.setListValue014(value);
            } else if (key.equals("marketing_customer.list_value_015")) {
                mtc.setListValue015(value);
            } else if (key.equals("marketing_customer.list_value_016")) {
                mtc.setListValue016(value);
            } else if (key.equals("marketing_customer.list_value_017")) {
                mtc.setListValue017(value);
            } else if (key.equals("marketing_customer.list_value_018")) {
                mtc.setListValue018(value);
            } else if (key.equals("marketing_customer.list_value_019")) {
                mtc.setListValue019(value);
            } else if (key.equals("marketing_customer.list_value_020")) {
                mtc.setListValue020(value);
            } else if (key.equals("marketing_customer.list_value_021")) {
                mtc.setListValue021(value);
            } else if (key.equals("marketing_customer.list_value_022")) {
                mtc.setListValue022(value);
            } else if (key.equals("marketing_customer.list_value_023")) {
                mtc.setListValue023(value);
            } else if (key.equals("marketing_customer.list_value_024")) {
                mtc.setListValue024(value);
            } else if (key.equals("marketing_customer.list_value_025")) {
                mtc.setListValue025(value);
            } else if (key.equals("marketing_customer.list_value_026")) {
                mtc.setListValue026(value);
            } else if (key.equals("marketing_customer.list_value_027")) {
                mtc.setListValue027(value);
            } else if (key.equals("marketing_customer.list_value_028")) {
                mtc.setListValue028(value);
            } else if (key.equals("marketing_customer.list_value_029")) {
                mtc.setListValue029(value);
            } else if (key.equals("marketing_customer.list_value_030")) {
                mtc.setListValue030(value);
            } else if (key.equals("marketing_customer.list_value_031")) {
                mtc.setListValue031(value);
            } else if (key.equals("marketing_customer.list_value_032")) {
                mtc.setListValue032(value);
            } else if (key.equals("marketing_customer.list_value_033")) {
                mtc.setListValue033(value);
            } else if (key.equals("marketing_customer.list_value_034")) {
                mtc.setListValue034(value);
            } else if (key.equals("marketing_customer.list_value_035")) {
                mtc.setListValue035(value);
            } else if (key.equals("marketing_customer.list_value_036")) {
                mtc.setListValue036(value);
            } else if (key.equals("marketing_customer.list_value_037")) {
                mtc.setListValue037(value);
            } else if (key.equals("marketing_customer.list_value_038")) {
                mtc.setListValue038(value);
            } else if (key.equals("marketing_customer.list_value_039")) {
                mtc.setListValue039(value);
            } else if (key.equals("marketing_customer.list_value_040")) {
                mtc.setListValue040(value);
            } else if (key.equals("marketing_customer.list_value_041")) {
                mtc.setListValue041(value);
            } else if (key.equals("marketing_customer.list_value_042")) {
                mtc.setListValue042(value);
            } else if (key.equals("marketing_customer.list_value_043")) {
                mtc.setListValue043(value);
            } else if (key.equals("marketing_customer.list_value_044")) {
                mtc.setListValue044(value);
            } else if (key.equals("marketing_customer.list_value_045")) {
                mtc.setListValue045(value);
            } else if (key.equals("marketing_customer.list_value_046")) {
                mtc.setListValue046(value);
            } else if (key.equals("marketing_customer.list_value_047")) {
                mtc.setListValue047(value);
            } else if (key.equals("marketing_customer.list_value_048")) {
                mtc.setListValue048(value);
            } else if (key.equals("marketing_customer.list_value_049")) {
                mtc.setListValue049(value);
            } else if (key.equals("marketing_customer.list_value_050")) {
                mtc.setListValue050(value);
            } else if (key.equals("marketing_customer.list_value_051")) {
                mtc.setListValue051(value);
            } else if (key.equals("marketing_customer.list_value_052")) {
                mtc.setListValue052(value);
            } else if (key.equals("marketing_customer.list_value_053")) {
                mtc.setListValue053(value);
            } else if (key.equals("marketing_customer.list_value_054")) {
                mtc.setListValue054(value);
            } else if (key.equals("marketing_customer.list_value_055")) {
                mtc.setListValue055(value);
            } else if (key.equals("marketing_customer.list_value_056")) {
                mtc.setListValue056(value);
            } else if (key.equals("marketing_customer.list_value_057")) {
                mtc.setListValue057(value);
            } else if (key.equals("marketing_customer.list_value_058")) {
                mtc.setListValue058(value);
            } else if (key.equals("marketing_customer.list_value_059")) {
                mtc.setListValue059(value);
            } else if (key.equals("marketing_customer.list_value_060")) {
                mtc.setListValue060(value);
            } else if (key.equals("marketing_customer.list_value_061")) {
                mtc.setListValue061(value);
            } else if (key.equals("marketing_customer.list_value_062")) {
                mtc.setListValue062(value);
            } else if (key.equals("marketing_customer.list_value_063")) {
                mtc.setListValue063(value);
            } else if (key.equals("marketing_customer.list_value_064")) {
                mtc.setListValue064(value);
            } else if (key.equals("marketing_customer.list_value_065")) {
                mtc.setListValue065(value);
            } else if (key.equals("marketing_customer.list_value_066")) {
                mtc.setListValue066(value);
            } else if (key.equals("marketing_customer.list_value_067")) {
                mtc.setListValue067(value);
            } else if (key.equals("marketing_customer.list_value_068")) {
                mtc.setListValue068(value);
            } else if (key.equals("marketing_customer.list_value_069")) {
                mtc.setListValue069(value);
            } else if (key.equals("marketing_customer.list_value_070")) {
                mtc.setListValue070(value);
            } else if (key.equals("marketing_customer.list_value_071")) {
                mtc.setListValue071(value);
            } else if (key.equals("marketing_customer.list_value_072")) {
                mtc.setListValue072(value);
            } else if (key.equals("marketing_customer.list_value_073")) {
                mtc.setListValue073(value);
            } else if (key.equals("marketing_customer.list_value_074")) {
                mtc.setListValue074(value);
            } else if (key.equals("marketing_customer.list_value_075")) {
                mtc.setListValue075(value);
            } else if (key.equals("marketing_customer.list_value_076")) {
                mtc.setListValue076(value);
            } else if (key.equals("marketing_customer.list_value_077")) {
                mtc.setListValue077(value);
            } else if (key.equals("marketing_customer.list_value_078")) {
                mtc.setListValue078(value);
            } else if (key.equals("marketing_customer.list_value_079")) {
                mtc.setListValue079(value);
            } else if (key.equals("marketing_customer.list_value_080")) {
                mtc.setListValue080(value);
            } else if (key.equals("marketing_customer.list_value_081")) {
                mtc.setListValue081(value);
            } else if (key.equals("marketing_customer.list_value_082")) {
                mtc.setListValue082(value);
            } else if (key.equals("marketing_customer.list_value_083")) {
                mtc.setListValue083(value);
            } else if (key.equals("marketing_customer.list_value_084")) {
                mtc.setListValue084(value);
            } else if (key.equals("marketing_customer.list_value_085")) {
                mtc.setListValue085(value);
            } else if (key.equals("marketing_customer.list_value_086")) {
                mtc.setListValue086(value);
            } else if (key.equals("marketing_customer.list_value_087")) {
                mtc.setListValue087(value);
            } else if (key.equals("marketing_customer.list_value_088")) {
                mtc.setListValue088(value);
            } else if (key.equals("marketing_customer.list_value_089")) {
                mtc.setListValue089(value);
            } else if (key.equals("marketing_customer.list_value_090")) {
                mtc.setListValue090(value);
            } else if (key.equals("marketing_customer.list_value_091")) {
                mtc.setListValue091(value);
            } else if (key.equals("marketing_customer.list_value_092")) {
                mtc.setListValue092(value);
            } else if (key.equals("marketing_customer.list_value_093")) {
                mtc.setListValue093(value);
            } else if (key.equals("marketing_customer.list_value_094")) {
                mtc.setListValue094(value);
            } else if (key.equals("marketing_customer.list_value_095")) {
                mtc.setListValue095(value);
            } else if (key.equals("marketing_customer.list_value_096")) {
                mtc.setListValue096(value);
            } else if (key.equals("marketing_customer.list_value_097")) {
                mtc.setListValue097(value);
            } else if (key.equals("marketing_customer.list_value_098")) {
                mtc.setListValue098(value);
            } else if (key.equals("marketing_customer.list_value_099")) {
                mtc.setListValue099(value);
            } else if (key.equals("marketing_customer.list_value_100")) {
                mtc.setListValue100(value);
            }

        } catch (Exception e) {
            log.error(e);
        }
    }

    public void edit(Marketing marketing) throws IllegalOrphanException, NonexistentEntityException, Exception {
        marketing = em.merge(marketing);
    }

    public void edit1(Marketing marketing) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            Query q = em.createNativeQuery("update marketing set total_assigned = ?1 where id = ?2");
            q.setParameter(1, marketing.getTotalAssigned());
            q.setParameter(2, marketing.getId());

            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Marketing marketing;
        try {
            marketing = em.getReference(Marketing.class, id);
            marketing.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The marketing with id " + id + " no longer exists.", enfe);
        }
        em.remove(marketing);
    }

    public List<Marketing> findMarketingEntities() {
        return findMarketingEntities(true, -1, -1);
    }

    public List<Marketing> findMarketingEntities(int maxResults, int firstResult) {
        return findMarketingEntities(false, maxResults, firstResult);
    }

    private List<Marketing> findMarketingEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Marketing as o where o.enable = true order by o.createDate desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    private List<Marketing> findTransferMarketingEntities() {
        Query q = em.createQuery("select object(o) from Marketing as o where o.totalAssigned is not null and o.startDate <= ?1 and o.endDate >= ?1 and o.enable = true and o.status = true order by o.code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public List<Marketing> findAvailableMarketingEntities() {
        Query q = em.createQuery("select object(o) from Marketing as o where (o.totalAssigned is null or o.totalAssigned < o.totalRecord) and o.totalRecord > 0 and o.enable = true and o.status = true and o.startDate <= ?1 and o.endDate >= ?1 order by o.code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public List<Marketing> findAvailableMarketingEntitiesByUserGroup(UserGroup usergroup) {
        Query q = em.createQuery("select object(o) from Marketing as o join o.userGroupCollection ug where (o.totalAssigned is null or o.totalAssigned < o.totalRecord) and o.totalRecord > 0 and o.enable = true and o.status = true and o.startDate <= ?1 and o.endDate >= ?1 and ug.id = ?2"
                + " order by o.code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        q.setParameter(2, usergroup.getId().intValue());
        return q.getResultList();
    }
    
    public List<Marketing> findAvailableMarketingEntitiesByUser(Campaign campaign, Users user) {
        Query q = em.createQuery("select object(o) from Marketing as o where (totalAssigned is null or totalAssigned < totalRecord) and totalRecord > 0 and enable = true and status = true and startDate <= ?1 and endDate >= ?1"
                + " and o.id in (select distinct a.assignmentId.marketing.id from AssignmentSupervisor as a where a.assignmentId.campaign.id = ?2 and a.userId.id = ?3)"
                + " order by code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        q.setParameter(2, campaign.getId().intValue());
        q.setParameter(3, user.getId().intValue());
        return q.getResultList();
    }

    public List<Marketing> findAvailableMarketingFromSup(Campaign campaign) {
        Query q = em.createQuery("select object(o) from Marketing as o where enable = true and status = true and startDate <= ?3 and endDate >= ?3 and o.id IN "
                + "(SELECT DISTINCT a.marketing FROM Assignment AS a , AssignmentSupervisor AS s WHERE a.id = s.assignmentId AND s.userId.id = ?1 and a.campaign = ?2 GROUP BY a.marketing "
                + "HAVING ((SUM(s.noCustomer) - SUM(s.noUsed)) != 0)) order by code");
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        q.setParameter(2, campaign);
        q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public Marketing findMarketing(Integer id) {
        return em.find(Marketing.class, id);
    }

    public int getMarketingCount() {
        return ((Long) em.createQuery("select count(o) from Marketing as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getMarketList() {
        List<Marketing> marketings = this.findMarketingEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getAvaialableMarketList() {
        List<Marketing> marketings = this.findAvailableMarketingEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getAvaialableMarketListFromSup(Campaign campaign) {
        List<Marketing> marketings = this.findAvailableMarketingFromSup(campaign);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getAvaialableTransferMarketList() {
        List<Marketing> marketings = this.findTransferMarketingEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public List<Marketing> findBy(String keyword) {
        String sql = "select object(o) from Marketing as o where o.enable = true";
        if (!keyword.isEmpty()) {
            sql += " and o.name like :keyword";
        }
        sql += " order by o.createDate desc";
        Query q = em.createQuery(sql);
        if (!keyword.isEmpty()) {
            q.setParameter("keyword", "%" + keyword.trim() + "%");
        }
        return q.getResultList();
    }

    public List<String[]> findMtcBySessionId(String sessionId, String str) {
        String sql = "select ";
        sql += str;
        sql += " from marketing_temp_customer as m"
                + " where m.session_id = :sessionId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);

        return q.getResultList();
    }

    public List<MarketingTempCustomer> findMtcBySessionId(String sessionId) {
        String sql = "select object(o) from MarketingTempCustomer as o "
                + "where o.sessionId = :sessionId";

        Query q = em.createQuery(sql);
        q.setParameter("sessionId", sessionId);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");

        return q.getResultList();
    }

    public List<MarketingTempCustomer> findByFilter(String sessionId, List<MarketingCriteria> mcList) {

        List<MarketingTempCustomer> list = null;

        boolean bNew = checkCriteriaNew(mcList);
        boolean bSale = checkCriteriaSale(mcList);

        try {
            String sql1 = "select object(m) from MarketingTempCustomer as m"
                    + " left join m.customer c"
                    + " where m.sessionId = :sessionId";
            if (bSale) {
                sql1 += " and c.id in";
                sql1 += " (select po.customer.id from PurchaseOrder as po"
                        + " left join po.purchaseOrderDetailCollection as pod"
                        + " left join po.contactHistoryCollection as contactHistory"
                        + " left join po.nosaleReason as nosaleReason"
                        + " left join po.approvalReason as approvalReason"
                        + " left join po.paymentReason as paymentReason"
                        + " left join po.qcReason as qcReason"
                        + " where 1=1";
                sql1 += this.genSQLSale(mcList, bSale);
                sql1 += ")";
            }

            String sql2 = this.genSQLNew(mcList, bNew);

            String sql = sql1 + sql2;
            Query q = em.createQuery(sql);
            q.setParameter("sessionId", sessionId);
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }
        return list;
    }

    private String genSQLNew(List<MarketingCriteria> mcList, boolean bChk) {
        String sql = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        for (MarketingCriteria mc : mcList) {
            if (bChk) {
                sql += " and"
                        + " (1=1";

                if (mc.getSelectDob() != null && mc.getSelectDob() && mc.getDobFrom() != null && mc.getDobTo() != null) {
                    sql += " and m.dob between '" + sdf.format(mc.getDobFrom()) + "' and '" + sdf.format(mc.getDobTo()) + "'";
                }
                if (mc.getSelectGender() != null && mc.getSelectGender()) {
                    sql += " and (";
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(mc.getGender(), ",");
                    while (st.hasMoreTokens()) {
                        String str = StringUtils.trim((String) st.nextToken());
                        if (i != 0) {
                            sql += " or";
                        }
                        sql += " ltrim(rtrim(m.gender)) = '" + str + "'";
                        i++;
                    }
                    sql += " )";
                }
                if (mc.getSelectProvince() != null && mc.getSelectProvince()) {
                    sql += " and (";
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(mc.getProvince(), ",");
                    while (st.hasMoreTokens()) {
                        String str = StringUtils.trim((String) st.nextToken());
                        if (i != 0) {
                            sql += " or";
                        }
                        sql += " ltrim(rtrim(m.currentProvinceName)) = '" + str + "'";
                        i++;
                    }
                    sql += " )";
                }
                if (mc.getSelectOccupation() != null && mc.getSelectOccupation()) {
                    sql += " and (";
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(mc.getOccupation(), ",");
                    while (st.hasMoreTokens()) {
                        String str = StringUtils.trim((String) st.nextToken());
                        if (i != 0) {
                            sql += " or";
                        }
                        sql += " ltrim(rtrim(m.occupation)) = '" + str + "'";
                        i++;
                    }
                    sql += " )";
                }
                if (mc.getSelectSalary() != null && mc.getSelectSalary()) {
                    if (mc.getSalaryFrom() != null) {
                        sql += " and m.salaryFrom >= " + mc.getSalaryFrom();
                    }
                    if (mc.getSalaryTo() != null) {
                        sql += " and m.salaryTo <= " + mc.getSalaryTo();
                    }
                }
                if (mc.getSelectApplicationDate() != null && mc.getSelectApplicationDate()) {
                    sql += " ";
                }
                if (mc.getSelectTelephonePrefix() != null && mc.getSelectTelephonePrefix() && mc.getTelephonePrefix() != null && !mc.getTelephonePrefix().isEmpty()) {
                    sql += " and (";
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(mc.getTelephonePrefix(), ",");
                    while (st.hasMoreTokens()) {
                        String pre = StringUtils.trim((String) st.nextToken());
                        if (i != 0) {
                            sql += " or";
                        }
                        sql += " (ltrim(rtrim(m.homePhone)) like '" + pre + "%' or ltrim(rtrim(m.officePhone)) like '" + pre + "%' or ltrim(rtrim(m.mobilePhone)) like '" + pre + "%' or ltrim(rtrim(m.fax)) like '" + pre + "%')";
                        i++;
                    }
                    sql += " )";
                }
                sql += ")";
            }
        }

        return sql;
    }

    private String genSQLSale(List<MarketingCriteria> mcList, boolean bChk) {
        String sql = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        for (MarketingCriteria mc : mcList) {
            if (bChk) {
                sql = " and"
                        + " (1=1";
                if (mc.getSelectProduct() != null && mc.getSelectProduct()) {
                    sql += " and pod.product.id in (" + mc.getProduct() + ")";
                }
                if (mc.getSelectContactDate() != null && mc.getSelectContactDate()) {
                    if (mc.getContactDateFrom() != null) {
                        sql += " and contactHistory.contactDate >= '" + sdf.format(mc.getContactDateFrom()) + "'";
                    }
                    if (mc.getContactDateTo() != null) {
                        sql += " and contactHistory.contactDate <= '" + sdf.format(mc.getContactDateTo()) + "'";
                    }
                }
                if (mc.getSelectSaleDate() != null && mc.getSelectSaleDate()) {
                    if (mc.getSaleDateFrom() != null) {
                        sql += " and po.saleDate >= '" + sdf.format(mc.getSaleDateFrom()) + "'";
                    }
                    if (mc.getSaleDateTo() != null) {
                        sql += " and po.saleDate <= '" + sdf.format(mc.getSaleDateTo()) + "'";
                    }
                }
                if (mc.getSelectEffectiveDate() != null && mc.getSelectEffectiveDate()) {

                }
                if (mc.getSelectCancelDate() != null && mc.getSelectCancelDate()) {

                }
                if (mc.getSelectOpout() != null && mc.getSelectOpout()) {

                }
                if (mc.getSelectNosaleReason() != null && mc.getSelectNosaleReason()) {
                    sql += " and po.nosaleReason.id in (" + mc.getNosaleReason() + ")";
                }
                if (mc.getSelectQcReject() != null && mc.getSelectQcReject()) {
                    sql += " and po.qcStatus = 'rejected' and po.qcReason in (" + mc.getQcReject() + ")";
                }
                if (mc.getSelectUwReject() != null && mc.getSelectUwReject()) {
                    sql += " and po.approvalStatus = 'rejected' and po.approvalReason in (" + mc.getUwReject() + ")";
                }
                if (mc.getSelectPaymentReject() != null && mc.getSelectPaymentReject()) {
                    sql += " and po.paymentStatus = 'rejected' and po.paymentReason in (" + mc.getPaymentReject() + ")";
                }
                sql += " )";

            }
        }

        return sql;
    }

    private boolean checkCriteriaNew(List<MarketingCriteria> mcList) {
        boolean bCheck = false;
        for (MarketingCriteria mc : mcList) {
            if ((mc.getSelectDob() != null && mc.getSelectDob())
                    || (mc.getSelectGender() != null && mc.getSelectGender())
                    || (mc.getSelectProvince() != null && mc.getSelectProvince())
                    || (mc.getSelectOccupation() != null && mc.getSelectOccupation())
                    || (mc.getSelectSalary() != null && mc.getSelectSalary())
                    || (mc.getSelectApplicationDate() != null && mc.getSelectApplicationDate())
                    || (mc.getSelectTelephonePrefix() != null && mc.getSelectTelephonePrefix())) {
                bCheck = true;
                break;
            }
        }
        return bCheck;

    }

    private boolean checkCriteriaSale(List<MarketingCriteria> mcList) {
        boolean bCheck = false;
        for (MarketingCriteria mc : mcList) {
            if ((mc.getSelectProduct() != null && mc.getSelectProduct())
                    || (mc.getSelectContactDate() != null && mc.getSelectContactDate())
                    || (mc.getSelectSaleDate() != null && mc.getSelectSaleDate())
                    || (mc.getSelectEffectiveDate() != null && mc.getSelectEffectiveDate())
                    || (mc.getSelectCancelDate() != null && mc.getSelectCancelDate())
                    || (mc.getSelectOpout() != null && mc.getSelectOpout())
                    || (mc.getSelectNosaleReason() != null && mc.getSelectNosaleReason())
                    || (mc.getSelectQcReject() != null && mc.getSelectQcReject())
                    || (mc.getSelectUwReject() != null && mc.getSelectUwReject())
                    || (mc.getSelectPaymentReject() != null && mc.getSelectPaymentReject())) {
                bCheck = true;
                break;
            }
        }
        return bCheck;

    }

    public List<CriteriaListValue> findMarketinCriteriaByMarketingId(Integer marketingId) {
        //Current Address Line 8 = Sub District
        String sql = "select new " + CriteriaListValue.class.getName() + " (cf.mappingField, cf.name, '', '', '') "
                + "from Marketing m "
                + "join m.fileTemplate f "
                + "join f.fileTemplateMappingCollection fm "
                + "join fm.customerField cf "
                + "where m.enable = true and m.id = ?1 "
                + "and (cf.mappingField like 'customer.name' or cf.mappingField like 'customer.surname' "
                + "or cf.mappingField like 'customer.gender' or cf.mappingField like 'customer.dob' "
                + "or cf.mappingField like 'customer.home_phone' or cf.mappingField like 'customer.office_phone' or cf.mappingField like 'customer.mobile_phone' "
                + "or cf.mappingField like 'customer.privilege' or cf.mappingField like 'customer.current_province_name'"
                + "or cf.mappingField like 'customer.current_district_name' or cf.mappingField like 'customer.current_address_line8')";
//                + "or cf.mappingField like '%weight%' or cf.mappingField like '%height%') ";
//                + "or cf.mappingField like 'customer.car_brand' or cf.mappingField like 'customer.car_model' or cf.mappingField like 'customer.car_year' "
        //+ "or cf.mappingField like '%nationality%' or cf.mappingField like '%occupation%' or cf.mappingField like '%remark%' ) ";      

        Query q = em.createQuery(sql);
        q.setParameter(1, marketingId);
        return q.getResultList();
    }

    public List<CriteriaListValue> findMarketinAdvanceCriteriaByMarketingId(Integer marketingId) {
        String sql = "select new " + CriteriaListValue.class.getName() + " "
                + "(cf.mappingField, fm.fileColumnName, fm.fieldType, fm.pattern, '', '', '', '') "
                + "from Marketing m "
                + "join m.fileTemplate f "
                + "join f.fileTemplateMappingCollection fm "
                + "join fm.customerField cf "
                + "where m.enable = true and m.id = ?1 "
                + "and (cf.mappingField like 'customer.flexfield%')";

        Query q = em.createQuery(sql);
        q.setParameter(1, marketingId);
        return q.getResultList();
    }
    
    public List<CriteriaListValue> findMarketinAdvanceCriteriaByFileTemplate(Integer templateId) {
        String sql = "select new " + CriteriaListValue.class.getName() + " "
                + "(cf.mappingField, fm.fileColumnName, fm.fieldType, fm.pattern, '', '', '', '') "
                + "from FileTemplate f "
                + "join f.fileTemplateMappingCollection fm "
                + "join fm.customerField cf "
                + "where f.id = ?1 "
                + "and (cf.mappingField like 'customer.flexfield%')";

        Query q = em.createQuery(sql);
        q.setParameter(1, templateId);
        return q.getResultList();
    }

    private List<Marketing> findAvailableAllMarketingEntities() {
        Query q = em.createQuery("select object(o) from Marketing as o where enable = true and status = true and startDate <= ?1 and endDate >= ?1 order by code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public Map<String, Integer> getAvaialableAllMarketList() {  //fine all avialable check startdate-enddate
        List<Marketing> marketings = this.findAvailableAllMarketingEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public List<Customer> createCustomer(List<MarketingTempCustomer> list) {
        List<Customer> customerList = new ArrayList<Customer>();
        Customer customer = null;
        Date now = new Date();
        for (MarketingTempCustomer mtc : list) {
            if (mtc.getListStatus().equals("new")) {
                customer = this.setCustomerValue(mtc);
                customer.setCreateDate(now);
                em.persist(customer);
            } else if (mtc.getListStatus().equals("exist")) {
                customer = mtc.getCustomer();
            }
            customerList.add(customer);
        }
        return customerList;
    }

    private Customer setCustomerValue(MarketingTempCustomer mtc) {
        Customer customer = new Customer();
        try {
            customer.setReferenceNo(mtc.getReferenceNo());
            customer.setInitial(mtc.getInitial());
            customer.setName(mtc.getName());
            customer.setSurname(mtc.getSurname());
            customer.setIdno(mtc.getIdno());
            customer.setGender(mtc.getGender());
            customer.setDob(mtc.getDob());
            customer.setWeight(mtc.getWeight());
            customer.setHeight(mtc.getHeight());
            customer.setNationality(mtc.getNationality());
            customer.setOccupation(mtc.getOccupation());
            customer.setHomePhone(mtc.getHomePhone());
            customer.setHomeExt(mtc.getHomeExt());
            customer.setOfficePhone(mtc.getOfficePhone());
            customer.setOfficeExt(mtc.getOfficeExt());
            customer.setMobilePhone(mtc.getMobilePhone());
            customer.setFax(mtc.getFax());
            customer.setEmail(mtc.getEmail());
            customer.setCustomerType(new CustomerType(mtc.getCustomerType()));
            customer.setCurrentFullname(mtc.getCurrentFullname());
            customer.setCurrentAddressLine1(mtc.getCurrentAddressLine1());
            customer.setCurrentAddressLine2(mtc.getCurrentAddressLine2());
            customer.setCurrentPostalCode(mtc.getCurrentPostalCode());
            customer.setContactPhone1(mtc.getContactPhone1());
            customer.setContactPhone2(mtc.getContactPhone2());
            customer.setContactPhone3(mtc.getContactPhone3());
            customer.setContactPhone4(mtc.getContactPhone4());
            customer.setContactPhone5(mtc.getContactPhone5());
            customer.setRemark(mtc.getRemark());
            customer.setFlexfield1(mtc.getFlexfield1());
            customer.setFlexfield2(mtc.getFlexfield2());
            customer.setFlexfield3(mtc.getFlexfield3());
            customer.setFlexfield4(mtc.getFlexfield4());
            customer.setFlexfield5(mtc.getFlexfield5());
            customer.setFlexfield6(mtc.getFlexfield6());
            customer.setFlexfield7(mtc.getFlexfield7());
            customer.setFlexfield8(mtc.getFlexfield8());
            customer.setFlexfield9(mtc.getFlexfield9());
            customer.setFlexfield10(mtc.getFlexfield10());
            customer.setFlexfield11(mtc.getFlexfield11());
            customer.setFlexfield12(mtc.getFlexfield12());
            customer.setFlexfield13(mtc.getFlexfield13());
            customer.setFlexfield14(mtc.getFlexfield14());
            customer.setFlexfield15(mtc.getFlexfield15());
        } catch (Exception e) {
            log.error(e);
        }
        return customer;
    }

    //public void checkExisting(List<MarketingTempCustomer> list, String sessionId){
    public void checkExisting(String sessionId) {
        //String name = "";
        //String surname = "";
        //String idno = "";
        //String mobilePhone = "";
        //for(MarketingTempCustomer mtc : list){

            //if(mtc.getName() == null || mtc.getName().equals("")) name = ""; else name = mtc.getName().trim();
        //if(mtc.getSurname() == null || mtc.getSurname().equals("")) surname = ""; else surname = mtc.getSurname().trim();
        //if(mtc.getIdno() == null || mtc.getIdno().equals("")) idno = ""; else idno = mtc.getIdno().trim();
        //if(mtc.getMobilePhone() == null || mtc.getMobilePhone().equals("")) mobilePhone = ""; else mobilePhone = mtc.getMobilePhone().trim();
        /*String sql = "select o.id from Customer o where"
         + " case when o.name is null then '' else ltrim(rtrim(o.name)) end  = :name"
         + " and case when o.surname is null then '' else ltrim(rtrim(o.surname)) end  = :surname"
         + " and case when o.idno is null then '' else ltrim(rtrim(o.idno)) end = :idno"
         + " and case when o.mobilePhone is null then '' else ltrim(rtrim(o.mobilePhone)) end = :mobilePhone";*/
        String sql = "update marketing_temp_customer set list_status = 'exist', customer_id = c.id"
                + " from customer c"
                + " inner join marketing_temp_customer m on ((case when m.name is null then '' else LTRIM(RTRIM(m.name)) end) = (case when m.name is null then '' else LTRIM(RTRIM(c.name)) end) and (case when c.surname is null then '' else LTRIM(RTRIM(c.surname)) end) = (case when m.surname is null then '' else LTRIM(RTRIM(m.surname)) end))"
                + " where m.session_id = :sessionId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("sessionId", sessionId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        /*
         q.setParameter("name", name);
         q.setParameter("surname", surname);
         q.setParameter("idno", idno);
         q.setParameter("mobilePhone", mobilePhone);
         Integer id = 0;
         try{
         id = ((List<Integer>) q.getResultList()).get(0);
         }catch(Exception e){
         //log.error(e);
         id = 0;
         }                            
         if(id == 0){
         mtc.setListStatus("new");
         }else{
         mtc.setListStatus("exist");
         mtc.setCustomer(new Customer(id));
         }
         em.merge(mtc);
         */
        //}
    }

    public void exportCalltable(Integer marketingID){
        String sql = "{call export_call_table(:marketingID)}";

        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingID", marketingID);
        try{
            q.getResultList();
        }catch(Exception e){
            log.error(e);
        }
    }

    public int checkMarketingCode(String code, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from Marketing as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from Marketing as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Integer countTotalRecords(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where 1=1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countDuplicateWithin(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.dupWithin = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countDuplicateYesSale(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.dupYes = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countDuplicateCustomer(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.dup = 1 and list_status = 'skip'";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countMergeCustomer(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.dup = 1 and list_status = 'merge'";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countOpOut(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.opout = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countWrongPhoneNo(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.badphone = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countLoaded(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where"
                + " o.dupWithin = 0 and o.dupYes = 0 and o.opout = 0 and o.blacklist = 0 and o.badphone = 0 and o.badformat = 0 and o.listStatus <> 'skip' and o.dnc = 0 and o.mib = 0";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countBlacklist(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.blacklist = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }

    public Integer countBadFormat(String mode, String sessionId, Marketing marketing) {
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.badformat = 1";
        if (mode.equals("add")) {
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        } else if (marketing.getId() != null) {
            sql += " and o.marketingId = :marketingId";
        }

        Query q = em.createQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        } else if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }

        return count;
    }
    
    public Integer countDnc(String mode, String sessionId, Marketing marketing){
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.dnc = 1";
        if(mode.equals("add")){
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        }else if(marketing.getId() != null){
            sql += " and o.marketingId = :marketingId";
        }
        
        Query q = em.createQuery(sql);
        
        if(mode.equals("add")){
            q.setParameter("sessionId", sessionId);
        }else if(marketing.getId() != null){
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }
        
        return count;
    }
    
    public Integer countMib(String mode, String sessionId, Marketing marketing){
        Integer count = 0;
        String sql = "select count(o) from MarketingTempCustomer as o where o.mib = 1";
        if(mode.equals("add")){
            sql += " and o.sessionId = :sessionId and o.marketingId is null";
        }else if(marketing.getId() != null){
            sql += " and o.marketingId = :marketingId";
        }
        
        Query q = em.createQuery(sql);
        
        if(mode.equals("add")){
            q.setParameter("sessionId", sessionId);
        }else if(marketing.getId() != null){
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            log.error(e);
        }
        
        return count;
    }
    
    public List<String[]> getTotalRecordsList(String mode, String sessionId, String strQuery, Marketing marketing){
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where 1=1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }
        
        Query q = em.createNativeQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }
        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getDuplicateWithinList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where dup_within = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getDuplicateYesSaleList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where dup_yes = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null and o.dupYes = 1";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getDuplicateCustomerList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where dup = 1 and list_status = 'skip'";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null and o.dup = 1";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getOpOutList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where opout = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null and o.opout = 1";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getBlackList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where blacklist = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null and o.blacklist = 1";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getWrongPhoneNoList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where badphone = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null and o.badphone = 1";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getWrongFormatList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where badformat = 1";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }

    public List<String[]> getMergeList(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where dup = 1 and list_status = 'merge' ";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }
    
    public List<String[]> getDncList(String mode, String sessionId, String strQuery, Marketing marketing){
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where isnull(dnc,0) = 1";
        
        if(mode.equals("add")){
            sql += " and session_id = :sessionId";
        }
        
        if(marketing.getId() != null){
            sql += " and marketing_id = :marketingId";
        }else if(marketing.getId() == null){
            sql += " and marketing_id is null";
        }
        
        Query q = em.createNativeQuery(sql);
        
        if(mode.equals("add")){
            q.setParameter("sessionId", sessionId);
        }
        
        if(marketing.getId() != null){
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }
        
        return list;
    }
    
    public List<String[]> getMibList(String mode, String sessionId, String strQuery, Marketing marketing){
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where isnull(mib,0) = 1";
        
        if(mode.equals("add")){
            sql += " and session_id = :sessionId";
        }
        
        if(marketing.getId() != null){
            sql += " and marketing_id = :marketingId";
        }else if(marketing.getId() == null){
            sql += " and marketing_id is null";
        }
        
        Query q = em.createNativeQuery(sql);
        
        if(mode.equals("add")){
            q.setParameter("sessionId", sessionId);
        }
        
        if(marketing.getId() != null){
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }
        
        return list;
    }

    public List<String[]> getLoadedList1(String mode, String sessionId, String strQuery, Marketing marketing) {
        List<String[]> list = null;
        String sql = "select"
                + strQuery
                + " from marketing_temp_customer where"
                + " dup_within = 0 and dup_yes = 0 and opout = 0 and blacklist = 0 and badphone = 0 and badformat = 0 and list_status <> 'skip' and dnc = 0 and mib = 0";

        if (mode.equals("add")) {
            sql += " and session_id = :sessionId";
        }

        if (marketing.getId() != null) {
            sql += " and marketing_id = :marketingId";
        } else if (marketing.getId() == null) {
            sql += " and marketing_id is null";
        }

        Query q = em.createNativeQuery(sql);

//        List<MarketingTempCustomer> list = null;
//        String sql = "select object(o) from MarketingTempCustomer as o where o.sessionId = :sessionId and o.marketingId is null"
//                + " and o.dupWithin = 0 and o.dupYes = 0 and o.dup = 0 and o.opout = 0 and o.blacklist = 0 and badphone = 0";
//        
//        Query q = em.createQuery(sql);
        if (mode.equals("add")) {
            q.setParameter("sessionId", sessionId);
        }

        if (marketing.getId() != null) {
            q.setParameter("marketingId", marketing.getId());
        }
        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }

        return list;
    }
    
    public List<String[]> getLoadHositoryList(Integer marketingId){
        List<String[]> list = null;
        String sql = "SELECT" +
                    "   ml.marketing_id," +
                    "   ml.session_id," +
                    "   CONVERT(varchar,start_import_date,103) + ' ' + CONVERT(varchar,start_import_date,108) AS import_date," +
                    "   COUNT(*) AS total_record," +
                    "   SUM(CONVERT(int,ISNULL(dup_within,0))) AS dup_within," +
                    "   SUM(CONVERT(int,ISNULL(dup_yes,0))) AS dup_yes," +
                    "   SUM(CASE WHEN CONVERT(int,ISNULL(dup,0)) = 1 AND mtc.list_status = 'skip' THEN 1 ELSE 0 END) AS dup," +
                    "   SUM(CONVERT(int,ISNULL(opout,0))) AS opout," +
                    "   SUM(CONVERT(int,ISNULL(blacklist,0))) AS blacklist," +
                    "   SUM(CONVERT(int,ISNULL(badphone,0))) AS badphone," +
                    "   SUM(CONVERT(int,ISNULL(dnc,0))) AS dnc," +
                    "   SUM(CONVERT(int,ISNULL(mib,0))) AS mib," +
                    "   SUM(CONVERT(int,ISNULL(badformat,0))) AS badformat," +
                    "   COUNT(*) - SUM(CONVERT(int,ISNULL(dup_within,0))) - SUM(CONVERT(int,ISNULL(dup_yes,0))) - SUM(CONVERT(int,ISNULL(dup,0))) - SUM(CONVERT(int,ISNULL(opout,0))) - SUM(CONVERT(int,ISNULL(blacklist,0))) - SUM(CONVERT(int,ISNULL(badphone,0))) - SUM(CONVERT(int,ISNULL(dnc,0))) - SUM(CONVERT(int,ISNULL(mib,0))) - SUM(CONVERT(int,ISNULL(badformat,0))) AS total_loaded" +
                    " FROM marketing_temp_customer mtc" +
                    " INNER JOIN marketing_lot ml ON mtc.marketing_id = ml.marketing_id AND mtc.marketing_id = :marketingId AND mtc.session_id = ml.session_id" +
                    " GROUP BY ml.marketing_id, ml.session_id, ml.start_import_date" +
                    " ORDER BY ml.start_import_date DESC";
        
        Query q = em.createNativeQuery(sql);   
        q.setParameter("marketingId", marketingId);

        try {
            list = q.getResultList();
        } catch (Exception e) {
            log.error(e);
        }
        
        return list;
    }
    
    public MarketingCustomer findByCustomerId(Integer customerId) {
        MarketingCustomer m = null;

        try {
            String sql = "select object(mc) from MarketingCustomer as mc"
                    + " where mc.marketingCustomerPK.customerId = :customerId";
            Query q = em.createQuery(sql);
            q.setParameter("customerId", customerId);

            m = (MarketingCustomer) q.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;

    }

    private String listToString(List<Integer> l) {
        StringBuilder sb = new StringBuilder();
        for (int id : l) {
            sb.append(id);
            sb.append(',');
        }
        return sb.toString();
    }
    
    public String createAdvSearchSQLCmd(List<CriteriaListValue> selectedAdvanceCriteria) {
        String strWhere = " ";
        if (selectedAdvanceCriteria != null) {
            for (CriteriaListValue obj : selectedAdvanceCriteria) {
                if (obj.isSelected()) { 
                    String fieldName = "";
                    fieldName = StringUtils.substring(obj.getMappingField(), (obj.getMappingField().lastIndexOf(".")) + 1, obj.getMappingField().length());

                    if(obj.getType().equals("Date")) {
                        try {
                            SimpleDateFormat outputFormat = new SimpleDateFormat(obj.getPattern());    
                            String outFromDate = outputFormat.format(obj.getFromValueDate());
                            
                            if(obj.getCriteria().equals("on")) {
                                strWhere += "and " + fieldName + " = '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("before")) {
                                strWhere += "and " + fieldName + " < '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("after")) {
                                strWhere += "and " + fieldName + " > '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("between")) {                                
                                String outToDate = outputFormat.format(obj.getToValueDate());                                
                                strWhere += "and " + fieldName + " between '" + outFromDate + "' and '" + outToDate + "' "; 
                            } else if(obj.getCriteria().equals("notEqual")) {
                                strWhere += "and " + fieldName + " <> '" + outFromDate + "' ";
                            }
                        } catch (Exception exn) {
                            exn.getMessage();
                        }
                    } else if (obj.getType().equals("Number")) {
                        if(!obj.getCriteria().isEmpty()) {
                            try {
                                Double fromValue = Double.parseDouble(obj.getFromValue());
                                if(obj.getCriteria().equals("between")) {
                                    Double toValue = Double.parseDouble(obj.getToValue());
                                     strWhere += "and CAST("+fieldName+" as DECIMAL(9,4)) between " + fromValue + " and " + toValue + " ";
                                     //and flexfield8 BETWEEN 10000 AND 25000
                                } else {
                                    strWhere += "and CAST("+fieldName+" as DECIMAL(9,4)) " + obj.getCriteria().trim() + " " + fromValue +" ";
                                }                              
                            } catch (Exception exn) {
                                exn.getMessage();
                            }                            
                        }
                    } else if (obj.getType().equals("String")) {
                        strWhere += "and " + fieldName + " like '%"+obj.getKeyword().trim()+"%' ";
                    }          
                }
            }
        }
        return strWhere;
    }

    public List<Object[]> criteriaSegmentationMarketing(Marketing marketing, List<Integer> selectedMarketingList, boolean selectContactResult,
            boolean selectGender, boolean selectAge, boolean selectHomePhone, boolean selectOfficePhone, boolean selectMobilePhone,
            boolean selectSaleResult, boolean selectYesSale, boolean selectNoSale, List<Integer> selectedContactResultList, String gender,
            Integer fromAge, Integer toAge, String homePhonePrefix, String officePhonePrefix, String mobilePhonePrefix, 
            List<Integer> selectedNoSaleResultList, List<CriteriaListValue> selectedAdvanceCriteria, String chkYesInProduct, List<Integer> selectedProduct) {

        String marketingStr = this.listToString(selectedMarketingList);
        String contactResultStr = this.listToString(selectedContactResultList);
        String noSaleStr = this.listToString(selectedNoSaleResultList);        
        String productStr = this.listToString(selectedProduct);
        if (marketingStr.length() > 0) {
            marketingStr = marketingStr.substring(0, marketingStr.length() - 1);
        }
        if (productStr.length() > 0) {
            productStr = productStr.substring(0, productStr.length() - 1);
        }

        //Find marketing customer criteria
        String select = "select distinct (cus.id) from marketing_customer mc "
                    + "inner join customer cus on cus.id = mc.customer_id ";     
        
        String where = "where mc.marketing_id in (" + marketingStr + ") and (cus.op_out is null or (cus.op_out = 1 and cus.op_out_date is not null)) ";
        
        if (marketing.getPeriodBlacklist() == -1) {
            where += "and (cus.op_out is null or cus.op_out = 0) ";
        } else if (marketing.getPeriodBlacklist() != null && marketing.getPeriodBlacklist() != 0) {
            where += "and ((cus.op_out is null or cus.op_out = 0) or (cus.op_out = 1 and (DATEDIFF(DAY, cus.op_out_date, GETDATE())) > " + marketing.getPeriodBlacklist() +")) ";
        }  
        
        if (selectGender) {
            if (gender != null && !gender.isEmpty()) {
                where += "and cus.gender like '" + gender.trim() + "%' ";
            }
        }
        if (selectAge) {
            where += "and (DATEDIFF(Year,cus.dob,getdate()) >= " + fromAge + " and DATEDIFF(Year,cus.dob,getdate()) <= " + toAge + " )";
        }
        if (selectHomePhone) {
            if (homePhonePrefix != null && !homePhonePrefix.isEmpty()) {
                if(homePhonePrefix.equals("notspecific")) {
                    where += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
                } else if (homePhonePrefix.equals("upc")) {
                    where += "and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '032%' "
                            + "or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '037%' or cus.home_phone like '038%' "
                            + "or cus.home_phone like '039%' or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' "
                            + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
                } else {
                    where += "and cus.home_phone like '" + homePhonePrefix.trim() + "%' ";
                }
            }
        }
        if (selectOfficePhone) {
            if (officePhonePrefix != null && !officePhonePrefix.isEmpty()) {
                if(officePhonePrefix.equals("notspecific")) {
                    where += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
                } else if (officePhonePrefix.equals("upc")) {
                    where += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '032%' "
                            + "or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '037%' or cus.office_phone like '038%' "
                            + "or cus.office_phone like '039%' or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' "
                            + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone  like '077%') ";
                } else {
                    where += "and cus.office_phone like '" + officePhonePrefix.trim() + "%' ";
                }
            }
        }
        if (selectMobilePhone) {
            if(mobilePhonePrefix.equals("notspecific")) {
                where += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
            } else {
                where += "and cus.mobile_phone like '" + mobilePhonePrefix.trim() + "%' ";
            }            
//            if (mobilePhonePrefix != null && !mobilePhonePrefix.isEmpty()) {
//                where += "and cus.mobile_phone like '" + mobilePhonePrefix.trim() + "%' ";
//            }
        }
        if (selectContactResult) {
            where += "and cus.id in (select distinct(ad.customer_id) "
                    + "from assignment_detail ad "
                    + "join assignment a on ad.assignment_id = a.id "
                    + "where ad.status like 'closed' "
                    + "and a.marketing_id in (" + marketingStr + ") ";
            if (contactResultStr.length() > 0) {
                contactResultStr = contactResultStr.substring(0, contactResultStr.length() - 1);
                where += "and ad.contact_result_id in (" + contactResultStr + ") ";
            }
            where += ") ";
        }
        
        if (selectSaleResult) {
//            where += "and cus.id in (select distinct (po.customer_id) from purchase_order po where (DATEDIFF(DAY, po.create_date, GETDATE()) + 1) > 90 ";
            where += "and cus.id in (select distinct (po.customer_id) from purchase_order po " 
                    + "join assignment_detail ad on po.assignment_detail_id = ad.id " 
                    + "join assignment a on a.id = ad.assignment_id " 
                    + "join purchase_order_detail pod on pod.purchase_order_id = po.id " 
                    + "where a.marketing_id in (" + marketingStr + ") ";
            
            if(marketing.getPeriodYessale() != null && marketing.getPeriodYessale() != 0) {
                where += "and (DATEDIFF(DAY, po.create_date, GETDATE())) > " + marketing.getPeriodYessale() + " ";
            } 
                        
            if (selectYesSale && selectNoSale) {
                if (noSaleStr.length() > 0) {
                    noSaleStr = noSaleStr.substring(0, noSaleStr.length() - 1);
                    where += "and po.nosale_reason_id in (" + noSaleStr + ") ";
                }
            } else {
                if (selectYesSale) {
                    where += "and po.sale_result like 'Y' ";
                }
                if (selectNoSale) {
                    where += "and po.sale_result like 'N' ";
                    if (noSaleStr.length() > 0) {
                        noSaleStr = noSaleStr.substring(0, noSaleStr.length() - 1);
                        where += "and po.nosale_reason_id in (" + noSaleStr + ") ";
                    }
                }
            }
            
            if (selectYesSale && productStr.length() > 0 && chkYesInProduct.equals("include")) {
                where += "and pod.product_id in (" + productStr + ") ";               
            }
            
            where += ") ";
        }
        
        //CREATE ADVANCE CRITERIA
        String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
        
        Query q = em.createNativeQuery(select + where + advCriteria);
        System.out.println("Criteria Segment "+select + where + advCriteria);
        List<Object[]> customerList = q.getResultList();
        return customerList;
    }

    
    public List<Marketing> findMarketingByFileTemplateAndProspect(Integer sponsorId, Integer templateId) {
        Query q = em.createQuery("select object(o) from Marketing as o where o.enable = true and o.prospectlistSponsor.id = ?1 "
                + "and o.fileTemplate.id = ?2 order by o.createDate desc");
        
        q.setParameter(1, sponsorId);
        q.setParameter(2, templateId);
        
        return q.getResultList();
    }
    
    public void createSegmentCustomer(String custStr) {
        Date now = new Date();
        String sql = "insert into customer "
                + "(reference_no,initial,name,surname,customer_type,privilege,idno,gender,dob,weight,height,nationality,occupation,"
                + "home_phone,home_ext,office_phone,office_ext,mobile_phone,fax,fax_ext,email,current_fullname,current_address_line1,"
                + "current_address_line2,current_address_line3,current_address_line4,current_address_line5,current_address_line6,"
                + "current_address_line7,current_address_line8,current_district_name,current_province_name,current_postal_code,home_fullname,"
                + "home_address_line1,home_address_line2,home_address_line3,home_address_line4,home_address_line5,home_address_line6,"
                + "home_address_line7,home_address_line8,home_district_name,home_province_name,home_postal_code,billing_fullname,"
                + "billing_address_line1,billing_address_line2,billing_address_line3,billing_address_line4,billing_address_line5,"
                + "billing_address_line6,billing_address_line7,billing_address_line8,billing_district_name,billing_province_name,"
                + "billing_postal_code,shipping_fullname,shipping_address_line1,shipping_address_line2,shipping_address_line3,shipping_address_line4,"
                + "shipping_address_line5,shipping_address_line6,shipping_address_line7,shipping_address_line8,shipping_district_name,"
                + "shipping_province_name,shipping_postal_code,contact_phone1,contact_phone2,contact_phone3,contact_phone4,contact_phone5,"
                + "remark,flexfield1,flexfield2,flexfield3,flexfield4,flexfield5,flexfield6,flexfield7,flexfield8,flexfield9,flexfield10,"
                + "flexfield11,flexfield12,flexfield13,flexfield14,flexfield15,flexfield16,flexfield17,flexfield18,flexfield19,flexfield20,"
                + "flexfield21,flexfield22,flexfield23,flexfield24,flexfield25,flexfield26,flexfield27,flexfield28,flexfield29,flexfield30,"
                + "flexfield31,flexfield32,flexfield33,flexfield34,flexfield35,flexfield36,flexfield37,flexfield38,flexfield39,flexfield40,"
                + "flexfield41,flexfield42,flexfield43,flexfield44,flexfield45,flexfield46,flexfield47,flexfield48,flexfield49,flexfield50,"
                + "flexfield51,flexfield52,flexfield53,flexfield54,flexfield55,flexfield56,flexfield57,flexfield58,flexfield59,flexfield60,"
                + "flexfield61,flexfield62,flexfield63,flexfield64,flexfield65,flexfield66,flexfield67,flexfield68,flexfield69,flexfield70," 
                + "flexfield71,flexfield72,flexfield73,flexfield74,flexfield75,flexfield76,flexfield77,flexfield78,flexfield79,flexfield80,"
                + "flexfield81,flexfield82,flexfield83,flexfield84,flexfield85,flexfield86,flexfield87,flexfield88,flexfield89,flexfield90,"
                + "flexfield91,flexfield92,flexfield93,flexfield94,flexfield95,flexfield96,flexfield97,flexfield98,flexfield99,flexfield100," 
                + "car_brand,car_model,car_year,car_character_group,car_number,car_province,create_date,create_by,update_date,update_by,parent_id)"
                + "select "
                + "cus.reference_no,cus.initial,cus.name,cus.surname,cus.customer_type,cus.privilege,cus.idno,cus.gender,cus.dob,cus.weight,"
                + "cus.height,cus.nationality,cus.occupation,cus.home_phone,cus.home_ext,cus.office_phone,cus.office_ext,cus.mobile_phone,"
                + "cus.fax,cus.fax_ext,cus.email,cus.current_fullname,cus.current_address_line1,cus.current_address_line2,cus.current_address_line3,"
                + "cus.current_address_line4,cus.current_address_line5,cus.current_address_line6,cus.current_address_line7,cus.current_address_line8,"
                + "cus.current_district_name,cus.current_province_name,cus.current_postal_code,cus.home_fullname,cus.home_address_line1,"
                + "cus.home_address_line2,cus.home_address_line3,cus.home_address_line4,cus.home_address_line5,cus.home_address_line6,"
                + "cus.home_address_line7,cus.home_address_line8,cus.home_district_name,cus.home_province_name,cus.home_postal_code,"
                + "cus.billing_fullname,cus.billing_address_line1,cus.billing_address_line2,cus.billing_address_line3,cus.billing_address_line4,"
                + "cus.billing_address_line5,cus.billing_address_line6,cus.billing_address_line7,cus.billing_address_line8,cus.billing_district_name,"
                + "cus.billing_province_name,cus.billing_postal_code,cus.shipping_fullname,cus.shipping_address_line1,cus.shipping_address_line2,"
                + "cus.shipping_address_line3,cus.shipping_address_line4,cus.shipping_address_line5,cus.shipping_address_line6,cus.shipping_address_line7,"
                + "cus.shipping_address_line8,cus.shipping_district_name,cus.shipping_province_name,cus.shipping_postal_code,cus.contact_phone1,"
                + "cus.contact_phone2,cus.contact_phone3,cus.contact_phone4,cus.contact_phone5,cus.remark,cus.flexfield1,cus.flexfield2,cus.flexfield3,"
                + "cus.flexfield4,cus.flexfield5,cus.flexfield6,cus.flexfield7,cus.flexfield8,cus.flexfield9,cus.flexfield10,cus.flexfield11,"
                + "cus.flexfield12,cus.flexfield13,cus.flexfield14,cus.flexfield15,cus.flexfield16,cus.flexfield17,cus.flexfield18,cus.flexfield19,"
                + "cus.flexfield20,cus.flexfield21,cus.flexfield22,cus.flexfield23,cus.flexfield24,cus.flexfield25,cus.flexfield26,cus.flexfield27,"
                + "cus.flexfield28,cus.flexfield29,cus.flexfield30,cus.flexfield31,cus.flexfield32,cus.flexfield33,cus.flexfield34,cus.flexfield35,"
                + "cus.flexfield36,cus.flexfield37,cus.flexfield38,cus.flexfield39,cus.flexfield40,cus.flexfield41,cus.flexfield42,cus.flexfield43,"
                + "cus.flexfield44,cus.flexfield45,cus.flexfield46,cus.flexfield47,cus.flexfield48,cus.flexfield49,cus.flexfield50,cus.flexfield51,"
                + "cus.flexfield52,cus.flexfield53,cus.flexfield54,cus.flexfield55,cus.flexfield56,cus.flexfield57,cus.flexfield58,cus.flexfield59,"
                + "cus.flexfield60,cus.flexfield61,cus.flexfield62,cus.flexfield63,cus.flexfield64,cus.flexfield65,cus.flexfield66,cus.flexfield67,"
                + "cus.flexfield68,cus.flexfield69,cus.flexfield70,cus.flexfield71,cus.flexfield72,cus.flexfield73,cus.flexfield74,cus.flexfield75,"
                + "cus.flexfield76,cus.flexfield77,cus.flexfield78,cus.flexfield79,cus.flexfield80,cus.flexfield81,cus.flexfield82,cus.flexfield83,"
                + "cus.flexfield84,cus.flexfield85,cus.flexfield86,cus.flexfield87,cus.flexfield88,cus.flexfield89,cus.flexfield90,cus.flexfield91,"
                + "cus.flexfield92,cus.flexfield93,cus.flexfield94,cus.flexfield95,cus.flexfield96,cus.flexfield97,cus.flexfield98,cus.flexfield99,"
                + "cus.flexfield100,cus.car_brand,cus.car_model,cus.car_year,cus.car_character_group,cus.car_number,cus.car_province,:createDate,:createBy,:createDate,:createBy, ISNULL(parent_id, id) "
                + "from customer cus "
                + "where cus.id in ("+custStr+")";

        Query q = em.createNativeQuery(sql);
        q.setParameter("createDate", now);
        q.setParameter("createBy", JSFUtil.getUserSession().getUsers().getLoginName());
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createSegmentMarketingCustomer(Integer marketingId, String custStr) {
        Date now = new Date();
        String sql = "insert into marketing_customer "
                + "(customer_id, marketing_id, new_list, create_date, assign, "
                + "list_value_001, list_value_002, list_value_003, list_value_004, list_value_005, list_value_006, list_value_007, list_value_008, list_value_009, list_value_010, "
                + "list_value_011, list_value_012, list_value_013, list_value_014, list_value_015, list_value_016, list_value_017, list_value_018, list_value_019, list_value_020, "
                + "list_value_021, list_value_022, list_value_023, list_value_024, list_value_025, list_value_026, list_value_027, list_value_028, list_value_029, list_value_030, "
                + "list_value_031, list_value_032, list_value_033, list_value_034, list_value_035, list_value_036, list_value_037, list_value_038, list_value_039, list_value_040, "
                + "list_value_041, list_value_042, list_value_043, list_value_044, list_value_045, list_value_046, list_value_047, list_value_048, list_value_049, list_value_050, "
                + "list_value_051, list_value_052, list_value_053, list_value_054, list_value_055, list_value_056, list_value_057, list_value_058, list_value_059, list_value_060, "
                + "list_value_061, list_value_062, list_value_063, list_value_064, list_value_065, list_value_066, list_value_067, list_value_068, list_value_069, list_value_070, "
                + "list_value_071, list_value_072, list_value_073, list_value_074, list_value_075, list_value_076, list_value_077, list_value_078, list_value_079, list_value_080, "
                + "list_value_081, list_value_082, list_value_083, list_value_084, list_value_085, list_value_086, list_value_087, list_value_088, list_value_089, list_value_090, "
                + "list_value_091, list_value_092, list_value_093, list_value_094, list_value_095, list_value_096, list_value_097, list_value_098, list_value_099, list_value_100) "
                + "select distinct newCust.id, :marketingId, 1, :createDate, 0, "
                + "mc.list_value_001, mc.list_value_002, mc.list_value_003, mc.list_value_004, mc.list_value_005, mc.list_value_006, mc.list_value_007, mc.list_value_008, mc.list_value_009, mc.list_value_010, "
                + "mc.list_value_011, mc.list_value_012, mc.list_value_013, mc.list_value_014, mc.list_value_015, mc.list_value_016, mc.list_value_017, mc.list_value_018, mc.list_value_019, mc.list_value_020, "
                + "mc.list_value_021, mc.list_value_022, mc.list_value_023, mc.list_value_024, mc.list_value_025, mc.list_value_026, mc.list_value_027, mc.list_value_028, mc.list_value_029, mc.list_value_030, "
                + "mc.list_value_031, mc.list_value_032, mc.list_value_033, mc.list_value_034, mc.list_value_035, mc.list_value_036, mc.list_value_037, mc.list_value_038, mc.list_value_039, mc.list_value_040, "
                + "mc.list_value_041, mc.list_value_042, mc.list_value_043, mc.list_value_044, mc.list_value_045, mc.list_value_046, mc.list_value_047, mc.list_value_048, mc.list_value_049, mc.list_value_050, "
                + "mc.list_value_051, mc.list_value_052, mc.list_value_053, mc.list_value_054, mc.list_value_055, mc.list_value_056, mc.list_value_057, mc.list_value_058, mc.list_value_059, mc.list_value_060, "
                + "mc.list_value_061, mc.list_value_062, mc.list_value_063, mc.list_value_064, mc.list_value_065, mc.list_value_066, mc.list_value_067, mc.list_value_068, mc.list_value_069, mc.list_value_070, "
                + "mc.list_value_071, mc.list_value_072, mc.list_value_073, mc.list_value_074, mc.list_value_075, mc.list_value_076, mc.list_value_077, mc.list_value_078, mc.list_value_079, mc.list_value_080, "
                + "mc.list_value_081, mc.list_value_082, mc.list_value_083, mc.list_value_084, mc.list_value_085, mc.list_value_086, mc.list_value_087, mc.list_value_088, mc.list_value_089, mc.list_value_090, "
                + "mc.list_value_091, mc.list_value_092, mc.list_value_093, mc.list_value_094, mc.list_value_095, mc.list_value_096, mc.list_value_097, mc.list_value_098, mc.list_value_099, mc.list_value_100 "

                //+ "from marketing_customer mc " 
                //+ "j join customer oldCus on mc.customer_id = oldCus.id " 
                //+ "join customer newCust on newCust.parent_id = ISNULL(oldCus.parent_id, oldCus.id) " 
                //+ "where newCust.parent_id is not null and oldCus.id in ("+custStr+") "
                //+ "and newCust.id not in ("+custStr+")";        
        
                + "from marketing_customer mc "
                + "join customer oldCus on mc.customer_id = oldCus.id " 
                + "inner join (select max(id) as id, LTRIM(RTRIM(name)) as name, LTRIM(RTRIM(surname)) as surname from customer cus group by LTRIM(RTRIM(name)), LTRIM(RTRIM(surname))) newCust "
                + "on (case when (oldCus.surname is not null and oldCus.surname <> '') then (LTRIM(RTRIM((oldCus.name))) + ' ' + LTRIM(RTRIM((oldCus.surname)))) else LTRIM(RTRIM((oldCus.name))) end) = (case when (newCust.surname is not null and newCust.surname <> '') then (LTRIM(RTRIM((newCust.name))) + ' ' + LTRIM(RTRIM((newCust.surname)))) else LTRIM(RTRIM((newCust.name))) end) "
                + "where oldCus.id in ("+custStr+") ";

        
        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.setParameter("createDate", now);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createSegmentationData(Marketing marketing, List<Object[]> customerList) {
        try {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();

            // 1. CREATE NEW MARKETING
            marketing.setId(null);
            marketing.setTotalTempRecord(customerList.size());
            marketing.setTotalRecord(customerList.size());
            marketing.setTotalAssigned(0);
            marketing.setAssignedNo(0);        
            marketing.setDatasource("file");        
            marketing.setDupType("never");        
            marketing.setPeriodCustomer(0);
            marketing.setCheckFormatTelNo(Boolean.FALSE);
            marketing.setPeriodYessale(0);
            marketing.setIsLinkProduct(Boolean.FALSE);        
            marketing.setCreateBy(username);
            marketing.setCreateDate(now);
            marketing.setUpdateBy(username);
            marketing.setUpdateDate(now);        
            em.persist(marketing);

            // CREATE STRING OF CUSTOMER ID
            StringBuilder sb = new StringBuilder();
            Iterator iCusStr = customerList.iterator();
            while (iCusStr.hasNext()) {
                sb.append(iCusStr.next());
                sb.append(',');
            }
            String custStr = sb.toString();
            if (custStr.length() > 0) {
                custStr = custStr.substring(0, custStr.length() - 1);
            }
            
            // 2. CREATE NEW CUSTOMER
            createSegmentCustomer(custStr);

            // 3. CREATE NEW MARKETING CUSTOMER
            createSegmentMarketingCustomer(marketing.getId(), custStr);
            
        } catch (Exception e) {
            log.error(e);
        }
        
    }
    
    public synchronized void purge(Integer marketingId) throws Exception {
        Query q = null;
        String sql = "";

        System.out.println("BEGIN PURGE LIST MARKETING ID: " + marketingId);
/*
        sql = "update assignment_detail set transfer_detail_id = null "
                + "from assignment_detail ad "
                + "join assignment a on a.id = ad.assignment_id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish update assignment_detail");

        sql = "delete assignment_supervisor_detail "
                + "from assignment_supervisor_detail asd "
                + "join assignment_supervisor asp on asd.assignment_supervisor_id = asp.id "
                + "join assignment a on asp.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_supervisor_detail");

        sql = "delete assignment_supervisor "
                + "from assignment_supervisor asp  "
                + "join assignment a on asp.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_supervisor");

        sql = "delete assignment_pooling_daily "
                + "from assignment_pooling_daily apd "
                + "join assignment_pooling ap on apd.assignment_pooling_id = ap.id "
                + "join assignment a on ap.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_pooling_daily");

        sql = "delete assignment_pooling "
                + "from assignment_pooling ap "
                + "join assignment a on ap.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_pooling");

        sql = "delete purchase_order_approval_exclusion "
                + "from purchase_order_approval_exclusion ex "
                + "join purchase_order_approval_log polog on ex.purchase_order_approval_log_id = polog.id "
                + "join purchase_order po on polog.purchase_order_id = po.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order_approval_exclusion");

        sql = "delete purchase_order_approval_log "
                + "from purchase_order_approval_log polog "
                + "join purchase_order po on polog.purchase_order_id = po.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order_approval_log");

        sql = "delete purchase_order_register "
                + "from purchase_order_register reg "
                + "join purchase_order_detail pod on reg.purchase_order_detail_id = pod.id "
                + "join purchase_order po on pod.purchase_order_id = po.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order_register");

        sql = "delete purchase_order_declaration "
                + "from purchase_order_declaration dec "
                + "join purchase_order_detail pod on dec.purchase_order_detail_id = pod.id "
                + "join purchase_order po on pod.purchase_order_id = po.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order_declaration");

        sql = "delete purchase_order_detail "
                + "from purchase_order_detail pod "
                + "join purchase_order po on pod.purchase_order_id = po.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order_detail");

        sql = "delete contact_history_purchase_order "
                + "from contact_history_purchase_order chpo "
                + "inner join purchase_order po on po.id = chpo.purchase_order_id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_purchase_order");

        sql = "delete contact_history_sale_result "
                + "from contact_history_sale_result chsr "
                + "inner join purchase_order po on po.id = chsr.purchase_order_id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_sale_result");

        sql = "delete contact_history_sale_result "
                + "from contact_history_sale_result chsr "
                + "join contact_history_assignment cha on cha.contact_history_id = chsr.contact_history_id "
                + "join assignment_detail ad on cha.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_sale_result");

        sql = "delete qa_trans_detail "
                + "from qa_trans_detail qtd "
                + "join qa_trans qa on qa.id = qtd.qa_trans_id "
                + "join purchase_order po on po.approval_qa_trans_id = qa.id "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete qa_trans_detail");

        sql = "delete purchase_order "
                + "from purchase_order po "
                + "join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete purchase_order");

        sql = "delete qa_trans_detail "
                + "from qa_trans_detail qtd "
                + "join qa_trans qa on qa.id = qtd.qa_trans_id "
                + "where qa.id not in (select approval_qa_trans_id  "
                + "from purchase_order where approval_qa_trans_id is not null)";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete qa_trans_detail");

        sql = "delete qa_trans "
                + "from qa_trans "
                + "where id not in (select approval_qa_trans_id  "
                + "from purchase_order where approval_qa_trans_id is not null)";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete qa_trans");

        sql = "delete campaign_customer "
                + "from campaign_customer cc "
                + "join marketing_customer mc on mc.customer_id = cc.customer_id "
                + "join assignment_detail ad on ad.id = cc.assignment_detail_id  "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId and mc.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete campaign_customer");

        sql = "update customer "
                + "set parent_id = case when c.id = (select min(id) from customer where parent_id = c.parent_id) then null else (select min(id) from customer where parent_id = c.parent_id) end "
                + "from customer c "
                + "where c.parent_id in (select customer_id from marketing_customer where marketing_id = :marketingId)";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish update parent_id customer");

        sql = "delete marketing_temp_customer "
                + "from marketing_temp_customer  "
                + "where marketing_id is null or marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete marketing_temp_customer");

        sql = "delete marketing_customer "
                + "from marketing_customer where marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete marketing_customer");

        sql = "delete contact_history_knowledge "
                + "from contact_history_knowledge chk "
                + "join contact_history_assignment cha on cha.contact_history_id = chk.contact_history_id "
                + "join assignment_detail ad on ad.id = cha.assignment_detail_id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_knowledge");

        sql = "delete contact_history_purchase_order "
                + "from contact_history_purchase_order chpo "
                + "join contact_history_assignment cha on chpo.contact_history_id = cha.contact_history_id "
                + "join assignment_detail ad on cha.assignment_detail_id = ad.id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_purchase_order");

        sql = "delete contact_history_assignment "
                + "from contact_history_assignment cha "
                + "join assignment_detail ad on ad.id = cha.assignment_detail_id "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete contact_history_assignment");

        sql = "delete contact_history_sale_result "
                + "from contact_history_sale_result  "
                + "where contact_history_id in "
                + "(select id "
                + "from contact_history "
                + "where customer_id not in ( "
                + "select customer_id from marketing_customer) and customer_id not in ( "
                + "select cc.customer_id from contact_case cc))";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete contact_history_sale_result");

        sql = "delete contact_history_knowledge " 
                + "where contact_history_id in " 
                + "(select ch.id " 
                + "from contact_history ch "
                + "join customer c on ch.customer_id = c.id "
                + "where c.created_by_cs is null and ch.customer_id not in ( "
                + "select customer_id from marketing_customer) and customer_id not in ( "
                + "select cc.customer_id from contact_case cc))";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete contact_history_knowledge");
        
        sql = "delete contact_history "
                + "from contact_history ch "
                + "join customer c on ch.customer_id = c.id "
                + "where c.created_by_cs is null and ch.customer_id not in ( "
                + "select customer_id from marketing_customer) and customer_id not in ( "
                + "select cc.customer_id from contact_case cc)";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete contact_history");

        sql = "delete assignment_product "
                + "from assignment_product ap "
                + "join assignment a on ap.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_product");

        sql = "delete assignment_detail "
                + "from assignment_detail ad "
                + "join assignment a on ad.assignment_id = a.id "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment_detail");

        sql = "delete assignment "
                + "from assignment a "
                + "where a.marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete assignment");

        sql = "delete assignment_transfer_detail "
                + "from assignment_transfer_detail atd "
                + "join customer c on atd.customer_id = c.id "
                + "where c.created_by_cs is null and atd.customer_id not in ( "
                + "select customer_id from marketing_customer) and atd.customer_id not in ( "
                + "select cc.customer_id from contact_case cc) ";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete assignment_transfer_detail");

        sql = "delete campaign_customer "
                + "from campaign_customer "
                + "where customer_id not in ( "
                + "select customer_id from marketing_customer) and customer_id not in ( "
                + "select cc.customer_id from contact_case cc) ";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete campaign_customer");

        sql = "delete customer "
                + "from customer where created_by_cs is null and id not in ( "
                + "select customer_id from marketing_customer) and id not in ( "
                + "select cc.customer_id from contact_case cc)";
        q = em.createNativeQuery(sql);
        q.executeUpdate();
        System.out.println("finish delete customer");

        sql = "delete report_log "
                + "from report_log "
                + "where marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete report_log");
        
        sql = "delete rpt_sale_performance1 "
                + "from rpt_sale_performance1 "
                + "where marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete rpt_sale_performance1");

        sql = "delete rpt_contact_history "
                + "from rpt_contact_history "
                + "where marketing_id = :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
        System.out.println("finish delete rpt_contact_history");
*/
        sql = "EXEC spMarketingList_PurgeList :marketingId";
        q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.executeUpdate();
            
        System.out.println("COMPLETE PURGE LIST");
    }
    
    public MarketingCustomer findMarketingCustomerByPK(Integer customerId, Integer marketingId) {
        MarketingCustomer m = null;
        try {
            String sql = "select object(mc) from MarketingCustomer as mc"
                    + " where mc.marketingCustomerPK.customerId = :customerId"
                    + " and mc.marketingCustomerPK.marketingId = :marketingId";
            Query q = em.createQuery(sql);
            q.setParameter("customerId", customerId);
            q.setParameter("marketingId", marketingId);

            m = (MarketingCustomer) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;

    }

    public List<Marketing> findMarketingByUserGroup(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " (";
        if (mug != null && mug.size() > 0) {
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ")";
            System.out.println("where : " + whereId);
            result = findMarketingGroupById(whereId);
            if (result == null) {
                result = new ArrayList<Marketing>();
            }
        } else {
            result = new ArrayList<Marketing>();
        }
        return result;
    }

    private List<MarketingUserGroup> findMarketingUserGroupByUserGroup(Integer userGroupId) {
        Query q = em.createQuery("select object(mu) from MarketingUserGroup as mu where mu.marketingUserGroupPK.userGroupId = :userGroupId");
        q.setParameter("userGroupId", userGroupId);
        return q.getResultList();
    }

    private List<Marketing> findMarketingGroupById(String where) {
        String sql = "select object(o) from Marketing as o where o.id in " + where + " and o.enable = true order by o.createDate desc";
        System.out.println("sql : " + sql);
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    public Map<String, Integer> getAvaialableMarketListByIsAdminFlag(int ugId) {
        List<Marketing> marketings = this.findMarketingByUserGroup1(ugId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getAvaialableMarketListFromSupByIsAdminFlag(Campaign campaign, int ugId) {
        List<Marketing> marketings = this.findMarketingByUserGroupBySup(campaign, ugId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    private List<Marketing> findMarketingByUserGroup1(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null) {
            whereId = " and o.id in ( ";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
            result = findAvailableMarketingEntities1(whereId);
            if (result == null) {
                result = new ArrayList<Marketing>();
            }
        } else {
            result = new ArrayList<Marketing>();
        }
        return result;
    }

    private List<Marketing> findAvailableMarketingEntities1(String where) {
        Query q = em.createQuery("select object(o) from Marketing as o where (totalAssigned is null or totalAssigned < totalRecord) and totalRecord > 0 and enable = true and status = true and startDate <= ?1 and endDate >= ?1 " + where + " order by code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    private List<Marketing> findMarketingByUserGroupBySup(Campaign campaign, Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null) {
            whereId = " and o.id in ( ";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
            result = findAvailableMarketingFromSupByIsAdmihnFlag(campaign, whereId);
            if (result == null) {
                result = new ArrayList<Marketing>();
            }
        } else {
            result = new ArrayList<Marketing>();
        }
        return result;
    }

    private List<Marketing> findAvailableMarketingFromSupByIsAdmihnFlag(Campaign campaign, String where) {
        Query q = em.createQuery("select object(o) from Marketing as o where enable = true and status = true and startDate <= ?3 and endDate >= ?3 and o.id IN "
                + "(SELECT DISTINCT a.marketing FROM Assignment AS a , AssignmentSupervisor AS s WHERE a.id = s.assignmentId AND s.userId.id = ?1 and a.campaign = ?2 " + where + " GROUP BY a.marketing "
                + "HAVING ((SUM(s.noCustomer) - SUM(s.noUsed)) != 0)) order by code");
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        q.setParameter(2, campaign);
        q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public Map<String, Integer> getAvaialableAllMarketListByIsAdminFlag(int ugId) {
        List<Marketing> marketings = this.findAvailableAllMarketingByUserGroup(ugId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    private List<Marketing> findAvailableAllMarketingEntitiesByIsAdminFlag(String where) {
        Query q = em.createQuery("select object(o) from Marketing as o where enable = true and status = true and startDate <= ?1 and endDate >= ?1 " + where + " order by code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    private List<Marketing> findAvailableAllMarketingByUserGroup(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null) {
            whereId = " and o.id in ( ";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
            result = findAvailableAllMarketingEntitiesByIsAdminFlag(whereId);
            if (result == null) {
                result = new ArrayList<Marketing>();
            }
        } else {
            result = new ArrayList<Marketing>();
        }
        return result;
    }

    public Map<String, Integer> getAvaialableTransferMarketListByMarketingUserGroup(int ugId) {
        List<Marketing> marketings = this.findTransferMarketingEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Marketing obj : marketings) {
            values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
        }
        return values;
    }

    private List<Marketing> findTransferMarketingEntitiesByMarketingUserGroup(String where) {
        Query q = em.createQuery("select object(o) from Marketing as o where totalAssigned is not null and startDate <= ?1 and endDate >= ?1 and enable = true and status = true " + where + " order by code");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    private List<Marketing> findAvailableTransferMarketingMarketingByUserGroup(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null) {
            whereId = " and o.id in ( ";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
            result = findTransferMarketingEntitiesByMarketingUserGroup(whereId);
            if (result == null) {
                result = new ArrayList<Marketing>();
            }
        } else {
            result = new ArrayList<Marketing>();
        }
        return result;
    }
    
    public List<Assignment> findMarketingByCampaignId(Integer campaignId) {
        Query q = em.createQuery("select  object(o) from Assignment as o where o.campaign.id = ?1 and o.marketing.enable = true order by o.createDate desc");
        q.setParameter(1, campaignId);
        return q.getResultList();
    }
}
