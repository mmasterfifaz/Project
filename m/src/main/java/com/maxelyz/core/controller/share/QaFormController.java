/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;

import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import static com.maxelyz.core.common.front.dispatch.RegistrationPouch.RECEIVER_MODE.COPY;
import static com.maxelyz.core.common.front.dispatch.RegistrationPouch.RECEIVER_MODE.EDIT;
import static com.maxelyz.core.common.front.dispatch.RegistrationPouch.RECEIVER_MODE.NEW;
import static com.maxelyz.core.common.front.dispatch.RegistrationPouch.SENDER_TYPE.SALEAPPROACHING;
import static com.maxelyz.core.common.front.dispatch.RegistrationPouch.SENDER_TYPE.SALEAPPROVAL;
import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.controller.front.customerHandling.RegistrationPoController;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.RegistrationFormDAO;
import com.maxelyz.core.model.dao.QaTransDAO;
import com.maxelyz.core.model.dao.QaTransDetailDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.QuestionnaireService;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.maxelyz.core.model.value.front.customerHandling.QaFormInfoValue;
import java.util.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class QaFormController {
    private static final Logger log = Logger.getLogger(QaFormController.class);
    
    private QaForm qaForm;
    private List<QaSelectedCategory> qaSelectedCategoryList;
    private List<QaQuestion> qaQuestionList;
    private List<QaChoice> qaChoiceList;
    
    private QaForm qcQaForm;
    private List<QaSelectedCategory> qcQaSelectedCategoryList;
    private List<QaQuestion> qcQaQuestionList;
    private List<QaChoice> qcQaChoiceList;
    
    private String fromPage ="";
    private boolean fromSaleApprovalPage=false;
    private boolean showPrevious = true;
    //private Integer poId;
    //private Integer choiceId;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    private String answer, remark, mode;
    
    public QaTrans qaTrans;
    public QaTransDetail qaTransDetail;
    private List<QaFormInfoValue> qaFormValueList;
    private List<QaTransDetail> qaTransDetailsList;
    
    public QaTrans qcQaTrans;
    public QaTransDetail qcQaTransDetail;
    private List<QaFormInfoValue> qcQaFormValueList;
    private List<QaTransDetail> qcQaTransDetailsList;
            
    private SelectItem answerSelectItems[];
    private String[] answerCheckbox;
    private RegistrationForm registrationForm;
    private Product product;
    
    private QaTrans qaTrans4Save = null;
    private boolean overlimitRemarkChar = false;
    
    @ManagedProperty(value = "#{questionnaireService}")
    private QuestionnaireService questionnaireService;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{qaTransDAO}")
    private QaTransDAO qaTransDAO;
    @ManagedProperty(value = "#{qaTransDetailDAO}")
    private QaTransDetailDAO qaTransDetailDAO;
    
    @PostConstruct
    public void initialize() {
        this.toRegistrationFormPo();
    }
    
    public void toRegistrationFormPo() {
        RegistrationPouch pouch = FrontDispatcher.pickupRegistrationPouch4Child();
        if (pouch != null) {
            switch (pouch.getReceiverMode()) {
                case NEW:
                case COPY:
                    this.mode = "add";
                    break;
                case EDIT:
                    this.mode = "edit";
                    break;  
            }
            this.purchaseOrder = pouch.getPuchaseOrder4Child();
            this.qaTrans4Save = pouch.getQaTrans4CollectData();
            RegistrationLogging.logInfo("QaForm Initial data from Pouch = "+pouch+", purchaseOrder="+this.purchaseOrder+", qaTrans4Save="+this.qaTrans4Save);
            
        } else {
            FacesContext fcontext = FacesContext.getCurrentInstance();
            RegistrationPoController parentController = fcontext.getApplication().evaluateExpressionGet(fcontext, "#{registrationPoController}", RegistrationPoController.class);
            
            if (parentController != null && parentController.getPurchaseOrder() != null){
                this.mode = ( parentController.getPurchaseOrder().getId() == null )?"add":"edit";
                this.purchaseOrder = parentController.getPurchaseOrder();
                this.qaTrans4Save = parentController.getQaTrans4Save();
            }
            RegistrationLogging.logInfo("QaForm Initial data from RegistrationController = "+parentController+", purchaseOrder="+this.purchaseOrder+", qaTrans4Save="+this.qaTrans4Save);
        
        } 
        
        
        if (purchaseOrder != null) {
            List<PurchaseOrderDetail> pDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
            purchaseOrderDetail = pDetails.get(0);
            product = purchaseOrderDetail.getProduct();
            registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(product.getId());
            if (mode.equals("edit")) {
                this.editQaFormAnswer();
                this.editQcQaFormAnswer();
            } else if (registrationForm.getQaForm() != null) {
                this.setQaForm(registrationForm.getQaForm().getId());
                this.setQcQaForm(registrationForm.getQcQaForm() != null && registrationForm.getQcQaForm().getId() != null ? registrationForm.getQcQaForm().getId():null);
            }
        }
        this.overlimitRemarkChar = false;
    }  
    
               
    public void setQaForm(Integer id) {
        if (id == null) {
            return;
        }
        qaForm = questionnaireService.findQaForm(id);
        qaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qaForm);
        qaFormValueList = new ArrayList<QaFormInfoValue>();
        for(QaSelectedCategory o: qaSelectedCategoryList){
            qaQuestionList = questionnaireService.findQaQuestionByCategoryId(o.getQaCategory());
            for(QaQuestion q: qaQuestionList){
                qaChoiceList = questionnaireService.findQaChoiceByQuestionId(q);
                QaFormInfoValue qaFormInfo = new QaFormInfoValue(q, qaChoiceList, answer, remark, answerCheckbox);
                qaFormValueList.add(qaFormInfo);
            }
        }
    }
    
    public void setQcQaForm(Integer id) {
        if (id == null) {
            return;
        }
        qcQaForm = questionnaireService.findQaForm(id);
        qcQaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qcQaForm);
        qcQaFormValueList = new ArrayList<QaFormInfoValue>();
        for(QaSelectedCategory o: qcQaSelectedCategoryList){
            qcQaQuestionList = questionnaireService.findQaQuestionByCategoryId(o.getQaCategory());
            for(QaQuestion q: qcQaQuestionList){
                qcQaChoiceList = questionnaireService.findQaChoiceByQuestionId(q);
                QaFormInfoValue qcQaFormInfoValue = new QaFormInfoValue(q, qcQaChoiceList, answer, remark, answerCheckbox);
                qcQaFormValueList.add(qcQaFormInfoValue);
            }
        }
    }
    
    public void editQaFormAnswer() {
        if(purchaseOrder != null){
            qaTrans = purchaseOrder.getQaTransApproval();
            qaTransDetailsList = qaTransDetailDAO.findQaTransDetailByQaTransId(qaTrans);
        }
        if(qaTrans != null) {
            qaForm = questionnaireService.findQaForm(qaTrans.getQaFormId());
            qaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qaForm);
            qaFormValueList = new ArrayList<QaFormInfoValue>();
            int i = 0;
            for(QaSelectedCategory o: qaSelectedCategoryList){
                qaQuestionList = questionnaireService.findQaQuestionByCategoryId(o.getQaCategory());
                for(QaQuestion q: qaQuestionList){
                    qaChoiceList = questionnaireService.findQaChoiceByQuestionId(q);
                    QaFormInfoValue qaFormInfo = new QaFormInfoValue(q, qaChoiceList, answer, remark, answerCheckbox);

                    // set answer
                    if(!qaTransDetailsList.isEmpty() && qaTransDetailsList != null) {
                        QaTransDetail d = qaTransDetailsList.get(i); 
                        String[] tempAnswer = null;
                        if(q.getType().equals("checkbox")) {
                            tempAnswer = d.getAnswer().split(",");
                            qaFormInfo.setAnswerCheckbox(tempAnswer);
                        } else
                            qaFormInfo.setAnswer(d.getAnswer());

                        qaFormInfo.setRemark(d.getRemark());
                    }
                    qaFormValueList.add(qaFormInfo);
                    i++;
                }
            }
        } else if(qaTrans == null && registrationForm.getQaForm()!=null ) {
            this.setQaForm(registrationForm.getQaForm().getId());
        }
    }
    
    public void editQcQaFormAnswer() {
        if(purchaseOrder != null){
            qcQaTrans = purchaseOrder.getQaTransQc();
            qcQaTransDetailsList = qaTransDetailDAO.findQaTransDetailByQaTransId(qcQaTrans);
        }
        if(qcQaTrans != null) {
            qcQaForm = questionnaireService.findQaForm(qcQaTrans.getQaFormId());
            qcQaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qcQaForm);
            qcQaFormValueList = new ArrayList<QaFormInfoValue>();
            int i = 0;
            for(QaSelectedCategory o: qcQaSelectedCategoryList){
                qcQaQuestionList = questionnaireService.findQaQuestionByCategoryId(o.getQaCategory());
                for(QaQuestion q: qcQaQuestionList){
                    qcQaChoiceList = questionnaireService.findQaChoiceByQuestionId(q);
                    QaFormInfoValue qaFormInfoValue = new QaFormInfoValue(q, qcQaChoiceList, answer, remark, answerCheckbox);

                    // set answer
                    if(!qcQaTransDetailsList.isEmpty() && qcQaTransDetailsList != null) {
                        QaTransDetail qaTransDetail = qcQaTransDetailsList.get(i); 
                        String[] tempAnswer = null;
                        if(q.getType().equals("checkbox")) {
                            tempAnswer = qaTransDetail.getAnswer().split(",");
                            qaFormInfoValue.setAnswerCheckbox(tempAnswer);
                        } else
                            qaFormInfoValue.setAnswer(qaTransDetail.getAnswer());

                        qaFormInfoValue.setRemark(qaTransDetail.getRemark());
                    }
                    qcQaFormValueList.add(qaFormInfoValue);
                    i++;
                }
            }
        } else if(qcQaTrans == null && registrationForm.getQcQaForm()!=null ) {
            this.setQcQaForm(registrationForm.getQcQaForm().getId());
        }
    }
        
    public String convertAnswerCheckbox(String[] answerCheckbox) {
        String answer = "";
        for (int i=0;i < answerCheckbox.length;i++) {
            answer += answerCheckbox[i];
            if ((answerCheckbox.length - 1) > i) {
                answer += ",";
            }
        }
        return answer;
    }
    
    public String convertAnswerDescription(String[] answerCheckbox, SelectItem[] answerSelectItems) {
        String answerDesc = "";
         for (int i=0;i < answerCheckbox.length;i++) {
            for(int j=0;j < answerSelectItems.length;j++) {
                if(answerSelectItems[j].getValue().toString().equals(answerCheckbox[i])) {
                    answerDesc += answerSelectItems[j].getLabel() + ",";
                }
            }
        }
        if(answerDesc.length()>0)
            answerDesc = answerDesc.substring(0, answerDesc.length() - 1);
        return answerDesc;
    }
    
    public String convertAnswerDesc(String answerValue, SelectItem[] answerSelectItems) {
        String answerDesc = "";
        for(int j=0;j < answerSelectItems.length;j++) {
            if(answerSelectItems[j].getValue().toString().equals(answerValue)) {
                answerDesc += answerSelectItems[j].getLabel() + ",";
            }
        }
        if(answerDesc.length()>0)
            answerDesc = answerDesc.substring(0, answerDesc.length() - 1);
        return answerDesc;
    }
        
    public void saveQaTrans() {
        if (qaTrans4Save == null ) saveQaTransOldFashion();
        else transferData2QaTrans4Save();
    }
    
    private void saveQaTransOldFashion(){
        if(qaForm != null){
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            if(qaTrans != null){
                qaTransDetailDAO.deleteQaTransDetails(qaTrans);
            } else  if(mode.equals("add") || qaTrans == null) {
                qaTrans = new QaTrans();
                qaTrans.setCreateBy(username);
                qaTrans.setCreateDate(now);
                qaTrans.setQaFormId(qaForm.getId());
                qaTrans.setQaFormName(qaForm.getName());
                qaTransDAO.create(qaTrans);
            }
            int i=0;
            for(QaFormInfoValue q: qaFormValueList) {
                i++;
                this.qaTransDetail = createQaTransDetail(q);
                qaTransDetail.setSeqNo(i);
                qaTransDetail.setQaTrans(qaTrans); // for save
                qaTransDetail.setCreateBy(username);
                qaTransDetail.setCreateDate(now);  
                this.getQaTransDetailDAO().create(qaTransDetail);
            }
            JSFUtil.getRequest().setAttribute("qaTrans", qaTrans);
        }
    }

    private void transferData2QaTrans4Save(){
        if (qaForm != null) {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            if (qaTrans4Save.getId() == null) {
                qaTrans4Save.setCreateBy(username);
                qaTrans4Save.setCreateDate(now);
                qaTrans4Save.setQaFormId(qaForm.getId());
                qaTrans4Save.setQaFormName(qaForm.getName());
            }
            qaTrans4Save.getQaTransDetailCollection().clear(); // clear before add new list of qa trasn
            int i = 0;
            for (QaFormInfoValue q : qaFormValueList) {
                i++;
                QaTransDetail newQaTrans = createQaTransDetail(q);
                newQaTrans.setSeqNo(i);
                newQaTrans.setQaTrans(qaTrans4Save); // for save
                newQaTrans.setCreateBy(username);
                newQaTrans.setCreateDate(now);
                qaTrans4Save.getQaTransDetailCollection().add(newQaTrans);
            }
        }       
    }
    
    public void saveQcQaTrans() {
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        if (qcQaTrans == null ) {
            if(qcQaForm != null){            
                qcQaTrans = new QaTrans();
                qcQaTrans.setCreateBy(username);
                qcQaTrans.setCreateDate(now);
                qcQaTrans.setQaFormId(qcQaForm.getId());
                qcQaTrans.setQaFormName(qcQaForm.getName());
                qaTransDAO.create(qcQaTrans);
                
                int i=0;
                for(QaFormInfoValue q: qcQaFormValueList) {
                    i++;
                    this.qcQaTransDetail = createQaTransDetail(q);
                    qcQaTransDetail.setSeqNo(i);
                    qcQaTransDetail.setQaTrans(qcQaTrans); // for save
                    qcQaTransDetail.setCreateBy(username);
                    qcQaTransDetail.setCreateDate(now);  
                    this.getQaTransDetailDAO().create(qcQaTransDetail);
                }
                purchaseOrder.setQaTransQc(qcQaTrans);
            }
        }else{
            qaTransDetailDAO.deleteQaTransDetails(qcQaTrans);
            int i=0;
            for(QaFormInfoValue q: qcQaFormValueList) {
                i++;
                this.qcQaTransDetail = createQaTransDetail(q);
                qcQaTransDetail.setSeqNo(i);
                qcQaTransDetail.setQaTrans(qcQaTrans); // for save
                qcQaTransDetail.setCreateBy(username);
                qcQaTransDetail.setCreateDate(new Date());  
                this.getQaTransDetailDAO().create(qcQaTransDetail);
            }
        }
        try { 
            purchaseOrderDAO.updateQaTransQc(purchaseOrder);
        }catch (Exception e) {
            log.error(e);
        }
    }
    
    private QaTransDetail createQaTransDetail(QaFormInfoValue q){
        QaTransDetail qaTransDetail = new QaTransDetail();
        qaTransDetail.setQaCategoryName(q.getQaQuestion().getQaCategory().getName());
        qaTransDetail.setQaCategory(q.getQaQuestion().getQaCategory());
        qaTransDetail.setQuestionName(q.getQaQuestion().getName());
        qaTransDetail.setQuestionCode(q.getQaQuestion().getCode());
        if(q.getQaQuestion().getShowNa()) {
            qaTransDetail.setNa("1");
        } else {
            qaTransDetail.setNa("0");
        }
        if(q.getQaQuestion().getShowRemark()) {
            if(q.getRemark() != null)
            qaTransDetail.setRemark(JSFUtil.removeEnter(q.getRemark()));
        } else {
            qaTransDetail.setRemark("");
        }
        if(q.getQaQuestion().getType().equals("textfield") || q.getQaQuestion().getType().equals("radio") || q.getQaQuestion().getType().equals("dropdown")) {   
            qaTransDetail.setAnswer(q.getAnswer());
            if(q.getQaQuestion().getType().equals("textfield")) {
                qaTransDetail.setAnswerDesc(q.getAnswer());
            } else if(q.getQaQuestion().getType().equals("radio") || q.getQaQuestion().getType().equals("dropdown")) {
                qaTransDetail.setAnswerDesc(convertAnswerDesc(q.getAnswer(),q.getAnswerSelectItems()));
            }
        } else if(q.getQaQuestion().getType().equals("checkbox")) {
            qaTransDetail.setAnswer(convertAnswerCheckbox(q.getAnswerCheckbox()));
            qaTransDetail.setAnswerDesc(convertAnswerDescription(q.getAnswerCheckbox(),q.getAnswerSelectItems()));
        }
        return qaTransDetail;
        
    }    
        
    //Managed Properties
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }
    
    public QaTransDAO getQaTransDAO() {
        return qaTransDAO;
    }

    public void setQaTransDAO(QaTransDAO qaTransDAO) {
        this.qaTransDAO = qaTransDAO;
    }

    public QaTransDetailDAO getQaTransDetailDAO() {
        return qaTransDetailDAO;
    }

    public void setQaTransDetailDAO(QaTransDetailDAO qaTransDetailDAO) {
        this.qaTransDetailDAO = qaTransDetailDAO;
    }
    
    //List to UI
    public QaForm getQaForm() {
        return qaForm;
    }

    public void setQaForm(QaForm qaForm) {
        this.qaForm = qaForm;
    }
    
    public List<QaSelectedCategory> getQaSelectedCategoryList() {
        return qaSelectedCategoryList;
    }

    public void setQaSelectedCategory(List<QaSelectedCategory> qaSelectedCategoryList) {
        this.qaSelectedCategoryList = qaSelectedCategoryList;
    }

    public List<QaQuestion> getQaQuestionList() {
        return qaQuestionList;
    }

    public void setQaQuestionList(List<QaQuestion> qaQuestionList) {
        this.qaQuestionList = qaQuestionList;
    }
  
    public List<QaFormInfoValue> getQaFormValueList() {
        return qaFormValueList;
    }

    public void setQaFormValueList(List<QaFormInfoValue> qaFormValueList) {
        this.qaFormValueList = qaFormValueList;
    }
    
    public List<QaChoice> getQaChoiceList() {
        return qaChoiceList;
    }

    public void setQaChoiceList(List<QaChoice> qaChoiceList) {
        this.qaChoiceList = qaChoiceList;
    }
    
    public QaForm getQCQaForm() {
        return qcQaForm;
    }

    public void setQcQaForm(QaForm qcQaForm) {
        this.qcQaForm = qcQaForm;
    }
    
    public List<QaSelectedCategory> getQcQaSelectedCategoryList() {
        return qcQaSelectedCategoryList;
    }

    public void setQcQaSelectedCategory(List<QaSelectedCategory> qcQaSelectedCategoryList) {
        this.qcQaSelectedCategoryList = qcQaSelectedCategoryList;
    }

    public List<QaQuestion> getQcQaQuestionList() {
        return qcQaQuestionList;
    }

    public void setQcQaQuestionList(List<QaQuestion> qcQaQuestionList) {
        this.qcQaQuestionList = qcQaQuestionList;
    }
  
    public List<QaFormInfoValue> getQcQaFormValueList() {
        return qcQaFormValueList;
    }

    public void setQcQaFormValueList(List<QaFormInfoValue> qcQaFormValueList) {
        this.qcQaFormValueList = qcQaFormValueList;
    }
    
    public List<QaChoice> getQcQaChoiceList() {
        return qcQaChoiceList;
    }

    public void setQcQaChoiceList(List<QaChoice> qcQaChoiceList) {
        this.qcQaChoiceList = qcQaChoiceList;
    }

    public SelectItem[] getAnswerSelectItems() {
        return answerSelectItems;
    }

    public void setAnswerSelectItems(SelectItem[] answerSelectItems) {
        this.answerSelectItems = answerSelectItems;
    }

    public boolean isOverlimitRemarkChar() {
        return overlimitRemarkChar;
    }
    
}
