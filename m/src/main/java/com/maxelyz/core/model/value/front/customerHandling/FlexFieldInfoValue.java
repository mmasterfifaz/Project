package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.ChildRegForm;

import javax.faces.model.SelectItem;
import java.util.List;

public class FlexFieldInfoValue {
    private int no;
    private String flexField;
    private Boolean requireFlexField;
    private String poFlexField;
    private String controlType;
    private Integer seqNo;
    private List<SelectItem> items;
    
    public FlexFieldInfoValue(int no, String flexField, Boolean requireFlexField, String poFlexField, String controlType, List<SelectItem> items, String pageForm, Integer seqNo) {
        this.no = no;
        this.flexField = flexField;
        this.requireFlexField = requireFlexField;
        this.controlType = controlType;
        this.seqNo = seqNo;
        if(pageForm.equals("registration")) {
            this.poFlexField = poFlexField;
        }else if(pageForm.equals("child_registration")) {
            if(controlType.equals("radio") || controlType.equals("checkbox"))
                this.poFlexField = changeItemsToJSON(items);
            else
                this.poFlexField = poFlexField;
        }else if(pageForm.equals("declaration")) {
            if(controlType.equals("radio") || controlType.equals("checkbox"))
                this.poFlexField = changeItemsToJSON(items);
            else
                this.poFlexField = poFlexField;
        }else if(pageForm.equals("confirm")) {
            if(controlType.equals("combobox") && poFlexField != null && !poFlexField.equals(""))
                this.poFlexField = changeCodeToText(poFlexField, items);
            else
                this.poFlexField = poFlexField;
        }
        this.items = items;
    }

    private String changeCodeToText(String poFlexField, List<SelectItem> items) {
        String value = "";
        for(SelectItem t: items){
            if(t.getValue().equals(poFlexField)) {
                value = t.getLabel();
                break;
            }
        }
        return value;
    }
    
    public String changeItemsToJSON(List<SelectItem> items) {
        String value = "";
        String[] label = new String[2];
        
        value = "{\"items\":[";
        /*
        for(SelectItem t: items){
            if (items.indexOf(t) > 0) {
                value = value + ",";
            }
            if (t.getLabel().indexOf("|") > 0) {
                label = t.getLabel().split("\\|");
                value = value + "{\"" + label[0] + "\":";
            }else{
                value = value + "{\"" + t.getLabel() + "\":";
            }
            value = value + (t.getValue().toString().equalsIgnoreCase("true") ? "\"P\"" : "\"\"");
            if (t.getLabel().indexOf("|") > 0) {
                if (label.length > 1) {
                    value = value + ",\"" + label[1] + "\":\"" + t.getDescription() + "\"}";
                }else{
                    value = value + ",\"\":\"" + t.getDescription() + "\"}";
                }
            }else{
                value = value + "}";
            }
        }
        */
        for(SelectItem item: items){
            if (items.indexOf(item) > 0) {
                value = value + ",";
            }
            if (item.getLabel().indexOf("|") > 0) {
                label = item.getLabel().split("\\|");
                if (label.length > 1) {
                    value = value + "{\"id\":\"" + item.getValue() + "\",\"label\":\"" + label[0] + "|" + label[1] + "\"";
                }else{
                    value = value + "{\"id\":\"" + item.getValue() + "\",\"label\":\"" + label[0] + "|\"";
                }
            }else{
                value = value + "{\"id\":\"" + item.getValue() + "\",\"label\":\"" + item.getLabel() + "\"";
            }
            value = value + (item.isDisabled() ? ",\"value\":\"P\"" : ",\"value\":\"\"");
            if (item.getLabel().indexOf("|") > 0) {
                /*
                if (label.length > 1) {
                    value = value + ",\"description label\":\"" + label[1] + "\"";
                }else{
                    value = value + ",\"description label\":\"\"";
                }
                */
                value = value + ",\"description\":\"" + item.getDescription() + "\"";
            }
            value = value + "}";
        }
        value = value + "]}"; 
        return value;
    }
    
    public boolean isEmptyCheckboxControl(ChildRegForm childRegForm,FlexFieldInfoValue childRegFx) {
        //boolean emptyControl = true;
        for (SelectItem item : childRegFx.getItemList()) {
            //boolean itemValue = Boolean.valueOf(item.getValue().toString());
            boolean itemChecked = item.isDisabled();
            if (item.getLabel().indexOf("|") > 0) {
                if(childRegForm.getCode().equals("tax")){
                    if (itemChecked){
                        return false;
                    }
                }else{
                    if (itemChecked && !item.getDescription().isEmpty()){
                        return false;
                    }
                }
            }else{
                if (itemChecked) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /*
    public FlexFieldInfoValue(int no, String flexField, Boolean requireFlexField, String poFlexField, String controlType, List<SelectItem> items) {
        this.no = no;
        this.flexField = flexField;
        this.requireFlexField = requireFlexField;
        this.controlType = controlType;
        this.poFlexField = poFlexField;
        this.items = items;
    }
    */

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
    
    public String getFlexField() {
        return flexField;
    }

    public void setFlexField(String flexField) {
        this.flexField = flexField;
    }

    public String getPoFlexField() {
        if(controlType.equals("radio") || controlType.equals("checkbox"))
            return changeItemsToJSON(items);
        else
            return poFlexField;
    }

    public void setPoFlexField(String poFlexField) {
        this.poFlexField = poFlexField;
    }

    public Boolean getRequireFlexField() {
        return requireFlexField;
    }

    public void setRequireFlexField(Boolean requireFlexField) {
        this.requireFlexField = requireFlexField;
    }
    
    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public List<SelectItem> getItemList() {
        return items;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

}