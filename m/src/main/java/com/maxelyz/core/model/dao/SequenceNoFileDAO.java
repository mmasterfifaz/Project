package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.SequenceNo;
import com.maxelyz.core.model.entity.SequenceNoFile;
import com.maxelyz.core.model.entity.SequenceNoFileDetail;
import com.maxelyz.utils.JSFUtil;
import com.opencsv.CSVReader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by support on 11/10/2017.
 */
@Transactional
public class SequenceNoFileDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(SequenceNoFile sequenceNoFile,File dataFile) throws PreexistingEntityException, Exception {
        String charset = "utf-8";
        BufferedReader br = null;
        CSVReader reader = null;
        String line[] ;
        Integer totalRecord=0;
        try {
            em.persist(sequenceNoFile);

            br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), charset));
            reader = new CSVReader(new FileReader(dataFile));

            while ((line = reader.readNext( )) != null) {
                //System.out.println(""+line[0]+"\n");
                if(line[0].trim().equals("")){
                    break;
                }
                SequenceNoFileDetail sequenceNoFileDetail = new SequenceNoFileDetail();
                sequenceNoFileDetail.setId(null);
                sequenceNoFileDetail.setSequenceNoFile(new SequenceNoFile(sequenceNoFile.getId()));
                sequenceNoFileDetail.setStatus("Available");
                sequenceNoFileDetail.setSequenceNo(line[0].trim());
                em.persist(sequenceNoFileDetail);
                totalRecord++;
            }
            sequenceNoFile.setTotalRecord(totalRecord++);
            em.merge(sequenceNoFile);
            updateDuplicateSequenceIsCancel(sequenceNoFile.getId());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateDuplicateSequenceIsCancel(Integer sequenceNoFileId){
        try{
            Query q = em.createNativeQuery("update a set a.status = 'cancel',a.update_date = CONVERT(datetime, getdate(),121),a.update_by ='system'  from  " +
                    "(SELECT " +
                    "  sequence_no " +
                    " ,sequence_no_file_id " +
                    " ,status " +
                    " ,update_date " +
                    " ,update_by " +
                    " " +
                    " FROM maxar_fwd_dtc.[dbo].[sequence_no_file_detail] where sequence_no_file_id = :sequenceNoFileId) as a ," +
                    "(SELECT " +
                    "  sequence_no " +
                    " FROM maxar_fwd_dtc.[dbo].[sequence_no_file_detail] where sequence_no_file_id <> :sequenceNoFileId) as b " +
                    " where a.sequence_no = b.sequence_no and a.sequence_no_file_id = :sequenceNoFileId ");
            q.setParameter("sequenceNoFileId",sequenceNoFileId);
            q.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void edit(SequenceNoFile sequenceNoFile) throws NonexistentEntityException, Exception {
        em.merge(sequenceNoFile);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SequenceNoFile sequenceNoFile;
        try {
            sequenceNoFile = em.getReference(SequenceNoFile.class, id);
            sequenceNoFile.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sequenceNo with id " + id + " no longer exists.", enfe);
        }
        em.remove(sequenceNoFile);
    }

    public SequenceNoFile findSequenceNoFIle(Integer id) {
        return em.find(SequenceNoFile.class, id);
    }

    public List<SequenceNoFile> findSequenceNoFileEntities() {
        return findSequenceNoFileEntities(true, -1, -1);
    }

    public List<SequenceNoFile> findSequenceNoFileEntities(int maxResults, int firstResult) {
        return findSequenceNoFileEntities(false, maxResults, firstResult);
    }

    private List<SequenceNoFile> findSequenceNoFileEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SequenceNoFile as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<SequenceNoFile> findSequenceNoFileBySequenceNoId(Integer sequenceNoId){
        try{
            Query q =null;
            if(sequenceNoId != 0) {
                q = em.createQuery("select object(o) from SequenceNoFile as o join o.sequenceNo as s where o.enable = true and s.id =:sequenceNoId order by o.id");
                q.setParameter("sequenceNoId",sequenceNoId);
            }else{
                q = em.createQuery("select object(o) from SequenceNoFile as o join o.sequenceNo as s where o.enable = true and s.generateType = 'File Upload' order by o.id");
            }
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }

    }

    public Integer findTotalRecordBySequenceNoId(Integer sequenceNoId){
        try{
            Query q = null;
            if(sequenceNoId!=0) {
                q = em.createQuery("select sum(o.totalRecord) as total from SequenceNoFile as o join o.sequenceNo as s where o.enable = true and s.id =:sequenceNoId");
                q.setParameter("sequenceNoId", sequenceNoId);
            }else{
                q = em.createQuery("select sum(o.totalRecord) as total from SequenceNoFile as o join o.sequenceNo as s where o.enable = true");
            }
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }
    public Integer  findAvailableBySequenceNoId(Integer sequenceNoId){
        try{
            Query q = null;
            if(sequenceNoId!=0) {
                q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o " +
                        "join o.sequenceNoFile as snf " +
                        "join snf.sequenceNo as sn where UPPER(o.status) = 'Available' and snf.enable=true and sn.id =:sequenceNoId ");
                q.setParameter("sequenceNoId", sequenceNoId);
            }else{
                q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o " +
                        "join o.sequenceNoFile as snf " +
                        " where UPPER(o.status) = 'Available' and snf.enable=true");
            }
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }

    public Integer findTotalRecordBySequenceNoFileId(Integer sequenceNoFileId){
        try{
            Query q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o join o.sequenceNoFile as s where s.id =:sequenceNoFileId");
            q.setParameter("sequenceNoFileId",sequenceNoFileId);
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }

    public Integer findAvailableRecordBySequenceNoFileId(Integer sequenceNoFileId){
        try{
            Query q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o join o.sequenceNoFile as s where o.status = 'Available' and s.id =:sequenceNoFileId");
            q.setParameter("sequenceNoFileId",sequenceNoFileId);
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }

    public Integer findCancelRecordBySequenceNoFileId(Integer sequenceNoFileId){
        try{
            Query q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o join o.sequenceNoFile as s where o.status = 'Cancel' and s.id =:sequenceNoFileId");
            q.setParameter("sequenceNoFileId",sequenceNoFileId);
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }

    public Integer findUsedRecordBySequenceNoFileId(Integer sequenceNoFileId){
        try{
            Query q = em.createQuery("select count(o) as total from SequenceNoFileDetail as o join o.sequenceNoFile as s where o.status = 'Used' and s.id =:sequenceNoFileId");
            q.setParameter("sequenceNoFileId",sequenceNoFileId);
            return  ((Long) q.getSingleResult()).intValue();
        }catch(NullPointerException e){
            return 0;
        }

    }
}
