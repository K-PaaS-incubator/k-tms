define(function (require) {

    var $ = require('jquery');

    return {
        //현재 날짜 테이블명 형태로 가져오기
        getAnalysisDay: function (target, options) {
            var dateTime = this.getServerTime();
            var strDay = "_" + dateTime.getFullYear().toString().substr(2, 2) +
                    "_" + this.leadingZero((dateTime.getMonth() + 1), 2) +
                    "_" + this.leadingZero(dateTime.getDate(), 2);
            return strDay;
        },
        //현재 시간 통계 조회 시간 형태로 가져오기 (대시보드용)
        getAnalysisTime: function () {
            var dateTime = this.getServerTime();
            var tempTime = dateTime.getTime() - (5 * 60 * 1000); //통계데이터를 가져와야 하기 때문에 현재 시간과 5분정도 텀을 준다.
            tempTime = new Date(tempTime);
            var strDate = tempTime.getFullYear() +
                    "-" + this.leadingZero((tempTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempTime.getDate(), 2) +
                    " " + this.leadingZero(tempTime.getHours(), 2) +
                    ":" + this.leadingZero((tempTime.getMinutes() - (tempTime.getMinutes() % 5)), 2);
            return strDate;
        },
        // FIXME : 미사용 메소드 주석 처리
        //현재시간 기준 전일 분 설정
        // getYesterDateTime: function (timeSet) {
        //     timeSet = timeSet || "00:00";
        //     var today = new Date();
        //     var tempTime = today.getTime() - (24 * 60 * 60 * 1000); //통계데이터를 가져와야 하기 때문에 현재 시간과 5분정도 텀을 준다.
        //     //var tempTime = today.getTime();
        //     tempTime = new Date(tempTime);
        //
        //     var strDate = tempTime.getFullYear() +
        //             "-" + this.leadingZero((tempTime.getMonth() + 1), 2) +
        //             "-" + this.leadingZero(tempTime.getDate(), 2) +
        //             " " + timeSet;
        //     return strDate;
        // },
        // getDateTimeChange: function (valueDate, interval) {
        //     var tempToTime = new Date(this.convertStrToDate(valueDate));
        //     var tempFromTime = tempToTime.getTime() - (interval * 1000);
        //     tempFromTime = new Date(tempFromTime);
        //
        //     var strFromDate = tempFromTime.getFullYear() +
        //             "-" + this.leadingZero((tempFromTime.getMonth() + 1), 2) +
        //             "-" + this.leadingZero(tempFromTime.getDate(), 2) +
        //             " " + this.leadingZero(tempFromTime.getHours(), 2) +
        //             ":" + this.leadingZero(tempFromTime.getMinutes(), 2);
        //     return strFromDate;
        // },
        //현재 시간 통계 조회 시간 형태로 가져오기 (조회화면용- 내부에서 현재시간을 초기화. getAnalysisTime-대시보드용과 다름 )
        getAnalysisTimeInitialization: function (periodUnit) {// TODO : 동일 메소드
            // FIXME : refs #21722 - 시간 조회 시 서버 시간을 가져올 것(ajax)
            var dateTime = this.getServerTime();
            var strDate = dateTime.getFullYear() +
                    "-" + this.leadingZero((dateTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(dateTime.getDate(), 2) +
                    " " + this.leadingZero(dateTime.getHours(), 2) +
                    ":" + this.leadingZero(dateTime.getMinutes()/* - (tempTime.getMinutes() % periodUnit))*/, 2); // GS인증 요청 : 현재 시간으로 검색 시간 설정
            return strDate;
        },
        getChangeMinute: function(min) {// TODO : 동일 메소드
            var dateTime = this.getServerTime();
            var strDate = dateTime.getFullYear() +
                    "-" + this.leadingZero((dateTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(dateTime.getDate(), 2) +
                    " " + this.leadingZero(dateTime.getHours(), 2) +
                    ":" + this.leadingZero((dateTime.getMinutes() - min), 2);
            return strDate;
        },
        //현재 시간 통계 조회 시간 형태로 가져오기 (차트용) return 값은 시작 시간, 종료시간
        getAnalysisChartTime: function (tableDate, interval, periodUnit) {
            // var today = new Date();
            var dateTime = this.getServerTime();
            periodUnit = periodUnit || 1;
            var tempTime = dateTime.getTime() - (periodUnit * 60 * 1000); //통계데이터를 가져와야 하기 때문에 현재 시간과 5분정도 텀을 준다.
            //var tempTime = today.getTime();
            //차트 종료 시간 설정
            var tempToTime = new Date(tempTime);

            var strToDate = tempToTime.getFullYear() +
                    "-" + this.leadingZero((tempToTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempToTime.getDate(), 2) +
                    " " + this.leadingZero(tempToTime.getHours(), 2) +
                    ":" + this.leadingZero((tempToTime.getMinutes() - (tempToTime.getMinutes() % periodUnit)), 2);

            //차트 시작 시간 설정
            var tempFromTime = tempToTime.getTime() - (interval * 1000);
            tempFromTime = new Date(tempFromTime);

            var strFromDate = tempFromTime.getFullYear() +
                    "-" + this.leadingZero((tempFromTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempFromTime.getDate(), 2) +
                    " " + this.leadingZero(tempFromTime.getHours(), 2) +
                    ":" + this.leadingZero((tempFromTime.getMinutes() - (tempFromTime.getMinutes() % periodUnit)), 2);

            var chartDate = {};
            chartDate[0] = strFromDate;
            chartDate[1] = strToDate;

            return chartDate;
        },
        // 현재 시간 통계 조회 시간 형태로 가져오기 (분석화면 리스트용) return 값은 시작 시간, 종료시간
        getAnalysisListTime: function (listDate, interval, periodUnit) {// TODO : 동일 메소드
            periodUnit = periodUnit || 1;
            // 리스트 종료 시간 설정
            var tempToTime = new Date(this.convertStrToDate(listDate));
            var strToDate = tempToTime.getFullYear() +
                    "-" + this.leadingZero((tempToTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempToTime.getDate(), 2) +
                    " " + this.leadingZero(tempToTime.getHours(), 2) +
                    ":" + this.leadingZero(tempToTime.getMinutes()/* - (tempToTime.getMinutes() % periodUnit))*/, 2); // GS인증 요청 : 현재 시간으로 검색 시간 설정

            // 리스트 시작 시간 설정
            var tempFromTime2 = tempToTime.getTime() - (interval * 1000);
            var tempFromTime = new Date(tempFromTime2);
            var strFromDate = tempFromTime.getFullYear() +
                    "-" + this.leadingZero((tempFromTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempFromTime.getDate(), 2) +
                    " " + this.leadingZero(tempFromTime.getHours(), 2) +
                    ":" + this.leadingZero(tempFromTime.getMinutes()/* - (tempFromTime.getMinutes() % periodUnit))*/, 2); // GS인증 요청 : 현재 시간으로 검색 시간 설정

            var listDate = {};
            listDate[0] = strFromDate;
            listDate[1] = strToDate;
            return listDate;
        },
        getNotAnalysisListTime: function (strDate, interval, periodUnit) {// TODO : 동일 메소드
            periodUnit = periodUnit || 1;
            // 리스트 종료 시간 설정
            var tempToTime = new Date(this.convertStrToDate(strDate));
            var strToDate = tempToTime.getFullYear() +
                    "-" + this.leadingZero((tempToTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempToTime.getDate(), 2) +
                    " " + this.leadingZero(tempToTime.getHours(), 2) +
                    ":" + this.leadingZero(tempToTime.getMinutes(), 2);

            // 리스트 시작 시간 설정
            var tempFromTime2 = tempToTime.getTime() - (interval * 1000);
            var tempFromTime = new Date(tempFromTime2);
            var strFromDate = tempFromTime.getFullYear() +
                    "-" + this.leadingZero((tempFromTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(tempFromTime.getDate(), 2) +
                    " " + this.leadingZero(tempFromTime.getHours(), 2) +
                    ":" + this.leadingZero(tempFromTime.getMinutes(), 2);

            var listDate = {};
            listDate[0] = strFromDate;
            listDate[1] = strToDate;
            return listDate;
        },
        // FIXME : 미사용 메소드 주석 처리
        // getCurrentStrDateTime: function () {
        //     var tempToTime = new Date();
        //
        //     var strToDate = tempToTime.getFullYear() +
        //             "-" + this.leadingZero((tempToTime.getMonth() + 1), 2) +
        //             "-" + this.leadingZero(tempToTime.getDate(), 2) +
        //             " " + this.leadingZero(tempToTime.getHours(), 2) +
        //             ":" + this.leadingZero((tempToTime.getMinutes()), 2);
        //     return strToDate;
        // },
        // getCurrentStrDate: function () {
        //     var tempToTime = new Date();
        //
        //     var strToDate = tempToTime.getFullYear() +
        //             "-" + this.leadingZero((tempToTime.getMonth() + 1), 2) +
        //             "-" + this.leadingZero(tempToTime.getDate(), 2);
        //     return strToDate;
        // },
        //현재 시간 구하기
        getCurrentTime: function () {
            var today = new Date(); // FIXME : Date 객체 수정 여부 판단 필요
            var year, month, day, hour, minute;
            year = today.getFullYear().toString().substr(0, 4);
            month = this.leadingZero((today.getMonth() + 1), 2);
            day = this.leadingZero(today.getDate(), 2);
            hour = this.leadingZero(today.getHours(), 2);
            minute = this.leadingZero(today.getMinutes(), 2);

            return ({
                year: year,
                month: month,
                day: day,
                hour: hour,
                minute: minute
            });
        },
        // FIXME : 미사용 메소드 주석 처리
        //현재 시간 구하기
        // getCurrentTimeReportStr: function (type, gap, time) {
        //     var today = new Date();
        //     var year, month, day, hour, minute;
        //
        //     if (time != "") {
        //         today = new Date(this.convertStrToDate(time));
        //     }
        //
        //     if (type == "day") {
        //         today.setDate(today.getDate() + gap);
        //         year = today.getFullYear().toString().substr(0, 4);
        //         month = this.leadingZero((today.getMonth() + 1), 2);
        //         day = this.leadingZero(today.getDate(), 2);
        //         hour = this.leadingZero(today.getHours(), 2);
        //     } else if (type == "week") {
        //         today.setDate(today.getDate() + gap);
        //         year = today.getFullYear().toString().substr(0, 4);
        //         month = this.leadingZero((today.getMonth() + 1), 2);
        //         day = this.leadingZero(today.getDate(), 2);
        //     } else if (type == "month") {
        //         today.getMonth(today.getMonth() + gap);
        //
        //         year = today.getFullYear().toString().substr(0, 4);
        //         month = this.leadingZero((today.getMonth() + 1), 2);
        //         day = "01";
        //     } else {
        //         year = today.getFullYear().toString().substr(0, 4);
        //         month = this.leadingZero((today.getMonth() + 1), 2);
        //         day = this.leadingZero(today.getDate(), 2);
        //     }
        //     if (time != "") {
        //         if (type == "day") {
        //             return year + "-" + month + "-" + day + " " + hour;
        //         } else if (type == "week") {
        //             return year + "-" + month + "-" + day;
        //         } else {
        //             return year + "-" + month + "-" + day;
        //         }
        //     } else {
        //         return year + "-" + month + "-" + day;
        //     }
        //
        // },
        // getCurrentMonthPeriod: function (gap) {
        //     var today = new Date();
        //     today.setMonth(today.getMonth() + gap);
        //     var strToDate = today.getFullYear() +
        //             "-" + this.leadingZero((today.getMonth() + 1), 2);
        //     return strToDate;
        // },
        // getTodayDay: function () {
        //     var today = new Date();
        //     return today.getDay();
        // },
        // ie에서 date객체를 찾지 못하는 문제
        convertStrToDate: function (dateStr) {
            var a = dateStr.split(" ");
            var d = a[0].split("-");
            var t = a[1].split(":");

            try {
                return new Date(d[0], (d[1] - 1), d[2], t[0], t[1]);
            } catch (e) {
                return null;
            }
        },
        leadingZero: function (n, digits) {
            //1 -> 01 과 같이 변경 하기
            var zero = '';
            n = n.toString();

            if (n.length < digits) {
                for (i = 0; i < digits - n.length; i++) {
                    zero += '0';
                }
            }

            return zero + n;
        },
        // 간편구간조회에 필요한 interval을 구한다. 
        divideTimeToArray: function (startDateString, endDateString, arrSize) {
            var divide = arrSize || 6;

            var startDate = new Date(this.convertStrToDate(startDateString));
            var endDate = new Date(this.convertStrToDate(endDateString));
            var tempStartDate = startDate.getTime();
            var tempEndDate = endDate.getTime();

            // date type을 string으로 변환
            var dateToStartStr = startDate.toString();
            var dateToEndStr = endDate.toString();
            var getDiffTime = parseInt(tempEndDate) - parseInt(tempStartDate);
            var getTermTime = parseInt(getDiffTime) / divide;
            var tempData = [];
            var standardTime = null;

            for (var i = 0; i < divide; i++) {
                if (standardTime == null) {
                    //standardTime = tempStartDate;
                    standardTime = tempEndDate;
                } else {
                    //standardTime = standardTime + getTermTime; //이전 
                    standardTime = standardTime;
                }
                //var endTime = standardTime + getTermTime; //이전
                //var endTime = standardTime + (getTermTime*(i+1));
                var startTime = standardTime - (getTermTime * (i + 1));
                tempData[i] = {
//					"tmStart": this.timeToString(standardTime),
//					"tmEnd": this.timeToString(endTime)
                    "tmStart": this.timeToString(startTime),
                    "tmEnd": this.timeToString(standardTime)
                };

            }
            return tempData;

        },
        getDefaultTime: function (startDateString, endDateString) {

            var startDate = new Date(this.convertStrToDate(startDateString));
            var endDate = new Date(this.convertStrToDate(endDateString));

            var tempStartDate = startDate.getTime();
            var tempEndDate = endDate.getTime();

            var tempBeforeStartDate, tempBeforeEndDate;

            var getDiffTime = parseInt(tempEndDate) - parseInt(tempStartDate);

            if (getDiffTime < 3 * 24 * 60 * 60 * 1000) { //3일 미만
                tempStartDate = tempEndDate - (5 * 60 * 1000); //최종 5분

                tempBeforeStartDate = tempStartDate - (5 * 60 * 1000);
                tempBeforeEndDate = tempStartDate;
            } else if (getDiffTime < 15 * 24 * 60 * 60 * 1000) { //15일 미만
                tempStartDate = tempEndDate - (60 * 60 * 1000); //최종 1시간

                tempBeforeStartDate = tempStartDate - (60 * 60 * 1000);
                tempBeforeEndDate = tempStartDate;
            } else if (getDiffTime >= 15 * 24 * 60 * 60 * 1000) { //15일 이상
                tempStartDate = tempEndDate - (24 * 60 * 60 * 1000); //최종 1일

                tempBeforeStartDate = tempStartDate - (24 * 60 * 60 * 1000);
                tempBeforeEndDate = tempStartDate;
            } else {
                tempStartDate = tempEndDate - (5 * 60 * 1000); //최종 5분

                tempBeforeStartDate = tempStartDate - (5 * 60 * 1000);
                tempBeforeEndDate = tempStartDate;
            }

            var defaultDate = [];
            defaultDate[0] = this.timeToString(tempStartDate);
            defaultDate[1] = this.timeToString(tempEndDate);

            //변화량을 위한 이전 시간 구하기
            defaultDate[2] = this.timeToString(tempBeforeStartDate);
            defaultDate[3] = this.timeToString(tempBeforeEndDate);

            return defaultDate;
        },
        timeToString: function (time) {// TODO : 동일 메소드
            var dateTime = new Date(time);
            var strDate = dateTime.getFullYear() +
                    "-" + this.leadingZero((dateTime.getMonth() + 1), 2) +
                    "-" + this.leadingZero(dateTime.getDate(), 2) +
                    " " + this.leadingZero(dateTime.getHours(), 2) +
                    ":" + this.leadingZero(dateTime.getMinutes(), 2);

            return strDate;
        },
        // FIXME : 미사용 메소드 주석 처리
        // 조회 end Date 시간 기준 interval 만큼의 시간 가져오기 ( 조회 end 시간 - interval ) =  조회시작 시간
        // getAnalysisTimeToEndDate: function (endDateString, period) {
        //     var intervalTime = period * 60 * 60 * 1000;
        //     var endDate = new Date(this.convertStrToDate(endDateString));		// 날짜값을 문자로 컨버팅
        //     var tempEndDate = endDate.getTime();								// 문자인 날짜를 시간으로 컨버팅
        //     return this.timeToString(tempEndDate - intervalTime);				// 마지막 날짜시간 - interval 결과를 날짜 형태로 반환
        // },
        getServerTime : function() { // refs #21722 - 서버 시간 조회 후 Date 객체 return
            var serverTime;
            Backbone.ajax({
                async: false,
                method: 'POST',
                contentType: 'application/json',
                url: 'api/currentSystemTime',
                success: function (data) {
                    serverTime = new Date(data.date);
                }
            });
            return serverTime;
        }
    };
});
