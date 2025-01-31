/**
 * search utils view
 * dependency : analysisDate.js 
 */
define(function(require) {
	
	//"use strict";
	
	// require library
	var $ = require('jquery'),
		analysisDate = require('utils/analysisDate');
	
	return {
		/**
		 * form에 value를 json타입으로 가져오는 함수를 호출한다. jquery.serialize-object.js lib를 사용 
		 * param[0] : form tag id(class)
		 * param[n] : extend param 
		 */
		getData: function(form) {
			var params = $(form).serializeObject();
			
			// checkbox value가 false 일 경우 처리하는 로직.
			$(form).find("input[type=checkbox]:not(:checked)").each(function(i, v) {
				params[$(this).prop("name")] = false;
			});
			
			// 1번째 인자를 제거
			// 1번째 값은 form 식별자 이므로 extend될 필요가 없음.
			var args = Array.prototype.slice.call(arguments, 1);
			
			// apply : 함수호출 함수 
			// apply(context, arguments(type Array));
			params = $.extend.apply(null, [false, params].concat(args));
			
			return params;
		},
		/**
		 * dateType에 맞춰 시작기간과 종료기간의 데이터를 가져오는 함수를 호출한다.
		 * defaultTime : 5분
		 * param[0] : data
		 */
		getPeriod: function(interval) {
			try {
				var defaultInterval = 5*60;
				var intervalTime;			
				var dateType = interval.split(" ");
				
				switch(dateType[1]){
				case "m":
					intervalTime = dateType[0] * 60;
					break;
				case "h":
					intervalTime = dateType[0] * 60 * 60;
					break;
				case "d":
					intervalTime = dateType[0] * 24 * 60 * 60;
					break;
				default:
					intervalTime = defaultInterval;
					break;
				}
				
				var date = analysisDate.getAnalysisListTime(analysisDate.getAnalysisTimeInitialization(), intervalTime);
				return {
					startDateInput: date[0],
					endDateInput: date[1]
				};
			}catch(e) {
				return new Error(e);
			}
		},
		/**
		 * 검색 조건을 초기화 해주는 함수를 호출한다. 
		 */
		setData: function(form, value) {
			$(form).find("input[type=text]").filter("#ipInput, #maskInput, #attackNameInput, #destPortInput, #attackPortInput, #thresholdNumInput, #thresholdTimeInput, #strContent, #strOperator").val("");
			//TODO reset을 typeofError 뱉는 이유
			//$(form).find("input[type=text]").reset();
			//$(form).find("input[type=text]").val("");
//			$(form).find("input[type=text]").each(function() {
//				$(this).reset();
//			});
			$(form).find('select').each(function() {
				$(this).find('option:first').attr('selected','true');
				$('.thresholdNumInput', this.el).attr('readonly', 'true');
				if(value == "direction") {
					$('#simpleTimeSelect option:eq(1)').attr('selected', 'selected');
				}else {
					$('.simpleTimeSelect option:eq(4)').attr('selected', 'selected');
				}
			});
			$(form).find("input[type=checkbox]:checked").each(function() {
				$(this).attr('checked', false);
				//var chk = $(this).is(":checked");
				//if(chk) $(form).attr('checked', false);
			});
		},
		setSelectData: function(form) {
			$(form).find('select').each(function() {
				$(".sessionServiceSelect option:eq(0)").attr('selected', 'selected');
				$(".sessionDirection option:eq(0)").attr('selected', 'selected');
				$('#winBoundSelect option:eq(0)').attr('selected', 'selected');
				$('.simpleTimeSelect option:eq(1)').attr('selected', 'selected');
			});
			$(form).find("input[type=text]").filter("#sessionInput").val("");
		}
	};
});