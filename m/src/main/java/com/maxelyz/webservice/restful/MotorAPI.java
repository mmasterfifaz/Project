package com.maxelyz.webservice.restful;

import com.google.gson.JsonObject;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.PurchaseOrderRegisterDAO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/")
public class MotorAPI {

    private static Logger log = Logger.getLogger(MotorAPI.class);

    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;
    @Autowired
    private PurchaseOrderRegisterDAO purchaseOrderRegisterDAO;

    @RequestMapping(value = "v1/WSTMUPDATEINFO", method = RequestMethod.POST , produces = "application/json; charset=utf-8")
    public @ResponseBody
    String WSTMUPDATEINFO(@RequestBody String body){
        String status = "SUCCESS";
        String message = "";
        String property = "";
        JsonObject jsonResponse = new JsonObject();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonBody = (JSONObject) parser.parse(body);

            // check values
            for (Object key : jsonBody.keySet()) {
                String keyStr = (String)key;
                String keyvalue = jsonBody.get(keyStr).toString();
                //System.out.println(keyvalue);
                if(keyvalue == null || StringUtils.isBlank(keyvalue)) {
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE","400 Bad Request. Value is null, blank or empty.");
                    return jsonResponse.toString();
                }
            }

            String leadId="",user="",saleCode="",companyCode="",packagecode= "";
            Double grossPremium =0.0, totalPremium =0.0;
            try{
                //get property and check property name
                property = "LEADID";
                leadId = jsonBody.get("LEADID").toString();
                property = "USER";
                user = jsonBody.get("USER").toString();
                property = "SALECODE";
                saleCode = jsonBody.get("SALECODE").toString();
                property = "COMPANYCODE";
                companyCode = jsonBody.get("COMPANYCODE").toString();
                property = "PACKAGECODE";
                packagecode = jsonBody.get("PACKAGECODE").toString();
                property = "GROSSPREMIUM";
                grossPremium = Double.parseDouble(jsonBody.get("GROSSPREMIUM").toString());
                property = "TOTALPREMIUM";
                totalPremium = Double.parseDouble(jsonBody.get("TOTALPREMIUM").toString());
            }catch (Exception e) {
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't get property " + property +". Please check your property name or value");
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

            try{
                String updateMessage ="";
                boolean updatePurchaseOrderRegister = purchaseOrderRegisterDAO.updatePurchaseOrderRegisterInfoFromBCIB(companyCode,packagecode,leadId);
                if(updatePurchaseOrderRegister==false){
                    updateMessage += "Can't update Purchase_order_register. //WSTMUPDATEINFO\n";
                }
                boolean updatePurchaseOrder = purchaseOrderDAO.updatePurchaseOrderInfoFromBCIB(grossPremium,totalPremium,leadId);
                if(updatePurchaseOrder==false){
                    updateMessage += "Can't update Purchase_order. //WSTMUPDATEINFO\n";
                }

                if(updatePurchaseOrderRegister==false || updatePurchaseOrder == false){
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE",updateMessage);
                    log.error("Can't get property" + updateMessage);
                    return jsonResponse.toString();
                }

            }catch (Exception e){
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't update data //WSTMUPDATEINFO. Cause: "+ e.getMessage());
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

        }catch (Exception e) {
            status = "ERROR";
            message = "400 Bad Request. JSON format";
            log.error("JSON format" + e.getMessage());
        }

        jsonResponse.addProperty("STATUS",status);
        jsonResponse.addProperty("MESSAGE",message);
        return jsonResponse.toString();
    }

    @RequestMapping(value = "v1/WSTMPREAPPROVEINFO", method = RequestMethod.POST , produces = "application/json; charset=utf-8")
    public @ResponseBody
    String WSTMPREAPPROVEINFO(@RequestBody String body){
        String status = "SUCCESS";
        String message = "";
        String property = "";
        JsonObject jsonResponse = new JsonObject();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonBody = (JSONObject) parser.parse(body);

            // check values
            for (Object key : jsonBody.keySet()) {
                String keyStr = (String)key;
                String keyvalue = jsonBody.get(keyStr).toString();
                //System.out.println(keyvalue);
                if(keyvalue == null || StringUtils.isBlank(keyvalue)) {
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE","400 Bad Request. Value is null, blank or empty.");
                    return jsonResponse.toString();
                }
            }

            String leadId="",user="",saleCode="",statuss ="";
            Date datetime;
            try{
                //get property and check property name
                property = "LEADID";
                leadId = jsonBody.get("LEADID").toString();
                property = "USER";
                user = jsonBody.get("USER").toString();
                property = "SALECODE";
                saleCode = jsonBody.get("SALECODE").toString();
                property = "DATETIME";
                SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
                datetime = dt.parse(jsonBody.get("DATETIME").toString());

                property = "STATUS";
                statuss = jsonBody.get("STATUS").toString();
            }catch (Exception e) {
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't get property " + property +". Please check your property name or value");
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

            try{
                String updateMessage ="";

                boolean updatePurchaseOrder = purchaseOrderDAO.updatePurchaseOrderApproveFromBCIB(saleCode,datetime,statuss,leadId);
                if(updatePurchaseOrder==false){
                    updateMessage += "Can't update Purchase_order. //WSTMUPDATEINFO";
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE",updateMessage);
                    log.error("Can't get property" + updateMessage);
                    return jsonResponse.toString();
                }

            }catch (Exception e){
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't update data //WSTMUPDATEINFO. Cause: "+ e.getMessage());
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

        }catch (Exception e) {
            status = "ERROR";
            message = "400 Bad Request. JSON format";
            log.error("JSON format" + e.getMessage());
        }

        jsonResponse.addProperty("STATUS",status);
        jsonResponse.addProperty("MESSAGE",message);
        return jsonResponse.toString();
    }

    @RequestMapping(value = "v1/WSTMPOLICYUPDATEINFO", method = RequestMethod.POST , produces = "application/json; charset=utf-8")
    public @ResponseBody
    String WSTMPOLICYUPDATEINFO(@RequestBody String body){
        String status = "SUCCESS";
        String message = "";
        String property = "";
        JsonObject jsonResponse = new JsonObject();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonBody = (JSONObject) parser.parse(body);

            // check values
            for (Object key : jsonBody.keySet()) {
                String keyStr = (String)key;
                String keyvalue = jsonBody.get(keyStr).toString();
                //System.out.println(keyvalue);
                if(keyvalue == null || StringUtils.isBlank(keyvalue)) {
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE","400 Bad Request. Value is null, blank or empty.");
                    return jsonResponse.toString();
                }
            }

            String leadId="",user="",saleCode="",datetime="",policyNo ="",policyDate ="";
            try{
                //get property and check property name
                property = "LEADID";
                leadId = jsonBody.get("LEADID").toString();
                property = "USER";
                user = jsonBody.get("USER").toString();
                property = "SALECODE";
                saleCode = jsonBody.get("SALECODE").toString();

                property = "DATETIME";
                SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = dt.parse(jsonBody.get("DATETIME").toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime = df.format(date);

                property = "POLICYNO";
                policyNo = jsonBody.get("POLICYNO").toString();

                property = "POLICYDATE";
                policyDate = jsonBody.get("POLICYDATE").toString();

                SimpleDateFormat dt2 = new SimpleDateFormat("yyyyMMdd");
                Date date2 = dt2.parse(jsonBody.get("POLICYDATE").toString());
                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                policyDate = df2.format(date2);

            }catch (Exception e) {
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't get property " + property +". Please check your property name or value");
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

            try{
                String updateMessage ="";

                boolean updatePurchaseOrder = purchaseOrderDAO.updatePurchaseOrderPolicyNoFromBCIB(datetime,policyNo,policyDate,leadId);
                if(updatePurchaseOrder==false){
                    updateMessage += "Can't update Purchase_order. //WSTMUPDATEINFO";
                    jsonResponse.addProperty("STATUS","ERROR");
                    jsonResponse.addProperty("MESSAGE",updateMessage);
                    log.error("Can't get property" + updateMessage);
                    return jsonResponse.toString();
                }

            }catch (Exception e){
                jsonResponse.addProperty("STATUS","ERROR");
                jsonResponse.addProperty("MESSAGE","Can't update data //WSTMUPDATEINFO. Cause: "+ e.getMessage());
                log.error("Can't get property" + e.getMessage());
                return jsonResponse.toString();
            }

        }catch (Exception e) {
            status = "ERROR";
            message = "400 Bad Request. JSON format";
            log.error("JSON format" + e.getMessage());
        }

        jsonResponse.addProperty("STATUS",status);
        jsonResponse.addProperty("MESSAGE",message);
        return jsonResponse.toString();
    }

    public PurchaseOrderRegisterDAO getPurchaseOrderRegisterDAO() {
        return purchaseOrderRegisterDAO;
    }

    public void setPurchaseOrderRegisterDAO(PurchaseOrderRegisterDAO purchaseOrderRegisterDAO) {
        this.purchaseOrderRegisterDAO = purchaseOrderRegisterDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

}
