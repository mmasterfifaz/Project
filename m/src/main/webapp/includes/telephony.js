var callalertTelno='default'; //global variable
var isManualCall = '';
var serviceNameTxt = '';
var dnisTxt = '';
var callCountTxt = '';
var uniqueIdTxt = '';
var callCategoryTxt='';

function callAlertSetInfo(telno, manualCall, serviceName) { 
    callalertTelno = telno;
    isManualCall =  manualCall;    
    serviceNameTxt = serviceName;
}

function callAlertSetData() {
    var telno = document.getElementById('callAlertPopupForm:callAlertTelephoneNo');
    var manualCall = document.getElementById('callAlertPopupForm:manualCall');
    var serviceName = document.getElementById('callAlertPopupForm:serviceName');
    telno.value = callalertTelno;
    manualCall.value = isManualCall;
    serviceName.value = serviceNameTxt;
    
    document.getElementById('callAlertPopupForm:callAlertRefreshButton').click();
}

function loopDelay(numberMillis) { 
    var now = new Date(); 
    var exitTime = now.getTime() + numberMillis; 
    while (true) { 
        now = new Date(); 
        if (now.getTime() > exitTime) 
        return false; 
    } 
}
