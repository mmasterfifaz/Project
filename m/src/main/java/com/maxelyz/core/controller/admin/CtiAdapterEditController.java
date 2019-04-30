package com.maxelyz.core.controller.admin;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CtiAdapterDAO;
import com.maxelyz.core.model.dao.TagFieldDAO;
import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.model.entity.TagField;
import com.maxelyz.core.model.value.admin.ParameterMappingValue;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;

@ManagedBean
@ViewScoped
public class CtiAdapterEditController {
 
    private static Logger log = Logger.getLogger(CtiAdapterEditController.class);
    private static String REDIRECT_PAGE = "ctiadapter.jsf";
    private static String SUCCESS = "ctiadapter.xhtml?faces-redirect=true";
    private static String FAILURE = "ctiadapteredit.xhtml";

    private CtiAdapter ctiAdapter;
    private List<ParameterMappingValue> parameterMapList;
    private String message;
    private String messageDup;
    private String messageDupParam;
    private String mode;
    private int paramId;
    
    // Custom Tag Field
    private List<TagField> tagFieldList;
    
    @ManagedProperty(value = "#{ctiAdapterDAO}")
    private CtiAdapterDAO ctiAdapterDAO;
    @ManagedProperty(value="#{tagFieldDAO}")
    private TagFieldDAO tagFieldDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:ctiadapter:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            ctiAdapter = new CtiAdapter();
            ctiAdapter.setEnable(Boolean.TRUE);
            ctiAdapter.setStatus(Boolean.TRUE);
            ctiAdapter.setCtiVendorCode("aspect");
            ctiAdapter.setRecorderVendorCode("aspect");
            parameterMapList = new ArrayList<ParameterMappingValue>();
        } else {
            mode = "edit";
            ctiAdapter = this.ctiAdapterDAO.findCtiAdapter(new Integer(selectedID));
            
            if(ctiAdapter == null){
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            } else {
                String param = ctiAdapter.getRecorderParam();
                Gson gson = new Gson();
                if(param != null && !param.equals("")) {
                    String convertToList = param.replace("{\"params-mapping\":", "");
                    convertToList = convertToList.substring(0, convertToList.length() - 1);
                    parameterMapList = gson.fromJson(convertToList, new TypeToken<List<ParameterMappingValue>>(){}.getType());
                } else {
                    parameterMapList = new ArrayList<ParameterMappingValue>();
                }                
            }
        }
        
        tagFieldList = getTagFieldDAO().findTagFieldByTagType("ctiadapter");        
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:ctiadapter:add"); 
       } else {
 	   return SecurityUtil.isPermitted("admin:ctiadapter:edit");  
       }
    }
    
    public String saveAction() {
        checkDupParamListener();
        if(messageDupParam.equals("")) {
            messageDup = "";
            if(checkName(ctiAdapter)) {
                try {
                    //Create JSON 20 Parameter
                    if(parameterMapList != null && parameterMapList.size() > 0) {
                        Gson gson = new Gson();
                        String convertToJson = "{\"params-mapping\":" + gson.toJson(parameterMapList) + "}";
                        ctiAdapter.setRecorderParam(convertToJson);
                    } else {
                        ctiAdapter.setRecorderParam(null);
                    }
                        
                    String username = JSFUtil.getUserSession().getUserName();
                    Date now = new Date();
                    if(mode.equals("add")) {
                        ctiAdapter.setId(null);
                        ctiAdapter.setEnable(true);
                        ctiAdapter.setCreateBy(username);
                        ctiAdapter.setCreateDate(now);                                        
                        ctiAdapterDAO.create(ctiAdapter);
                    } else {
                        ctiAdapter.setUpdateBy(username);
                        ctiAdapter.setUpdateDate(now);
                        ctiAdapterDAO.edit(ctiAdapter);                        
                    }
                } catch (Exception e) {
                    log.error(e);
                    message = e.getMessage();
                    return FAILURE;
                }
                return SUCCESS;
             } else {
                messageDup = " Name is already taken";
                return null;            
            }  
        } else {
            return null;
        }                 
    }
   
    public Boolean checkName(CtiAdapter ctiAdapter) {
        String name = ctiAdapter.getName();
        Integer id=0; 
        if(ctiAdapter.getId() != null)
            id = ctiAdapter.getId();
        
        CtiAdapterDAO dao = getCtiAdapterDAO();        
        Integer cnt = dao.checkCtiAdapterName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    //Listener
    private void reCalculateSeqNo() {
        int id=0;
        for (ParameterMappingValue q: parameterMapList) {
            q.setId(++id);
        }
    }  
    
    public void addQuestionListener(ActionEvent event) {
        int size = parameterMapList.size();
        ParameterMappingValue param = new ParameterMappingValue();
        param.setId(size+1);
        param.setName("");
        param.setValue("");
        parameterMapList.add(param);
    }
            
    public void insertQuestionListener(ActionEvent event) {
        int id = Integer.parseInt(JSFUtil.getRequestParameterMap("paramId"));
        ParameterMappingValue param = new ParameterMappingValue();
        param.setName("");
        param.setValue("");
        parameterMapList.add(id-1, param);
        reCalculateSeqNo(); 
    }
  
    public void deleteQuestionListener(ActionEvent event) {
        int id = Integer.parseInt(JSFUtil.getRequestParameterMap("paramId"));
        parameterMapList.remove(id-1);
        reCalculateSeqNo(); 
    }
    
    public void checkDupParamListener() {
        messageDupParam = "";
        if(parameterMapList != null && !parameterMapList.isEmpty()) {            
            for(ParameterMappingValue chk: parameterMapList) {        
                int cnt = 0;
                for(ParameterMappingValue obj: parameterMapList) {             
                    if(chk.getName().equals(obj.getName())) {
                        cnt++;
                    }
                    if(cnt > 1) {
                        messageDupParam = " Duplicate Seleted Parameter: "+obj.getName();                          
                        break;
                    }
                }
            }
        }
    }
        
    public void addTagParamNo() {
        paramId = Integer.parseInt(JSFUtil.getRequestParameterMap("paramId"));
    }
    
    public void selectTagFieldListener() {
        int id = Integer.parseInt(JSFUtil.getRequestParameterMap("paramId"));
        String tagField = (String) JSFUtil.getRequestParameterMap("tagField");
        if(!parameterMapList.isEmpty()) {
            ParameterMappingValue value = parameterMapList.get(id-1);
            value.setValue(tagField);            
            parameterMapList.set(id-1, value);
        }
    }
    
    public CtiAdapterDAO getCtiAdapterDAO() {
        return ctiAdapterDAO;
    }

    public void setCtiAdapterDAO(CtiAdapterDAO ctiAdapterDAO) {
        this.ctiAdapterDAO = ctiAdapterDAO;
    }

    public TagFieldDAO getTagFieldDAO() {
        return tagFieldDAO;
    }

    public void setTagFieldDAO(TagFieldDAO tagFieldDAO) {
        this.tagFieldDAO = tagFieldDAO;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public CtiAdapter getCtiAdapter() {
        return ctiAdapter;
    }

    public void setCtiAdapter(CtiAdapter ctiAdapter) {
        this.ctiAdapter = ctiAdapter;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<ParameterMappingValue> getParameterMapList() {
        return parameterMapList;
    }

    public void setParameterMapList(List<ParameterMappingValue> parameterMapList) {
        this.parameterMapList = parameterMapList;
    }

    public List<TagField> getTagFieldList() {
        return tagFieldList;
    }

    public void setTagFieldList(List<TagField> tagFieldList) {
        this.tagFieldList = tagFieldList;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getMessageDupParam() {
        return messageDupParam;
    }

    public void setMessageDupParam(String messageDupParam) {
        this.messageDupParam = messageDupParam;
    }

}
