/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;


import com.maxelyz.core.model.dao.ApprovalRuleDAO;
import com.maxelyz.core.model.entity.ApprovalRule;
import com.maxelyz.core.model.entity.ApprovalRuleDetail;
import com.maxelyz.core.model.entity.ApprovalRuleDetailPK;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ApprovalRuleDetailController {

    private static Logger log = Logger.getLogger(ApprovalRuleDetailController.class);
    
    private String type;
    private ApprovalRule approvalRule;
    private ApprovalRuleDetail approvalRuleDetail;
    private ApprovalRuleDetailPK approvalRuleDetailPK;
    private List<ApprovalRuleDetail> approvalRuleDetailList;
    private Integer seqNo;
    private String fieldName;
    private String operator;
    private String textValue;
    private String[] selectedValues;
    
    @ManagedProperty(value = "#{approvalRuleDAO}")
    private ApprovalRuleDAO approvalRuleDAO;
    
    @PostConstruct
    public void initialize() {
        Integer id = Integer.parseInt(JSFUtil.getRequestParameterMap("id"));
        if(id != null && id != 0){
            try{
                approvalRule = approvalRuleDAO.findApprovalRule(id);
            }catch(Exception e){
                log.error(e);
            }
        }
        type = JSFUtil.getRequestParameterMap("type");
        this.initApprovalRuleDetailList();
        
    }
    
    private void initApprovalRuleDetailList(){
        if(approvalRule != null && approvalRule.getId() != null){
            approvalRuleDetailList = approvalRuleDAO.findApprovalRuleDetail(approvalRule.getId(), type);
            if(approvalRuleDetailList == null){
                approvalRuleDetailList = new ArrayList<ApprovalRuleDetail>();
            }
        }
    }
    
    public void addActionListener(ActionEvent event){
        add();
    }
    
    public String saveAction(){
        save();
        return "approvalrule.jsf?faces-redirect=true";
    }
    
    public String backAction(){
        return "approvalrule.jsf?faces-redirect=true";
    }
    
    private void save(){
        try{
            List<ApprovalRuleDetail> list = approvalRuleDAO.findApprovalRuleDetail(approvalRule.getId(), type);
            for(ApprovalRuleDetail apd : list){
                approvalRuleDAO.removeApprovalRuleDetail(apd.getApprovalRuleDetailPK());
            }
        }catch(Exception e){
            log.error(e);
        }
        
        for(ApprovalRuleDetail apd : approvalRuleDetailList){
            approvalRuleDAO.createApprovalRuleDetail(apd);
        }        
    }
    
    private void add(){
        if (!fieldName.equals("") && !operator.equals("") && !textValue.equals("")) {
            boolean exist = false;
            for (ApprovalRuleDetail apd : approvalRuleDetailList) {
                if (apd.getFieldName().equals(fieldName)
                        && apd.getOperation().equals(operator)
                        && apd.getValue().equals(textValue)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                seqNo = approvalRuleDetailList.size() + 1;
                approvalRuleDetailPK = new ApprovalRuleDetailPK(approvalRule.getId(), type, seqNo);
                approvalRuleDetail = new ApprovalRuleDetail(approvalRuleDetailPK, fieldName, operator, textValue);
                approvalRuleDetailList.add(approvalRuleDetail);
            }
            fieldName = "";
            operator = "";
            textValue = "";
        }
    }
    
    public void removeActionListener(){
        List<ApprovalRuleDetail> list = approvalRuleDetailList;
        for(String s : selectedValues){
            for(ApprovalRuleDetail apd : list){
                if(apd.getApprovalRuleDetailPK().getSeqNo() == Integer.parseInt(s)){
                    approvalRuleDetailList.remove(apd);
                    break;
                }        
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ApprovalRule getApprovalRule() {
        return approvalRule;
    }

    public void setApprovalRule(ApprovalRule approvalRule) {
        this.approvalRule = approvalRule;
    }

    public ApprovalRuleDAO getApprovalRuleDAO() {
        return approvalRuleDAO;
    }

    public void setApprovalRuleDAO(ApprovalRuleDAO approvalRuleDAO) {
        this.approvalRuleDAO = approvalRuleDAO;
    }

    public ApprovalRuleDetail getApprovalRuleDetail() {
        return approvalRuleDetail;
    }

    public void setApprovalRuleDetail(ApprovalRuleDetail approvalRuleDetail) {
        this.approvalRuleDetail = approvalRuleDetail;
    }

    public List<ApprovalRuleDetail> getApprovalRuleDetailList() {
        return approvalRuleDetailList;
    }

    public void setApprovalRuleDetailList(List<ApprovalRuleDetail> approvalRuleDetailList) {
        this.approvalRuleDetailList = approvalRuleDetailList;
    }

    public ApprovalRuleDetailPK getApprovalRuleDetailPK() {
        return approvalRuleDetailPK;
    }

    public void setApprovalRuleDetailPK(ApprovalRuleDetailPK approvalRuleDetailPK) {
        this.approvalRuleDetailPK = approvalRuleDetailPK;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String[] getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(String[] selectedValues) {
        this.selectedValues = selectedValues;
    }
    
}
