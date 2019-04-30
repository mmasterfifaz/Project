package com.maxelyz.social.controller.admin;

import com.google.gson.Gson;
import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.social.controller.SoEnumDTO;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@RequestScoped
public class SocialCaseTypeController_verJSON implements Serializable {

    private static Logger log = Logger.getLogger(SocialCaseTypeController_verJSON.class);
    private static String REFRESH = "socialcasetype.xhtml?faces-redirect=true";
    private static String EDIT = "socialcasetypeedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();

    private List<SoEnumDTO> enumDTOs;
    private SoEnumDTO enumDTO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:socialcasetype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }

        List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
        String strURLs;
        strURLs = "http://tipjer.com/applications/socialcrm_services/attribute_get_ids_keywords/CASETYPE";

        enumDTOs = new ArrayList<SoEnumDTO>();
        try {
            JSONArray jss = readJsonArrayFromUrl(strURLs);

            for (int i = 0; i < jss.length(); i++) {
                JSONObject js = jss.getJSONObject(i);
                System.out.println(String.valueOf(i) + ':' + js.toString());
                StringTemplateDTO fl = new StringTemplateDTO(String.valueOf(i) + ':' + js.toString());
                list.add(fl);

                Gson gson = new Gson();
                SoEnumDTO enumDTO = gson.fromJson(js.toString(), SoEnumDTO.class);
                enumDTOs.add(enumDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

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

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetype:delete");
    }

     public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
//        CaseTypeDAO dao = getCaseTypeDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
//                    caseType = dao.findCaseType(item.getKey());
//                    caseType.setEnable(false);
//                    dao.edit(caseType);
                    System.out.println(item.getKey());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SoEnumDTO> getEnumDTOs() {
        return enumDTOs;
    }

    public void setEnumDTOs(List<SoEnumDTO> enumDTOs) {
        this.enumDTOs = enumDTOs;
    }

    public SoEnumDTO getEnumDTO() {
        return enumDTO;
    }

    public void setEnumDTO(SoEnumDTO enumDTO) {
        this.enumDTO = enumDTO;
    }

    public List<SoEnumDTO> getList() {
        return getEnumDTOs();
    }
}
