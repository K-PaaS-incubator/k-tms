/** 
 *
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lAuditSetIndex: '',
			lType1: '',
			lType2: '',
			strContent: '',
			lWarningIndex: '',
			strSmsContent: '',
			nApply: '',
			
			lWarningSetIndex:'',
			strAlarmType:'',
			nCount:'',
			nSecond:'',
			lMailGroup:0,
			lSmsGroup:0,
			nType:'',
			nAlarmType:0,
			
			secondType1:'', //프로그램알림 checked or ''
			secondType2:'', //긴급알람 checked or ''
			secondType3:'', //가시가청알람 checked or ''
			secondType4:'', //sms전송 checked or ''
			secondType5:'', //mail전송 checked or ''
			nApplyType: '', // 사용여부 checked or '' 
			
			mailType:'',
			smsType:'',
			mailType0 : '', // selected 될 메일전송의 option0 ~ option2
			mailType1 : '', 
			mailType2 : '', 
			smsType0 : '', // selected 될 sms전송의 option0 ~ option2
			smsType1 : '',
			smsType2 : '', 
			completed: false,
			tabType: '', // tab의 종류(행위, 오류, 경고)에 따라 리스트에 표현할 아이콘
			changeData: false
			
		},
		edit: function(){
			
			this.set({
				editing: true
			});
		}
	});
});