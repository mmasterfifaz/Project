package com.maxelyz.social.controller.admin;

import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class SocialCaseTypeEditController {
    private static Logger log = Logger.getLogger(SocialCaseTypeEditController.class);
    private static String REDIRECT_PAGE = "socialcasetype.jsf";
    private static String SUCCESS = "socialcasetype.xhtml?faces-redirect=true";
    private static String FAILURE = "socialcasetypeedit.xhtml";

    private SoCaseType soCaseType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:socialcasetype:view"))
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
            mode = "add";
            soCaseType = new SoCaseType();
            soCaseType.setSoCaseType(null);
            soCaseType.setEnable(Boolean.TRUE);
            soCaseType.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            SoCaseTypeDAO dao = getSoCaseTypeDAO();
            soCaseType = dao.findSoCaseType(new Integer(selectedID));
            if (soCaseType==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
       }
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:socialcasetype:add");
        } else {
            return SecurityUtil.isPermitted("admin:socialcasetype:edit");
        }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(soCaseType)) {
            SoCaseTypeDAO dao = getSoCaseTypeDAO();
            String username = JSFUtil.getUserSession().getUserName();

            try {
                if (getMode().equals("add")) {
                    soCaseType.setId(null);
                    soCaseType.setCreateBy(username);
                    soCaseType.setCreateDate(new Date());
                    dao.create(soCaseType);
                } else {
                    soCaseType.setUpdateBy(username);
                    soCaseType.setUpdateDate(new Date());
                    dao.edit(soCaseType);
                }

//                sendAPI("CASETYPE",soCaseType.getCode(),soCaseType.getName(),soCaseType.getKeyword());

            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
//                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;
//            return FAILURE;
        }
    }

    public Boolean checkCode(SoCaseType soCaseType) {
        String code = soCaseType.getCode();
        Integer id=0;
        if(soCaseType.getId() != null)
            id = soCaseType.getId();
        SoCaseTypeDAO dao = getSoCaseTypeDAO();

        Integer cnt = dao.checkSoCaseTypeCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
   
    public String backAction() {
        return SUCCESS;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            System.out.println(jsonText);
            return jsonText;
        } finally {
            is.close();
        }
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            System.out.println(jsonText);
            JSONArray jArray = new JSONArray(jsonText);
            return jArray;
        } finally {
            is.close();
        }
    }

    public String urlEncode(String s) {
        try {
            s = URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return s;
    }

    public void sendAPI(String type, String code, String name, String keywordsByComma) {
        List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
        String strURLs;

        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//            sdf.format(new Date());

            strURLs = "http://tipjer.com/applications/socialcrm_services/attribute_insupd"
                    +"/"+(type)
                    +"/"+(code)
                    +"/"+(name)
                    +"/"+(keywordsByComma);
            System.out.println(strURLs);

            strURLs = "http://tipjer.com/applications/socialcrm_services/attribute_insupd"
                    +"/"+urlEncode(type)
                    +"/"+urlEncode(code)
                    +"/"+urlEncode(name)
                    +"/"+urlEncode(keywordsByComma);
            System.out.println(strURLs);

            String response = readFromUrl(strURLs);
            System.out.println(response);
//            StringTemplateDTO fl = new StringTemplateDTO(response);
//            list.add(fl);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SoCaseType getSoCaseType() {
        return soCaseType;
    }

    public void setSoCaseType(SoCaseType soCaseType) {
        this.soCaseType = soCaseType;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }


}
