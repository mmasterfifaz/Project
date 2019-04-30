package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.core.model.entity.Occupation;
import com.maxelyz.core.model.entity.OccupationCategory;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderQuestionaire;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.model.SelectItem;

import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
@ViewScoped
public class RegistrationFrontController {

    private static Logger log = Logger.getLogger(RegistrationFrontController.class);
    private static String REDIRECT_PAGE = JSFUtil.getRequestContextPath()+"/campaign/assignmentList.jsf";
    private static String SUCCESS = "confirmation.xhtml";
    private static String CONFIRMATION_PAGE = "/front/customerHandling/confirmationPo.xhtml";
    private static String SALEHISTORY_PAGE = "/front/customerHandling/saleHistory.xhtml?faces-redirect=true";
//    private static String SUCCESS = "shoppingCart.xhmtl";
    private static String EDIT = "campaignedit.xhtml";
    private static String SELF = "registration.xhtml";
    private static String ASSIGNED = "assigned";
    private static String VIEWED = "viewed";
    private static String OPENED = "opened";
    private static String FOLLOWUP = "followup";
    private Date defaultDob = null;
    private Product product;
    private RegistrationForm registrationForm;
    private TempPurchaseOrder tempPurchaseOrder;
    private TempPurchaseOrderDetail tempPurchaseOrderDetail;
    private Map<String, Integer> provinceList;
    private List<QuestionnaireInfoValue<TempPurchaseOrderQuestionaire>> questionnaires;
    private List<RegistrationInfoValue<TempPurchaseOrderRegister>> regInfo = new ArrayList<RegistrationInfoValue<TempPurchaseOrderRegister>>();
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private String message;
    private String returnPage;
    private Boolean disableSaveButton=false;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;
    private UserSession userSession;
    private PurchaseOrder purchaseOrder;

    private List<OccupationCategory> occupationCategoryList;
    
    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{occupationCategoryDAO}")
    private OccupationCategoryDAO occupationCategoryDAO;
    @ManagedProperty(value = "#{occupationDAO}")
    private OccupationDAO occupationDAO;

    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    
    @PostConstruct
    public void initialize() {
        try {
            userSession = JSFUtil.getUserSession();

            Integer tempPoId = 0;//(Integer) JSFUtil.getRequest().getAttribute("tempPurchaseOrderId");
            if (JSFUtil.getRequestParameterMap("tempPurchaseOrderId") != null && !JSFUtil.getRequestParameterMap("tempPurchaseOrderId").equals("")) {
                tempPoId = Integer.parseInt(JSFUtil.getRequestParameterMap("tempPurchaseOrderId"));
            } else if (JSFUtil.getRequest().getAttribute("tempPurchaseOrderId") != null && !JSFUtil.getRequest().getAttribute("tempPurchaseOrderId").equals("")) {
                tempPoId = (Integer) JSFUtil.getRequest().getAttribute("tempPurchaseOrderId");
            }
            Integer productId = 0;//(Integer) JSFUtil.getRequest().getAttribute("productId");
            if (JSFUtil.getRequestParameterMap("productId") != null && !JSFUtil.getRequestParameterMap("productId").equals("")) {
                productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
            } else if (JSFUtil.getRequest().getAttribute("productId") != null && !JSFUtil.getRequest().getAttribute("productId").equals("")) {
                productId = (Integer) JSFUtil.getRequest().getAttribute("productId");
            }
            tempPurchaseOrder = this.getPurchaseOrderDAO().findTempPurchaseOrder(tempPoId);
            tempPurchaseOrderDetail = this.getPurchaseOrderDAO().findTempPurchaseOrderDetailByProduct(tempPoId, productId);
            product = this.getProductDAO().findProduct(productId);
            if (tempPurchaseOrder == null || tempPurchaseOrderDetail == null || product == null) {
                throw new Exception("No Data Found, tempPurchaseOrder or tempPurchaseOrderDetail or product is null");
            }
            updateAssignmentDetail();


        } catch (Exception e) {
            log.error(e);
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } finally {
            JSFUtil.getRequest().removeAttribute("tempPurchaseOrderId");
            JSFUtil.getRequest().removeAttribute("productId");
        }

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1);  //year, month, day
        defaultDob = c.getTime();

        int productId = tempPurchaseOrderDetail.getProduct().getId();
        registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(productId);
        nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
        followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

        this.setProviceList();
        this.initOccupationCategoryList();
        this.setRegistrationForm();
        this.setQuestionnares();
    }



    private void updateAssignmentDetail() {
        AssignmentDetail assignmentDetail = null;
        try {
            if (userSession.getAssignmentDetail() != null) {
                assignmentDetail = userSession.getAssignmentDetail();
            } else {
                assignmentDetail = tempPurchaseOrder.getAssignmentDetail();
            }

            if (assignmentDetail != null) {
                if(assignmentDetail.getStatus().equals(ASSIGNED) || assignmentDetail.getStatus().equals(VIEWED)){
                    assignmentDetailDAO.updateStatus(assignmentDetail, OPENED);
                    JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String editAction() {
        return EDIT;
    }

    public String backAction() {
        return SUCCESS;
    }

    public String selfAction() {
        initialize();
        return SELF;
    }

    private void setPoFlexField(TempPurchaseOrderRegister poReg, List<FlexFieldInfoValue> fxInfo) {
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
            if (poReg.getCurrentDistrict().getId() == null || poReg.getCurrentDistrict().getId() == 0) {
                poReg.setCurrentDistrict(null);
            }
        }
        if (poReg.getHomeDistrict() != null) {
            if (poReg.getHomeDistrict().getId() == null || poReg.getHomeDistrict().getId() == 0) {
                poReg.setHomeDistrict(null);
            }
        }
        if (poReg.getBillingDistrict() != null) {
            if (poReg.getBillingDistrict().getId() == null || poReg.getBillingDistrict().getId() == 0) {
                poReg.setBillingDistrict(null);
            }
        }
        if (poReg.getShippingDistrict() != null) {
            if (poReg.getShippingDistrict().getId() == null || poReg.getShippingDistrict().getId() == 0) {
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
        for (RegistrationInfoValue<TempPurchaseOrderRegister> reg : regInfo) {
            TempPurchaseOrderRegister poReg = reg.getPo();
            poReg.setTempPurchaseOrderDetail(tempPurchaseOrderDetail);
            this.setNoUseDistrict(poReg);
            if (reg.getOccupationId()!=null && reg.getOccupationId()!=0) {
                poReg.setOccupation(new Occupation(reg.getOccupationId()));
            } else {
                poReg.setOccupation(null);
            }
            if(reg.getCurrentDistrictId() != null && reg.getCurrentDistrictId() != 0){
                poReg.setCurrentDistrict(new District(reg.getCurrentDistrictId()));
            }else{
                poReg.setCurrentDistrict(null);
            }
            if(reg.getHomeDistrictId() != null && reg.getHomeDistrictId() != 0){
                poReg.setHomeDistrict(new District(reg.getHomeDistrictId()));
            }else{
                poReg.setHomeDistrict(null);
            }
            if(reg.getBillingDistrictId() != null && reg.getBillingDistrictId() != 0){
                poReg.setBillingDistrict(new District(reg.getBillingDistrictId()));
            }else{
                poReg.setBillingDistrict(null);
            }
            if(reg.getShippingDistrictId() != null && reg.getShippingDistrictId() != 0){
                poReg.setShippingDistrict(new District(reg.getShippingDistrictId()));
            }else{
                poReg.setShippingDistrict(null);
            }
            
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
            poq.setTempPurchaseOrderDetail(tempPurchaseOrderDetail);
            poQuestionaire.add(poq);
        }
        if (no > 0) {
            tempPurchaseOrderDetail.setTempPurchaseOrderRegisterCollection(regDetail);
            if (poQuestionaire.size() > 0) {
                tempPurchaseOrderDetail.setTempPurchaseOrderQuestionaireCollection(poQuestionaire);
            }
            try {
                List<TempPurchaseOrderDetail> poDetails = new ArrayList<TempPurchaseOrderDetail>();
                poDetails.add(tempPurchaseOrderDetail);
                tempPurchaseOrder.setTempPurchaseOrderDetailCollection(poDetails);
                tempPurchaseOrder.setNosaleReason(nosaleReasonId != null && nosaleReasonId > 0 ? new NosaleReason(nosaleReasonId) : null);
                tempPurchaseOrder.setFollowupsaleReason(followupsaleReasonId != null && followupsaleReasonId > 0 ? new FollowupsaleReason(followupsaleReasonId) : null);
                tempPurchaseOrder.setApprovalStatus(JSFUtil.getBundleValue("approvalWaitingValue"));

                this.getPurchaseOrderDAO().editTemp(tempPurchaseOrder, tempPurchaseOrderDetail);
                //checkout
                this.checkOut(tempPurchaseOrder);
                
                JSFUtil.getRequest().setAttribute("purchaseOrderId", purchaseOrder.getId());
                JSFUtil.getRequest().setAttribute("productId", product.getId());

                if (String.valueOf(purchaseOrder.getSaleResult()).equals("Y")) {
                    return CONFIRMATION_PAGE;
                } else {
                    return SALEHISTORY_PAGE;
                }
            } catch (Exception e) {
                
                for (RegistrationInfoValue<TempPurchaseOrderRegister> reg : regInfo) {
                   TempPurchaseOrderRegister poReg = reg.getPo();
                   this.recoverDistrict(poReg);
                }    
                message = e.getMessage();
                log.error(e);
            }
        }
        return null;
    }
//checkout
    public void checkOut(TempPurchaseOrder tpo) {

        String status = "closed";
        Date flDate = null;
        if(tpo != null) {
            if(String.valueOf(tpo.getSaleResult()).equals("F")){
                status = FOLLOWUP;
                flDate = tpo.getFollowupsaleDate();
            }
            boolean b = this.updatePurchaseOrder(tpo);
            if (b) {
                this.clearTempPurchaseOrder(tpo);// clear TempPurchaseOrder and TempPurchaseOrderDetail

            }
        }
        updateAssignmentDetail(status, flDate);

    }

    private void updateAssignmentDetail(String status, Date flDate) {
        AssignmentDetail assignmentDetail = null;
        try {
            if (userSession.getAssignmentDetail() != null) {
                assignmentDetail = userSession.getAssignmentDetail();
            } else {
                assignmentDetail = tempPurchaseOrder.getAssignmentDetail();
            }

            if (assignmentDetail != null) {
                assignmentDetail.setStatus(status);
                assignmentDetail.setFollowupsaleDate(flDate);
                assignmentDetail.setUpdateDate(new Date());
                if (assignmentDetail.getSaleResult() == null) {
                    assignmentDetail.setSaleResult(String.valueOf(tempPurchaseOrder.getSaleResult()));
                }else if (!assignmentDetail.getSaleResult().equals("Y")){
                    assignmentDetail.setSaleResult(String.valueOf(tempPurchaseOrder.getSaleResult()));
                }
                assignmentDetailDAO.edit(assignmentDetail);
            }
            JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean updatePurchaseOrder(TempPurchaseOrder tpo) {
        try {

            if (tpo != null) {

                PurchaseOrder po = new PurchaseOrder();

                po.setRefNo(tpo.getRefNo());
                po.setCustomer(tpo.getCustomer());
                po.setAssignmentDetail(tpo.getAssignmentDetail());
                po.setPurchaseDate(new Date());
                po.setTotalAmount(tpo.getTotalAmount());
                po.setChannelType(tpo.getChannelType());
                po.setSaleResult(tpo.getSaleResult());
                po.setSaleDate(tpo.getSaleDate());
                po.setNosaleReason(tpo.getNosaleReason());
                po.setFollowupsaleDate(tpo.getFollowupsaleDate());
                po.setFollowupsaleReason(tpo.getFollowupsaleReason());
                po.setBillingFullname(tpo.getBillingFullname());
                po.setBillingAddressLine1(tpo.getBillingAddressLine1());
                po.setBillingAddressLine2(tpo.getBillingAddressLine2());
                po.setBillingDistrict(tpo.getBillingDistrict());
                po.setBillingPostalCode(tpo.getBillingPostalCode());
                po.setBillingPhone(tpo.getBillingPhone());
                po.setShippingFullname(tpo.getShippingFullname());
                po.setShippingAddressLine1(tpo.getShippingAddressLine1());
                po.setShippingAddressLine2(tpo.getShippingAddressLine2());
                po.setShippingDistrict(tpo.getShippingDistrict());
                po.setShippingPostalCode(tpo.getShippingPostalCode());
                po.setShippingPhone(tpo.getShippingPhone());
                po.setPaymentMethod(tpo.getPaymentMethod());
                po.setPaymentType(tpo.getPaymentType());
                po.setCardType(tpo.getCardType());
                po.setCardHolderName(tpo.getCardHolderName());
                po.setCardNo(tpo.getCardNo());
                po.setCardExpiryMonth(tpo.getCardExpiryMonth());
                po.setCardExpiryYear(tpo.getCardExpiryYear());
                po.setPaymentAmount(tpo.getPaymentAmount());
                po.setTraceNo(tpo.getTraceNo());
                po.setApprovalStatus(tpo.getApprovalStatus());
                po.setApproveDate(tpo.getApproveDate());
                po.setApproveBy(tpo.getApproveBy());
                po.setApproveByUser(tpo.getApproveByUser());
                po.setQcStatus(tpo.getQcStatus());
                po.setQcDate(tpo.getQcDate());
                po.setQcBy(tpo.getQcBy());
                po.setQcByUser(tpo.getQcByUser());
                po.setSettlement(tpo.getSettlement());
                po.setSettlementDate(tpo.getSettlementDate());
                po.setSettlementBy(tpo.getSettlementBy());
                po.setCreateDate(tpo.getCreateDate());
                po.setCreateBy(tpo.getCreateBy());
                po.setCreateByUser(tpo.getCreateByUser());
                po.setUpdateDate(new Date());
                po.setUpdateBy(userSession.getUserName());
                po.setUpdateByUser(userSession.getUsers());
                po.setFx1(tpo.getFx1());
                po.setFx2(tpo.getFx2());
                po.setFx3(tpo.getFx3());
                po.setFx4(tpo.getFx4());
                po.setFx5(tpo.getFx5());
                po.setFx6(tpo.getFx6());
                po.setFx7(tpo.getFx7());
                po.setFx8(tpo.getFx8());
                po.setFx9(tpo.getFx9());
                po.setFx10(tpo.getFx10());
                po.setFx11(tpo.getFx11());
                po.setFx12(tpo.getFx12());
                po.setFx13(tpo.getFx13());
                po.setFx14(tpo.getFx14());
                po.setFx15(tpo.getFx15());

                PurchaseOrderDetail pod = null;
                Collection<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>();
                Collection<TempPurchaseOrderDetail> tpodList = tpo.getTempPurchaseOrderDetailCollection();
                for (TempPurchaseOrderDetail tpod : tpodList) {
                    pod = this.setPurchaseOrderDetail(po, tpod);
                    podList.add(pod);
                }

                po.setPurchaseOrderDetailCollection(podList);

                purchaseOrderDAO.create(po);

                //set purchaseOrderId
                purchaseOrder = po;

                JSFUtil.getUserSession().setPurchaseOrders(po); //set po to usersession
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    private PurchaseOrderDetail setPurchaseOrderDetail(PurchaseOrder po, TempPurchaseOrderDetail tpod){
        PurchaseOrderDetail pod = new PurchaseOrderDetail();
//        pod.setId(tpod.getId());
        pod.setPurchaseOrder(po);
        //pod.setAssignmentDetail(assignmentDetailDAO.findAssignmentDetail(tpod.getAssignmentDetailId()));
        pod.setProduct(tpod.getProduct());
        pod.setProductPlan(tpod.getProductPlan());
        pod.setPaymentMode(tpod.getPaymentMode());
        pod.setDeliveryMethod(tpod.getDeliveryMethod());
        pod.setUnitPrice(tpod.getUnitPrice());
        pod.setQuantity(tpod.getQuantity());
        pod.setAmount(tpod.getAmount());
        pod.setCreateDate(new Date());

        PurchaseOrderRegister por = null;
        Collection<TempPurchaseOrderRegister> tporList = tpod.getTempPurchaseOrderRegisterCollection();
        Collection<PurchaseOrderRegister> porList = new ArrayList<PurchaseOrderRegister>();
        for(TempPurchaseOrderRegister tpor : tporList){
            por = this.setPurchaseOrderRegister(pod, tpor);
            porList.add(por);
        }

        PurchaseOrderQuestionaire poq = null;
        Collection<TempPurchaseOrderQuestionaire> tpoqList = tpod.getTempPurchaseOrderQuestionaireCollection();
        Collection<PurchaseOrderQuestionaire> poqList = new ArrayList<PurchaseOrderQuestionaire>();
        for(TempPurchaseOrderQuestionaire tpoq : tpoqList){
            poq = this.setPurchaseOrderQuestionaire(pod, tpoq);
            poqList.add(poq);
        }

        pod.setPurchaseOrderRegisterCollection(porList);
        pod.setPurchaseOrderQuestionaireCollection(poqList);

        return pod;
    }

    private PurchaseOrderRegister setPurchaseOrderRegister(PurchaseOrderDetail pod, TempPurchaseOrderRegister tpor){
        PurchaseOrderRegister por = new PurchaseOrderRegister();
//        por.setId(tpor.getId());
        por.setPurchaseOrderDetail(pod);
        por.setNo(tpor.getNo());
        por.setInitial(tpor.getInitial());
        por.setName(tpor.getName());
        por.setSurname(tpor.getSurname());
        por.setIdcardTypeId(tpor.getIdcardTypeId());
        por.setIdno(tpor.getIdno());
        por.setGender(tpor.getGender());
        por.setDob(tpor.getDob());
        por.setWeight(tpor.getWeight());
        por.setHeight(tpor.getHeight());
        por.setMaritalStatus(tpor.getMaritalStatus());
        por.setNationality(tpor.getNationality());
        por.setOccupation(tpor.getOccupation());
        por.setJobDescription(tpor.getJobDescription());
        por.setHomePhone(tpor.getHomePhone());
        por.setOfficePhone(tpor.getOfficePhone());
        por.setMobilePhone(tpor.getMobilePhone());
        por.setEmail(tpor.getEmail());
        por.setCurrentFullname(tpor.getCurrentFullname());
        por.setCurrentAddressLine1(tpor.getCurrentAddressLine1());
        por.setCurrentAddressLine2(tpor.getCurrentAddressLine2());
        por.setCurrentDistrict(tpor.getCurrentDistrict());
        por.setCurrentPostalCode(tpor.getCurrentPostalCode());
        por.setHomeFullname(tpor.getHomeFullname());
        por.setHomeAddressLine1(tpor.getHomeAddressLine1());
        por.setHomeAddressLine2(tpor.getHomeAddressLine2());
        por.setHomeDistrict(tpor.getHomeDistrict());
        por.setHomePostalCode(tpor.getHomePostalCode());
        por.setBillingFullname(tpor.getBillingFullname());
        por.setBillingAddressLine1(tpor.getBillingAddressLine1());
        por.setBillingAddressLine2(tpor.getBillingAddressLine2());
        por.setBillingDistrict(tpor.getBillingDistrict());
        por.setBillingPostalCode(tpor.getBillingPostalCode());
        por.setShippingFullname(tpor.getShippingFullname());
        por.setShippingAddressLine1(tpor.getShippingAddressLine1());
        por.setShippingAddressLine2(tpor.getShippingAddressLine2());
        por.setShippingDistrict(tpor.getShippingDistrict());
        por.setShippingPostalCode(tpor.getShippingPostalCode());
        por.setFx1(tpor.getFx1());
        por.setFx2(tpor.getFx2());
        por.setFx3(tpor.getFx3());
        por.setFx4(tpor.getFx4());
        por.setFx5(tpor.getFx5());
        por.setFx6(tpor.getFx6());
        por.setFx7(tpor.getFx7());
        por.setFx8(tpor.getFx8());
        por.setFx9(tpor.getFx9());
        por.setFx10(tpor.getFx10());
        por.setFx11(tpor.getFx11());
        por.setFx12(tpor.getFx12());
        por.setFx13(tpor.getFx13());
        por.setFx14(tpor.getFx14());
        por.setFx15(tpor.getFx15());
        por.setFx16(tpor.getFx16());
        por.setFx17(tpor.getFx17());
        por.setFx18(tpor.getFx18());
        por.setFx19(tpor.getFx19());
        por.setFx20(tpor.getFx20());

        return por;
    }

    private PurchaseOrderQuestionaire setPurchaseOrderQuestionaire(PurchaseOrderDetail pod, TempPurchaseOrderQuestionaire tpoq){
        PurchaseOrderQuestionaire poq = new PurchaseOrderQuestionaire();
//        poq.setId(null);
        poq.setPurchaseOrderDetail(pod);
        poq.setSeqNo(tpoq.getSeqNo());
        poq.setQuestion(tpoq.getQuestion());
        poq.setAnswerChoice(tpoq.getAnswerChoice());
        poq.setAnswer(tpoq.getAnswer());
        poq.setDescription(tpoq.getDescription());

        return poq;
    }

    private void clearTempPurchaseOrder(TempPurchaseOrder tpo) {
        try {
            if (tpo != null) {
                purchaseOrderDAO.destroyTemp(tpo.getId());
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
//checkout
    public void setRegistrationForm() {
        regInfo = new ArrayList<RegistrationInfoValue<TempPurchaseOrderRegister>>();
        List<RegistrationField> registrationFields = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (int i = 0; i < 6; i++) {
            try {
                TempPurchaseOrderRegister tpor = null;
                if(tempPurchaseOrderDetail.getTempPurchaseOrderRegisterCollection() != null){
                    for(TempPurchaseOrderRegister tt : tempPurchaseOrderDetail.getTempPurchaseOrderRegisterCollection()){
                        if((i+1) == tt.getNo()){
                            tpor = tt;
                        }
                    }
                }

                if (tpor == null) {
                    tpor = new TempPurchaseOrderRegister();
                    
                }
                if(tpor.getId() == null || tpor.getId() == 0){
                    if(tpor.getCurrentDistrict() == null) tpor.setCurrentDistrict(new District());
                    if(tpor.getHomeDistrict() == null) tpor.setHomeDistrict(new District());
                    if(tpor.getShippingDistrict() == null) tpor.setShippingDistrict(new District());
                    if(tpor.getBillingDistrict() == null) tpor.setBillingDistrict(new District());
                }
                RegistrationInfoValue<TempPurchaseOrderRegister> item = new RegistrationInfoValue<TempPurchaseOrderRegister>(registrationForm, registrationFields, i + 1, districtDAO, subdistrictDAO, occupationDAO, tpor, "registration");
                if(item.getPo().getDob() == null){
                    item.getPo().setDob(defaultDob);
                    item.setAge(defaultDob);
                }else{
                    item.setAge(item.getPo().getDob());
                }
                regInfo.add(item);
            } catch (NonexistentEntityException e) {
                //no need recovery this process
            } catch (Exception e) {
                log.error(e);
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
            if (tempPurchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection() != null) {
                for(TempPurchaseOrderQuestionaire tq : tempPurchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection()){
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

    public List<RegistrationInfoValue<TempPurchaseOrderRegister>> getRegInfo() {
        return regInfo;
    }

    public void setProviceList() {
        provinceList = getProvinceDAO().getProvinceList();
    }

    public Map<String, Integer> getProvinceList() {
        return provinceList;
    }

    public List<OccupationCategory> getOccupationCategoryList() {
        return occupationCategoryList;
    }

    public void initOccupationCategoryList() {
        this.occupationCategoryList = this.getOccupationCategoryDAO().findOccupationCategoryEntities();
    }



    public Boolean getDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(Boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }


    public TempPurchaseOrder getTempPurchaseOrder() {
        return tempPurchaseOrder;
    }

    public void setTempPurchaseOrder(TempPurchaseOrder tempPurchaseOrder) {
        this.tempPurchaseOrder = tempPurchaseOrder;
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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public OccupationCategoryDAO getOccupationCategoryDAO() {
        return occupationCategoryDAO;
    }

    public void setOccupationCategoryDAO(OccupationCategoryDAO occupationCategoryDAO) {
        this.occupationCategoryDAO = occupationCategoryDAO;
    }

    public OccupationDAO getOccupationDAO() {
        return occupationDAO;
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        this.occupationDAO = occupationDAO;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
    }
    
    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
}
}
