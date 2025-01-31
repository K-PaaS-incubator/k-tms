/** 
 * @author 
 * @since 
 * @description 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AuditLogModel = require('models/systemStatus/auditLogModel');
	
	return Backbone.Collection.extend({
		model : AuditLogModel
	});
	
});