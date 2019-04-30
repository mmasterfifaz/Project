package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.dao.ProductPlanDetailDAO;
import com.maxelyz.core.model.entity.CustomerField;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.FileTemplateMappingPK;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanCategory;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class ProductPlanImportController {

    private static Logger log = Logger.getLogger(ProductPlanImportController.class);
    private static String SUCCESS = "productplan.xhtml?faces-redirect=true";
    private static String FAILURE = "productplanimport.xhtml";
    private static String REDIRECT_PAGE = "product.jsf";
    private static File dataFile;
    private Integer step = 1;
    private String message = "";
    private List<FileTemplateMapping> fileTemplateMappings = new ArrayList<FileTemplateMapping>();
    private List<ProductPlan> productPlanTotal = new ArrayList<ProductPlan>();
    private List<ProductPlan> productPlanSuccess = new ArrayList<ProductPlan>();
    private List<ProductPlan> productPlanFail = new ArrayList<ProductPlan>();
    private List<ProductPlanDetail> productPlanDetailTotal = new ArrayList<ProductPlanDetail>();
    private List<ProductPlanDetail> productPlanDetailSuccess = new ArrayList<ProductPlanDetail>();
    private List<ProductPlanDetail> productPlanDetailFail = new ArrayList<ProductPlanDetail>();
    private Product product;
    private String productID = "";
    private Map<Integer, Integer> mapPlanNo = new LinkedHashMap<Integer, Integer>();
    Map<Integer, List<ProductPlanDetail>> mapProductPlanWithDetail = new LinkedHashMap<Integer, List<ProductPlanDetail>>();
    private List<ProductPlanDetail> productPlanDetailNewPlanCode = new ArrayList<ProductPlanDetail>();
    private List<ProductPlanDetail> productPlanDetailExistingPlanCode = new ArrayList<ProductPlanDetail>();
    private String warningPopup, warningMsg;
    private boolean canNextToStep2 = false, canProceed = false;

    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productPlanDetailDAO}")
    private ProductPlanDetailDAO productPlanDetailDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        productID = JSFUtil.getRequestParameterMap("productID");

        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            product = this.getProductDAO().findProduct(new Integer(productID));
        }
    }

    public void uploadCompleteListener(FileUploadEvent event) {
        canNextToStep2 = false;
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
            canNextToStep2 = true;
        } catch (Exception e) {
            log.error(e.getMessage());
            message = "Wrong format. Data could not insert.";
            canNextToStep2 = false;
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
        canNextToStep2 = false;
        message = "";
    }

    public String backAction() {
        return SUCCESS + "&productID=" + productID;
    }

    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:add");
    }

    public void nextStep1ToStep2(ActionEvent event) {
        message = "";
        step = 2;
        FileInputStream file = null;
        productPlanTotal.clear();
        productPlanSuccess.clear();
        productPlanFail.clear();
        productPlanDetailTotal.clear();
        productPlanDetailSuccess.clear();
        productPlanDetailFail.clear();
        warningPopup = "false";
        warningMsg = "";
        canNextToStep2 = false;
        canProceed = true;
        try {
            file = new FileInputStream(dataFile);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet1 = workbook.getSheetAt(0);
            HSSFRow r = sheet1.getRow(0);
            HSSFSheet sheet2 = workbook.getSheetAt(1);
            r = sheet2.getRow(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            this.generateProductPlanList(sheet1, evaluator);
            this.setPlanNoMap();
            this.generateProductPlanDetailList(sheet2, evaluator);
            this.determineProductPlanDetail();
            this.setPlanDetailToEachPlan();
            if (productPlanTotal == null || (productPlanTotal != null && productPlanTotal.size() == 0)) {
                if (productPlanDetailSuccess == null || productPlanDetailSuccess != null && productPlanDetailSuccess.size() == 0) {
                    canProceed = false;
                    message = " No plan information. Data could not import.";
                }
            }
            if ((productPlanSuccess == null || (productPlanSuccess != null && productPlanSuccess.size() == 0)) && (productPlanDetailSuccess == null || (productPlanDetailSuccess != null && productPlanDetailSuccess.size() == 0))) {
                canProceed = false;
                message = " No plan information. Data could not import.";
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            message = " Data could not insert";
            clearFileUpload();
            step = 1;
            canNextToStep2 = false;
        }
    }

    public void previousStep2ToStep1(ActionEvent event) {
        step = 1;
        canNextToStep2 = false;
        warningPopup = "false";
        warningMsg = "";
        message = "";
    }

    public String saveStep2Listener() throws Exception {
        String username = JSFUtil.getUserSession().getUsers().getLoginName();
        try {
            insertProductPlan();
        } catch (Exception e) {
            log.error(e);
            message = " Data could not insert";
            clearFileUpload();
            step = 2;
            return FAILURE;
        }
        return SUCCESS + "&productID=" + productID;
    }

    private void generateProductPlanList(HSSFSheet sheet, FormulaEvaluator evaluator) {
        HSSFRow row;
        HSSFCell cell;
        int lastedRow = sheet.getLastRowNum();
        boolean chk = false;
        for (int r = 1; r < lastedRow + 1; r++) {
            row = sheet.getRow(r);
            ProductPlan pp = new ProductPlan();
            pp.setProductPlanCategory(null);
            pp.setEnable(true);
            pp.setProduct(product);
            chk = false;
            if (row != null) {
                for (int i = 0; i < 26; i++) {
                    cell = row.getCell(i);
                    //plan_no
                    if (i == 0) {
                        try {
                            Double d = cell.getNumericCellValue();
                            if (d.toString() != null && !d.toString().isEmpty()) {
                                pp.setNo(d.intValue());
                            } else {
                                pp.setNo(null);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setNo(null);
                            pp.setPlanNo(s);
                            pp.setReason("Plan No could not insert because " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 1) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 100) {
                                    pp.setCode(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setCode(s);
                                    pp.setReason(pp.getReason() + " , " + "Plan no must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setCode(s);
                                pp.setReason(pp.getReason() + " , " + "Plan no is required");
                                chk = true;
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setCode(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 2) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 100) {
                                    pp.setName(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setName(s);
                                    pp.setReason(pp.getReason() + " , " + "Plan name must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setName(s);
                                pp.setReason(pp.getReason() + " , " + "Plan name is required");
                                chk = true;
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setName(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 3) {
                        try {
                            pp.setSumInsured(new Double(cell.getNumericCellValue()));
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setSumInsured(null);
                            pp.setSumInsuredTemp(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 4) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().equalsIgnoreCase("Monthly")) {
                                    pp.setPaymentMode("1");
                                } else if (cell.getStringCellValue().equalsIgnoreCase("Quarterly")) {
                                    pp.setPaymentMode("2");
                                } else if (cell.getStringCellValue().equalsIgnoreCase("Half Year")) {
                                    pp.setPaymentMode("3");
                                } else if (cell.getStringCellValue().equalsIgnoreCase("Yearly")) {
                                    pp.setPaymentMode("4");
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setPaymentMode(s);
                                    pp.setReason(pp.getReason() + " , " + "Payment Mode must be (Monthly,Quarterly,Half Year,Yearly) only");
                                    chk = true;
                                }
                            } else {
                                pp.setPaymentMode(null);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setPaymentMode(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 5) {
                        try {
                            Double d = cell.getNumericCellValue();
                            if (d.intValue() == 1) {
                                pp.setMasterPlan(Boolean.TRUE);
                            } else if (d.intValue() == 0) {
                                pp.setMasterPlan(Boolean.FALSE);
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setMasterPlan(null);
                                pp.setMasterPlanTemp(s);
                                pp.setReason(pp.getReason() + " , " + "Master Plan must be (1,0) only");
                                chk = true;
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setMasterPlan(null);
                            pp.setMasterPlanTemp(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                        // Insert code add flex field 1-20
                    } else if (i == 6) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx1(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx1(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx1(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx1(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 7) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx2(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx2(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx2(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx2(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 8) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx3(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx3(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx3(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx3(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 9) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx4(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx4(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx4(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx4(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 10) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx5(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx5(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx5(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx5(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 11) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx6(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx6(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx6(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx6(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 12) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx7(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx7(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx7(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx7(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 13) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx8(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx8(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx8(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx8(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 14) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx9(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx9(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx9(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx9(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 15) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx10(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx10(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx10(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx10(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 16) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx11(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx11(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx11(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx11(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 17) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx12(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx12(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx12(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx12(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 18) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx13(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx13(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx13(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx13(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 19) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx14(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx14(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx14(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx14(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 20) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx15(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx15(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx15(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx15(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 21) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx16(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx16(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx16(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx16(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 22) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx17(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx17(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx17(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx17(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 23) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx18(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx18(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx18(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx18(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 24) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx19(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx19(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx19(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx19(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 25) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pp.setFx20(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pp.setFx20(s);
                                    pp.setReason(pp.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pp.setFx20(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pp.setFx20(s);
                            pp.setReason(pp.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    }
                }
                pp.setStatus("success");
                if (chk) {
                    pp.setStatus("fail");
                }
                if (pp.getStatus().equalsIgnoreCase("success")) {
                    pp = checkDupPlan(pp);
                }
                productPlanTotal.add(pp);
            }
        }

    }

    private ProductPlan checkDupPlan(ProductPlan p) {
        if (p.getNo() != null) {
            //check if exist in DB
            List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(p.getNo(), product.getId());
            if (pL != null && pL.size() > 0) {
                p.setStatus("fail");
                p.setReason("Duplicate Product Plan No. Data could not insert.");
            }
        }
        return p;
    }

    private String getValueFromCell(HSSFCell cell, FormulaEvaluator evaluator) {
        String result = null;
        if (cell != null) {
            switch (evaluator.evaluateFormulaCell(cell)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    if (cell.getBooleanCellValue()) {
                        result = "1";
                    } else {
                        result = "0";
                    }
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    Double d = cell.getNumericCellValue();
                    result = d.toString();
                    break;
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    result = "";
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cell.getErrorCellValue());
                    result = "Error";
                    break;

                // CELL_TYPE_FORMULA will never occur
                case Cell.CELL_TYPE_FORMULA:
                    result = "Error";
                    break;
                case -1:
                    try {
                        result = cell.getStringCellValue();
                    } catch (Exception ex) {
                        try {
                            Double a = cell.getNumericCellValue();
                            result = a.toString();
                        } catch (Exception e) {
                            result = null;
                        }
                    }
                    break;
            }
        }
        return result;
    }

    private void generateProductPlanDetailList(HSSFSheet sheet, FormulaEvaluator evaluator) {
        HSSFRow row;
        HSSFCell cell;
        int lastedRow = sheet.getLastRowNum();
        boolean chk = false;
        for (int r = 1; r <= lastedRow; r++) {
            row = sheet.getRow(r);
            ProductPlanDetail pd = new ProductPlanDetail();
            chk = false;
            if (row != null) {
                for (int i = 0; i < 31; i++) {
                    cell = row.getCell(i);
                    if (i == 0) {
                        try {
                            Double d = cell.getNumericCellValue();
                            if (d.toString() != null && !d.toString().isEmpty()) {
                                pd.setPlanNo(d.intValue());
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setPlanNo(null);
                                pd.setPlanNoTemp(s);
                                pd.setReason(pd.getReason() + " , " + "Product Plan Code is required");
                                chk = true;
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setPlanNo(null);
                            pd.setPlanNoTemp(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 1) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setFromVal(f.intValue());
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFromVal(null);
                            pd.setFromValTemp(s);
                            pd.setReason(pd.getReason() + " , " + "From Value must be a number");
                            chk = true;
                        }
                    } else if (i == 2) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setToVal(f.intValue());
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setToVal(null);
                            pd.setToValTemp(s);
                            pd.setReason(pd.getReason() + " , " + "To Value must be a number");
                            chk = true;
                        }
                    } else if (i == 3) {
                        try {
                            pd.setGender(null);
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().equals("F") || cell.getStringCellValue().equals("M") || cell.getStringCellValue().equals("N/A")) {
                                    pd.setGender(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setGender(s);
                                    pd.setReason(pd.getReason() + " , " + "Gender must be (F,M,N/A) only");
                                    chk = true;
                                }
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setGender(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 4) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setPrice(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setPrice(null);
                            pd.setPriceTemp(s);
                            pd.setReason(pd.getReason() + " , " + "Price must be a number");
                            chk = true;
                        }
                    } else if (i == 5) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setStampDuty(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setStampDuty(null);
                            pd.setStampDutyTemp(s);
                            pd.setReason(pd.getReason() + " , " + "Stamp Duty must be a number");
                            chk = true;
                        }
                    } else if (i == 6) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setVat(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setVat(null);
                            pd.setVatTemp(s);
                            pd.setReason(pd.getReason() + " , " + "VAT must be a number");
                            chk = true;
                        }
                    } else if (i == 7) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setNetPremium(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setNetPremium(null);
                            pd.setNetPremiumTemp(s);
                            pd.setReason(pd.getReason() + " , " + "Net Premium must be a number");
                            chk = true;
                        }
                    } else if (i == 8) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setAnnualNetPremium(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setAnnualNetPremium(null);
                            pd.setAnnualNetPremiumTemp(s);
                            pd.setReason(pd.getReason() + " , " + "Annual Net Premium must be a number");
                            chk = true;
                        }
                    } else if (i == 9) {
                        try {
                            Double f = cell.getNumericCellValue();
                            pd.setAnnualPrice(f);
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setAnnualPrice(null);
                            pd.setAnnualPriceTemp(s);
                            pd.setReason(pd.getReason() + " , " + "Annual Price must be a number");
                            chk = true;
                        }
                        // Insert code add flex field 1-20 and plan code
                    } else if (i == 10) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx1(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx1(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx1(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx1(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 11) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx2(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx2(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx2(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx2(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 12) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx3(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx3(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx3(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx3(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 13) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx4(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx4(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx4(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx4(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 14) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx5(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx5(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx5(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx5(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 15) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx6(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx6(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx6(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx6(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 16) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx7(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx7(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx7(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx7(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 17) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx8(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx8(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx8(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx8(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 18) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx9(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx9(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx9(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx9(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 19) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx10(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx10(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx10(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx10(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 20) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx11(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx11(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx11(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx11(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 21) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx12(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx12(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx12(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx12(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 22) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx13(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx13(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx13(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx13(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 23) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx14(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx14(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx14(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx14(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 24) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx15(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx15(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx15(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx15(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 25) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx16(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx16(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx16(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx16(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 26) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx17(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx17(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx17(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx17(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 27) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx18(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx18(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx18(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx18(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 28) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx19(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx19(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx19(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx19(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 29) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 1000) {
                                    pd.setFx20(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setFx20(s);
                                    pd.setReason(pd.getReason() + " , " + "Flexfield must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setFx20(s);
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setFx20(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    } else if (i == 30) {
                        try {
                            if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                if (cell.getStringCellValue().length() < 100) {
                                    pd.setCode(cell.getStringCellValue());
                                } else {
                                    String s = this.getValueFromCell(cell, evaluator);
                                    pd.setCode(s);
                                    pd.setReason(pd.getReason() + " , " + "Plan no must be less than 100 characters");
                                    chk = true;
                                }
                            } else {
                                String s = this.getValueFromCell(cell, evaluator);
                                pd.setCode(s);
                                pd.setReason(pd.getReason() + " , " + "Plan no is required");
                                chk = true;
                            }
                        } catch (Exception e) {
                            String s = this.getValueFromCell(cell, evaluator);
                            pd.setCode(s);
                            pd.setReason(pd.getReason() + " , " + e.getMessage());
                            chk = true;
                        }
                    }
                }
                pd.setStatus("success");
                if (chk) {
                    pd.setStatus("fail");
                }
                pd.setDoInsert(false);
                pd = checkWithPlan(pd);
                pd = checkDupPlanDetail(pd);
                productPlanDetailTotal.add(pd);
            }
        }
    }

    private ProductPlanDetail checkDupPlanDetail(ProductPlanDetail pd) {
        //check if exist in DB
        if (pd.getStatus().equalsIgnoreCase("success")) {
            List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
            if (pL != null && pL.size() > 0) {
                if (warningPopup != null && warningPopup.equalsIgnoreCase("false")) {
                    warningPopup = "true";
                    warningMsg = warningMsg + " Data already exist. New data will be inserted.";
                }
                pd.setDoInsert(true);
            }
        }
        return pd;
    }

    private ProductPlanDetail checkWithPlan(ProductPlanDetail pd) {
        boolean chk = false;
        if (productPlanSuccess != null && productPlanSuccess.size() > 0) {
            for (ProductPlan p : productPlanSuccess) {
                if (p.getNo() == pd.getPlanNo()) {
                    return pd;
                }
            }
            if (pd.getPlanNo() != null) {
                //check if exist in DB
                List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
                if (pL != null && pL.size() > 0) {
                    return pd;
                } else {
                    pd.setStatus("fail");
                    pd.setReason("No plan information. Data could not insert");
                    return pd;
                }
            } else {
                pd.setStatus("fail");
                pd.setReason("No plan information. Data could not insert");
                return pd;
            }
        }
        if (productPlanFail != null && productPlanFail.size() > 0) {
            for (ProductPlan p : productPlanFail) {
                if ((p.getNo() == pd.getPlanNo()) && (pd.getPlanNo() != null)) {
                    //check if exist in DB
                    List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
                    if (pL != null && pL.size() > 0) {
                        return pd;
                    } else {
                        pd.setStatus("fail");
                        pd.setReason("No plan information. Data could not insert");
                        return pd;
                    }
                }
            }
            if (productPlanSuccess != null && productPlanSuccess.size() > 0) {
                for (ProductPlan pp : productPlanSuccess) {
                    if (pp.getNo() != null && pd.getPlanNo() != null && pp.getNo() == pd.getPlanNo()) {
                        return pd;
                    } else {
                        pd.setStatus("fail");
                        pd.setReason("No plan information. Data could not insert");
                        return pd;
                    }
                }
            } else {
                if (pd.getPlanNo() != null) {
                    //check if exist in DB
                    List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
                    if (pL != null && pL.size() > 0) {
                        return pd;
                    } else {
                        pd.setStatus("fail");
                        pd.setReason("No plan information. Data could not insert");
                        return pd;
                    }
                } else {
                    return pd;
                }

            }

        } else {
            if (pd.getPlanNo() != null) {
                //check if exist in DB
                List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
                if (pL != null && pL.size() > 0) {
                    return pd;
                } else {
                    pd.setStatus("fail");
                    pd.setReason("No plan information. Data could not insert");
                    return pd;
                }
            }
        }
        return pd;
    }

    private void setPlanNoMap() {
        if (productPlanTotal != null && productPlanTotal.size() > 0) {
            for (ProductPlan p : productPlanTotal) {
                mapPlanNo.put(p.getNo(), p.getNo());
                if (p.getStatus().equalsIgnoreCase("success")) {
                    productPlanSuccess.add(p);
                } else {
                    productPlanFail.add(p);
                }
            }
        }
    }

    private void determineProductPlanDetail() {
        if (productPlanDetailTotal != null && productPlanDetailTotal.size() > 0) {
            for (ProductPlanDetail pd : productPlanDetailTotal) {
                if (pd.getStatus() != null && pd.getStatus().equalsIgnoreCase("success")) {
                    productPlanDetailSuccess.add(pd);
                } else {
                    productPlanDetailFail.add(pd);
                }
            }
        }
    }

    private void setPlanDetailToEachPlan() {
        if (productPlanDetailSuccess != null && productPlanDetailSuccess.size() > 0) {
            for (ProductPlanDetail pd : productPlanDetailSuccess) {
                if (mapPlanNo.containsKey(pd.getPlanNo())) {
                    if (mapProductPlanWithDetail.containsKey(pd.getPlanNo())) {
                        List<ProductPlanDetail> pdList = mapProductPlanWithDetail.get(pd.getPlanNo());
                        pdList.add(pd);
                        mapProductPlanWithDetail.put(pd.getPlanNo(), pdList);
                    } else {
                        List<ProductPlanDetail> pdList = new ArrayList<ProductPlanDetail>();
                        pdList.add(pd);
                        mapProductPlanWithDetail.put(pd.getPlanNo(), pdList);
                    }
                } else {
                    // Case : plan no not existing in prodhuct plan
                    List<ProductPlan> tp = this.getProductPlanDAO().findProductPlanByNo(pd.getPlanNo());
                    if (tp != null && tp.size() == 1) {
                        pd.setProductPlan(tp.get(0));
                        productPlanDetailExistingPlanCode.add(pd);
                    } else {
                        productPlanDetailNewPlanCode.add(pd);
                    }
                }
            }
        }
    }

    private void insertProductPlan() {
        try {
            if (productPlanSuccess != null && productPlanSuccess.size() > 0) {
                for (ProductPlan p : productPlanSuccess) {
                    int in = 1;
                    this.getProductPlanDAO().create(p);
                    if (mapProductPlanWithDetail != null && mapProductPlanWithDetail.containsKey(p.getNo())) {
                        for (ProductPlanDetail pd : mapProductPlanWithDetail.get(p.getNo())) {
                            pd.setProductPlan(p);
                            if (pd.getDoInsert()) {
                                ProductPlanDetail pTemp = this.getProductPlanDetailDAO().findLastProductPlanDetailByProductAndPlanNo(pd.getPlanNo(), product.getId());
                                if (pTemp != null) {
                                    Integer maxSeq = pTemp.getSeqNo();
                                    pd.setSeqNo(++maxSeq);
                                }
                            } else {
                                pd.setSeqNo(in);
                                in++;
                            }
                            this.getProductPlanDetailDAO().create(pd);
                        }
                    }
                }
            } else {
                if (productPlanDetailSuccess != null && productPlanDetailSuccess.size() > 0) {
                    for (ProductPlanDetail pd : productPlanDetailSuccess) {
                        if (pd.getDoInsert()) {
                            List<ProductPlan> pL = this.getProductPlanDAO().findProductPlanByNoAndProductId(pd.getPlanNo(), product.getId());
                            pd.setProductPlan(pL.get(0));
                            ProductPlanDetail pTemp = this.getProductPlanDetailDAO().findLastProductPlanDetailByProductAndPlanNo(pd.getPlanNo(), product.getId());
                            if (pTemp != null) {
                                Integer maxSeq = pTemp.getSeqNo();
                                pd.setSeqNo(++maxSeq);
                                this.getProductPlanDetailDAO().create(pd);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProductPlanImportController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private void displayProductPlanList(List<ProductPlan> pList) {
        if (pList != null && pList.size() > 0) {
            for (ProductPlan p : pList) {
                System.out.println("No : " + p.getNo() + " , Code : " + p.getCode() + " , Sum Insure : " + p.getSumInsured() + " , Payment Mode : " + p.getPaymentMode() + " , Master Plan : " + p.getMasterPlan() + " , status : " + p.getStatus() + " , reason : " + p.getReason());
            }
            System.out.println("-----------------------------------------------------------------------------------");
        }
    }

    private void displayProductPlanDetailList(List<ProductPlanDetail> pdList) {
        if (pdList != null && pdList.size() > 0) {
            for (ProductPlanDetail pd : pdList) {
                System.out.println("Plan No : " + pd.getPlanNo() + " , From Val : " + pd.getFromVal() + " , To Val : " + pd.getToVal() + " , Gender : " + pd.getGender() + " , Price : " + pd.getPrice() + " , Stamp Duty : " + pd.getStampDuty() + " , VAT : " + pd.getVat() + " , Net Premium : " + pd.getNetPremium() + " , Annual Net Premium : " + pd.getAnnualNetPremium() + " , Annual Price : " + pd.getAnnualPrice() + " , Status : " + pd.getStatus() + " , Reason : " + pd.getReason());
            }
            System.out.println("-----------------------------------------------------------------------------------");
        }
    }

    private void displayMapList() {
        if (mapProductPlanWithDetail != null && mapProductPlanWithDetail.size() > 0) {
            for (Map.Entry<Integer, List<ProductPlanDetail>> item : mapProductPlanWithDetail.entrySet()) {
                System.out.println("-------------------------- Plan No : " + item.getKey() + " --------------------------");
                if (item.getValue() != null && item.getValue().size() > 0) {
                    for (ProductPlanDetail pd : item.getValue()) {
                        System.out.println("Plan No : " + pd.getPlanNo() + " , From Val : " + pd.getFromVal() + " , To Val : " + pd.getToVal() + " , Gender : " + pd.getGender() + " , Price : " + pd.getPrice() + " , Stamp Duty : " + pd.getStampDuty() + " , VAT : " + pd.getVat() + " , Net Premium : " + pd.getNetPremium() + " , Annual Net Premium : " + pd.getAnnualNetPremium() + " , Annual Price : " + pd.getAnnualPrice() + " , Status : " + pd.getStatus() + " , Reason : " + pd.getReason());
                    }
                }
            }
            System.out.println("-----------------------------------------------------------------------------------");
        }
    }

    ////////////////////////////////////////////////// GET SET METHOD /////////////////////////////////////////////////
    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static void setSUCCESS(String SUCCESS) {
        ProductPlanImportController.SUCCESS = SUCCESS;
    }

    public static String getFAILURE() {
        return FAILURE;
    }

    public static void setFAILURE(String FAILURE) {
        ProductPlanImportController.FAILURE = FAILURE;
    }

    public static File getDataFile() {
        return dataFile;
    }

    public static void setDataFile(File dataFile) {
        ProductPlanImportController.dataFile = dataFile;
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

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public ProductPlanDetailDAO getProductPlanDetailDAO() {
        return productPlanDetailDAO;
    }

    public void setProductPlanDetailDAO(ProductPlanDetailDAO productPlanDetailDAO) {
        this.productPlanDetailDAO = productPlanDetailDAO;
    }

    public List<ProductPlan> getProductPlanTotal() {
        return productPlanTotal;
    }

    public void setProductPlanTotal(List<ProductPlan> productPlanTotal) {
        this.productPlanTotal = productPlanTotal;
    }

    public List<ProductPlan> getProductPlanSuccess() {
        return productPlanSuccess;
    }

    public void setProductPlanSuccess(List<ProductPlan> productPlanSuccess) {
        this.productPlanSuccess = productPlanSuccess;
    }

    public List<ProductPlan> getProductPlanFail() {
        return productPlanFail;
    }

    public void setProductPlanFail(List<ProductPlan> productPlanFail) {
        this.productPlanFail = productPlanFail;
    }

    public List<ProductPlanDetail> getProductPlanDetailTotal() {
        return productPlanDetailTotal;
    }

    public void setProductPlanDetailTotal(List<ProductPlanDetail> productPlanDetailTotal) {
        this.productPlanDetailTotal = productPlanDetailTotal;
    }

    public List<ProductPlanDetail> getProductPlanDetailSuccess() {
        return productPlanDetailSuccess;
    }

    public void setProductPlanDetailSuccess(List<ProductPlanDetail> productPlanDetailSuccess) {
        this.productPlanDetailSuccess = productPlanDetailSuccess;
    }

    public List<ProductPlanDetail> getProductPlanDetailFail() {
        return productPlanDetailFail;
    }

    public void setProductPlanDetailFail(List<ProductPlanDetail> productPlanDetailFail) {
        this.productPlanDetailFail = productPlanDetailFail;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean getCanNextToStep2() {
        return canNextToStep2;
    }

    public void setCanNextToStep2(boolean canNextToStep2) {
        this.canNextToStep2 = canNextToStep2;
    }

    public boolean getCanProceed() {
        return canProceed;
    }

    public void setCanProceed(boolean canProceed) {
        this.canProceed = canProceed;
    }

    public String getWarningPopup() {
        return warningPopup;
    }

    public void setWarningPopup(String warningPopup) {
        this.warningPopup = warningPopup;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
    }

}
