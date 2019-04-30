
        function createContactHistory(phoneNo, callCategory, trackId, uniqueId){
            if (phoneNo.length >= 8 && phoneNo.length <= 9) {
                phoneNo = '0' + phoneNo;
            }

            if (phoneNo.length === 7) {
                phoneNo = '02' + phoneNo;
            }
            var m = document.getElementById('mode');
            var p = document.getElementById('phoneNo');
            var c = document.getElementById('callCategory');
            var t = document.getElementById('trackId');
            var u = document.getElementById('uniqueId');
            m.value = "create";
            p.value = phoneNo;
            c.value = callCategory;
            t.value = trackId;
            u.value = uniqueId;
            console.log('ActionServlet : createContactHistory');
            //alert(p.value + ',' + c.value + ',' + t.value);
            //document.frmCreate.action = "../login/ActionServlet";
            document.frmCreate.submit();
        }

        function setLineStatus(status){
            //console.log("setLineStatus : " + status);
            var m = document.getElementById('mode');
            var s = document.getElementById('status');
            m.value = "status"
            s.value = status;
            document.frmCreate.submit();              
        }

        function setLineStatusTelephone(status){
            //console.log("setLineStatusTelephone : " + status);
            var m = document.getElementById('mode');
            var s = document.getElementById('status');
            m.value = "statusTelephone"
            s.value = status;
            document.frmCreate.submit();              
        }

        function setTalkTime(trackId, talkTime){
            /*if(talkTime === null || talkTime === ''){
                talkTime = parent.frames['telephonyframe'].document.frm.telephonyOCX.getTalkTime();
            }
            if(trackId === null || trackId === ''){
                trackId = parent.frames['telephonyframe'].document.frm.telephonyOCX.getTrackID_AutoRecording();
            }*/
            //console.log('Talk Time Servlet : ' + talkTime);
            var m = document.getElementById('mode');
            var t = document.getElementById('talkTime');
            var id = document.getElementById('trackId');
            m.value = "talkTime";
            id.value = trackId;
            t.value = talkTime;
            document.frmCreate.submit();          
        }
        
        function setShowContactSummary(){
            var m = document.getElementById('mode');
            m.value = "showContactSummary";
            document.frmCreate.submit();
        }

        function setDialNo(dialNo){
            var m = document.getElementById('mode');
            var d = document.getElementById('dialNo');
            m.value = "dialNo";
            d.value = dialNo;
            document.frmCreate.submit();          
        }

        function setInboundStatus(){
            var m = document.getElementById('mode');
            m.value = "inbound";
            document.frmCreate.submit();          
        }