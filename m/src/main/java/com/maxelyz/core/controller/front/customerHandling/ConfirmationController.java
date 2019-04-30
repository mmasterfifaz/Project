package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
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
public class ConfirmationController {

    private static Logger log = Logger.getLogger(RegistrationFrontController.class);

    private static String REDIRECT_PAGE = JSFUtil.getRequestContextPath()+"/campaign/assignmentList.jsf";
    private static String SUCCESS = "/front/customerHandling/shoppingCart.xhtml?faces-redirect=true";
    private static String BACK = "/front/customerHandling/registration.jsf";
    private static String FAILURE = "campaignsummary.xhtml";
    private Date defaultDob = null;
    private Product product;
    private RegistrationForm registrationForm;
    private TempPurchaseOrder tempPurchaseOrder;
    private TempPurchaseOrderDetail tempPurchaseOrderDetail;
    private Map<String, Integer> provinceList;
    private List<QuestionnaireInfoValue<TempPurchaseOrderQuestionaire>> questionnaires;
    private List<RegistrationInfoValue> regInfo;
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private String message;
    private String returnPage;
    private Boolean disableSaveButton=false;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;
    private String confirmScript;

    private static String[] strA = {"#customerInitial","#customerFullname",
                                    "#tmrCode","#tmrFullname"};

    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
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

    @PostConstruct
    public void initialize() {
        regInfo = new ArrayList<RegistrationInfoValue>();
        try {
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

        } catch (Exception e) {
            log.error(e);
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
 //           return REDIRECT_PAGE;
        } finally {
//            JSFUtil.getRequest().removeAttribute("tempPurchaseOrderId");
//            JSFUtil.getRequest().removeAttribute("productId");
        }

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1);  //year, month, day
        defaultDob = c.getTime();

        int productId = tempPurchaseOrderDetail.getProduct().getId();
        registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(productId);
        nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
        followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

        this.setProviceList();
        this.setRegistrationForm();
        this.setQuestionnares();
        this.setConfirmScript();

    }

    public void toConfirm(ActionEvent event){
        initialize();
    }

    public void initListener(ActionEvent event) {
        initialize();
    }

    public String backAction() {
    
        JSFUtil.getRequest().setAttribute("tempPurchaseOrderId", tempPurchaseOrder.getId());
        JSFUtil.getRequest().setAttribute("productId", product.getId());
        
        return BACK;
    }

    private void setConfirmScript(){
        if(product != null && !product.getConfirmationScript().equals("")){
            UserSession userSession = JSFUtil.getUserSession();
            confirmScript = product.getConfirmationScript();
            TempPurchaseOrderRegister tpor1 = (TempPurchaseOrderRegister) regInfo.get(0).getPo();
            for(Integer i = 0; i < strA.length; i++){
                String str = strA[i];
                if(confirmScript.indexOf(strA[i]) != -1){
                    if(str.equals("#customerInitial")){
                        confirmScript = confirmScript.replaceAll(str, tpor1.getInitial());
                    }else if(str.equals("#customerFullname")){
                        confirmScript = confirmScript.replaceAll(str, (tpor1.getName() + " " + tpor1.getSurname()));
                    }else if(str.equals("#tmrCode")){
                        confirmScript = confirmScript.replaceAll(str, userSession.getUsers().getId().toString());
                    }else if(str.equals("#tmrFullname")){
                        confirmScript = confirmScript.replaceAll(str, (userSession.getUsers().getName() + " " + userSession.getUsers().getSurname()));
                    }
                }
            }
        }
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

    public String back(){
        return BACK;
    }


    public String saveAction() {
        return SUCCESS;
    }

    public void setRegistrationForm() {
        List<RegistrationField> registrationFields = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (int i = 0; i < 6; i++) {
            try {
                TempPurchaseOrderRegister tpor = null;
                List<TempPurchaseOrderRegister> tempPor = purchaseOrderDAO.findTempPurchaseOrderRegisterByPoDetail(tempPurchaseOrderDetail.getId());
                if(tempPor != null){
                    for(TempPurchaseOrderRegister tt : tempPor){
                        if((i+1) == tt.getNo()){
                            tpor = tt;
                        }else{
                            tpor = null;
                        }
                    }
                }
                RegistrationInfoValue<TempPurchaseOrderRegister> item = new RegistrationInfoValue<TempPurchaseOrderRegister> (registrationForm, registrationFields, i + 1, districtDAO, subdistrictDAO, occupationDAO, tpor, "confirm");
                if(item.getPo().getDob() == null){
                    item.getPo().setDob(defaultDob);
                    item.setAge(defaultDob);
                }else{
                    item.setAge(item.getPo().getDob());
                }
                if(item.getPo().getCurrentDistrict() != null && item.getPo().getCurrentDistrict().getId() != null)
                    item.getPo().setCurrentDistrict(districtDAO.findDistrict(item.getPo().getCurrentDistrict().getId()));

                if(item.getPo().getHomeDistrict() != null && item.getPo().getHomeDistrict().getId() != null)
                    item.getPo().setHomeDistrict(districtDAO.findDistrict(item.getPo().getHomeDistrict().getId()));

                if(item.getPo().getBillingDistrict() != null && item.getPo().getBillingDistrict().getId() != null)
                    item.getPo().setBillingDistrict(districtDAO.findDistrict(item.getPo().getBillingDistrict().getId()));

                if(item.getPo().getShippingDistrict() != null && item.getPo().getShippingDistrict().getId() != null)
                    item.getPo().setShippingDistrict(districtDAO.findDistrict(item.getPo().getShippingDistrict().getId()));
                regInfo.add(item);

            } catch (NonexistentEntityException e) {
//                e.printStackTrace();
                //no need recovery process
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
        TempPurchaseOrderQuestionaire tpoq = null;
        for (QuestionnaireDetail q : questionnaireDetails) {
            if (tempPurchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection() != null) {
                for(TempPurchaseOrderQuestionaire tq : tempPurchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection()){
                    if(q.getNo() == tq.getSeqNo()){
                        tpoq = tq;
                        break;
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

    public OccupationDAO getOccupationDAO() {
        return occupationDAO;
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        this.occupationDAO = occupationDAO;
    }

    public TempPurchaseOrderDetail getTempPurchaseOrderDetail() {
        return tempPurchaseOrderDetail;
    }

    public void setTempPurchaseOrderDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) {
        this.tempPurchaseOrderDetail = tempPurchaseOrderDetail;
    }

    public String getConfirmScript() {
        return confirmScript;
    }

    public void setConfirmScript(String confirmScript) {
        this.confirmScript = confirmScript;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
}

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
    }
    
}
