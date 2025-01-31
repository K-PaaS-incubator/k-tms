/** 
 * 사용자 정의 유형 콜렉션 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SignatureClassTypeModel = require('models/securityPolicy/signatureClassTypeModel');
	
	return Backbone.Collection.extend({
		model : SignatureClassTypeModel
	});
	
});