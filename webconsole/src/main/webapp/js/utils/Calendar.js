define(function(require) {

	var $ 			   = require('jquery'),
		datetimepicker = require('datetimepicker');

	return {
		setDefault: function(target, options) {
			$(target).datetimepicker({
				dayOfWeekStart: options.dayOfWeekStart || 1, // 시작 요일 지정 
				lang: options.lang || 'kr', // 언어 선택
				step: options.step || 1 // 시간 간격
				//closeOnWithoutClick: false	// 날짜 혹은 시간이 선택되지 않았을 경우 닫힘 제지 
			});
		},
// jquery-simple-datetime 캘린더로 변경 하였을 경우 사용 		
//		setDefault: function(target, options) {
//			$(target).appendDtpicker({
//				locale: options.locale || 'ko', // 언어 선택
//				minuteInterval: options.step || 5, // 시간 간격
//				closeButton: true	// 닫기 버튼 
//			});
//		},
		setDatepicker: function(target) {
			$(target).datetimepicker({
				dayOfWeekStart: 1,
				lang: 'kr',
				step: 1
			});
		},
		setDateTimepicker: function(target) {
			$(target).datetimepicker({
				dayOfWeekStart: 5,
				lang: 'kr',
				step: 5
			});
		},
		setReportDayPicker: function(target) {
			$(target).datetimepicker({
				dayOfWeekStart: 0,
				lang: 'kr'
			});
		},
		setReportWeekPicker: function(target) {
			$(target).datetimepicker({
				lang:'kr',
				timepicker:false,
				format:'Y-m-d',
				beforeShowDay: function(date){ return [date.getDay() == 1,""]}
			});
		},
		setReportMonthPicker: function(target) {
			$(target).monthPicker();
		},
		setDayPicker: function(target) {
			$(target).datetimepicker({
				dayOfWeekStart: 0,
				lang: 'kr',
				format:'Y-m-d',
                                timepicker: false,
			});
		},
		setMonthPicker: function(target){
			$(target).datetimepicker({
				dayOfWeekStart: 0,
				lang: 'kr',
				format:'Y-m'
			});
		},
		disable: function(target) {
			$(target).datetimepicker("destroy");
		},
        setMaxDate: function(target, options) {
			$(target).datetimepicker({
				dayOfWeekStart: options.dayOfWeekStart || 1, 	// 시작 요일 지정 
				lang: options.lang || 'kr', 					// 언어 선택
				step: options.step || 1, 						// 시간 간격
                maxDate : options.maxDate,
                timepicker: options.timepicker || false
			});
		}
	};
});
