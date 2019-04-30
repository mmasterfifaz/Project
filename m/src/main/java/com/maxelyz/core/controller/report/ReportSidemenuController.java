/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

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
public class ReportSidemenuController {
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setCustomerDetail(null);
        JSFUtil.getUserSession().setMainPage("report");
    }
    
    public boolean isShowMenuRight(){
      return isSLAPermitted() || isInformationProvidingPermitted() || isAdminPermitted()
              || isMarketingPermitted() || isTransferDetailPermitted() || isAgentPermitted() || isTelesalesReportPermitted();
    }
    
    public boolean isShowMenuLeft(){
       return isCustomerServicesPermitted() || isCampaignSalesPermitted() || isSocialPermitted();
    }
    
//    public boolean isSocialPermitted() {
//        return SecurityUtil.isPermitted("social:report:view");
//    }
    
    public boolean isSocialPermitted() {
        return SecurityUtil.isPermitted("report:social:view");
    }
         
    public boolean isCustomerServicesPermitted() {
        return SecurityUtil.isPermitted("report:customerservices:view");
    }
    
    public boolean isAgentByCaseTypePermitted() {
        return SecurityUtil.isPermitted("report:soagentbycasetype:view");
    }
    
    public boolean isPerformanceByPeriodPermitted() {
        return SecurityUtil.isPermitted("report:soperformancebyperiod:view");
    }

    public boolean isCustomerDetailByDatePermitted() {
        return SecurityUtil.isPermitted("report:socustomerdetailbydate:view");
    }

    public boolean isForwardMsgByDatePermitted() {
        return SecurityUtil.isPermitted("report:soforwardmsgbydate:view");
    }

    public boolean isPerformanceByBreaktimePermitted() {
        return SecurityUtil.isPermitted("report:soperformancebybreaktime:view");
    }

    public boolean isPerformanceByOperationPermitted() {
        return SecurityUtil.isPermitted("report:soperformancebyoperation:view");
    }

    public boolean isApprovalReasonByUserPermitted() {
        return SecurityUtil.isPermitted("report:soapprovalreasonbyuser:view");
    }

    public boolean isIgnoreReasonByUserPermitted() {
        return SecurityUtil.isPermitted("report:soignorereasonbyuser:view");
    }

    public boolean isSLAPerformanceByCustomerPermitted() {
        return SecurityUtil.isPermitted("report:soslaperformancebycustomer:view");
    }

    public boolean isSLAPerformanceBySourcePermitted() {
        return SecurityUtil.isPermitted("report:soslaperformancebysource:view");
    }

    public boolean isSLAPerformanceByUserPermitted() {
        return SecurityUtil.isPermitted("report:soslaperformancebyuser:view");
    }

    public boolean isSLAApprovalByCasePermitted() {
        return SecurityUtil.isPermitted("report:soslaapprovalbycase:view");
    }

    public boolean isSLAApprovalBySupPermitted() {
        return SecurityUtil.isPermitted("report:soslaapprovalbysup:view");
    }

    public boolean isMsgPerformanceByCasePermitted() {
        return SecurityUtil.isPermitted("report:somsgperformancebycase:view");
    }

    public boolean isDailyMsgByCasePermitted() {
        return SecurityUtil.isPermitted("report:sodailymsgbycase:view");
    }

    public boolean isMonthlyMsgByCasePermitted() {
        return SecurityUtil.isPermitted("report:somonthlymsgbycase:view");
    }

    public boolean isPerformanceByUserPermitted() {
        return SecurityUtil.isPermitted("report:soperformancebyuser:view");
    }

    public boolean isApprovalByCasePermitted() {
        return SecurityUtil.isPermitted("report:soapprovalbycase:view");
    }

    public boolean isMsgByCustomerPermitted() {
        return SecurityUtil.isPermitted("report:somsgbycustomer:view");
    }

    public boolean isMsgByServicePermitted() {
        return SecurityUtil.isPermitted("report:somsgbyservice:view");
    }

    public boolean isApprovalEmailByDayPermitted() {
        return SecurityUtil.isPermitted("report:soapprovalemailbyday:view");
    }
    
    public boolean isPerformanceByDatePermitted() {
        return SecurityUtil.isPermitted("report:soperformancebydate:view");
    }

    public boolean isCustomerByContactPermitted() {
        return SecurityUtil.isPermitted("report:socustomerbycontact:view");
    }

    public boolean isMsgPerformanceByPriorityTimePermitted() {
        return SecurityUtil.isPermitted("report:somsgperformancebyprioritytime:view");
    }

    public boolean isMsgPerformanceByPriorityPermitted() {
        return SecurityUtil.isPermitted("report:somsgperformancebypriority:view");
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
    
    public boolean isSummaryReportCheckLeadPermitted() {
        return SecurityUtil.isPermitted("report:summaryreportchecklead:view");
    }
    
    public boolean isCallCodeByCampaignPermitted() {
        return SecurityUtil.isPermitted("report:callcodebycampaignid:view");
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
    
    public boolean isTelesalesReportPermitted() {
        return SecurityUtil.isPermitted("report:telesales:view");
    }
    
    //public boolean isMtlReportPermitted() {
    //    return SecurityUtil.isPermitted("report:mtl:view");
    //}
    
    public boolean isMtlSalesReportPermitted() {
        return SecurityUtil.isPermitted("report:salesreport:view");
    }
    
    public boolean isMtlOutcomeBreakdownReportPermitted() {
        return SecurityUtil.isPermitted("report:outcomebreakdownreport:view");
    }
    
    public boolean isFwdSalesReportPermitted() {
        return SecurityUtil.isPermitted("report:fwdsalesreport:view");
    }
    
    public boolean isFwdPerformanceReportPermitted() {
        return SecurityUtil.isPermitted("report:fwdperformancereport:view");
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
    public boolean isMediaPlanPermitted() {
        return SecurityUtil.isPermitted("report:mediaplanreport:view");
    }
    public boolean isCollectionPermitted() {
        return SecurityUtil.isPermitted("report:collectionreport:view");
    }
    public boolean isTotalSalesPermitted() {
        return SecurityUtil.isPermitted("report:totalsalesreport:view");
    }
    // 'isMarketingListExportPermitted()' added by Songwisit on 9 June 2016
    public boolean isMarketingListExportPermitted() {
        return SecurityUtil.isPermitted("report:marketinglistexport:view");
    }
    public boolean isMarketingListByChannelPermitted() {
        return SecurityUtil.isPermitted("report:marketinglistbychannel:view");
    }
    public boolean isAgentPermitted() {
        return SecurityUtil.isPermitted("report:agent:view");
    }
    public boolean isAdminPermitted() {
        return SecurityUtil.isPermitted("report:admin:view");
    }
    public boolean isAttendanceReportPermitted() {
        return SecurityUtil.isPermitted("report:attendancereport:view");
    }
    public boolean isAgentPerformancePermitted() {
        return SecurityUtil.isPermitted("report:agentperformance:view");
    }
    public boolean isAgentPerformanceBySaleStatusPermitted() {
        return SecurityUtil.isPermitted("report:agentperformancebysalestatus:view");
    }
     
    public boolean isSaleRecordByStatusPermitted() {
        return SecurityUtil.isPermitted("report:salerecordbystatus:view");
    }
    public boolean isSaleRecordByPaymentTransactionPermitted() {
        return SecurityUtil.isPermitted("report:salerecordbypaymenttransaction:view");
    }
    public boolean isSalePerformanceByProductPermitted() {
        return SecurityUtil.isPermitted("report:saleperformancebyproduct:view");
    }
    public boolean isDailyCallStatusPermitted() {
        return SecurityUtil.isPermitted("report:dailycallstatus:view") ;
    }
    
    public boolean isRejectCollectionReportPermitted() {
        return SecurityUtil.isPermitted("report:rejectcollectionreport:view");
    }
    
    public boolean isCallCodeLeadsSegmentAnalysisPermitted() {
        return SecurityUtil.isPermitted("report:callcodeleadssegmentanalysis:view") ;
    }
    
    public boolean isLeadSegmentationPerformanceByMarketingPermitted() {
        return SecurityUtil.isPermitted("report:leadsegmentationperformancebymarketing:view") ;
    }
    
    public boolean isLeadSegmentationPerformanceByCustomerPermitted() {
        return SecurityUtil.isPermitted("report:leadsegmentationperformancebycustomer:view") ;
    }
    
    public boolean isSummaryBillingTransactionPermitted() {
        return SecurityUtil.isPermitted("report:summarybillingtransaction:view") ;
    }

    public boolean isLeadSegmentationPerformanceByFamilyPermitted() {
        return SecurityUtil.isPermitted("report:leadsegmentationperformancebyfamily:view") ;
    }

    public boolean isDailyPerformanceReportbyTSRPermitted() {
        return SecurityUtil.isPermitted("report:dailyperformancereportbytsr:view");
    }

    public boolean isDailySaleByAgentPermitted() {
        return SecurityUtil.isPermitted("report:dailysalebyagent:view");
    }

    public boolean isNewDailyReportPermitted() {
        return SecurityUtil.isPermitted("report:newdailyreport:view");
    }
    
    public boolean isAgentPerformanceSummaryReportPermitted() {
        return SecurityUtil.isPermitted("report:agentperformancesummaryreport:view");
    }
    
    public boolean isTelesalesDailyReportByInsurerPermitted() {
        return SecurityUtil.isPermitted("report:telesalesdailyreportbyinsurer:view");
    }
    
    public boolean isTelesalesDailyReportByProductPermitted() {
        return SecurityUtil.isPermitted("report:telesalesdailyreportbyproduct:view");
    }
    
    public boolean isProductivityByTelesaleReportPermitted() {
        return SecurityUtil.isPermitted("report:productivitybytelesale:view");
    }
    
    public boolean isLeadPerformanceReportPermitted() {
        return SecurityUtil.isPermitted("report:leadperformancereport:view");
    }
    
    public boolean isDailySalePerfomanceReportPermitted() {
        return SecurityUtil.isPermitted("report:dailysaleperfomancereport:view");
    }
    
}

