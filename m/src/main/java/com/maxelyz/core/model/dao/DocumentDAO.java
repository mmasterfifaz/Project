/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Document;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.xml.bind.ParseConversionEvent;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DocumentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Document document) {
        em.persist(document);
    }

    public void edit(Document document) throws NonexistentEntityException, Exception {
        document = em.merge(document);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Document document;
        try {
            document = em.getReference(Document.class, id);
            document.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The document with id " + id + " no longer exists.", enfe);
        }
        em.remove(document);
    }

    public List<Document> findDocumentEntities() {
        return findDocumentEntities(true, -1, -1);
    }

    public List<Document> findDocumentEntities(int maxResults, int firstResult) {
        return findDocumentEntities(false, maxResults, firstResult);
    }

    private List<Document> findDocumentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Document as o where o.enable = true order by o.title");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Document findDocument(Integer id) {
        return em.find(Document.class, id);
    }

    public List<Document> findDocumentBySearch(String title, String fileName, Date dateFrom, Date dateTo, Integer userGroupId, Integer documentUploadTypeId, String status) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        List<Document> list = new ArrayList<Document>();
        String sql = ("select object(o) from Document as o where o.enable = true ");
        if (title != null && !title.isEmpty()) {
            sql += " and o.title like '%" + title + "%' ";
        }

        if (fileName != null && !fileName.isEmpty()) {
            sql += " and o.fileName like '%" + fileName + "%' ";
        }

        if (dateFrom != null && dateTo != null) {
            sql += " and o.updateDate between '" + sdfFrom.format(dateFrom) + "' and '" + sdfTo.format(dateTo) + "'";
        }

        if (userGroupId != null && userGroupId != 0) {
            sql += " and o.userGroup.id = " + userGroupId + " ";
        }

        if (documentUploadTypeId != null && documentUploadTypeId != 0) {
            sql += " and o.documentUploadType.id = " + documentUploadTypeId + " ";
        }

        if (status != null && !status.isEmpty()) {
            if (status.equals("enable")) {
                sql += " and o.status = true ";
            } else if (status.equals("disable")) {
                sql += " and o.status = false ";
            }
        }
        Query q = em.createQuery(sql);
        list = (List<Document>) q.getResultList();
        return list;
    }

    public int getDocumentCount() {
        Query q = em.createQuery("select count(o) from Document as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<Document> getMergeDocument(Integer productid, Integer usergroupid) {
        List<Document> list = null;

        Query q = em.createNativeQuery("SELECT distinct dbo.[document].*\n" +
                "FROM  dbo.[document] \n" +
                "INNER JOIN dbo.product_document ON dbo.[document].id = dbo.product_document.document_id \n" +
                "--INNER JOIN dbo.document_upload_type ON dbo.[document].type_id = dbo.document_upload_type.id\n" +
                "where dbo.product_document.product_id =:productid and \n" +
                "dbo.[document].assign_to_user_group_id =:usergroupid and \n" +
                "dbo.[document].status = 1 and dbo.[document].enable = 1 and\n" +
                "dbo.product_document.status = 1 and dbo.product_document.enable = 1 and\n" +
                "dbo.product_document.purchase_order_status = 'approved'", Document.class);
//        List<Object[]> l = q.getResultList();
        q.setParameter("productid", productid);
        q.setParameter("usergroupid", usergroupid);
        list = (List<Document>) q.getResultList();
        return list;

    }

    public String findFileNamebyDocumentId(Integer id) {
        Query q = em.createQuery("Select distinct o.fileName from Document as o where o.id =:id and o.enable = true");
        q.setParameter("id", id);

        return ((String) q.getSingleResult());
    }

    public int checkDocumentTitle(String title, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from Document as o where o.title =:title and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from Document as o where o.title =:title and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("title", title);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Document> getAvailableMergeDocument() {
        List<Document> mlist = null;

        Query statement = em.createQuery("select object(o) from Document o where o.status = true and o.enable = true");
        mlist = (List<Document>) statement.getResultList();

        return mlist;
    }

    public List<Document> getMergeDocumentByRefNumber(String listRefNumber) {
        List<Document> mergeDocumentByRefNumber = null;

        //create query get merge document list with JPA style
        Query statement = em.createQuery("select object(d)"
                + " from Document d, PurchaseOrder po, ProductDocument doc, PurchaseOrderDetail pod"
                + " where pod.purchaseOrder = po.id and doc.product = pod.product and doc.document = d.id and po.refNo = :listRefNumber and doc.enable = 'true'");

        statement.setParameter("listRefNumber", listRefNumber);
        mergeDocumentByRefNumber = (List<Document>) statement.getResultList();

        return mergeDocumentByRefNumber;
    }   
}
