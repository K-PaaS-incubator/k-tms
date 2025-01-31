/** 
 * 탐지정책 도움말 정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lCode: '',
			strDescription: '',
			sSeverity: '',
			strAttackType: '',
			strSummary: '',
			strhelpDescription: '',
			strFalsePositive: '',
			strSolution: '',
			strReference: '',
			strCveId: '',
			strbId: '',
			strVul: '',
			strNotVul : '',
			strAddrsPoof: '',
		}
	});
});