define(function(require) {
	
	"use strict";
	
	var $ 			= require('jquery'),
		_ 			= require('underscore'),
		Backbone 	= require('backbone'),
		locale 		= require('i18n!nls/str');
	
	var tpl 		= require('text!tpl/securityPolicy/auditLogPolicyListItem.html');
	
	return Backbone.View.extend({
		template: _.template(tpl),
		tagName: 'tr',
		initialize: function(options) {
			this.type 		= options.type; 	// 조회화면 또는 수정화면 (info, edit)
			this.tabType 	= options.tabType; 	// 행위, 오류, 경고 (action, error, warning)
			this.listenTo(this.model, 'change', this.render);
		},
		events: {
			'change .editApply'				: 'closeApply',
			'change .editMail'				: 'closeMail',
			'change .editSms'				: 'closeSms',
			'change .editEmergencyAlarm'	: 'closeEmergencyAlarm',
			'change .editAudibilityAlarm'	: 'closeAudibilityAlarm',
			'change .editProgramAlarm' 		: 'closeProgramAlarm'
		},
		render: function() {
			var tabType;
			
			this.model.set({
				tabType : 'log-'+this.tabType
			});
			this.$el.html(this.template({
				locale: locale,
				model: this.model.toJSON()
			}));
			
			this.$applyCheckBox 		= $('.editApply', this.el);
			this.$mailSelect			= $('.editMail', this.el);
			this.$smsSelect				= $('.editSms', this.el);
			this.$emergencyCheckBox 	= $('.editEmergencyAlarm', this.el);
			this.$audibilityCheckBox 	= $('.editAudibilityAlarm', this.el);
			this.$programCheckBox 		= $('.editProgramAlarm', this.el);
			
			return this;
		},
		toggleVisible: function(visible) {
			if (visible) {
				this.$el.addClass('editing');
			} else {
				this.$el.removeClass('editing');
			}
		},
		closeApply: function() {
			var value;
			var textValue;
			if(this.$applyCheckBox.prop('checked')) {
				value = 1;
				textValue = 'checked';
			} else {
				value = 0;
				textValue = '';
			}
			this.model.set({
				nApply : value,
				nApplyType : textValue,
				changeData : true
			});
		},
		closeMail: function() {
			var value = this.$mailSelect.val();
			var textValue;
			var mailType0, mailType1, mailType2;
			var nAlarmType = this.model.get('nAlarmType');
			
			if(value == 0) {
				textValue = locale.beNotInUse;
				mailType0 ='selected';
				mailType1 = '';
				mailType2 = '';
				nAlarmType = parseInt(nAlarmType) - parseInt(1);
			} else if(value == 1) {
				textValue = locale.admin;
				mailType0 ='';
				mailType1 = 'selected';
				mailType2 = '';
				// 변경하기전 lMailGroup 값이 0이였을때만 +1를 한다.
				// 변경전 값이 1또는 2 라면 이미 nAlarmType에 +1가 되어있기때문
				if(this.model.get('lMailGroup') == 0) {
					nAlarmType = parseInt(nAlarmType) + parseInt(1);
				}
			} else if(value == 2) {
				textValue = locale.user;
				mailType0 ='';
				mailType1 = '';
				mailType2 = 'selected';
				// 변경하기전 lMailGroup 값이 0이였을때만 +1를 한다.
				// 변경전 값이 1또는 2 라면 이미 nAlarmType에 +1가 되어있기때문
				if(this.model.get('lMailGroup') == 0) {
					nAlarmType = parseInt(nAlarmType) + parseInt(1);
				}
			}
			
			this.model.set({
				lMailGroup : value,
				mailType : textValue,
				mailType0 :mailType0,
				mailType1 : mailType1,
				mailType2 :mailType2,
				nAlarmType: nAlarmType,
				changeData : true
			});
		},
		closeSms: function() {
			var value = this.$smsSelect.val();
			var textValue;
			var smsType0, smsType1, smsType2;
			var nAlarmType= this.model.get('nAlarmType');
			
			if(value == 0) {
				textValue = locale.beNotInUse;
				smsType0 ='selected';
				smsType1 = '';
				smsType2 = '';
				nAlarmType = parseInt(nAlarmType) - parseInt(2);  
				
			} else if(value == 1) {
				textValue = locale.admin;
				smsType0 ='';
				smsType1 = 'selected';
				smsType2 = '';
				// 변경하기전 lSmsGroup 값이 0이였을때만 +2를 한다.
				// 변경전 값이 1또는 2 라면 이미 nAlarmType에 +2가 되어있기때문
				if(this.model.get('lSmsGroup') == 0) {
					nAlarmType = parseInt(nAlarmType) + parseInt(2);
				}
				
			} else if(value == 2) {
				textValue = locale.user;
				smsType0 = '';
				smsType1 = '';
				smsType2 = 'selected';
				// 변경하기전 lSmsGroup 값이 0이였을때만 +2를 한다.
				// 변경전 값이 1또는 2 라면 이미 nAlarmType에 +2가 되어있기때문
				if(this.model.get('lSmsGroup') == 0) {
					nAlarmType = parseInt(nAlarmType) + parseInt(2);
				}
			}

			this.model.set({
				lSmsGroup : value,
				smsType : textValue,
				smsType0 : smsType0,
				smsType1 : smsType1,
				smsType2 : smsType2,
				nAlarmType: nAlarmType,
				changeData : true
			});
		},
		closeEmergencyAlarm: function() {
			
			var value = this.$emergencyCheckBox.val();
			var textValue;
			var nAlarmType = this.model.get('nAlarmType');
			if(this.$emergencyCheckBox.prop('checked')) {
				textValue = 'checked';
				nAlarmType = parseInt(nAlarmType) + parseInt(value);
			}else {
				textValue = '';
				nAlarmType = parseInt(nAlarmType) - parseInt(value);
			}
			
			this.model.set({
				secondType3 : textValue,
				nAlarmType : nAlarmType,
				changeData : true
			});
		},
		closeAudibilityAlarm: function() {
			
			var value = this.$audibilityCheckBox.val();
			var textValue;
			var nAlarmType = this.model.get('nAlarmType');
			if(this.$audibilityCheckBox.prop('checked')) {
				textValue = 'checked';
				nAlarmType = parseInt(nAlarmType) + parseInt(value);
			} else {
				textValue = '';
				nAlarmType = parseInt(nAlarmType) - parseInt(value);
			}
			
			this.model.set({
				secondType2 : textValue,
				nAlarmType: nAlarmType,
				changeData : true
			});
		},
		closeProgramAlarm: function() {
			var value = this.$programCheckBox.val();
			var textValue;
			var nAlarmType = this.model.get('nAlarmType');
			if(this.$programCheckBox.prop('checked')) {
				textValue = 'checked';
				nAlarmType = parseInt(nAlarmType) + parseInt(value);
			} else {
				textValue = '';
				nAlarmType = parseInt(nAlarmType) - parseInt(value);
			}
			
			this.model.set({
				secondType1 : textValue,
				nAlarmType: nAlarmType,
				changeData : true
			});
		}
	});
});