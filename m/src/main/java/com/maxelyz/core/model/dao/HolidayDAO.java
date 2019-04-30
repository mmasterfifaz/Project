/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Holiday;
import com.maxelyz.utils.JSFUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HolidayDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Holiday holiday) {
        em.persist(holiday);
    }

    public void edit(Holiday holiday) throws NonexistentEntityException, Exception {
        holiday = em.merge(holiday);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Holiday holiday;
        try {
            holiday = em.getReference(Holiday.class, id);
            holiday.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The holiday with id " + id + " no longer exists.", enfe);
        }
        em.remove(holiday);
    }

    public List<Holiday> findHolidayEntities() {
        return findHolidayEntities(true, -1, -1);
    }

    public List<Holiday> findHolidayEntities(int maxResults, int firstResult) {
        return findHolidayEntities(false, maxResults, firstResult);
    }

    private List<Holiday> findHolidayEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Holiday as o where o.enable=true  order by o.holiday desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<Integer> findYears() {
        Query q = em.createQuery("select distinct year(holiday) from Holiday where enable = true order by year(holiday)");
        return q.getResultList();
    }

    public Holiday findHoliday(Integer id) {
        return em.find(Holiday.class, id);
    }

    public boolean isHoliday(Date date) {
        date = JSFUtil.toDateWithoutTime(date);
        Query q = em.createQuery("select object(o) from Holiday o where o.holiday = ?1 and o.enable = true");
        q.setParameter(1, date);
        return !q.getResultList().isEmpty();
    }

    public List<Holiday> findHolidayByYear(String year) {
        String criteria = "";
        if (!year.equals("0")) {
            criteria = "and year(holiday) = " + year;
        }
        Query q = em.createQuery("select object(o) from Holiday as o where o.enable = true " + criteria + " order by holiday");
        return q.getResultList();
    }

    public int getHolidayCount() {
        Query q = em.createQuery("select count(o) from Holiday as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public Date getSLADate(Date date, Double hour) {
        Double globalStartWorkingHour = JSFUtil.getApplication().getSlaStartWorkingHour();
        Double globalEndWorkingHour = JSFUtil.getApplication().getSlaEndWorkingHour();
        boolean skipWeekEnd  = JSFUtil.getApplication().isSkipWeekEnd();
        boolean skipHoliday  = JSFUtil.getApplication().isSkipHoliday();
        Date slaDate = getSLADate(date, hour*1.0, globalStartWorkingHour, globalEndWorkingHour, skipWeekEnd, skipHoliday);       
        return slaDate;
    }

    private Calendar skipHoliday(Calendar date, boolean skipWeekend, boolean skipHoliday) {
         while (true) {
            int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
            if (skipWeekend &&
               (dayOfWeek==Calendar.SATURDAY ||dayOfWeek==Calendar.SUNDAY)) {
                date.set(Calendar.DAY_OF_MONTH,date.get(Calendar.DAY_OF_MONTH)+1);
            } else if (skipHoliday) {
                if (this.isHoliday(date.getTime())) {
                    date.set(Calendar.DAY_OF_MONTH,date.get(Calendar.DAY_OF_MONTH)+1);
                } else {
                    break;
                }
            } else {
                break;
            }
         }
         return date;
    }
    
    public Date getSLADate(Date date, Double slaHour, Double globalStartWorkingHour, Double globalEndWorkingHour, boolean skipWeekend, boolean skipHoliday) {
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        startDate.setTime(date);
        endDate.setTime(date);       

        int standardStartHour = globalStartWorkingHour.intValue();
        int standardStartMinute = (int)Math.round((globalStartWorkingHour - standardStartHour)*100);

        int standardEndHour = globalEndWorkingHour.intValue();
        int standardEndMinute = (int)Math.round((globalEndWorkingHour-standardEndHour)*100);

        int hour = slaHour.intValue();
        int minute = (int)Math.round((slaHour - hour)*100);
        while (true) {
            int remainHour = standardEndHour - endDate.get(Calendar.HOUR_OF_DAY);//เวลาที่เหลือในแต่ละวัน
            int remainMinute = standardEndMinute - endDate.get(Calendar.MINUTE);
            if (remainMinute < 0) {
                remainHour--;
                remainMinute = 60- Math.abs(remainMinute);
            }

            if (remainHour-hour>0 || (remainHour-hour==0 && remainMinute-minute>=0)) { //ถ้าเวลาที่เหลือมากกว่าเวลาของ sla -> ใช้เวลาที่เหลือ
                endDate.set(Calendar.HOUR_OF_DAY, endDate.get(Calendar.HOUR_OF_DAY) + hour);
                endDate.set(Calendar.MINUTE, endDate.get(Calendar.MINUTE) + minute);
                this.skipHoliday(endDate, skipWeekend, skipHoliday);
                break;
            } else {
                hour = hour - remainHour;
                minute = minute - remainMinute;
                if (minute < 0) {
                    hour--;
                    minute = 60 - Math.abs(minute);
                }
                endDate.set(Calendar.DAY_OF_MONTH,endDate.get(Calendar.DAY_OF_MONTH)+1);
                endDate.set(Calendar.HOUR_OF_DAY,standardStartHour);
                endDate.set(Calendar.MINUTE,standardStartMinute);
                this.skipHoliday(endDate, skipWeekend, skipHoliday);
            }
        }
        
        return (endDate.getTime());
    }
}
