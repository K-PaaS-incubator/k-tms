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
			'nEventPort' : '',
			'strEmailServer' : '',
			'strDbtsn' : '',
			'strDbuid': '',
			'strDbpwd':'',
			'nRawEnable' :'',
			'nRawPeriodic': '',
			'n5MinEnable': '',
			'n5MinPeriodic': '',
			'nHourEnable': '',
			'nDayEnable':'',
			'nAuditEnable': '',
			'nAuditPeriodic':'',
			'nLogspaceEnable':'',
			'nLogspacePeriodic':'',
			'nTrafficspaceEnable':'',
			'nTrafficspacePeriodic':'',
			'nSessionspaceEnable':'',
			'nTabledropEnable':'',
			'nTabledropUsage':'',
			'nTrafficspacedropEnable':'',
			'nTrafficspacedropUsage':'',
			'nSessionspacedropEnable':'',
			'nSessionspacedropUsage':'',
			'nIndexdropEnable': '',
			'nIndexdropUsage': '',
			'nEmailTabledropEnable':'',
			'nEmailTableurgentdropEnable':'',
			'nSensorStatusStoreperiod': '',
			'nUserRuleEncrypt': '',
			'strManagerVersion':'',
			'nUpdateState':0,
			'tmUpdateState':'',
			'modDate': '',
			'nTrafficThreshold': 0,
			'nTrafficThresholdMax': 0,
			'nTrafficThresholdTime': 0,
			'nTrafficThresholdTimeMax': 0,
			
			// 변경이력 관리
			'changeNEventPort': false,				// 매니저연결설정 포트 변경결과 
			'changeStrEmailServer': false,			// 이메일서버 변경결과
			'changeStrAlertProgramPathName': false,	// 외부프로그램 설정 변경결과
			'changeNTrafficThreshold': false,		// 트래픽이상설정 변경결과
			'changeNTrafficThresholdMax': false,		// 트래픽이상설정 변경결과
			'changeTableMaintenancePeriod': false,	// 테이블유지기간 설정 변경결과
			'changeTableSpaceMaintenancePeriod': false, // 테이블스페이스 변경결과
            'managerSettingRtn':'', //저장 결과값
		}
	});
});