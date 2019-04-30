package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.FollowupsaleReasonDAO;
import com.maxelyz.core.model.dao.NosaleReasonDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.RegistrationFormDAO;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.Questionnaire;
import com.maxelyz.core.model.entity.QuestionnaireDetail;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.RegistrationForm;
import com.maxelyz.core.model.entity.TempPurchaseOrder;
import com.maxelyz.core.model.entity.TempPurchaseOrderDetail;
import com.maxelyz.core.model.entity.TempPurchaseOrderQuestionaire;
import com.maxelyz.core.model.entity.TempPurchaseOrderRegister;
import com.maxelyz.core.model.value.front.customerHandling.FlexFieldInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.QuestionnaireInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.RegistrationInfoValue;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.faces.bean.ViewScoped;
import org.richfaces.event.CurrentDateChangeEvent;

@ManagedBean
@RequestScoped
@ViewScoped
public abstract class RegistrationAbstractController<E_PO, E_PO_DETAIL, E_QUESTIONNAIRE > {
/*
    private static Logger log = Logger.getLogger(RegistrationAbstractController.class);

    private Date defaultDob = null;
    private Product product;
    private RegistrationForm registrationForm;
    private E_PO purchaseOrder;
    private E_PO_DETAIL purchaseOrderDetail;
    private Map<String, Integer> provinceList;
    private List<QuestionnaireInfoValue<E_QUESTIONNAIRE>> questionnaires;
    private List<RegistrationInfoValue> regInfo = new ArrayList<RegistrationInfoValue>();
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private String message;
    private Boolean disableSaveButton=false;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;    

    
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    
    @PostConstruct
    public void initialize() {
        try {
            Integer tempPoId = (Integer) JSFUtil.getRequest().getAttribute("tempPurchaseOrderId");
            Integer productId = (Integer) JSFUtil.getRequest().getAttribute("productId");
            purchaseOrder = this.getPurchaseOrderDAO().findTempPurchaseOrder(tempPoId);
            purchaseOrderDetail = this.getPurchaseOrderDAO().findTempPurchaseOrderDetailByProduct(tempPoId, productId);
            product = this.getProductDAO().findProduct(productId);
            if (purchaseOrder == null || purchaseOrderDetail == null || product == null) {
                throw new Exception("No Data Found, purchaseOrder or purchaseOrderDetail or product is null");
            }


        } catch (Exception e) {
            log.error(e);
            JSFUtil.redirect(getRedirectPage());
            return;
        } finally {
            JSFUtil.getRequest().removeAttribute("tempPurchaseOrderId");
            JSFUtil.getRequest().removeAttribute("productId");
        }

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1);  //year, month, day
        defaultDob = c.getTime();

        int productId = purchaseOrderDetail.getProduct().getId();
        registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(productId);
        nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
        followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

        this.setProviceList();
        this.setRegistrationForm();
        this.setQuestionnares();
    }
    public abstract String getRedirectPage();

    public abstract String editAction();

    public abstract String backAction();

    private void setPoFlexField(E_PO poReg, List<FlexFieldInfoValue> fxInfo) {
        for (FlexFieldInfoValue fx : fxInfo) {
            int no = fx.getNo();
            switch (no) {
                case 1:
                    poReg.setFx1(fx.getPoFlexField());
                    break;
                case 2:
                    poReg.setFx2(fx.getPoFlexField());
                    break;
                case 3:
                    poReg.setFx3(fx.getPoFlexField());
                    break;
                case 4:
                    poReg.setFx4(fx.getPoFlexField());
                    break;
                case 5:
                    poReg.setFx5(fx.getPoFlexField());
                    break;
                case 6:
                    poReg.setFx6(fx.getPoFlexField());
                    break;
                case 7:
                    poReg.setFx7(fx.getPoFlexField());
                    break;
                case 8:
                    poReg.setFx8(fx.getPoFlexField());
                    break;
                case 9:
                    poReg.setFx9(fx.getPoFlexField());
                    break;
                case 10:
                    poReg.setFx10(fx.getPoFlexField());
                    break;
                case 11:
                    poReg.setFx11(fx.getPoFlexField());
                    break;
                case 12:
                    poReg.setFx12(fx.getPoFlexField());
                    break;
                case 13:
                    poReg.setFx13(fx.getPoFlexField());
                    break;
                case 14:
                    poReg.setFx14(fx.getPoFlexField());
                    break;
                case 15:
                    poReg.setFx15(fx.getPoFlexField());
                    break;
                case 16:
                    poReg.setFx16(fx.getPoFlexField());
                    break;
                case 17:
                    poReg.setFx17(fx.getPoFlexField());
                    break;
                case 18:
                    poReg.setFx18(fx.getPoFlexField());
                    break;
                case 19:
                    poReg.setFx19(fx.getPoFlexField());
                    break;
                case 20:
                    poReg.setFx20(fx.getPoFlexField());
                    break;
            }
        }
    }

    private void setNoUseDistrict(TempPurchaseOrderRegister poReg) {
        if (poReg.getCurrentDistrict() != null) {
            if (poReg.getCurrentDistrict().getId() == null) {
                poReg.setCurrentDistrict(null);
            }
        }
        if (poReg.getHomeDistrict() != null) {
            if (poReg.getHomeDistrict().getId() == null) {
                poReg.setHomeDistrict(null);
            }
        }
        if (poReg.getBillingDistrict() != null) {
            if (poReg.getBillingDistrict().getId() == null) {
                poReg.setBillingDistrict(null);
            }
        }
        if (poReg.getShippingDistrict() != null) {
            if (poReg.getShippingDistrict().getId() == null) {
                poReg.setShippingDistrict(null);
            }
        }
    }
     private void recoverDistrict(TempPurchaseOrderRegister poReg) {
        if (poReg.getCurrentDistrict()== null) {
            poReg.setCurrentDistrict(new District());
        }
        if (poReg.getHomeDistrict()== null) {
            poReg.setHomeDistrict(new District());
        }
        if (poReg.getBillingDistrict() == null) {
            poReg.setBillingDistrict(new District());
        }
        if (poReg.getShippingDistrict() == null) {
            poReg.setShippingDistrict(new District());
        }
    }


    public String saveAction() {
        int no = 0;      
        List<TempPurchaseOrderRegister> regDetail = new ArrayList<TempPurchaseOrderRegister>();
        List<TempPurchaseOrderQuestionaire> poQuestionaire = new ArrayList<TempPurchaseOrderQuestionaire>();
        //Registration
        for (RegistrationInfoValue reg : regInfo) {
            TempPurchaseOrderRegister poReg = reg.getPo();
            poReg.setTempPurchaseOrderDetail(purchaseOrderDetail);
            this.setNoUseDistrict(poReg);

            List<FlexFieldInfoValue> fxInfo = reg.getFlexFields();
            this.setPoFlexField(poReg, fxInfo);
            poReg.setNo(++no);
            regDetail.add(poReg);
        }
        //Questionaire
        int i=0;
        for (QuestionnaireInfoValue<TempPurchaseOrderQuestionaire> q : questionnaires) {
            TempPurchaseOrderQuestionaire poq = q.getPurchaseOrderQuestionaire();
            String question = q.getQuestion();
            SelectItem[] choice = q.getAnswerSelectItems();
            Integer answerChoice = poq.getAnswerChoice();
            if (answerChoice!=null) {
                String answer = choice[answerChoice-1].getLabel();
                poq.setAnswer(answer);
            } 
            poq.setSeqNo(++i);
            poq.setQuestion(question);
            poq.setTempPurchaseOrderDetail(purchaseOrderDetail);
            poQuestionaire.add(poq);
        }
        if (no > 0) {
            purchaseOrderDetail.setTempPurchaseOrderRegisterCollection(regDetail);
            if (poQuestionaire.size() > 0) {
                purchaseOrderDetail.setTempPurchaseOrderQuestionaireCollection(poQuestionaire);
            }
            try {
                purchaseOrder.setNosaleReason(nosaleReasonId != null && nosaleReasonId > 0 ? new NosaleReason(nosaleReasonId) : null);
                purchaseOrder.setFollowupsaleReason(followupsaleReasonId != null && followupsaleReasonId > 0 ? new FollowupsaleReason(followupsaleReasonId) : null);
                List<TempPurchaseOrderDetail> poDetails = new ArrayList<TempPurchaseOrderDetail>();
                poDetails.add(purchaseOrderDetail);
                purchaseOrder.setTempPurchaseOrderDetailCollection(poDetails);
                this.getPurchaseOrderDAO().editTemp(purchaseOrder);

                JSFUtil.getRequest().setAttribute("tempPurchaseOrderId", purchaseOrder.getId());
                JSFUtil.getRequest().setAttribute("productId", product.getId());
                return SUCCESS;
            } catch (Exception e) {
                
                for (RegistrationInfoValue reg : regInfo) {
                   TempPurchaseOrderRegister poReg = reg.getPo();
                   this.recoverDistrict(poReg);
                }    
                message = e.getMessage();
                log.error(e);
            }
        }
        return null;
    }

    public void setRegistrationForm() {
        List<RegistrationField> registrationFields = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (int i = 0; i < 6; i++) {
            try {
                TempPurchaseOrderRegister tpor = null;
                if(purchaseOrderDetail.getTempPurchaseOrderRegisterCollection() != null){
                    for(TempPurchaseOrderRegister tt : purchaseOrderDetail.getTempPurchaseOrderRegisterCollection()){
                        if((i+1) == tt.getNo()){
                            tpor = tt;
                        }
                    }
                }
                RegistrationInfoValue item = new RegistrationInfoValue(registrationForm, registrationFields, i + 1, districtDAO, tpor);
                
                item.getPo().setDob(defaultDob);
                item.setAge(defaultDob);
                regInfo.add(item);
              
            } catch (Exception e) {
                //no need recovery process
            }
        }
    }

    public void setQuestionnares() {
        Questionnaire questionnaire = registrationForm.getQuestionnaire();
        if (questionnaire == null) {
            return;
        }
        List<QuestionnaireDetail> questionnaireDetails = (List<QuestionnaireDetail>) questionnaire.getQuestionnaireDetailCollection();
        questionnaires = new ArrayList<QuestionnaireInfoValue<TempPurchaseOrderQuestionaire>>();
        
        for (QuestionnaireDetail q : questionnaireDetails) {
            TempPurchaseOrderQuestionaire tpoq = null;
            if (purchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection() != null) {
                for(TempPurchaseOrderQuestionaire tq : purchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection()){
                    if(q.getNo() == tq.getSeqNo()){
                        tpoq = tq;
                        break;
                    }else{
                        tpoq =  null;
                    }
                }
            }
            if(tpoq == null) tpoq = new TempPurchaseOrderQuestionaire();
            QuestionnaireInfoValue<TempPurchaseOrderQuestionaire> item = new QuestionnaireInfoValue<TempPurchaseOrderQuestionaire>(questionnaire, q, tpoq);
            questionnaires.add(item);
        }
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public List<RegistrationInfoValue> getRegInfo() {
        return regInfo;
    }

    public void setProviceList() {
        provinceList = getProvinceDAO().getProvinceList();
    }

    public Map<String, Integer> getProvinceList() {
        return provinceList;

    }

    public Boolean getDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(Boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }


    public TempPurchaseOrder getTempPurchaseOrder() {
        return purchaseOrder;
    }

    public void setTempPurchaseOrder(TempPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getNosaleReasonId() {
        return nosaleReasonId;
    }

    public void setNosaleReasonId(Integer nosaleReasonId) {
        this.nosaleReasonId = nosaleReasonId;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
    }
    
    //List
    public List<SelectItem> getDistrictList(Integer provinceId) {
        List<District> districtList = getDistrictDAO().findDistrictByProvinceId(provinceId);
        List<SelectItem> values = new ArrayList<SelectItem>();
        values.add(new SelectItem(null, JSFUtil.getBundleValue("pleaseselect")));
        for (District obj : districtList) {
            values.add(new SelectItem(obj.getId(), obj.getName()));
        }
        return values;
    }
    
    public List<QuestionnaireInfoValue<TempPurchaseOrderQuestionaire>> getQuestionnaires() {
        return questionnaires;
    }
    
    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }
    
    public List<FollowupsaleReason> getFollowupsaleReasonList() {
        return followupsaleReasonList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //Managed Properties
    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }
    */
  
}
