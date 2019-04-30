/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.mng;

import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.Subdistrict;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtilsBean;

/**
 *
 * @author DevTeam
 */
public class RegistrationMasterDataServiceImp implements RegistrationMasterDataService{
    
        
    public RegistrationMasterDataServiceImp() {
        BeanUtilsBean.getInstance().getConvertUtils().register(false, true, -1);
        
    }    

    @Override
    public List<Province> lookupAllProvince(boolean readonlyMode) throws Exception {
         List<Province> result = new ArrayList<Province>(0);
        try {
           result = AddressManager.getInstance().getProvinceList();
           if (!readonlyMode){
               List<Province> newresult = new ArrayList<Province>(result.size());
               for (Province p : result ){ 
                   Province newp = new Province();  
                   BeanUtilsBean.getInstance().copyProperties(newp, p);
                   newresult.add(newp);
               }
               result = newresult;
           }
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Override
    public List<Subdistrict> lookupSubDistrictByDistrictId(boolean readonlyMode,Integer districtId) throws Exception {
        List<Subdistrict> result = new ArrayList<Subdistrict>(0);
        try {
            result = AddressManager.getInstance().getSubDistrictListByDistrictId(districtId);
            if (!readonlyMode){
               List<Subdistrict> newresult = new ArrayList<Subdistrict>(result.size());
               for (Subdistrict s : result ){ 
                   Subdistrict news = new Subdistrict();
                   BeanUtilsBean.getInstance().copyProperties(news, s);
                   newresult.add(news);
               }
               result = newresult;
           }
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Override
    public List<District> lookupDistrictByProvinceId(boolean readonlyMode,Integer provinceId) throws Exception {
        List<District> result = new ArrayList<District>(0);
        try {
            result = AddressManager.getInstance().getDistrictListByProvinceId(provinceId);
            if (!readonlyMode){
               List<District> newresult = new ArrayList<District>(result.size());
               for (District d : result ){ 
                   District newd = new District();
                   BeanUtilsBean.getInstance().copyProperties(newd, d);
                   newresult.add(newd);
               }
               result = newresult;
           }
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    
    @Override
    public District findShortenDistrictById(Integer districtId) throws Exception {
        District result = null;
        try {
            result = AddressManager.getInstance().getShortenDistrictById(districtId);
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Override
    public Province findShortenProvinceById(Integer provinceId) throws Exception {
        Province result = null;
        try {
            result = AddressManager.getInstance().getShortenProvinceById(provinceId);
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Override
    public Subdistrict findShortenSubDistrictById(Integer subDistrictId) throws Exception {
        Subdistrict result = null;
        try {
            result = AddressManager.getInstance().getShortenSubDistrictById(subDistrictId);
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    
}
