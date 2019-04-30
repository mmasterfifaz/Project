
            
function create(PhoneNo, CallCategory, TrackID){
    var uniqueId = parent.frames['telephonyframe'].document.frm.telephonyOCX.getAspectParameter18();
    if (typeof parent.frames['actionframe'].createContactHistory === 'function') {
        
        if (PhoneNo.length >= 8 && PhoneNo.length <= 9) {
            PhoneNo = '0' + PhoneNo;
        }

        if (PhoneNo.length === 7) {
            PhoneNo = '02' + PhoneNo;
        }
        //WriteToFile("Phone No. : " + PhoneNo + ", " + "Track ID : " + TrackID);
        console.log(PhoneNo);
        parent.frames['actionframe'].createContactHistory(PhoneNo, CallCategory, TrackID, uniqueId);
    }
}
            
function buttonChange(state){
    if (typeof parent.frames['mainframe'].buttonDialChange === 'function') {
        //var status = parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus().toUpperCase();
        parent.frames['mainframe'].buttonDialChange(state);
    }                
}
            
function toggleHide(){
    parent.frames['mainframe'].document.getElementById('frm:tgSub').click();
}
            
function setLineStatus(status){ 
    
    if (typeof parent.frames['actionframe'].setLineStatus === 'function') {
        //alert(status);
        parent.frames['actionframe'].setLineStatus(status);
    }
}
            
function onRingingAction(ANI, serviceName, dnis, callCategory, callType){
    var callCount, uniqueId;
    if (ANI.length >= 8 && ANI.length <= 9) {
        ANI = '0'+ANI;
    }

    if (dnis.length === 7) {
        dnis = '02'+dnis;
    }
    //alert(dnis);
    if(callCategory.toUpperCase() === 'INBOUND'){
        if (typeof parent.frames['actionframe'].setInboundStatus === 'function') {
            parent.frames['actionframe'].setInboundStatus();
        }
    }
    callCount =  parent.frames['telephonyframe'].document.frm.telephonyOCX.getAspectParameter19();
    if(callCount === '') {
        callCount = '1';
    }
    uniqueId = parent.frames['telephonyframe'].document.frm.telephonyOCX.getAspectParameter18();
    parent.frames['mainframe'].callAlert(ANI, serviceName, dnis, callCount, uniqueId, callCategory);
   //if(serviceName.toUpperCase() == 'INBOUNDSERVICE') {
        //window.open('#{request.contextPath}/share/phonedirectorytelephone.jsf','phoneDirectory','width=1250,height=550,left=30,top=80,scrollbars=1,resizable=1');
        //return false;
    //}
}
            
function showContactSummary(){
    if (typeof parent.frames['actionframe'].setShowContactSummary === 'function') {
        parent.frames['actionframe'].setShowContactSummary();
    }
}
            
function setTalkTime() {
    var trackId = document.frm.telephonyOCX.getTrackID_AutoRecording();
    var talkTime = document.frm.telephonyOCX.getTalkTime();
    if (typeof parent.frames['actionframe'].setTalkTime === 'function') {
        console.log('Talk Time = ' + talkTime);
        console.log('Track ID = ' + trackId);
        parent.frames['actionframe'].setTalkTime(trackId, talkTime);
    }                
}
            
function setDialNo(dialNo) {
    if (typeof parent.frames['actionframe'].setDialNo === 'function') {
        parent.frames['actionframe'].setDialNo(dialNo);
    }                
}
            
function checkLineStatus() {
    return document.frm.telephonyOCX.getLineStatus();         
}
            
function callPhoneDial(no) {
    document.frm.telephonyOCX.PhoneDial(no);   
    document.frm.telephonyOCX.AgentNotReady();
}
            
function callPhoneTranferWay(no) {
    document.frm.telephonyOCX.PhoneTransfer(no);       
    document.frm.telephonyOCX.AgentNotReady();
    //alert(document.frm.telephonyOCX.getLineStatus());
}
            
function callExt(extNo) {
    document.frm.telephonyOCX.PhoneDTMF(extNo);
}
            
            //function callPhoneTransferService(no, serviceName) {     //blind transfer
function callPhoneTransferService(no, callCount) {     //blind transfer  
    //document.frm.telephonyOCX.setAspectParameter18('test');
    if(document.frm.telephonyOCX.getAspectParameter19 === '') {
        document.frm.telephonyOCX.setAspectParameter19(1);
    } else {
        document.frm.telephonyOCX.setAspectParameter19(callCount);
    }
    document.frm.telephonyOCX.setAspectParameter20(no);
    document.frm.telephonyOCX.PhoneTransferService("#{global.transferToService}"); 
    document.frm.telephonyOCX.AgentNotReady();
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
            
function callTransferComplete() {
    document.frm.telephonyOCX.PhoneTransferComplete(); 
    setTalkTime();
    //document.frm.telephonyOCX.Aspect_SaveDisposition('MX_MOVE');
    document.frm.telephonyOCX.Aspect_SaveDisposition("#{global.transferCompleteDisposition}");
    //parent.frames['telephonyframe'].document.frm.telephonyOCX.AgentReady();
}

function callTransferCancel() {
    document.frm.telephonyOCX.PhoneCancelTransfer();         
}

function callBlindComplete() {
    setTalkTime();
    //document.frm.telephonyOCX.Aspect_SaveDisposition('MX_MOVE');
    document.frm.telephonyOCX.Aspect_SaveDisposition("#{global.transferCompleteDisposition}");
    //parent.frames['telephonyframe'].document.frm.telephonyOCX.AgentReady();
}

function initLineStatus() {
    var lineStatus = parent.frames['telephonyframe'].document.frm.telephonyOCX.getLineStatus();    
    setLineStatus(lineStatus)
}

function checkCategory() {
    return document.frm.telephonyOCX.getCallCategory();
}

function WriteToFile(str) {
    //C:\Windows\Microsoft.NET\Framework\v2.0.50727\caspol.exe -m -ag Trusted_Zone -url http://192.168.8.125:8080/* FullTrust
    try {
        var fso, s;
        fso = new ActiveXObject("Scripting.FileSystemObject");
        var d = new Date();
        var day = d.getDate();
        var m = d.getMonth() + 1;
        var y = d.getFullYear();
        var strDate = y + '_' + m + '_' + day;
        //var logFile = "C:\\" + strDate + "_save_dis.txt";
        var logFile = "C:\\" + strDate + "_log.txt";

        s = fso.OpenTextFile(logFile , 8, 1, -2);
        s.WriteLine(d + ' : ' + str);
        s.Close();
    }catch(err){
            var strErr = 'Error:';
            //strErr += '\nID:' + id;
            document.write(strErr);
    }
}