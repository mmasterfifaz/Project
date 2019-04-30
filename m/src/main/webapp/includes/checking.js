function checkDelete(frm) {
    var found = false;
    for(i=0; i<frm.elements.length; i++){
        if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true){
            found = true;
            break;
        }
    }
    if (!found) {
        alert('Please select data');
        return false;
    }
    if (!confirm('Confirm to delete?')) {
        return false;
    }
    return true;
}

function checkSelectData(frm) {
    var found = false;
    for(i=0; i<frm.elements.length; i++){
        if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true){
            found = true;
            break;
        }
    }
    if (!found) {
        alert('Please select data');
        return false;
    }
    
    return true;
}


function isCurrencyKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46 )
        return false;
    return true;
}
function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}
function isNumberCommaKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        if(charCode == 44){
            return true;
        }else { return false; }
    return true;
}

function isNumberFloatKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode !== 46)
        return false;

    return true;
}

function checkAll(){
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.name != 'allbox')&&(e.type=='checkbox')) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}

function checkAllToDelete(){
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.name !== 'allbox') && (e.name !== 'frm:routingOption') && (e.type === 'checkbox')) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}

function checkAllToDelete2(){
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.name !== 'allbox') && (e.name !== 'frm:routingOption') && (e.type === 'checkbox') && (!e.checked)) {
            document.forms[0].allbox.checked = false;
            break;
        }
        document.forms[0].allbox.checked = true;
    }
}

function mediaPlayer(id,voice) {
    var str = "../admin/mediaplayer.jsf?trackId=" + id + "&mediaPath=" + voice;
    //console.log(str);
    var width = 330;
    var height = 150;
    var left = parseInt((screen.availWidth / 2) - (width / 2));
    var top = parseInt((screen.availHeight / 2) - (height / 2));
    var windowFeatures = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
    window.open(str, "popMediaPlayer", windowFeatures);
    return false;
}

function popupMediaPlayer(id, dir){
    var str = "mediaplayer.jsf";
    mediaPlayer(id, dir, str);
}

function popupMediaPlayerFront(id, dir){
    var str = "../../admin/mediaplayer.jsf";
    mediaPlayer(id, dir, str);
}

function popupMediaPlayerFront1(id, dir){
    var str = "../admin/mediaplayer.jsf";
    mediaPlayer(id, dir, str);
}

function popupMediaDownload(id, dir) {
    //alert(999);
    var str1 = "../admin/mediadownload.jsf";
    var str = str1 + "?trackId=" + id + "&mediaPath=" + dir;
    var width = 330;
    var height = 150;
    var left = parseInt((screen.availWidth / 2) - (width / 2));
    var top = parseInt((screen.availHeight / 2) - (height / 2));
    var windowFeatures = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
    window.open(str, "popMediaDownload", windowFeatures);
    return false;
}

function checkUserAll(){
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
         if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}

function checkSelectChkbox() {
    var count=0;
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            if (e.checked)
                count++;
        }
    }
    if (count>0) {
        return true;
    } else {
        alert('Please select User');
        return false;
    }
}
            
function playSound(soundfile) {
    var str = "<embed src='" + soundfile + "' hidden='true' autostart='true' loop='false' />";
    document.getElementById("dummy").innerHTML = str;
}

function getSelectedUser(){
    var str = "";
    var frm = document.f2;

    for(var i=0; i<frm.elements.length; i++){
        
        if(frm.elements[i].type==="checkbox" && frm.elements[i].checked===true){
            //alert(frm.elements[i].value);
            str += frm.elements[i].value + ",";
        }
    }
    
    if(str !== ""){
        str = str.substring(0, str.length - 1);
    }
    
    setSelectedUsers(str);
//    alert(str);
    //document.getElementById("f2:selectedUserId").value = str;
    //alert(document.getElementById("f2:selectedUserId").value);
}

function checkAllEmail(source){
    checkboxes = document.getElementsByName('chk');
    for(var i=0, n=checkboxes.length;i<n;i++) {
        checkboxes[i].checked = source.checked;
    }
}

function getSelectedEmail(){
    var str = "";
    var frm = document.f2;

    for(var i=0; i<frm.elements.length; i++){
        if(frm.elements[i].type === "checkbox" && frm.elements[i].name !== "allbox" && frm.elements[i].checked === true){
            str += frm.elements[i].value + ",";
        }
    }
    
    if(str !== ""){
        str = str.substring(0, str.length - 1);
    }
    setSelectedEmails(str);
}

function openWindowDownload(trackId){
    var str1 = "mediadownload.jsf";
    var str = str1 + "?trackId=" + trackId;
    var width = 330;
    var height = 150;
    var left = parseInt((screen.availWidth/2) - (width/2));
    var top = parseInt((screen.availHeight/2) - (height/2));
    var windowFeatures = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
    window.open(str,"popMediaDownload",windowFeatures);
    return false;
}
