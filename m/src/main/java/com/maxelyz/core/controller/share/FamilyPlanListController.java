/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.dao.ProductPlanDetailDAO;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author DEV_NB_DELL02
 */
@ManagedBean
@ViewScoped
public class FamilyPlanListController {
    private boolean showTableAgeInsurePerson;
    private Integer insurePersonID = 2;
    private Integer age1 = 0;
    private Integer age2 = 0;
    private Integer age3 = 0;
    private Integer age4 = 0;
    private Integer age5 = 0;
    private Integer age6 = 0;

    private String gender1 = "Male";
    private String gender2 = "Male";
    private String gender3 = "Male";
    private String gender4 = "Male";
    private String gender5 = "Male";
    private String gender6 = "Male";

    private Double total1 = 0.0;
    private Double total2 = 0.0;
    private Double total3 = 0.0;
    private Double total4 = 0.0;
    private Double total5 = 0.0;
    private Double total6 = 0.0;

    private Double netPremiumMain1;
    private Double netPremiumMain2;
    private Double netPremiumMain3;
    private Double netPremiumMain4;
    private Double netPremiumMain5;
    private Double netPremiumMain6;

    private Double netPremiumChild1;
    private Double netPremiumChild2;
    private Double netPremiumChild3;
    private Double netPremiumChild4;
    private Double netPremiumChild5;
    private Double netPremiumChild6;

    private Double totalPremium;
    private Double totalPremiumDiscount;
    private Double totalDiscount;

    private List<ProductPlanDetail> productPlanDetailListForMain;
    private List<ProductPlanDetail> productPlanDetailListForChild;
    private String message;
    private String messageDup;

    private List<ProductPlan> productPlanList;
    private Integer productPlanId;
    private Integer familyProductLogic = 0;

    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productPlanDetailDAO}")
    private ProductPlanDetailDAO productPlanDetailDAO;

    @PostConstruct
    public void initialize() {
        showTableAgeInsurePerson = false;

        String productId = JSFUtil.getRequestParameterMap("productId");
        productPlanList = productPlanDAO.findProductPlanByProductID(new Integer(productId));
        if (JSFUtil.getRequestParameterMap("familyProductLogic") != null && JSFUtil.getRequestParameterMap("familyProductLogic")!="") {
            familyProductLogic = Integer.parseInt(JSFUtil.getRequestParameterMap("familyProductLogic"));
        }
    }

    public void numberChangeListener(ValueChangeEvent event) {
        insurePersonID = (Integer) event.getNewValue();
    }

    public Map<Integer, Integer> getAgeInsure() {
        Map<Integer, Integer> ageInsure = new ConcurrentSkipListMap<Integer, Integer>();
        for (int i = 0; i <= 99; i++) {
            ageInsure.put(i, i);
        }
        return ageInsure;
    }

    public Map<Integer, Integer> getNumberOfInsure() {
        Map<Integer, Integer> numberOfInsure = new ConcurrentSkipListMap<Integer, Integer>();
        for (int i = 2; i <= 6; i++) {
            numberOfInsure.put(i, i);
        }
        return numberOfInsure;
    }

    public void calculate() {
        int countNumberOfPersonInseured = 0;
        netPremiumMain1 = 0.0;
        netPremiumMain2 = 0.0;
        netPremiumMain3 = 0.0;
        netPremiumMain4 = 0.0;
        netPremiumMain5 = 0.0;
        netPremiumMain6 = 0.0;
        
        netPremiumChild1 = 0.0;
        netPremiumChild2 = 0.0;
        netPremiumChild3 = 0.0;
        netPremiumChild4 = 0.0;
        netPremiumChild5 = 0.0;
        netPremiumChild6 = 0.0;
        
        totalPremium = 0.0;
        totalPremiumDiscount = 0.0;
        totalDiscount = 0.0;
  
        if (productPlanId == null) {
            messageDup = "Please select Plan";
            showTableAgeInsurePerson = false;
        } else {
            messageDup = "";
            showTableAgeInsurePerson = true;
        }

        productPlanDetailListForMain = getProductPlanDetailDAO().findProductPlanDetailByProductPlan(productPlanId);
        Integer productPlanChild = getProductPlanDAO().findProductPlanRelationByProductPlanID(productPlanId);
        productPlanDetailListForChild = getProductPlanDetailDAO().findProductPlanDetailByProductPlan(productPlanChild);

        for (int i = 1; i <= 6; i++) {
            for (ProductPlanDetail ppd : productPlanDetailListForMain) {

                if (i == 1 && gender1.equalsIgnoreCase("male") && ppd.getFromVal() <= age1 && ppd.getToVal() >= age1 && ppd.getGender().equals("M")) {
                    netPremiumMain1 = ppd.getNetPremium();
                } else if (i == 2 && gender2.equalsIgnoreCase("male") && ppd.getFromVal() <= age2 && ppd.getToVal() >= age2 && ppd.getGender().equals("M")) {
                    netPremiumMain2 = ppd.getNetPremium();
                } else if (i == 3 && gender3.equalsIgnoreCase("male") && ppd.getFromVal() <= age3 && ppd.getToVal() >= age3 && ppd.getGender().equals("M")) {
                    netPremiumMain3 = ppd.getNetPremium();
                } else if (i == 4 && gender4.equalsIgnoreCase("male") && ppd.getFromVal() <= age4 && ppd.getToVal() >= age4 && ppd.getGender().equals("M")) {
                    netPremiumMain4 = ppd.getNetPremium();
                } else if (i == 5 && gender5.equalsIgnoreCase("male") && ppd.getFromVal() <= age5 && ppd.getToVal() >= age5 && ppd.getGender().equals("M")) {
                    netPremiumMain5 = ppd.getNetPremium();
                } else if (i == 6 && gender6.equalsIgnoreCase("male") && ppd.getFromVal() <= age6 && ppd.getToVal() >= age6 && ppd.getGender().equals("M")) {
                    netPremiumMain6 = ppd.getNetPremium();
                }

                if (i == 1 && gender1.equalsIgnoreCase("female") && ppd.getFromVal() <= age1 && ppd.getToVal() >= age1 && ppd.getGender().equals("F")) {
                    netPremiumMain1 = ppd.getNetPremium();
                } else if (i == 2 && gender2.equalsIgnoreCase("female") && ppd.getFromVal() <= age2 && ppd.getToVal() >= age2 && ppd.getGender().equals("F")) {
                    netPremiumMain2 = ppd.getNetPremium();
                } else if (i == 3 && gender3.equalsIgnoreCase("female") && ppd.getFromVal() <= age3 && ppd.getToVal() >= age3 && ppd.getGender().equals("F")) {
                    netPremiumMain3 = ppd.getNetPremium();
                } else if (i == 4 && gender4.equalsIgnoreCase("female") && ppd.getFromVal() <= age4 && ppd.getToVal() >= age4 && ppd.getGender().equals("F")) {
                    netPremiumMain4 = ppd.getNetPremium();
                } else if (i == 5 && gender5.equalsIgnoreCase("female") && ppd.getFromVal() <= age5 && ppd.getToVal() >= age5 && ppd.getGender().equals("F")) {
                    netPremiumMain5 = ppd.getNetPremium();
                } else if (i == 6 && gender6.equalsIgnoreCase("female") && ppd.getFromVal() <= age6 && ppd.getToVal() >= age6 && ppd.getGender().equals("F")) {
                    netPremiumMain6 = ppd.getNetPremium();
                }
            }

            for (int j = 1; j <= 6; j++) {
                for (ProductPlanDetail ppd : getProductPlanDetailListForChild()) {

                    if (j == 1 && gender1.equalsIgnoreCase("male") && ppd.getFromVal() <= age1 && ppd.getToVal() >= age1 && ppd.getGender().equals("M")) {
                        netPremiumChild1 = ppd.getNetPremium();
                    } else if (j == 2 && gender2.equalsIgnoreCase("male") && ppd.getFromVal() <= age2 && ppd.getToVal() >= age2 && ppd.getGender().equals("M")) {
                        netPremiumChild2 = ppd.getNetPremium();
                    } else if (j == 3 && gender3.equalsIgnoreCase("male") && ppd.getFromVal() <= age3 && ppd.getToVal() >= age3 && ppd.getGender().equals("M")) {
                        netPremiumChild3 = ppd.getNetPremium();
                    } else if (j == 4 && gender4.equalsIgnoreCase("male") && ppd.getFromVal() <= age4 && ppd.getToVal() >= age4 && ppd.getGender().equals("M")) {
                        netPremiumChild4 = ppd.getNetPremium();
                    } else if (j == 5 && gender5.equalsIgnoreCase("male") && ppd.getFromVal() <= age5 && ppd.getToVal() >= age5 && ppd.getGender().equals("M")) {
                        netPremiumChild5 = ppd.getNetPremium();
                    } else if (j == 6 && gender6.equalsIgnoreCase("male") && ppd.getFromVal() <= age6 && ppd.getToVal() >= age6 && ppd.getGender().equals("M")) {
                        netPremiumChild6 = ppd.getNetPremium();
                    }

                    if (i == 1 && gender1.equalsIgnoreCase("female") && ppd.getFromVal() <= age1 && ppd.getToVal() >= age1 && ppd.getGender().equals("F")) {
                        netPremiumChild1 = ppd.getNetPremium();
                    } else if (j == 2 && gender2.equalsIgnoreCase("female") && ppd.getFromVal() <= age2 && ppd.getToVal() >= age2 && ppd.getGender().equals("F")) {
                        netPremiumChild2 = ppd.getNetPremium();
                    } else if (j == 3 && gender3.equalsIgnoreCase("female") && ppd.getFromVal() <= age3 && ppd.getToVal() >= age3 && ppd.getGender().equals("F")) {
                        netPremiumChild3 = ppd.getNetPremium();
                    } else if (j == 4 && gender4.equalsIgnoreCase("female") && ppd.getFromVal() <= age4 && ppd.getToVal() >= age4 && ppd.getGender().equals("F")) {
                        netPremiumChild4 = ppd.getNetPremium();
                    } else if (j == 5 && gender5.equalsIgnoreCase("female") && ppd.getFromVal() <= age5 && ppd.getToVal() >= age5 && ppd.getGender().equals("F")) {
                        netPremiumChild5 = ppd.getNetPremium();
                    } else if (j == 6 && gender6.equalsIgnoreCase("female") && ppd.getFromVal() <= age6 && ppd.getToVal() >= age6 && ppd.getGender().equals("F")) {
                        netPremiumChild6 = ppd.getNetPremium();
                    }
                }
            }
        }
        
        if (insurePersonID >= 1) {
            total1 = netPremiumMain1 - netPremiumChild1;
            totalPremium += netPremiumMain1;
            totalPremiumDiscount += netPremiumChild1;
            if(netPremiumMain1 != 0)countNumberOfPersonInseured++;
        }
        if (insurePersonID >= 2) {
            total2 = netPremiumMain2 - netPremiumChild2;
            totalPremium += netPremiumMain2;
            totalPremiumDiscount += netPremiumChild2;
            if(netPremiumMain2 != 0)countNumberOfPersonInseured++;
        }
        if (insurePersonID >= 3) {
            total3 = netPremiumMain3 - netPremiumChild3;
            totalPremium += netPremiumMain3;
            totalPremiumDiscount += netPremiumChild3;
            if(netPremiumMain3 != 0)countNumberOfPersonInseured++;
        }
        if (insurePersonID >= 4) {
            total4 = netPremiumMain4 - netPremiumChild4;
            totalPremium += netPremiumMain4;
            totalPremiumDiscount += netPremiumChild4;
            if(netPremiumMain4 != 0)countNumberOfPersonInseured++;
        }
        if (insurePersonID >= 5) {
            total5 = netPremiumMain5 - netPremiumChild5;
            totalPremium += netPremiumMain5;
            totalPremiumDiscount += netPremiumChild5;
            if(netPremiumMain5 != 0)countNumberOfPersonInseured++;
        }
        if (insurePersonID >= 6) {
            total6 = netPremiumMain6 - netPremiumChild6;
            totalPremium += netPremiumMain6;
            totalPremiumDiscount += netPremiumChild6;
            if(netPremiumMain6 != 0)countNumberOfPersonInseured++;
        }
        
        if(countNumberOfPersonInseured < familyProductLogic){
            message = "Discount Plan not Apply";
            netPremiumChild1 = 0.0;
            netPremiumChild2 = 0.0;
            netPremiumChild3 = 0.0;
            netPremiumChild4 = 0.0;
            netPremiumChild5 = 0.0;
            netPremiumChild6 = 0.0;
            total1 = 0.0;
            total2 = 0.0;
            total3 = 0.0;
            total4 = 0.0;
            total5 = 0.0;
            total6 = 0.0;
            totalDiscount = 0.0;
            totalPremiumDiscount = 0.0;
        } else {
            message = "";
            totalDiscount = total1 + total2 + total3 + total4 + total5 + total6;
        }

    }

    public boolean isShowTableAgeInsurePerson() {
        return showTableAgeInsurePerson;
    }

    public void setShowTableAgeInsurePerson(boolean showTableAgeInsurePerson) {
        this.showTableAgeInsurePerson = showTableAgeInsurePerson;
    }

    public Integer getInsurePersonID() {
        return insurePersonID;
    }

    public void setInsurePersonID(Integer insurePersonID) {
        this.insurePersonID = insurePersonID;
    }

    public List<ProductPlan> getProductPlanList() {
        return productPlanList;
    }

    public void setProductPlanList(List<ProductPlan> productPlanList) {
        this.productPlanList = productPlanList;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public Integer getProductPlanId() {
        return productPlanId;
    }

    public void setProductPlanId(Integer productPlanId) {
        this.productPlanId = productPlanId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAge1() {
        return age1;
    }

    public void setAge1(Integer age1) {
        this.age1 = age1;
    }

    public Integer getAge2() {
        return age2;
    }

    public void setAge2(Integer age2) {
        this.age2 = age2;
    }

    public Integer getAge3() {
        return age3;
    }

    public void setAge3(Integer age3) {
        this.age3 = age3;
    }

    public Integer getAge4() {
        return age4;
    }

    public void setAge4(Integer age4) {
        this.age4 = age4;
    }

    public Integer getAge5() {
        return age5;
    }

    public void setAge5(Integer age5) {
        this.age5 = age5;
    }

    public Integer getAge6() {
        return age6;
    }

    public void setAge6(Integer age6) {
        this.age6 = age6;
    }

    public String getGender1() {
        return gender1;
    }

    public void setGender1(String gender1) {
        this.gender1 = gender1;
    }

    public String getGender2() {
        return gender2;
    }

    public void setGender2(String gender2) {
        this.gender2 = gender2;
    }

    public String getGender3() {
        return gender3;
    }

    public void setGender3(String gender3) {
        this.gender3 = gender3;
    }

    public String getGender4() {
        return gender4;
    }

    public void setGender4(String gender4) {
        this.gender4 = gender4;
    }

    public String getGender5() {
        return gender5;
    }

    public void setGender5(String gender5) {
        this.gender5 = gender5;
    }

    public String getGender6() {
        return gender6;
    }

    public void setGender6(String gender6) {
        this.gender6 = gender6;
    }

    public Double getTotal1() {
        return total1;
    }

    public void setTotal1(Double total1) {
        this.total1 = total1;
    }

    public Double getTotal2() {
        return total2;
    }

    public void setTotal2(Double total2) {
        this.total2 = total2;
    }

    public Double getTotal3() {
        return total3;
    }

    public void setTotal3(Double total3) {
        this.total3 = total3;
    }

    public Double getTotal4() {
        return total4;
    }

    public void setTotal4(Double total4) {
        this.total4 = total4;
    }

    public Double getTotal5() {
        return total5;
    }

    public void setTotal5(Double total5) {
        this.total5 = total5;
    }

    public Double getTotal6() {
        return total6;
    }

    public void setTotal6(Double total6) {
        this.total6 = total6;
    }

    public Double getNetPremiumMain1() {
        return netPremiumMain1;
    }

    public void setNetPremiumMain1(Double netPremiumMain1) {
        this.netPremiumMain1 = netPremiumMain1;
    }

    public Double getNetPremiumMain2() {
        return netPremiumMain2;
    }

    public void setNetPremiumMain2(Double netPremiumMain2) {
        this.netPremiumMain2 = netPremiumMain2;
    }

    public Double getNetPremiumMain3() {
        return netPremiumMain3;
    }

    public void setNetPremiumMain3(Double netPremiumMain3) {
        this.netPremiumMain3 = netPremiumMain3;
    }

    public Double getNetPremiumMain4() {
        return netPremiumMain4;
    }

    public void setNetPremiumMain4(Double netPremiumMain4) {
        this.netPremiumMain4 = netPremiumMain4;
    }

    public Double getNetPremiumMain5() {
        return netPremiumMain5;
    }

    public void setNetPremiumMain5(Double netPremiumMain5) {
        this.netPremiumMain5 = netPremiumMain5;
    }

    public Double getNetPremiumMain6() {
        return netPremiumMain6;
    }

    public void setNetPremiumMain6(Double netPremiumMain6) {
        this.netPremiumMain6 = netPremiumMain6;
    }

    public Double getNetPremiumChild1() {
        return netPremiumChild1;
    }

    public void setNetPremiumChild1(Double netPremiumChild1) {
        this.netPremiumChild1 = netPremiumChild1;
    }

    public Double getNetPremiumChild2() {
        return netPremiumChild2;
    }

    public void setNetPremiumChild2(Double netPremiumChild2) {
        this.netPremiumChild2 = netPremiumChild2;
    }

    public Double getNetPremiumChild3() {
        return netPremiumChild3;
    }

    public void setNetPremiumChild3(Double netPremiumChild3) {
        this.netPremiumChild3 = netPremiumChild3;
    }

    public Double getNetPremiumChild4() {
        return netPremiumChild4;
    }

    public void setNetPremiumChild4(Double netPremiumChild4) {
        this.netPremiumChild4 = netPremiumChild4;
    }

    public Double getNetPremiumChild5() {
        return netPremiumChild5;
    }

    public void setNetPremiumChild5(Double netPremiumChild5) {
        this.netPremiumChild5 = netPremiumChild5;
    }

    public Double getNetPremiumChild6() {
        return netPremiumChild6;
    }

    public void setNetPremiumChild6(Double netPremiumChild6) {
        this.netPremiumChild6 = netPremiumChild6;
    }

    public List<ProductPlanDetail> getProductPlanDetailListForMain() {
        return productPlanDetailListForMain;
    }

    public void setProductPlanDetailListForMain(List<ProductPlanDetail> productPlanDetailListForMain) {
        this.productPlanDetailListForMain = productPlanDetailListForMain;
    }

    public List<ProductPlanDetail> getProductPlanDetailListForChild() {
        return productPlanDetailListForChild;
    }

    public void setProductPlanDetailListForChild(List<ProductPlanDetail> productPlanDetailListForChild) {
        this.productPlanDetailListForChild = productPlanDetailListForChild;
    }

    public ProductPlanDetailDAO getProductPlanDetailDAO() {
        return productPlanDetailDAO;
    }

    public void setProductPlanDetailDAO(ProductPlanDetailDAO productPlanDetailDAO) {
        this.productPlanDetailDAO = productPlanDetailDAO;
    }

    public Double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(Double totalPremium) {
        this.totalPremium = totalPremium;
    }

    public Double getTotalPremiumDiscount() {
        return totalPremiumDiscount;
    }

    public void setTotalPremiumDiscount(Double totalPremiumDiscount) {
        this.totalPremiumDiscount = totalPremiumDiscount;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public Integer getFamilyProductLogic() {
        return familyProductLogic;
    }

    public void setFamilyProductLogic(Integer familyProductLogic) {
        this.familyProductLogic = familyProductLogic;
    }
}
