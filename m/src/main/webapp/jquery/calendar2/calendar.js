        $.datepicker.regional['th'] ={
            changeMonth: true,
            changeYear: true,
            //defaultDate: GetFxupdateDate(FxRateDateAndUpdate.d[0].Day),
            yearOffSet: 543,
            showOn: "button",
            buttonImage: '../../jquery/calendar2/calendar.gif',
            buttonImageOnly: true,
            dateFormat: 'dd M yy',
            dayNames: ['อาทิตย์', 'จันทร์', 'อังคาร', 'พุธ', 'พฤหัสบดี', 'ศุกร์', 'เสาร์'],
            dayNamesMin: ['อา', 'จ', 'อ', 'พ', 'พฤ', 'ศ', 'ส'],
            monthNames: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม'],
            monthNamesShort: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.'],
            constrainInput: true,

            prevText: 'ก่อนหน้า',
            nextText: 'ถัดไป',
            yearRange: '-100:+0',
            buttonText: 'เลือก'

        };
        $.datepicker.setDefaults($.datepicker.regional['th']);

        var Holidays;

        //On Selected Date
        //Have Check Date
        function CheckDate(date) {
            var day = date.getDate();
            var selectable = true;//ระบุว่าสามารถเลือกวันที่ได้หรือไม่ True = ได้ False = ไม่ได้
            var CssClass = '';

            return [selectable, CssClass, ''];
        }


        //=====================================================================================================
        //On Selected Date
        function SelectedDate(dateText, inst) {
            //inst.selectedMonth = Index of mounth
            //(inst.selectedMonth+1)  = Current Mounth
            var DateText = inst.selectedDay + '/' + (inst.selectedMonth + 1) + '/' + inst.selectedYear;
            //CallGetUpdateInMonth(ReFxupdateDate(dateText));
            //CallGetUpdateInMonth(DateText);
            return [dateText, inst]
        }
        //=====================================================================================================
        //Call Date in month on click image
        function OnBeforShow(input, inst) {
            var month = inst.currentMonth + 1;
            var year = inst.currentYear;
            //currentDay: 10
            //currentMonth: 6
            //currentYear: 2012
            GetDaysShows(month, year);

        }
        //=====================================================================================================
        //On Selected Date
        //On Change Drop Down
        function ChangMonthAndYear(year, month, inst) {

            GetDaysShows(month, year);
        }

        //=====================================
        function GetDaysShows(month, year) {
            Holidays = [1,4,6,11]; // Sample Data
        }
        //=====================================

