/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.QaQuestion;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaTransDetail;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Administrator
 */
public class QaFormInfoValue {
    private QaQuestion qaQuestion;
    private List<QaChoice> qaChoiceList;
    private String answer;
    private String remark;
    private SelectItem answerSelectItems[];
    private String[] answerCheckbox;
    private Integer seqNo;
    
    public QaFormInfoValue(QaQuestion qaQuestion, List<QaChoice> qaChoiceList, String answer, String remark, String[] answerCheckbox) {
        this.qaQuestion = qaQuestion;
        this.setChoiceList(qaChoiceList, answer, answerCheckbox);
        this.remark = remark;
    }
    
    public QaFormInfoValue(QaQuestion qaQuestion, List<QaChoice> qaChoiceList, String answer, String remark, QaTransDetail qaTransDetail, Integer seqNo) {
        this.qaQuestion = qaQuestion;
        this.setAnswer(qaChoiceList, answer, qaTransDetail, remark, seqNo);
    }
    
    private void setAnswer(List<QaChoice> qaChoiceList, String answer, QaTransDetail qaTransDetail, String remark, Integer seqNo) {
        this.qaChoiceList = qaChoiceList;
        String type = qaQuestion.getType();
        answer = "";
        if (type.equals("textfield")) {
           answer = qaTransDetail.getAnswer();
        } else if (type.equals("radio") || type.equals("dropdown")) {
                for(QaChoice c: qaChoiceList) {
                    if(!qaTransDetail.getAnswer().equals("")) {
                        if(c.getValue() == Integer.parseInt(qaTransDetail.getAnswer()))
                            answer = c.getLabel();
                    }
                }
        } else {
            String[] tempAnswer = qaTransDetail.getAnswer().split(",");
            for(int i=0; i< tempAnswer.length; i++)
                for(QaChoice c: qaChoiceList) {
                    if(!tempAnswer[i].equals("")) {
                        if(c.getValue() == Integer.parseInt(tempAnswer[i])) {
                            answer += c.getLabel();
                            if ((tempAnswer.length - 1) > i) {
                                answer += ", ";
                            }
                        }
                    }
                }
        }
        if(answer.equals(""))
            answer = "-";
        remark = qaTransDetail.getRemark();
        this.seqNo = seqNo;
        this.answer = answer;
        this.remark = remark;
    }
        
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QaQuestion getQaQuestion() {
        return qaQuestion;
    }

    public void setQaQuestion(QaQuestion qaQuestion) {
        this.qaQuestion = qaQuestion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    private void setChoiceList(List<QaChoice> qaChoiceList, String answer, String[] answerCheckbox) {
        this.qaChoiceList = qaChoiceList;
        String type = qaQuestion.getType();
        answer = "";
        if (type.equals("textfield")) {
            return;
        } else {
            answerSelectItems = new SelectItem[qaQuestion.getNoChoice()];
            answerCheckbox = new String[qaQuestion.getNoChoice()];
            Integer i = 0;
            for(QaChoice c: qaChoiceList) {
                answerSelectItems[i] = new SelectItem(c.getValue(), c.getLabel());
                answerCheckbox[i] = "";
                if(c.getDefault1()) {
                    if (type.equals("checkbox"))
                        answerCheckbox[i] = c.getValue()+"";
                    else
                        answer = c.getValue()+"";
                }
                i++;
            }
        }
        this.answer = answer;
        this.answerCheckbox = answerCheckbox;
    }
        
    public SelectItem[] getAnswerSelectItems() {
        return answerSelectItems;
    }
    
    public String[] getAnswerCheckbox() {
        return answerCheckbox;
    }
    
    public void setAnswerCheckbox(String[] answerCheckbox) {
        this.answerCheckbox = answerCheckbox;
    }
    
}
