function showPopup(){

    var left = document.documentElement.clientWidth/2 - jQuery('.popup').width()/2;
    var top = 0;
    jQuery('.popup').css({'top':top,'left':left});
    jQuery('.popup').show();

    jQuery('#divOrthogonal').css("height","470px");
    jQuery('#divOrthogonal').show();
    jQuery('#divOrthogonal', window.parent.document).css("height", window.parent.document.documentElement.scrollHeight+"px");
    jQuery('#divOrthogonal', window.parent.document).show();
}

function hidePopup(){
    jQuery('.popup').hide();
    jQuery('#divOrthogonal').hide();
    jQuery('#divOrthogonal', window.parent.document).hide();
}

