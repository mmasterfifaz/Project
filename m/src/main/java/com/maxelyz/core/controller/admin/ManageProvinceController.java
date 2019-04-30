/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.SubdistrictDAO;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Subdistrict;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author admin
 */
@ManagedBean
@ViewScoped
public class ManageProvinceController implements Serializable {

    private static Logger log = Logger.getLogger(ManageProvinceController.class);
    private static String SUCCESS = "manageprovince.xhtml?faces-redirect=true";
    private static String FAILURE = "manageprovince.xhtml";
    private static String REFRESH = "manageprovince.xhtml?faces-redirect=true";
    private static String BACK = "index.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Province> Provinces;
    private List<District> Districts;
    private List<Subdistrict> Subdistricts;
    private Province province;
    private District district;
    private Subdistrict subdistrict;

    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;

    private Boolean isAdd;
    private SelectItem[] provinceList;
    private SelectItem[] districtList;
    private SelectItem[] subdistrictList;
    private String messageDup;
    private String messageError;
    private String provinceName;
    private String districtName;
    private String subdistrictName;
    private String postalCode;
    private String subdistrictCode;
    private String oldSubdistrictCode;
    private String provinceCode;
    private String districtCode;
    private Integer provinceId;
    private Integer districtId;
    private String oldProvinceCode;
    private String oldDistrictCode;
    private Integer subdistrictId;
    private Integer currentProvinceId;
    private Integer currentDistrictId;
    private Integer currentSubdistrictId;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:manageprovince:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        ProvinceDAO dao = provinceDAO;
        Provinces = dao.findProvinceEntities();

        DistrictDAO dao1 = districtDAO;
        Districts = dao1.findDistrictEntities();

        SubdistrictDAO dao2 = subdistrictDAO;
        Subdistricts = dao2.findSubDistrictEntities();

        setProvinceList(1);
        setDistrictList(0);
        setSubdistrictList(0);

    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:manageprovince:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:manageprovince:delete");
    }

    public void addProvinceAction() {
        messageDup = null;
        currentProvinceId = null;
        provinceCode = null;
        provinceName = null;
        isAdd = true;
    }

    public void addDistrictAction() {
        messageDup = null;
        districtId = null;
        districtCode = null;
        districtName = null;
        currentDistrictId = null;
        provinceId = null;
        provinceName = null;
        isAdd = true;
    }

    public void addSubdistrictAction() {
        messageDup = null;
        subdistrictId = null;
        subdistrictCode = null;
        subdistrictName = null;
        postalCode = null;
        districtId = null;
        districtName = null;
        provinceId = null;
        provinceName = null;
        currentSubdistrictId = null;
        isAdd = true;
    }

    public void editProvinceAction() {
        if (currentProvinceId != null) {
            provinceCode = null;
            provinceName = "";

            Province province = provinceDAO.findProvince(currentProvinceId);
            if (province != null) {
                oldProvinceCode = province.getCode();
                provinceCode = province.getCode();
                provinceName = province.getName();
            }
            messageDup = null;
            isAdd = true;
        }
    }

    public void editDistrictAction() {
        if (currentDistrictId != null) {
            districtCode = null;
            districtName = "";
            provinceId = null;
            provinceName = "";

            District district = districtDAO.findDistrict(currentDistrictId);
            if (district != null) {
                oldDistrictCode = district.getCode();
                districtCode = district.getCode();
                districtName = district.getName();
            }
            messageDup = null;
            isAdd = true;
        }
    }

    public void editSubdistrictAction() {
        if (currentSubdistrictId != null) {
            subdistrictCode = null;
            subdistrictName = "";
            postalCode = "";

            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(currentSubdistrictId);
            if (subdistrict != null) {
                oldSubdistrictCode = subdistrict.getCode();
                subdistrictCode = subdistrict.getCode();
                subdistrictName = subdistrict.getName();
                postalCode = subdistrict.getPostalCode();
            }
            messageDup = null;
            isAdd = true;
        }
    }

    public String cancelAction() {
        messageDup = null;
        isAdd = false;
        return REFRESH;
    }

    public String backAction() {
        return BACK;
    }

    public void saveProvinceAction() {
        messageDup = null;
        province = new Province();
        Boolean isCodeDup = false;

        try {

            if (currentProvinceId != null) {
                //if old id = new id 
                if (oldProvinceCode != provinceCode) {
                    for (int i = 0; i < Provinces.size(); i++) {
                        if (!(Provinces.get(i).getId().equals(currentProvinceId))) {
                            if (Provinces.get(i).getCode().equals(provinceCode)) {
                                isCodeDup = true;
                            }
                        }
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    province = provinceDAO.findProvince(currentProvinceId);
                    province.setCode(provinceCode);
                    province.setName(provinceName);
                    provinceDAO.edit(province);
                    isAdd = false;
                }

            } else {

                for (int i = 0; i < Provinces.size(); i++) {
                    if (Provinces.get(i).getCode().equals(provinceCode)) {
                        isCodeDup = true;
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    province.setId(null);
                    province.setCode(provinceCode);
                    province.setName(provinceName);
                    provinceDAO.create(province);
                    isAdd = false;
                }
                FacesContext.getCurrentInstance().renderResponse();
            }
        } catch (Exception e) {
            log.error(e);
        }
        setProvinceList(currentProvinceId);
    }

    public void saveDistrictAction() {
        messageDup = null;
        messageError = null;
        province = new Province();
        district = new District();
        Boolean isCodeDup = false;

        try {
            if (currentProvinceId == null) {
                messageError = "Please select province before add district";
            }

            if (currentDistrictId != null) {
                //compare old code with current code
                if (oldDistrictCode != districtCode) {
                    for (int i = 0; i < Districts.size(); i++) {
                        if (!(Districts.get(i).getId().equals(currentDistrictId))) {
                            if (Districts.get(i).getCode().equals(districtCode)) {
                                isCodeDup = true;
                            }
                        }
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    district = districtDAO.findDistrict(currentDistrictId);
                    district.setCode(districtCode);
                    district.setName(districtName);
                    districtDAO.edit(district);
                    isAdd = false;
                }

            } else {

                for (int i = 0; i < Districts.size(); i++) {
                    if (Districts.get(i).getCode().equals(districtCode)) {
                        isCodeDup = true;
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    Province tmpProvince = provinceDAO.findProvince(currentProvinceId);
                    district.setId(null);
                    district.setCode(districtCode);
                    district.setName(districtName);
                    district.setProvinceId(tmpProvince);
                    district.setProvinceName(tmpProvince.getName());
                    districtDAO.create(district);
                    isAdd = false;
                }
                FacesContext.getCurrentInstance().renderResponse();
            }

        } catch (Exception e) {
            log.error(e);
        }
        setDistrictList(currentProvinceId);
    }

    public void saveSubDistrictAction() {
        messageDup = null;
        messageError = null;
        subdistrict = new Subdistrict();
        Boolean isCodeDup = false;

        try {
            if (currentDistrictId == null) {
                messageError = "Please select district before add subdistrict";
            }

            if (currentSubdistrictId != null) {
                //compare old code with current code
                if (oldSubdistrictCode != subdistrictCode) {
                    for (int i = 0; i < Subdistricts.size(); i++) {
                        if (!(Subdistricts.get(i).getId().equals(currentSubdistrictId))) {
                            if (Subdistricts.get(i).getCode().equals(subdistrictCode)) {
                                isCodeDup = true;
                            }
                        }
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    subdistrict = subdistrictDAO.findSubDistrict(currentSubdistrictId);
                    subdistrict.setCode(subdistrictCode);
                    subdistrict.setName(subdistrictName);
                    subdistrict.setPostalCode(postalCode);
                    subdistrictDAO.edit(subdistrict);
                    isAdd = false;
                }

            } else {

                for (int i = 0; i < Subdistricts.size(); i++) {
                    if (Subdistricts.get(i).getCode().equals(subdistrictCode)) {
                        isCodeDup = true;
                    }
                }

                if (isCodeDup == true) {
                    messageDup = "Code are duplicate.";
                } else {
                    Province tmpProvince = provinceDAO.findProvince(currentProvinceId);
                    District tmpDistrict = districtDAO.findDistrict(currentDistrictId);
                    subdistrict.setId(null);
                    subdistrict.setCode(subdistrictCode);
                    subdistrict.setName(subdistrictName);
                    subdistrict.setPostalCode(postalCode);
                    subdistrict.setDistrict(tmpDistrict);
                    subdistrict.setDistrictName(tmpDistrict.getName());
                    subdistrict.setProvince(tmpProvince);
                    subdistrict.setProvinceName(tmpProvince.getName());
                    subdistrictDAO.create(subdistrict);
                    isAdd = false;
                }
                FacesContext.getCurrentInstance().renderResponse();
            }
        } catch (Exception e) {
            log.error(e);
        }
        setSubdistrictList(currentDistrictId);
    }

    public String deleteSubdistrictAction() throws Exception {
        if (currentSubdistrictId != null) {
            subdistrictDAO.findSubdistrictDeleteById(currentSubdistrictId);
        }
        setSubdistrictList(currentDistrictId);
        return REFRESH;
    }

    public String deleteDistrictAction() throws Exception {
        if (currentDistrictId != null) {
            districtDAO.findDistrictDeleteById(currentDistrictId);
        }
        setDistrictList(currentProvinceId);
        setSubdistrictList(0);
        return REFRESH;
    }

    public String deleteProvinceAction() throws Exception {
        if (currentProvinceId != null) {
            provinceDAO.findProvinceDeleteById(currentProvinceId);
        }
        setProvinceList(1);
        setDistrictList(0);
        return REFRESH;
    }

    //get list from province    
    public void setProvinceList(Integer currentProvinceId) {
        Provinces = provinceDAO.findProvinceEntities();
        provinceList = new SelectItem[Provinces.size() + 1];
        int pos = 0;
        provinceList[pos++] = new SelectItem(null, "Select Province List");
        for (Province ft : Provinces) {
            provinceList[pos++] = new SelectItem(ft.getId(), ft.getName());
        }
    }

    //get list from district
    public void setDistrictList(Integer currentProvinceId) {
        Districts = districtDAO.findDistrictByProvinceId(currentProvinceId);
        districtList = new SelectItem[Districts.size() + 1];
        int pos = 0;
        districtList[pos++] = new SelectItem(null, "Select District List");
        for (District ft : Districts) {
            districtList[pos++] = new SelectItem(ft.getId(), ft.getName());
        }
    }

    //get list from subdistrict
    public void setSubdistrictList(Integer currentDistrictId) {
        Subdistricts = subdistrictDAO.findSubDistrictByDistrictId(currentDistrictId);
        subdistrictList = new SelectItem[Subdistricts.size() + 1];
        int pos = 0;
        subdistrictList[pos++] = new SelectItem(null, "Select Subdistrict List");
        for (Subdistrict ft : Subdistricts) {
            subdistrictList[pos++] = new SelectItem(ft.getId(), ft.getName() + " | " + ft.getPostalCode());
        }
    }

    public void onListSelectedProvinceListener(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            currentProvinceId = Integer.parseInt(event.getNewValue().toString());
            setDistrictList(currentProvinceId);
            currentDistrictId = 0;
            setSubdistrictList(currentDistrictId);
        } else {
            setDistrictList(0);
            setSubdistrictList(0);
        }
    }

    public void onListSelectedDistrictListener(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            currentDistrictId = Integer.parseInt(event.getNewValue().toString());
            setSubdistrictList(currentDistrictId);
        } else {
            setSubdistrictList(0);
        }
    }

    public void onListSelectedSubDistrictListener(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            currentSubdistrictId = Integer.parseInt(event.getNewValue().toString());
        } else {
            setSubdistrictList(currentDistrictId);
        }
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Province> getProvinces() {
        return Provinces;
    }

    public void setProvinces(List<Province> Provinces) {
        this.Provinces = Provinces;
    }

    public List<District> getDistricts() {
        return Districts;
    }

    public void setDistricts(List<District> Districts) {
        this.Districts = Districts;
    }

    public List<Subdistrict> getSubdistricts() {
        return Subdistricts;
    }

    public void setSubdistricts(List<Subdistrict> Subdistricts) {
        this.Subdistricts = Subdistricts;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Subdistrict getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(Subdistrict subdistrict) {
        this.subdistrict = subdistrict;
    }

    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
    }

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
    }

    public SelectItem[] getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(SelectItem[] provinceList) {
        this.provinceList = provinceList;
    }

    public SelectItem[] getDistrictList() {
        return districtList;
    }

    public void setDistrictList(SelectItem[] districtList) {
        this.districtList = districtList;
    }

    public SelectItem[] getSubdistrictList() {
        return subdistrictList;
    }

    public void setSubdistrictList(SelectItem[] subdistrictList) {
        this.subdistrictList = subdistrictList;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getSubdistrictId() {
        return subdistrictId;
    }

    public void setSubdistrictId(Integer subdistrictId) {
        this.subdistrictId = subdistrictId;
    }

    public String getSubdistrictCode() {
        return subdistrictCode;
    }

    public void setSubdistrictCode(String subdistrictCode) {
        this.subdistrictCode = subdistrictCode;
    }

    public String getSubdistrictName() {
        return subdistrictName;
    }

    public void setSubdistrictName(String subdistrictName) {
        this.subdistrictName = subdistrictName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Boolean isAdd) {
        this.isAdd = isAdd;
    }

    public Integer getCurrentProvinceId() {
        return currentProvinceId;
    }

    public void setCurrentProvinceId(Integer currentProvinceId) {
        this.currentProvinceId = currentProvinceId;
    }

    public Integer getCurrentDistrictId() {
        return currentDistrictId;
    }

    public void setCurrentDistrictId(Integer currentDistrictId) {
        this.currentDistrictId = currentDistrictId;
    }

    public String getOldProvinceCode() {
        return oldProvinceCode;
    }

    public void setOldProvinceCode(String oldProvinceCode) {
        this.oldProvinceCode = oldProvinceCode;
    }

    public String getOldDistrictCode() {
        return oldDistrictCode;
    }

    public void setOldDistrictCode(String oldDistrictCode) {
        this.oldDistrictCode = oldDistrictCode;
    }

    public Integer getCurrentSubdistrictId() {
        return currentSubdistrictId;
    }

    public void setCurrentSubdistrictId(Integer currentSubdistrictId) {
        this.currentSubdistrictId = currentSubdistrictId;
    }

    public String getOldSubdistrictCode() {
        return oldSubdistrictCode;
    }

    public void setOldSubdistrictCode(String oldSubdistrictCode) {
        this.oldSubdistrictCode = oldSubdistrictCode;
    }
}
