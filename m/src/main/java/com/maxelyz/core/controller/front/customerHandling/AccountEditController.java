/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.AccountDAO;
import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.entity.Account;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author prawait
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class AccountEditController extends BaseController{

    private Account account;
    private String mode;
    private Integer districtId;
    private Integer provinceId;
    private Boolean districtDisabled;
    private List<Province> provinceList  = new ArrayList<Province>();
    private List districts;

    private static String SUCCESS = "../search/searchAccount.jsf?faces-redirect=true";
    private static String EDIT = "../customerHandling/accountEdit.jsf";
    private static String FAIL = "accountEdit.jsf";

    @ManagedProperty(value="#{accountDAO}")
    private AccountDAO accountDAO;
    @ManagedProperty(value="#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value="#{districtDAO}")
    private DistrictDAO districtDAO;


    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("searchaccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        setProvinceList(getProvinceDAO().findProvinceEntities());
        String selectId = getRequest("selectId");
        if(selectId == null) {
            mode = "add";
            setDistricts(Integer.valueOf(0));
            account = new Account();
            setDistrictDisabled(Boolean.valueOf(true));
        } else {
            mode = "edit";
            account = getAccountDAO().findAccount(Integer.valueOf(Integer.parseInt(selectId)));
            if(account.getProvinceId() != null && account.getProvinceId() != 0){
                setDistricts(account.getProvinceId());
            }
            if(account.getDistrict() != null){
                districtId = account.getDistrict().getId();
            }
        }
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("searchaccount:add");
       } else {
 	   return SecurityUtil.isPermitted("searchaccount:edit"); 
       }
    }  
    
    
    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /*
     * get Account for edit
     */
    public String editAction(){
        return EDIT;
    }

    private List getDistrictList(Integer provinceId)
    {
        List districtList = getDistrictDAO().findDistrictByProvinceId(provinceId);
        List values = new ArrayList();
        values.add(new SelectItem(Integer.valueOf(0), CrmConstant.PLEASE_SELECT));
        District obj;
        for(Iterator i$ = districtList.iterator(); i$.hasNext(); values.add(new SelectItem(obj.getId(), obj.getName())))
            obj = (District)i$.next();

        if(null == values || values.size() == 1)
            setDistrictDisabled(Boolean.valueOf(true));
        else
            setDistrictDisabled(Boolean.valueOf(false));
        return values;
    }

    /**
     * pack and save customer detail.
     */
    public String saveAction(){
        try
        {
            if("add".equals(mode))
            {
                if(districtId != null && districtId.intValue() != 0)
                    account.setDistrict(getDistrictDAO().findDistrict(districtId));
                account.setCreateBy(getLoginUser().getName());
                account.setCreateDate(new Date());
                getAccountDAO().create(account);
            } else
            {
                if(districtId != null && districtId.intValue() != 0)
                    account.setDistrict(getDistrictDAO().findDistrict(districtId));
                account.setUpdateBy(getLoginUser().getName());
                account.setUpdateDate(new Date());
                getAccountDAO().edit(account);
            }
        }
        catch(Exception e)
        {
            return FAIL;
        }
        return SUCCESS;
    }

    /**
     * @param provinceList the provinceList to set
     */
    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List getProvinces()
    {
        List provinceValueList = new ArrayList();
        provinceValueList.add(new SelectItem(Integer.valueOf(0), CrmConstant.PLEASE_SELECT));
        Iterator i$ = provinceList.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Province province = (Province)i$.next();
            if(province.getId() != null)
                provinceValueList.add(new SelectItem(province.getId(), province.getName()));
        } while(true);
        return provinceValueList;
    }

    /**
     * @return the provinceDAO
     */
    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    /**
     * @param provinceDAO the provinceDAO to set
     */
    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    /**
     * @return the districtId
     */
    public Integer getDistrictId() {
        return districtId;
    }

    /**
     * @param districtId the districtId to set
     */
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    /**
     * @return the districtDAO
     */
    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    /**
     * @param districtDAO the districtDAO to set
     */
    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public List getDistricts()
    {
        return districts;
    }

    public void setDistricts(Integer provinceId)
    {
        if(districts != null)
            districts.clear();
        districts = getDistrictList(provinceId);
    }

    public void provinceListener(ValueChangeEvent event) {
        Integer provinceIdChange = (Integer)event.getNewValue();
        setDistricts(provinceIdChange);
//        FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * @return the accountDAO
     */
    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    /**
     * @param accountDAO the accountDAO to set
     */
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the provinceId
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId the provinceId to set
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * @return the districtDisabled
     */
    public Boolean getDistrictDisabled() {
        return districtDisabled;
    }

    /**
     * @param districtDisabled the districtDisabled to set
     */
    public void setDistrictDisabled(Boolean districtDisabled) {
        this.districtDisabled = districtDisabled;
    }
}
