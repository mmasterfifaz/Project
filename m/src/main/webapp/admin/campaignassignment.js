function checkSubmit() {
    var count=0;
    var str;
    var rdoAssignmentType = document.getElementsByName("frm:assignmentType");
    if(rdoAssignmentType.length === 0) {
        rdoAssignmentType = document.getElementsByName("frm:assignmentTypeAdmin");
    }
    
    for(i=0; i < rdoAssignmentType.length; i++){
        if(rdoAssignmentType[i].checked == true){
            found = true;
            str = rdoAssignmentType[i].value;
            break;
        }
    }
    
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            if (e.checked) {
                var thisAssigned = document.forms[0].elements[i+1];
                if((thisAssigned.type=='text') && str != 'average' && str != 'pooling' && ((thisAssigned.value=='') || (thisAssigned.value==0))){
                    alert('Please assign record to select User');
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
        return confirm('Confirm to assign?\n');
    } else {
        alert('Please select User');
        return false;
    }
}

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