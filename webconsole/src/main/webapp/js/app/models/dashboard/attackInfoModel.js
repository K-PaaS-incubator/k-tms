define(function(require) {
	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		locale = require('i18n!nls/str');
	

	return Backbone.Model.extend({
		
		
		defaults : {
			'lCode': '',
			'strTitle': '',
			'strAttackType': '',
			'bSeverity': '',
			'strSummary': '',
			'strDescription': '',
			'strFalsePositive': '',
			'strSolution': '',
			'strReference': '',
			'strCveId': '',
			'strbId': '',
			'strVul': '',
			'strNotVul': '',
			'strAddrspoof': '',
			'strDetect': '',
			'signatureRule': '',
			'severityColor': '',
			'rNum': '',
			'cntInfo':'',
			'cntLow':'',
			'cntMed':'',
			'cntHigh':'',
			'sigHelpDescription':'',
			'strName':'',
			'nClassType':0
		}
	});
});