/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.mng;

import com.maxelyz.core.model.dao.ApprovalRuleDAO;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.QaTransDAO;
import com.maxelyz.core.model.dao.QaTransDetailDAO;
import com.maxelyz.core.model.dao.SubdistrictDAO;
import com.maxelyz.core.model.ndao.RegistrationTxDAO;
import com.maxelyz.core.service.NextSequenceService;
import com.maxelyz.core.service.NextSequenceServiceImpl;
import javax.faces.context.FacesContext;

/**
 *
 * @author DevTeam
 */
public class ContextResourceFinder {
    /************ CONSTANT - SERVICE *************/
    private final static String REGISTRATIONMDSERVICE = "#{registrationMasterDataService}";
    private final static String NEXTSEQSERVICE = "#{nextSequenceService}";
    /************ CONSTANT - DAO *************/
    private final static String PROVINCEDAO = "#{provinceDAO}";
    private final static String DISTRICTDAO = "#{districtDAO}";
    private final static String SUBDISTRICTDAO = "#{subdistrictDAO}";
    private final static String APPROVALRULEDAO ="#{approvalRuleDAO}";
    private final static String REGISTRATIONTXDAO = "#{registrationTxDAO}";
    private final static String PURCHASEORDERDAO = "#{purchaseOrderDAO}";
    private final static String ASSIGNMENTDETAILDAO = "#{assignmentDetailDAO}";
    private final static String CONTACTHISTORYDAO = "#{contactHistoryDAO}";
    private final static String QATRANSDAO ="#{qaTransDAO}";
    private final static String QATRANSDETAILDAO ="#{qaTransDetailDAO}";
    
    
    public static RegistrationMasterDataService getRegistrationMasterDataService(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        RegistrationMasterDataService service = fcontext.getApplication().evaluateExpressionGet(fcontext, REGISTRATIONMDSERVICE, RegistrationMasterDataServiceImp.class);
        return service;
    }
    
    public static NextSequenceService getNextSequenceService(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        NextSequenceService service = fcontext.getApplication().evaluateExpressionGet(fcontext, NEXTSEQSERVICE, NextSequenceServiceImpl.class);
        return service;
    }
    
    public static DistrictDAO getDistrictDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        DistrictDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, DISTRICTDAO, DistrictDAO.class);
        return dao;
    }
        
    public static ProvinceDAO getProvinceDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        ProvinceDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, PROVINCEDAO, ProvinceDAO.class);
        return dao;
    }
    
    public static SubdistrictDAO getSubDistrictDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        SubdistrictDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, SUBDISTRICTDAO, SubdistrictDAO.class);
        return dao;
    }
    
    public static ApprovalRuleDAO getApprovalRuleDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        ApprovalRuleDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, APPROVALRULEDAO, ApprovalRuleDAO.class);
        return dao;
    }

     public static RegistrationTxDAO getRegistrationTxDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        RegistrationTxDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, REGISTRATIONTXDAO, RegistrationTxDAO.class);
        return dao;
    }    
    
    public static PurchaseOrderDAO getPurchaseOrderDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        PurchaseOrderDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, PURCHASEORDERDAO, PurchaseOrderDAO.class);
        return dao;
    }
    
    public static AssignmentDetailDAO getAssignmentDetailDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        AssignmentDetailDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, ASSIGNMENTDETAILDAO, AssignmentDetailDAO.class);
        return dao;
    }

    public static ContactHistoryDAO getContactHistoryDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        ContactHistoryDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, CONTACTHISTORYDAO, ContactHistoryDAO.class);
        return dao;
    }
    public static QaTransDAO getQaTransDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        QaTransDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, QATRANSDAO, QaTransDAO.class);
        return dao;
    }
    
    public static QaTransDetailDAO getQaTransDetailDAO(){
        FacesContext  fcontext = FacesContext.getCurrentInstance();
        QaTransDetailDAO dao = fcontext.getApplication().evaluateExpressionGet(fcontext, QATRANSDETAILDAO, QaTransDetailDAO.class);
        return dao;  
    }

}
