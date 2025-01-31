/** 
 * yara 룰 정책 그룹 등록정보
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var YaraRulePolicyModel = require('models/securityPolicy/yaraRulePolicyGroupModel');
	
	return Backbone.Collection.extend({
		model : YaraRulePolicyModel
	});
	
});