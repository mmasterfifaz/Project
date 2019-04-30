function setEmoTest1(){
    var emotion = document.getElementById("frm:emotion");
    var emotionTxt = document.getElementById("emotionTxt");

    alert(emotion.value);
    alert(emotionTxt.value);
    emotion.value = emotionTxt.value;
    alert(emotion.value);
}

function setEmoTest2(){
    var emotion = document.getElementById("frm:emotion");
    var emotionRd = document.getElementsByName("emotionFrm");
//    var emotionFrm = frm.emotionFrm;  //no errors

    alert(emotionRd.length);
    alert(emotionRd[0].value);
    alert(emotionRd[1].value);
    alert(emotionRd[2].value);
    alert(emotionRd.value);
    emotion.value = emotionRd.value;
    alert(emotion.value);
}

function setEmotion(){
    var emotion = document.getElementById("frm:emotion");

    var radioArray = document.getElementsByName("emotionFrm");
    var i;
    var value;
    for (i=0; i<radioArray.length; i++){
        if (radioArray[i].checked){
            value = radioArray[i].value;
            break;
        }
    }
    emotion.value = value;
    alert(emotion.value);
}

function setControllerRadioObject(pControllerObjId, pWebObjName){
//    alert(pControllerObjId);
//    alert(pWebObjName);

    var controllerObj = document.getElementById(pControllerObjId);
    var radioArray = document.getElementsByName(pWebObjName);
    var i;
    var value;
    for (i=0; i<radioArray.length; i++){
        if (radioArray[i].checked){
            value = radioArray[i].value;
            break;
        }
    }
//    alert(value);
    controllerObj.value = value;
//    alert(controllerObj.value);
}

function setWebRadioObject(pControllerObjId, pWebObjName){
    var controllerObj = document.getElementById(pControllerObjId);
    var radioArray = document.getElementsByName(pWebObjName);
    var i;
    var value = controllerObj.value;
//    alert(value);
    for (i=0; i<radioArray.length; i++){
        if (value == radioArray[i].value){
            radioArray[i].checked = true;
            break;
        }
    }
}
