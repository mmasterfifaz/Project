/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.report;

import com.maxelyz.social.controller.report.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class SocialReportSidemenuController {
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setCustomerDetail(null);
    }
    
    public boolean isShowMenuRight(){
      return isSLAPermitted() || isInformationProvidingPermitted();
    }
    
    public boolean isShowMenuLeft(){
       return isCustomerServicesPermitted() || isCampaignSalesPermitted();
    }
    

    
    public boolean isStatisticReportofDailyMessageByCasePermitted() {
        return SecurityUtil.isPermitted("social:report:dailymessagebycase:view");
    }
     
    public boolean isPerformanceByUserPermitted() {
        return SecurityUtil.isPermitted("social:report:performancebyuser:view");
    }
    
        

    
    public boolean isCustomerServicesPermitted() {
        return SecurityUtil.isPermitted("report:customerservices:view");
    }
    
    public boolean isContactSumByInboundPermitted() {
        return SecurityUtil.isPermitted("report:contactsummarybyinboundchannel:view");
    }
     
    public boolean isContactSumByOutboundPermitted() {
        return SecurityUtil.isPermitted("report:contactsummarybyoutboundchannel:view");
    }

    public boolean isContactSumByContactDatePermitted() {
        return SecurityUtil.isPermitted("report:contactsummarybycontactdate:view");
    }    
    
    public boolean isContactSumByStatusPermitted() {
        return SecurityUtil.isPermitted("report:contactsummarybystatus:view");
    }    
    
    public boolean isCaseHandlingByUserGroupPermitted() {
        return SecurityUtil.isPermitted("report:casehandlingbyusergroup:view");
    }

    public boolean isCaseDetailPermitted() {
        return SecurityUtil.isPermitted("report:casedetail:view");
    }    
    
    public boolean isCaseListPermitted() {
        return SecurityUtil.isPermitted("report:caselist:view");
    }
        
    public boolean isFirstCallPermitted() {
        return SecurityUtil.isPermitted("report:firstcall:view") ;
    }
    
    public boolean istop10casePermitted() {
        return SecurityUtil.isPermitted("report:top10case:view") ;
    }
    
    public boolean isMulticasePermitted() {
        return SecurityUtil.isPermitted("report:multicase:view") ;
    }
    
    public boolean isCampaignSalesPermitted() {
        return SecurityUtil.isPermitted("report:sale:view");
    }
  
    public boolean isSalePerfByUserPermitted() {
        return SecurityUtil.isPermitted("report:saleperformancebyuser:view");
    }
    
    public boolean isSalePerfByUserGroupPermitted() {
        return SecurityUtil.isPermitted("report:saleperformancebyusergroup:view");
    }
    
    public boolean isSalePerfByDatePermitted() {
        return SecurityUtil.isPermitted("report:saleperformancebydate:view");
    }
    
    public boolean isMarketListPerfPermitted() {
        return SecurityUtil.isPermitted("report:marketinglistperformance:view");
    }

    public boolean isYesSaleAnalysisPermitted() {
        return SecurityUtil.isPermitted("report:yessaleanalysis:view");
    }    
     
    public boolean isCallOutcomePermitted() {
        return SecurityUtil.isPermitted("report:calloutcome:view");
    }
    
    public boolean isCallOutcomeByListPermitted() {
        return  SecurityUtil.isPermitted("report:calloutcomebylist:view");
    }
    
    public boolean isCallOutcomeByListAllPermitted() {
        return  SecurityUtil.isPermitted("report:calloutcomebylistall:view");
    }

    public boolean isNoSaleReasonPermitted() {
        return SecurityUtil.isPermitted("report:nosalereason:view");
    }    
    
    public boolean isAIAExportApplicationPermitted() {
        return SecurityUtil.isPermitted("report:exportapplication:view");
    }
     
    public boolean isSLAPermitted() {
        return SecurityUtil.isPermitted("report:sla:view");
    }

    public boolean isSlaCaseCloseByCaseTypePermitted() {
        return SecurityUtil.isPermitted("report:slacaseclosedbycasetype:view");
    }

    public boolean isSlaCaseCloseByDatePermitted() {
        return SecurityUtil.isPermitted("report:slacaseclosedbydate:view");
    }
    
    public boolean isTransferDetailPermitted() {
        return SecurityUtil.isPermitted("report:transferdetail:view");
    }   
    
    public boolean isInformationProvidingPermitted() {
        return SecurityUtil.isPermitted("report:info:view");
    }
  
    public boolean isTop20KbPermitted() {
        return SecurityUtil.isPermitted("report:top20kb:view");
    }
    
    public boolean isTop20ProductInfoPermitted() {
        return SecurityUtil.isPermitted("report:top20productinfo:view");
    }

    public boolean isKbStructurePermitted() {
        return SecurityUtil.isPermitted("report:kbstructure:view");
    }
     
    public boolean isKbDailyUsePermitted() {
        return SecurityUtil.isPermitted("report:kbdailyuse:view") ;
    }

    public boolean isProductInfoDailyUsePermitted() {
        return SecurityUtil.isPermitted("report:productinfodailyuse:view");
    }    
    
    //motor
    
    public boolean isMotorRegistrationPermitted() {
        return SecurityUtil.isPermitted("report:motorregistration:view");
    }    
        
    public boolean isMotorPayInSlipUsePermitted() {
        return SecurityUtil.isPermitted("report:motorpayin:view");
    }    
    
    //lmg
    public boolean isSaleByRecordsPermitted() {
        return SecurityUtil.isPermitted("report:salebyrecords:view");
    }
    
     
    public boolean isCommentKbPermitted() {
        return SecurityUtil.isPermitted("report:commentkb:view");
    }
 
    
    //anuwat
    
    public boolean isCustomerStatByChannelPermitted() {
        return SecurityUtil.isPermitted("report:customerstatbychannel:view");
    }
    
    public boolean isMarketingRecordByAssignmentPermitted() {
        return SecurityUtil.isPermitted("report:marketingrecordbyassignment:view");
    }
    
    public boolean isMarketingRecordByUserPermitted() {
        return SecurityUtil.isPermitted("report:marketingrecordbyuser:view");
    }
    public boolean isMarketingPermitted() {
        return SecurityUtil.isPermitted("report:marketing:view");
    }
    public boolean isMarketingListByProductTypePermitted() {
        return SecurityUtil.isPermitted("report:marketinglistbyproducttype:view");
    }
    public boolean isMarketingListBySaleStatusPermitted() {
        return SecurityUtil.isPermitted("report:marketinglistbystatus:view");
    }
    public boolean isMarketingListByChannelPermitted() {
        return SecurityUtil.isPermitted("report:marketinglistbychannel:view");
    }
    public boolean isAgentPermitted() {
        return SecurityUtil.isPermitted("report:agent:view");
    }
    public boolean isAgentPerformancePermitted() {
        return SecurityUtil.isPermitted("report:agentperformance:view");
    }
    public boolean isAgentPerformanceBySaleStatusPermitted() {
        return SecurityUtil.isPermitted("report:agentperformancebysalestatus:view");
    }
     
    public boolean isSaleRecordByStatusPermitted() {
        System.out.println(SecurityUtil.isPermitted("report:salerecordbystatus:view"));
        return SecurityUtil.isPermitted("report:salerecordbystatus:view");
    }
    public boolean isSaleRecordByPaymentTransactionPermitted() {
        return SecurityUtil.isPermitted("report:salerecordbypaymenttransaction:view");
    }
    public boolean isSalePerformanceByProductPermitted() {
        return SecurityUtil.isPermitted("report:saleperformancebyproduct:view");
    }
    
}

