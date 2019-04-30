function checkAllFollowup() {
    var checked = false;
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if (e.id=='frm:followupallbox') 
            checked = e.checked;
        if ((e.id != 'frm:followupallbox')&&(e.type=='checkbox')&&(e.id.search('followupbox')>0)) {
            e.checked = checked;
        }
    }
}

function checkAllOpen() {
    var checked = false;
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if (e.id=='frm:openallbox') 
            checked = e.checked;
        if ((e.id != 'frm:openallbox')&&(e.type=='checkbox')&&(e.id.search('openbox')>0)) {
            e.checked = checked;
        }
    }
}

function checkAllUser() {
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.id != 'userallbox')&&(e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            e.checked = document.forms[0].userallbox.checked;
        }
    }
}

function checkSubmit() {
    var count=0;
    
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            if (e.checked) {
                var thisAssigned = document.forms[0].elements[i+1];
                if((thisAssigned.type=='text') && ((thisAssigned.value=='') || (thisAssigned.value==0))){
                    alert('Please assign record to selected User');
                    thisAssigned.focus();
                    return false;
                }
                else {
                    count++;
                }
            }
        }
    }
    
    if (count>0) {
        return confirm('This process will removed customer list from this campaign.\nConfirm to un-assign?\n');
    } else {
        alert('Please select User');
        return false;
    }
}

/*
function checkSubmit() {
    var count=0;
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            if (e.checked)
                count++;
        }
    }
    if (count>0) {
        return confirm('This process will removed customer list from this campaign.\nConfirm to un-assign?\n');
    } else {
        alert('Please select User');
        return false;
    }
}*/
