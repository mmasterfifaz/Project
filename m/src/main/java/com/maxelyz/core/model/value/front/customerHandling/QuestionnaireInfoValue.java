package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.Questionnaire;
import com.maxelyz.core.model.entity.QuestionnaireDetail;
import javax.faces.model.SelectItem;

public final class QuestionnaireInfoValue<T> {

    private Questionnaire questionnaire;
    private QuestionnaireDetail questionnaireDetail;
    private T purchaseOrderQuestionaire;
    private SelectItem answerSelectItems[];

    public QuestionnaireInfoValue(Questionnaire questionnare, QuestionnaireDetail questionnaireDetail, T purchaseOrderQuestionaire) {
        this.questionnaire = questionnare;
        this.setQuestionnaireDetail(questionnaireDetail);
        this.purchaseOrderQuestionaire = purchaseOrderQuestionaire;

    }

    public T getPurchaseOrderQuestionaire() {
        return purchaseOrderQuestionaire;
    }

    public void setPurchaseOrderQuestionaire(T purchaseOrderQuestionaire) {
        this.purchaseOrderQuestionaire = purchaseOrderQuestionaire;
    }

    public String getQuestion() {
        return questionnaireDetail.getQuestion();
    }

    public int getNo() {
        return questionnaireDetail.getNo();
    }

    private void setQuestionnaireDetail(QuestionnaireDetail questionnaireDetail) {
        this.questionnaireDetail = questionnaireDetail;
        String type = questionnaire.getType();
        if (type.equals("open")) {
            return;
        }
        if (type.equals("multiple2")) {
            answerSelectItems = new SelectItem[2];
            answerSelectItems[0] = new SelectItem(1, questionnaireDetail.getAnswer1());
            answerSelectItems[1] = new SelectItem(2, questionnaireDetail.getAnswer2());
        } else if (type.equals("multiple3")) {
            answerSelectItems = new SelectItem[3];
            answerSelectItems[0] = new SelectItem(1, questionnaireDetail.getAnswer1());
            answerSelectItems[1] = new SelectItem(2, questionnaireDetail.getAnswer2());
            answerSelectItems[2] = new SelectItem(3, questionnaireDetail.getAnswer3());
        } else if (type.equals("multiple4")) {
            answerSelectItems = new SelectItem[4];
            answerSelectItems[0] = new SelectItem(1, questionnaireDetail.getAnswer1());
            answerSelectItems[1] = new SelectItem(2, questionnaireDetail.getAnswer2());
            answerSelectItems[2] = new SelectItem(3, questionnaireDetail.getAnswer3());
            answerSelectItems[3] = new SelectItem(4, questionnaireDetail.getAnswer4());
        } else {
            answerSelectItems = new SelectItem[5];
            answerSelectItems[0] = new SelectItem(1, questionnaireDetail.getAnswer1());
            answerSelectItems[1] = new SelectItem(2, questionnaireDetail.getAnswer2());
            answerSelectItems[2] = new SelectItem(3, questionnaireDetail.getAnswer3());
            answerSelectItems[3] = new SelectItem(4, questionnaireDetail.getAnswer4());
            answerSelectItems[4] = new SelectItem(5, questionnaireDetail.getAnswer5());
        }
    }

    public SelectItem[] getAnswerSelectItems() {
        return answerSelectItems;
    }
   

}
