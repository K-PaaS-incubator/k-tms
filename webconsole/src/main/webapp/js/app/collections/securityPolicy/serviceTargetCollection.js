/** 
 * 세션감시정책 > 서비스에 매핑된 데이터
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var ServiceTargetModel = require('models/securityPolicy/serviceTargetModel');
	
	return Backbone.Collection.extend({
		model : ServiceTargetModel
	});
	
});