/** 
 * 세션감시정책
 */
define(function(require) {
	
	"use strict";
	
	// require library
	var $ 							= require('jquery'),
		Backbone 					= require('backbone');
	
	var SessionMonitorPolicyModel 	= require('models/securityPolicy/sessionMonitorPolicyModel');
	
	return Backbone.Collection.extend({
		model : SessionMonitorPolicyModel,
		completed: function() {
			return this.where({completed: true});
		}
	});
	
});