function initial(){
    jQuery('.swap').selectedimage({alt:"Customer"});
    jQuery('#navigate', window.parent.document).html(jQuery('#naviage_hide').html());

    if(jQuery('[id$=home_same]:checked').length){
        disableAddress("home");
    }

    if(jQuery('[id$=billing_same]:checked').length){
        disableAddress("billing");
    }

    if(jQuery('[id$=shipping_same]:checked').length){
        disableAddress("shipping");
    }

    function disableAddress(type){
        jQuery('#'+type+'_full_name').css("display","none");
        jQuery('#'+type+'_full_name_disable').removeClass("hidden");
        jQuery('#'+type+'_address_line1').css("display","none");
        jQuery('#'+type+'_address_line1_disable').removeClass("hidden");
        jQuery('#'+type+'_address_line2').css("display","none");
        jQuery('#'+type+'_address_line2_disable').removeClass("hidden");
        jQuery('#'+type+'_postal_code').css("display","none");
        jQuery('#'+type+'_postal_code_disable').removeClass("hidden");
        jQuery('[id$='+type+'District]').attr("disabled",true);
        jQuery('[id$='+type+'Province]').attr("disabled",true);
    }

    function enableAddress(type){
        jQuery('#'+type+'_full_name').css("display","");
        jQuery('#'+type+'_full_name_disable').addClass("hidden");
        jQuery('#'+type+'_address_line1').css("display","");
        jQuery('#'+type+'_address_line1_disable').addClass("hidden");
        jQuery('#'+type+'_address_line2').css("display","");
        jQuery('#'+type+'_address_line2_disable').addClass("hidden");
        jQuery('#'+type+'_postal_code').css("display","");
        jQuery('#'+type+'_postal_code_disable').addClass("hidden");
        jQuery('[id$='+type+'District]').removeAttr("disabled");
        jQuery('[id$='+type+'Province]').removeAttr("disabled");
    }

    jQuery('[id$=home_same]').click(function(){
        if(jQuery('[id$=home_same]:checked').length){
            disableAddress("home");
        }else{
            enableAddress("home");
        }
    });

    jQuery('[id$=billing_same]').click(function(){
        if(jQuery('[id$=billing_same]:checked').length){
            disableAddress("billing");
        }else{
            enableAddress("billing");
        }
    });

    jQuery('[id$=shipping_same]').click(function(){
        if(jQuery('[id$=shipping_same]:checked').length){
            disableAddress("shipping");
        }else{
            enableAddress("shipping");
        }
    });

    jQuery('[id$=currentProvince]').change(function(){
        districtOption(jQuery(this),jQuery('#currentDistrictId'));
    });

    jQuery('[id$=homeProvince]').change(function(){
        districtOption(jQuery(this),jQuery('#homeDistrictId'));
    });

    jQuery('[id$=billingProvince]').change(function(){
        districtOption(jQuery(this),jQuery('#billingDistrictId'));
    });

    jQuery('[id$=shippingProvince]').change(function(){
        districtOption(jQuery(this),jQuery('#shippingDistrictId'));
    });


}

