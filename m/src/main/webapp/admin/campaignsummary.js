function checkCancle(frm) {
    var found = false;
    for(i=0; i<frm.elements.length; i++){
        if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true){
            found = true;
            break;
        }
    }
    if (!found) {
        alert('Please select user');
        return false;
    }
    if (!confirm('Confirm to cancel?')) {
        return false;
    }
    return true;
}