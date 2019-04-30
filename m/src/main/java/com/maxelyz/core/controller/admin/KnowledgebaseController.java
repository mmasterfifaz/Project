package com.maxelyz.core.controller.admin;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.*;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.BusinessUnitValue;
import com.maxelyz.core.model.value.admin.LocationValue;
import com.maxelyz.core.model.value.admin.ServiceTypeValue;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.richfaces.event.DropEvent;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeToggleEvent;

@ManagedBean
//@RequestScoped
@ViewScoped
public class KnowledgebaseController implements Serializable {

    private static Logger log = Logger.getLogger(KnowledgebaseController.class);
    private static String REFRESH = "knowledgebase.xhtml";
    private static String REFRESHIMPORT = "knowledgebaseimport.xhtml";
    private static String EDIT = "knowledgebaseedit.xhtml";
    private Map<Long, Boolean> selectedIds = new ConcurrentHashMap<Long, Boolean>();
    private List<Knowledgebase> knowledgebases;
    //anuwat 
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "knowledgebase");
    private FileUploadBean attUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "atttemp");
    private List<Knowledgebase> relateknowledgebases;
    private List<KnowledgebaseBoard> knowledgebasesboard;
    private String comment;
    private Long vote;
    private int displayvote;
    private boolean disablevote;
    private List<ServiceTypeValue> serviceTypeValueList;
    private Map<String, Integer> mapRelate;
    private int BUFFER = 1024;
    private String[] fileUnziplist;
    //anuwat
    private Knowledgebase knowledgebase;
    private String keyword;
    private String keyappv;
    private String message;
    private boolean appvimport;
    private int contentown;
    private List<SwingTreeNodeImpl> root= null;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
//    @ManagedProperty(value = "#{usersDAO}")
//    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        message = "";
        appvimport = false;
        contentown = JSFUtil.getUserSession().getUsers().getId();
//        knowledgebases = this.getKnowledgebaseDAO().findTop();
        knowledgebases = this.getKnowledgebaseDAO().findKnowledgebaseAdminByUserGroupId(JSFUtil.getUserSession().getUserGroup().getId());

    }
    
    public void toggleListener(TreeToggleEvent event) {
        UITree tree = (UITree) event.getComponent();
        try {
            SwingTreeNodeImpl modelNode = (SwingTreeNodeImpl)tree.getRowData();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        /*for (int i = 0; i < 5; i++) { 
           // this.findAndChildNode(modelNode, null);
             System.out.println(i);
         }*/
    }
    
    public FileUploadBean getAttUploadBean() {
        return attUploadBean;
    }

    public void setAttUploadBean(FileUploadBean attUploadBean) {
        this.attUploadBean = attUploadBean;
    }

    public int getContentown() {
        return contentown;
    }

    public void setContentown(int contentown) {
        this.contentown = contentown;
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:kb:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:kb:delete");
    }

    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:kb:edit");
    }

    public List<Knowledgebase> getList() {
        return getKnowledgebases();
    }

    public void searchListener(ActionEvent event) {
        searchAction();
    }

    public boolean isDisablevote() {
        return disablevote;
    }

    public void setDisablevote(boolean disablevote) {
        this.disablevote = disablevote;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

//    public UsersDAO getUsersDAO() {
//        return usersDAO;
//    }
//
//    public void setUsersDAO(UsersDAO usersDAO) {
//        this.usersDAO = usersDAO;
//    }

//    public List<Users> getUsersList() {
//        return usersList;
//    }
//
//    public void setUsersList(List<Users> usersList) {
//        this.usersList = usersList;
//    }

    /**
     * Use in Front Knowledge Popup
     *
     * @param event
     */
    public void selectIdListener(ActionEvent event) {
        try {
            String id = (String) JSFUtil.getRequestParameterMap("id");
            this.getKnowledgebaseDAO().updateViewKnowledgebaseById(new Integer(id));
            knowledgebase = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(id));
            if (knowledgebase != null) {
                relateknowledgebases = new ArrayList<Knowledgebase>();
                List<Knowledgebase> nlist = this.getKnowledgebaseDAO().findKnowledgebaseRelateId(knowledgebase.getId(), knowledgebase.getVersion());
                for (Knowledgebase n : nlist) {
                    relateknowledgebases.add(n);
                }
                knowledgebasesboard = new ArrayList<KnowledgebaseBoard>();
                List<KnowledgebaseBoard> blist = this.getKnowledgebaseDAO().findKnowledgebaseBoardById(knowledgebase.getId(), knowledgebase.getVersion());
                int i = 0;
                for (KnowledgebaseBoard b : blist) {
                    b.setNo(blist.size() - i);
                    knowledgebasesboard.add(b);
                    i++;
                }
                loadVate();

            }
            //Save kb to session
            UserSession userSession = JSFUtil.getUserSession();
            userSession.setKnowledgebase(knowledgebase);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void dropListener(DropEvent dropEvent) {
        message = "";
//        System.out.println("Drop Listener");
        if (!SecurityUtil.isPermitted("admin:kb:edit")) {
            return;
        }
//        SwingTreeNodeImpl
        Integer sourceid = (Integer) dropEvent.getDragValue();
        Integer targetid = (Integer) dropEvent.getDropValue();
//        if (sourceid.equals(targetid)) {
//            return;
//        }
        if (this.getKnowledgebaseDAO().isCircular(sourceid,targetid)) {
            message = "Cannot drop here.";
            return;
        }
        try {
            //put source (child) to target (parent)
            Knowledgebase kb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(sourceid));
            Knowledgebase refkb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(targetid));
            kb.setKnowledgebase(refkb);
            this.getKnowledgebaseDAO().edit(kb);
            //update knowledge member
//            this.searchAction();
            knowledgebases = this.getKnowledgebaseDAO().findTop();
            initTree();
            //render Tree
//            FacesContext ac = FacesContext.getCurrentInstance();
//            UITreeNode destNode = (dropEvent.getSource() instanceof UITreeNode) ? (UITreeNode) dropEvent.getSource() : null;
//            UITree destTree = destNode != null ? destNode.getUITree() : null;
//            ac.addComponentToAjaxRender(destTree);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void markAsRootListener(ActionEvent event) {
        String selectedID = (String) JSFUtil.getRequestParameterMap("selectedId");
        Knowledgebase kb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(selectedID));
        kb.setKnowledgebase(null);
        try {
            this.getKnowledgebaseDAO().edit(kb);
        } catch (Exception e) {
            log.error(e);
        }
        searchAction();
    }

    public String searchAction() {
//        if(keyword == null || keyappv == null) {
////        if (keyword.trim().length() == 0 && keyappv.trim().length() == 0) {
//            initialize();
//        } else {
//            System.out.println("Search Action");
            //knowledgebases = this.getKnowledgebaseDAO().findKnowledgebaseByName(keyword);
            knowledgebases = this.getKnowledgebaseDAO().findKnowledgebaseAdminByName(JSFUtil.getUserSession().getUserGroup().getId(), keyword, keyappv);
//        }
        initTree();
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        KnowledgebaseDAO dao = getKnowledgebaseDAO();
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedId");
            dao.disableKnowledgebaseEntity(new Integer(selectedID));
        } catch (Exception e) {
            log.error(e);
        }
        searchAction();
        return REFRESH;
    }

//    private void findAndChildNode(TreeNode parentNode, Knowledgebase parentKb) {
////        System.out.println(parentKb.getKnowledgebaseCollection());
//        for (Knowledgebase childKb : parentKb.getKnowledgebaseCollection()) {
////            System.out.println(childKb.getTopic()+" - "+childKb.getKnowledgebase());
//            if (childKb.getEnable()) {
//                if(!childKb.getKnowledgebaseCollection().isEmpty()) {
//                    MXTreeNode childNode = new MXTreeNode(childKb.getId(),childKb.getTopic(),"NODE");
//                    parentNode.addChild(childKb, childNode);
//                    this.findAndChildNode(childNode, childKb);//Recursive
//                } else {
//                    MXTreeNode childNode = new MXTreeNode(childKb.getId(),childKb.getTopic(),"LEAF");
//                    parentNode.addChild(childKb, childNode);
//                }
//            }
//        }
//    }
//
//    private void addNodes(String path, TreeNode node, List<Knowledgebase> kb) {
//        //Level1
//        for (Knowledgebase kb1 : kb) {
//            if(!kb1.getKnowledgebaseCollection().isEmpty()) {
//                MXTreeNode node1 = new MXTreeNode(kb1.getId(),kb1.getTopic(),"NODE");
//                node.addChild(kb1, node1);
//                //Level2 to n 
//                findAndChildNode(node1, kb1);
//            } else {
//                MXTreeNode node1 = new MXTreeNode(kb1.getId(),kb1.getTopic(),"LEAF");
//                node.addChild(kb1, node1);
//            }
//        }
//    }
//
//    private void initTree() {
//        root = new TreeNodeImpl();
//        addNodes(null, root, knowledgebases);
//    }
    
//    public TreeNode getRoot() {
//        if (root == null) {
//            initTree();
//        }
//        return root;
//    }
    private void findAndChildNode(SwingTreeNodeImpl parentNode, Knowledgebase parentKb) {//,int traverseNo) {
        SwingTreeNodeImpl childNode;
        for (Knowledgebase childKb : parentKb.getKnowledgebaseCollection()) {
            //call query check condition childKb
            if (childKb.getEnable()) {
                childNode = new SwingTreeNodeImpl(childKb.getId(), childKb.getTopic());
               // if (traverseNo==1) {    
                    if(!childKb.getKnowledgebaseCollection().isEmpty()) {
                        this.findAndChildNode(childNode, childKb);//, --traverseNo);//Recursive
                    }
                //}
                parentNode.addChild(childNode);
            }
        }
    }
            
    private void addNodes(String path, List<SwingTreeNodeImpl> root, List<Knowledgebase> kb) {
        SwingTreeNodeImpl node;
        for (Knowledgebase kb1 : kb) {
            node = new SwingTreeNodeImpl(kb1.getId(), kb1.getTopic());
            if(!kb1.getKnowledgebaseCollection().isEmpty()) {
                findAndChildNode(node, kb1);//,1);
            } 
            root.add(node);
        }
    }
    
    public void initTree() {
        root = new ArrayList<SwingTreeNodeImpl>(); 
        addNodes(null, root, knowledgebases);
    }
    
    public void setRoot(List<SwingTreeNodeImpl> root) {
        this.root = root; 
    }
    
    public List<SwingTreeNodeImpl> getRoot() { 
        if (root == null){
            initTree(); 
        }
        return root; 
    }
    
    public Map<Long, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Long, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Knowledgebase> getKnowledgebases() {
        return knowledgebases;
    }

    public void setKnowledgebases(List<Knowledgebase> knowledgebases) {
        this.knowledgebases = knowledgebases;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyappv() {
        return keyappv;
    }

    public void setKeyappv(String keyappv) {
        this.keyappv = keyappv;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public List<ServiceTypeValue> getServiceTypeValueList() {
        return serviceTypeValueList;
    }

    public void setServiceTypeValueList(List<ServiceTypeValue> serviceTypeValueList) {
        this.serviceTypeValueList = serviceTypeValueList;
    }

    public List<Knowledgebase> getRelateknowledgebases() {
        return relateknowledgebases;
    }

    public void setRelateknowledgebases(List<Knowledgebase> relateknowledgebases) {
        this.relateknowledgebases = relateknowledgebases;
    }

    public List<KnowledgebaseBoard> getKnowledgebasesboard() {
        return knowledgebasesboard;
    }

    public void setKnowledgebasesboard(List<KnowledgebaseBoard> knowledgebasesboard) {
        this.knowledgebasesboard = knowledgebasesboard;
    }

    //anuwat
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }

    public int getDisplayvote() {
        return displayvote;
    }

    public void setDisplayvote(int displayvote) {
        this.displayvote = displayvote;
    }

    public boolean isAppvimport() {
        return appvimport;
    }

    public void setAppvimport(boolean appvimport) {
        this.appvimport = appvimport;
    }

    public void saveComment(ActionEvent event) {
        if (knowledgebase != null && comment != null && !comment.equalsIgnoreCase("")) {
            KnowledgebaseBoard knowledgebaseBoard = new KnowledgebaseBoard();

            knowledgebaseBoard.setKnowledgebaseId(knowledgebase.getId());
            knowledgebaseBoard.setKnowledgebaseVersion(knowledgebase.getVersion());
            knowledgebaseBoard.setKbboardComment(comment.trim());
            knowledgebaseBoard.setCreateBy(JSFUtil.getUserSession().getUserName());
            knowledgebaseBoard.setCreateDate(new Date());
            this.getKnowledgebaseDAO().createComment(knowledgebaseBoard);

            relateknowledgebases = new ArrayList<Knowledgebase>();
            List<Knowledgebase> nlist = this.getKnowledgebaseDAO().findKnowledgebaseRelateId(knowledgebase.getId(), knowledgebase.getVersion());
            for (Knowledgebase n : nlist) {
                relateknowledgebases.add(n);
            }
            knowledgebasesboard = new ArrayList<KnowledgebaseBoard>();
            List<KnowledgebaseBoard> blist = this.getKnowledgebaseDAO().findKnowledgebaseBoardById(knowledgebase.getId(), knowledgebase.getVersion());
            int i = 0;
            for (KnowledgebaseBoard b : blist) {
                b.setNo(blist.size() - i);
                knowledgebasesboard.add(b);
                i++;
            }

            comment = new String();
        }
    }

    public void saveVate(ActionEvent event) throws Exception {
        if (knowledgebase != null && vote != 0) {
            KnowledgebaseVote knowledgebaseVote = new KnowledgebaseVote();
            knowledgebaseVote.setKnowledgebaseVotePK(new KnowledgebaseVotePK(knowledgebase.getId(), knowledgebase.getVersion(), JSFUtil.getUserSession().getUsers().getId()));
            knowledgebaseVote.setVote(vote);
            knowledgebaseVote.setCreateBy(JSFUtil.getUserSession().getUserName());
            knowledgebaseVote.setCreateDate(new Date());
            this.getKnowledgebaseDAO().createVote(knowledgebaseVote);

        }
        loadVate();

    }

    public void loadVate() throws Exception {
        vote = new Long(0);
        displayvote = new Integer(0);
        displayvote = this.getKnowledgebaseDAO().getKnowledgebaseVoteAverage(knowledgebase.getId(), knowledgebase.getVersion());

        List<KnowledgebaseVote> uservotelist = this.getKnowledgebaseDAO().findKnowledgebaseBoardVoteUserId(knowledgebase.getId(), knowledgebase.getVersion(), JSFUtil.getUserSession().getUsers().getId());
        if (uservotelist != null && uservotelist.size() > 0) {
            disablevote = true;
        } else {
            disablevote = false;
        }
    }
    //////////////anuwat

    public String importAction() {
        try {
            if (fileUploadBean != null) {
                fillServiceType();
                List<File> fileList = fileUploadBean.getFiles();
                for (File f : fileList) {
                    mapRelate = new LinkedHashMap<String, Integer>();
                    FileInputStream fileInputStream = new FileInputStream(f);
                    if (f.getName().contains(".xlsx")) {
                        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                        XSSFSheet xssfSheet = workBook.getSheetAt(0);
                        Iterator rowIterator = xssfSheet.rowIterator();
                        XSSFRow xssfRow;

                        try{
                            while (rowIterator.hasNext()) {
                                xssfRow = (XSSFRow) rowIterator.next();
                                XSSFCell codeCell = xssfRow.getCell(0);
                                XSSFCell topicCell = xssfRow.getCell(1);
                                XSSFCell DescriptionCell = xssfRow.getCell(2);
                                XSSFCell scheduleCell = xssfRow.getCell(3);
                                XSSFCell startdateCell = xssfRow.getCell(4);
                                XSSFCell enddateCell = xssfRow.getCell(5);
                                XSSFCell faqCell = xssfRow.getCell(6);
                                XSSFCell attFileCell = xssfRow.getCell(7);
                                XSSFCell urlCell = xssfRow.getCell(8);

                                if (xssfRow.getRowNum() > 0) {
                                    if (topicCell != null && !topicCell.getStringCellValue().trim().isEmpty()) {

                                        Knowledgebase kb = new Knowledgebase();
                                        KnowledgebaseDivision kbv = new KnowledgebaseDivision();

                                        kb.setTopic(topicCell.getStringCellValue());
                                        kb.setDescription(DescriptionCell.getStringCellValue().replaceAll("\n", "<br/>"));
                                        kb.setSchedule(scheduleCell.getBooleanCellValue());
                                        if(startdateCell!=null&&enddateCell!=null){
                                        kb.setStartdate(startdateCell.getDateCellValue());
                                        kb.setEnddate(enddateCell.getDateCellValue());
                                        }
                                        kb.setVersion("1.0");
                                        kb.setEnable(true);
                                        kb.setCreateBy(JSFUtil.getUserSession().getUserName());
                                        kb.setCreateDate(new Date());
                                        if(appvimport){
                                        kb.setApproval(appvimport);
                                        kb.setApprovalBy(JSFUtil.getUserSession().getUserName());
                                        kb.setApprovalDate(new Date());
                                        }
                                        kb.setFaq(faqCell.getBooleanCellValue());
                                        kb.setContentown(contentown);
                                        knowledgebaseDAO.create(kb);

                                        mapRelate.put(codeCell.getStringCellValue(), kb.getId());

                                        kbv.setKnowledgebaseDivisionPK(new KnowledgebaseDivisionPK(kb.getId(), kb.getVersion().trim()));
                                        kbv.setTopic(kb.getTopic());
                                        kbv.setDescription(kb.getDescription());
                                        kbv.setEnable(kb.getEnable());
                                        kbv.setSchedule(kb.getSchedule());
                                        if(startdateCell!=null&&enddateCell!=null){
                                        kbv.setStartdate(kb.getStartdate());
                                        kbv.setEnddate(kb.getEnddate());
                                        }
                                        kbv.setViewcount(kb.getViewcount());
                                        kbv.setCreateBy(JSFUtil.getUserSession().getUserName());
                                        kbv.setCreateDate(new Date());
                                        kbv.setUpdateBy(JSFUtil.getUserSession().getUserName());
                                        kbv.setUpdateDate(new Date());
                                        kbv.setContentown(contentown);
                                        if(appvimport){
                                        kbv.setApproval(appvimport);
                                        kbv.setApprovalBy(JSFUtil.getUserSession().getUserName());
                                        kbv.setApprovalDate(new Date());
                                        }
                                        knowledgebaseDAO.createDivision(kbv);

                                        ///////////attFile//////////////
                                        if (attFileCell != null) {
                                            String[] attFileList = (String[]) attFileCell.getStringCellValue().split(",");
                                            for (String attFile : attFileList) {
                                                KnowledgebaseAttfile kbaf = new KnowledgebaseAttfile();
                                                kbaf.setKnowledgebaseId(kb.getId());
                                                kbaf.setKbattfileFilename(attFile);
                                                kbaf.setCreateBy(JSFUtil.getUserSession().getUserName());
                                                kbaf.setCreateDate(new Date());
                                                knowledgebaseDAO.createAttFile(kbaf);
                                            }
                                        }
                                        ///////////url///////////////
                                        if (urlCell != null) {
                                            String[] urlList = (String[]) urlCell.getStringCellValue().split(";");
                                            for (String urlSum : urlList) {
                                                KnowledgebaseUrl kburl = new KnowledgebaseUrl();
                                                String[] url = (String[]) urlSum.split(",");

                                                kburl.setKnowledgebaseId(kb.getId());
                                                kburl.setKnowledgebaseVersion(kb.getVersion());
                                                kburl.setKburlText(url[0]);
                                                kburl.setKburlLink(url[1]);
                                                knowledgebaseDAO.createUrl(kburl);
                                            }
                                        }
                                        ////////// /////////////

                                        for (ServiceTypeValue s : serviceTypeValueList) {
                                            for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                                                for (LocationValue l : b.getLocationValueList()) {
                                                    if (l.isSelected()) {
                                                        KnowledgebaseLocation knowledgebaseLocation = new KnowledgebaseLocation(kb.getId(), s.getServiceType().getId(), b.getBusinessUnit().getId(), l.getLocation().getId());
                                                        knowledgebaseDAO.createLocation(knowledgebaseLocation);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }catch(Exception e){
                            log.error(e);
                        }
//////////////////////////////
                        rowIterator = xssfSheet.rowIterator();
                        while (rowIterator.hasNext()) {
                            xssfRow = (XSSFRow) rowIterator.next();
                            XSSFCell codeCell = xssfRow.getCell(0);
                            XSSFCell relateCell = xssfRow.getCell(9);
                            if (xssfRow.getRowNum() > 0) {
                            if (!relateCell.getStringCellValue().isEmpty()) {
                                int kbid  =mapRelate.get(codeCell.getStringCellValue()); 
                                String[] relateList = relateCell.getStringCellValue().split(",");
                                for (String relate : relateList) {
                                    KnowledgebaseRelated kbrl = new KnowledgebaseRelated();
                                    int kbrelid=mapRelate.get(relate);
                                    kbrl.setKnowledgebaseRelatedPK(new KnowledgebaseRelatedPK(kbid, "1.0", kbrelid));
                                    knowledgebaseDAO.createRelated(kbrl);
                                }
                            }
                            }

                        }
                        //System.out.println(workBook.getActiveSheetIndex());
                    } else if (f.getName().contains(".xls")) {

                        POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
                        HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);

                        HSSFSheet hssfSheet = workBook.getSheetAt(0);
                        Iterator rowIterator = hssfSheet.rowIterator();
                        HSSFRow hssfRow;
                        while (rowIterator.hasNext()) {
                            hssfRow = (HSSFRow) rowIterator.next();
                            HSSFCell codeCell = hssfRow.getCell(0);
                            HSSFCell topicCell = hssfRow.getCell(1);
                            HSSFCell DescriptionCell = hssfRow.getCell(2);
                            HSSFCell scheduleCell = hssfRow.getCell(3);
                            HSSFCell startdateCell = hssfRow.getCell(4);
                            HSSFCell enddateCell = hssfRow.getCell(5);
                            HSSFCell faqCell = hssfRow.getCell(6);
                            HSSFCell attFileCell = hssfRow.getCell(7);
                            HSSFCell urlCell = hssfRow.getCell(8);


                            if (hssfRow.getRowNum() > 0) {
                                if (topicCell != null && !topicCell.getStringCellValue().trim().isEmpty()) {

                                    Knowledgebase kb = new Knowledgebase();
                                    KnowledgebaseDivision kbv = new KnowledgebaseDivision();

                                    kb.setTopic(topicCell.getStringCellValue());
                                    kb.setDescription(DescriptionCell.getStringCellValue().replaceAll("\n", "<br/>"));
                                    kb.setSchedule(scheduleCell.getBooleanCellValue());
                                    if(startdateCell!=null&&enddateCell!=null){
                                    kb.setStartdate(startdateCell.getDateCellValue());
                                    kb.setEnddate(enddateCell.getDateCellValue());
                                    }
                                    kb.setVersion("1.0");
                                    kb.setEnable(true);
                                    kb.setCreateBy(JSFUtil.getUserSession().getUserName());
                                    kb.setCreateDate(new Date());
                                    if(appvimport){
                                    kb.setApproval(appvimport);
                                    kb.setApprovalBy(JSFUtil.getUserSession().getUserName());
                                    kb.setApprovalDate(new Date());
                                    }
                                    kb.setFaq(faqCell.getBooleanCellValue());
                                    kb.setContentown(contentown);
                                    knowledgebaseDAO.create(kb);

                                    mapRelate.put(codeCell.getStringCellValue(), kb.getId());

                                    kbv.setKnowledgebaseDivisionPK(new KnowledgebaseDivisionPK(kb.getId(), kb.getVersion().trim()));
                                    kbv.setTopic(kb.getTopic());
                                    kbv.setDescription(kb.getDescription());
                                    kbv.setEnable(kb.getEnable());
                                    kbv.setSchedule(kb.getSchedule());
                                    if(startdateCell!=null&&enddateCell!=null){
                                    kbv.setStartdate(kb.getStartdate());
                                    kbv.setEnddate(kb.getEnddate());
                                    }
                                    kbv.setViewcount(kb.getViewcount());
                                    kbv.setCreateBy(JSFUtil.getUserSession().getUserName());
                                    kbv.setCreateDate(new Date());
                                    kbv.setUpdateBy(JSFUtil.getUserSession().getUserName());
                                    kbv.setUpdateDate(new Date());
                                    if(appvimport){
                                    kbv.setApproval(appvimport);
                                    kbv.setApprovalBy(JSFUtil.getUserSession().getUserName());
                                    kbv.setApprovalDate(new Date());
                                    }
                                    kbv.setContentown(contentown);
                                    knowledgebaseDAO.createDivision(kbv);

                                    ///////////attFile//////////////
                                    if (attFileCell != null) {
                                        String[] attFileList = (String[]) attFileCell.getStringCellValue().split(",");
                                        for (String attFile : attFileList) {
                                            KnowledgebaseAttfile kbaf = new KnowledgebaseAttfile();
                                            kbaf.setKnowledgebaseId(kb.getId());
                                            kbaf.setKbattfileFilename(attFile);
                                            kbaf.setCreateBy(JSFUtil.getUserSession().getUserName());
                                            kbaf.setCreateDate(new Date());
                                            knowledgebaseDAO.createAttFile(kbaf);
                                        }
                                    }
                                    ///////////url///////////////
                                    if (urlCell != null) {
                                        String[] urlList = (String[]) urlCell.getStringCellValue().split(";");
                                        for (String urlSum : urlList) {
                                            KnowledgebaseUrl kburl = new KnowledgebaseUrl();
                                            String[] url = (String[]) urlSum.split(",");

                                            kburl.setKnowledgebaseId(kb.getId());
                                            kburl.setKnowledgebaseVersion(kb.getVersion());
                                            kburl.setKburlText(url[0]);
                                            kburl.setKburlLink(url[1]);
                                            knowledgebaseDAO.createUrl(kburl);
                                        }
                                    }
                                    ////////// /////////////

                                    for (ServiceTypeValue s : serviceTypeValueList) {
                                        for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                                            for (LocationValue l : b.getLocationValueList()) {
                                                if (l.isSelected()) {
                                                    KnowledgebaseLocation knowledgebaseLocation = new KnowledgebaseLocation(kb.getId(), s.getServiceType().getId(), b.getBusinessUnit().getId(), l.getLocation().getId());
                                                    knowledgebaseDAO.createLocation(knowledgebaseLocation);
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
//////////////////////////////
                        rowIterator = hssfSheet.rowIterator();
                        while (rowIterator.hasNext()) {
                            hssfRow = (HSSFRow) rowIterator.next();
                            HSSFCell codeCell = hssfRow.getCell(0);
                            HSSFCell relateCell = hssfRow.getCell(9);
                            if (hssfRow.getRowNum() > 0) {
                            if (relateCell.getStringCellValue() != null && !relateCell.getStringCellValue().isEmpty() ) {
                                int kbid  =mapRelate.get(codeCell.getStringCellValue());                                
                                String[] relateList = relateCell.getStringCellValue().split(",");
                                for (String relate : relateList) {
                                    KnowledgebaseRelated kbrl = new KnowledgebaseRelated();
                                    int kbrelid=mapRelate.get(relate);
                                    kbrl.setKnowledgebaseRelatedPK(new KnowledgebaseRelatedPK(kbid, "1.0", kbrelid));
                                    knowledgebaseDAO.createRelated(kbrl);
                                }
                            }
                            }

                        }
                        //System.out.println(f);
                    }
                    fileUploadBean.createDirName("import");
                    fileUploadBean.moveFile(f, fileUploadBean.getFolderPath());
                }
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            //searchAction();
            return REFRESHIMPORT;
        }
    }

    private void fillServiceType() {
        serviceTypeValueList = new ArrayList<ServiceTypeValue>();
        List<ServiceType> list = serviceTypeDAO.findServiceTypeEntities();
        for (ServiceType s : list) {
            ServiceTypeValue serviceTypeValue = new ServiceTypeValue(s);
            serviceTypeValue.setBusinessUnitValueList(fillBu(s));

            serviceTypeValue.setSelected(true);

            serviceTypeValueList.add(serviceTypeValue);
        }
    }

    private List<BusinessUnitValue> fillBu(ServiceType serviceType) {
        List<BusinessUnitValue> businessUnitValueList = new ArrayList<BusinessUnitValue>();
        Collection<BusinessUnit> list = serviceType.getBusinessUnitCollection();
        for (BusinessUnit b : list) {
            BusinessUnitValue businessUnitValue = new BusinessUnitValue(b);
            businessUnitValue.setLocationValueList(fillLocation(serviceType.getId(), b));

            businessUnitValue.setSelected(true);

            businessUnitValueList.add(businessUnitValue);
        }
        return businessUnitValueList;
    }

    private List<LocationValue> fillLocation(Integer serviceTypeId, BusinessUnit businessUnit) {
        List<LocationValue> locationValueList = new ArrayList<LocationValue>();
        Collection<Location> list = businessUnit.getLocationCollection();

        for (Location location : list) {
            LocationValue locationValue = new LocationValue(location);

            locationValue.setSelected(true);

            locationValueList.add(locationValue);
        }
        return locationValueList;
    }

    public String importAttAction() {
        //ZipFile 
        try {

            List<File> zipList = attUploadBean.getFiles();

            for (File fzf : zipList) {
                File pathfile = new File(attUploadBean.getAbsolutePath() + "/unzip");

                //////////////////////////
                unzip(fzf, pathfile);
                //////////////////////////
                ////////////////////////  ค้นหา ไฟล์ Att เพื่อหา kb_id และย้ายไฟล์ ไป knowledgebase / id / file
                Map<String, Integer> mapAtt = knowledgebaseDAO.getAttList(fileUnziplist);
                //System.out.println(mapAtt.size());
                for (String uz : fileUnziplist) {
                    //System.out.println(uz+"="+mapAtt.get(uz));
                    File tempfile = new File(attUploadBean.getAbsolutePath() + "/unzip/" + uz);
                    //File attfile = new File(fileUploadBean.getAbsolutePath() + "/"+mapAtt.get(uz)+"/" + uz);
                    fileUploadBean.createDirName(mapAtt.get(uz).toString());
                    fileUploadBean.moveFile(tempfile, fileUploadBean.getFolderPath());

                }

                (new File(attUploadBean.getAbsolutePath()+"/"+fzf.getName())).delete();
 
            }

        } catch (Exception e) {
            log.error(e);
        } finally {



            return REFRESHIMPORT;
        }
    }

    public File unzip(File inFile, File outFolder) {
        try {
            int sfl = 0;
            BufferedOutputStream out = null;
            ZipInputStream in = new ZipInputStream(
                    new BufferedInputStream(
                    new FileInputStream(inFile)));
            ZipEntry entry;
            ZipFile zipFile = new ZipFile(inFile);
            fileUnziplist = new String[zipFile.size()];
            while ((entry = in.getNextEntry()) != null) {
                //System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];

                // write the files to the disk
                out = new BufferedOutputStream(
                        new FileOutputStream(outFolder.getPath() + "/" + entry.getName()), BUFFER);

                while ((count = in.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                fileUnziplist[sfl++] = entry.getName();
                cleanUp(out);
            }
            cleanUp(in);
            return outFolder;
        } catch (Exception e) {
            e.printStackTrace();
            return inFile;
        }
    }

    private void cleanUp(InputStream in) throws Exception {
        in.close();
    }

    private void cleanUp(OutputStream out) throws Exception {
        out.flush();
        out.close();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
