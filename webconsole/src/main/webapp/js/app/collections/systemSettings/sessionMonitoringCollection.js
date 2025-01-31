/** 
 * 가상센서 > 세션 감시
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var SessionMonitoringModel = require('models/systemSettings/sessionMonitoringModel');
	
	return Backbone.Collection.extend({
		
		model : SessionMonitoringModel
	});
});