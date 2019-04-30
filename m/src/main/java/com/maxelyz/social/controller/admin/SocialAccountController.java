package com.maxelyz.social.controller.admin;

import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.entity.SoAccount;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@RequestScoped
public class SocialAccountController implements Serializable {

    private static Logger log = Logger.getLogger(SocialAccountController.class);
    private static String REFRESH = "socialaccount.xhtml?faces-redirect=true";
    private static String EDIT = "socialaccountedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoAccount> soAccounts;
    private SoAccount soAccount;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:socialaccount:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        soAccounts = soAccountDAO.findSoAccountEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:socialaccount:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:socialaccount:delete");
    }

    public List<SoAccount> getList() {
        return getSoAccounts();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soAccount = soAccountDAO.findSoAccount(item.getKey());
                    soAccount.setEnable(false);
                    soAccountDAO.edit(soAccount);

//                    sendAPI("XXXXX",soAccount.getCode(),soAccount.getName(),soAccount.getKeyword());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
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

    public void sendAPI(String type, String code, String name, String keyword) {
        List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
        String strURLs;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            sdf.format(new Date());

            type = URLEncoder.encode(type, "UTF-8");
            code = URLEncoder.encode(code, "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            keyword = URLEncoder.encode(keyword, "UTF-8");

            strURLs = "http://tipjer.com/applications/socialcrm_services/attribute_insupd"+"/"+type+"/"+code+"/"+name+"/"+keyword;
//            strURLs = "http://tipjer.com/applications/socialcrm_services/attribute_insupd"+"/"+type+"/"+code+"/"+"bb"+"/"+"cc";
            System.out.println(strURLs);
            String response = readFromUrl(strURLs);
            System.out.println(response);
//            StringTemplateDTO fl = new StringTemplateDTO(response);
//            list.add(fl);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SoAccount> getSoAccounts() {
        return soAccounts;
    }

    public void setSoAccounts(List<SoAccount> soAccounts) {
        this.soAccounts = soAccounts;
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

}
