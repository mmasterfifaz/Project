/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.RptSalePerformance;
import com.maxelyz.core.model.entity.RptSalePerformance1;
import com.maxelyz.core.model.entity.RptSalePerformancePK;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RptSalePerformanceDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(RptSalePerformance rptSalePerformance) throws PreexistingEntityException, Exception {
        em.persist(rptSalePerformance);
    }

    public void create(RptSalePerformance1 rptSalePerformance) throws PreexistingEntityException, Exception {
        em.persist(rptSalePerformance);
    }

    public void edit(RptSalePerformance rptSalePerformance) throws NonexistentEntityException, Exception {
        rptSalePerformance = em.merge(rptSalePerformance);
    }

    public void destroy(RptSalePerformancePK rptSalePerformancePK) throws Exception {
        RptSalePerformance1 rptSalePerformance;
        try {
            rptSalePerformance = em.getReference(RptSalePerformance1.class, rptSalePerformancePK);
            rptSalePerformance.getRptSalePerformancePK();
        } catch (Exception e) {
            return;
        }
        em.remove(rptSalePerformance);
    }

    public void destroyByDate(Date date) {
        Query q = em.createQuery("delete from RptSalePerformance1 o where o.rptSalePerformancePK.saleDate = ?1");
        q.setParameter(1, date);
        q.executeUpdate();
    }

    public List<RptSalePerformance> findRptSalePerformanceEntities() {
        return findRptSalePerformanceEntities(true, -1, -1);
    }

    public List<RptSalePerformance> findRptSalePerformanceEntities(int maxResults, int firstResult) {
        return findRptSalePerformanceEntities(false, maxResults, firstResult);
    }

    private List<RptSalePerformance> findRptSalePerformanceEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from RptSalePerformance as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RptSalePerformance findRptSalePerformance(RptSalePerformancePK id) {
        return em.find(RptSalePerformance.class, id);
    }

    public RptSalePerformance1 findRptSalePerformanceNew(RptSalePerformancePK id) {
        return em.find(RptSalePerformance1.class, id);
    } 

    public int getRptSalePerformanceCount() {
        Query q = em.createQuery("select count(o) from RptSalePerformance as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public void updateRptMap(Calendar currentDate, Map<RptSalePerformancePK, RptSalePerformance1> rptMap, List<Object[]> objQuery, String fieldName) {
        RptSalePerformance1 rptSalePerformance;    
        for (Object[] obj: objQuery) {
            Integer userId = (Integer) obj[0];
            Integer campaignId = (Integer) obj[1];
            Integer marketingId = (Integer) obj[2];
            int value = 0;
            if (!(fieldName.equalsIgnoreCase("TotalAmount") || fieldName.equalsIgnoreCase("TotalPendingAmount") || fieldName.equalsIgnoreCase("TotalAmountToday")
                    || fieldName.equalsIgnoreCase("TotalNewContact") || fieldName.equalsIgnoreCase("TotalOldContact")
                    || fieldName.equalsIgnoreCase("listNewNetPremiumCredit") || fieldName.equalsIgnoreCase("listNewNetPremiumDebit")
                    || fieldName.equalsIgnoreCase("listNewGrossPremiumCredit") || fieldName.equalsIgnoreCase("listNewGrossPremiumDebit")
                    || fieldName.equalsIgnoreCase("listOldNetPremiumCredit") || fieldName.equalsIgnoreCase("listOldNetPremiumDebit")
                    || fieldName.equalsIgnoreCase("listOldGrossPremiumCredit") || fieldName.equalsIgnoreCase("listOldGrossPremiumDebit")
            )) {
                value = Integer.parseInt(obj[3].toString());
            }

            RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), userId, campaignId, marketingId);
            if (rptMap.containsKey(rptSalePerformancePK)) {
                rptSalePerformance = rptMap.get(rptSalePerformancePK);
            } else {
                rptSalePerformance = new RptSalePerformance1();
                rptSalePerformance.setRptSalePerformancePK(rptSalePerformancePK);
            }
            if (fieldName.equalsIgnoreCase("ListAssigned")) {
                rptSalePerformance.setListAssigned(value);
            } else if (fieldName.equalsIgnoreCase("ListUsed")) {
                rptSalePerformance.setListUsed(value);
            } else if (fieldName.equalsIgnoreCase("ListNew")) {
                rptSalePerformance.setListNew(value);
                rptSalePerformance.setListOld(rptSalePerformance.getListUsed()==null?0:rptSalePerformance.getListUsed()-value);
            } else if (fieldName.equalsIgnoreCase("ListOpened")) {
                rptSalePerformance.setListOpened(value);
            } else if (fieldName.equalsIgnoreCase("ListFollowup")) {
                rptSalePerformance.setListFollowup(value);
            } else if (fieldName.equalsIgnoreCase("ListUncontactable")) {
                rptSalePerformance.setListUncontactable(value);
            } else if (fieldName.equalsIgnoreCase("ListUnreachable")) {
                rptSalePerformance.setListUnreachable(value);
            } else if (fieldName.equalsIgnoreCase("ListContactableClose")) {
                rptSalePerformance.setListClosedContactable(value);
            } else if (fieldName.equalsIgnoreCase("ListUnContactableClose")) {
                rptSalePerformance.setListClosedUncontactable(value);           
            } else if (fieldName.equalsIgnoreCase("ListUnreachableClose")) {
                rptSalePerformance.setListClosedUnreachable(value);
                //rptSalePerformance.setListClosedUnreachable(rptSalePerformance.getListUsed());//-rptSalePerformance.getListOpened()-rptSalePerformance.getListFollowup()-rptSalePerformance.getListClosedContactable()-rptSalePerformance.getListClosedUncontactable());
            } else if (fieldName.equalsIgnoreCase("CallAttempt")) {
                rptSalePerformance.setCallAttempt(value);
            } else if (fieldName.equalsIgnoreCase("CallSuccess")) {
                rptSalePerformance.setCallSuccess(value);
            } else if (fieldName.equalsIgnoreCase("CallFail")) {
                rptSalePerformance.setCallFail(value);
            } else if (fieldName.equalsIgnoreCase("ListYesSale")) {
                rptSalePerformance.setListYesSale(value);
            } else if (fieldName.equalsIgnoreCase("ProductYesSale")) {
                rptSalePerformance.setProductYesSale(value);
            } else if (fieldName.equalsIgnoreCase("ProductYesSaleToday")) {
                rptSalePerformance.setProductYesSaleToday(value);
            } else if (fieldName.equalsIgnoreCase("ProductYesPendingSale")) {
                rptSalePerformance.setProductYesPendingSale(value);
            } else if (fieldName.equalsIgnoreCase("FirstEnd")) {
                rptSalePerformance.setListClosedFirstend(value);
            } else if (fieldName.equalsIgnoreCase("ListNoSale")) {
                rptSalePerformance.setListNoSale(value);
            } else if (fieldName.equalsIgnoreCase("ProductNoSale")) {
                rptSalePerformance.setProductNoSale(value);
            } else if (fieldName.equalsIgnoreCase("ListFollowupSale")) {
                rptSalePerformance.setListFollowupSale(value);
            } else if (fieldName.equalsIgnoreCase("ProductFollowupSale")) {
                rptSalePerformance.setProductFollowupSale(value);
            } else if (fieldName.equalsIgnoreCase("TotalAmount")) {
                rptSalePerformance.setTotalAmount(((Double)obj[3]));
            } else if (fieldName.equalsIgnoreCase("TotalAmountToday")) {
                rptSalePerformance.setTotalAmountToday(((Double)obj[3]));
            } else if (fieldName.equalsIgnoreCase("TotalPendingAmount")) {
                rptSalePerformance.setTotalPendingAmount(((Double)obj[3]));
            } else if (fieldName.equalsIgnoreCase("ListNewContact")) {
                rptSalePerformance.setListNewContact(value);
            } else if (fieldName.equalsIgnoreCase("TotalNewContact")) {
                rptSalePerformance.setTotalNewContact(((BigDecimal)obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("ListOldContact")) {
                rptSalePerformance.setListOldContact(value);
            } else if (fieldName.equalsIgnoreCase("TotalOldContact")) {
                rptSalePerformance.setTotalOldContact(((BigDecimal)obj[3]).doubleValue());

            } else if (fieldName.equalsIgnoreCase("listNewNetPremiumCredit")) {
                rptSalePerformance.setListNewNetPremiumCredit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listNewNetPremiumDebit")) {
                rptSalePerformance.setListNewNetPremiumDebit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listNewGrossPremiumCredit")) {
                rptSalePerformance.setListNewGrossPremiumCredit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listNewGrossPremiumDebit")) {
                rptSalePerformance.setListNewGrossPremiumDebit(((BigDecimal) obj[3]).doubleValue());

            } else if (fieldName.equalsIgnoreCase("listOldNetPremiumCredit")) {
                rptSalePerformance.setListOldNetPremiumCredit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listOldNetPremiumDebit")) {
                rptSalePerformance.setListOldNetPremiumDebit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listOldGrossPremiumCredit")) {
                rptSalePerformance.setListOldGrossPremiumCredit(((BigDecimal) obj[3]).doubleValue());
            } else if (fieldName.equalsIgnoreCase("listOldGrossPremiumDebit")) {
                rptSalePerformance.setListOldGrossPremiumDebit(((BigDecimal) obj[3]).doubleValue());

           }
            rptMap.put(rptSalePerformancePK, rptSalePerformance);
        }
    }

    private void runningTime(String text, Calendar beginTime) {
        System.out.println(text+(Calendar.getInstance().getTimeInMillis() - beginTime.getTimeInMillis())/1000);//running time in seconds

    }
    
    public String updateSalePerformanceReport() {
        Date saleDate = new Date();  
        return this.updateSalePerformanceReportNew(saleDate, saleDate);
    }

    public String updateSalePerformanceReportNew(Date fromSaleDate, Date toSaleDate) { 
         Map<RptSalePerformancePK, RptSalePerformance1> rptMap = new ConcurrentHashMap<RptSalePerformancePK, RptSalePerformance1>();
         try {
            
            Query q1 = em.createQuery("select u from Users u order by u.name");

            //List Assigned
             Query q2 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                     + "from AssignmentDetail ad "
                     + "join ad.assignmentId a "
                     + "where ad.assignDate between :fromCurrentDate and :toCurrentDate "
                     + "group by ad.users.id, a.campaign, a.marketing");
            //List Used
            Query q3 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and  ad.status not in ('assigned','viewed') "
                        + "group by ad.users.id, a.campaign, a.marketing");

            //List Used New
             Query q31 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "join ad.customerId c "
                        + "join c.campaignCustomerCollection cc "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and ad.status not in ('assigned','viewed') "
                        + "and cc.listUsedDate between :fromCurrentDate and :toCurrentDate "
                        + "group by ad.users.id, a.campaign, a.marketing ");
            
            
             //List Used Old
             /*Query q32 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "join ad.customerId c "
                        + "join c.campaignCustomerCollection cc "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and ad.status not in ('assigned','viewed') "
                        + "and cc.listUsedDate < :fromCurrentDate "
                        + "group by ad.users.id, a.campaign, a.marketing");
             */
            //List Opened
            Query q4 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and  ad.status = 'opened' "
                        + "group by ad.users.id, a.campaign, a.marketing");

            //List Followup
            Query q5 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and  ad.status = 'followup' "
                        + "group by ad.users.id, a.campaign, a.marketing");

            //List Uncontactable ติดต่อได้แต่ไม่เจอตัว และยังไม่ปิด list
           /* Query q51 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and ad.status <> 'closed' "
                        + "and contactresult.contactStatus = 'Contactable' "
                        + "group by ad.users.id, a.campaign, a.marketing");
            */   
             Query q51 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        //+ "join ad.users u "
                        + "where contactresult.contactStatus = 'Contactable' "  
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and status <> 'closed') "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus in ('DMC','DMCNotOffer')) "
                        + "group by ad.users.id, a.campaign, a.marketing");  //oat 05 sep 11

            //List Unreachable ติดต่อไม่ได้ และยังไม่ปิด list
            /*Query q52 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        //+ "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and ad.status <> 'closed' "
                        + "and contactresult.contactStatus = 'Uncontactable' "
                        + "group by ad.users.id, a.campaign, a.marketing");*/
             Query q52 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        //+ "join ad.users u "
                        + "where contactresult.contactStatus = 'Uncontactable' "
                        //+ "and ad.unassign is null "
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and status <> 'closed') "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus in ('DMC','DMCNotOffer')) "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus = 'Contactable') "
                        + "group by ad.users.id, a.campaign, a.marketing"); //oat 5 sep 11


            //List Contactable Close->inactive 
            Query q6 = em.createNativeQuery("select u.id, a.campaign_id, a.marketing_id, count(distinct ad.id) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select max(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        + " where ch.create_date between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id"
                        + " inner join contact_history ch on ch.id = cha.contact_history_id "
                        + " inner join contact_result on contact_result.id = ch.contact_result_id"
                        + " inner join users u on u.id = ad.user_id"
                        + " where contact_result.contact_status in ('DMC','DMCNotOffer') "                     
                        + " and ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and ad.status = 'closed' "
                        + " group by u.id, a.campaign_id, a.marketing_id");

            //List UnContactable Close //result = assignment(close) - contact('DMC')
             Query q7 = em.createNativeQuery("select u.id, a.campaign_id, a.marketing_id, count(distinct ad.id) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select max(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        + " where ch.create_date between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id"
                        + " inner join contact_history ch on ch.id = cha.contact_history_id "
                        + " inner join contact_result on contact_result.id = ch.contact_result_id"
                        + " inner join users u on u.id = ad.user_id"
                        + " where contact_result.contact_status = 'Contactable' "                     
                        + " and ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and ad.status = 'closed' "
                        + " group by u.id, a.campaign_id, a.marketing_id");
             /*em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where contactresult.contactStatus = 'Contactable' "                     
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where update_date between :fromCurrentDate and :toCurrentDate "
                        + "and status = 'closed') "
                        + "and adhist.id in "
                        + "  (select max(ch.id) from ContactHistory ch "
                        + "  join ch.assignmentDetailCollection adc "
                        + "  where ch.createDate between :fromCurrentDate and :toCurrentDate "
                        + "  group by adc.id) "
                        + "group by u.id, a.campaign, a.marketing");*/
                        
           /* Query q7 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where contactresult.contactStatus = 'Contactable' "
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and status = 'closed') "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus in ('DMC','DMCNotOffer')) "
                        + "group by u.id, a.campaign, a.marketing");
            */
            //List Unreachable Close
             Query q9 = em.createNativeQuery("select u.id, a.campaign_id, a.marketing_id, count(distinct ad.id) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select max(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        + " where ch.create_date between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id"
                        + " inner join contact_history ch on ch.id = cha.contact_history_id "
                        + " inner join contact_result on contact_result.id = ch.contact_result_id"
                        + " inner join users u on u.id = ad.user_id"
                        + " where contact_result.contact_status = 'Uncontactable' "                     
                        + " and ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and ad.status = 'closed' "
                        + " group by u.id, a.campaign_id, a.marketing_id");
             /*em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where contactresult.contactStatus = 'Uncontactable' "                     
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where update_date between :fromCurrentDate and :toCurrentDate "
                        + "and status = 'closed') "
                        + "and adhist.id in "
                        + "  (select max(ch.id) from ContactHistory ch "
                        + "  join ch.assignmentDetailCollection adc "
                        + "  where ch.createDate between :fromCurrentDate and :toCurrentDate "
                        + "  group by adc.id) "
                        + "group by u.id, a.campaign, a.marketing");*/
                        
             /*
            Query q9 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where contactresult.contactStatus = 'Uncontactable' "
                        //+ "and ad.unassign is null "  
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and status = 'closed') "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus in ('DMC','DMCNotOffer')) "
                        + "and ad.id not in "
                        + "(select distinct ad1.id from AssignmentDetail ad1 "
                        + "join ad1.contactHistoryCollection adhist1 "
                        + "join adhist1.contactResult contactresult1 "
                        + "where ad1.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult1.contactStatus = 'Contactable') "
                        + "group by u.id, a.campaign, a.marketing");
            */
            /*Query q9 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join ad.users u "
                        + "where contactresult.contactStatus = 'Uncontactable' "
                        + "and ad.id in "
                        + "(select id from AssignmentDetail "
                        + "where updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and status = 'closed') "
                        + "and ad.id not in "
                        + "(select disticnt ad.id from AssignmentDetail ad "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactResult contactresult "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult.contactStatus = 'DMC') "
                        + "and ad.id not in "
                        + "(select distinct ad.id from AssignmentDetail ad "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactResult contactresult "
                        + "where ad.updateDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult.contactStatus = 'Contactable') "
                        + "group by u.id, a.campaign, a.marketing");*/

            //Call Attempt
            Query q10 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct adhist.id) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join adhist.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult.contactStatus in ('Contactable','DMC','DMCNotOffer','Uncontactable') "
                        + "group by u.id, a.campaign, a.marketing");
            //Call Success
            Query q11 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct adhist.id) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join adhist.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult.contactStatus in ('Contactable','DMC','DMCNotOffer') "
                        + "group by u.id, a.campaign, a.marketing");
            //Call Fail -->Call Attempt - Call Success
            Query q12 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct adhist.id) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "join adhist.contactResult contactresult "
                        + "join adhist.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and contactresult.contactStatus = 'Uncontactable' "
                        + "group by u.id, a.campaign, a.marketing");
            //List Yes Sale
            Query q13 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                       // + "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
            //Product Yes Sale
            Query q14 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.saleDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.paymentStatus='approved' and po.qcStatus='approved' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
            //Product Yes Pending Sale
            Query q14_1 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
            //Product First Call Yes Sale
            Query q14_2 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' and ad.callAttempt=1 and ad.status = 'closed' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
             //Product Yes Sale Today
            Query q14_3 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.saleDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.paymentStatus='approved' and po.qcStatus='approved' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
            //List No Sale
            /*
            Query q15 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'N' "
                        + "group by u.id, a.campaign, a.marketing");*/
            Query q15 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactHistorySaleResultCollection saleResult "
                        + "join saleResult.purchaseOrderId po "
                        //+ "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and saleResult.saleResult = 'N' "
                        + "group by ad.users.id, a.campaign, a.marketing");
          
            //Product No Sale
            /*Query q16 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'N' "
                        + "group by u.id, a.campaign, a.marketing");*/
            
            Query q16 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct saleResult) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactHistorySaleResultCollection saleResult "
                        + "join saleResult.purchaseOrderId po "
                        //+ "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and saleResult.saleResult = 'N' "
                        + "group by ad.users.id, a.campaign, a.marketing");

             //List Followup Sale
            /*
            Query q17 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'F' "
                        + "group by u.id, a.campaign, a.marketing");*/
            Query q17 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactHistorySaleResultCollection saleResult "
                        //+ "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and saleResult.saleResult = 'F' "
                        + "group by ad.users.id, a.campaign, a.marketing");

            //Product Followup Sale
            /*Query q18 = em.createQuery("select u.id, a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'F' "
                        + "group by u.id, a.campaign, a.marketing");*/
            Query q18 = em.createQuery("select ad.users.id, a.campaign.id, a.marketing.id, count(distinct saleResult) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist "
                        + "join adhist.contactHistorySaleResultCollection saleResult "
                        //+ "join ad.users u "
                        + "where adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and saleResult.saleResult = 'F' "
                        + "group by ad.users.id, a.campaign, a.marketing");
            
            //total amount
            Query q19 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, sum(po.annualNetPremium) "
                        + "from PurchaseOrder po "
                          
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.saleDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' and po.approvalStatus='approved' and po.paymentStatus='approved' and po.qcStatus='approved' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");
            
            //total amount today
            Query q19_1 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, sum(po.annualNetPremium) "
                        + "from PurchaseOrder po "
                          
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.saleDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' and po.approvalStatus='approved' and po.paymentStatus='approved' and po.qcStatus='approved' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");

            //total pending amount
            Query q20 = em.createQuery("select po.createByUser.id, a.campaign.id, a.marketing.id, sum(po.annualNetPremium) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        //+ "join ad.users u "
                        + "where po.purchaseDate between :fromCurrentDate and :toCurrentDate "                      
                        + "and po.saleResult = 'Y' "
                        + "group by po.createByUser.id, a.campaign, a.marketing");

            //Count List New Contact 
            Query q21 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, count(distinct ad.id) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        //+ " where cast(convert(varchar(10), ch.create_date, 126) as datetime) between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
                        + " inner join contact_history on contact_history.id = cha.contact_history_id "
                        //+ " inner join contact_result on contact_result.id = ch.contact_result_id"
                        //+ " inner join users u on u.id = ad.user_id"
                        + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                        + " where ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and po.sale_result = 'Y' "
                        + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                        + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //Total List New Contact 
            Query q21_1 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        //+ " where cast(convert(varchar(10), ch.create_date, 126) as datetime) between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
                        + " inner join contact_history on contact_history.id = cha.contact_history_id "
                        //+ " inner join contact_result on contact_result.id = ch.contact_result_id"
                        //+ " inner join users u on u.id = ad.user_id"
                        + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                        + " where ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and po.sale_result = 'Y' "
                        + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                        + " group by ad.user_id, a.campaign_id, a.marketing_id");

             //listNewNetPremiumCredit
             Query q21_2 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                     + " from assignment_detail ad "
                     + " inner join assignment a on a.id = ad.assignment_id"
                     + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                     + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                     + " where po.sale_date between :fromCurrentDate and :toCurrentDate "
                     + " and po.sale_result = 'Y' "
                     + " and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved' "
                     + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                     + " group by ad.user_id, a.campaign_id, a.marketing_id");

             //listNewNetPremiumDebit
             Query q21_3 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                     + " from assignment_detail ad "
                     + " inner join assignment a on a.id = ad.assignment_id"
                     + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                     + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.debitcard = 1 "
                     + " where po.sale_date between :fromCurrentDate and :toCurrentDate "
                     + " and po.sale_result = 'Y' "
                     + " and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved' "
                     + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                     + " group by ad.user_id, a.campaign_id, a.marketing_id");

             //listNewGrossPremiumCredit
             Query q21_4 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                     + " from assignment_detail ad "
                     + " inner join assignment a on a.id = ad.assignment_id"
                     + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                     + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                     + " where po.purchase_date between :fromCurrentDate and :toCurrentDate "
                     + " and po.sale_result = 'Y' "
                     + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                     + " group by ad.user_id, a.campaign_id, a.marketing_id");

             //listNewGrossPremiumDebit
             Query q21_5 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                     + " from assignment_detail ad "
                     + " inner join assignment a on a.id = ad.assignment_id"
                     + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                     + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.debitcard = 1 "
                     + " where po.purchase_date between :fromCurrentDate and :toCurrentDate "
                     + " and po.sale_result = 'Y' "
                     + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                     + " group by ad.user_id, a.campaign_id, a.marketing_id");

             //Count List Old Contact
            Query q22 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, count(distinct ad.id) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        //+ " where cast(convert(varchar(10), ch.create_date, 126) as datetime) between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
                        + " inner join contact_history on contact_history.id = cha.contact_history_id "
                        //+ " inner join contact_result on contact_result.id = ch.contact_result_id"
                        //+ " inner join users u on u.id = ad.user_id"
                        + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                        + " where ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and po.sale_result = 'Y' "
                        + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) < cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                        + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //Total List Old Contact 
            Query q22_1 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                        + " from assignment_detail ad "
                        + " inner join assignment a on a.id = ad.assignment_id"
                        + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                        + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                        //+ " where cast(convert(varchar(10), ch.create_date, 126) as datetime) between :fromCurrentDate and :toCurrentDate "
                        + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
                        + " inner join contact_history on contact_history.id = cha.contact_history_id "
                        //+ " inner join contact_result on contact_result.id = ch.contact_result_id"
                        //+ " inner join users u on u.id = ad.user_id"
                        + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                        + " where ad.update_date between :fromCurrentDate and :toCurrentDate "
                        + " and po.sale_result = 'Y' "
                        + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) < cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                        + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //listOldNetPremiumCredit
            Query q22_2 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                    + " from assignment_detail ad "
                    + " inner join assignment a on a.id = ad.assignment_id"
                    + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                    + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                    + " where po.sale_date between :fromCurrentDate and :toCurrentDate "
                    + " and po.sale_result = 'Y' "
                    + " and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved' "
                    + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                    + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //listOldNetPremiumDebit
            Query q22_3 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                    + " from assignment_detail ad "
                    + " inner join assignment a on a.id = ad.assignment_id"
                    + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                    + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.debitcard = 1 "
                    + " where po.sale_date between :fromCurrentDate and :toCurrentDate "
                    + " and po.sale_result = 'Y' "
                    + " and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved' "
                    + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                    + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //listOldGrossPremiumCredit
            Query q22_4 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                    + " from assignment_detail ad "
                    + " inner join assignment a on a.id = ad.assignment_id"
                    + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                    + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                    + " where po.purchase_date between :fromCurrentDate and :toCurrentDate "
                    + " and po.sale_result = 'Y' "
                    + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                    + " group by ad.user_id, a.campaign_id, a.marketing_id");

            //listOldGrossPremiumDebit
            Query q22_5 = em.createNativeQuery("select ad.user_id, a.campaign_id, a.marketing_id, sum(po.annual_net_premium) "
                    + " from assignment_detail ad "
                    + " inner join assignment a on a.id = ad.assignment_id"
                    + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                    + " inner join payment_method pmc on pmc.id = po.payment_method and pmc.debitcard = 1 "
                    + " where po.purchase_date between :fromCurrentDate and :toCurrentDate "
                    + " and po.sale_result = 'Y' "
                    + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                    + " group by ad.user_id, a.campaign_id, a.marketing_id");

            fromSaleDate = JSFUtil.toDateWithoutTime(fromSaleDate);
            toSaleDate = JSFUtil.toDateWithoutTime(toSaleDate);
            toSaleDate = JSFUtil.toDateWithMaxTime(toSaleDate);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(fromSaleDate);
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(toSaleDate);
            while (currentDate.before(toDate)) {
                this.destroyByDate(currentDate.getTime());

                rptMap.clear();
                System.out.println("Current Date:"+currentDate.getTime().toString()+"==================");
                q2.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q2.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q3.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q3.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q31.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q31.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //q32.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                //q32.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q4.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q4.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q5.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q5.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q51.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q51.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q52.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q52.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q6.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q6.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q7.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q7.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //q8 firstend
                q9.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q9.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q10.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q10.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q11.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q11.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q12.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q12.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q13.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q13.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q14.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q14.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q14_1.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q14_1.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q14_2.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q14_2.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q14_3.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q14_3.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q15.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q15.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q16.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q16.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q17.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q17.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q18.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q18.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q19.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q19.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q19_1.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q19_1.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q20.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q20.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21_1.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21_1.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21_2.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21_2.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21_3.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21_3.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21_4.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21_4.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q21_5.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q21_5.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22_1.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22_1.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22_2.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22_2.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22_3.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22_3.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22_4.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22_4.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                q22_5.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q22_5.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));

                //System.out.println("Before query");
                Calendar now;
                now = Calendar.getInstance();
                List<Object[]> objQ2 = (List<Object[]>)q2.getResultList();
//                runningTime("Q2 Time(ms.):", now);
//                now = Calendar.getInstance();
                List<Object[]> objQ3 = (List<Object[]>)q3.getResultList();
                List<Object[]> objQ31 = (List<Object[]>)q31.getResultList();
                //List<Object[]> objQ32 = (List<Object[]>)q32.getResultList();
                //runningTime("Q3 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ4 = (List<Object[]>)q4.getResultList();
                //runningTime("Q4 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ5 = (List<Object[]>)q5.getResultList();
                //runningTime("Q5 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ51 = (List<Object[]>)q51.getResultList();
                //runningTime("Q51 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ52 = (List<Object[]>)q52.getResultList();
                //runningTime("Q52 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ6 = (List<Object[]>)q6.getResultList();
                //runningTime("Q6 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ7 = (List<Object[]>)q7.getResultList();
                //runningTime("Q7 Time(ms.):", now);
                //now = Calendar.getInstance();
                //q8
                List<Object[]> objQ9 = (List<Object[]>)q9.getResultList();
                //runningTime("Q9 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ10 = (List<Object[]>)q10.getResultList();
                //runningTime("Q10 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ11 = (List<Object[]>)q11.getResultList();
                //runningTime("Q11 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ12 = (List<Object[]>)q12.getResultList();
                //runningTime("Q12 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ13 = (List<Object[]>)q13.getResultList();
                //runningTime("Q13 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ14 = (List<Object[]>)q14.getResultList();
                
                List<Object[]> objQ14_1 = (List<Object[]>)q14_1.getResultList();
                
                List<Object[]> objQ14_2 = (List<Object[]>)q14_2.getResultList();
                
                List<Object[]> objQ14_3 = (List<Object[]>)q14_3.getResultList();
                
                //runningTime("Q14 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ15 = (List<Object[]>)q15.getResultList();
                //runningTime("Q15 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ16 = (List<Object[]>)q16.getResultList();
                //runningTime("Q16 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ17 = (List<Object[]>)q17.getResultList();
                //runningTime("Q17 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ18 = (List<Object[]>)q18.getResultList();
                //runningTime("Q18 Time(ms.):", now);
                //now = Calendar.getInstance();
                List<Object[]> objQ19 = (List<Object[]>)q19.getResultList();
                List<Object[]> objQ19_1 = (List<Object[]>)q19_1.getResultList();
                List<Object[]> objQ20 = (List<Object[]>)q20.getResultList();
                
                List<Object[]> objQ21 = (List<Object[]>)q21.getResultList();
                List<Object[]> objQ21_1 = (List<Object[]>)q21_1.getResultList();
                List<Object[]> objQ21_2 = (List<Object[]>)q21_2.getResultList();
                List<Object[]> objQ21_3 = (List<Object[]>)q21_3.getResultList();
                List<Object[]> objQ21_4 = (List<Object[]>)q21_4.getResultList();
                List<Object[]> objQ21_5 = (List<Object[]>)q21_5.getResultList();

                List<Object[]> objQ22 = (List<Object[]>)q22.getResultList();
                List<Object[]> objQ22_1 = (List<Object[]>)q22_1.getResultList();
                List<Object[]> objQ22_2 = (List<Object[]>)q22_2.getResultList();
                List<Object[]> objQ22_3 = (List<Object[]>)q22_3.getResultList();
                List<Object[]> objQ22_4 = (List<Object[]>)q22_4.getResultList();
                List<Object[]> objQ22_5 = (List<Object[]>)q22_5.getResultList();

                runningTime("Running RptUpdate Time(ms.):", now);
           
                //System.out.println("End query");

                this.updateRptMap(currentDate, rptMap, objQ2, "ListAssigned");
                this.updateRptMap(currentDate, rptMap, objQ3, "ListUsed");
                this.updateRptMap(currentDate, rptMap, objQ31, "ListNew");
                this.updateRptMap(currentDate, rptMap, objQ31, "ListOld");
                this.updateRptMap(currentDate, rptMap, objQ4, "ListOpened");
                this.updateRptMap(currentDate, rptMap, objQ5, "ListFollowup");
                this.updateRptMap(currentDate, rptMap, objQ51, "ListUncontactable");
                this.updateRptMap(currentDate, rptMap, objQ52, "ListUnreachable");
                this.updateRptMap(currentDate, rptMap, objQ6, "ListContactableClose");
                this.updateRptMap(currentDate, rptMap, objQ7, "ListUncontactableClose");
                //q8
                this.updateRptMap(currentDate, rptMap, objQ9, "ListUnreachableClose");
                this.updateRptMap(currentDate, rptMap, objQ10, "CallAttempt");
                this.updateRptMap(currentDate, rptMap, objQ11, "CallSuccess");
                this.updateRptMap(currentDate, rptMap, objQ12, "CallFail");
                this.updateRptMap(currentDate, rptMap, objQ13, "ListYesSale");
                this.updateRptMap(currentDate, rptMap, objQ14, "ProductYesSale");
                this.updateRptMap(currentDate, rptMap, objQ14_1, "ProductYesPendingSale");
                this.updateRptMap(currentDate, rptMap, objQ14_2, "FirstEnd");
                this.updateRptMap(currentDate, rptMap, objQ14_3, "ProductYesSaleToday");
                this.updateRptMap(currentDate, rptMap, objQ15, "ListNoSale");
                this.updateRptMap(currentDate, rptMap, objQ16, "ProductNoSale");
                this.updateRptMap(currentDate, rptMap, objQ17, "ListFollowupSale");
                this.updateRptMap(currentDate, rptMap, objQ18, "ProductFollowupSale");
                this.updateRptMap(currentDate, rptMap, objQ19, "TotalAmount");
                this.updateRptMap(currentDate, rptMap, objQ19_1, "TotalAmountToday");
                this.updateRptMap(currentDate, rptMap, objQ20, "TotalPendingAmount");
                this.updateRptMap(currentDate, rptMap, objQ21, "ListNewContact");
                this.updateRptMap(currentDate, rptMap, objQ21_1, "TotalNewContact");
                this.updateRptMap(currentDate, rptMap, objQ21_2, "listNewNetPremiumCredit");
                this.updateRptMap(currentDate, rptMap, objQ21_3, "listNewNetPremiumDebit");
                this.updateRptMap(currentDate, rptMap, objQ21_4, "listNewGrossPremiumCredit");
                this.updateRptMap(currentDate, rptMap, objQ21_5, "listNewGrossPremiumDebit");

                this.updateRptMap(currentDate, rptMap, objQ22, "ListOldContact");
                this.updateRptMap(currentDate, rptMap, objQ22_1, "TotalOldContact");
                this.updateRptMap(currentDate, rptMap, objQ22_2, "listOldNetPremiumCredit");
                this.updateRptMap(currentDate, rptMap, objQ22_3, "listOldNetPremiumDebit");
                this.updateRptMap(currentDate, rptMap, objQ22_4, "listOldGrossPremiumCredit");
                this.updateRptMap(currentDate, rptMap, objQ22_5, "listOldGrossPremiumDebit");

                for (Map.Entry<RptSalePerformancePK,RptSalePerformance1> entry : rptMap.entrySet()) {
                    RptSalePerformance1 rptSale =  entry.getValue();
                    if ((rptSale.getListOpened()!=null&&rptSale.getListOpened()>0) ||
                        (rptSale.getListFollowup()!=null&&rptSale.getListFollowup()>0) ||
                        (rptSale.getCallAttempt()>0) ||
                        (rptSale.getProductYesSale()!=null&&rptSale.getProductYesSale()>0)) {
                        this.create(entry.getValue()); //...then insert
                    }
                }
                currentDate.add(Calendar.DATE, 1);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error:"+e.getMessage();
        }

        return "Completed";
    }

    public String updateSalePerformanceReport(Date fromSaleDate, Date toSaleDate){
        try {
            /*Query q0 = em.createQuery("select ad.assignDate from AssignmentDetail ad where ad.assignDate >= :fromSaleDate order by ad.assignDate");
            q0.setMaxResults(1);
            q0.setParameter("fromSaleDate", JSFUtil.toDateWithoutTime(fromSaleDate));
            fromSaleDate = (Date)q0.getSingleResult();*/
            

            Query q1 = em.createQuery("select u from Users u order by u.name");
            
            //List Used New
            Query q2 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "where ad.status not in ('assigned','viewed') "
                        + "and ad.users = :user "
                        + "and ad.assignDate between :fromCurrentDate and :toCurrentDate "
                        + "and adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "group by a.campaign, a.marketing");
            //List Used Old
            Query q3 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "where ad.status not in ('assigned','viewed') "
                        + "and ad.users = :user "
                        + "and ad.assignDate < :fromCurrentDate "
                        + "and adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "group by a.campaign, a.marketing");
            //Call Attempt
            Query q4 = em.createQuery("select a.campaign.id, a.marketing.id, count(ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "where ad.users = :user "
                        + "and adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "group by a.campaign, a.marketing");
            //Call Success
            Query q5 = em.createQuery("select a.campaign.id, a.marketing.id, count(ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "where ad.users = :user "
                        + "and adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and adhist.contactResult.contactStatus = '"+JSFUtil.getBundleValue("contactableValue")+"' "
                        + "group by a.campaign, a.marketing");

            //Sale Attempt
            Query q6 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "where po.createByUser = :user "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "group by a.campaign, a.marketing");

            //Yes Sale
            Query q7 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct po), sum(po.totalAmount) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "where po.createByUser = :user "
                        + "and po.saleDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult = 'Y' "
                        + "group by a.campaign, a.marketing");

            //No Sale
           Query q8 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "where po.createByUser = :user "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult ='N' "
                        + "group by a.campaign, a.marketing");

            //Followup
            //Query q9
             Query q9 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct po) "
                        + "from PurchaseOrder po "
                        + "join po.assignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "where po.createByUser = :user "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult ='F' "
                        + "group by a.campaign, a.marketing");

            //Uncontactable
             Query q10 = em.createQuery("select a.campaign.id, a.marketing.id, count(distinct ad) "
                        + "from AssignmentDetail ad "
                        + "join ad.assignmentId a "
                        + "join ad.contactHistoryCollection adhist  "
                        + "where ad.users = :user "
                        + "and adhist.createDate between :fromCurrentDate and :toCurrentDate "
                        + "and adhist.contactResult.contactStatus = '"+JSFUtil.getBundleValue("uncontactableValue")+"' "
                        + "group by a.campaign, a.marketing");

             //total amount...as is q7
             /*
              Query q11 = em.createQuery("select sum(po.totalAmount) from PurchaseOrder po "
                        + "where po.createByUser = :user "
                        + "and po.purchaseDate between :fromCurrentDate and :toCurrentDate "
                        + "and po.saleResult ='Y'");*/

            fromSaleDate = JSFUtil.toDateWithoutTime(fromSaleDate);
            toSaleDate = JSFUtil.toDateWithMaxTime(toSaleDate);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(fromSaleDate);
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(toSaleDate);
            while (currentDate.before(toDate)) {
                System.out.println("Current Date:"+currentDate.getTime().toString()+"==================");
                //List use new   
                q2.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q2.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //List use old
                q3.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q3.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //Call Attempt
                q4.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q4.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //Call Success
                q5.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q5.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
               
                //Sale Attempt
                q6.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q6.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //Yes Sale
                q7.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q7.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //No Sale
                q8.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q8.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //Followup
                q9.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q9.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));

                //Uncontactble
                q10.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                q10.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));
                //Total Amount
                //q11.setParameter("fromCurrentDate", JSFUtil.toDateWithoutTime(currentDate.getTime()));
                //q11.setParameter("toCurrentDate", JSFUtil.toDateWithMaxTime(currentDate.getTime()));

                List<Users> users = (List<Users>) q1.getResultList();//q1
                for (Users u : users) {
                    
                    q2.setParameter("user", u);
                    q3.setParameter("user", u);
                    q4.setParameter("user", u);
                    q5.setParameter("user", u);
                    q6.setParameter("user", u);
                    q7.setParameter("user", u);
                    q8.setParameter("user", u);
                    q9.setParameter("user", u);
                    q10.setParameter("user", u);
                    //q11.setParameter("user", u);

                    RptSalePerformance rptSalePerformance;
                    //Long listusenew = (Long)q2.getSingleResult();
                    List<Object[]> objQ2 = (List<Object[]>)q2.getResultList(); 
                    for (Object[] obj: objQ2) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int listUsedNew = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(listUsedNew);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setListUsedNew(listUsedNew);
                            this.edit(rptSalePerformance);
                        }
                    }
                    //Long listuseold = (Long)q3.getSingleResult();
                    List<Object[]> objQ3 = (List<Object[]>)q3.getResultList();
                    for (Object[] obj: objQ3) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int listUsedOld = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(listUsedOld);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setListUsedOld(listUsedOld);
                            this.edit(rptSalePerformance);
                        }
                    }
    
                    //Long callAttempt = (Long)q4.getSingleResult();
                    List<Object[]> objQ4 = (List<Object[]>)q4.getResultList();
                    for (Object[] obj: objQ4) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int callAttempt = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(callAttempt);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setCallAttempt(callAttempt);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //Long callSuccess = (Long)q5.getSingleResult();
                    List<Object[]> objQ5 = (List<Object[]>)q5.getResultList();
                    for (Object[] obj: objQ5) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int callSuccess = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(callSuccess);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setCallSuccess(callSuccess);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //Long saleAttempt = (Long)q6.getSingleResult();
                    List<Object[]> objQ6 = (List<Object[]>)q6.getResultList();
                    for (Object[] obj: objQ6) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int saleAttempt = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(saleAttempt);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setSaleAttempt(saleAttempt);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //Long yesSale = (Long)q7.getSingleResult();
                    System.out.println("");
                    System.out.println("Yes Sale===========");
                    List<Object[]> objQ7 = (List<Object[]>)q7.getResultList();
                    for (Object[] obj: objQ7) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int yesSale = ((Long) obj[2]).intValue();
                        double amount = ((Double) obj[3]).doubleValue();
                        BigDecimal totalAmount = new BigDecimal(amount);
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(yesSale);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(yesSale);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(totalAmount);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setSaleAttempt(yesSale);
                            rptSalePerformance.setYesSale(yesSale);
                            rptSalePerformance.setTotalAmount(totalAmount);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //Long noSale = (Long)q8.getSingleResult();
                    List<Object[]> objQ8 = (List<Object[]>)q8.getResultList();
                    for (Object[] obj: objQ8) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int noSale = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(noSale);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(noSale);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            int saleAttempt = rptSalePerformance.getSaleAttempt();
                            rptSalePerformance.setSaleAttempt(saleAttempt+noSale);
                            rptSalePerformance.setNoSale(noSale);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //q9 not query
                    List<Object[]> objQ9 = (List<Object[]>)q9.getResultList();
                    for (Object[] obj: objQ9) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int followup = ((Long) obj[2]).intValue();
                        
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(followup);
                            rptSalePerformance.setUncontactable(0);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setFollowUp(followup);
                            this.edit(rptSalePerformance);
                        }
                    }

                    //Long uncontactable = (Long)q10.getSingleResult();
                    List<Object[]> objQ10 = (List<Object[]>)q10.getResultList();
                    for (Object[] obj: objQ10) {
                        Integer campaignId = (Integer) obj[0];
                        Integer marketingId = (Integer) obj[1];
                        int uncontactable = ((Long) obj[2]).intValue();
                        RptSalePerformancePK rptSalePerformancePK = new RptSalePerformancePK(currentDate.getTime(), u.getId(), campaignId, marketingId);
                        rptSalePerformance = this.findRptSalePerformance(rptSalePerformancePK);
                        if (rptSalePerformance==null) {
                            rptSalePerformance = new RptSalePerformance(rptSalePerformancePK);
                            rptSalePerformance.setListUsedNew(0);
                            rptSalePerformance.setListUsedOld(0);
                            rptSalePerformance.setCallAttempt(0);
                            rptSalePerformance.setCallSuccess(0);
                            rptSalePerformance.setSaleAttempt(0);
                            rptSalePerformance.setSaleOffering(0);
                            rptSalePerformance.setYesSale(0);
                            rptSalePerformance.setNoSale(0);
                            rptSalePerformance.setContactable(0);
                            rptSalePerformance.setFollowUp(0);
                            rptSalePerformance.setUncontactable(uncontactable);
                            rptSalePerformance.setTotalAmount(BigDecimal.ZERO);
                            this.create(rptSalePerformance);
                        } else {
                            rptSalePerformance.setUncontactable(uncontactable);
                            this.edit(rptSalePerformance);
                        }
                    }


                   // Double totalAmount = (Double)q11.getSingleResult();



                    System.out.print("user:"+u.getName());
                   // System.out.print("\told:"+listuseold);
                    //System.out.print("\tnew:"+listusenew);
                    //System.out.print("\tcall attempt:"+callAttempt);
                    //System.out.print("\tcall success:"+callSuccess);
                    //System.out.print("\tsale attempt:"+saleAttempt);
                    //System.out.print("\tYes sale:"+yesSale);
                    //System.out.println("\tNo sale:"+noSale);
                    //followup
                    //fxxx
                    //System.out.println("\tUncontactable:"+uncontactable);
                    //System.out.println("\tTotal Amount:"+totalAmount);

                }
                currentDate.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            return "Error:"+e.getMessage();
        }
       
        return "Completed";
    }

    public RptSalePerformance findRptSalePerformance(Integer userId) {
        String sql = "select object(o) from RptSalePerformance o"
                + " where o.users.id = :userId";

        Query q = em.createQuery(sql);
//        q.setParameter("saleDate", JSFUtil.toDateWithoutTime(new Date()));
        q.setParameter("userId", userId);

        RptSalePerformance rpt = q.getResultList().isEmpty() ? null : (RptSalePerformance) q.getResultList().get(0);

        return rpt;
    }
    
    public RptSalePerformance1 findRptSalePerformance(Users user) {
       
        Query q = em.createQuery("select o from RptSalePerformance1 o where o.rptSalePerformancePK.userId = ?1 and o.rptSalePerformancePK.saleDate = ?2 "
               );
        q.setParameter(1, user.getId());
        q.setParameter(2, JSFUtil.toDateWithoutTime(new Date()));
        //return (RptSalePerformance1)q.getSingleResult();
        
        //System.out.println("size="+q.getResultList().size());
        List<RptSalePerformance1> rpt = (List<RptSalePerformance1>)q.getResultList();
        RptSalePerformance1 r = new RptSalePerformance1();
        
        for (RptSalePerformance1 o : rpt) {
            r.setListAssigned(r.getListAssigned()+o.getListAssigned());
            if (o.getListUsed()!=null)
                r.setListUsed(r.getListUsed()+o.getListUsed());
            if (o.getListOpened()!=null)
                r.setListOpened(r.getListOpened()+o.getListOpened());
            if (o.getListFollowup()!=null)
                r.setListFollowup(r.getListFollowup()+o.getListFollowup());
            if (o.getListUncontactable()!=null)
                r.setListUncontactable(r.getListUncontactable()+o.getListUncontactable());
            if (o.getListUnreachable()!=null)
                r.setListUnreachable(r.getListUnreachable()+o.getListUnreachable());
            if (o.getListClosedContactable()!=null)
                r.setListClosedContactable(r.getListClosedContactable()+o.getListClosedContactable());
            if (o.getListClosedUncontactable()!=null)
                r.setListClosedUncontactable(r.getListClosedUncontactable()+o.getListClosedUncontactable());
            if (o.getListClosedFirstend()!=null)
                r.setListClosedFirstend(r.getListClosedFirstend()+o.getListClosedFirstend());
            if (o.getListClosedUnreachable()!=null)
                r.setListClosedUnreachable(r.getListClosedUnreachable()+o.getListClosedUnreachable());
          
            r.setCallAttempt(r.getCallAttempt()+o.getCallAttempt());
            r.setCallSuccess(r.getCallSuccess()+o.getCallSuccess());
            r.setCallFail(r.getCallFail()+o.getCallFail());
            r.setSaleAttempt(r.getSaleAttempt()+o.getSaleAttempt());
            r.setListYesSale(r.getListYesSale()+o.getListYesSale());
            r.setProductYesPendingSale((r.getProductYesPendingSale() == null ? 0 : r.getProductYesPendingSale()) + (o.getProductYesPendingSale() == null ? 0 : o.getProductYesPendingSale()));
            r.setListNoSale(r.getListNoSale()+o.getListNoSale());
            r.setListFollowupSale(r.getListFollowupSale()+o.getListFollowupSale());
            r.setProductNoSale(r.getProductNoSale()+o.getProductNoSale());
            r.setTotalPendingAmount((r.getTotalPendingAmount() == null ? 0 : r.getTotalPendingAmount()) + (o.getTotalPendingAmount() == null ? 0 : o.getTotalPendingAmount()));
            if (o.getProductYesSale()!=null)
                r.setProductYesSale(r.getProductYesSale()+o.getProductYesSale());
           
            r.setProductFollowupSale(r.getProductFollowupSale()+o.getProductFollowupSale());
            //r.setTotalAmount(r.getTotalAmount()+o.getTotalAmount());
        }
        //return (q.getResultList().isEmpty() ? null: (RptSalePerformance1)q.getResultList().get(0) );
        return r;
       
    }
}
