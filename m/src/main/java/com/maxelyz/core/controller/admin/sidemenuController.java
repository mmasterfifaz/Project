
package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class sidemenuController {
    @PostConstruct
    public void initialize() {
   
    }
    
    public boolean isShowMenuRight(){
      return isAgentScriptPermitted() || isHolidayPermitted() || isMasterDataPermitted() || isUserManagementPermitted() || isConfigurationPermitted() || isWorkflowManagementPermitted() || isDeclarationPermitted() || isChildRegPermitted() || isChildRegTypePermitted() || isManageListValuePermitted();
    }
    
    public boolean isShowMenuLeft(){
        return isMarketingSourcePermitted() || isProductManagementPermitted() || isCampaignPermitted() || isSalesManagementPermitted() || isInformationManagementPermitted() || isListExclusionPermitted() || isWorkflowManagementPermitted();
    }
    
    public boolean isMarketingSourcePermitted() {
        return SecurityUtil.isPermitted("admin:marketing:view");
    }
        
    public boolean isMarketingListSourcePermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistsource:view");
    }
    
    public boolean isMarketingListUploadPermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistupload:view");
    }
    
      public boolean isProductSourcePermitted() {
        return SecurityUtil.isPermitted("admin:productsource:view");
    }
      
    public boolean isProductManagementPermitted() {
        return SecurityUtil.isPermitted("admin:product:view") ;
    }
    
    public boolean isProductCategoryPermitted() {
        return SecurityUtil.isPermitted("admin:productcategory:view");
    }
    
    public boolean isProductInformationPermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:view") ;
    }
    
    public boolean isCampaignPermitted() {
        return SecurityUtil.isPermitted("admin:campaign:view") ;
    }
    
    public boolean isSalesManagementPermitted() {
        return SecurityUtil.isPermitted("admin:sales:view");
    }
    
    public boolean isDeclarationPermitted() {
        return SecurityUtil.isPermitted("admin:declaration:view");
    }
    
    public boolean isChildRegPermitted() {
        return SecurityUtil.isPermitted("admin:childreg:view");
    }
    
    public boolean isChildRegTypePermitted() {
        return SecurityUtil.isPermitted("admin:childregtype:view");
    }

    public boolean isManageListValuePermitted() {
        return SecurityUtil.isPermitted("admin:managelistvalue:view");
    }

    public boolean isNoSaleReasonPermitted() {
        return SecurityUtil.isPermitted("admin:nosalereason:view");
    }   
    
    public boolean isAssignmentListMonitoringPermitted() {
        return SecurityUtil.isPermitted("admin:assignmentlistmonitoring:view");
    }
    
    public boolean isAutoAssignmentQueuePermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentqueue:view");
    }
        
    public boolean isContactRecordPermitted() {
        return SecurityUtil.isPermitted("admin:contactrecord:view");
    }
    
    public boolean isApprochingRecordPermitted() {
        return SecurityUtil.isPermitted("admin:approchingrecord:view");
    }

    public boolean isSendDocumentPermitted() {
        return SecurityUtil.isPermitted("admin:senddocument:view");
    }

    public boolean isSalesApprovalPermitted() {
        return SecurityUtil.isPermitted("admin:salesapproval:view");
    }

    public boolean isExportSalesResultPermitted() {
        return SecurityUtil.isPermitted("admin:exportsalesresult:view");
    }
    
    public boolean isExportYesFilePermitted() {
        return SecurityUtil.isPermitted("admin:exportyesfile:view");
    }

    public boolean isExportNoFilePermitted() {
        return SecurityUtil.isPermitted("admin:exportnofile:view");
    }

    public boolean isExportVoiceFilePermitted() {
        return SecurityUtil.isPermitted("admin:exportvoicefile:view");
    }

    public boolean isRawdataFilePermitted() {
        return SecurityUtil.isPermitted("admin:rawdatafile:view");
    }

    public boolean isImportPaymentPermitted() {
        return SecurityUtil.isPermitted("admin:importpayment:view");
    }
    
    public boolean isExportPaymentPermitted() {
        return SecurityUtil.isPermitted("admin:exportpayment:view");
    }
    
    public boolean isInformationManagementPermitted() {
        return SecurityUtil.isPermitted("admin:info:view") ;
    }
    
    public boolean isSocialPermitted() {
        return SecurityUtil.isPermitted("admin:social:view") ;
    }

    public boolean isSocialCaseTypePermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetype:view") ;
    }
    
    public boolean isSocialCaseTopicPermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetopic:view") ;
    }
    
    public boolean isSocialPriorityPermitted() {
        return SecurityUtil.isPermitted("admin:socialpriority:view") ;
    }
    
    public boolean isSocialAccountPermitted() {
        return SecurityUtil.isPermitted("admin:socialaccount:view") ;
    }
    
    public boolean isSocialServicePermitted() {
        return SecurityUtil.isPermitted("admin:socialservice:view") ;
    }
                
    public boolean isSocialTransferPermitted() {
        return SecurityUtil.isPermitted("social:transfer:view") ;
    }
    
    public boolean isSocialInboxQuotaPermitted() {
        return SecurityUtil.isPermitted("admin:inboxQuota:view") ;
    }
    
    public boolean isSocialSignaturePermitted() {
        return SecurityUtil.isPermitted("admin:emailSignature:view") ;
    }
    
    public boolean isTransferReasonPermitted() {
        return SecurityUtil.isPermitted("admin:transferReason:view") ;
    }
    
    public boolean isNotReadyReasonPermitted() {
        return SecurityUtil.isPermitted("admin:notreadyReason:view") ;
    }
    
    public boolean isIgnoreReasonPermitted() {
        return SecurityUtil.isPermitted("admin:ignoreReason:view") ;
    }       
    
    public boolean isLogoffReasonPermitted() {
        return SecurityUtil.isPermitted("admin:logoffReason:view") ;
    }
        
    public boolean isSocialEmailAccountPermitted() {
        return SecurityUtil.isPermitted("admin:emailAccount:view") ;
    }
    
    public boolean isKnowledgeBasePermitted() {
        return SecurityUtil.isPermitted("admin:kb:view");
    }

    public boolean isMsgBroadcastPermitted() {
        return SecurityUtil.isPermitted("admin:messagebroadcast:view");
    }

    public boolean isNewsPermitted() {
        return SecurityUtil.isPermitted("admin:news:view") ;
    }
    
    public boolean isListExclusionPermitted() {
        return SecurityUtil.isPermitted("admin:listexclusion:view") ;
    }
    
    public boolean isAgentScriptPermitted() {
        return SecurityUtil.isPermitted("admin:agentscript:view") ;
    }
        
    public boolean isHolidayPermitted() {
        return SecurityUtil.isPermitted("admin:holiday:view") ;
    }
    
    public boolean isMasterDataPermitted() {
        return SecurityUtil.isPermitted("admin:masterdata:view");
    }
      
    public boolean isAutoAssignmentConfigPermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentconfig:view");
    }
    
    public boolean isAutoAssignmentServicePermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentservice:view");
    }
          
    public boolean isCaseTypePermitted() {
        return SecurityUtil.isPermitted("admin:casetype:view");
    }
    
    public boolean isCaseTopicPermitted() {
        return SecurityUtil.isPermitted("admin:casetopic:view") ;
    }
    
    public boolean isCaseDetailPermitted() {
        return SecurityUtil.isPermitted("admin:casedetail:view");
    }
    
    public boolean isCaseRequestPermitted() {
        return SecurityUtil.isPermitted("admin:caserequest:view");
    }
    
    public boolean isDocumentPermitted() {
        return SecurityUtil.isPermitted("admin:document:view");
    }
    
    public boolean isFollowupPermitted() {
        return SecurityUtil.isPermitted("admin:followup:view");
    }
  
   public boolean isActivityTypePermitted() {
        return SecurityUtil.isPermitted("admin:activitytype:view");
    }  
  
   public boolean isContactResultPermitted() {
        return SecurityUtil.isPermitted("admin:contactresult:view");
    }  
  
    public boolean isObjectionPermitted() {
        return SecurityUtil.isPermitted("admin:objection:view");
    }
        
    public boolean isWorkflowManagementPermitted() {
        return SecurityUtil.isPermitted("admin:workflow:view");
    }
    
    public boolean isWorkflowRulePermitted() {
        return SecurityUtil.isPermitted("admin:workflowrule:view");
    }
    
    public boolean isCaseWorkflowPermitted() {
        return SecurityUtil.isPermitted("admin:caseworkflow:view");
    }
    
    public boolean isUserManagementPermitted() {
        return SecurityUtil.isPermitted("admin:usermangement:view");
    }
    
    public boolean isUserGroupPermitted() {
        return SecurityUtil.isPermitted("admin:usergroup:view");
    }
    
    public boolean isUserPermitted() {
        return SecurityUtil.isPermitted("admin:user:view");
    }
    
    public boolean isSessionMonitorPermitted() {
        return SecurityUtil.isPermitted("admin:sessionmonitor:view");
    }
    
    public boolean isAuditLogPermitted() {
        return SecurityUtil.isPermitted("admin:auditlog:view");
    }
        
    public boolean isConfigurationPermitted() {
        return SecurityUtil.isPermitted("admin:configuration:view") ;
    }
    
    public boolean isFlexFieldPermitted() {
        return SecurityUtil.isPermitted("admin:flexfield:view");
    }
    
    public boolean isCustomerLayoutPermitted() {
        return SecurityUtil.isPermitted("admin:customerlayout:view");
    }
    
    public boolean isFileFormatPermitted() {
        return SecurityUtil.isPermitted("admin:fileformat:view");
    }
        
    public boolean isRegistrationPermitted() {
        return SecurityUtil.isPermitted("admin:registration:view");
    }
        
    public boolean isQuestionairePermitted() {
        return SecurityUtil.isPermitted("admin:questionnaire:view");
    }   
        
    public boolean isQuestionaireCategoryPermitted() {
        return SecurityUtil.isPermitted("admin:questionnairecategory:view");
    } 
        
    public boolean isCustomerHistoricalPermitted() {
        return SecurityUtil.isPermitted("admin:customerhistorical:view");
    } 
    
    public boolean isMediaPlanPermitted() {
        return SecurityUtil.isPermitted("admin:mediaplan:view");
    } 

    public boolean isCtiAdapterPermitted() {
        return SecurityUtil.isPermitted("admin:ctiadapter:view");
    } 
        
    public boolean isSequenceNumberPermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumber:view");
    } 
           
    public boolean isSystemConfigurationPermitted() {
        return SecurityUtil.isPermitted("admin:systemconfiguration:view");
    }   
        
    public boolean isBrandPermitted() {
        return SecurityUtil.isPermitted("admin:brand:view");
    }   
        
    public boolean isModelPermitted() {
        return SecurityUtil.isPermitted("admin:model:view");
    }   
        
    public boolean isModelTypePermitted() {
        return SecurityUtil.isPermitted("admin:modeltype:view");
    } 
        
    public boolean isApprovalRulePermitted() {
        return SecurityUtil.isPermitted("admin:approvalrule:view");
    }
        
    public boolean isPaymentMethodPermitted() {
        return SecurityUtil.isPermitted("admin:paymentmethod:view");
    }
    
    public boolean isDeliveryMethodPermitted() {
        return SecurityUtil.isPermitted("admin:deliverymethod:view");
    }
    
    public boolean isPhoneCategoryPermitted() {
        return SecurityUtil.isPermitted("admin:phonecategory:view");
    }
    
    public boolean isPhoneDirectoryPermitted() {
        return SecurityUtil.isPermitted("admin:phonedirectory:view");
    }
    
    public boolean isApprovalReasonPermitted() {
        return SecurityUtil.isPermitted("admin:approvalreason:view");
    }
    
    public boolean isOnlinePaymentLogPermitted() {
        return SecurityUtil.isPermitted("admin:onlinepaymentlog:view");
    }
    
    public boolean isServiceTypePermitted() {
        return SecurityUtil.isPermitted("admin:servicetype:view");
    }
    
    public boolean isBusinessUnitPermitted() {
        return SecurityUtil.isPermitted("admin:businessunit:view");
    }
    
    public boolean isLocationPermitted() {
        return SecurityUtil.isPermitted("admin:location:view");
    }
    
    public boolean isTemplateCategoryPermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplateCategory:view");
    }
        
    public boolean isMessageTemplatePermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplate:view");
    }
    
    public boolean isBankPermitted() {
        return SecurityUtil.isPermitted("admin:bank:view");
    }

    public boolean isExportVoiceFilePasswordPermitted() {
        return SecurityUtil.isPermitted("admin:exportvoicefilepassword:view");
    }
    
    public boolean isCardExclusionPermitted() {
        return SecurityUtil.isPermitted("admin:cardexclusion:view") ;
    }
    
    public boolean isManageProvincePermitted() {
        return SecurityUtil.isPermitted("admin:manageprovince:view") ;
    }
    
    public boolean isCampaignChannelPermitted(){
        return SecurityUtil.isPermitted("admin:campaignchannel:view") ;
    }
    
    public boolean isMarketingAutoPermitted(){
        return SecurityUtil.isPermitted("admin:marketingauto:view") ;
    }
    
    public boolean isContactResultPlanPermitted() {
        return SecurityUtil.isPermitted("admin:contactresultplan:view") ;
    }

    public boolean isSequenceNumberFilePermitted() {
        return SecurityUtil.isPermitted("admin:sequencenumberfile:view");
    }
    
    public boolean isCompanyLogoPermitted(){
        return SecurityUtil.isPermitted("admin:companylogo:view");
    }

    public boolean isContactResultMappingPermitted() {
        return SecurityUtil.isPermitted("admin:contactresultmapping:view") ;
    }

}
