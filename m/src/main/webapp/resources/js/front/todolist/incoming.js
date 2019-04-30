function initial(){

    var currentRow;
    hidePopup();
    //jQuery('.swap').selectedimage({alt:"To Do List"});
    //jQuery('.swap').selectedimage({alt:"Incoming"});
    //jQuery('#navigate', window.parent.document).html(jQuery('#naviage_hide').html());

    jQuery('.case_id').click(function(){
       currentRow = jQuery(this);
        jQuery.ajax({
           type: "POST",
           url: "caseAcceptance.jsf",
           data: "selectedID="+jQuery(this).children('input').val(),
           async: false,
           success: function(data){
               
            jQuery('.popup').html(data);
            showPopup();
            jQuery('#close').click(function(){
                hidePopup();
            });

                jQuery('#accept').click(function(){
                   jQuery.ajax({
                       type: "POST",
                       url: "/MaxarCRM/CaseAcceptanceUpdateAjax",
                       data: "activityId="+jQuery('#activityId').val()
                           +"&remark="+jQuery('#remarkComment').val()
                           +"&receiveStatus=accepted",
                       async: false,
                       success : function(data){
                            
                            if(data=="Success"){
                                hidePopup();
                                currentRow.parent().parent().hide();
                            }else{
                                alert(data);
                            }
                       }
                    });

                });

                jQuery('#reject').click(function(){
                    jQuery.ajax({
                       type: "POST",
                       url: "/MaxarCRM/CaseAcceptanceUpdateAjax",
                       data: "activityId="+jQuery('#activityId').val()
                           +"&remark="+jQuery('#remarkComment').val()
                           +"&receiveStatus=reject",
                       async: false,
                       success : function(data){
                            if(data=="Success"){
                                hidePopup();
                                currentRow.parent().parent().hide();
                            }else{
                                alert(data);
                            }
                       }
                    });
                });

           }
         });
       
    });
}

