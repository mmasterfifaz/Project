package com.maxelyz.social.controller.admin;

import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoChannelDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class SocialAccountEditController {
    private static Logger log = Logger.getLogger(SocialAccountEditController.class);
    private static String REDIRECT_PAGE = "socialaccount.jsf";
    private static String SUCCESS = "socialaccount.xhtml?faces-redirect=true";
    private static String FAILURE = "socialaccountedit.xhtml";

    private SoAccount soAccount;
    private String mode;
    private String message;
    private String messageDup;
    private Integer soChannelId;
    private List<SoChannel> soChannels;

    @ManagedProperty(value="#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value="#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:socialaccount:view"))
        {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
            mode = "add";
            soAccount = new SoAccount();
            soAccount.setEnable(Boolean.TRUE);
            soAccount.setStatus(Boolean.TRUE);
        } else {
           mode = "edit";
           SoAccountDAO dao = getSoAccountDAO();
            soAccount = dao.findSoAccount(new Integer(selectedID));
           if (soAccount==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           } else {
               if (soAccount.getSoChannel()!=null) {
                   soChannelId = soAccount.getSoChannel().getId();
               }
           }
       }

       soChannels = soChannelDAO.findSoChannelStatus();
    }

    public void changeChannel(ActionEvent event) {
        soChannelId = soChannelId;
    }

    public boolean isSavePermitted() {
        boolean b;
        if (mode.equals("add")) {
            b= SecurityUtil.isPermitted("admin:socialaccount:add");

        } else {
            b= SecurityUtil.isPermitted("admin:socialaccount:edit");
        }
        return b;
    }

    public String saveAction() {
        messageDup = "";
        if(checkCode(soAccount)) {
            SoAccountDAO dao = getSoAccountDAO();
            String username = JSFUtil.getUserSession().getUserName();
            if (soChannelId!=null && soChannelId!=0) {
                soAccount.setSoChannel(new SoChannel(soChannelId));
            } else {
                soAccount.setSoChannel(null);
            }

            try {
                if (getMode().equals("add")) {
                    soAccount.setId(null);
                    soAccount.setEnable(true);
                    soAccount.setCreateBy(username);
                    soAccount.setCreateDate(new Date());
                    dao.create(soAccount);
                } else {
                    soAccount.setUpdateBy(username);
                    soAccount.setUpdateDate(new Date());
                    dao.edit(soAccount);
                }

                sendAPI(soAccount.getCode(),soAccount.getName(),soChannelDAO.findSoChannel(soChannelId).getName(),soAccount.getDescription(),soAccount.getKeyword(),soAccount.getPtRoom());
//            public void sendAPI(String sourceId, String sourceName, String sourceType, String description, String keywordsByComma, String language);
//            http://tipjer.com/applications/socialcrm_services/source_insupd/twitter_test1/twitter ทดสอบ 1/TWITTER/รายละเอียด1/เอไอเอส,iphone,สามจี/th


            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return FAILURE;
        }
    }

    public Boolean checkCode(SoAccount soAccount) {
        String code = soAccount.getCode();
        Integer id=0;
        if(soAccount.getId() != null)
            id = soAccount.getId();
        SoAccountDAO dao = getSoAccountDAO();

        Integer cnt = dao.checkSoAccountCode(code, id);
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

    public void sendAPI(String sourceId, String sourceName, String sourceType, String description, String keywordsByComma, String ptRoom) {
        List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
        String strURLs = "";

        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//            sdf.format(new Date());

//            strURLs = "http://tipjer.com/applications/socialcrm_services/source_insupd"
//                    +"/"+(sourceId)
//                    +"/"+(sourceName)
//                    +"/"+(sourceType)
//                    +"/"+(description)
//                    +"/"+(keywordsByComma)
//                    +"/"+(ptRoom);
//            System.out.println(strURLs);

//            strURLs = "http://tipjer.com/applications/socialcrm_services/source_insupd"
//                    +"/"+urlEncode(sourceId)
//                    +"/"+urlEncode(sourceName)
//                    +"/"+urlEncode(sourceType)
//                    +"/"+urlEncode(description)
//                    +"/"+urlEncode(keywordsByComma)
//                    +"/"+urlEncode(ptRoom);
//            System.out.println(strURLs);

            if (sourceType.equalsIgnoreCase("FACEBOOK")) {
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_fb_update"
                        +"/"+(sourceId)
                        +"/"+(sourceName)
                        +"/"+(description)
                        +"/"+(keywordsByComma);
                System.out.println(strURLs);
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_fb_update"
                        +"/"+urlEncode(sourceId)
                        +"/"+urlEncode(sourceName)
                        +"/"+urlEncode(description)
                        +"/"+urlEncode(keywordsByComma);
                System.out.println(strURLs);
            } else if (sourceType.equalsIgnoreCase("TWITTER")) {
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_tw_update_search"
                        +"/"+(sourceId)
                        +"/"+(sourceName)
                        +"/"+(description)
                        +"/"+(keywordsByComma)
                        +"/th";
//                        +"/"+urlEncode(twLang);
                System.out.println(strURLs);
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_tw_update_search"
                        +"/"+urlEncode(sourceId)
                        +"/"+urlEncode(sourceName)
                        +"/"+urlEncode(description)
                        +"/"+urlEncode(keywordsByComma)
                        +"/th";
//                        +"/"+urlEncode(twLang);
                System.out.println(strURLs);
            } else if (sourceType.equalsIgnoreCase("PANTIP")) {
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_pt_update"
                        +"/"+(sourceId)
                        +"/"+(sourceName)
                        +"/"+(description)
                        +"/"+(keywordsByComma)
                        +"/"+(ptRoom);
                System.out.println(strURLs);
                strURLs = "http://tipjer.com/applications/socialcrm_services/source_pt_update"
                        +"/"+urlEncode(sourceId)
                        +"/"+urlEncode(sourceName)
                        +"/"+urlEncode(description)
                        +"/"+urlEncode(keywordsByComma)
                        +"/"+urlEncode(ptRoom);
                System.out.println(strURLs);
            } else if (sourceType.equalsIgnoreCase("INSTAGRAM")) {

            }

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

    public SoAccount getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(SoAccount soAccount) {
        this.soAccount = soAccount;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }

    public Map<String, Integer> getSoChannelList() {
        return soChannelDAO.getSoChannelList();
    }

    public Integer getSoChannelId() {
        return soChannelId;
    }

    public void setSoChannelId(Integer soChannelId) {
        this.soChannelId = soChannelId;
    }

//    public String getSoChannelId() {
//        return soChannelId;
//    }
//
//    public void setSoChannelId(String soChannelId) {
//        this.soChannelId = soChannelId;
//    }

    public List<SoChannel> getSoChannels() {
        return soChannels;
    }

    public void setSoChannels(List<SoChannel> soChannels) {
        this.soChannels = soChannels;
    }

}
