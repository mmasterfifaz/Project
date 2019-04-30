/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.FileTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FileTemplateDAO {

    private static Logger log = Logger.getLogger(FileTemplateDAO.class);
    @PersistenceContext
    private EntityManager em;

    public void create(FileTemplate fileTemplate) {
        em.persist(fileTemplate);
    }

    public void edit(FileTemplate fileTemplate) {
        fileTemplate = em.merge(fileTemplate);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        FileTemplate fileTemplate;
        try {
            fileTemplate = em.getReference(FileTemplate.class, id);
            fileTemplate.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The fileTemplate with id " + id + " no longer exists.", enfe);
        }
        em.remove(fileTemplate);
    }

    public List<FileTemplate> findFileTemplateEntities() {
        return findFileTemplateEntities(true, -1, -1);
    }

    public List<FileTemplate> findFileTemplateEntities(int maxResults, int firstResult) {
        return findFileTemplateEntities(false, maxResults, firstResult);
    }

    private List<FileTemplate> findFileTemplateEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from FileTemplate as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public FileTemplate findFileTemplate(Integer id) {
        return em.find(FileTemplate.class, id);
    }

    public int getFileTemplateCount() {
        return ((Long) em.createQuery("select count(o) from FileTemplate as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getFileTemplateMap() {
        List<FileTemplate> fileTemplates = this.findFileTemplateEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (FileTemplate obj : fileTemplates) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public static String getFieldEnclosed(FileTemplate fileTemplate) {
        String fieldEnclosed;
        if (fileTemplate.getFieldEnclosed().equals("custom")) {
            fieldEnclosed = fileTemplate.getFieldEnclosedCustom();
        } else {
            if (fileTemplate.getFieldEnclosed().equals("none")) {
                fieldEnclosed = "";
            } else if (fileTemplate.getFieldEnclosed().equals("singlequote")) {
                fieldEnclosed = "'";
            } else if (fileTemplate.getFieldEnclosed().equals("doublequote")) {
                fieldEnclosed = "\"";
            } else {
                fieldEnclosed = "";
            }
        }
        return fieldEnclosed;
    }

    public static String getFieldDelimiter(FileTemplate fileTemplate) {
        String fieldDelimiter;
                if (fileTemplate.getFieldDelimiter().equals("custom")) {
            fieldDelimiter = fileTemplate.getFieldDelimiterCustom();
        } else {
            if (fileTemplate.getFieldDelimiter().equals("tab")) {
                fieldDelimiter = "\t";
            } else if (fileTemplate.getFieldDelimiter().equals("comma")) {
                fieldDelimiter = ",";
            } else if (fileTemplate.getFieldDelimiter().equals("semicolon")) {
                fieldDelimiter = ";";
            } else {
                fieldDelimiter = "";
            }
        }
        return fieldDelimiter;
    }

    public static List<String> readColumnNameFromFile(File file, FileTemplate fileTemplate) throws Exception {
        List<String> columnNames = new ArrayList<String>();
        String charset, fieldEnclosed, fieldDelimiter, rowDelimiter;
        if (fileTemplate.getUnicode()) {
            charset = "utf-8";
        } else {
            charset = "tis-620";
        }
       
        fieldEnclosed = getFieldEnclosed(fileTemplate);
        fieldDelimiter = getFieldDelimiter(fileTemplate);


        /*
        if (fileTemplate.getRowDelimiter().equals("custom")) {
        rowDelimiter = fileTemplate.getRowDelimiterCustom();
        } else {
        if (fileTemplate.getRowDelimiter().equals("windows")) {
        rowDelimiter = "\n\r";
        } else if (fileTemplate.getRowDelimiter().equals("mac")) {
        rowDelimiter = "\n";
        } else if (fileTemplate.getRowDelimiter().equals("unix")) {
        rowDelimiter = "\r";
        } else {
        rowDelimiter = "";
        }
        }
         */

        BufferedReader dataIns = null;
        try {
            dataIns = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), charset));
            String data, column;
            int i = 0;
            columnNames.clear();
            //Read only first line
            if ((data = dataIns.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(data, fieldDelimiter);
                //get Column Name
                while (st.hasMoreTokens()) {
                    column = st.nextToken();
                    if (fileTemplate.getHasColumnName()) {
                        if (!fieldEnclosed.equals("")) {
                            if (column.length() > 0) {
                                column = column.substring(1);
                            }
                            if (column.length() > 0) {
                                column = column.substring(0, column.length() - 1);
                            }
                        }
                        columnNames.add(column);
                    } else {
                        i++;
                        columnNames.add("Column" + i);
                    }
                }
            }

        } catch (Exception e) {
            //log.error(e.getMessage());
            throw e;
        } finally {
            try {
                if (dataIns != null) {
                    dataIns.close();
                }
            } catch (Exception e) {
                //log.error(e.getMessage());
            }
        }
        return columnNames;
    }
    
    public int checkFileTemplateName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from FileTemplate as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from FileTemplate as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
