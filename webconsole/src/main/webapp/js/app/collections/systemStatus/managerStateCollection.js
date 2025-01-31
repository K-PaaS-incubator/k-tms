/** 
 * 매니저 상태 조회 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var ManagerStateModel = require('models/systemStatus/managerStateModel');
	
	return Backbone.Collection.extend({
		model : ManagerStateModel
	});
	
});