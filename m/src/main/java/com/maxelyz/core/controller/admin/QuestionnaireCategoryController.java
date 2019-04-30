package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaChoiceDAO;
import com.maxelyz.core.model.dao.QaQuestionDAO;
import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaQuestion;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@ManagedBean
@RequestScoped
public class QuestionnaireCategoryController {
            
    private static Logger log = Logger.getLogger(QuestionnaireCategoryController.class);
    private static String REFRESH = "questionnairecategory.xhtml?faces-redirect=true";
    private static String EDIT = "questionnairecategoryedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<QaCategory> qaCatergoryList;

    @ManagedProperty(value = "#{qaCategoryDAO}")
    private QaCategoryDAO qaCategoryDAO;
    @ManagedProperty(value = "#{qaQuestionDAO}")
    private QaQuestionDAO qaQuestionDAO;
    @ManagedProperty(value = "#{qaChoiceDAO}")
    private QaChoiceDAO qaChoiceDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:questionnairecategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:questionnairecategory:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:questionnairecategory:delete");
    }

    public List<QaCategory> getQaCatergoryList() {
        return  qaCategoryDAO.findQaCategoryEntities();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    QaCategory qaCategory = qaCategoryDAO.findQaCategory(item.getKey());
                    qaCategory.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    qaCategory.setUpdateDate(new Date());
                    qaCategory.setEnable(false);
                    qaCategoryDAO.edit(qaCategory);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String copyAction() {
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("id");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            QaCategoryDAO dao = getQaCategoryDAO();

            QaCategory qaCategory = dao.findQaCategory(new Integer(selectedID));
            QaCategory qaCategoryCopy = new QaCategory();

            int count =0;
            count = dao.checkQaCategoryName(qaCategory.getName(),0);
            if(count!=0){
                String name = qaCategory.getName();
                String name2 = qaCategory.getName();
                int count1 = StringUtils.countMatches(qaCategory.getName(), "- Copy ");
                String str1 = "";
                int number1 =0;
                boolean process = true;
                if(count1!=0 && qaCategory.getName().substring(qaCategory.getName().length()-6,qaCategory.getName().length()).equals("- Copy")) {
                    str1 = name.substring(name.length() - (4 + (7 * (count1+1))), name.length());
                    if(str1.indexOf("(") != -1 && str1.indexOf(")") != -1){
                        str1 = str1.substring(str1.indexOf("(") + 1, str1.indexOf(")"));
                        number1 = Integer.parseInt(str1);
                    }else {
                        process = false;
                        int countCopy = StringUtils.countMatches(qaCategory.getName(), "- Copy ");
                        int length;
                        if(countCopy==0){
                            length = (4+(7*(countCopy+1))) + qaCategory.getName().length();
                        }else {
                            length = (4+(7*(countCopy+1))) +  qaCategory.getName().length();
                        }
                        name = dao.getQaCategoryNameForCheckCopy(qaCategory.getName(),length+1);
                        if(name.contains(" - Copy") && name.substring(name.length() - 6, name.length()).equals("- Copy")) {
                            int count3 = StringUtils.countMatches(name, "- Copy ");
                            String str = "";
                            str = name.substring(name.length() - (4+(7*(count3+1))), name.length());
                            if(str1.indexOf("(") != -1 && str1.indexOf(")") != -1) {
                                str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                                int numberForCopy = Integer.parseInt(str);
                                qaCategoryCopy.setName(name.replace("(" + numberForCopy + ") - Copy", "(" + (numberForCopy + 1) + ") - Copy"));
                            } else {
                                if(name.contains(" - Copy") && name.substring(name.length() - 6, name.length()).equals("- Copy") && !name.equals(name2)) {
                                    int count4 = StringUtils.countMatches(name, "- Copy ");
                                    String str4 = "";
                                    str4 = name.substring(name.length() - (4+(7*(count4+1))), name.length());
                                    str4 = str4.substring(str4.indexOf("(") + 1, str4.indexOf(")"));
                                    int numberForCopy = Integer.parseInt(str4);
                                    qaCategoryCopy.setName(name.substring(0,name.length() - (name.length()-name2.length()))+ name.substring(name.length() - (name.length() - name2.length()),name.length()).replace("(" + numberForCopy + ") - Copy", "(" + (numberForCopy + 1) + ") - Copy"));
                                } else {
                                    qaCategoryCopy.setName(qaCategory.getName() + " (2) - Copy");
                                }
                            }
                        } else {
                            qaCategoryCopy.setName(qaCategory.getName() + " (2) - Copy");
                        }
                    }
                }
                if(process){
                    if(name.contains(" - Copy") && qaCategory.getName().substring(qaCategory.getName().length()-6,qaCategory.getName().length()).equals("- Copy")){
                        int countCopy = StringUtils.countMatches(qaCategory.getName(), "- Copy ");
                        int length;
                        if(countCopy==0){
                            length = (4+(7*(countCopy+1))) + qaCategory.getName().length();
                        }else {
                            length = (4+(7*(countCopy+1))) +  qaCategory.getName().length();
                        }
                        int count2 = dao.countQaCategoryNameLike(qaCategory.getName(),length +1);
                        if(count2==1) {
                            name += " - Copy";
                            int count3 = StringUtils.countMatches(name, "- Copy ");
                            String strName = "";
                            strName = name.substring(name.length() - (4+(7*((count3+1)))), name.length());
                            strName = strName.substring(strName.indexOf("(") + 1, strName.indexOf(")"));
                            int numberForCopy = Integer.parseInt(strName);
                            qaCategoryCopy.setName(name.replace("(" + numberForCopy + ")", "(" + (numberForCopy) + ")"));
                        }else{
                            name = dao.getQaCategoryNameForCheckCopy(qaCategory.getName().replace("(" + number1 + ")", "(%)"),0);
                            int count3 = StringUtils.countMatches(name, "- Copy ");
                            String strName = "";
                            strName = name.substring(name.length() - (4+(7*(count3+1))), name.length());
                            strName = strName.substring(strName.indexOf("(") + 1, strName.indexOf(")"));
                            int numberForCopy = Integer.parseInt(strName);
                            qaCategoryCopy.setName(name.replace("(" + numberForCopy + ")", "(" + (numberForCopy + 1) + ")"));
                        }
                    }else {
                        int countCopy = StringUtils.countMatches(qaCategory.getName(), "- Copy ");
                        int length;
                        if(countCopy==0){
                            length = (4+(7*(countCopy+1))) + qaCategory.getName().length();
                        }else {
                            length = (4+(7*(countCopy+1))) +  qaCategory.getName().length();
                        }
                        name = dao.getQaCategoryNameForCheckCopy(qaCategory.getName(), length + 1);
                        if(name.contains(" - Copy") && name.substring(name.length() - 6, name.length()).equals("- Copy")) {
                            int count3 = StringUtils.countMatches(name, "- Copy ");
                            String str = "";
                            str = name.substring(name.length() - (4+(7*(count3+1))), name.length());
                            str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                            int numberForCopy = Integer.parseInt(str);
                            qaCategoryCopy.setName(name.replace("(" + numberForCopy + ")", "(" + (numberForCopy + 1) + ")"));
                        } else {
                            qaCategoryCopy.setName(qaCategory.getName() + " (2) - Copy");
                        }
                    }
                }
            }else{
                qaCategoryCopy.setName(qaCategory.getName());
            }
            qaCategoryCopy.setDescription(qaCategory.getDescription());
            qaCategoryCopy.setEnable(true);
            qaCategoryCopy.setCreateBy(username);
            qaCategoryCopy.setCreateDate(now);
            dao.create(qaCategoryCopy);
            
            Collection<QaQuestion> qaQuestionList = qaCategory.getQaQuestionCollection();
            List<QaQuestion> qaQuestionCopyList = new ArrayList<QaQuestion>();
            for(QaQuestion qaq: qaQuestionList) {
                QaQuestion questionCopy = new QaQuestion();
                
                questionCopy.setQaCategory(qaCategoryCopy);
                questionCopy.setCode(qaq.getCode());
                questionCopy.setName(qaq.getName());
                questionCopy.setNoChoice(qaq.getNoChoice());
                questionCopy.setSeqNo(qaq.getSeqNo());
                questionCopy.setShowNa(qaq.getShowNa());
                questionCopy.setShowRemark(qaq.getShowRemark());
                questionCopy.setType(qaq.getType());
                
                Collection<QaChoice> qaChoiceList = qaq.getQaChoiceCollection();
                List<QaChoice> qaChoiceCopyList = new ArrayList<QaChoice>();
                this.getQaQuestionDAO().create(questionCopy);

                for(QaChoice qac: qaChoiceList){
                    QaChoice choiceCopy = new QaChoice();
                    
                    choiceCopy.setQaQuestion(questionCopy);
                    choiceCopy.setLabel(qac.getLabel());
                    choiceCopy.setSeqNo(qac.getSeqNo());
                    choiceCopy.setValue(qac.getValue());
                    choiceCopy.setDefault1(qac.getDefault1());
                    
                    this.getQaChoiceDAO().create(choiceCopy);
                    qaChoiceCopyList.add(choiceCopy);
                }
                questionCopy.setQaChoiceCollection(qaChoiceCopyList);
                this.getQaQuestionDAO().edit(questionCopy);
                qaQuestionCopyList.add(questionCopy);
            }
            qaCategory.setQaQuestionCollection(qaQuestionCopyList);
            dao.edit(qaCategoryCopy);
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
  
    public QaCategoryDAO getQaCategoryDAO() {
        return qaCategoryDAO;
    }

    public void setQaCategoryDAO(QaCategoryDAO qaCategoryDAO) {
        this.qaCategoryDAO = qaCategoryDAO;
    }

    public QaChoiceDAO getQaChoiceDAO() {
        return qaChoiceDAO;
    }

    public void setQaChoiceDAO(QaChoiceDAO qaChoiceDAO) {
        this.qaChoiceDAO = qaChoiceDAO;
    }

    public QaQuestionDAO getQaQuestionDAO() {
        return qaQuestionDAO;
    }

    public void setQaQuestionDAO(QaQuestionDAO qaQuestionDAO) {
        this.qaQuestionDAO = qaQuestionDAO;
    }


}
