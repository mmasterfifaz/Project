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
        return confirm('Confirm to Transfer?\n');
    } else {
        alert('Please select User');
        return false;
    }
}

function checkUserAll(){
    for (var i=0; i<document.forms[0].elements.length; i++) {
        var e=document.forms[0].elements[i];
        if ((e.type=='checkbox')&&(e.id.search('userbox')>0)) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}
