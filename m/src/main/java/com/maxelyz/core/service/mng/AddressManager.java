/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.mng;

import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.Subdistrict;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author DevTeam
 */
public class AddressManager {
    
    private static AddressManager manager;  

    private List<Province> provinceList;
    private LinkedHashMap<Integer,List<District> > districtListMapProvinceId; // mapping between provinceid and district list in province
    private LinkedHashMap<Integer,List<Subdistrict> > subDistrictListMapDistrictId; // mapping between district and subdistrict list in particular district

    private AddressManager() throws Exception{
        this.districtListMapProvinceId = new LinkedHashMap<Integer,List<District>>(0);
        this.subDistrictListMapDistrictId = new LinkedHashMap<Integer,List<Subdistrict>>(0);
        buildProvinceList();

    }
    
    protected static AddressManager getInstance() throws Exception{
        if (AddressManager.manager==null){ // Apply Singleton
            AddressManager.manager = new AddressManager();
        }
        return manager;
    }

    public List<Province> getProvinceList() throws Exception{
        return Collections.unmodifiableList(this.provinceList);
    }

    public List<District> getDistrictListByProvinceId(Integer provinceId)throws Exception{
        List<District> result = null;
        if (provinceId!=null){
            List<District> list = this.districtListMapProvinceId.get(provinceId); // get list from mapping object
            if (list!=null) result = buildDistrictList4ProvinceId(provinceId); // if list is never in mapping object, query from database and keep it in mappling object
        }else{
           result = new ArrayList<District>(0);
        }
        return Collections.unmodifiableList(result);
    }
    
    public List<Subdistrict> getSubDistrictListByDistrictId(Integer districtId)throws Exception{
        List<Subdistrict> result = null;
        if (districtId!=null){
            List<Subdistrict> list = this.subDistrictListMapDistrictId.get(districtId); // get list from mapping object
            if (list!=null) result = buildSubDistrictList4DistrictId(districtId); // if list is never in mapping object, query from database and keep it in mappling object
        }else{
           result = new ArrayList<Subdistrict>(0);
        }
        return Collections.unmodifiableList(result);
    } 

    public Province getShortenProvinceById(Integer provinceId)throws Exception {
        return ContextResourceFinder.getProvinceDAO().findShortenProvinceById(provinceId);
    }
    
    public District getShortenDistrictById(Integer districtId)throws Exception{
        return ContextResourceFinder.getDistrictDAO().findShortenDistrictById(districtId);
    }
    
    public Subdistrict getShortenSubDistrictById(Integer subDistrictId) throws Exception {
        return ContextResourceFinder.getSubDistrictDAO().findShortenSubDistrictById(subDistrictId);
    }
    
    /***************************** PRIVATE METHOD ********************************/
    
    private void buildProvinceList(){
        //Step1 : initial value
        this.provinceList = new ArrayList<Province>(0);
        //Step2 : retrieve province data 
        List<Object[]> tmpProvinceList = ContextResourceFinder.getProvinceDAO().getProvinceList1(); // id, name
        if (tmpProvinceList!=null){
            for(Object[] p : tmpProvinceList){// Each province
                this.provinceList.add(new Province((Integer)p[0], (String)p[1]) );
            }
        }
    }
    
    private List<District> buildDistrictList4ProvinceId(Integer id){
         List<District> holdDistrictList = new ArrayList<District>(0);
         List<Object[]> tmpDistrictList = ContextResourceFinder.getDistrictDAO().findDistrictByProvinceId1(id, ""); // id, name
         if ( tmpDistrictList!=null ){
            for(Object[] d : tmpDistrictList ){// Each district
                holdDistrictList.add(new District((Integer)d[0], (String)d[1]));
            }
            this.districtListMapProvinceId.put(id, holdDistrictList); // keep data in list for next time
         }
         return holdDistrictList;
    }
  
    private List<Subdistrict> buildSubDistrictList4DistrictId(Integer id){
        List<Subdistrict> holdSubDistrictList = new ArrayList<Subdistrict>(0);
        List<Object[]> tmpSubDistrictList = ContextResourceFinder.getSubDistrictDAO().findSubDistrictByDistrictId2(id); // id, name, postal code
        if (tmpSubDistrictList!=null){
            for(Object[] s : tmpSubDistrictList){// Each subdistrict
                holdSubDistrictList.add(new Subdistrict((Integer)s[0], (String)s[1], (String)s[2]));
            }
            this.subDistrictListMapDistrictId.put(id, holdSubDistrictList);
        }        
         return holdSubDistrictList;
    }
}
