/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.mng;

import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.Subdistrict;
import java.util.List;
   
/**
 *
 * @author DevTeam
 */
public interface RegistrationMasterDataService {
    List<Province> lookupAllProvince(boolean readonlyMode) throws Exception;
    List<District> lookupDistrictByProvinceId(boolean readonlyMode,Integer provinceId) throws Exception;
    List<Subdistrict> lookupSubDistrictByDistrictId(boolean readonlyMode,Integer districtId) throws Exception;
    
    Province findShortenProvinceById(Integer provinceId) throws Exception;
    District findShortenDistrictById(Integer districtId) throws Exception;
    Subdistrict findShortenSubDistrictById(Integer subDistrictId) throws Exception;

}
