package com.maxelyz.core.controller.admin;

import com.google.gson.Gson;
import com.maxelyz.core.model.dao.MediaPlanDAO;
import com.maxelyz.core.model.entity.CustomerField;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.FileTemplateMappingPK;
import com.maxelyz.core.model.entity.MediaPlan;
import com.maxelyz.core.model.entity.MediaPlanImportLog;
import com.maxelyz.core.model.entity.MediaPlanTemp;
import com.maxelyz.core.model.entity.ModuleAuditLog;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class MediaPlanImportController {

    private static Logger log = Logger.getLogger(MediaPlanImportController.class);
    private static String SUCCESS = "mediaplan.xhtml?faces-redirect=true";
    private static String FAILURE = "mediaplanimport.xhtml";
    private static File dataFile;
    private Integer step = 1;
    private String message = "";
    private MediaPlanImportLog mediaPlanImportLog;
    private List<MediaPlanTemp> totalRecords = new ArrayList<MediaPlanTemp>();
    private List<MediaPlanTemp> successRecords = new ArrayList<MediaPlanTemp>();
    private List<MediaPlanTemp> failRecords = new ArrayList<MediaPlanTemp>();
    List<MediaPlanTemp> mptList = new ArrayList<MediaPlanTemp>();
    private List<MediaPlanImportLog> importHistories = new ArrayList<MediaPlanImportLog>();
    private String idSuccess = "", idFail = "";
    private List<String[]> dataListStringPopup;
    private String strQuery;
    private List<FileTemplateMapping> fileTemplateMappings = new ArrayList<FileTemplateMapping>();

    @ManagedProperty(value = "#{mediaPlanDAO}")
    private MediaPlanDAO mediaPlanDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:mediaplan:edit")) {
            SecurityUtil.redirectUnauthorize();
        }
        importHistories = mediaPlanDAO.findMediaPlanImortLogEntities();
        fileTemplateMappings = getFileTemplateMappingsList();
    }

    public void uploadCompleteListener(FileUploadEvent event) {
        Date date = new Date();
        try {
            UploadedFile item = event.getUploadedFile();

            String fileName = item.getName();
            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            if (!fileName.isEmpty() && fileName.indexOf(",") != -1) {
                fileName = fileName.replace(",", "");
            }

            this.dataFile = new File(JSFUtil.getuploadPath() + date.getTime() + "_" + fileName);
            this.copy(item.getInputStream(), this.dataFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void copy(InputStream in, File file) {
        try {
            if (!file.exists()) {
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFileUpload() {
        if (dataFile != null) {
            dataFile.delete();
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("admin:mediaplan:add");
    }

    public void nextStep1ToStep2(ActionEvent event) {
        message = "";
        step = 2;
        try {
            mediaPlanImportLog = new MediaPlanImportLog();
            mediaPlanImportLog.setFileName(dataFile.getName());
            mediaPlanImportLog.setImportBy(JSFUtil.getUserSession().getUsers().getLoginName());
            mediaPlanImportLog.setImportDate(new Date());
            insertMediaPlanFromFile(dataFile);
        } catch (Exception e) {
            log.error(e);
            message = " Data could not insert";
            clearFileUpload();
            step = 1;
        }

    }

    private void insertMediaPlanFromFile(File dataFile) throws Exception {
        String charset, fieldEnclosed, fieldDelimiter;
        Long totalDup = 0l;
        Long totalOpOut = 0l;
        List<MediaPlan> mpList = new ArrayList<MediaPlan>();
        mptList = new ArrayList<MediaPlanTemp>();

        Map<Integer, FileTemplateMapping> fileMap = new TreeMap<Integer, FileTemplateMapping>();
        Map<FileTemplateMapping, String> dataMap = new HashMap<FileTemplateMapping, String>(); //customer_field_name, datafromfile
        fileTemplateMappings = getFileTemplateMappingsList();

        for (FileTemplateMapping fileTemplateMapping : fileTemplateMappings) { //sort by FileColumnNo
            fileMap.put(fileTemplateMapping.getFileColumnNo(), fileTemplateMapping);
        }

        BufferedReader dataIns = null;
        try {
            dataIns = new BufferedReader(
                    new InputStreamReader(new FileInputStream(dataFile), "tis-620"));

            String data, column;
            int recordNo = 0;

            //Skip first line
            dataIns.readLine();//skip

            while ((data = dataIns.readLine()) != null) {
                String[] st = data.split(",");
                int noofColumn = st.length;

                Integer columnNo = 0;

                //get column
                FileTemplateMapping mapping = new FileTemplateMapping();
                while (columnNo < noofColumn) {

                    column = st[columnNo]; ////read column data ->idx begin at 0
                    columnNo++;
//                    if (fileMap.get(columnNo) != null) { //if has column in FileTemplateMapping ->begin at 1
//                        if (!fieldEnclosed.equals("")) {
//                            if (column.startsWith(fieldEnclosed)) {
//                                column = column.substring(1);
//                            }
//                            if (column.endsWith(fieldEnclosed)) {
//                                column = column.substring(0, column.length() - 1);
//                            }
//                        }
                    mapping = new FileTemplateMapping();
                    mapping = fileMap.get(columnNo);
                    dataMap.put(mapping, column);
//                    }
                }
                MediaPlanTemp mpt = new MediaPlanTemp();
                mpt.setBadFormat(Boolean.FALSE);
                mpt.setImportStatus("success");
                mpt.setReason("insert");
                boolean cc = true;
                for (Map.Entry<FileTemplateMapping, String> e : dataMap.entrySet()) {

                    String customerFieldName = e.getKey().getCustomerField().getMappingField();
                    String type = e.getKey().getFieldType();
                    String isRequired = e.getKey().getCustomerField().getType();    //Mandatory or not?
                    String pattern = e.getKey().getPattern();
                    String defaultValue = e.getKey().getDefaultValue();
                    cc = true;
                    //Check Bad Format From template mapping
                    if ((e.getValue() == null || (e.getValue() != null && e.getValue().isEmpty())) && defaultValue != null) {
                        e.setValue(defaultValue);
                        cc = false;
                    }
                    if (e.getValue() != null && e.getValue().isEmpty()) {
                        cc = false;
                        e.setValue("");
                    }
                    if (isRequired != null && isRequired.equalsIgnoreCase("Mandatory")) {
                        try {
                            if (e.getValue() == null || (e.getValue() != null && e.getValue().isEmpty())) {
                                Integer.parseInt("--");
                            }
                            if (e.getKey().getCustomerField().getMappingField().toString().equalsIgnoreCase("spot_ref_id")) {
                                MediaPlan existing = new MediaPlan();
                                existing = mediaPlanDAO.findExistingMediaPlanByRefID(e.getValue().toString());
                                if (existing != null) {
                                    mpt.setReason("update-" + existing.getId());
                                }
                            }
                        } catch (Exception exn) {
                            mpt.setBadFormat(Boolean.TRUE);
                            mpt.setImportStatus("fail");
                            mpt.setReason(e.getKey().getCustomerField().getName() + " is required");
                            System.out.println("Data must not be null");
                        }
                    }
                    if (cc) {
                        if (type.equals("Date")) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                                Date dateCheck = sdf.parse(e.getValue());
                                e.setValue(sdf.format(dateCheck));
                            } catch (Exception exn) {
                                mpt.setBadFormat(Boolean.TRUE);
                                mpt.setImportStatus("fail");
                                mpt.setReason(e.getKey().getCustomerField().getName() + " have bad Format from Date Type Data");
                                System.out.println("Bad Format from Date Type Data");
                            }
                        } else if (type.equals("int")) {
                            try {
                                Integer.parseInt(e.getValue());
                            } catch (Exception exn) {
                                mpt.setBadFormat(Boolean.TRUE);
                                mpt.setImportStatus("fail");
                                mpt.setReason(e.getKey().getCustomerField().getName() + "must be a number");
                                System.out.println("Bad Format from int Type Data");
                            }
                        } else if (type.equals("double")) {
                            try {
                                String dd = e.getValue().replace(",", "");
                                Double.parseDouble(dd);
                            } catch (Exception exn) {
                                mpt.setBadFormat(Boolean.TRUE);
                                mpt.setImportStatus("fail");
                                mpt.setReason(e.getKey().getCustomerField().getName() + " must be a double");
                                System.out.println("Bad Format from double Type Data");
                            }
                        } else if (type.equals("String")) {
                            try {
                                if (pattern != null && !pattern.isEmpty()) {
                                    int l = Integer.parseInt(pattern);
                                    if (e.getValue().toString().length() > l) {
                                        Integer.parseInt("--");
                                    }
                                }
                            } catch (Exception exn) {
                                mpt.setBadFormat(Boolean.TRUE);
                                mpt.setImportStatus("fail");
                                mpt.setReason(e.getKey().getCustomerField().getName() + " is limited to " + e.getKey().getPattern() + " charactors");
                                System.out.println("Bad Format from double Type Data");
                            }
                        } else if (type.equals("Day")) {
                            try {
                                if (pattern != null && !pattern.isEmpty()) {
                                    if (e.getValue().toString().equals("MON") || e.getValue().toString().equals("TUE") || e.getValue().toString().equals("WED")
                                            || e.getValue().toString().equals("THU") || e.getValue().toString().equals("FRI") || e.getValue().toString().equals("SAT")
                                            || e.getValue().toString().equals("SUN")) {
                                    } else {
                                        Integer.parseInt("--");
                                    }
                                }
                            } catch (Exception exn) {
                                mpt.setBadFormat(Boolean.TRUE);
                                mpt.setImportStatus("fail");
                                mpt.setReason(e.getKey().getCustomerField().getName() + " format must be DDD (MON,TUE,WED,THU,FRI,SAT,SUN)");
                                System.out.println("Bad Format from day Type Data");
                            }
                        }
                    }
                    mpt = this.setMediaPlanTempValue(mpt, customerFieldName, e.getValue());      // Mapping Value into field
                }
                recordNo++;
                try {
                    if (mpt != null) {
                        mptList.add(mpt);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Record ref ID : " + mpt.getSpotRefId());

                }
            }
            int succRec = 0;
            int failRec = 0;
            totalRecords.clear();
            successRecords.clear();
            failRecords.clear();
            idSuccess = "";
            idFail = "";
            for (MediaPlanTemp obj : mptList) {
                mediaPlanDAO.create(obj); //insert new media plan temp
                totalRecords.add(obj);
                if (obj.getImportStatus() != null && obj.getImportStatus().toString().equalsIgnoreCase("success")) {
                    succRec++;
                    successRecords.add(obj);
                    idSuccess += obj.getId() + ",";
                } else {
                    failRec++;
                    failRecords.add(obj);
                    idFail += obj.getId() + ",";
                }
            }
            mediaPlanImportLog.setTotalRecord(recordNo);
            mediaPlanImportLog.setRejectRecord(failRec);
            mediaPlanImportLog.setSuccessRecord(succRec);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataIns != null) {
                    dataIns.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private List<FileTemplateMapping> getFileTemplateMappingsList() {
        fileTemplateMappings = new ArrayList<FileTemplateMapping>();
        FileTemplateMapping fileTemplateMapping = new FileTemplateMapping();
        FileTemplateMappingPK pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(1);
        pkId.setCustomerField(1);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        fileTemplateMapping.setFileColumnNo(1);
        CustomerField customerField = new CustomerField();
        customerField.setName("Spot Reference ID");
        customerField.setMappingField("spot_ref_id");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("50");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(2);
        pkId.setCustomerField(2);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(2);
        customerField.setName("Spot Type");
        customerField.setMappingField("spot_type");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("10");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(3);
        pkId.setCustomerField(3);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(3);
        customerField.setName("Day of spot");
        customerField.setMappingField("day_of_spot");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("Day");
        fileTemplateMapping.setPattern("3");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(4);
        pkId.setCustomerField(4);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(4);
        customerField.setName("Date of spot");
        customerField.setMappingField("date_of_spot");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("Date");
        fileTemplateMapping.setPattern("dd-MMM-yy");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(5);
        pkId.setCustomerField(5);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(5);
        customerField.setName("Spot Telephone Number");
        customerField.setMappingField("spot_telephone_number");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("20");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(6);
        pkId.setCustomerField(6);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(6);
        customerField.setName("Channel");
        customerField.setMappingField("channel");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("20");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(7);
        pkId.setCustomerField(7);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(7);
        customerField.setName("Program name");
        customerField.setMappingField("program_name");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("100");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(8);
        pkId.setCustomerField(8);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(8);
        customerField.setName("Program type");
        customerField.setMappingField("program_type");
        customerField.setType("Mandatory");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("50");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(9);
        pkId.setCustomerField(9);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(9);
        customerField.setName("Show Time Start");
        customerField.setMappingField("show_time_start");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("time");
        fileTemplateMapping.setPattern("hh:mm:ss");
        fileTemplateMapping.setDefaultValue("00:00:00");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(10);
        pkId.setCustomerField(10);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(10);
        customerField.setName("Show Time End");
        customerField.setMappingField("show_time_end");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("time");
        fileTemplateMapping.setPattern("hh:mm:ss");
        fileTemplateMapping.setDefaultValue("00:00:00");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(11);
        pkId.setCustomerField(11);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(11);
        customerField.setName("Actual on Air time");
        customerField.setMappingField("actual_on_air_time");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("time");
        fileTemplateMapping.setPattern("hh:mm:ss");
        fileTemplateMapping.setDefaultValue("00:00:00");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(12);
        pkId.setCustomerField(12);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(12);
        customerField.setName("Section Break");
        customerField.setMappingField("section_break");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("50");
        fileTemplateMapping.setDefaultValue(null);
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(13);
        pkId.setCustomerField(13);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(13);
        customerField.setName("Copy Length");
        customerField.setMappingField("copy_length");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("int");
        fileTemplateMapping.setPattern(null);
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(14);
        pkId.setCustomerField(14);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(14);
        customerField.setName("Tape");
        customerField.setMappingField("tape");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("100");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(15);
        pkId.setCustomerField(15);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(15);
        customerField.setName("Net Cost per Spot");
        customerField.setMappingField("net_cost_per_spot");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("double");
        fileTemplateMapping.setPattern(null);
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(16);
        pkId.setCustomerField(16);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(16);
        customerField.setName("Status");
        customerField.setMappingField("status");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("15");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(17);
        pkId.setCustomerField(17);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(17);
        customerField.setName("Media Agency Remark");
        customerField.setMappingField("media_agency_remark");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(18);
        pkId.setCustomerField(18);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(18);
        customerField.setName("MTL Remark 1");
        customerField.setMappingField("mtl_remark1");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(19);
        pkId.setCustomerField(19);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(19);
        customerField.setName("MTL Remark 2");
        customerField.setMappingField("mtl_remark2");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(20);
        pkId.setCustomerField(20);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(20);
        customerField.setName("MTL Remark 3");
        customerField.setMappingField("mtl_remark3");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(21);
        pkId.setCustomerField(21);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(21);
        customerField.setName("MTL Remark 4");
        customerField.setMappingField("mtl_remark4");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(22);
        pkId.setCustomerField(22);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(22);
        customerField.setName("Product Assign");
        customerField.setMappingField("Product_Assign");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        fileTemplateMapping = new FileTemplateMapping();
        pkId = new FileTemplateMappingPK();
        pkId.setFileTemplateId(23);
        pkId.setCustomerField(23);
        fileTemplateMapping.setFileTemplateMappingPK(pkId);
        customerField = new CustomerField();
        fileTemplateMapping.setFileColumnNo(23);
        customerField.setName("ProductCode");
        customerField.setMappingField("ProductCode");
        customerField.setType("input");
        fileTemplateMapping.setCustomerField(customerField);
        fileTemplateMapping.setFieldType("String");
        fileTemplateMapping.setPattern("500");
        fileTemplateMappings.add(fileTemplateMapping);
        return fileTemplateMappings;
    }

    private MediaPlanTemp setMediaPlanTempValue(MediaPlanTemp mpt, String key, String value) {
        if (value != null) {
            value = value.trim();
        }
        try {
            if (key.equals("spot_ref_id")) {
                mpt.setSpotRefId(value);
            } else if (key.equals("spot_type")) {
                mpt.setSpotType(value);
            } else if (key.equals("day_of_spot")) {
                mpt.setDayOfSpot(value);
            } else if (key.equals("date_of_spot")) {
                mpt.setDateOfSpot(value);
            } else if (key.equals("spot_telephone_number")) {
                mpt.setSpotTelephoneNumber(value);
            } else if (key.equals("channel")) {
                mpt.setChannel(value);
            } else if (key.equals("program_name")) {
                mpt.setProgramName(value);
            } else if (key.equals("program_type")) {
                mpt.setProgramType(value);
            } else if (key.equals("show_time_start")) {
                mpt.setShowTimeStart(value);
            } else if (key.equals("show_time_end")) {
                mpt.setShowTimeEnd(value);
            } else if (key.equals("actual_on_air_time")) {
                mpt.setActualOnAirTime(value);
            } else if (key.equals("section_break")) {
                mpt.setSectionBreak(value);
            } else if (key.equals("copy_length")) {
                try {
                    mpt.setCopyLength(Integer.parseInt(value));
                } catch (Exception ex) {
                    mpt.setCopyLength(null);
                }
            } else if (key.equals("tape")) {
                mpt.setTape(value);
            } else if (key.equals("net_cost_per_spot")) {
                mpt.setNetCostPerSpot(value);
            } else if (key.equals("status")) {
                mpt.setStatus(value);
            } else if (key.equals("media_agency_remark")) {
                mpt.setMediaAgencyRemark(value);
            } else if (key.equals("mtl_remark1")) {
                mpt.setMtlRemark1(value);
            } else if (key.equals("mtl_remark2")) {
                mpt.setMtlRemark2(value);
            } else if (key.equals("mtl_remark3")) {
                mpt.setMtlRemark3(value);
            } else if (key.equals("mtl_remark4")) {
                mpt.setMtlRemark4(value);
            } else if (key.equals("Product_Assign")) {
                mpt.setProductAssign(value);
            } else if (key.equals("ProductCode")) {
                mpt.setProductCode(value);
            }
            return mpt;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public String saveStep2Listener() throws Exception {
        String username = JSFUtil.getUserSession().getUsers().getLoginName();
        try {
            MediaPlan mp;
            //Insert to module audit log
            ModuleAuditLog log;
            for (MediaPlanTemp obj : mptList) {
                if (obj.getImportStatus() != null && obj.getImportStatus().equalsIgnoreCase("success")) {
                    mp = new MediaPlan();
                    mp = mediaplanTemptoMediaPlan(obj);
                    mp.setEnable(Boolean.TRUE);
                    mp.setUpdateBy(username);
                    mp.setUpdateDate(new Date());
                    log = new ModuleAuditLog();
                    log.setModuleName("media_plan");
                    if (obj.getReason().equalsIgnoreCase("insert")) {
                        mp.setCreateBy(username);
                        mp.setCreateDate(new Date());
                        mediaPlanDAO.create(mp);
                        log.setActionType("Add new record");
                    } else {
                        String ids = obj.getReason();
//                        update-31
                        Integer id = Integer.parseInt(ids.substring(7));
                        mp.setId(id);
                        mediaPlanDAO.edit(mp);
                        log.setActionType("Update record");
                    }
                    log.setRefNo(mp.getId());
                    log.setActionDate(new Date());
                    log.setActionBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    //Create JSON MediaPlan
                    Gson gson = new Gson();
                    String tempJson = gson.toJson(mp);
                    String[] b = tempJson.split("spotRefId");
                    String json = "{\"spotRefId" + b[1];
                    log.setActionDetail(json);
                    mediaPlanDAO.create(log);
                }
            }
            mediaPlanImportLog.setStatus("success");
            mediaPlanDAO.create(mediaPlanImportLog);
        } catch (Exception e) {
            log.error(e);
            message = " Data could not insert";
            mediaPlanImportLog.setStatus("fail");
            mediaPlanDAO.create(mediaPlanImportLog);
            deleteMediaPlanTemp();
            clearFileUpload();
            step = 2;
            return FAILURE;
        }
        return SUCCESS;
    }

    private MediaPlan mediaplanTemptoMediaPlan(MediaPlanTemp temp) throws ParseException {
        MediaPlan mediaPlan = new MediaPlan();
        mediaPlan.setSpotRefId(temp.getSpotRefId());
        mediaPlan.setSpotType(temp.getSpotType());
        mediaPlan.setDayOfSpot(temp.getDayOfSpot());
        DateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        String dateAsString = temp.getDateOfSpot().toString() + " 00:00:00";
//        System.out.println("Log > date : " + dateAsString);
        Date date = sourceFormat.parse(dateAsString);
        mediaPlan.setDateOfSpot(date);
        mediaPlan.setSpotTelephoneNumber(temp.getSpotTelephoneNumber());
        mediaPlan.setChannel(temp.getChannel());
        mediaPlan.setProgramName(temp.getProgramName());
        mediaPlan.setProgramType(temp.getProgramType());
        mediaPlan.setShowTimeStart(temp.getShowTimeStart());
        mediaPlan.setShowTimeEnd(temp.getShowTimeEnd());
        mediaPlan.setActualOnAirTime(temp.getActualOnAirTime());
        mediaPlan.setSectionBreak(temp.getSectionBreak());
        mediaPlan.setCopyLength(temp.getCopyLength());
        mediaPlan.setTape(temp.getTape());
        BigDecimal net = new BigDecimal("0.0");
        if (temp.getNetCostPerSpot() != null && !temp.getNetCostPerSpot().isEmpty()) {
            net = new BigDecimal(temp.getNetCostPerSpot());
        }
        mediaPlan.setNetCostPerSpot(net);
        mediaPlan.setStatus(temp.getStatus());
        mediaPlan.setMediaAgencyRemark(temp.getMediaAgencyRemark());
        mediaPlan.setMtlRemark1(temp.getMtlRemark1());
        mediaPlan.setMtlRemark2(temp.getMtlRemark2());
        mediaPlan.setMtlRemark3(temp.getMtlRemark3());
        mediaPlan.setMtlRemark4(temp.getMtlRemark4());
        mediaPlan.setProductAssign(temp.getProductAssign());
        mediaPlan.setProductCode(temp.getProductCode());
        return mediaPlan;
    }

    public void previousStep2ToStep1(ActionEvent event) {
        step = 1;
        this.deleteMediaPlanTemp();
    }

    private void deleteMediaPlanTemp() {
        try {
            mediaPlanDAO.deleteMediaPlanTemp(mptList);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void importHistoryListener(ActionEvent event) {
        importHistories = mediaPlanDAO.findMediaPlanImortLogEntities();
    }

    public void getLoadedList(ActionEvent event) {
        if (JSFUtil.getRequestParameterMap("mode") != null && !JSFUtil.getRequestParameterMap("mode").isEmpty()) {
            if (JSFUtil.getRequestParameterMap("mode").toString().equalsIgnoreCase("success")) {
                dataListStringPopup = this.getMediaPlanDAO().getLoadedList(idSuccess);
            } else {
                dataListStringPopup = this.getMediaPlanDAO().getLoadedList(idFail);
            }
        }
    }

    ////////////////////////////////////////////////// GET SET METHOD /////////////////////////////////////////////////
    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static void setSUCCESS(String SUCCESS) {
        MediaPlanImportController.SUCCESS = SUCCESS;
    }

    public static String getFAILURE() {
        return FAILURE;
    }

    public static void setFAILURE(String FAILURE) {
        MediaPlanImportController.FAILURE = FAILURE;
    }

    public static File getDataFile() {
        return dataFile;
    }

    public static void setDataFile(File dataFile) {
        MediaPlanImportController.dataFile = dataFile;
    }

    public MediaPlanDAO getMediaPlanDAO() {
        return mediaPlanDAO;
    }

    public void setMediaPlanDAO(MediaPlanDAO mediaPlanDAO) {
        this.mediaPlanDAO = mediaPlanDAO;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MediaPlanImportLog getMediaPlanImportLog() {
        return mediaPlanImportLog;
    }

    public void setMediaPlanImportLog(MediaPlanImportLog mediaPlanImportLog) {
        this.mediaPlanImportLog = mediaPlanImportLog;
    }

    public List<MediaPlanTemp> getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(List<MediaPlanTemp> totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<MediaPlanTemp> getSuccessRecords() {
        return successRecords;
    }

    public void setSuccessRecords(List<MediaPlanTemp> successRecords) {
        this.successRecords = successRecords;
    }

    public List<MediaPlanTemp> getFailRecords() {
        return failRecords;
    }

    public void setFailRecords(List<MediaPlanTemp> failRecords) {
        this.failRecords = failRecords;
    }

    public List<MediaPlanImportLog> getImportHistories() {
        return importHistories;
    }

    public void setImportHistories(List<MediaPlanImportLog> importHistories) {
        this.importHistories = importHistories;
    }

    public List<String[]> getDataListStringPopup() {
        return dataListStringPopup;
    }

    public void setDataListStringPopup(List<String[]> dataListStringPopup) {
        this.dataListStringPopup = dataListStringPopup;
    }

    public List<FileTemplateMapping> getFileTemplateMappings() {
        return fileTemplateMappings;
    }

    public void setFileTemplateMappings(List<FileTemplateMapping> fileTemplateMappings) {
        this.fileTemplateMappings = fileTemplateMappings;
    }

}
