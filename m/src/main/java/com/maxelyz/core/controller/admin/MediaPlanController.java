package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.MediaPlanDAO;
import com.maxelyz.core.model.entity.MediaPlan;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

@ManagedBean
@RequestScoped
@ViewScoped
public class MediaPlanController implements Serializable {

    private static Logger log = Logger.getLogger(MediaPlanController.class);
    private static String REFRESH = "mediaplan.xhtml?faces-redirect=true";
    private static String EDIT = "mediaplanedit.xhtml";
    private static String IMPORT = "mediaplanimport.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<MediaPlan> mediaplans;
    private String spotRefIDKey = "", channelKey = "", programNameKey = "";
    private Date mediaDatetoKey, mediaDatefromKey;

    protected boolean decorateXLSStringLine = false;
    protected boolean decorateXLSEmptyWhenNull = false;
    protected int[] decorateXLSStringLineColumnIdList = {};

    @ManagedProperty(value = "#{mediaPlanDAO}")
    private MediaPlanDAO mediaPlanDAO;
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:mediaplan:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        mediaplans = new ArrayList<MediaPlan>();
        MediaPlanDAO dao = getMediaPlanDAO();
        mediaplans = dao.findMediaPlanEntities();
    }

    public void searchActionListener(ActionEvent event) {
        this.search();
    }

    private void search() {
        mediaplans = mediaPlanDAO.findMediaPlanFromSearch(spotRefIDKey, channelKey, programNameKey, mediaDatefromKey, mediaDatetoKey);
    }

    public String editAction() {
        return EDIT;
    }

    public String importAction() {
        return IMPORT;
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:mediaplan:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:mediaplan:delete");
    }

    public void initializeListener() {

    }

    public void generateXLS() {
        //response.setContentType("application/vnd.ms-excel");
        //response.setCharacterEncoding("UTF-8");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US);
        String fName = "media_plan_result_" + sdf.format(new Date()) + ".xls";
        PrintWriter out = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Media Plan");
            sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle dataStyleFloat = workbook.createCellStyle();
            dataStyleFloat.setBorderTop(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderRight(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyleFloat.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyleFloat.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFDataFormat df = workbook.createDataFormat();
            dataStyleFloat.setDataFormat(df.getFormat("#,##0.00"));

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fName + "\"");

            HSSFRow row;
            HSSFCell cell;
            ResultSetMetaData rsmd = rs.getMetaData();
            int numOfCol = rsmd.getColumnCount();
            int numStartRow = 0;
            if (rs.next()) {
                 row = sheet.createRow(numStartRow);
                for (int i = 2; i <= numOfCol; i++) {
                    cell = row.createCell(i - 2);
                    cell.setCellStyle(dataStyle);
                    cell.setCellValue(rsmd.getColumnName(i));
                }
                numStartRow++;
                do {
                    row = sheet.createRow(numStartRow);
                    for (int i = 2; i <= 24; i++) {
                        cell = row.createCell(i - 2);
                        cell.setCellStyle(dataStyle);
                        if (rs.getString(i) != null && !rs.getString(i).equalsIgnoreCase("null") && !rs.getString(i).isEmpty()) {
                            if (i == 5) {
                                cell.setCellValue(sdf1.format(rs.getDate(i)));
                            } else {
                                cell.setCellValue(rs.getString(i));
                            }
                        } else {
                            cell.setCellValue("");  
                        }
                    }
                    numStartRow++;
                } while (rs.next());
            }

            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
//            System.out.println("End Export File");

        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                log.error(ex);
            }
        }
    }

    private String getExcelQuery() {
        String where = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        if (spotRefIDKey != null && !spotRefIDKey.isEmpty()) {
            where += (where == null || (where != null && where.isEmpty())) ? " where spot_ref_id like '%" + spotRefIDKey + "%' " : " and spot_ref_id like '%" + spotRefIDKey + "%' ";
        }
        if (channelKey != null && !channelKey.isEmpty()) {
            where += (where == null || (where != null && where.isEmpty())) ? " where channel like '%" + channelKey + "%' " : " and channel like '%" + channelKey + "%' ";
        }
        if (programNameKey != null && !programNameKey.isEmpty()) {
            where += (where == null || (where != null && where.isEmpty())) ? " where program_name like '%" + programNameKey + "%' " : " and program_name like '%" + programNameKey + "%' ";
        }
        if (mediaDatefromKey != null) {
            String df = sdf.format(mediaDatefromKey);
            where += (where == null || (where != null && where.isEmpty())) ? " where date_of_spot >= '" + df + "' " : " and date_of_spot >= '" + df + "' ";
        }
        if (mediaDatetoKey != null) {
            String dt = sdf.format(mediaDatetoKey);
            where += (where == null || (where != null && where.isEmpty())) ? " where date_of_spot <= '" + dt + "' " : " and date_of_spot <= '" + dt + "' ";
        }
//        System.out.println("SELECT id,spot_ref_id,spot_type,day_of_spot,date_of_spot,spot_telephone_number, channel,program_name,program_type,show_time_start,show_time_end,actual_on_air_time,section_break,copy_length,tape,net_cost_per_spot,status,media_agency_remark,mtl_remark1,mtl_remark2,mtl_remark3,mtl_remark3,Product_Assign,ProductCode FROM media_plan " + where);
        return "SELECT id,spot_ref_id,spot_type,day_of_spot,date_of_spot,spot_telephone_number, channel,program_name,program_type,show_time_start,show_time_end,actual_on_air_time,section_break,copy_length,tape,net_cost_per_spot,status,media_agency_remark,mtl_remark1,mtl_remark2,mtl_remark3,mtl_remark3,Product_Assign,ProductCode FROM media_plan " + where;
    }

    protected boolean isInsertDataStyleOnXLSColumn(int columnId) {
        if (decorateXLSStringLine && decorateXLSStringLineColumnIdList != null) {
            for (int i : decorateXLSStringLineColumnIdList) {
                if (i == columnId) {
                    return true;
                }
            }
        }
        return false;
    }

    public String deleteAction() throws Exception {
        try {
            MediaPlan plan = new MediaPlan();
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    plan = mediaPlanDAO.findMediaPlan(item.getKey());
                    plan.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    plan.setUpdateDate(new Date());
                    plan.setEnable(false);
                    mediaPlanDAO.edit(plan);

                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    ///////////////////////////////// GET SET METHOD /////////////////////////////////////
    public static String getREFRESH() {
        return REFRESH;
    }

    public static void setREFRESH(String REFRESH) {
        MediaPlanController.REFRESH = REFRESH;
    }

    public static String getEDIT() {
        return EDIT;
    }

    public static void setEDIT(String EDIT) {
        MediaPlanController.EDIT = EDIT;
    }

    public static String getIMPORT() {
        return IMPORT;
    }

    public static void setIMPORT(String IMPORT) {
        MediaPlanController.IMPORT = IMPORT;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<MediaPlan> getMediaplans() {
        return mediaplans;
    }

    public void setMediaplans(List<MediaPlan> mediaplans) {
        this.mediaplans = mediaplans;
    }

    public MediaPlanDAO getMediaPlanDAO() {
        return mediaPlanDAO;
    }

    public void setMediaPlanDAO(MediaPlanDAO mediaPlanDAO) {
        this.mediaPlanDAO = mediaPlanDAO;
    }

    public String getSpotRefIDKey() {
        return spotRefIDKey;
    }

    public void setSpotRefIDKey(String spotRefIDKey) {
        this.spotRefIDKey = spotRefIDKey;
    }

    public String getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    public String getProgramNameKey() {
        return programNameKey;
    }

    public void setProgramNameKey(String programNameKey) {
        this.programNameKey = programNameKey;
    }

    public Date getMediaDatetoKey() {
        return mediaDatetoKey;
    }

    public void setMediaDatetoKey(Date mediaDatetoKey) {
        this.mediaDatetoKey = mediaDatetoKey;
    }

    public Date getMediaDatefromKey() {
        return mediaDatefromKey;
    }

    public void setMediaDatefromKey(Date mediaDatefromKey) {
        this.mediaDatefromKey = mediaDatefromKey;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
