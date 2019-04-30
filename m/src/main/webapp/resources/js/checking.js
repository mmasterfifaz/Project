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
    return true;
}
