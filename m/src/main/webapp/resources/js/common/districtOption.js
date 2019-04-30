function districtOption(seletedOption,changeOption){
     jQuery.ajax({
       type: "POST",
       url: "/MaxarCRM/DistrictSelectAjax",
       data: "provinceId="+seletedOption.val(),
       async: false,
       success: function(data){
           changeOption.removeAttr("disabled");
           changeOption.html(data);
           if(changeOption.children().length==1){
              changeOption.attr("disabled",true);
           }
       }
     });
}

