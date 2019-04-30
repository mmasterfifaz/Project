function transferSetData() {
    var telno = document.getElementById('transferPanel:transferTelephoneNo');
    telno.value = callalertTelno;
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
//check state for dial or transfer
//if idle go to dial else active or hold go to transfer 3 way call popup
function callTransfer(transferType, transferNo , callCount) {
    var no = "", lineStatus;
    no = transferNo;
    transferNo.value = no;
    if (window.opener.parent.frames.telephonyframe != null) {
        lineStatus = window.opener.parent.frames['telephonyframe'].checkLineStatus();
        if (lineStatus.toUpperCase()=='IDLE') {
             if(transferNo == '') {
                alert('Please enter telephone number.');
            }
            window.opener.parent.frames['telephonyframe'].callPhoneDial(no);
        } else 
            if (lineStatus.toUpperCase()=='ACTIVE' || lineStatus.toUpperCase()=='HELD') {
                if(transferNo == '') {
                    alert('Please enter telephone number.');
                } else if(transferType == '') {
                    alert('Please select transfer type.');
                }

                if(transferType == '3way') {
                    window.opener.parent.frames['telephonyframe'].callPhoneTranferWay(no);
                } else  if (transferType == 'blind') {
                            window.opener.parent.frames['telephonyframe'].callPhoneTransferService(no, callCount);
                        }
            } else {
                
            }
    }
    loopDelay(1000);
    var callCat = window.opener.parent.frames['telephonyframe'].checkCategory();
    changeCallCategory(callCat);
    changeStatus();
}

function callExt(extNo) {
    if (window.opener.parent.frames.telephonyframe != null) {
        if(extNo.value != "") {
            lineStatus = window.opener.parent.frames['telephonyframe'].checkLineStatus();
            if (lineStatus.toUpperCase()=='ACTIVE') {
                window.opener.parent.frames['telephonyframe'].callExt(extNo);
            }
        } else {
            alert('Please enter extension number.');
        }
    }
}

function transferComplete() {
    window.opener.parent.frames['telephonyframe'].callTransferComplete();
    loopDelay(1000);
    var callCat = window.opener.parent.frames['telephonyframe'].checkCategory();
    changeCallCategory(callCat);
    changeStatus();
}
 
function transferCancel(reason){
    if(reason == '') {
        alert('Please select reason');
    } else {
       window.opener.parent.frames['telephonyframe'].callTransferCancel();
       loopDelay(1000);
       var callCat = window.opener.parent.frames['telephonyframe'].checkCategory();
       changeCallCategory(callCat);
       changeStatus();
    }
}
 
function blindComplete() {
    window.opener.parent.frames['telephonyframe'].callTransferComplete();
    loopDelay(1000);
    var callCat = window.opener.parent.frames['telephonyframe'].checkCategory();
    changeCallCategory(callCat);
    changeStatus();
}
