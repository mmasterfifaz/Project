package com.maxelyz.core.controller.share;

//ใช้แล้ว 
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.controller.admin.SwingTreeNodeImpl;
//import com.maxelyz.core.model.dao.BusinessUnitDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.*;
//import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
//import org.ajax4jsf.context.AjaxContext;
import javax.faces.bean.ViewScoped;
//import org.richfaces.component.UITree;
//import org.richfaces.component.UITreeNode;
//import org.richfaces.event.DropEvent;
//import org.richfaces.model.TreeNode;
//import org.richfaces.model.TreeNodeImpl;
//import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
//import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
//@RequestScoped
@ViewScoped
public class KnowledgePopupController implements Serializable {

    private static Logger log = Logger.getLogger(KnowledgePopupController.class);
    private static String REFRESH = "knowledgebase.xhtml";
    private static String EDIT = "knowledgebaseedit.xhtml";
    private static String VIEWVERSION = "knowledgebaseversion.xhtml";
    private Map<Long, Boolean> selectedIds = new ConcurrentHashMap<Long, Boolean>();
    private List<Knowledgebase> knowledgebases;
    private List<Knowledgebase> faqs;
    //anuwat
    private List<Knowledgebase> relateknowledgebases;
    private List<KnowledgebaseBoard> knowledgebasesboard;
    private List<KnowledgebaseAttfile> knowledgebaseAttfileList;
    private List<KnowledgebaseUrl> knowledgebasesUrlList;
    private String comment;
    private Long vote;
    private int displayvote;
    private boolean disablevote;
    private Users users;
    private String message;
    //anuwat
    private Knowledgebase knowledgebase;
    private String keyword;
//    private TreeNode root = null;
    private List<SwingTreeNodeImpl> root= null;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        message = "";
        knowledgebases = this.getKnowledgebaseDAO().findKnowledgebaseByUserGroupId(JSFUtil.getUserSession().getUserGroup().getId());
        faqs = this.getKnowledgebaseDAO().findFAQKBByUserGroupId(JSFUtil.getUserSession().getUserGroup().getId());
    }

    public void listener(ActionEvent event) {
        initialize();
    }

    public List<Knowledgebase> getList() {
        return getKnowledgebases();
    }

    public void searchListener() {//ActionEvent event) {
        searchAction();
    }

    public boolean isDisablevote() {
        return disablevote;
    }

    public void setDisablevote(boolean disablevote) {
        this.disablevote = disablevote;
    }

    public String viewAction() {
        return VIEWVERSION;
    }

    /**
     * Use in Front Knowledge Popup
     *
     * @param event
     */
    public void selectIdListener(ActionEvent event) {
        message = "";
        try {
            String id = (String) JSFUtil.getRequestParameterMap("id");
            if(id != null && !id.isEmpty()) {
                this.getKnowledgebaseDAO().updateViewKnowledgebaseById(new Integer(id));
                knowledgebase = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(id));
                if (knowledgebase != null && knowledgebase.getApproval()) {
                    users = usersDAO.findUsers(knowledgebase.getContentown());
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
                    fillKbAttFileList();
                    loadVate();
                    fillKbUrlList();
                } else {
                    knowledgebase = null;
                    message = "Knowledgebase does not Approve";
                }
                //Save kb to session
                UserSession userSession = JSFUtil.getUserSession();
                if (knowledgebase != null) {
                    userSession.setKnowledgebase(knowledgebase);
                }

                JSFUtil.getUserSession().showInboundContactSummary();            
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

//    public void dropListener(DropEvent dropEvent) {
//        if (!SecurityUtil.isPermitted("admin:kb:edit")) {
//            return;
//        }
//        Integer sourceid = (Integer) dropEvent.getDragValue();
//        Integer targetid = (Integer) dropEvent.getDropValue();
//        if (sourceid.equals(targetid)) {
//            return;
//        }
//        try {
//            put source (child) to target (parent)
//            Knowledgebase kb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(sourceid));
//            Knowledgebase refkb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(targetid));
//            kb.setKnowledgebase(refkb);
//            this.getKnowledgebaseDAO().edit(kb);
//            update knowledge member
//            searchAction();
//            render Tree
//            AjaxContext ac = AjaxContext.getCurrentInstance();
//            UITreeNode destNode = (dropEvent.getSource() instanceof UITreeNode) ? (UITreeNode) dropEvent.getSource() : null;
//            UITree destTree = destNode != null ? destNode.getUITree() : null;
//            ac.addComponentToAjaxRender(destTree);
//            
//        } catch (Exception e) {
//            log.error(e);
//        }
//
//    }

//    public void markAsRootListener(ActionEvent event) {
//        String selectedID = (String) JSFUtil.getRequestParameterMap("selectedId");
//        Knowledgebase kb = this.getKnowledgebaseDAO().findKnowledgebase(new Integer(selectedID));
//        kb.setKnowledgebase(null);
//        try {
//            this.getKnowledgebaseDAO().edit(kb);
//        } catch (Exception e) {
//            log.error(e);
//        }
//        searchAction();
//    }

    public String searchAction() {
//        if (keyword.trim().length() == 0) {
//            initialize();
//        } else { 
            knowledgebases = this.getKnowledgebaseDAO().findKnowledgebaseByName(JSFUtil.getUserSession().getUserGroup().getId(), keyword);
            faqs = this.getKnowledgebaseDAO().findFAQKBByName(JSFUtil.getUserSession().getUserGroup().getId(), keyword);
//        }
        initTree();
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

//    public String deleteAction() {
//        KnowledgebaseDAO dao = getKnowledgebaseDAO();
//        try {
//            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedId");
//            dao.disableKnowledgebaseEntity(new Integer(selectedID));
//        } catch (Exception e) {
//            log.error(e);
//        }
//        searchAction();
//        return REFRESH;
//    }
//
//    private void findAndChildNode(TreeNode parentNode, Knowledgebase parentKb) {
//        for (Knowledgebase childKb : parentKb.getKnowledgebaseCollection()) {
//            if (childKb.getEnable()) {
//                if (childKb.getApproval()) {
//
//                    if(childKb.getSchedule()){
//                    Date realdate = new Date();
//                    Calendar cal = Calendar.getInstance() ; 
//                    cal.setTime(childKb.getStartdate());
//                    Date sdate = cal.getTime(); 
//                    cal.setTime(childKb.getEnddate());
//                    cal.add(Calendar.DATE,1);
//                    Date edate = cal.getTime(); 
//                    if ( realdate.after(sdate) && realdate.before(edate)) {
//                        TreeNode childNode = new TreeNodeImpl();
//                        childNode.setData(childKb);
//                        parentNode.addChild(childKb.getId(), childNode);
//                        this.findAndChildNode(childNode, childKb);//Recursive
//                    }
//                    }else{
//                       TreeNode childNode = new TreeNodeImpl();
//                        childNode.setData(childKb);
//                        parentNode.addChild(childKb.getId(), childNode);
//                        this.findAndChildNode(childNode, childKb);//Recursive
//                    }
//                }
//
//            }
//        }
//    }
    
    private void findAndChildNode(SwingTreeNodeImpl parentNode, Knowledgebase parentKb) {
        SwingTreeNodeImpl childNode;
        for (Knowledgebase childKb : parentKb.getKnowledgebaseCollection()) {
            //call query check condition childKb
            if (childKb.getEnable() && childKb.getApproval()) {
                if(childKb.getSchedule()){
                    Date realdate = new Date();
                    Calendar cal = Calendar.getInstance() ; 
                    cal.setTime(childKb.getStartdate());
                    Date sdate = cal.getTime(); 
                    cal.setTime(childKb.getEnddate());
                    cal.add(Calendar.DATE,1);
                    Date edate = cal.getTime(); 
                    if ( realdate.after(sdate) && realdate.before(edate)) {
                        //set node tree
                        childNode = new SwingTreeNodeImpl(childKb.getId(), childKb.getTopic());
                        if(!childKb.getKnowledgebaseCollection().isEmpty()) {
                            this.findAndChildNode(childNode, childKb);//Recursive
                        }
                        parentNode.addChild(childNode);
                    }
                } else {
                    childNode = new SwingTreeNodeImpl(childKb.getId(), childKb.getTopic());
                    if(!childKb.getKnowledgebaseCollection().isEmpty()) {
                        this.findAndChildNode(childNode, childKb);//Recursive
                    }
                    parentNode.addChild(childNode);
                 }
            }
        }
    }

//    private void addNodes(String path, TreeNode node, List<Knowledgebase> kb, List<Knowledgebase> faq) {
//        if(!faq.isEmpty()){
//                TreeNodeImpl nodef = new TreeNodeImpl();
//                Knowledgebase root1 = new Knowledgebase();
//                root1.setTopic("FAQ");
//                nodef.setData(root1);
//                node.addChild(1, nodef);
//                Level1
//                for (Knowledgebase kb0 : faq) {
//                    TreeNodeImpl node1 = new TreeNodeImpl();
//                    node1.setData(kb0);
//                    nodef.addChild(kb0, node1);
//                    Level2 to n 
//                    findAndChildNode(node1, kb0);
//                }
//        }
//        if(!kb.isEmpty()){
//                TreeNodeImpl node0 = new TreeNodeImpl();
//                Knowledgebase root2 = new Knowledgebase();
//                root2.setTopic("Knowledgebases");
//                node0.setData(root2);
//
//                node.addChild(2, node0);
//                Level1
//                for (Knowledgebase kb1 : kb) {
//                    TreeNodeImpl node1 = new TreeNodeImpl();
//                    node1.setData(kb1);
//                    node0.addChild(kb1, node1);
//                    Level2 to n 
//                    findAndChildNode(node1, kb1);
//                }
//        }
//    }
    
    private void addNodes(String path, List<SwingTreeNodeImpl> root, List<Knowledgebase> kb, List<Knowledgebase> faq)  {
        SwingTreeNodeImpl nodeFaq;
        if(!faq.isEmpty()){
            SwingTreeNodeImpl nodef = new SwingTreeNodeImpl(null,"FAQ");
            root.add(nodef);
            for (Knowledgebase kb0 : faq) {
                nodeFaq = new SwingTreeNodeImpl(kb0.getId(), kb0.getTopic());
                if(!kb0.getKnowledgebaseCollection().isEmpty()) {
                    findAndChildNode(nodeFaq, kb0);
                } 
                nodef.addChild(nodeFaq);
            }
        }
        
        SwingTreeNodeImpl nodeKb;
        if(!kb.isEmpty()){
            SwingTreeNodeImpl nodeK = new SwingTreeNodeImpl(0,"Knowledgebases");
            root.add(nodeK);
            for (Knowledgebase kb1 : kb) {
                nodeKb = new SwingTreeNodeImpl(kb1.getId(), kb1.getTopic());
                if(!kb1.getKnowledgebaseCollection().isEmpty()) {
                    findAndChildNode(nodeKb, kb1);

                } 
                nodeK.addChild(nodeKb);
            }
        }
    }
//    private void initTree() {
//        root = new TreeNodeImpl();
//
//        addNodes(null, root, knowledgebases, faqs);
//         
//    }
    public void initTree() {
        root = new ArrayList<SwingTreeNodeImpl>(); 
        addNodes(null, root, knowledgebases, faqs);
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
    
//    public TreeNode getRoot() {
//        if (root == null) {
//            initTree();
//        }
//        return root;
//    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
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

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
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

//    anuwat
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

    public List<KnowledgebaseAttfile> getKnowledgebaseAttfileList() {
        return knowledgebaseAttfileList;
    }

    public void setKnowledgebaseAttfileList(List<KnowledgebaseAttfile> knowledgebaseAttfileList) {
        this.knowledgebaseAttfileList = knowledgebaseAttfileList;
    }

    public List<KnowledgebaseUrl> getKnowledgebasesUrlList() {
        return knowledgebasesUrlList;
    }

    public void setKnowledgebasesUrlList(List<KnowledgebaseUrl> knowledgebasesUrlList) {
        this.knowledgebasesUrlList = knowledgebasesUrlList;
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

    public void saveVote(ActionEvent event) throws Exception {
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

    private void fillKbAttFileList() {
        try {
            knowledgebaseAttfileList = new ArrayList<KnowledgebaseAttfile>();
            if (knowledgebase.getId() != null) {
                List<KnowledgebaseAttfile> list = this.getKnowledgebaseDAO().findKnowledgebaseAttFileById(knowledgebase.getId());


                for (KnowledgebaseAttfile s : list) {
                    knowledgebaseAttfileList.add(s);
                }

            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void fillKbUrlList() {
        try {
            knowledgebasesUrlList = new ArrayList<KnowledgebaseUrl>();
            if (knowledgebase.getId() != null && !knowledgebase.getVersion().isEmpty()) {
                List<KnowledgebaseUrl> list = this.getKnowledgebaseDAO().findKnowledgebaseUrlById(knowledgebase.getId(), knowledgebase.getVersion());


                for (KnowledgebaseUrl s : list) {
                    knowledgebasesUrlList.add(s);
                }

            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    
    public void sendMail(ActionEvent event) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
