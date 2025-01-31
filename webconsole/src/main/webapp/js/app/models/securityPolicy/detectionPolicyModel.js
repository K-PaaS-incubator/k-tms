/** 
 * 탐지정책 등록정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			rNum : 0, 
			lCode: '',
			sAlive: '',
			sClassType: '',
			strDescription: '',
			sSeverity: '',
			strSigRule: '',
			lUsed: '',
			lResponse:'131073',
			lThresholdTime: '',
			lThresholdNum: '',
			lvsensorIndex: '',
			strAttackType: '',
			strCveId:'',
			strbId:'',
			strVul: '',
			strAddrsPoof:'',
			totalRows : 0,
			lResponseValue: '131073',		// 패킷 저장			// unchecked일 경우 131073 checked는 131074
			lThresholdNumValue: '',			// 임계값 (개)
			lThresholdTimeValue: '',		// 임계값 (초)
			lUsedValue: '0',				// 사용					// unchecked일 경우 0 checked는 1
			changing: false,
			count: false,
			lResponseBool : false,
			lBlockBool: false,
			lBlock:'268566529',
			lBlockValue: '268566529',	// 차단사용, 로우데이터 미사용	// 초기값 지정
		},
		validate: function(attrs, options) {
			// 탐지정책 > 배포 대상 임계치 설정 유효성 검증 
			// 시스템 설정 > 가상센서 > 탐지정책 임계치 설정 유효성 검증  
			// 일괄배포 임계치 설정 유효성 검증 
			if (options.attribute == 'lThresholdNumValue') {
				this.set({
					lThresholdNumValue: attrs.lThresholdNumValue
				});
				var result = Backbone.Utils.validation.validateIsNull2(attrs.lThresholdNumValue);
				if (result == true) {
					return Backbone.Utils.validation.validateIsNumberRange(attrs.lThresholdNumValue);
				} else {
					return result;
				}
			}
			if (options.attribute == 'lThresholdTimeValue') {
				this.set({
					lThresholdTimeValue: attrs.lThresholdTimeValue
				});
				var result = Backbone.Utils.validation.validateIsNull2(attrs.lThresholdTimeValue);
				if (result == true) {
					return Backbone.Utils.validation.validateIsTimeRange(attrs.lThresholdTimeValue);
				} else {
					return result;
				}
			}
		}
	});
});