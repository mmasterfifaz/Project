function onStartAutoRecordingChange(RecordingType, PhoneNo, CallCategory, TrackID) {
    console.log('##### : onStartAutoRecordingChange : #' + RecordingType + "#");
    console.log('##### : PhoneNo : ' + PhoneNo);
    console.log('##### : CallCategory : ' + CallCategory);
    console.log('##### : TrackID : ' + TrackID);
    //var currentState = ApiGetCurrentState();
    var recordingTypeStr = "";
    var callCategoryStr = "";
    if (RecordingType === 1) {
        recordingTypeStr = "A";
    } else if (RecordingType === 0) {
        recordingTypeStr = "M";
    } else {
        recordingTypeStr = "A";
    }

    if (CallCategory === 1) {
        callCategoryStr = "inbound";
    } else if (CallCategory === 2) {
        callCategoryStr = "outbound";
    } else {
        callCategoryStr = "outbound";
    }
    //console.log('recordingTypeStr = ' + recordingTypeStr);
    if (recordingTypeStr === 'A') {
        if (typeof parent.document.getElementById('actionframe').contentWindow.createContactHistory === 'function') {
            console.log('onStartAutoRecordingChange 2 = ' + currentState);
            parent.document.getElementById('actionframe').contentWindow.createContactHistory(PhoneNo, callCategoryStr, TrackID, '');
        }
    }
}