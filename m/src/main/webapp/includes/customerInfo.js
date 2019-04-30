function getIframeWindow(iframe_object) {
    var doc;

    if (iframe_object.contentWindow) {
      return iframe_object.contentWindow;
    }

    if (iframe_object.window) {
      return iframe_object.window;
    } 

    if (!doc){
        if(iframe_object.contentDocument) {
            doc = iframe_object.contentDocument;
        }
    } 

    if (!doc){
        if(iframe_object.document) {
            doc = iframe_object.document;
        }
    }

    if (doc){
        if(doc.defaultView) {
            return doc.defaultView;
        }
    }

    if (doc){
        if(doc.parentWindow) {
            return doc.parentWindow;
        }
    }

    return undefined;
}
function changeImage(ImageID,ImageFileName) {
    if(document.getElementById(ImageID) !== null){
        document.getElementById(ImageID).src = ImageFileName;
    }
}

function clickContactNo(frm) {
    var found = false;
    var str = "";
    var no = "";
    var noSub = "";
    var ext = "";
    var extSub = "";

    //var slPhone = document.getElementById("frm:slPhone");
    //slPhone.selectedIndex = 0;

    var contactNo = document.getElementById("frm:contactNo");
    var extNo = document.getElementById("frm:extNo");
        
    var callNo = document.getElementById("frm:callNo");
    var callExt = document.getElementById("frm:callExt");
        
    contactNo.value = "";
    extNo.value = "";
    contactNo.readOnly = true;
    extNo.readOnly = true;

    var rdoContactNos = document.getElementsByName("rdoContactNo");
    for(i=0; i < rdoContactNos.length; i++){
        if(rdoContactNos[i].checked == true){
            found = true;
            str = rdoContactNos[i].value;
            break;
        }
    }

    if(found){
        if(str == "mobile"){
            contactNo.value = document.getElementById("frm:mobileHide").value;
            extNo.value = "";
            callNo.value = document.getElementById("frm:mobileShow").value;
            callExt.value = "";
        }else if(str == "home"){
            contactNo.value = document.getElementById("frm:homeHide").value;
            extNo.value = document.getElementById("frm:homeExtShow").value;
            callNo.value = document.getElementById("frm:homeShow").value;
            callExt.value = document.getElementById("frm:homeExtShow").value;
        }else if(str == "office"){
            contactNo.value = document.getElementById("frm:officeHide").value;
            extNo.value = document.getElementById("frm:officeExtShow").value;
            callNo.value = document.getElementById("frm:officeShow").value;
            callExt.value = document.getElementById("frm:officeExtShow").value;
        }else if(str == "contact1"){
            contactNo.value = document.getElementById("frm:contact1Hide").value;
            extNo.value = document.getElementById("frm:contactExt1Show").value;
            callNo.value = document.getElementById("frm:contact1Show").value;
            callExt.value = document.getElementById("frm:contactExt1Show").value;
        }else if(str == "contact2"){
            contactNo.value = document.getElementById("frm:contact2Hide").value;
            extNo.value = document.getElementById("frm:contactExt2Show").value;
            callNo.value = document.getElementById("frm:contact2Show").value;
            callExt.value = document.getElementById("frm:contactExt2Show").value;
        }else if(str == "contact3"){
            contactNo.value = document.getElementById("frm:contact3Hide").value;
            extNo.value = document.getElementById("frm:contactExt3Show").value;
            callNo.value = document.getElementById("frm:contact3Show").value;
            callExt.value = document.getElementById("frm:contactExt3Show").value;
        }else if(str == "contact4"){
            contactNo.value = document.getElementById("frm:contact4Hide").value;
            extNo.value = document.getElementById("frm:contactExt4Show").value;
            callNo.value = document.getElementById("frm:contact4Show").value;
            callExt.value = document.getElementById("frm:contactExt4Show").value;
        }else if(str == "contact5"){
            contactNo.value = document.getElementById("frm:contact5Hide").value;
            extNo.value = document.getElementById("frm:contactExt5Show").value;
            callNo.value = document.getElementById("frm:contact5Show").value;
            callExt.value = document.getElementById("frm:contactExt5Show").value;
        }
        
    }
    //alert(callNo.value);


    /*
    if (found) {
        if(str.length != 0){
            if(str.indexOf(",") != -1){
                no = str.substr(0, str.indexOf(","));
                noSub = no != "" ? (no.substr(0, no.length - 4)) + "****" : "";
                ext = str.substr(str.indexOf(",")+1, str.length);
                if(ext.length >= 4){
                    extSub = (ext.substr(0, ext.length - 4)) + "****";
                }else if(ext.length == 0){
                    extSub = "";
                }else{
                    extSub = "****";
                }
            }else{
                no = str;
                noSub = (no.substr(0, no.length - 4)) + "****";
            }
        }

//        contactNo.value = noSub; //Substring ****
//        extNo.value = extSub; //Substring ****
        contactNo.value = no;
        extNo.value = ext;

        callNo.value = no;
        callExt.value = ext;

    }
    */
}

function defautNo(){
    var callNo = document.getElementById("frm:callNo");
    var callExt = document.getElementById("frm:callExt");
    var contactNo = document.getElementById("frm:contactNo");
    var extNo = document.getElementById("frm:extNo");
    //alert(callNo.value);
    var rdoContactNos = document.getElementsByName("rdoContactNo");
    //alert(rdoContactNos.length);
    //for(i=0; i < rdoContactNos.length; i++){
    
        if(rdoContactNos.length == 1){
            rdoContactNos[0].checked = true;
            if(contactNo.value == ''){
                contactNo.value = document.getElementById("frm:defaultPhoneNoHide").value;
                extNo.value = document.getElementById("frm:defaultPhoneNoExtHide").value;
                callNo.value = document.getElementById("frm:defaultPhoneNoShow").value;
                callExt.value = document.getElementById("frm:defaultPhoneNoExtShow").value;
                //alert(contactNo.value + ", " + extNo.value + ", " + callNo.value + ", " + callExt.value);
            }
            //alert(callNo.value);
            //break;
        }
    //}
}
//function refreshStatusTelephone() {
//    if (document.getElementById("frm:panelDial") !== null) {
//        var status = "";
//        if (parent.frames.telephonyframe !== null && parent.frames.telephonyframe !== undefined) {
//            status = parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus() !== null ? parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase() : ''; ;
//        }
//
//        if (typeof parent.frames['actionframe'].setLineStatus === 'function') {
//            parent.frames['actionframe'].setLineStatusTelephone(status);
//        }
//        if(status !== 'AUTHENTICATED' && status !== 'LOGGING IN' && status !== 'INACTIVE'){
//            buttonDialChange(status); 
//        }
//    }
//}

function setExtNo(){
    var extNo = document.getElementById("frm:extNo");
    var callExt = document.getElementById("frm:callExt");
    callExt.value = extNo.value;
    //alert(callNo.value);
    
}

function setPhoneNo(){
    var contactNo = document.getElementById("frm:contactNo");
    var callNo = document.getElementById("frm:callNo");
    callNo.value = contactNo.value;
    //alert(callNo.value);
    
}

function clickCall() {
    var callNo = document.getElementById("frm:callNo");
     if(callNo.value == '' || callNo.value.length < 9 || callNo.value.length > 10) {
        alert('Invalid Phone number.');
    } else {
//        document.getElementById("frm:clickCallH").value = "true";
//        document.getElementById("frm:showContactSummaryH").value = "true";
//        document.getElementById('frm:clickNextCallH').value = "false";

        var no = "";
        var ext = "";
        var str = "";

        var rdoContactNos = document.getElementsByName("rdoContactNo");
        for(i=0; i < rdoContactNos.length; i++){
            if(rdoContactNos[i].checked === true){
                found = true;
                str = rdoContactNos[i].value;
                break;
            }
        }
        var contactNo = document.getElementById("frm:contactNo");
        var extNo = document.getElementById("frm:extNo");        
        var callExt = document.getElementById("frm:callExt");   
        
        setTelNoB4NextCall(callNo.value);
    }

}

//function clickExt(){
//    var extNo = document.getElementById("frm:extNo");
//    //alert(extNo.value);
//    if (parent.frames.telephonyframe != null) {
//        if(extNo.value != ""){
//                if (parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase()=='ACTIVE') {
//                    //alert(extNo.value);
//                    parent.frames['telephonyframe'].document.frm.telephonyOCX.PhoneDTMF(extNo.value); //send extension no. to aspect
//        }
//
//        }else{
//                if (parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase()=='ACTIVE' ||
//                    parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase()=='WRAP' ||
//                    parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase()=='DIALING' ||
//                    parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase()=='HELD') {
//                    //goto Contact Summary
//                    //alert(extNo.value);
//                    //Richfaces.showModalPanel('contactSummaryPopup');
//    }
//}
//    }
//}

function clickInsert(){
    var contactNo = document.getElementById("frm:contactNo");
    var extNo = document.getElementById("frm:extNo");
    var callNo = document.getElementById("frm:callNo");
    var callExt = document.getElementById("frm:callExt");
    //var slPhone = document.getElementById("frm:slPhone");
    //slPhone.selectedIndex = 0;

    contactNo.value = "";
    contactNo.readOnly = false;
    extNo.value = "";
    extNo.readOnly = false;
    callNo.value = "";
    callExt.value = "";
}

function changeContactNo(){
    var no = "";
    var ext = "";
    var noSub = "";
    var extSub = "";
    var str = "";
    
    var rdoContactNos = document.getElementsByName("rdoContactNo");
    for(i=0; i < rdoContactNos.length; i++){
        rdoContactNos[i].checked = false;
    }

    var slPhone = document.getElementById("frm:slPhone");
    str = slPhone.value;


    if(str.length != 0){
        if(str.indexOf(",") != -1){
            no = str.substr(0, str.indexOf(","));
            noSub = no != "" ? (no.substr(0, no.length - 4)) + "****" : "";
            ext = str.substr(str.indexOf(",")+1, str.length);
            if(ext.length >= 4){
                extSub = (ext.substr(0, ext.length - 4)) + "****";
            }else if(ext.length == 0){
                extSub = "";
            }else{
                extSub = "****";
            }
        }else{
            no = str;
            noSub = (no.substr(0, no.length - 4)) + "****";
        }
    }

    var contactNo = document.getElementById("frm:contactNo");
    var extNo = document.getElementById("frm:extNo");
    var callNo = document.getElementById("frm:callNo");
    var callExt = document.getElementById("frm:callExt");

//    contactNo.value = noSub;
//    extNo.value = extSub;
    contactNo.value = no;
    extNo.value = ext;
    callNo.value = no;
    callExt.value = ext;
}
function buttonDialChange(lineStatus) {
    if (document.getElementById("frm:panelDial") !== null) {
        console.log('005 : customerInfo.js : buttonDialChange : ' + lineStatus);
        var dd = document.getElementById("frm:dialStatusH");
        if (dd !== null) {
            dd.value = lineStatus !== null ? lineStatus : '';
        }
        //alert(document.getElementById('frm:dialStatusH').value);
        if (parent.frames.telephonyframe !== null) {
            //customer handling page
            if (document.getElementById("frm:dialBtn") !== null) {
                //alert(lineStatus);
                changeButton(lineStatus);
                //changeButton1();
            }
        }
    }
}
    
function validateEmail(em){
    var str = em.value;
    if(str != ""){
        var currentTagTokens = str.split(",");
        var txt = "";
        for ( var i = 0; i < currentTagTokens.length; i++ ) {
            txt = currentTagTokens[i].replace(/^\s+|\s+$/g, '') ;;
            if(!checkEmail(txt)){
                alert("Invalid E-mail Address! Please re-enter.");
                em.focus();
                return false;
                break;
            }
        }
    }
    return true;
}

function checkEmail(myForm) {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(myForm)){
        return (true);
    }
    //alert("Invalid E-mail Address! Please re-enter.")
    //myForm.focus();
    //myForm.select();
    return (false);
}

//function checkTelephonyTrackId(){
//    if (parent.frames.telephonyframe != null) {
//        if (typeof parent.frames['telephonyframe'].create == 'function') {
//            var status = parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase();
//            //alert(status);
//            if(status == 'ACTIVE' || status == 'WRAP' || status == 'DIALING') {                
//                var callCategory = parent.frames['telephonyframe'].document.frm.telephonyOCX.getCallCategory();
//                var trackId = parent.frames['telephonyframe'].document.frm.telephonyOCX.getTrackID_AutoRecording();
//                var phoneNo = parent.frames['telephonyframe'].document.frm.telephonyOCX.getPhoneNo();
//                //alert(trackId + " : " + phoneNo);
//                //alert(callCategory + ", " + trackId + ", " + phoneNo);                
//                parent.frames['telephonyframe'].create(phoneNo, callCategory, trackId);
//            }
//        }
//    }
//}

function saveTelephony(){
    var defaultNos = document.getElementsByName("defaultNo");
    var defaultNo = "";
    for(i=0; i < defaultNos.length; i++){
        if(defaultNos[i].checked){
            defaultNo = defaultNos[i].value;
            break;
        }
    }
    document.getElementById("frm:defaultTelephoneValue").value = defaultNo;
    //alert(defaultNo);
}

function initDefaultValue(value){
    var defaultNos = document.getElementsByName("defaultNo");
    var checked = false;
    for(i=0; i < defaultNos.length; i++){
        if(defaultNos[i].value == value){
            defaultNos[i].checked = true;
            checked = true;
            //alert(defaultNos[i].value);
            break;
        }
    }
    if(!checked){
        for(i=0; i < defaultNos.length; i++){
            if(i == 0){
                defaultNos[i].checked = true;
                break;
}
        }
    }
}

function showDefaultNo(){
    var defaultNos = document.getElementsByName("rdoContactNo");
    var no = "";

    for(i=0; i < defaultNos.length; i++){
        if(i == 0){
            defaultNos[i].checked = false;
            break;
        }
    }

    for(i=0; i < defaultNos.length; i++){
        if(defaultNos[i].checked){
            no = defaultNos[i].value;
            break;
        }
    }
    //alert(no);
    /*
    if(no != ""){
        var txtNo = "";
        if(no.indexOf(",") != -1){
            var txt = no.split(",");
            var txtNo = txt[0];
            document.getElementById('frm:contactNo').value = txtNo;
            document.getElementById('frm:extNo').value = txt[1];
            document.getElementById("frm:callNo").value = txt[0];
            document.getElementById("frm:callExt").value = txt[1];
        }else{
            txtNo = no;
            document.getElementById('frm:contactNo').value = txtNo;
            document.getElementById("frm:callNo").value = no;
            document.getElementById("frm:callExt").value = "";
        }        
    }
    */
}
