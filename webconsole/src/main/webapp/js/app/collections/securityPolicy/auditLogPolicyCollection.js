/** 
 * 감사로그정책 행위/오류/경고
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AuditLogPolicyModel = require('models/securityPolicy/auditLogPolicyModel');
	
	return Backbone.Collection.extend({
		model : AuditLogPolicyModel
	});
	
});